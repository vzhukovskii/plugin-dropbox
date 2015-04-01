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
package org.eclipse.che.ide.ext.dropbox.server.oauth2.token.impl;

import com.dropbox.core.DbxException;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.auth.shared.dto.OAuthToken;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.UnauthorizedException;
import org.eclipse.che.commons.env.EnvironmentContext;
import org.eclipse.che.commons.user.User;
import org.eclipse.che.ide.ext.dropbox.server.oauth2.DropboxOAuthAuthenticator;
import org.eclipse.che.ide.ext.dropbox.server.oauth2.token.TokenProvider;

import java.io.IOException;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class OAuthTokenProvider implements TokenProvider {
    private DropboxOAuthAuthenticator oAuthAuthenticator;

    @Inject
    public OAuthTokenProvider(DropboxOAuthAuthenticator oAuthAuthenticator) {
        this.oAuthAuthenticator = oAuthAuthenticator;
    }

    @Override
    public String getToken() throws Exception {
        final User currentUser = EnvironmentContext.getCurrent().getUser();

        OAuthToken token;
        try {
            token = oAuthAuthenticator.getToken(currentUser.getId());
        } catch (IOException e) {
            if (e.getCause() != null && e.getCause() instanceof DbxException.InvalidAccessToken) {
                throw new UnauthorizedException("Authentication required");
            }
            throw new ServerException(e.getMessage(), e);
        }

        if (token == null || Strings.isNullOrEmpty(token.getToken())) {
            throw new UnauthorizedException("Authentication required");
        }

        return token.getToken();
    }
}
