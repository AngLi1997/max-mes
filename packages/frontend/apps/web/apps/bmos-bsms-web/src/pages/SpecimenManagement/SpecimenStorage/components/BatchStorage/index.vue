<!-- 整批入库 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('请输入入库批号')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit">
    <template v-if="props.isCheck" #formBefore>
      {{ t('整批核对已完成，是否进行整批入库？') }}
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { batchInStorage } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const props = defineProps({
    isCheck: {
      type: Boolean,
      default: false,
    },
  });

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (row: any) => {
    open.value = true;
    await nextTick();
    setFormModels({
      syncBatchNo: row.syncBatchNo,
      inWarehouseBatchNo: row.syncBatchNo,
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await batchInStorage(formModal);
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
