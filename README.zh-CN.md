# ui.web

[English](README.md)

[Norm UI](https://github.com/normlanguage/ui) 的 Vaadin 后端。布局、状态、节点复用和组件作用域由 `ui` 提供，颜色由 [ui.theme](https://github.com/normlanguage/ui.theme) 提供。

## 在应用中挂载

在容器的 attach 回调中调用 `WebApplication(container: container, widget: MyWidget())`。返回的资源支持显式卸载；容器 detach 时自动释放渲染器和组件作用域。每个挂载独立管理渲染器和调度器，并使用所属 Vaadin Session 的锁。

Java 应用使用 `WebContainer` 作为容器，或在现有 Vaadin 路由上声明 `@Uses(WebContainer.class)`。它统一声明动态创建控件所需的前端依赖。Norm 对应绑定为 `ui.web.native.Container`。公开绑定和依赖版本以 [module.norm](ui/web/module.norm) 为准。

后端按 Vaadin add-on 打包。路由、AppShell、Push、Spring Boot 和生产前端 bundle 由应用拥有。[示例应用](samples/demo) 提供这些配置并挂载普通 Norm 组件。

## 运行示例

使用 JDK 25 和匹配的 Norm 工具链，通过 `NORM_EXECUTABLE` 指定启动器；依赖版本见模块声明。

```powershell
./scripts/prepare.ps1
./scripts/norm.ps1 run samples/demo
```

打开 [localhost:8080](http://localhost:8080)。示例展示输入绑定、列表节点复用、主题切换、组件释放及响应式布局。另开一个浏览器标签页即可验证独立状态。

[prepare.ps1](scripts/prepare.ps1) 构建并校验已提交的产物指纹。明确更新 Java 产物时运行 [update-artifacts.ps1](scripts/update-artifacts.ps1)。前端源码变更后先重建示例 bundle：

```powershell
./gradlew.bat :demo:vaadinBuildFrontend -PrebuildFrontend=true --rerun-tasks
./gradlew.bat :demo:clean :demo:publish
./scripts/update-artifacts.ps1
```

[生产 bundle](samples/demo/src/main/bundles/prod.bundle) 按 [Vaadin 源码管理规范](https://vaadin.com/docs/latest/flow/configuration/source-control) 提交。

## 源码索引

| 职责 | 唯一来源 |
| --- | --- |
| 模块身份与公开绑定 | [module.norm](ui/web/module.norm) |
| 后端入口 | [backend.norm](ui/web/backend.norm) |
| 内部节点与主题映射 | [backend_nodes.norm](ui/web/backend_nodes.norm) |
| 通用布局映射 | [layout.norm](ui/web/layout.norm) |
| 挂载与调度 | [application.norm](ui/web/application.norm) |
| 原生组件扩展 | [native.norm](ui/web/native.norm) |
| Session 生命周期 | [WebSession](src/main/java/dev/normlanguage/ui/web/WebSession.java) |
| 前端依赖边界 | [WebContainer](src/main/java/dev/normlanguage/ui/web/WebContainer.java) |
| 客户端布局观察 | [norm-layout.js](src/main/frontend/norm-layout.js) |
| 示例应用 | [samples/demo](samples/demo) |

## 验证

```powershell
./gradlew.bat :test --tests 'dev.normlanguage.ui.web.WebNodeTest' --tests 'dev.normlanguage.ui.web.WebSessionTest' --tests 'dev.normlanguage.ui.web.LibraryPackagingTest' :consumerTest
./scripts/norm.ps1 test ui/web --filter ui.web.test.rendering
./scripts/norm.ps1 check samples/demo
```

[Java 测试](src/test/java/dev/normlanguage/ui/web) 使用真实 Vaadin 对象与打包产物。[Norm 测试](ui/web/tests) 验证通用渲染器和原生扩展。[CI](.github/workflows/package.yml) 校验发布产物和示例。

采用 [MPL 2.0](LICENSE)。
