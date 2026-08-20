<!-- 请验确认 -->
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
    :rowSelections="rowSelections"
    :requests="[getPage]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('请验确认')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0>
      <Button type="primary" :disabled="!selectedRowKeys1.length" @click="openSignModal(selectedRowKeys1, false)">
        {{ t('批量确认') }}
      </Button>
      <Button type="primary" @click="openStartVerify">{{ t('发起请验') }}</Button>
    </template>
  </BMPageComponent>
  <SignModal ref="signModalRef" @submitSuccess="fetchTableData" />
  <StartVerify ref="startVerifyRef" @submitSuccess="submitSuccess" />
  <PrintVerify ref="printVerifyRef" v-if="showPrint" />
</template>

<script setup lang="ts">
  import { nextTick, onMounted, reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks/useTable';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import SyncForm from './SyncForm.vue';
  import { Tabs, TabPane, message } from 'ant-design-vue';
  import { getLimsTree, getCheckOrderPage, confirmCheckOrder, getCheckOrderInfo } from '@/services/index';
  import { SignModal, StartVerify } from './components/index';
  import { CHECK_STATUS } from '@/utils/enum';
  import { PrintVerify } from '@/components/PrintVerify';

  const signModalRef = ref<InstanceType<typeof SignModal>>();
  const startVerifyRef = ref<InstanceType<typeof StartVerify>>();
  const treeData = ref<DataNode[]>([]);
  const pageRef = ref<any>();

  const emit = defineEmits(['openVerify', 'print']);

  // 请求数据
  const getPage = async (params: any) => {
    try {
      const data = {
        ...params,
        processCode: CHECK_STATUS.CONFIRM,
      };
      if (!params.categoryId || params.categoryId === 'all') {
        return await getCheckOrderPage({
          ...data,
          categoryId: 0,
          categoryFlag: true,
        });
      }

      return await getCheckOrderPage(data);
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  const fetchTableData = () => {
    pageRef.value.fetchData();
  };

  const submitSuccess = async (printFlag, orderNo) => {
    if (printFlag) {
      await print(orderNo);
    }
    fetchTableData();
  };

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
  const openSignModal = (list: any[], flag: boolean) => {
    signModalRef.value?.openModal(list, flag);
  };

  // 打开发起请验弹窗
  const openStartVerify = () => {
    startVerifyRef.value?.openModal();
  };

  // 查看请验详情
  const openVerify = (row: any) => {
    emit('openVerify', row);
  };

  const printVerifyRef = ref<InstanceType<typeof PrintVerify>>();
  const showPrint = ref<boolean>(false);
  // 打印
  const print = async data => {
    try {
      const res = await getCheckOrderInfo(data);
      showPrint.value = true;
      nextTick(() => {
        printVerifyRef.value?.printDom(res.data);
        setTimeout(() => {
          showPrint.value = false;
        }, 0);
      });
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  const { columnsFirst, formFirstProps, rowData } = useTable({
    props: {
      openSignModal,
      openVerify,
      print,
    },
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
