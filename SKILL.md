# ui.web Skill

Use this skill to work with Norm module `ui.web` version 4. The module declaration at [ui/web/module.norm](ui/web/module.norm) is the single source of truth for exports, dependencies, and Java bindings.

## Public API

The module exports the declarations in `ui/web/backend.norm`, `ui/web/application.norm`, `ui/web/layout.norm`, and `ui/web/native.norm`:

- `backend`: web renderer and widget integration built on `ui`.
- `application`: application mounting and lifecycle APIs.
- `native.UI`, `native.Container`, `native.Component`, and `native.Span`: Vaadin native bindings.

Use `WebApplication(container: native.Container, widget: ui.Widget)` from an attach callback. A mount owns its renderer and scheduler and must be explicitly unmounted when its resource handle is no longer needed. `native.Container` is implemented by `dev.normlanguage.ui.web.WebContainer` and is also available to Java routes through `@Uses(WebContainer.class)`.

## Java bindings

`internal.WebNode`, `internal.WebSession`, `native.Component`, `native.Span`, `native.UI`, `native.Container`, and `internal.Registration` are declared in `ui/web/module.norm`. Do not add duplicate aliases or bypass the module boundary; update the module declaration and regenerate this file when the public API changes.

## Verification

Use the repository scripts and the checks defined in `.github/workflows/package.yml`. The consumer example in `samples/demo` is the runtime acceptance path. Keep package versions aligned with `ui` 10 and `ui.theme` 4.
