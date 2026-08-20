<!-- 检验录入 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :showAddChildren="false"
    :rowKeys="['orderNo']"
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
    :show-tool-bars="[true]"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :requests="[getPage]"
    :columns="[columnsFirst]"
  >
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('检验录入')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0>
      <Button type="primary" :disabled="!selectedRowKeys1.length" @click="openInput(selectedRowKeys1, false)">{{ t('批量录入') }}</Button>
    </template>
  </BMPageComponent>
  <SignModal 
    ref="signModalRef"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @submitSuccess="fetchTableData"
  />
</template>

<script setup lang="tsx">
import { onMounted, reactive, ref } from 'vue';
import { t } from '@bmos/i18n';
import { useTable } from './hooks/useTable';
import { DataNode } from 'ant-design-vue/es/tree';
import { BMPageComponent, BMTableTitle } from '@bmos/components';
import {
  Tabs,
  TabPane,
  message,
  Alert,
} from 'ant-design-vue';
import { 
  getLimsTree,
  getCheckOrderPage,
  getCheckOrderAnalyzeValid,
  submitCheckOrderInspect,
  terminateCheckOrder
} from '@/services/index';
import {
  SignModal
} from './components/index';
import {
  CHECK_STATUS
} from '@/utils/enum'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

const signModalRef = ref<InstanceType<typeof SignModal>>();
const treeData = ref<DataNode[]>([]);
const pageRef = ref<any>();

const emit = defineEmits(['openVerify', 'openInput']);

// 请求数据
const getPage = async (params: any) => {
  try {
    const data = {
      ...params,
      processCode: CHECK_STATUS.INSPECT,
    }
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

const fetchTableData = async (formModal: any) => {
  try {
    if(signModalProps.signatureAction == 20){
      const list = [{orderNo: rowData.value.orderNo}] as any
      await submitCheckOrderInspect([...list])
      message.success(t('提交成功'));
    } else {
      await terminateCheckOrder({
        id: rowData.value.id,
        reason: formModal.reason
      })
      message.success(t('操作成功'));
    }
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

const signModalProps = reactive({
  title: t('检验终止'),
  extraSchemas: [
    {
      field: 'reason',
      label: t('原因'),
      component: 'Input',
      required: true,
      componentProps: {
        maxLength: 100
      }
    }
  ],
  signatureAction: 22
})

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

const signatureDataFn = (formModal: any) => {
  const list = [{orderNo: rowData.value.orderNo}] as any

  return JSON.stringify(formModal.reason ? {id: rowData.value.id, reason: formModal.reason} : [...list]);
}

// 打开终止/提交弹窗 flag -- true:终止 false:提交
const openSignModal = async (row, flag: boolean) => {
  if(flag){
    signModalProps.title = t('检验终止')
    signModalProps.extraSchemas = [
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
      }
    ]
    signModalProps.signatureAction = 22
  } else {
    const { data } = await getCheckOrderAnalyzeValid({orderNoList: row.orderNo, count: 0})
    signModalProps.title = t('提交')
    signModalProps.extraSchemas = data <= 0 ? [] : [
      {
        field: 'label',
        component: () => (
          <Alert 
            class='approval-alert'
            message={`${t('存在')}${data}${t('项未录入分析项，是否批量录为N/A并提交')}`}
            type='warning'
            showIcon={true}
            icon={<ExclamationCircleOutlined />}
          />
        )
      },
    ]
    signModalProps.signatureAction = 20
  }
  signModalRef.value?.openModal(row, flag);
}

// 查看请验详情
const openVerify = (row: any) => {
  emit('openVerify', row);
}

// 进入录入页面
// flag -- true: 查看 false: 录入
const openInput = (list: any, flag: any) => {
  emit('openInput', list, flag);
}

const { columnsFirst, formFirstProps, rowData } =useTable({
  props: {
    openSignModal,
    openVerify,
    openInput
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