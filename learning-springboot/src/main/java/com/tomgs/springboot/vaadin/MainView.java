package com.tomgs.springboot.vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * MainView
 *
 * @author tomgs
 * @since 1.0
 */
@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Text("Hello World"));
    }
}
