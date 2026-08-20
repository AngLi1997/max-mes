<!-- 发布 弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    wrapClassName="modalSizeLarge"
    :show-cancel-button="false"
    @okModal="cancel">
    <template #title>
      <div class="header">
        <CloseCircleFilled style="color: red; font-size: 18px" />
        <h2>{{ t('发布错误') }}</h2>
      </div>
    </template>
    <template #formBefore>
      <p style="margin: 8px 0 0 0">
        {{ t('本次申请数据无法提交审核，校验异常信息如下') }}
      </p>
      <div
        :style="{
          height: dataList.length > 0 && dataList.length < 20 ? `${dataList.length * 44 + 122}px` : '50vh',
        }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="dataList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 700, y: 200 }"
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
  import { BMModalForm, BMTable } from '@bmos/components';
  import { CloseCircleFilled } from '@ant-design/icons-vue';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);

  const openModal = async (rows: any) => {
    dataList.value = rows && rows.length > 0 ? rows : [];
    open.value = true;
    await nextTick();
    console.log('tableRef', tableRef.value);
  };

  const cancel = () => {
    open.value = false;
    dataList.value = [];
    // tableRef.value?.fetchData();
  };

  defineExpose({ openModal });
</script>

<style lang="less" scoped>
  .header {
    margin-top: 8px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    h2 {
      margin: 0;
      margin-left: 10px;
      font-size: 18px;
      font-weight: bold;
    }
  }
</style>
