# ui.web Skill

Use this skill to work with Norm module `ui.web` version 5. Use only declarations listed below.

## `ui.web`

Source: `JavaArrays.norm`

### `JavaComponentArray`

```norm
class JavaComponentArray
```

#### `JavaComponentArray`

```norm
JavaComponentArray(JavaComponentArrayBindingToken token)
```

Parameters:

- `token`: `JavaComponentArrayBindingToken`

#### `size`

```norm
Integer size()
```

Returns: `Integer`

#### `get`

```norm
Component? get(Integer index)
```

Parameters:

- `index`: `Integer`

Returns: `Component?`

#### `set`

```norm
Void set(Integer index, Component? value)
```

Parameters:

- `index`: `Integer`
- `value`: `Component?`

Returns: `Void`

### `javaComponentArrayNew`

```norm
JavaComponentArray javaComponentArrayNew(Integer size)
```

Parameters:

- `size`: `Integer`

Returns: `JavaComponentArray`

## `ui.web`

Source: `application.norm`

### `WebApplication`

```norm
class WebApplication implements Resource
```

#### `WebApplication`

```norm
WebApplication(Component container, Widget widget)
```

Parameters:

- `container`: `Component`
- `widget`: `Widget`

#### `close`

```norm
Void close()
```

Returns: `Void`

## `ui.web`

Source: `backend.norm`

### `WebBackend`

```norm
class WebBackend implements LayoutBackend
```

#### `createLayout`

```norm
RenderNode createLayout(LayoutSpec specification)
```

Parameters:

- `specification`: `LayoutSpec`

Returns: `RenderNode`

#### `create`

```norm
RenderNode create(ViewContent content)
```

Parameters:

- `content`: `ViewContent`

Returns: `RenderNode`

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/AttachNotifier.norm`

### `AttachNotifier`

```norm
interface AttachNotifier extends Serializable
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/ClickNotifier.norm`

### `ClickNotifier`

```norm
interface ClickNotifier<T extends Component> extends Serializable
```

Type parameters:

- `T` extends `Component`

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/DetachNotifier.norm`

### `DetachNotifier`

```norm
interface DetachNotifier extends Serializable
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasComponents.norm`

### `HasComponents`

```norm
interface HasComponents extends HasComponentsOfType<Component>
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasComponentsOfType.norm`

### `HasComponentsOfType`

```norm
interface HasComponentsOfType<T extends Component> extends HasElement, HasEnabled
```

Type parameters:

- `T` extends `Component`

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasElement.norm`

### `HasElement`

```norm
interface HasElement extends Serializable
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasEnabled.norm`

### `HasEnabled`

```norm
interface HasEnabled extends HasElement
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasOrderedComponents.norm`

### `HasOrderedComponents`

```norm
interface HasOrderedComponents extends HasComponents
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasSize.norm`

### `HasSize`

```norm
interface HasSize extends HasElement
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasStyle.norm`

### `HasStyle`

```norm
interface HasStyle extends HasElement
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HasText.norm`

### `HasText`

```norm
interface HasText extends HasElement
```

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HtmlComponent.norm`

### `HtmlComponent`

```norm
class HtmlComponent extends Component implements HasSize, HasStyle, AttachNotifier, DetachNotifier
```

#### `HtmlComponent`

```norm
HtmlComponent(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/HtmlContainer.norm`

### `HtmlContainer`

```norm
class HtmlContainer extends HtmlComponent implements HasComponents, HasText, HasSize, HasStyle, AttachNotifier, DetachNotifier
```

#### `HtmlContainer`

```norm
HtmlContainer(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

## `ui.web.com.vaadin.flow.component`

Source: `com/vaadin/flow/component/PollNotifier.norm`

### `PollNotifier`

```norm
interface PollNotifier extends Serializable
```

## `ui.web.com.vaadin.flow.component.html`

Source: `com/vaadin/flow/component/html/Div.norm`

### `Div`

```norm
class Div extends HtmlContainer implements ClickNotifier<Div>, HasOrderedComponents, HasComponents, HasText, HasSize, HasStyle, AttachNotifier, DetachNotifier
```

#### `Div`

```norm
Div(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

## `ui.web.com.vaadin.flow.router`

Source: `com/vaadin/flow/router/RouterLayout.norm`

### `RouterLayout`

```norm
interface RouterLayout extends HasElement
```

## `ui.web.internal`

Source: `internal/Registration.norm`

### `Registration`

```norm
interface Registration extends Serializable
```

#### `remove`

```norm
Void remove()
```

Returns: `Void`

## `ui.web.internal`

Source: `internal/WebNode.norm`

### `WebNode`

```norm
class WebNode implements AutoCloseable
```

#### `WebNode`

```norm
WebNode(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

#### `breakpoint`

```norm
Void breakpoint(Double arg0, Integer arg1)
```

Parameters:

- `arg0`: `Double`
- `arg1`: `Integer`

Returns: `Void`

#### `checked`

```norm
Void checked(Boolean arg0)
```

Parameters:

- `arg0`: `Boolean`

Returns: `Void`

#### `children`

```norm
Void children(JavaList<WebNode>? arg0)
```

Parameters:

- `arg0`: `JavaList<WebNode>?`

Returns: `Void`

#### `clear`

```norm
Void clear()
```

Returns: `Void`

#### `close`

```norm
Void close()
```

Returns: `Void`

#### `direction`

```norm
Void direction(Boolean arg0)
```

Parameters:

- `arg0`: `Boolean`

Returns: `Void`

#### `enabled`

```norm
Void enabled(Boolean arg0)
```

Parameters:

- `arg0`: `Boolean`

Returns: `Void`

#### `focus`

```norm
Void focus()
```

Returns: `Void`

#### `grid`

```norm
Void grid(Integer arg0)
```

Parameters:

- `arg0`: `Integer`

Returns: `Void`

#### `gridSpan`

```norm
Void gridSpan(Integer arg0)
```

Parameters:

- `arg0`: `Integer`

Returns: `Void`

#### `identity`

```norm
Void identity(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

#### `layout`

```norm
Void layout(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

#### `layoutStyle`

```norm
Void layoutStyle(String? arg0, String? arg1)
```

Parameters:

- `arg0`: `String?`
- `arg1`: `String?`

Returns: `Void`

#### `masonry`

```norm
Void masonry(Double arg0, Double arg1)
```

Parameters:

- `arg0`: `Double`
- `arg1`: `Double`

Returns: `Void`

#### `multiline`

```norm
Void multiline(Integer arg0, Boolean arg1)
```

Parameters:

- `arg0`: `Integer`
- `arg1`: `Boolean`

Returns: `Void`

#### `nativeComponent`

```norm
Component? nativeComponent()
```

Returns: `Component?`

#### `onAction`

```norm
Void onAction(Function<Void()>? arg0)
```

Parameters:

- `arg0`: `Function<Void()>?`

Returns: `Void`

#### `onText`

```norm
Void onText(Function<Void(String?)>? arg0)
```

Parameters:

- `arg0`: `Function<Void(String?)>?`

Returns: `Void`

#### `onToggle`

```norm
Void onToggle(Function<Void(Boolean?)>? arg0)
```

Parameters:

- `arg0`: `Function<Void(Boolean?)>?`

Returns: `Void`

#### `placeholder`

```norm
Void placeholder(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

#### `readText`

```norm
String? readText()
```

Returns: `String?`

#### `responsive`

```norm
Void responsive()
```

Returns: `Void`

#### `style`

```norm
Void style(String? arg0, String? arg1)
```

Parameters:

- `arg0`: `String?`
- `arg1`: `String?`

Returns: `Void`

#### `text`

```norm
Void text(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

#### `theme`

```norm
Void theme(JavaMap<String, String>? arg0)
```

Parameters:

- `arg0`: `JavaMap<String, String>?`

Returns: `Void`

#### `title`

```norm
Void title(Boolean arg0)
```

Parameters:

- `arg0`: `Boolean`

Returns: `Void`

### `webNodeNew`

```norm
WebNode webNodeNew(Component? arg0)
```

Parameters:

- `arg0`: `Component?`

Returns: `WebNode`

### `webNodeNew`

```norm
WebNode webNodeNew(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `WebNode`

## `ui.web.internal`

Source: `internal/WebSession.norm`

### `WebSession`

```norm
class WebSession implements AutoCloseable
```

#### `WebSession`

```norm
WebSession(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

#### `close`

```norm
Void close()
```

Returns: `Void`

#### `later`

```norm
Void later(Function<Void()>? arg0)
```

Parameters:

- `arg0`: `Function<Void()>?`

Returns: `Void`

#### `onClose`

```norm
Registration? onClose(Function<Void()>? arg0)
```

Parameters:

- `arg0`: `Function<Void()>?`

Returns: `Registration?`

#### `report`

```norm
Void report(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

#### `root`

```norm
Void root(WebNode? arg0)
```

Parameters:

- `arg0`: `WebNode?`

Returns: `Void`

### `webSessionNew`

```norm
WebSession webSessionNew(Component? arg0)
```

Parameters:

- `arg0`: `Component?`

Returns: `WebSession`

## `ui.web`

Source: `native.norm`

### `nativeComponent`

```norm
View nativeComponent<T>(String kind, Function<T()> create, Function<Component(T)> component, Function<Void(T, ViewScope)> update, Function<Void(T)> close, List<Widget> children, Function<Void(T, List<Component>)>? arrange, NodeKey? key, Boolean enabled)
```

Type parameters:

- `T`

Parameters:

- `kind`: `String`
- `create`: `Function<T()>`
- `component`: `Function<Component(T)>`
- `update`: `Function<Void(T, ViewScope)>`
- `close`: `Function<Void(T)>`
- `children`: `List<Widget>`
- `arrange`: `Function<Void(T, List<Component>)>?`
- `key`: `NodeKey?`
- `enabled`: `Boolean`

Returns: `View`

## `ui.web.native`

Source: `native/Component.norm`

### `Component`

```norm
class Component implements HasStyle, AttachNotifier, DetachNotifier
```

#### `Component`

```norm
Component(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

## `ui.web.native`

Source: `native/Container.norm`

### `Container`

```norm
class Container extends Div implements ClickNotifier<Div>, HasOrderedComponents, HasComponents, HasText, HasSize, HasStyle, AttachNotifier, DetachNotifier
```

#### `Container`

```norm
Container(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

### `containerNew`

```norm
Container containerNew()
```

Returns: `Container`

## `ui.web.native`

Source: `native/Span.norm`

### `Span`

```norm
class Span extends HtmlContainer implements ClickNotifier<Span>, HasComponents, HasText, HasSize, HasStyle, AttachNotifier, DetachNotifier
```

#### `Span`

```norm
Span(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`

#### `getText`

```norm
String? getText()
```

Returns: `String?`

#### `setText`

```norm
Void setText(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Void`

### `spanNew`

```norm
Span spanNew()
```

Returns: `Span`

### `spanNew`

```norm
Span spanNew(Function<String?()>? arg0)
```

Parameters:

- `arg0`: `Function<String?()>?`

Returns: `Span`

### `spanNew`

```norm
Span spanNew(String? arg0)
```

Parameters:

- `arg0`: `String?`

Returns: `Span`

### `spanNew`

```norm
Span spanNew(JavaComponentArray? arg0)
```

Parameters:

- `arg0`: `JavaComponentArray?`

Returns: `Span`

## `ui.web.native`

Source: `native/UI.norm`

### `UI`

```norm
class UI extends Component implements PollNotifier, HasComponents, RouterLayout, HasStyle, AttachNotifier, DetachNotifier
```

#### `UI`

```norm
UI(__JarBindingToken token)
```

Parameters:

- `token`: `__JarBindingToken`
