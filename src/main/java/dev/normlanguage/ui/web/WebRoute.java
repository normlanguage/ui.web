package dev.normlanguage.ui.web;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public final class WebRoute extends Div {
  private WebSession session;
  private final WebPages pages;

  public WebRoute(WebPages pages) {
    this.pages = pages;
    setWidthFull();
    getStyle().set("min-height", "100vh");
  }

  protected void onAttach(AttachEvent event) {
    super.onAttach(event);
    session = new WebSession(event.getUI(), this);
    try {
      pages.mount(session);
    } catch (RuntimeException failure) {
      session.close();
      throw failure;
    }
  }

  protected void onDetach(DetachEvent event) {
    try {
      if (session != null) session.close();
    } finally {
      session = null;
      super.onDetach(event);
    }
  }
}
