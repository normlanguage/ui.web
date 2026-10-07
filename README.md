# ui.web

[中文](README.zh-CN.md)

Vaadin backend for [Norm UI](https://github.com/normlanguage/ui). Layout, state, reconciliation and widget scopes come from `ui`; colors come from [ui.theme](https://github.com/normlanguage/ui.theme).

## Mount in an application

Use `WebApplication(container: container, widget: MyWidget())` from the container's attach callback. Its resource handle supports explicit unmounting; detaching the container releases the renderer and widget scopes automatically. Each mount owns its renderer and scheduler while using the enclosing Vaadin session lock.

Use `WebContainer` as the Java application's container, or declare `@Uses(WebContainer.class)` on an existing Vaadin route. It declares the dynamic backend's frontend dependencies. Norm exposes it as `ui.web.native.Container`. Public bindings and dependency versions are defined in [module.norm](ui/web/module.norm).

The backend is a Vaadin add-on. Applications own routes, AppShell, Push, Spring Boot and their production frontend bundle. The [consumer example](samples/demo) owns these settings and mounts ordinary Norm widgets.

## Run the consumer example

Use JDK 25 and a matching Norm toolchain. Set `NORM_EXECUTABLE` to its launcher; required package versions are specified by the module declaration.

```powershell
./scripts/prepare.ps1
./scripts/norm.ps1 run samples/demo
```

Open [localhost:8080](http://localhost:8080). The example covers input binding, keyed reordering, theme changes, widget disposal and responsive layouts. A second browser tab mounts independent widget state.

[prepare.ps1](scripts/prepare.ps1) builds and verifies committed artifact resolutions. To deliberately update Java artifacts, run [update-artifacts.ps1](scripts/update-artifacts.ps1). After frontend changes, rebuild the consumer bundle first:

```powershell
./gradlew.bat :demo:vaadinBuildFrontend -PrebuildFrontend=true --rerun-tasks
./gradlew.bat :demo:clean :demo:publish
./scripts/update-artifacts.ps1
```

The [production bundle](samples/demo/src/main/bundles/prod.bundle) follows [Vaadin source control guidance](https://vaadin.com/docs/latest/flow/configuration/source-control).

## Source index

| Concern | Source |
| --- | --- |
| Module identity and public bindings | [module.norm](ui/web/module.norm) |
| Backend facade | [backend.norm](ui/web/backend.norm) |
| Internal nodes and theme projection | [backend_nodes.norm](ui/web/backend_nodes.norm) |
| Shared layout projection | [layout.norm](ui/web/layout.norm) |
| Mount and scheduler | [application.norm](ui/web/application.norm) |
| Native component integration | [native.norm](ui/web/native.norm) |
| Session lifecycle | [WebSession](src/main/java/dev/normlanguage/ui/web/WebSession.java) |
| Frontend dependency boundary | [WebContainer](src/main/java/dev/normlanguage/ui/web/WebContainer.java) |
| Client layout observation | [norm-layout.js](src/main/frontend/norm-layout.js) |
| Consumer application | [samples/demo](samples/demo) |

## Verify

```powershell
./gradlew.bat :test --tests 'dev.normlanguage.ui.web.WebNodeTest' --tests 'dev.normlanguage.ui.web.WebSessionTest' --tests 'dev.normlanguage.ui.web.LibraryPackagingTest' :consumerTest
./scripts/norm.ps1 test ui/web --filter ui.web.test.rendering
./scripts/norm.ps1 check samples/demo
```

[Tests](src/test/java/dev/normlanguage/ui/web) exercise real Vaadin objects and packaged artifacts. [Norm tests](ui/web/tests) exercise the shared renderer and native extensions. [CI](.github/workflows/package.yml) verifies the release artifact and consumer.

Licensed under [MPL 2.0](LICENSE).
