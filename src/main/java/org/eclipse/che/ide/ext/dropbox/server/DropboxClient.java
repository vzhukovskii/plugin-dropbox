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
import org.eclipse.che.api.auth.shared.dto.OAuthToken;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.UnauthorizedException;
import org.eclipse.che.api.project.server.FolderEntry;
import org.eclipse.che.commons.env.EnvironmentContext;
import org.eclipse.che.commons.user.User;
import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.ext.dropbox.server.oauth2.DropboxOAuthAuthenticator;
import org.eclipse.che.ide.ext.dropbox.shared.DropboxItemReference;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.eclipse.che.ide.ext.dropbox.shared.DropboxItemReference.ItemType.FILE;
import static org.eclipse.che.ide.ext.dropbox.shared.DropboxItemReference.ItemType.FOLDER;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxClient {
    private DropboxOAuthAuthenticator authenticator;

    @Inject
    public DropboxClient(DropboxOAuthAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    public List<DropboxItemReference> listDropboxFolder(@Nonnull String path) throws UnauthorizedException, ServerException {
        if (Strings.isNullOrEmpty(path)) {
            throw new ServerException("Path shouldn't be null");
        }

        DbxClient dbxClient = getClient();
        DbxEntry.WithChildren dbxMetadataWithChildren;

        try {
            dbxMetadataWithChildren = dbxClient.getMetadataWithChildren(path);
        } catch (DbxException e) {
            throw new ServerException(e.getMessage(), e);
        }

        List<DropboxItemReference> dropboxItems = new ArrayList<>();
        for (DbxEntry dbxEntry : dbxMetadataWithChildren.children) {
            DropboxItemReference dropboxItemReference = DtoFactory.getInstance().createDto(DropboxItemReference.class)
                    .withName(dbxEntry.name)
                    .withPath(dbxEntry.path)
                    .withType(dbxEntry.isFile() ? FILE : FOLDER);

            dropboxItems.add(dropboxItemReference);
        }

        return dropboxItems;
    }

    public void importDropboxFolder(@Nonnull String path, @Nonnull FolderEntry baseFolder) throws UnauthorizedException, ServerException, ConflictException, ForbiddenException {
        if (Strings.isNullOrEmpty(path)) {
            throw new ServerException("Path shouldn't be null");
        }

        DbxClient dbxClient = getClient();
        doDownload(dbxClient, path, baseFolder);
    }

    private void doDownload(DbxClient dbxClient, String path, FolderEntry baseFolder) throws ConflictException, ForbiddenException, ServerException {
        try {
            DbxEntry.WithChildren dbxEntries = dbxClient.getMetadataWithChildren(path);

            for (DbxEntry dbxEntry : dbxEntries.children) {
                if (dbxEntry.isFolder()) {
                    FolderEntry childFolder = baseFolder.createFolder(dbxEntry.name);
                    doDownload(dbxClient, dbxEntry.asFolder().path, childFolder);
                } else if (dbxEntry.isFile()) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        dbxClient.getFile(dbxEntry.asFile().path, dbxEntry.asFile().rev, outputStream);
                        baseFolder.createFile(dbxEntry.name, outputStream.toByteArray(), "application/octet-stream"); //need to find way to guess mime type from input stream
                    }
                }
            }
        } catch (DbxException | IOException e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    private DbxClient getClient() throws UnauthorizedException, ServerException {
        final User currentUser = EnvironmentContext.getCurrent().getUser();

        OAuthToken token;
        try {
            token = authenticator.getToken(currentUser.getId());
        } catch (IOException e) {
            if (e.getCause() != null && e.getCause() instanceof DbxException.InvalidAccessToken) {
                throw new UnauthorizedException("Authentication required");
            }
            throw new ServerException(e.getMessage(), e);
        }

        if (token == null || Strings.isNullOrEmpty(token.getToken())) {
            throw new UnauthorizedException("Authentication required");
        }

        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Codenvy/1.0", Locale.getDefault().toString());
        return new DbxClient(dbxRequestConfig, token.getToken());
    }
}
