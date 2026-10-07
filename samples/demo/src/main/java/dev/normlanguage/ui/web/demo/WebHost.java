package dev.normlanguage.ui.web.demo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import java.util.concurrent.CountDownLatch;
import com.vaadin.flow.component.Component;
import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

public final class WebHost implements AutoCloseable {
  private final int port;
  private final Consumer<Component> factory;
  private ConfigurableApplicationContext context;
  private final CountDownLatch stopped = new CountDownLatch(1);
  private boolean started;

  public WebHost(int port, Consumer<Component> factory) {
    if (port < 1 || port > 65535) throw new IllegalArgumentException("port must be in 1..65535");
    this.port = port;
    this.factory = java.util.Objects.requireNonNull(factory);
  }

  public void run() throws InterruptedException {
    synchronized (this) {
      if (started) throw new IllegalStateException("Web host already started");
      started = true;
      var application = new SpringApplication(Application.class);
      application.addInitializers(
          ctx -> ctx.getBeanFactory().registerSingleton("normPages", new WebPages(factory)));
      context =
          application.run(
              "--server.port=" + port,
              "--vaadin.launch-browser=false",
              "--vaadin.productionMode=true");
    }
    try {
      stopped.await();
    } finally {
      close();
    }
  }

  public void close() {
    ConfigurableApplicationContext current;
    synchronized (this) {
      current = context;
      context = null;
      stopped.countDown();
    }
    if (current != null) current.close();
  }

  @Push
  @SpringBootApplication(scanBasePackageClasses = WebHost.class)
  public static class Application implements AppShellConfigurator {}
}
