package dev.normlanguage.ui.web;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.UIDetachedException;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.VaadinSession;
import java.util.*;

public final class WebSession implements AutoCloseable {
  private final UI ui;
  private final VaadinSession owner;
  private final Div root;
  private final List<Runnable> cleanup = new ArrayList<>();
  private boolean closed;

  WebSession(UI ui, Div root) {
    this.ui = ui;
    this.owner = Objects.requireNonNull(ui.getSession());
    this.root = root;
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
          root.removeAll();
          root.add(node.component());
        });
  }

  public void onClose(Runnable release) {
    locked(
        () -> {
          if (closed) release.run();
          else cleanup.add(release);
        });
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
          RuntimeException failure = null;
          for (int i = cleanup.size() - 1; i >= 0; i--) {
            try {
              cleanup.get(i).run();
            } catch (RuntimeException error) {
              if (failure == null) failure = error;
              else failure.addSuppressed(error);
            }
          }
          cleanup.clear();
          root.removeAll();
          if (failure != null) throw failure;
        });
  }
}
