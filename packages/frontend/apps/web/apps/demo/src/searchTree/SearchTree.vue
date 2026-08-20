<template>
  <div class="bm-search-tree">
    <BMSearchTree
      ref="searchTreeRef"
      @select="select"
      @action="action"
      @register="register"
      @addItem="addItem"
      show-icon
      v-bind="treeProps"
      v-model:expanded-keys="expandedKey">
      <!-- <template #switcherIcon="{ switcherCls }">
        <ShrinkOutlined :class="switcherCls" />
      </template> -->
      <!-- <template #icon>
          <SmileOutlined />
      </template> -->
    </BMSearchTree>
  </div>
</template>
<script lang="tsx" setup>
  import {
    BMSearchTree,
    SearchTreeProps,
    SearchTreeInstance,
    ActionListItem,
  } from '@bmos/components';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { ComponentInternalInstance, onMounted, reactive, ref } from 'vue';

  const addItem = () => {
    console.log('addItem');
  };
  const searchTreeRef = ref<SearchTreeInstance>();
  const treeProps: SearchTreeProps = reactive({
    treeData: [
      {
        key: 'all',
        title: '全部',
        children: [
          {
            title: '0-0',
            key: '0-0',
            children: [
              {
                title: '0-0-012312313131sdfsa水电费撒012312313131sdfsa水电费撒时光大点时光大点',
                key: '0-0-0',
                children: [
                  {
                    title: '0-0-0-0',
                    key: '0-0-0-0',
                  },
                  {
                    title: '0-0-0-1',
                    key: '0-0-0-1',
                  },
                  {
                    title: '0-0-0-2',
                    key: '0-0-0-2',
                  },
                ],
              },
              {
                title: '0-0-1',
                key: '0-0-1',
                children: [
                  {
                    title: '0-0-1-0',
                    key: '0-0-1-0',
                  },
                  {
                    title: '0-0-1-1',
                    key: '0-0-1-1',
                  },
                  {
                    title: '0-0-1-2',
                    key: '0-0-1-2',
                  },
                ],
              },
              {
                title: '0-0-2',
                key: '0-0-2',
              },
            ],
          },
          {
            title: '0-1',
            key: '0-1',
            notShowMoreBtn: true,
            children: [
              {
                title: '0-1-0',
                key: '0-1-0',
                children: [
                  {
                    title: '0-1-0-0',
                    key: '0-1-0-0',
                  },
                  {
                    title: '0-1-0-1',
                    key: '0-1-0-1',
                  },
                  {
                    title: '0-1-0-2',
                    key: '0-1-0-2',
                  },
                ],
              },
              {
                title: '0-1-1',
                key: '0-1-1',
                children: [
                  {
                    title: '0-1-1-0',
                    key: '0-1-1-0',
                  },
                  {
                    title: '0-1-1-1',
                    key: '0-1-1-1',
                  },
                  {
                    title: '0-1-1-2',
                    key: '0-1-1-2',
                  },
                ],
              },
              {
                title: '0-1-2',
                key: '0-1-2',
              },
            ],
          },
          {
            title: '0-2ce电饭锅厚积薄发四u发吧是打发撒一顿饭噶苏可以发噶苏代发',
            key: '0-2',
          },
        ],
      },
    ],
    actionList: [
      {
        title: '添加子节点',
        action: 'addChildren',
      },
      {
        title: '删除节点',
        action: 'deleteNode',
      },
      {
        title: '编辑节点',
        action: 'editNode',
      },
      {
        title: '自定义操作',
        action: 'customAction',
        render: (node: DataNode) => {
          return (
            <a href='javascript:;' onClick={() => handleClickAction(node.key)}>
              自定义操作11111
            </a>
          );
        },
      },
    ],
  });
  const select = (
    selectedKeys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    console.log('selected', selectedKeys, info);
  };

  const action = (action: ActionListItem, node: DataNode) => {
    console.log('action', action, node);
  };
  const handleClickAction = (key: Key) => {
    console.log('handleClickAction', key);
  };

  const register = (instance: ComponentInternalInstance['exposed']) => {
    console.log('register', instance);
  };

  const expandedKey = ref<Key[]>([]);
  onMounted(() => {
    expandedKey.value = ['0-0-0'];
  });
</script>

<style lang="less">
  .bm-search-tree {
    width: 264px;
  }
</style>
