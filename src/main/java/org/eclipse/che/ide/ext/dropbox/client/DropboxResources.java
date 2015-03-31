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

import com.google.gwt.resources.client.ClientBundle;
import org.eclipse.che.ide.ext.dropbox.client.importer.DropboxProjectImporterViewImpl;

/**
 * @author Vladyslav Zhukovskyi
 */
public interface DropboxResources extends ClientBundle {

    @Source({"importer/DropboxProjectImporterView.css", "org/eclipse/che/ide/ui/Styles.css"})
    DropboxProjectImporterViewImpl.Style dropboxProjectImporterPageStyle();

}
