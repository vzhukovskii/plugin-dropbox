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
package org.eclipse.che.ide.ext.dropbox.server.inject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import org.eclipse.che.api.project.server.ProjectImporter;
import org.eclipse.che.ide.ext.dropbox.server.DropboxProjectImporter;
import org.eclipse.che.ide.ext.dropbox.server.oauth2.DropboxOAuthAuthenticator;
import org.eclipse.che.ide.ext.dropbox.server.rest.DropboxService;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.security.oauth.OAuthAuthenticator;

/**
 * @author Vladyslav Zhukovskyi
 */
@DynaModule
public class DropboxModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<OAuthAuthenticator> oAuthAuthenticators = Multibinder.newSetBinder(binder(), OAuthAuthenticator.class);
        oAuthAuthenticators.addBinding().to(DropboxOAuthAuthenticator.class);

        bind(DropboxService.class);

        Multibinder.newSetBinder(binder(), ProjectImporter.class).addBinding().to(DropboxProjectImporter.class);

        //test data
        bindConstant().annotatedWith(Names.named("oauth.dropbox.clientid")).to(""); //app key
        bindConstant().annotatedWith(Names.named("oauth.dropbox.clientsecret")).to(""); //app secret
        bindConstant().annotatedWith(Names.named("oauth.dropbox.redirecturis")).to("http://dev.box.com/api/oauth/callback");
        bindConstant().annotatedWith(Names.named("oauth.dropbox.authuri")).to("https://www.dropbox.com/1/oauth2/authorize");
        bindConstant().annotatedWith(Names.named("oauth.dropbox.tokenuri")).to("https://api.dropbox.com/1/oauth2/token");

        bindConstant().annotatedWith(Names.named("oauth.foo.token")).to("");
    }
}
