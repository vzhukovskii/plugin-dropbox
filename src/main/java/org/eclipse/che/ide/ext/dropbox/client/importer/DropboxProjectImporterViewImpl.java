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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.ext.dropbox.client.DropboxResources;
import org.eclipse.che.ide.ui.Styles;

/**
 * @author Vladyslav Zhukovskyi
 */
public class DropboxProjectImporterViewImpl extends Composite implements DropboxProjectImporterView {

    interface DropboxProjectImporterViewImplUiBinder extends UiBinder<DockPanel, DropboxProjectImporterViewImpl> {
    }

    @UiField(provided = true)
    Style style;

    @UiField
    Button openDbxButton;

    @UiField
    FlowPanel dbxContainer;

    @UiField
    DeckPanel dbxPanelSwitcher;

    private ActionDelegate delegate;

    @Inject
    public DropboxProjectImporterViewImpl(DropboxResources resources,
                                          DropboxProjectImporterViewImplUiBinder uiBinder) {
        style = resources.dropboxProjectImporterPageStyle();
        style.ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));

        openDbxButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onLoadDropboxButtonClicked();
            }
        });
    }

    @Override
    public void showDropboxPanelFor(boolean authorized) {

    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    public interface Style extends Styles {
        String mainPanel();

        String namePanel();

        String labelPosition();

        String marginTop();

        String alignRight();

        String alignLeft();

        String labelErrorPosition();

        String radioButtonPosition();

        String description();

        String label();

        String horizontalLine();

        String blockHeader();
    }
}
