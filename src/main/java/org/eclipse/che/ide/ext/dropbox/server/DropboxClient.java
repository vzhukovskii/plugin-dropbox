/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.dropbox.server;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.google.api.client.repackaged.com.google.common.base.Strings;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.UnauthorizedException;
import org.eclipse.che.api.project.server.FolderEntry;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.ext.dropbox.server.oauth2.token.TokenProvider;
import org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItem;
import org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItemList;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItem.ItemType.FILE;
import static org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItem.ItemType.FOLDER;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxClient {
    private TokenProvider tokenProvider;

    @Inject
    public DropboxClient(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public DbxItemList listDropboxFolder(@Nonnull String path) throws UnauthorizedException, ServerException {
        if (Strings.isNullOrEmpty(path)) {
            throw new ServerException("Path shouldn't be null");
        }

        DbxClient dbxClient = getDbxClient();
        DbxEntry.WithChildren dbxMetadataWithChildren;

        try {
            dbxMetadataWithChildren = dbxClient.getMetadataWithChildren(path);
        } catch (DbxException e) {
            throw new ServerException(e.getMessage(), e);
        }

        List<DbxItem> dropboxItems = new ArrayList<>();
        if (dbxMetadataWithChildren.children != null) {
            for (DbxEntry dbxEntry : dbxMetadataWithChildren.children) {
                DbxItem dbxItem = DtoFactory.getInstance().createDto(DbxItem.class)
                                            .withName(dbxEntry.name)
                                            .withPath(dbxEntry.path)
                                            .withType(dbxEntry.isFile() ? FILE : FOLDER);

                dropboxItems.add(dbxItem);
            }
        }

        return DtoFactory.getInstance().createDto(DbxItemList.class).withItemList(dropboxItems);
    }

    public void importDropboxFolder(@Nonnull String path, @Nonnull FolderEntry baseFolder)
            throws UnauthorizedException, ServerException, ConflictException, ForbiddenException {
        if (Strings.isNullOrEmpty(path)) {
            throw new ServerException("Path shouldn't be null");
        }

        DbxClient dbxClient = getDbxClient();
        doRecursiveDownload(dbxClient, path, baseFolder);
    }

    private void doRecursiveDownload(DbxClient dbxClient, String path, FolderEntry baseFolder)
            throws ConflictException, ForbiddenException, ServerException {
        try {
            DbxEntry.WithChildren dbxEntries = dbxClient.getMetadataWithChildren(path);

            for (DbxEntry dbxEntry : dbxEntries.children) {
                if (dbxEntry.isFolder()) {
                    FolderEntry childFolder = baseFolder.createFolder(dbxEntry.name);
                    doRecursiveDownload(dbxClient, dbxEntry.asFolder().path, childFolder);
                } else if (dbxEntry.isFile()) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        dbxClient.getFile(dbxEntry.asFile().path, dbxEntry.asFile().rev, outputStream);
                        baseFolder.createFile(dbxEntry.name, outputStream.toByteArray(), null);
                    }
                }
            }
        } catch (DbxException | IOException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    private DbxClient getDbxClient() throws UnauthorizedException, ServerException {
        String token;

        try {
            token = tokenProvider.getToken();
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                throw (UnauthorizedException)e;
            }
            throw new ServerException(e);
        }

        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Codenvy/1.0", Locale.getDefault().toString());
        return new DbxClient(dbxRequestConfig, token);
    }
}
