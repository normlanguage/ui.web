package dev.normlanguage.ui.web;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import org.junit.jupiter.api.Test;

class ConsumerPackagingTest {
  @Test
  void consumerPomExportsItsDependencyManagement() throws Exception {
    var pom =
        java.nio.file.Files.readString(java.nio.file.Path.of(System.getProperty("consumerPom")));
    assertTrue(pom.contains("<dependencyManagement>"));
    assertTrue(pom.contains("<artifactId>vaadin-bom</artifactId>"));
  }

  @Test
  void productionConsumerIncludesAllDynamicBackendDependencies() throws Exception {
    try (var jar = new JarFile(System.getProperty("consumerJar"))) {
      var stats =
          new String(
              jar.getInputStream(jar.getEntry("META-INF/VAADIN/config/stats.json")).readAllBytes(),
              StandardCharsets.UTF_8);
      for (var dependency :
          new String[] {
            "vaadin-button.js",
            "vaadin-text-field.js",
            "vaadin-text-area.js",
            "vaadin-checkbox.js",
            "norm-layout.js"
          }) {
        assertTrue(stats.contains(dependency), dependency);
      }
      var updatedLayout = false;
      for (var entry : java.util.Collections.list(jar.entries())) {
        if (!entry.getName().endsWith(".js")) continue;
        var source = new String(jar.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8);
        if (source.contains("responsive-grid")) updatedLayout = true;
      }
      assertTrue(
          updatedLayout, "consumer bundle must contain the backend's current layout observer");
      assertNotNull(jar.getEntry("META-INF/LICENSE"));
    }
  }
}
