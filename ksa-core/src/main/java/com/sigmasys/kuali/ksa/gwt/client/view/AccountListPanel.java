package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.service.AccountColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ColumnModelFactory;
import com.sigmasys.kuali.ksa.gwt.client.service.ServiceFactory;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.OkCancelDialog;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;

import java.util.Collection;


/**
 * AccountListPanel
 *
 * @author Michael Ivanov
 */
public class AccountListPanel extends AbstractListPanel<AccountModel> {

    private static final AccountColumnModelFactory columnFactory = new AccountColumnModelFactory();

    private Button viewChargesButton;
    private Button viewPaymentsButton;
    private Button viewDefermentsButton;
    private Button ageDebtButton;

    private void addTabItem(String name) {
        KsaPanel panel = Registry.get(KsaPanel.class.getName());
        if (panel != null) {
            TabItem item = new TabItem(name);
            item.setLayout(new FitLayout());
            item.setClosable(true);
            item.setIconStyle("tabs");
            // TODO:
            item.add(WidgetFactory.createTextBold("COMING SOON!!!"));
            panel.add(item);
            panel.setSelection(item);
        }
    }


    @Override
    protected ToolBar createTopComponent() {

        viewChargesButton = new Button("View Charges");
        viewChargesButton.setToolTip("Charges for the selected account will be shown on the new tab");
        viewChargesButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                AccountModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Charges (Account ID: " + model.getId() + ")");
                }
            }
        });

        viewPaymentsButton = new Button("View Payments");
        viewPaymentsButton.setToolTip("Payments for the selected account will be shown on the new tab");
        viewPaymentsButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                AccountModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Payments (Account ID: " + model.getId() + ")");
                }
            }
        });

        viewDefermentsButton = new Button("View Deferments");
        viewDefermentsButton.setToolTip("Deferments for the selected account will be shown on the new tab");
        viewDefermentsButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                AccountModel model = getSelectedItem();
                if (model != null) {
                    addTabItem("Deferments (Account ID: " + model.getId() + ")");
                }
            }
        });

        ageDebtButton = new Button("Age Transactions");
        ageDebtButton.setToolTip("Aging debt");
        ageDebtButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                final AccountModel model = getSelectedItem();
                if (model != null) {
                    Dialog dialog = new OkCancelDialog() {
                        @Override
                        protected void init() {
                            setModal(false);
                            setHeading("Age Transactions (Account ID: " + model.getId() + ")");
                            setResizable(true);
                            setClosable(true);
                            setSize(700, 400);
                            setLayout(new BorderLayout());
                            BorderLayoutData layoutData = new BorderLayoutData(Style.LayoutRegion.CENTER);
                            layoutData.setMargins(new Margins(20, 20, 20, 20));
                            add(WidgetFactory.createTextBold("COMING SOON!!!"), layoutData);
                        }
                    };

                    KsaDesktop desktop = Registry.get(KsaDesktop.class.getName());
                    if (desktop != null) {
                        desktop.openWindow(dialog, false);
                    } else {
                        dialog.show();
                    }
                }
            }
        });


        ToolBar toolBar = new ToolBar();

        toolBar.add(viewChargesButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(viewPaymentsButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(viewDefermentsButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(ageDebtButton);

        enableButtons(false);

        return toolBar;
    }

    @Override
    protected ColumnModelFactory<AccountModel> getColumnModelFactory() {
        return columnFactory;
    }

    @Override
    protected void loadData(PagingLoadConfig loadConfig,
                            AsyncCallback<PagingLoadResult<AccountModel>> callback) {
        enableButtons(false);
        SortInfo sortInfo = loadConfig.getSortInfo();
        ServiceFactory.getAccountService().findAccounts(
                getSearchCriteria(),
                sortInfo.getSortDir().name(),
                sortInfo.getSortField(),
                loadConfig.getOffset(),
                loadConfig.getLimit(), callback);

    }

    @Override
    protected String getAutoExpandColumn() {
        return AccountModel.COLUMN_STREET_ADDRESS;
    }


    protected void enableButtons(boolean enabled) {
        viewChargesButton.setEnabled(enabled);
        viewPaymentsButton.setEnabled(enabled);
        viewDefermentsButton.setEnabled(enabled);
        ageDebtButton.setEnabled(enabled);
    }

    @Override
    protected void onSingleRowSelection(AccountModel item) {
        enableButtons(item != null);
        super.onSingleRowSelection(item);
    }

    @Override
    protected void onEmptyRowSelection() {
        enableButtons(false);
        super.onEmptyRowSelection();
    }

    @Override
    protected void onMultipleRowSelection(Collection<AccountModel> items) {
        enableButtons(false);
        super.onMultipleRowSelection(items);
    }
}
