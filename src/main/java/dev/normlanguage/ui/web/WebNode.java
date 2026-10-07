package dev.normlanguage.ui.web;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import java.util.*;
import java.util.function.Consumer;

public final class WebNode implements AutoCloseable {
  private final Component component;
  private final Set<String> themeKeys = new HashSet<>();
  private final Set<String> layoutKeys = new HashSet<>();
  private final List<Registration> listeners = new ArrayList<>();
  private Runnable action;
  private Consumer<String> textChanged;
  private Consumer<Boolean> checkedChanged;
  private boolean closed;
  private int columns;
  private final java.util.SortedMap<Double, Integer> breakpoints = new java.util.TreeMap<>();

  public WebNode(String kind) {
    this(create(kind));
  }

  private static Component create(String kind) {
    return switch (kind) {
      case "text" -> new Span();
      case "button" -> new Button();
      case "input" -> new TextField();
      case "multiline" -> new TextArea();
      case "toggle" -> new Checkbox();
      case "container" -> new WebLayout();
      default -> throw new IllegalArgumentException("Unknown node kind: " + kind);
    };
  }

  public WebNode(Component component) {
    this.component = java.util.Objects.requireNonNull(component);
    if (component instanceof Button button)
      listeners.add(button.addClickListener(e -> fireAction()));
    if (component instanceof TextField field) {
      field.setValueChangeMode(ValueChangeMode.EAGER);
      listeners.add(
          field.addValueChangeListener(
              e -> {
                if (e.isFromClient() && textChanged != null) textChanged.accept(e.getValue());
              }));
      listeners.add(
          field.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, e -> fireAction()));
    }
    if (component instanceof TextArea field) {
      field.setValueChangeMode(ValueChangeMode.EAGER);
      listeners.add(
          field.addValueChangeListener(
              e -> {
                if (e.isFromClient() && textChanged != null) textChanged.accept(e.getValue());
              }));
    }
    if (component instanceof Checkbox field)
      listeners.add(
          field.addValueChangeListener(
              e -> {
                if (e.isFromClient() && checkedChanged != null) checkedChanged.accept(e.getValue());
              }));
  }

  Element element() {
    return component.getElement();
  }

  public Component nativeComponent() {
    return component;
  }

  Component component() {
    return component;
  }

  public void enabled(boolean value) {
    element().setEnabled(value);
  }

  public void title(boolean title) {
    if (!(component instanceof Span)) return;
    if (title) {
      element().setAttribute("role", "heading");
      element().setAttribute("aria-level", "1");
    } else {
      element().removeAttribute("role");
      element().removeAttribute("aria-level");
    }
  }

  public void identity(String key) {
    if (key.isEmpty()) element().removeAttribute("data-norm-key");
    else element().setAttribute("data-norm-key", key);
  }

  public void text(String value) {
    if (component instanceof TextField field) field.setValue(value);
    else if (component instanceof TextArea field) field.setValue(value);
    else if (component instanceof Checkbox field) field.setLabel(value);
    else if (component instanceof HasText text) text.setText(value);
  }

  public String readText() {
    if (component instanceof TextField field) return field.getValue();
    if (component instanceof TextArea field) return field.getValue();
    throw new IllegalStateException("Node is not a text field");
  }

  public void placeholder(String value) {
    if (component instanceof TextField field) field.setPlaceholder(value);
    else if (component instanceof TextArea field) field.setPlaceholder(value);
  }

  public void multiline(int rows, boolean editable) {
    var field = (TextArea) component;
    field.setReadOnly(!editable);
    field.setMinHeight((rows * 1.5) + "em");
  }

  public void checked(boolean value) {
    ((Checkbox) component).setValue(value);
  }

  public void onAction(Runnable callback) {
    action = callback;
  }

  public void onText(Consumer<String> callback) {
    textChanged = callback;
  }

  public void onToggle(Consumer<Boolean> callback) {
    checkedChanged = callback;
  }

  void fireAction() {
    if (!closed && action != null) action.run();
  }

  public void clear() {
    text("");
  }

  public void focus() {
    ((Focusable<?>) component).focus();
  }

  public void children(List<WebNode> nodes) {
    if (!(component instanceof Div)) {
      if (!nodes.isEmpty()) throw new IllegalArgumentException("leaf node cannot own children");
      return;
    }
    var target = element();
    var wanted = nodes.stream().map(WebNode::element).toList();
    target.getChildren().filter(e -> !wanted.contains(e)).toList().forEach(target::removeChild);
    for (int i = 0; i < wanted.size(); i++) target.insertChild(i, wanted.get(i));
  }

  public void style(String key, String value) {
    if (value.isEmpty()) element().getStyle().remove(key);
    else element().getStyle().set(key, value);
  }

  public void theme(Map<String, String> properties) {
    themeKeys.forEach(key -> element().getStyle().remove(key));
    themeKeys.clear();
    properties.forEach(
        (key, value) -> {
          themeKeys.add(key);
          style(key, value);
        });
  }

  public void layout(String display) {
    breakpoints.clear();
    element().getStyle().remove("--norm-columns");
    layoutKeys.forEach(k -> element().getStyle().remove(k));
    layoutKeys.clear();
    element().setAttribute("data-norm-layout", display);
    element().removeAttribute("data-norm-breakpoints");
    layoutStyle("display", display);
  }

  public void layoutStyle(String key, String value) {
    layoutKeys.add(key);
    style(key, value);
  }

  public void grid(int columns) {
    if (columns < 1) throw new IllegalArgumentException("columns must be positive");
    this.columns = columns;
    element().setAttribute("data-norm-columns", Integer.toString(columns));
    layoutStyle(
        "grid-template-columns", "repeat(var(--norm-columns," + columns + "), minmax(0,1fr))");
  }

  public void gridSpan(int span) {
    if (span < 1) throw new IllegalArgumentException("grid span must be positive");
    element().setAttribute("data-norm-span", Integer.toString(span));
  }

  public void breakpoint(double minimumWidth, int columns) {
    if (!Double.isFinite(minimumWidth) || minimumWidth < 0 || columns < 1)
      throw new IllegalArgumentException("invalid grid breakpoint");
    breakpoints.put(minimumWidth, columns);
  }

  public void responsive() {
    if (breakpoints.isEmpty()) return;
    String data =
        breakpoints.entrySet().stream()
            .map(e -> "[" + e.getKey() + "," + e.getValue() + "]")
            .collect(java.util.stream.Collectors.joining(",", "[", "]"));
    element().setAttribute("data-norm-breakpoints", data);
  }

  public void masonry(double minimumColumnWidth, double gap) {
    if (!Double.isFinite(minimumColumnWidth)
        || minimumColumnWidth <= 0
        || !Double.isFinite(gap)
        || gap < 0) throw new IllegalArgumentException("invalid masonry dimensions");
    element().setAttribute("data-norm-layout", "masonry");
    element().setAttribute("data-norm-column-width", Double.toString(minimumColumnWidth));
    element().setAttribute("data-norm-gap", Double.toString(gap));
    layoutStyle("position", "relative");
  }

  public void direction(boolean rtl) {
    element().setAttribute("dir", rtl ? "rtl" : "ltr");
  }

  public void close() {
    if (closed) return;
    closed = true;
    listeners.forEach(Registration::remove);
    listeners.clear();
    action = null;
    textChanged = null;
    checkedChanged = null;
    element().removeFromParent();
  }
}
