package org.kuali.student.lum.ui.requirements.client.view;

import org.kuali.student.common.ui.client.mvc.Controller;
import org.kuali.student.common.ui.client.mvc.ViewComposite;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SimpleView extends ViewComposite {
    
    private Panel mainPanel = new SimplePanel();

    public SimpleView(Controller controller) {
        super(controller, "Simple View");
        super.initWidget(mainPanel);
        Panel testPanel = new VerticalPanel();
        testPanel.add(new Label("Simple View"));
        mainPanel.add(testPanel);
    }
    
}
