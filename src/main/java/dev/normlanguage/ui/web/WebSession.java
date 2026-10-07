package dev.normlanguage.ui.web;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import java.util.*;

public final class WebSession implements AutoCloseable {
  private final UI ui;
  private final VaadinSession owner;
  private final Component root;
  private final Registration detached;
  private final List<Runnable> cleanup = new ArrayList<>();
  private boolean closed;

  public WebSession(Component root) {
    this(
        root.getUI()
            .orElseThrow(
                () -> new IllegalStateException("mount container must be attached to a UI")),
        root);
  }

  WebSession(UI ui, Component root) {
    if (!(root instanceof HasComponents))
      throw new IllegalArgumentException("mount container must support child components");
    this.ui = ui;
    this.owner = Objects.requireNonNull(ui.getSession());
    this.root = root;
    detached = root.addDetachListener(event -> close());
  }

  private void locked(Runnable action) {
    owner.lock();
    try {
      action.run();
    } finally {
      owner.unlock();
    }
  }

  public void root(WebNode node) {
    locked(
        () -> {
          if (closed) throw new IllegalStateException("session is closed");
          ((HasComponents) root).removeAll();
          ((HasComponents) root).add(node.nativeComponent());
        });
  }

  public Registration onClose(Runnable release) {
    Runnable entry = release::run;
    locked(
        () -> {
          if (closed) entry.run();
          else cleanup.add(entry);
        });
    return Registration.once(() -> locked(() -> cleanup.remove(entry)));
  }

  public void later(Runnable action) {
    locked(
        () -> {
          if (closed) return;
          if (ui.getSession() != owner) {
            close();
            return;
          }
          try {
            ui.access(
                () -> {
                  if (!closed) action.run();
                });
          } catch (UIDetachedException failure) {
            close();
          }
        });
  }

  public void report(String message) {
    locked(
        () ->
            owner
                .getErrorHandler()
                .error(new com.vaadin.flow.server.ErrorEvent(new IllegalStateException(message))));
  }

  public void close() {
    locked(
        () -> {
          if (closed) return;
          closed = true;
          detached.remove();
          RuntimeException failure = null;
          var releases = List.copyOf(cleanup);
          cleanup.clear();
          for (int i = releases.size() - 1; i >= 0; i--) {
            try {
              releases.get(i).run();
            } catch (RuntimeException error) {
              if (failure == null) failure = error;
              else failure.addSuppressed(error);
            }
          }
          ((HasComponents) root).removeAll();
          if (failure != null) throw failure;
        });
  }
}
