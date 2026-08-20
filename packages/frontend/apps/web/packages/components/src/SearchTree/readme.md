# 搜索树组件

同 AntV 树组件，封装了搜索逻辑，以及每项右侧的操作

## 基础用法

```vue
<template>
  <BMSearchTree
    ref="searchRef"
    :expanded-keys="expandedKeys"
    :showAllAddIcon="false"
    :showAction="false"
    :tree-data="treeData"
    :checkable="true"
    :checked-keys="curCheckedKeys"
    :fieldNames="{ title: 'showName', key: 'id' }"
    @check="check"></BMSearchTree>
</template>

<script lang="tsx" setup>
  import { BMSearchTree, type SearchTreeInstance } from '@bmos/components';
  import { reactive, ref } from 'vue';

  const searchRef = ref<SearchTreeInstance>();

  const curCheckedKeys = ref<string[] | { checked: string[]; halfChecked: string[] }>([]);
  const curCheckedNodes = ref<DataNode[]>([]);
  const expandedKeys = ref<string[]>(['all']);

  const check = (keys: any, e: any) => {
    curCheckedNodes.value = e.checkedNodes;
    curCheckedKeys.value = keys;
  };

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      // 获取工艺树
      const { data } = await getProcessListTreeReq();
      treeData.value = [
        {
          showName: t('全部'),
          id: 'all',
          key: 'all',
          children: data,
        },
      ];
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>
```

## API

### SearchTree Props

| 属性              | 说明                   | 类型               | 默认值                                                                         |
| ----------------- | ---------------------- | ------------------ | ------------------------------------------------------------------------------ |
| 所有 AntdV tree   | 所有 AntdV tree        | `TreeProps`        | `treeData: [], selectable: true, autoExpandParent: true,expandedKeys: ['all']` |
| searchPlaceholder | 搜索框的 placeholder   | `string`           | `请输入关键字`                                                                 |
| showSearch        | 是否显示搜索框         | `boolean`          | `true`                                                                         |
| showAll           | 是否添加全部项         | `boolean`          | `true`                                                                         |
| showAllAddIcon    | 是否显示全部的添加按钮 | `boolean`          | `true`                                                                         |
| showAction        | 是否显示更多操作       | `boolean`          | `true`                                                                         |
| actionList        | 更多操作中显示的列表   | `ActionListItem[]` | `[]`                                                                           |
| filterSearch      | 开启搜索过滤           | `boolean`          | `true`                                                                         |
| showCancelButton  | 是否显示取消按钮       | `boolean`          | `true`                                                                         |

### Events

| 事件名                 | 说明                     | 回调参数 |
| ---------------------- | ------------------------ | -------- |
| register               | 组件注册触发             | -        |
| 'update:expanded-keys' | expanded-keys改变        |          |
| action                 | 更多按钮中的item触发事件 | -        |
| expand                 | 同 antdV                 | -        |
