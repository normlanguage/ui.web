package dev.normlanguage.ui.web.demo;

import com.vaadin.flow.component.Component;
import java.util.function.Consumer;

final class WebPages {
  private final Consumer<Component> factory;

  WebPages(Consumer<Component> factory) {
    this.factory = factory;
  }

  void mount(Component session) {
    factory.accept(session);
  }
}
