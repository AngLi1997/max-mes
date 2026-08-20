<template>
  <Modal v-model:open="open" wrapClassName="modalSizeMedium" :title="t('同步物料')" @ok="handleOk">
    <div class="modal-content">
      <Tabs v-model:activeKey="activeKey" @change="tabChange">
        <TabPane key="1" :tab="t('同步物料')"></TabPane>
        <TabPane key="2" :tab="t('同步物料分类')"></TabPane>
      </Tabs>
      <div class="issued-content">
        <div class="item-left">
          <div class="title">{{ t('物料列表') }}</div>
          <div style="height: calc(100% - 38px); padding: 0 2px">
            <BMSearchTree
              v-if="activeKey === '1'"
              v-model:expandedKeys="leftExpandedKeys"
              v-model:checkedKeys="leftCheckedKeys"
              checkable
              :field-names="{
                title: 'showName',
                key: 'id',
              }"
              :tree-data="leftTreeData"
              @check="treeActiveKey1CheckHandle"></BMSearchTree>
            <BMSearchTree
              v-if="activeKey === '2'"
              v-model:checkedKeys="leftCheckedKeys"
              checkable
              checkStrictly
              :expandedKeys="leftExpandedKeys"
              :autoExpandParent="autoExpandParent"
              :field-names="{ title: 'showName', key: 'id' }"
              :tree-data="leftTreeData"
              @expand="onExpand"
              @check="handleCheck"></BMSearchTree>
          </div>
        </div>
      </div>
    </div>
  </Modal>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import type { TreeProps } from 'ant-design-vue';
  import { message, Modal, Tabs, TabPane } from 'ant-design-vue';
  import { postCargoSyncApi, getPlatformMaterialTreeApi, getCargoSyncTreeAllApi } from '@/services';
  import type { TableInstance } from '@bmos/components';
  import { BMSearchTree } from '@bmos/components';
  const props = defineProps<{
    open: boolean;
    categoryType: number;
    tableInstance: TableInstance;
    fetchTreeData: any;
  }>();

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();
  const open = computed({
    get() {
      return props.open;
    },
    set(value: boolean) {
      emit('update:open', value);
    },
  });

  const activeKey = ref('1');
  const searchValue = ref('');
  const leftTreeData = ref<TreeProps['treeData']>([]);
  const leftExpandedKeys = ref<string[]>(['0']);
  const leftCheckedKeys = ref<string[] | any>([]);
  const leftCheckedNodes = ref([]);
  const halfCheckedKeys = ref([]);
  const autoExpandParent = ref<boolean>(false);
  const canLoad = ref<boolean>(false);

  const tabChange = () => {
    leftCheckedKeys.value = [];
    leftCheckedNodes.value = [];
    leftCheckedNodes.value = [];
    searchValue.value = '';
    canLoad.value = true;
    getDefaultTreeData();
  };

  const treeActiveKey1CheckHandle = (checkedKeys: any[], e: any) => {
    leftCheckedNodes.value = e.checkedNodes;
    halfCheckedKeys.value = e.halfCheckedKeys;
  };
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
  // 选中全部节点时，将所有的子节点选中
  // 取消全部节点选中时，将所有的子节点取消选中
  // 选中子节点时，将父节点选中
  // 取消父节点时，将子节点取消选中
  const handleCheck = (checkedKeys: any[], e: any) => {
    if (e.checked) {
      let ids: string[] = [];
      if (e.node.id === '0') {
        ids = getChildrenIds(e.node, []);
      } else {
        ids = getParentIds(e.node, []);
      }
      ids.forEach(item => {
        if (leftCheckedKeys.value.checked.includes(item)) {
          return;
        }
        leftCheckedKeys.value.checked.push(item);
      });
    } else {
      if (e.node.id === '0') {
        leftCheckedKeys.value.checked = [];
      } else {
        let ids: string[] = [];
        ids = getChildrenIds(e.node, []);
        ids.forEach(item => {
          const index = leftCheckedKeys.value.checked.findIndex((id: string) => id === item);
          if (index > -1) {
            leftCheckedKeys.value.checked.splice(index, 1);
          }
        });
      }
    }
  };
  const handleOk = async () => {
    let materialCategoryIds = [];
    let materialIds: any = [];
    if (activeKey.value === '2') {
      materialCategoryIds = leftCheckedKeys.value.checked || [];
    } else {
      leftCheckedNodes.value.forEach((item: any) => {
        if (item.categoryFlag) {
          materialCategoryIds.push(item.id);
        } else if (item.id !== '0') {
          materialIds.push(item.id);
        }
      });
      const halfIds = halfCheckedKeys.value.filter(item => item !== '0');
      materialCategoryIds.push(...halfIds);
    }
    if (materialCategoryIds.length === 0) {
      message.info(t('请选择同步物料'));
      return;
    }
    try {
      await postCargoSyncApi({
        materialCategoryIds,
        materialIds,
        // categoryType: props.categoryType,
      });
      message.success(t('同步成功'));
      open.value = false;
      props.tableInstance?.fetchData();
      props.fetchTreeData();
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const getDefaultTreeData = async () => {
    canLoad.value = true;
    leftExpandedKeys.value = ['0'];
    try {
      if (activeKey.value === '1') {
        const res: any = await getPlatformMaterialTreeApi();
        leftTreeData.value = [{ id: '0', showName: t('全部'), children: res.data || [] }] as any;
      } else {
        const res: any = await getCargoSyncTreeAllApi();
        // {
        // categoryType: props.categoryType,
        // }

        leftTreeData.value = [{ id: '0', showName: t('全部'), children: res.data || [] }] as any;
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const onExpand = (keys: string[]) => {
    leftExpandedKeys.value = keys;
    autoExpandParent.value = false;
  };

  watch(
    () => props.open,
    newV => {
      if (newV) {
        activeKey.value = '1';
        leftTreeData.value = [];
        leftCheckedKeys.value = [];
        leftCheckedNodes.value = [];
        leftCheckedNodes.value = [];
        searchValue.value = '';
        getDefaultTreeData();
      }
    },
  );
</script>

<style lang="less" scoped>
  .issued-content {
    border-radius: 5px;
    border: 1px solid var(--bmos-first-level-border-color);
    box-sizing: border-box;
    .item-left {
      height: 456px;
      box-sizing: border-box;

      .title {
        background-color: var(--bmos-disable-color);
        color: var(--bmos-second-level-text-color);
        height: 38px;
        box-sizing: border-box;
        padding: 10px 16px;
        border-radius: 5px 5px 0 0;
      }
    }
  }
</style>
