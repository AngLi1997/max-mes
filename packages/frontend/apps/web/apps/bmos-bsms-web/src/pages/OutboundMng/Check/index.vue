<!-- 整盘/合并出库弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('出库确认')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          auto-height
          row-key="id"
          :show-tool-bar="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { getOutboundSortingPage, outboundCanDelivery } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { isNull } from '@bmos/utils';

  const { checkTypeDict } = getDicts();

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, updateSchema } = useForm();
  const { tableRef, columns } = useTable();

  const formData = ref<any>({});
  const actionType = ref<'batchNo' | 'trayNo'>('batchNo');

  const openModal = async (row: any, type: 'batchNo' | 'trayNo') => {
    formData.value = row;
    actionType.value = type;
    open.value = true;
    await nextTick();
    // if (type === 'trayNo') {
    //   return;
    // }
    updateSchema({
      field: 'verifyType',
      componentProps: {
        options: checkTypeDict.filter((item: any) => {
          if (row.warehouse?.value == 1) {
            return item.value === 1;
          }
          return !isNull(row.verifyType) ? item.value === row.verifyType : true;
        }),
      },
    });
    setFormModels({
      verifyType: row.typeValue == 1 && row.warehouse?.value == 2 ? (!isNull(row.verifyType) ? row.verifyType : 2) : 1,
    });
  };

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    if (actionType.value === 'trayNo') {
      return {
        data: [formData.value],
        totle: 1,
      };
    }
    const datas = {
      ...params,
      currentInventoryStatus: 2,
      batchNo: formData.value.batchNo,
    };
    return await getOutboundSortingPage(datas);
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        batchNo: formData.value.batchNo,
        sortingNo: actionType.value === 'trayNo' ? [formData.value.sortingPlanBatchNo] : undefined,
        trayNo: actionType.value === 'trayNo' ? [formData.value.bigContainerNo] : undefined,
        verifyType: formModal.verifyType,
      };

      return await outboundCanDelivery(params);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
