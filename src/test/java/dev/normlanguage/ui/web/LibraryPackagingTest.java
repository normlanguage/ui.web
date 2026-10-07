package dev.normlanguage.ui.web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.jar.JarFile;
import org.junit.jupiter.api.Test;

class LibraryPackagingTest {
  @Test
  void backendIsAnAddonWithConsumerOwnedApplicationMetadata() throws Exception {
    try (var jar = new JarFile(System.getProperty("libraryJar"))) {
      assertNotNull(jar.getEntry("META-INF/LICENSE"));
      assertNotNull(jar.getEntry("META-INF/frontend/norm-layout.js"));
      assertNull(jar.getEntry("META-INF/VAADIN/config/flow-build-info.json"));
      assertNull(jar.getEntry("META-INF/VAADIN/config/stats.json"));
      assertNull(jar.getEntry("dev/normlanguage/ui/web/WebHost.class"));
    }
    assertThrows(
        ClassNotFoundException.class,
        () -> Class.forName("org.springframework.boot.SpringApplication"));
  }
}
