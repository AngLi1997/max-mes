<!-- 分析项管理 -->
<template>
  <div class="parameter-config-table">
    <BMTable
      ref="tableRef"
      :data-request="loadData"
      :columns="columns"
      :formProps="formProps"
      :showRefresh="false"
    >
    <template #toolbar>
      <Button type="primary" @click="watchEditInfo({}, MODAL_STATUS.ADD)">{{ t('新增') }}</Button>
    </template>
    </BMTable>
  </div>
  <InfoDialog 
    ref="infoDialogRef"
    @submitSuccess="fetchTableData"
  />
</template>

<script setup lang="ts">
import {
  BMTable,
  TableInstance,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';
import {
  useTable
} from './hooks';
import InfoDialog from './components/InfoDialog.vue';
import { MODAL_STATUS } from './types/enum';
import { 
  getAnalyzePage
} from '@/services/index';
import { message } from 'ant-design-vue';

const tableRef = ref<TableInstance>();
const infoDialogRef = ref<InstanceType<typeof InfoDialog>>();

const loadData = async (params: any) => {
  try {
    return await getAnalyzePage(params);
  } catch (error) {
    message.error(error.message);
  }
  

  // return new Promise(resolve => {
  //   resolve({
  //     data: [
  //       {
  //         id: '1',
  //         name: 'tttt',
  //         code: 'PN658776577',
  //       },
  //     ],
  //     total: 3,
  //   });
  // });
};

const watchEditInfo = (row: any, type: MODAL_STATUS) => {
  infoDialogRef.value.openModal(row, type);
}

const { columns, formProps, viewReportModalOpen, rowData } =useTable({
  props: {
    watchEditInfo
  }
});

// 刷新列表
const fetchTableData = async () => {
  tableRef.value?.fetchData();
  console.log('getFormProps', tableRef.value)
}

</script>

<style scoped lang="less">
.parameter-config-table {
  padding: var(--bmos-padding-small);
  background-color: var(--bmos-primary-color-white);
  height: 100%;
  .bmos-table {
    height: 100%;
  }
}
</style>