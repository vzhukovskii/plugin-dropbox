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
package org.eclipse.che.ide.ext.dropbox.client.action;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;

/**
 * @author Vladyslav Zhukovskyi
 */
@Singleton
public class FooAction extends Action {
    @Inject
    public FooAction() {
        super("Foo title", "Foo description");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Window.alert("Foo");
    }

    @Override
    public void update(ActionEvent e) {
        e.getPresentation().setVisible(true);
        e.getPresentation().setEnabled(true);
    }
}
