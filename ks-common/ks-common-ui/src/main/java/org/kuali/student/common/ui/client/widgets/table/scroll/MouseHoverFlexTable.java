package org.kuali.student.common.ui.client.widgets.table.scroll;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;

public class MouseHoverFlexTable extends FlexTable {
    private TableModel tableModel;
    public MouseHoverFlexTable(){
        sinkEvents(Event.ONMOUSEOVER |Event.ONMOUSEOUT);
        
    }
    public void setModel(TableModel tableModel){
        this.tableModel = tableModel;
    }
    
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        Element td = getEventTargetCell(event);
        if (td == null)
            return;
       
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOVER: {
                Element tr = DOM.getParent(td);
                tr.setAttribute("class", "table-row-hover");

                break;
            }
            case Event.ONMOUSEOUT: {
                int count = tableModel.getRowCount();
                for (int r = 0; r < count; r++) {
                    Element tr = getRowFormatter().getElement(r);
                    if (tableModel.getRow(r).isSelected()) {
                    	tr.setAttribute("class", "table-row-selected");
                    }else{
                    	tr.setAttribute("class", "table-row");
                    }
                }
                break;
            }

        }

    }

}