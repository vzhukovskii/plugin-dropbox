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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.eclipse.che.ide.ext.dropbox.server.oauth2.token.TokenProvider;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DummyTokenProvider implements TokenProvider {
    private String token;

    @Inject
    public DummyTokenProvider(@Named("oauth.dummy.token") String token) {
        this.token = token;
    }

    @Override
    public String getToken() throws Exception {
        return token;
    }
}
