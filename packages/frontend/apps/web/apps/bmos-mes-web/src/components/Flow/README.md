# Flow 流程图组件

> 路径:`packages/frontend/apps/web/apps/bmos-mes-web/src/components/Flow/` 入口:`import Flow from '@/components/Flow'`

基于 **AntV X6 图编辑引擎**封装的 Vue 3 + TypeScript 流程图编排组件,用于在 MES 系统中可视化地**绘制与编辑工艺流程、审批
流程、工序流转**等有向图。

提供开箱即用的能力:

- 左侧拖拽栏(可自定义节点类型)拖入节点
- 顶部工具栏(撤销 / 重做 / 缩放 / 适应画布 / 删除)
- 内置 5 种节点形状(开始、结束、工序、任务、网关)
- 连线校验、节点选中、删除联动、撤销重做、键盘快捷键
- 数据 `JSON` 双向导入导出,支持编辑 / 只读两种模式

---

## 一、技术栈与依赖

| 依赖                                                                               | 作用                                                                                          |
| ---------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| `@antv/x6`                                                                         | 图编辑核心(Graph / Cell / Node / Edge / Shape)                                                |
| `@antv/x6-vue-shape`                                                               | 将 Vue 组件注册为 X6 节点形状                                                                 |
| `@antv/x6-plugin-dnd`                                                              | 拖拽生成节点                                                                                  |
| `@antv/x6-plugin-{selection,snapline,keyboard,clipboard,history,export,transform}` | 框选、对齐线、快捷键、剪贴板、历史栈、导出、节点缩放                                          |
| `ant-design-vue`                                                                   | Divider / Space / Tooltip / message                                                           |
| `@bmos/components`(`BMIcon`)、`@bmos/i18n`(`t`)、`@bmos/utils`                     | 图标、国际化、工具函数                                                                        |
| `unplugin-auto-import`                                                             | `nextTick` / `onActivated` / `computed` 等**自动导入**(见[注意事项](#十四注意事项与已知问题)) |

## 二、目录结构

```
Flow/
├── flow.vue              # 主组件:模板 + 初始化 + 组装 hooks
├── index.ts              # 导出入口(默认导出 Flow,具名导出 Flow)
├── type/
│   ├── flow.ts           # flowProps(属性) + flowEmits(事件) + 类型
│   ├── enum.ts           # FlowNodeEnum:节点形状枚举
│   └── toolBar.ts        # FlowLeftToolBar:左侧工具栏项类型
├── hooks/
│   ├── useFlowState.ts   # 状态:画布/端口/节点默认配置、fromJSON、isView
│   ├── useFlowMethods.ts # 方法:拖拽、缩放、增删、数据读写等
│   ├── useEventListener.ts # 事件:插件装载、快捷键、节点/边/连线监听
│   └── useFlowContext.ts # provide/inject 全局上下文(供子组件取实例)
└── components/
    ├── BasicNode.vue     # 工序/任务节点(图标+名称+设置/下一步按钮)
    ├── StartNode.vue     # 开始节点
    ├── EndNode.vue       # 结束节点
    ├── GatewayNode.vue   # 网关节点(菱形 + 号)
    ├── LeftToolbar.vue   # 左侧拖拽栏
    └── TopToolBar.vue    # 顶部工具栏
```

## 三、核心概念:节点类型(FlowNodeEnum)

定义于 `type/enum.ts`,同时是 X6 注册的 `shape` 名:

| 枚举      | shape 值                  | 说明                                               | 渲染组件      |
| --------- | ------------------------- | -------------------------------------------------- | ------------- |
| `START`   | `custom-vue-start-node`   | 开始节点(绿色,不可删除 / 不可作为连线目标)         | StartNode     |
| `END`     | `custom-vue-end-node`     | 结束节点(红色,不可作为连线源)                      | EndNode       |
| `CUSTOM`  | `custom-vue-node`         | **工序节点**(默认,带"设置"和"下一步"按钮)          | BasicNode     |
| `TASK`    | `custom-task-node`        | 任务节点(同样用 BasicNode,但图标 / 显隐可独立配置) | BasicNode     |
| `GATEWAY` | `custom-vue-gateway-node` | 控制器 / 网关(菱形)                                | GatewayNode   |
| `EDGE`    | `edge`                    | 连线                                               | X6 Shape.Edge |

初始化时默认放置一个"开始"和一个"结束"节点(`initGraph`)。

---

## 四、Props 属性

声明于 `type/flow.ts` 的 `flowProps`。

### 数据与模式

| 属性                | 类型      | 默认值  | 说明                                                           |
| ------------------- | --------- | ------- | -------------------------------------------------------------- |
| `modalJson`         | `Array`   | `[]`    | 画布 JSON 数据,传入即回显(对应 X6 `graph.fromJSON`),深监听变化 |
| `isView`            | `Boolean` | `false` | **只读模式**:禁用插件、交互、左侧栏与大部分工具栏操作          |
| `isOptionClickNode` | `Boolean` | `false` | 为 true 时,仅 `data.isAllowClick !== false` 的节点可被点击选中 |
| `connecting`        | `Object`  | `{}`    | 自定义 X6 连线配置(注意:非空时会**整体覆盖**默认 connecting)   |

### 节点图标与显隐(工序节点 CUSTOM)

| 属性           | 默认          | 说明                 |
| -------------- | ------------- | -------------------- |
| `leftIcon`     | `'Procedure'` | 左侧图标             |
| `nextIcon`     | `'Process2'`  | "下一步"按钮图标     |
| `showNextIcon` | `true`        | 是否显示"下一步"按钮 |
| `showSetIcon`  | `true`        | 是否显示"设置"按钮   |
| `showDivider`  | `true`        | 是否显示分割线       |

### 任务节点 TASK(独立于工序节点)

| 属性                                                       | 默认           | 说明             |
| ---------------------------------------------------------- | -------------- | ---------------- |
| `taskLeftIcon`                                             | `'Procedure2'` | 左侧图标         |
| `taskNextIcon`                                             | `'File'`       | "下一步"按钮图标 |
| `showTaskNextIcon` / `showTaskSetIcon` / `showTaskDivider` | `true`         | 对应显隐开关     |

### 工具栏显隐

| 属性                                                                                | 默认       | 说明                                                         |
| ----------------------------------------------------------------------------------- | ---------- | ------------------------------------------------------------ |
| `isShowLeftToolBar`                                                                 | `true`     | 显示左侧拖拽栏                                               |
| `isShowTopToolBar`                                                                  | `true`     | 显示顶部工具栏                                               |
| `leftMap`                                                                           | `[]`       | 左侧栏项配置,见 [`FlowLeftToolBar`](#五flowlefttoolbar-类型) |
| `isTransform`                                                                       | `true`     | 是否允许缩放节点尺寸(Transform 插件)                         |
| `notLimitStartOrEndTransform`                                                       | `false`    | 是否允许开始 / 结束节点也可缩放                              |
| `showUndo` / `showRedo` / `showReset` / `showZoomIn` / `showZoomOut` / `showDelete` | `true`     | 顶部各按钮显隐                                               |
| `mouseenter`                                                                        | `Function` | 自定义节点 hover 回调(默认 hover 显示连接桩)                 |

## 五、Emits 事件

声明于 `flowEmits`:

| 事件              | 参数            | 触发时机                                                                |
| ----------------- | --------------- | ----------------------------------------------------------------------- |
| `handleClickSet`  | `(cell, shape)` | 点击节点上的"设置"图标                                                  |
| `handleClickNext` | `(cell, shape)` | 点击节点上的"下一步"图标                                                |
| `nodeClick`       | `(cell)`        | 点击任意节点                                                            |
| `flowDataChange`  | `()`            | 画布发生任何变更(增删节点 / 边、连线、移动、缩放)                       |
| `graphRender`     | `(graph)`       | 画布初始化并完成事件绑定后,回传 X6 Graph 实例                           |
| `reset`           | `(formModel)`   | ⚠️ 已声明但代码中**未实际 emit**(见[注意事项](#十四注意事项与已知问题)) |

## 六、Expose 暴露的实例方法

主组件通过 `defineExpose(instance)` 把全部状态 / 方法对外暴露,可通过 `ref` 调用(类型:`FlowInstanceType`)。常用方法:

| 方法                                            | 说明                                                          |
| ----------------------------------------------- | ------------------------------------------------------------- |
| `getFlowData()`                                 | 导出画布 JSON(`graph.toJSON()`),用于保存                      |
| `getCellDataById(id)`                           | 读取某节点 `data`                                             |
| `updateFormValue(id, formValue)`                | 合并更新节点 data(深合并),`formValue.name` 会同步为节点 label |
| `updateCellData(id, formValue, key='formData')` | 更新 data 下指定子键(默认 `formData`),深合并                  |
| `updateCellDataValue(id, formValue)`            | 整体覆盖式更新节点 data                                       |
| `selectNodeById(id)`                            | 根据 id 选中并框选节点                                        |
| `deleteNode()`                                  | 删除当前选中(开始 / 结束节点不可删)                           |
| `undo()` / `redo()`                             | 撤销 / 重做                                                   |
| `reset()`                                       | 缩放回归 1.0 并居中                                           |
| `zoomIn()` / `zoomOut()`                        | 放大 / 缩小 0.2                                               |
| `dragstartNode(e, item)`                        | 触发拖拽创建节点(左侧栏内部使用)                              |
| `initGraph(cells?)`                             | 初始化 / 重置画布(默认放开始 + 结束)                          |
| `graph`                                         | X6 Graph 实例(`Ref<Graph>`)                                   |
| `register`                                      | X6 Vue-shape 的 `register`,用于注册自定义节点                 |

判断辅助:`isCustomNode` / `isNotStartOrEndCell` / `isNotGateway`。

## 七、Slots 插槽

| 插槽     | 作用域参数                 | 说明                                                |
| -------- | -------------------------- | --------------------------------------------------- |
| `custom` | `instance`(全部状态与方法) | 在画布之上叠加自定义内容(如自绘浮层),可拿到完整实例 |

---

## 八、内部架构与生命周期

`flow.vue` 把逻辑拆分到三个 hooks 并组合:

```
useFlowState        ── 默认配置(graph/port/node)、fromJSON、isView
   + useFlowMethods ── 操作方法(增删/缩放/数据读写)
   + useEventListener─ 装载插件、绑定快捷键与图事件
   => instance(全部合并) ── defineExpose + createFlowContext(provide)
```

**初始化流程(`initFn`,在 `onMounted` / `onActivated` 调用):**

1. 注册 5 个 Vue-shape(`custom-vue-node` 等)
2. 组装 `Graph.Options`:合并默认配置 + `interacting`(根据 `isView` 关闭连线 / 连线桩交互)+ `connecting`(可被 prop 覆盖)
3. 销毁旧实例 → `new Graph(options)` → `initGraph()`(放置开始 / 结束节点)
4. 若有 `fromJSON`(传入 `modalJson`)→ `graph.fromJSON()` → 延时 `reset()` 适应画布
5. `setEventListener()`:装载 7 个插件 + 绑定快捷键 + 绑定节点 / 边事件
6. `new Dnd({ target: graph })` 启用拖拽

**响应式:**`watch(fromJSON, deep)` 在外部数据变化时重新 `fromJSON` + `reset`;`useFlowState` 内
`watch(props.modalJson, immediate, deep)` 同步到 `fromJSON`。

**`keep-alive` 支持:**`onActivated` 时重新生成 `flowKey`(`Math.random()` 强制重渲染 DOM)并再次 `initFn()`,说明该组件**预
期被 `<keep-alive>` 包裹**。

**卸载:**`onUnmounted` 调用 `graph.dispose()` 释放资源。

## 九、连接校验规则(`validateConnection`)

默认 connecting 中内置的连线合法性校验:

- 必须从"输出链接桩"起、连到"输入链接桩"
- 禁止连接到自身(同节点)
- 禁止以开始节点(`start-*-port`)作为目标
- 禁止以结束节点(`end-*-port`)作为源
- 路由:`manhattan`(曼哈顿路由);连接器:`rounded`(圆角);支持吸附、允许多连线、禁止自环

> ⚠️ 若传入 `connecting` prop,**会整体覆盖**上述默认规则(含 `validateConnection`),需自行实现校验。

## 十、键盘快捷键

| 快捷键                 | 行为                          |
| ---------------------- | ----------------------------- |
| `Ctrl/Cmd + Z`         | 撤销                          |
| `Ctrl/Cmd + Shift + Z` | 重做                          |
| `Ctrl/Cmd + 1`         | 放大 0.1(上限 1.5)            |
| `Ctrl/Cmd + 2`         | 缩小 0.1(下限 0.5)            |
| `Delete` / `Backspace` | 删除选中(开始 / 结束节点除外) |

剪贴板复制 / 剪切 / 粘贴(`Ctrl+C/X/V`)代码已写好但**被注释关闭**。

## 十一、删除联动(`cell:removed`)

删除节点或边时会做网关级联清理:

- 删除**边**时,若其 target 是网关 → 一并删除该网关
- 删除**节点**时,查找以其为 source 的所有边,若这些边的 target 是网关 → 一并删除

---

## 十二、使用方法

### 1. 最简用法

```vue
<script setup lang="ts">
  import Flow from '@/components/Flow';
  import { FlowNodeEnum } from '@/components/Flow/type';
  import type { FlowLeftToolBar } from '@/components/Flow/type/toolBar';
  import { BMIcon } from '@bmos/components';

  // 左侧可拖入的节点
  const leftMap: FlowLeftToolBar[] = [
    {
      title: '工作流',
      label: '工序节点',
      shape: FlowNodeEnum.CUSTOM, // 工序节点
      width: 206,
      height: 44,
      icon: () => <BMIcon type='Process' />,
    },
    {
      title: '控制器',
      label: '网关',
      shape: FlowNodeEnum.GATEWAY,
      width: 28,
      height: 28,
      icon: () => <BMIcon type='Gateway' />,
    },
  ];
</script>

<template>
  <Flow :leftMap="leftMap" left-icon="Process2" next-icon="ProcessNext" style="height: 600px" />
</template>
```

### 2. 数据回显 + 保存(通过 ref 操作实例)

```vue
<script setup lang="ts">
  import { ref } from 'vue';
  import Flow from '@/components/Flow';
  import type { FlowInstanceType } from '@/components/Flow/type';

  const flowRef = ref<FlowInstanceType>();
  const modalJson = ref<any[]>([]); // 后端返回的画布 JSON

  // 节点"设置"按钮 → 弹窗录入 → 回写节点 data
  const onSetting = (cell: any) => {
    const data = flowRef.value?.getCellDataById(cell.id);
    // ...打开表单,提交后:
    flowRef.value?.updateFormValue(cell.id, { name: '新工序名', duration: 10 });
  };

  const save = () => {
    const json = flowRef.value?.getFlowData(); // 导出整张画布
    // 调用后端保存接口...
  };
</script>

<template>
  <Flow ref="flowRef" :modalJson="modalJson" :leftMap="leftMap" @handleClickSet="onSetting" @flowDataChange="onDirty" />
</template>
```

### 3. 只读(查看)模式

```vue
<Flow :modalJson="json" :isView="true" :isShowLeftToolBar="false" :showDelete="false" />
```

真实案例 `ProcessFlow/index.vue` 中的写法(按 `isView` 批量设置):

```ts
const isViewFlowToolBarAttr = computed(() =>
  isView.value
    ? {
        showUndo: false,
        showRedo: false,
        showDelete: false,
        isTransform: false,
        isShowLeftToolBar: false,
        isView: true,
      }
    : {},
);
```

### 4. 网关配置(真实业务模式)

`ProcessFlow` 中的典型交互链:

1. 用户点击网关节点 → 组件 emit `nodeClick(cell)`
2. 父组件判断 `cell.shape === FlowNodeEnum.GATEWAY`,打开"网关配置抽屉"
3. 抽屉中选择分支目标节点 → 调用 `flowRef.updateCellDataValue(cell.id, { gatewayType, ... })` 回写

## 十三、`FlowLeftToolBar` 类型

```ts
interface FlowLeftToolBar {
  title: string; // 分组标题
  label: string; // 节点默认名称
  icon: any; // 拖拽栏图标(返回 VNode 的函数)
  shape: FlowNodeEnum; // 拖入后生成的节点形状
  width?: number;
  height?: number;
  [key: string]: any; // 其余字段会写入节点 data
}
```

---

## 十四、注意事项与已知问题

1. **自动导入依赖**:`flow.vue` 中 `nextTick` / `onActivated`、`LeftToolbar` 中 `computed` 等未显式 `import`,依赖
   `unplugin-auto-import`(已确认 `vite.config.ts` / `auto-imports.d.ts` 配置)。**迁移到未配置该插件的项目会报错**,需手动
   补 import。

2. **`keep-alive` 假设**:`onActivated` 钩子会重建画布。若该组件**未被 `<keep-alive>` 包裹**,该钩子不触发(仅 `onMounted`
   生效);被包裹时切回会重绘——请确认外层是否为缓存组件。

3. **`getFlowData(_needTransformData)`** 参数实际**未使用**,注释中的"是否需要转换数据"未实现,直接返回 `graph.toJSON()`。

4. **`undo` / `redo` 注释写反**:`undo()` 内部调用 `graph.undo()`(实际为撤销),但 JSDoc 注释为"重做";`redo()` 同理。方法名
   与行为一致,仅注释有误。

5. **`reset` 事件为死代码**:`flowEmits` 声明了 `reset` 事件,但代码中从未 `emit('reset', ...)`,无法监听到。TopToolBar 的
   `@reset` 在 `flow.vue` 内部消费(调用"适应画布")。

6. **`#graph-container` 上的 `@setting` 监听疑似冗余**:BasicNode 的 `setting` 是 Vue 组件事件,不会冒泡为 DOM 事件,该监听
   基本不会触发,实际设置回调走的是 `register` 中的 `onSetting`。

7. **`connecting` 覆盖语义**:传入非空 `connecting` 会**整体替换**默认 connecting(含校验规则、路由、连接器),需自行补全。

8. **CSS 变量依赖**:节点选中态、图标颜色依赖 `--bmos-primary-color` / `--bmos-success-color` / `--bmos-danger-color` 等
   主题变量,需在主题层提供。
