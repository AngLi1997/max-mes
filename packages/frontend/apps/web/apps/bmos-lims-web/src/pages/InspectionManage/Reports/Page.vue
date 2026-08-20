<!-- 报告生成 -->
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
    :show-tool-bars="[true]"
    :requests="[getPage]"
    :columns="[columnsFirst]"
  >
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('检验报告生成')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <SignModal 
    ref="signModalRef"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @submitSuccess="submitSuccess"
  />
</template>

<script setup lang="tsx">
import { onMounted, reactive, ref } from 'vue';
import { t } from '@bmos/i18n';
import { useTable } from './hooks/useTable';
import { DataNode } from 'ant-design-vue/es/tree';
import { BMPageComponent, BMTableTitle } from '@bmos/components';
import SyncForm from './SyncForm.vue';
import {
  Tabs,
  TabPane,
  message,
  Alert,
} from 'ant-design-vue';
import { 
  getLimsTree,
  getCheckOrderPage, 
  terminateCheckOrder,
  generateCheckOrderReport
} from '@/services/index';
import {
  SignModal
} from './components/index';
import {
  CHECK_STATUS
} from '@/utils/enum'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

const signModalRef = ref<InstanceType<typeof SignModal>>();
// const startVerifyRef = ref<InstanceType<typeof StartVerify>>();
const treeData = ref<DataNode[]>([]);
const pageRef = ref<any>();

const emit = defineEmits(['openVerify', 'openGenerate']);

// 请求数据
const getPage = async (params: any) => {
  try {
    const data = {
      ...params,
      processCode: CHECK_STATUS.REPORT,
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

const submitSuccess = async (formModal: any) => {
  try {
    const data = {
      id: rowData.value.id,
      reason: formModal.reason
    }
    if(signModalProps.signatureAction == 24){
      await generateCheckOrderReport(data)
    } else {
      await terminateCheckOrder(data)
    }
    message.success(t('操作成功'));
    pageRef.value.fetchData()
  } catch(error: any) {
    message.error(error?.message);
  }
}

const signatureDataFn = (formModal: any) => {
  const data = {
    id: formModal.id,
    reason: formModal.reason,
  }
  return JSON.stringify(data);
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


// 打开确认弹窗
const openSignModal = (row: any, flag: boolean) => {
  if (flag) {
    signModalProps.title = t('重新检测')
    signModalProps.extraSchemas = [
      {
        field: 'label',
        component: () => (
          <Alert 
            class='approval-alert'
            message={t('检验任务数据将重新录入，当前检验报告作废，是否继续？')}
            type='warning'
            showIcon={true}
            icon={<ExclamationCircleOutlined />}
          />
        )
      },
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
      }
    ]
    signModalProps.signatureAction = 24
  } else {
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
  }
  signModalRef.value?.openModal(row, flag);
}

// 查看请验详情
const openVerify = (row: any) => {
  emit('openVerify', row);
}

// 进入报告生成
const openGenerate = (row: any) => {
  emit('openGenerate', row);
}



const { columnsFirst, formFirstProps, rowData } =useTable({
  props: {
    openSignModal,
    openVerify,
    openGenerate
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