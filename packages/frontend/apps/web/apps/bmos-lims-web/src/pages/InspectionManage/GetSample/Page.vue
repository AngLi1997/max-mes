<!-- 取样 -->
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
      <BMTableTitle :title="t('取样')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <SignModal 
    ref="signModalRef"
    @submitSuccess="fetchTableData"
  />
  <PrintVerify 
    ref="printVerifyRef" 
    v-if="showPrint"
  />
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue';
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
  getCheckOrderInfo
} from '@/services/index';
import {
  SignModal
} from './components/index';
import {
  CHECK_STATUS
} from '@/utils/enum'
import {
  PrintVerify
} from '@/components/PrintVerify';

const signModalRef = ref<InstanceType<typeof SignModal>>();
const treeData = ref<DataNode[]>([]);
const pageRef = ref<any>();

const emit = defineEmits(['openVerify']);

// 请求数据
const getPage = async (params: any) => {
  try {
    const data = {
      ...params,
      processCode: CHECK_STATUS.TAKE,
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

const fetchTableData = () => {
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
      },
    ];
  } catch (error) {
    message.error(error.message);
  }
};

// 打开取样弹窗
const openSignModal = (row: any, flag: boolean) => {
  signModalRef.value?.openModal(row, flag);
}

// 查看请验详情
const openVerify = (row: any) => {
  emit('openVerify', row);
}

const printVerifyRef = ref<InstanceType<typeof PrintVerify>>();
const showPrint = ref<boolean>(false);
// 打印
const print = async (data) => {
  try {
    const res = await getCheckOrderInfo(data);
    showPrint.value = true
    nextTick(() => {
      printVerifyRef.value?.printDom(res.data);
      setTimeout(() => {
        showPrint.value = false
      }, 0);
    });
  } catch(error: any) {
    message.error(error?.message);
  }
}

const { columnsFirst, formFirstProps, rowData } =useTable({
  props: {
    openSignModal,
    openVerify,
    print
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