<template>
  <a-space>
    <a-tree
      v-model:expandedKeys="expandedKeys"
      v-model:selectedKeys="selectedKeys"
      v-model:checkedKeys="checkedKeys"
      checkable
      :tree-data="treeData"></a-tree>
  </a-space>
  <span style="margin-left: 100px"></span>
  <a-space>
    <a-tree
      class="displayTree"
      :default-expand-all="true"
      :tree-data="displayTreeData"></a-tree>
  </a-space>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import type { TreeProps } from 'ant-design-vue';

  const treeData: TreeProps['treeData'] = [
    {
      title: 'parent 1',
      key: '0-0',
      children: [
        {
          title: 'parent 1-0',
          key: '0-0-0',
          children: [
            {
              title: '当title的宽度超过最大宽度时用省略号表示并且不换行',
              key: '0-0-0-0',
            },
            { title: 'leaf', key: '0-0-0-1' },
            { title: 'leaf', key: '0-0-0-2' },
          ],
        },
        {
          title: 'parent 1-1',
          key: '0-0-1',
          children: [{ key: '0-0-1-0', title: 'sss' }],
        },
      ],
    },
  ];

  const expandedKeys = ref<string[]>(['0-0-0', '0-0-1']);
  const selectedKeys = ref<string[]>(['0-0-0', '0-0-1']);
  const checkedKeys = ref<string[]>(['0-0-0', '0-0-1']);
  const displayTreeData = [
    {
      title: '选项1',
      key: '0-0-0-0',
      children: [
        {
          title: '选项1-1',
          key: '0-0-0-0-0',
          children: [
            {
              title: '当title的宽度超过最大宽度时用省略号表示并且不换行',
              key: '0-0-0-0-0-0',
            },
            { title: '0-0-0-0-0-1', key: '0-0-0-0-0-1' },
          ],
        },
        {
          title: '0-0-0-1',
          key: '0-0-0-1',
          children: [
            { title: '0-0-0-1-0', key: '0-0-0-1-0' },
            { title: '0-0-0-1-1', key: '0-0-0-1-1' },
          ],
        },
      ],
    },
  ];
</script>
