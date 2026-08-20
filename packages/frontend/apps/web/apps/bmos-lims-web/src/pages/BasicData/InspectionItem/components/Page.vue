<!-- 检验项目管理 -->
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
</template>

<script setup lang="tsx">
import {
  BMTable,
  TableInstance,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';
import {
  useTable
} from './hooks';
import { MODAL_STATUS } from '../types/enum';
import {
  getInspectionItemPage
} from '@/services/index';
import { message } from 'ant-design-vue';

const tableRef = ref<TableInstance>();

const emit = defineEmits(['watchEditInfo']);

const loadData = async (params: any) => {
  try {
    const res = await getInspectionItemPage(params);
    return res
  } catch (error: any) {
    message.error(error?.message);
  }
};

const watchEditInfo = (row: any, type: MODAL_STATUS) => {
  // infoDialogRef.value.openModal(row, type);
  emit('watchEditInfo', row, type);
}

const { columns, formProps, viewReportModalOpen, rowData } =useTable({
  props: {
    watchEditInfo
  }
});
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
</style>./components/hooks