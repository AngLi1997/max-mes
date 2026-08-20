<!-- 报告审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :showAddChildren="false"
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
    :isSelects="[false, false]"
    :requests="[getPage]"
    :columns="[columnsFirst]"
  >
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('检验报告审核')"></BMTableTitle>
    </template>
    <!-- <template #tableHeaderToolbar0>
      <Button type="primary" @click="openSignModal(selectedRowKeys1,false)">{{ t('批量录入') }}</Button>
    </template> -->
  </BMPageComponent>
  <SignModal 
    ref="signModalRef"
    :signatureDataFn="signatureDataFn"
    @submitSuccess="fetchTableData"
  />
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { t } from '@bmos/i18n';
import { useTable } from './hooks/useTable';
import { DataNode } from 'ant-design-vue/es/tree';
import { BMPageComponent, BMTableTitle } from '@bmos/components';
import {
  Tabs,
  TabPane,
  message,
} from 'ant-design-vue';
import { 
  getLimsTree,
  getCheckOrderPage,
  terminateCheckOrder
} from '@/services/index';
import {
  SignModal
} from './components/index';
import {
  CHECK_STATUS
} from '@/utils/enum'

const signModalRef = ref<InstanceType<typeof SignModal>>();
const treeData = ref<DataNode[]>([]);
const pageRef = ref<any>();

const emit = defineEmits(['openVerify', 'openInput', 'openAudit']);

// 请求数据
const getPage = async (params: any) => {
  const data = {
    ...params,
    processCode: CHECK_STATUS.AUDIT_REPORT,
  }
  try {
    if (!params.categoryId || params.categoryId === 'all') {
      return await getCheckOrderPage({
        ...data,
        categoryId: 0,
        categoryFlag: true,
      });
    }
    return await getCheckOrderPage(data);
  } catch(error: any) {
    message.error(error?.message);
  }
};

const signatureDataFn = (formModel: any) => {
  const data = {
    id: rowData.value.id,
    reason: formModel.reason,
  }
  return JSON.stringify(data);
}

const fetchTableData = async (formModel: any) => {
  try {
    const data = {
      id: rowData.value.id,
      reason: formModel.reason,
    }
    await terminateCheckOrder(data)
    message.success(t('操作成功'));
    pageRef.value.fetchData();
  } catch(error: any) {
    message.error(error?.message);
  }
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
      },
    ];
  } catch (error) {
    message.error(error.message);
  }
};

const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
const operationSelectedRows = ref<any>([]); //存表格多选的数据

// 多选
const rowSelections = reactive([
  {
    type: 'checkbox',
    hideSelectAll: false,
    selectedRowKeys: selectedRowKeys1.value,
    onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
      selectedRowKeys1.value = selectedRowKeys;
      if (rowSelections[0]?.selectedRowKeys) {
        rowSelections[0].selectedRowKeys = selectedRowKeys;
      }
      operationSelectedRows.value = selectedRows;
    },
  },
  null,
]);

// 打开确认弹窗
const openSignModal = (row: any, flag: boolean) => {
  signModalRef.value?.openModal(row, flag);
}

// 查看请验详情
const openVerify = (row: any) => {
  emit('openVerify', row);
}

// 进入审核页面
const openAudit = (row: any) => {
  emit('openAudit', row);
}

const { columnsFirst, formFirstProps, rowData } =useTable({
  props: {
    openSignModal,
    openVerify,
    openAudit
  }
});

onMounted(() => {
  getTreeData();
});

</script>

<style lang="less" scoped>
// :deep .bmos-tool-bar {
//   justify-content: flex-start;
// }
</style>