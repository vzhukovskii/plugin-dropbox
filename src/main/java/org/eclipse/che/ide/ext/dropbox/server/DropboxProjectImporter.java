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

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.UnauthorizedException;
import org.eclipse.che.api.core.util.LineConsumerFactory;
import org.eclipse.che.api.project.server.FolderEntry;
import org.eclipse.che.api.project.server.ProjectImporter;
import org.eclipse.che.ide.ext.dropbox.shared.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxProjectImporter implements ProjectImporter {

    private DropboxClient client;

    @Inject
    public DropboxProjectImporter(DropboxClient client) {
        this.client = client;
    }

    @Override
    public String getId() {
        return Constants.IMPORTER_ID;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public ImporterCategory getCategory() {
        return ImporterCategory.ARCHIVE;
    }

    @Override
    public String getDescription() {
        return "Here you can import any folder from Dropbox into Codenvy as standalone project.";
    }

    @Override
    public void importSources(FolderEntry baseFolder, String location, Map<String, String> parameters)
            throws ForbiddenException, ConflictException, UnauthorizedException, IOException, ServerException {
        importSources(baseFolder, location, parameters, LineConsumerFactory.NULL);
    }

    @Override
    public void importSources(FolderEntry baseFolder, String location, Map<String, String> parameters,
                              LineConsumerFactory lineConsumerFactory)
            throws ForbiddenException, ConflictException, UnauthorizedException, IOException, ServerException {
        client.importDropboxFolder(location, baseFolder);
    }
}
