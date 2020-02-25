/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2019 Serge Rider (serge@jkiss.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ui.editors.sql.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jkiss.dbeaver.ui.navigator.NavigatorUtils;
import org.jkiss.dbeaver.ui.navigator.database.NavigatorViewBase;

public class SQLEditorHandlerSyncConnection extends AbstractHandler {

    public SQLEditorHandlerSyncConnection()
    {
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final NavigatorViewBase navigatorView = NavigatorUtils.getActiveNavigatorView(event);
        if (navigatorView == null) {
            return null;
        }
        IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
        if (NavigatorUtils.syncEditorWithNavigator(navigatorView, activeEditor)) {
            HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().activate(activeEditor);
        }
        return null;
    }

}
