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

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

import org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItem;
import org.eclipse.che.ide.ext.dropbox.shared.ListSpecifier;
import org.eclipse.che.ide.rest.AsyncRequestCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
@ImplementedBy(DropboxClientServiceImpl.class)
public interface DropboxClientService {

    /**
     * List specified Dropbox folder.
     *
     * @param path
     *         path which should be listed. May be nullable
     * @param listSpecifier
     *         what we should fetch, e.g. files only, folders only or both
     * @param callback
     *         callback
     */
    void listDropboxFolder(@Nullable String path,
                           @Nonnull ListSpecifier listSpecifier,
                           @Nonnull AsyncRequestCallback<List<DbxItem>> callback);
}
