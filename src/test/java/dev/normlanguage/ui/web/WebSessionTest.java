package dev.normlanguage.ui.web;

import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import java.util.*;
import org.junit.jupiter.api.Test;

class WebSessionTest {
  static UI ui() {
    var session =
        new com.vaadin.flow.server.VaadinSession(null) {
          private final java.util.concurrent.locks.ReentrantLock lock =
              new java.util.concurrent.locks.ReentrantLock();

          public java.util.concurrent.locks.Lock getLockInstance() {
            return lock;
          }

          public void lock() {
            lock.lock();
          }

          public void unlock() {
            lock.unlock();
          }

          public boolean hasLock() {
            return lock.isHeldByCurrentThread();
          }
        };
    return new UI() {
      public com.vaadin.flow.server.VaadinSession getSession() {
        return session;
      }
    };
  }

  @Test
  void sessionsOwnIndependentRootsAndCloseInReverseOrder() {
    var firstRoot = new Div();
    var secondRoot = new Div();
    var first = new WebSession(ui(), firstRoot);
    var second = new WebSession(ui(), secondRoot);
    first.root(new WebNode("text"));
    second.root(new WebNode("input"));
    var release = new ArrayList<Integer>();
    first.onClose(() -> release.add(1));
    first.onClose(() -> release.add(2));
    first.close();
    first.close();
    assertEquals(List.of(2, 1), release);
    assertEquals(0, firstRoot.getComponentCount());
    assertEquals(1, secondRoot.getComponentCount());
    second.close();
    assertEquals(0, secondRoot.getComponentCount());
  }

  @Test
  void cleanupFailureStillReleasesRemainingResources() {
    var session = new WebSession(ui(), new Div());
    var released = new ArrayList<Integer>();
    session.onClose(() -> released.add(1));
    session.onClose(
        () -> {
          throw new IllegalStateException("expected");
        });
    assertThrows(IllegalStateException.class, session::close);
    assertEquals(List.of(1), released);
    session.close();
  }

  @Test
  void closingFromWorkerHoldsCapturedSessionLock() throws Exception {
    var ui = ui();
    var session = new WebSession(ui, new Div());
    var held = new java.util.concurrent.atomic.AtomicBoolean();
    session.onClose(() -> held.set(ui.getSession().hasLock()));
    var worker = new Thread(session::close);
    worker.start();
    worker.join(2000);
    assertFalse(worker.isAlive());
    assertTrue(held.get());
  }

  @Test
  void detachedUiReleasesUnderItsOriginalSessionLock() {
    var attached = ui();
    var owner = attached.getSession();
    var detached = new java.util.concurrent.atomic.AtomicBoolean();
    var ui =
        new UI() {
          public com.vaadin.flow.server.VaadinSession getSession() {
            return detached.get() ? null : owner;
          }
        };
    var session = new WebSession(ui, new Div());
    var held = new java.util.concurrent.atomic.AtomicBoolean();
    session.onClose(() -> held.set(owner.hasLock()));
    detached.set(true);
    session.later(() -> fail("detached action must not execute"));
    assertTrue(held.get());
    session.close();
  }

  @Test
  void registrationAfterCloseIsReleasedImmediately() {
    var session = new WebSession(ui(), new Div());
    session.close();
    var calls = new ArrayList<Integer>();
    session.onClose(() -> calls.add(1));
    assertEquals(List.of(1), calls);
  }
}
