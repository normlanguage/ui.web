package dev.normlanguage.ui.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class WebNodeTest {
  @Test
  void emptyRenderedChildrenPreserveLeafText() {
    for (String kind : List.of("text", "button")) {
      var node = new WebNode(kind);
      node.text("Visible label");
      node.children(List.of());
      assertEquals("Visible label", node.element().getText());
    }
  }

  @Test
  void clearingThemeRestoresInheritedComponentTokens() {
    var node = new WebNode("input");
    node.layoutStyle("width", "100%");
    node.theme(
        java.util.Map.of(
            "--vaadin-input-field-background", "#111", "--vaadin-input-field-value-color", "#eee"));
    node.theme(java.util.Map.of());
    assertNull(node.element().getStyle().get("--vaadin-input-field-background"));
    assertNull(node.element().getStyle().get("--vaadin-input-field-value-color"));
    assertEquals("100%", node.element().getStyle().get("width"));
  }

  @Test
  void keyedChildrenKeepIdentityWhenReordered() {
    var parent = new WebNode("container");
    var a = new WebNode("input");
    var b = new WebNode("button");
    parent.children(List.of(a, b));
    a.text("kept");
    parent.children(List.of(b, a));
    assertSame(a.element().getNode(), parent.element().getChild(1).getNode());
    assertEquals("kept", a.readText());
  }

  @Test
  void listenersAreReplacedAndClosed() {
    var node = new WebNode("button");
    var calls = new AtomicInteger();
    node.onAction(calls::incrementAndGet);
    node.onAction(() -> calls.addAndGet(2));
    node.fireAction();
    assertEquals(2, calls.get());
    node.close();
    node.fireAction();
    assertEquals(2, calls.get());
  }

  @Test
  void updatingInputDoesNotInvokeClientBinding() {
    var node = new WebNode("input");
    var calls = new AtomicInteger();
    node.onText(v -> calls.incrementAndGet());
    node.text("from state");
    assertEquals(0, calls.get());
    assertEquals("from state", node.readText());
  }

  @Test
  void layoutStylesResetAcrossUpdatesAndPreserveEnvironment() {
    var node = new WebNode("container");
    node.style("color", "red");
    node.layout("flex");
    node.layoutStyle("gap", "10px");
    node.style("--norm-columns", "8");
    node.layout("grid");
    assertNull(node.element().getStyle().get("--norm-columns"));
    assertNull(node.element().getStyle().get("gap"));
    assertEquals("red", node.element().getStyle().get("color"));
  }
}
