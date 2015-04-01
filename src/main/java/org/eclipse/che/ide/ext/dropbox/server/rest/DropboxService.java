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
package org.eclipse.che.ide.ext.dropbox.server.rest;


import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.UnauthorizedException;
import org.eclipse.che.ide.ext.dropbox.server.DropboxClient;
import org.eclipse.che.ide.ext.dropbox.shared.dto.DbxItemList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Vladyslav Zhukovskyi
 */
@Path("dropbox")
public class DropboxService {
    private static final Logger LOG = LoggerFactory.getLogger(DropboxService.class);

    private DropboxClient client;

    @Inject
    public DropboxService(DropboxClient client) {
        this.client = client;
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public DbxItemList listDropboxFolder(@DefaultValue("/") @QueryParam("path") String path)
            throws UnauthorizedException, ServerException {
        return client.listDropboxFolder(path);
    }
}
