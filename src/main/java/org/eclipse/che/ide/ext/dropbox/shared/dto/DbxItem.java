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
package org.eclipse.che.ide.ext.dropbox.shared.dto;

import org.eclipse.che.dto.shared.DTO;

/**
 * @author Vladyslav Zhukovskyi
 */
@DTO
public interface DbxItem {
    public enum ItemType {
        FILE,
        FOLDER
    }

    String getPath();

    /** Set path of item. */
    void setPath(String path);

    DbxItem withPath(String path);

    /** Get name of item. */
    String getName();

    /** Set name of item. */
    void setName(String name);

    DbxItem withName(String name);

    /** Get type of item, e.g. "file", "folder". */
    ItemType getType();

    /** Set type of item, e.g. "file" or "folder". */
    void setType(ItemType itemType);

    DbxItem withType(ItemType itemType);
}
