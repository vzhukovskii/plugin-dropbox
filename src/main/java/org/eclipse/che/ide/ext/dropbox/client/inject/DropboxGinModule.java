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
package org.eclipse.che.ide.ext.dropbox.client.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.ide.api.project.wizard.ImportWizardRegistrar;
import org.eclipse.che.ide.ext.dropbox.client.DropboxClientService;
import org.eclipse.che.ide.ext.dropbox.client.importer.DropboxImportWizardRegistrar;

/**
 * @author Vladyslav Zhukovskyi
 */
@ExtensionGinModule
public class DropboxGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(DropboxClientService.class);

        GinMultibinder.newSetBinder(binder(), ImportWizardRegistrar.class).addBinding().to(DropboxImportWizardRegistrar.class);
    }
}
