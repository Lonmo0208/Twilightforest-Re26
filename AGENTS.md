# 暮色森林 Re26 模组移植方案（Neoforge → Fabric）

> **目标**：将暮色森林（Twilight Forest）Re26 版本从 Neoforge API 完整迁移至 Fabric API，确保功能、资源与游戏表现一致。

---

## 1. 环境与前置准备

- **移植基准**：基于项目目录下的 Fabric 模组模板 `.\twilightforest-template-26.1.2` 搭建基础工程。
- **关键依赖源码**（用于参考与调试）：
	- Fabric API：`E:\MC源码对比\Fabric-26.1.2\fabric-api-26.1.2`
	- Fabric Loader：`E:\MC源码对比\Fabric-26.1.2\fabric-loader-26.1.2`
	
- **原版 Minecraft 26.1.2 源码**（用于底层逻辑对照）：
  `E:\MC源码对比\26.1.2-base`
  *说明*：当需要深入理解某些原生类行为时，可查阅此路径，但不要直接复制原版代码到模组中。

- **非官方 Fabric 移植版**（仅供参考思路,毕竟是老Fabric移植的，但是还是可以多看看！！！！）：
  `E:\MC源码对比\twilightforest-fabric-1.21.1`[这个最接近，或许可以多多看！]
  `E:\MC源码对比\twilightforest-unofficial-1.20.1-fabric`
 
  
- **开发环境**：确保 JDK 25+、Gradle 9+、IntelliJ IDEA 或 VSCode 已配置好 Fabric 开发环境。

---

## 2. 构建配置迁移（`.gradle` 及相关文件）

- 对照 Fabric 模板中的 `build.gradle`、`settings.gradle`、`gradle.properties`，替换 Neoforge 原有的构建脚本。
- 调整依赖声明：
	- 将 `neoform` `neoforge`替换为 `fabric-loader` + `fabric-api`。
- 迁移资源处理（`runData` 对应逻辑见第 4 节）。

---

## 3. 代码迁移策略（Neoforge → Fabric 对照）

- **核心思路**：逐模块对比 Neoforge 与 Fabric 的 API 差异，寻找等效实现，而非简单复制。
- **参考方法**：
	- 将 Neoforge 的事件总线（`EventBus`）替换为 Fabric 的 `ModEvents` 或回调接口（如 `EntityEvents`、`BlockEvents`）。
	- 注册方式：Neoforge 的 `DeferredRegister` → Fabric 的 `Registry` + `Registerable`。
	- 网络通信：Neoforge 的 `NetworkRegistry` → Fabric 的 `Networking` 包。
	- 世界生成（Feature/Structure）：Neoforge 的 `ConfiguredFeature` → Fabric 的 `StructureFeature` 及 `BiomeModifications`。
- **实操流程**：
	1. 将原模组源码复制到 Fabric 工程，按包结构调整。
	2. 修复所有 import 引用，逐步替换为 Fabric 对应的类。
	3. 针对缺失的 API，通过阅读 Fabric API 源码（上述路径）查找替代方案或自行实现桥接。

---

## 4. 关键机制：数据生成（`runData` → Fabric 等效）[不确定，你看着来]
> **❗ 强制约束**：**绝对禁止在移植项目中创建任何 Neoforge 专有类**（包括但不限于继承 `net.neoforged.*` 的类、实现其接口，或引入任何 Neoforge 依赖包）。所有功能必须通过 Fabric API 或 Minecraft 原生类实现。若遇到无法直接替换的功能，应寻找 Fabric 等效方案或自行编写兼容层，但不得以任何形式引入 Neoforge 的包路径或类。

- **Neoforge 的 `runData`**：用于通过 Gradle 任务执行数据生成（如配方、战利品表、标签等），依赖 `DataGenerator`。
- **Fabric 对应方案**：
	- Fabric 没有内置的 `runData` 任务，但可通过 **Fabric Data Generation API**（`fabric-data-generation-api-v1`）实现。
	- 需要实现 `DataGeneratorEntrypoint` 接口，并在 `fabric.mod.json` 中声明。
	- 添加 Gradle 任务（如 `runDatagen`）调用生成器，输出至 `src/main/generated`，并配置资源路径。
- **移植要点**：将原 Neoforge 的 `DataProvider` 实现改写为 Fabric 的 `DataProvider`（如 `RecipeProvider` → `FabricRecipeProvider`）。

---

## 5. 补充建议（移植过程中的最佳实践）
- **日志与调试**：善用 Fabric 的 `DEBUG功能`，对比 Neoforge 与 Fabric 的日志输出，定位差异。
- **性能考量**：Fabric 更轻量，但某些 Neoforge 优化（如缓存）需手动适配；注意 `@Environment` 注解的正确使用。
- **文档与注释**：为每次 API 替换撰写注释，便于后续维护和团队协作。

---

## 6. 最终验证与发布

- 完成全部代码迁移后，执行完整游戏测试（生存模式流程、多人联机兼容性）。
- 打包 `jar` 并在干净客户端安装，验证无崩溃、无资源缺失。

---

> **总结**：本次移植的关键在于 **理解 Neoforge 的实现意图**，并在 Fabric 生态中找到对等替代，而非机械翻译。借助 Fabric 源码与模板工程，可大幅减少试错成本。如有未知阻塞点，建议查阅 Fabric 官方 Wiki 或社区讨论。
>
> > **❗ 强制约束**：**绝对禁止在移植项目中创建任何 Neoforge 专有类**（包括但不限于继承 `net.neoforged.*` 的类、实现其接口，或引入任何 Neoforge 依赖包）。所有功能必须通过 Fabric API 或 Minecraft 原生类实现。若遇到无法直接替换的功能，应寻找 Fabric 等效方案或自行编写兼容层，但不得以任何形式引入 Neoforge 的包路径或类。
> > **❗ 强制约束**：**绝对禁止在移植项目中创建任何 Neoforge 专有类**（包括但不限于继承 `net.neoforged.*` 的类、实现其接口，或引入任何 Neoforge 依赖包）。所有功能必须通过 Fabric API 或 Minecraft 原生类实现。若遇到无法直接替换的功能，应寻找 Fabric 等效方案或自行编写兼容层，但不得以任何形式引入 Neoforge 的包路径或类。
> > **❗ 强制约束**：**绝对禁止在移植项目中创建任何 Neoforge 专有类**（包括但不限于继承 `net.neoforged.*` 的类、实现其接口，或引入任何 Neoforge 依赖包）。所有功能必须通过 Fabric API 或 Minecraft 原生类实现。若遇到无法直接替换的功能，应寻找 Fabric 等效方案或自行编写兼容层，但不得以任何形式引入 Neoforge 的包路径或类。