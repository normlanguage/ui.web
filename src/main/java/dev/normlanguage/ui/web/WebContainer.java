package dev.normlanguage.ui.web;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

@Tag("norm-layout")
@JsModule("./norm-layout.js")
@Uses(Button.class)
@Uses(TextField.class)
@Uses(TextArea.class)
@Uses(Checkbox.class)
public class WebContainer extends Div {
  public WebContainer() {
    getStyle().set("display", "block");
  }
}
