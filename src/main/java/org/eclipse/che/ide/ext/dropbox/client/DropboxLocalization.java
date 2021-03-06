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

import com.google.gwt.i18n.client.Messages;

/**
 * @author Vladyslav Zhukovskyi
 */
public interface DropboxLocalization extends Messages {

    /** Import wizard */

    @Key("import.wizard.project.information")
    public String importWizardProjectInformation();

    @Key("import.wizard.dropbox.browser")
    public String importWizardDropboxBrowser();
}
