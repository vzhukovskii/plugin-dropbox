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
package org.eclipse.che.ide.ext.dropbox.client.importer;

import org.eclipse.che.api.project.shared.dto.ImportProject;
import org.eclipse.che.ide.api.project.wizard.ImportWizardRegistrar;
import org.eclipse.che.ide.api.wizard.WizardPage;
import org.eclipse.che.ide.collections.Array;
import org.eclipse.che.ide.collections.Collections;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.che.ide.ext.dropbox.shared.Constants;

import javax.annotation.Nonnull;

/**
 * @author Vladyslav Zhukovskyi
 */
public class DropboxImportWizardRegistrar implements ImportWizardRegistrar {

    private final Array<Provider<? extends WizardPage<ImportProject>>> wizardPages;

    @Inject
    public DropboxImportWizardRegistrar(final Provider<DropboxProjectImporterPresenter> provider) {
        wizardPages = Collections.createArray();
        wizardPages.add(provider);
    }

    @Nonnull
    @Override
    public String getImporterId() {
        return Constants.WIZARD_ID;
    }

    @Nonnull
    @Override
    public Array<Provider<? extends WizardPage<ImportProject>>> getWizardPages() {
        return wizardPages;
    }
}
