<!-- 检验报告签发 -->
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
      <BMTableTitle :title="t('检验报告签发')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <Sign 
    ref="signModalRef"
    v-model:open="signOpen"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @signSuccess="fetchTableData"
  />
</template>

<script setup lang="ts">
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
} from 'ant-design-vue';
import { 
  getLimsTree,
  getCheckOrderPage, 
  terminateCheckOrder
} from '@/services/index';
import {
  Sign
} from '@/components/Sign';
import {
  CHECK_STATUS
} from '@/utils/enum'

const signModalRef = ref<InstanceType<typeof Sign>>();
// const startVerifyRef = ref<InstanceType<typeof StartVerify>>();
const treeData = ref<DataNode[]>([]);
const pageRef = ref<any>();

const emit = defineEmits(['openVerify', 'openIssuance']);


// 请求数据
const getPage = async (params: any) => {
  try {
    const data = {
      ...params,
      processCode: CHECK_STATUS.SIGN,
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
    pageRef.value?.fetchData();
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

const signOpen = ref(false);

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

// 打开终止弹窗
const openSignModal = (row: any, flag: boolean) => {
  signOpen.value = true;
}

// 查看请验详情
const openVerify = (row: any) => {
  emit('openVerify', row);
}

// 进入报告生成
const openIssuance = (row: any) => {
  emit('openIssuance', row);
}

const { columnsFirst, formFirstProps, rowData } =useTable({
  props: {
    openVerify,
    openIssuance,
    openSignModal
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