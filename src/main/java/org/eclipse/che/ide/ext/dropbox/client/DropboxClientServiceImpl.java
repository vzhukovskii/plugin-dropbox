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
package org.eclipse.che.ide.ext.dropbox.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.eclipse.che.commons.lang.Strings;
import org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItem;
import org.eclipse.che.ide.ext.dropbox.shared.ListSpecifier;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.AsyncRequestLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxClientServiceImpl implements DropboxClientService {
    private static final String LIST_FOLDER = "/list";

    /** REST service context. */
    private final String              baseUrl;
    /** Loader to be displayed. */
    private final AsyncRequestLoader  loader;
    private final AsyncRequestFactory asyncRequestFactory;

    @Inject
    public DropboxClientServiceImpl(@Named("restContext") String baseUrl,
                                    AsyncRequestLoader loader,
                                    AsyncRequestFactory asyncRequestFactory) {
        this.baseUrl = baseUrl + "/dropbox";
        this.loader = loader;
        this.asyncRequestFactory = asyncRequestFactory;
    }

    @Override
    public void listDropboxFolder(@Nullable String path, @Nonnull ListSpecifier listSpecifier,
                                  @Nonnull AsyncRequestCallback<List<DbxItem>> callback) {
        String url = baseUrl + LIST_FOLDER;

        if (Strings.isNullOrEmpty(path)) {
            url = url + "?path=" + path;
        }

        asyncRequestFactory.createGetRequest(url).loader(loader).send(callback);
    }
}
