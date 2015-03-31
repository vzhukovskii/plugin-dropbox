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

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.api.project.shared.dto.ImportProject;
import org.eclipse.che.ide.api.wizard.AbstractWizardPage;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class DropboxProjectImporterPresenter extends AbstractWizardPage<ImportProject> implements DropboxProjectImporterView.ActionDelegate  {

    private DropboxProjectImporterView view;

    @Inject
    public DropboxProjectImporterPresenter(DropboxProjectImporterView view) {
        this.view = view;
        this.view.setDelegate(this);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
