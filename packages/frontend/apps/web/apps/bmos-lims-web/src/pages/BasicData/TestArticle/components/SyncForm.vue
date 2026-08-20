<!-- 同步检品组件 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检品同步')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @cancel="cancel"
    @okModal="ok">
    <Tabs v-model:activeKey="activeKey" @change="onChange">
      <TabPane v-for="item in items" :key="item.key" :tab="item.label"></TabPane>
    </Tabs>
    <div class="issued-content">
      <div class="title">{{ (activeKey === '1' ? t('物料') : t('分类')) + t('列表') }}</div>
      <BMSearchTree
        v-if="changeFlag"
        ref="searchTreeRef"
        v-model:expandedKeys="expandedKeys"
        v-model:checkedKeys="checkedKeys"
        v-model:selectedKeys="selectedKeys"
        :showAllAddIcon="false"
        :showAddChildren="false"
        :showDeleteNode="false"
        :showAction="false"
        checkable
        :checkStrictly="activeKey === '2'"
        v-bind="treeProps"
        @check="check"
        @expand="expand"></BMSearchTree>
    </div>
  </BMModalForm>
</template>

<script setup lang="tsx">
  import { BMModalForm, ModalFormInstance, BMSearchTree, SearchTreeProps } from '@bmos/components';
  import { TreeProps } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { t } from '@bmos/i18n';
  import { reactive, ref } from 'vue';
  import { Tabs, TabPane, type TabsProps, message } from 'ant-design-vue';
  import { getSyncTree, getSyncTreeAll, syncData } from '@/services/index';

  const emit = defineEmits(['ok']);

  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean | undefined>(false);

  const getTreeApi = ref(getSyncTree);

  const checkedKeys = ref<string[] | any>([]); //用于回显的数据
  const selectedKeys = ref<string[]>([]);
  const expandedKeys = ref<string[]>(['0']); //默认展开全部
  // 展开收起时触发
  const expand = () => {
    // console.log('expanded', expandedKeys, info);
  };

  const onLoadData: TreeProps['loadData'] = (treeNode: any) => {
    return new Promise<void>(resolve => {
      if ((treeNode.dataRef.children && treeNode.dataRef.children.length > 0) || !treeNode.dataRef.categoryFlag) {
        treeNode.dataRef.children.forEach((item: any) => {
          item.isLeaf = !item.categoryFlag;
        });
        // resolve();
        // return;
      }
      // getTreeApi
      //   .value({
      //     parentId: treeNode.dataRef.id,
      //   })
      //   .then((res: any) => {
      //     res.data.forEach((item: any) => {
      //       item.isLeaf = !item.categoryFlag;
      //       item.showName = `${item.mergeCode}-${item.name}`;
      //     });
      //     treeNode.dataRef.children = res.data || null;
      //     treeProps.treeData = treeProps.treeData ? [...treeProps.treeData] : [];
      //     resolve();
      //   });
      resolve();
    });
  };

  // 同步相关
  const activeKey = ref('1');

  const treeProps: SearchTreeProps = reactive({
    addChildrenNeedCode: true,
    fieldNames: {
      title: 'showName',
      key: 'id',
    },
    loadData: onLoadData,
    treeData: [],
  });

  const getParentIds = (node: any, ids: any[]) => {
    if (node.key !== '0') {
      ids.push(node.key);
    }
    if (node.parent) {
      getParentIds(node.parent, ids);
    }

    return ids;
  };
  // 获取所有的children的id
  const getChildrenIds = (node: any, ids: any[]) => {
    if (node.children) {
      node.children.forEach((item: any) => {
        ids.push(item.id);
        getChildrenIds(item, ids);
      });
    }
    return ids;
  };

  // 选中复选框触发
  const check = (selectedKeys: KEY[] | { checked: KEY[]; halfChecked: KEY[] }, info: any) => {
    syncLists.materialCategoryIds = [];
    syncLists.materialIds = [];
    if (activeKey.value == '1') {
      const materialCategoryIds = new Map();
      const materialIds = new Map();

      info.checkedNodes.forEach((item: any) => {
        if (item.id !== '0') {
          const stack = [item];
          while (stack.length) {
            const currentNode = stack.pop();
            if (currentNode.children) {
              stack.push(...currentNode.children);
            }
            if (currentNode.categoryFlag) {
              materialCategoryIds.set(currentNode.id, true);
            } else {
              materialIds.set(currentNode.id, true);
            }
          }
          syncLists.materialCategoryIds = Array.from(materialCategoryIds.keys());
          syncLists.materialIds = Array.from(materialIds.keys());
          // if (item.categoryFlag) {
          //   if (!syncLists.materialCategoryIds.includes(item.id)) {
          //     syncLists.materialCategoryIds.push(item.id);
          //   }
          // } else {
          //   if (!syncLists.materialIds.includes(item.id)) {
          //     syncLists.materialIds.push(item.id);
          //   }
          // }
        }
      });
      console.log('syncLists', syncLists);
      const halfIds = info.halfCheckedKeys.filter((item: any) => item !== '0');
      syncLists.materialCategoryIds.push(...halfIds);
    }
    // 物料分类勾选全部
    // 选中全部节点时，将所有的子节点选中
    // 取消全部节点选中时，将所有的子节点取消选中
    // 选中子节点时，将父节点选中
    // 取消父节点时，将子节点取消选中
    else if (activeKey.value == '2') {
      if (info.checked) {
        let ids: string[] = [];
        if (info.node.id === '0') {
          ids = getChildrenIds(info.node, []);
        } else {
          ids = getParentIds(info.node, []);
        }
        ids.forEach(item => {
          if (checkedKeys.value.checked.includes(item)) {
            return;
          }
          checkedKeys.value.checked.push(item);
        });
      } else {
        if (info.node.id === '0') {
          checkedKeys.value.checked = [];
          syncLists.materialCategoryIds = [];
          syncLists.materialIds = [];
        } else {
          let ids: string[] = [];
          ids = getChildrenIds(info.node, []);
          ids.forEach(item => {
            const index = checkedKeys.value.checked.findIndex((id: string) => id === item);
            if (index > -1) {
              checkedKeys.value.checked.splice(index, 1);
            }
          });
        }
      }
    }
  };

  const syncLists = reactive({
    materialCategoryIds: [] as any,
    materialIds: [] as any,
  });

  // const register = (registerModal: ModalFormInstance) => {
  //   modalFormRef.value = registerModal;
  // };
  const initData = () => {
    syncLists.materialCategoryIds = [];
    syncLists.materialIds = [];
    getTreeApi.value = getSyncTree;
    checkedKeys.value = [];
    selectedKeys.value = [];
    expandedKeys.value = ['0'];
    activeKey.value = '1';
  };

  const openModal = async () => {
    initData();
    open.value = true;
    await getTreeData(0);
  };

  const getTreeData = async (parentId: number | string) => {
    try {
      const { data } = await getTreeApi.value({ parentId });
      const ans = data.length
        ? data.map((item: any) => {
            return {
              ...item,
              isLeaf: !item.categoryFlag,
              showName: `${item.mergeCode}-${item.name}`,
            };
          })
        : [];

      treeProps.treeData = [{ id: '0', showName: t('全部'), children: ans || [] }] as any;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 提交
  const ok = async () => {
    try {
      // 物料分类
      if (activeKey.value == '2') {
        syncLists.materialCategoryIds = checkedKeys.value.checked || [];
      }
      if (syncLists.materialCategoryIds.length === 0 && syncLists.materialIds.length === 0) {
        message.error(t('请选择数据'));
        return;
      }
      await syncData({
        ...syncLists,
        categoryType: 3,
      });
      message.success(t('同步成功'));
      cancel();
      emit('ok');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const cancel = () => {
    open.value = false;
    activeKey.value = '1';
  };

  // watch(
  //   () => activeKey.value,
  //   (val) => {
  //     if (val) {
  //       getTreeApi.value = val == '1' ? getSyncTree : getSyncTreeAll;
  //     }
  //   },
  // );

  const changeFlag = ref(true);

  // 切换
  const onChange = async (key: Key) => {
    changeFlag.value = false;
    if (key) {
      getTreeApi.value = key == '1' ? getSyncTree : getSyncTreeAll;
    }
    treeProps.treeData = [];
    treeProps.loadData = key == '1' ? onLoadData : undefined;
    syncLists.materialCategoryIds = [];
    syncLists.materialIds = [];
    checkedKeys.value = [];
    expandedKeys.value = ['0'];
    selectedKeys.value = [];
    setTimeout(async () => {
      changeFlag.value = true;
      await getTreeData(0);
    }, 0);
  };
  // @ts-ignore
  const items = reactive<TabsProps['items']>([
    {
      key: '1',
      label: t('同步物料'),
    },
    {
      key: '2',
      label: t('同步物料分类'),
    },
  ]);

  defineExpose({
    openModal,
    ok,
    cancel,
  });
</script>

<style lang="less" scoped>
  .issued-content {
    display: flex;
    border-radius: 5px;
    border: 1px solid var(--bmos-first-level-border-color);
    box-sizing: border-box;
    flex-direction: column;
    .title {
      width: 100%;
      background-color: var(--bmos-disable-color);
      color: var(--bmos-second-level-text-color);
      height: 38px;
      box-sizing: border-box;
      padding: 10px 16px;
    }
  }
  :deep .bmos-search-tree .tree-inner-search-class {
    height: calc(100vh - 240px - 128px - 38px - 56px - 36px - 34px);
  }
</style>
