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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.constraints.Anchor;
import org.eclipse.che.ide.api.constraints.Constraints;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.ext.dropbox.client.action.FooAction;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
@Extension(title = "Dropbox Support", version = "1.0.0")
public class DropboxExtension {
    public static final String DROPBOX_COMMAND_GROUP = "DropboxCommandGroup";
    public static final String DROPBOX_GROUP_MAIN_MENU = "Dropbox";

    @Inject
    public DropboxExtension(ActionManager actionManager,
                            FooAction fooAction) {

        Constraints beforeWindow = new Constraints(Anchor.BEFORE, IdeActions.GROUP_WINDOW);
        final DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(IdeActions.GROUP_MAIN_MENU);
        DefaultActionGroup dbxMenu = new DefaultActionGroup(DROPBOX_GROUP_MAIN_MENU, true, actionManager);

        DefaultActionGroup dbxCommandGroup = new DefaultActionGroup(DROPBOX_COMMAND_GROUP, false, actionManager);

        actionManager.registerAction(DROPBOX_GROUP_MAIN_MENU, dbxMenu);
        mainMenu.add(dbxMenu, beforeWindow);

        actionManager.registerAction(DROPBOX_COMMAND_GROUP, dbxCommandGroup);
        dbxMenu.add(dbxCommandGroup);
        dbxCommandGroup.addSeparator();

        // actions
        actionManager.registerAction("DropboxFoo", fooAction);
        dbxCommandGroup.add(fooAction);
    }
}
