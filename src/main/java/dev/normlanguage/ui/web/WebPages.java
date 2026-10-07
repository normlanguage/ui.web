package dev.normlanguage.ui.web;

import java.util.function.Consumer;

final class WebPages {
  private final Consumer<WebSession> factory;

  WebPages(Consumer<WebSession> factory) {
    this.factory = factory;
  }

  void mount(WebSession session) {
    factory.accept(session);
  }
}
