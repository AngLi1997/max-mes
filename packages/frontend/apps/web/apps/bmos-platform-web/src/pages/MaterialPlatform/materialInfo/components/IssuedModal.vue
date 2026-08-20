<template>
  <Modal v-model:open="open" wrapClassName="modalSizeLarge" :title="t('下发物料')" @ok="handleOk">
    <div class="modal-content">
      <Tabs v-model:activeKey="activeKey" @change="tabChange">
        <TabPane key="1" :tab="t('下发物料')"></TabPane>
        <TabPane key="2" :tab="t('下发物料分类')"></TabPane>
      </Tabs>
      <div class="issued-content">
        <div class="item-left">
          <div class="title">{{ t('物料列表') }}</div>
          <div style="height: calc(100% - 38px)">
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
        <div class="item-right">
          <div class="title">{{ t('下发列表') }}</div>
          <div class="tree">
            <CheckboxGroup
              v-for="item in issueBusinesseList"
              :key="item.platformName"
              v-model:value="item.value"
              style="width: 100%">
              <div class="checkbox-container">
                <Checkbox v-for="child in item.children" :key="child.childCode" :value="child.childCode">
                  {{ child.childName }}
                </Checkbox>
              </div>
            </CheckboxGroup>
          </div>
        </div>
      </div>
    </div>
  </Modal>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMSearchTree } from '@bmos/components';
  import { message, TabPane, Tabs, type TreeProps } from 'ant-design-vue';
  import type { TableInstance } from '@bmos/components';
  import {
    postMaterialIssueApi,
    getMaterialCategoryTreeApi,
    getMaterialTreeApi,
    getMaterialIssueBusinesseApi,
  } from '@/api/materialPlatform/materialInfo';
  const props = defineProps<{
    open: boolean;
    tableInstance: TableInstance;
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
  const issueBusinesseList = ref<any[]>([]);

  const tabChange = () => {
    leftCheckedKeys.value = [];
    leftCheckedNodes.value = [];
    issueBusinesseList.value.forEach((item: any) => {
      item.value = [];
    });
    searchValue.value = '';
    canLoad.value = true;
    getDefaultTreeData();
  };

  const treeActiveKey1CheckHandle = (checkedKeys: any[], e: any) => {
    leftCheckedNodes.value = e.checkedNodes;
    halfCheckedKeys.value = e.halfCheckedKeys;
  };
  const getParentIds = (node, ids: any[]) => {
    if (node.key !== '0') {
      ids.push(node.key);
    }
    if (node.parent) {
      getParentIds(node.parent, ids);
    }
    return ids;
  };
  // 获取所有的children的id
  const getChildrenIds = (node, ids: any[]) => {
    if (node.children) {
      node.children.forEach(item => {
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
      let ids = [];
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
      materialCategoryIds = leftCheckedKeys.value.checked;
    } else {
      leftCheckedNodes.value.forEach((item: any) => {
        if (item.categoryFlag) {
          materialCategoryIds.push(item.id);
        } else {
          materialIds.push(item.id);
        }
      });

      const halfIds = halfCheckedKeys.value.filter(item => item !== '0');
      materialCategoryIds.push(...halfIds);
    }
    const businesses = issueBusinesseList.value
      .filter(item => {
        return item.value && item.value.length > 0;
      })
      .map(item => {
        return {
          platformName: item.platformName,
          childCodeList: item.value,
        };
      });
    if (materialCategoryIds.length === 0) {
      message.info(t('请选择下发物料'));
      return;
    }
    if (businesses.length === 0) {
      message.info(t('请选择下发业务'));
      return;
    }
    try {
      const res: any = await postMaterialIssueApi({
        businesses,
        materialCategoryIds,
        materialIds,
      });
      message.success(t('下发成功'));
      open.value = false;
      props.tableInstance?.fetchData();
    } catch (error) {
      message.error(error.message);
    }
  };

  const getDefaultTreeData = async () => {
    canLoad.value = true;
    leftExpandedKeys.value = ['0'];
    try {
      if (activeKey.value === '1') {
        const res: any = await getMaterialTreeApi();
        leftTreeData.value = [{ id: '0', showName: t('全部'), children: res.data || [] }] as any;
      } else {
        const res: any = await getMaterialCategoryTreeApi();
        leftTreeData.value = [{ id: '0', showName: t('全部'), children: res.data || [] }] as any;
      }
    } catch (error) {
      message.error(error.message);
    }
  };
  const getMaterialIssueBusinesse = async () => {
    try {
      const res: any = await getMaterialIssueBusinesseApi();
      res.data.forEach((item: any) => {
        item.value = ref([]);
      });
      issueBusinesseList.value = res.data || [];
    } catch (error) {
      message.error(error.message);
    }
  };

  const onExpand = (keys: string[]) => {
    leftExpandedKeys.value = keys;
    autoExpandParent.value = false;
  };

  watch(open, (newV, oldV) => {
    if (newV) {
      searchValue.value = '';
      activeKey.value = '1';
      leftCheckedKeys.value = [];
      leftCheckedNodes.value = [];
      leftCheckedNodes.value = [];
      leftExpandedKeys.value = [];
      getDefaultTreeData();
      getMaterialIssueBusinesse();
    }
  });
</script>

<style lang="less" scoped>
  .modal-content {
    height: 505px;
  }
  .issued-content {
    width: 828px;
    display: flex;
    border-radius: 5px;
    border: 1px solid var(--bmos-first-level-border-color);
    box-sizing: border-box;
    .item-left,
    .item-right {
      height: 447px;
      width: 413px;
      .title {
        background-color: var(--bmos-disable-color);
        color: var(--bmos-second-level-text-color);
        height: 38px;
        box-sizing: border-box;
        padding: 10px 16px;
      }
      .tree {
        padding: 6px 16px;
        height: calc(100% - 38px);
        overflow-x: hidden;
        .checkbox-container {
          padding: 10px 0;
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
      }
    }

    .item-left {
      border-right: 1px solid var(--bmos-first-level-border-color);
      box-sizing: border-box;
      .title {
        border-radius: 5px 0 0 0;
      }
    }

    .item-right {
      .title {
        border-radius: 0 5px 0 0;
      }
    }
  }
</style>
