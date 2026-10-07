# ui.web

Vaadin Flow backend for [Norm UI](https://github.com/normlanguage/ui). Applications use ordinary `Widget` objects, fields and `Binding`; the shared renderer owns reconciliation and resource scopes. Each browser UI mounts its own page and renderer.

Module identity, dependencies and native bindings are defined in [module.norm](ui/web/module.norm). Theme values come from [ui.theme](https://github.com/normlanguage/ui.theme). This package has no JavaFX dependency.

## Run the example

Use JDK 25 and a matching Norm toolchain. Set `NORM_EXECUTABLE` to the compiler launcher when it is not built in a sibling `Norm` checkout. Prerequisite package versions are specified by the module declaration.

```powershell
./scripts/prepare.ps1
./scripts/norm.ps1 check samples/demo
./scripts/norm.ps1 run samples/demo
```

Open [localhost:8080](http://localhost:8080). The [example](samples/demo/application.norm) demonstrates ordinary field updates, two-way editing, keyed reordering, theme switching and mounting/disposal. Open another tab to exercise independent page state.

The [build](build.gradle.kts) uses Vaadin's production frontend pipeline and includes the resulting assets in the Java artifact. The provided [host](src/main/java/dev/normlanguage/ui/web/WebHost.java) uses Spring Boot and Vaadin Flow push. [WebApplication](ui/web/application.norm) connects each host session to its widget renderer. No separate frontend development server is required by the packaged example.

## Source index

| Concern | Source |
| --- | --- |
| Backend and basic elements | [backend.norm](ui/web/backend.norm) |
| Shared layout projection | [layout.norm](ui/web/layout.norm) |
| Window-independent mount and scheduler | [application.norm](ui/web/application.norm) |
| Custom Vaadin components | [native.norm](ui/web/native.norm) |
| Session locking and cleanup | [WebSession](src/main/java/dev/normlanguage/ui/web/WebSession.java) |
| Native node and event ownership | [WebNode](src/main/java/dev/normlanguage/ui/web/WebNode.java) |
| Browser layout observation | [norm-layout.js](src/main/frontend/norm-layout.js) |

## Verification

[Java tests](src/test/java/dev/normlanguage/ui/web) exercise real Vaadin nodes and sessions. [Norm tests](ui/web/tests) mount the shared renderer on the actual backend, including native extension and lifecycle behavior. CI is defined in [package.yml](.github/workflows/package.yml).

```powershell
./gradlew.bat test --tests 'dev.normlanguage.ui.web.WebNodeTest' --tests 'dev.normlanguage.ui.web.WebSessionTest'
./scripts/norm.ps1 test ui/web --filter ui.web.test.rendering
```
