/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2023 DBeaver Corp and others
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
package org.jkiss.dbeaver.ui.dialogs.driver;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.connection.DBPDriverFileInfo;
import org.jkiss.dbeaver.model.connection.DBPDriverFileSource;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.internal.UIConnectionMessages;
import org.jkiss.utils.ArrayUtils;
import org.jkiss.utils.CommonUtils;

class DriverDownloadManualPage extends DriverDownloadPage {

    private DBPDriverFileSource fileSource;
    private Table filesTable;

    DriverDownloadManualPage() {
        super(UIConnectionMessages.dialog_driver_download_manual_page_config_driver_file, UIConnectionMessages.dialog_driver_download_manual_page_download_driver_file, null);
        setPageComplete(false);
    }

    @Override
    public void createControl(Composite parent) {
        final DBPDriver driver = getWizard().getDriver();

        setMessage(NLS.bind(UIConnectionMessages.dialog_driver_download_manual_page_download_config_driver_file, driver.getFullName()));

        Composite composite = UIUtils.createPlaceholder(parent, 1);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Link infoText = new Link(composite, SWT.NONE);
        infoText.setText(NLS.bind(UIConnectionMessages.dialog_driver_download_manual_page_driver_file_missing_text, driver.getFullName()));
        infoText.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getWizard().getContainer().buttonPressed(DriverDownloadDialog.EDIT_DRIVER_BUTTON_ID);
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        infoText.setLayoutData(gd);

        Group filesGroup = UIUtils.createControlGroup(composite, UIConnectionMessages.dialog_driver_download_manual_page_driver_file, 1, -1, -1);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalIndent = 10;
        filesGroup.setLayoutData(gd);

        final Combo sourceCombo = new Combo(filesGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (DBPDriverFileSource source : driver.getDriverFileSources()) {
            sourceCombo.add(source.getName());
        }
        final Link driverLink = new Link(filesGroup, SWT.NONE);
        driverLink.setText("<a>" + driver.getDriverFileSources().get(0).getUrl() + "</a>");
        driverLink.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        driverLink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                UIUtils.openWebBrowser(driver.getDriverFileSources().get(sourceCombo.getSelectionIndex()).getUrl());
            }
        });

        filesTable = new Table(filesGroup, SWT.BORDER | SWT.FULL_SELECTION);
        filesTable.setHeaderVisible(true);
        filesTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        UIUtils.createTableColumn(filesTable, SWT.LEFT, UIConnectionMessages.dialog_driver_download_manual_page_column_file);
        UIUtils.createTableColumn(filesTable, SWT.LEFT, UIConnectionMessages.dialog_driver_download_manual_page_column_required);
        UIUtils.createTableColumn(filesTable, SWT.LEFT, UIConnectionMessages.dialog_driver_download_manual_page_column_description);

        sourceCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectFileSource(driver.getDriverFileSources().get(sourceCombo.getSelectionIndex()));
                driverLink.setText("<a>" + driver.getDriverFileSources().get(sourceCombo.getSelectionIndex()).getUrl() + "</a>");
            }
        });

        sourceCombo.select(0);
        selectFileSource(driver.getDriverFileSources().get(0));
        UIUtils.packColumns(filesTable, true);

        createLinksPanel(composite);

        composite.setTabList(ArrayUtils.remove(Control.class, composite.getTabList(), infoText));

        setControl(composite);
    }

    private void selectFileSource(DBPDriverFileSource source) {
        fileSource = source;
        filesTable.removeAll();
        for (DBPDriverFileInfo file : fileSource.getFiles()) {
            new TableItem(filesTable, SWT.NONE).setText(new String[] {
                file.getName(),
                !file.isOptional() ? UIConnectionMessages.dialog_driver_download_manual_page_yes : UIConnectionMessages.dialog_driver_download_manual_page_no,
                CommonUtils.notEmpty(file.getDescription()) });
        }
    }

    @Override
    public boolean isPageComplete() {
        return fileSource != null;
    }

    @Override
    void resolveLibraries() {
        // do nothing
    }

    @Override
    boolean performFinish() {
        UIUtils.asyncExec(() -> UIUtils.openWebBrowser(fileSource.getUrl()));
        return false;
//        DriverEditDialog dialog = new DriverEditDialog(null, getWizard().getDriver());
//        dialog.open();
    }

}