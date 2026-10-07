package dev.normlanguage.ui.web.demo;

import com.vaadin.flow.component.AttachEvent;
import dev.normlanguage.ui.web.WebContainer;
import com.vaadin.flow.router.Route;

@Route("")
public final class WebRoute extends WebContainer {
  private final WebPages pages;

  public WebRoute(WebPages pages) {
    this.pages = pages;
    setWidthFull();
    getStyle().set("min-height", "100vh");
  }

  protected void onAttach(AttachEvent event) {
    super.onAttach(event);
    pages.mount(this);
  }
}
