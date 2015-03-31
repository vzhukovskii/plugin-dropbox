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
package org.eclipse.che.ide.ext.dropbox.server.oauth2;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.inject.name.Named;
import org.eclipse.che.api.auth.shared.dto.OAuthToken;
import org.eclipse.che.ide.ext.dropbox.shared.Constants;
import org.eclipse.che.security.oauth.OAuthAuthenticationException;
import org.eclipse.che.security.oauth.OAuthAuthenticator;
import org.eclipse.che.security.oauth.shared.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxOAuthAuthenticator extends OAuthAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(DropboxOAuthAuthenticator.class);

    @Inject
    public DropboxOAuthAuthenticator(@Named("oauth.dropbox.clientid") String clientId,
                                     @Named("oauth.dropbox.clientsecret") String clientSecret,
                                     @Named("oauth.dropbox.redirecturis") String[] redirectUris,
                                     @Named("oauth.dropbox.authuri") String authUri,
                                     @Named("oauth.dropbox.tokenuri") String tokenUri,
                                     MemoryDataStoreFactory dataStoreFactory) throws IOException {
        super(clientId, clientSecret, redirectUris, authUri, tokenUri, dataStoreFactory);
    }

    @Override
    public User getUser(OAuthToken oAuthToken) throws OAuthAuthenticationException {
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Codenvy/1.0", Locale.getDefault().toString());
        DbxClient dbxClient = new DbxClient(dbxRequestConfig, oAuthToken.getToken());

        LOG.info("Trying to fetch user with token: {}", oAuthToken.getToken());

        try {
            DbxAccountInfo accountInfo = dbxClient.getAccountInfo();

            DropboxUser user = new DropboxUser(accountInfo.userId + "", accountInfo.userId + "", accountInfo.displayName);
            LOG.info("Dropbox user fetched: {}", user.toString());
            return user;
        } catch (DbxException e) {
            LOG.warn("Error occurred during user obtaining", e);
            throw new OAuthAuthenticationException(e.getMessage(), e);
        }
    }

    @Override
    public OAuthToken getToken(String userId) throws IOException {
        final OAuthToken token = super.getToken(userId);
        if (token == null || Strings.isNullOrEmpty(token.getToken())) {
            LOG.info("Dropbox token is null or empty");
            return null;
        }

        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig("Codenvy/1.0", Locale.getDefault().toString());
        DbxClient dbxClient = new DbxClient(dbxRequestConfig, token.getToken());

        try {
            dbxClient.getAccountInfo();
        } catch (DbxException e) {
            LOG.warn("Error occurred during token validation", e);
            return null;
        }

        return token;
    }

    @Override
    public String getOAuthProvider() {
        return Constants.OAUTH_ID;
    }
}
