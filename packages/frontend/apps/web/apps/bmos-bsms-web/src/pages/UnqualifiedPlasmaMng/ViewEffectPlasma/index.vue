<!-- 查看影响血浆 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('查看影响血浆')"
    wrapClassName="modalSizeLarge"
    :cancel-button-text="t('关闭')"
    :show-ok-button="false">
    <template #formBefore>
      <div class="header">
        <div class="header-info">
          <span>{{ `${t('总数量')}: ${cntObj.amount}` }}</span>
          <span>{{ `${t('总重量')}: ${cntObj.totalWeight}` }}</span>
        </div>
        <Button type="primary" :loading="loading" @click="exportExcel">{{ t('导出') }}</Button>
      </div>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          headerTitle=""
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
  import { useTable } from './hooks/useTable';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { getUnqualifiedPlasmaAffectedPlasma, exportUnqualifiedPlasmaAffectedPlasma } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const unqualifiedPlasmaInfoId = ref<any>({});

  const openModal = async (id: any) => {
    unqualifiedPlasmaInfoId.value = id;
    open.value = true;
  };

  const cntObj = ref<any>({
    amount: 0,
    totalWeight: 0,
  });

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,
      unqualifiedPlasmaInfoId: unqualifiedPlasmaInfoId.value,
    };
    // return getLoginLogList(datas);
    const { data } = await getUnqualifiedPlasmaAffectedPlasma(datas);

    cntObj.value = {
      amount: data.amount,
      totalWeight: data.totalWeight,
    };

    return {
      data: data.detailList,
    };
  };

  const cancel = () => {
    open.value = false;
    cntObj.value = {
      amount: 0,
      totalWeight: 0,
    };
  };

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);
  const exportExcel = async () => {
    try {
      loading.value = true;
      const res = await exportUnqualifiedPlasmaAffectedPlasma({
        unqualifiedPlasmaInfoId: unqualifiedPlasmaInfoId.value,
      });
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  defineExpose({ openModal, cancel });
</script>

<style lang="less" scoped>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    &-info {
      display: flex;
      justify-content: flex-start;
      align-items: center;

      span {
        font-size: 14px;
        &:nth-child(1) {
          margin-right: 20px;
        }
      }
    }
  }
</style>
