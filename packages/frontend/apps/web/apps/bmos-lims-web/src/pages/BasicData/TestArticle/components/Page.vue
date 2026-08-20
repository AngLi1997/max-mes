<!-- 检品管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="true"
    :showAction="true"
    :showAddChildren="true"
    :rowKeys="['id']"
    :treeData="treeData"
    :autoExpandParent="true"
    :defaultExpandParent="true"
    :search="[true]"
    :formProps="[formFirstProps]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :treeField="{
      field: {
        categoryId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :showHeader="[false]"
    :requests="[getPage as DataRequestFn]"
    :columns="[columnsFirst]"
    :actionList="[
      {
        title: t('新增子分类'),
        action: 'addChildren',
      },
      {
        title: t('编辑分类'),
        action: 'editNode',
      },
      {
        title: t('删除分类'),
        action: 'deleteNode',
      },
    ]"
    @tree-action="handleTreeAction"
  >
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('检品管理列表')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0>
      <Button @click="sync">
        {{ t('同步') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 同步弹窗 -->
  <SyncForm 
    ref="syncFormRef"
    @ok="fetchTableData"
  />
  <!-- 树形控件操作弹窗 -->
  <TreeActionForm
    ref="treeActionFormRef"
    :treeData="treeData"
    @fetchTreeData="fetchTableData"
  />
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { t } from '@bmos/i18n';
import { TableTitle } from '@bmos/components';
import { useTable } from '../hooks';
import { DataNode } from 'ant-design-vue/es/tree';
import { 
  DataRequestFn, 
  BMPageComponent,
  BMTableTitle,
} from '@bmos/components';
import SyncForm from './SyncForm.vue';
import TreeActionForm from './TreeActionForm.vue';
import type { ActionListItem } from '@bmos/components';
import { 
  getLimsTree,
  getTestArticleList 
} from '@/services/index';
import { message } from 'ant-design-vue';

const selectTreeKey = ref<string | undefined>(undefined);
const selectTreeCode = ref<string>('');

const treeData = ref<DataNode[]>([]);

const emit = defineEmits(['watchEditInfo']);

// 请求数据
const getPage = async (params: any) => {
  try {
    if (!params.categoryId || params.categoryId === 'all') {
      return await getTestArticleList({
        ...params,
        categoryId: 0,
        categoryFlag: true,
      });
    }

    return await getTestArticleList(params);
  } catch (error: any) {
    message.error(error?.message);
  }
};

const pageRef = ref();

const fetchTableData = async () => {
  await getTreeData();
  pageRef.value.fetchData();
}

// 获取树
const getTreeData = async () => {
  try {
    const { data } = await getLimsTree({});
    treeData.value = [
      {
        id: 'all',
        name: t('全部'),
        showName: t('全部'),
        key: 'all',
        categoryFlag: true,
        children: data,
      }
    ];
  } catch (error) {
    message.error(error.message);
  }
};

const watchEditInfo = (row: any, disabled: boolean) => {
  emit('watchEditInfo', row, disabled);
}


const { columnsFirst, formFirstProps, viewReportModalOpen, rowData } =useTable({
  props: {
    watchEditInfo
  }
});

// 树相关操作
const treeActionFormRef = ref<InstanceType<typeof TreeActionForm>>();

const handleTreeAction = (action: ActionListItem, node?: any) => {
  // console.log('tree-action', action, node);
  treeActionFormRef.value?.action(action, node);
};

// 同步操作

const syncFormRef = ref<InstanceType<typeof SyncForm>>();

const sync = () => {
  syncFormRef.value?.openModal();
};


onMounted(() => {
  getTreeData();
});
</script>

<style scoped lang="less">
</style>