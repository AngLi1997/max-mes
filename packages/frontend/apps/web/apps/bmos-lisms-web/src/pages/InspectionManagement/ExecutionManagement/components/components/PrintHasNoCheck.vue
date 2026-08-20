<template>
  <NormalModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('打印蛋白电泳检测报告')"
    :okButtonText="t('继续打印')"
    :cancelButtonText="t('返回复核')"
    :submit="submit"
    wrapClassName="modalSizeMedium"
    @cancelModal="cancelModal">
    <div class="print-error-tip">{{ errorMsg }}</div>
  </NormalModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { getReportFile } from '@/services';
  import { pdfPreview } from '@/utils';

  defineOptions({
    inheritAttrs: false,
  });
  const emit = defineEmits(['ok', 'cancel']);
  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      params: string;
      errorMsg: string;
    }>(),
    {
      params: '',
    },
  );

  const submit = async () => {
    try {
      const res = await getReportFile({
        fileType: 'INSPECT_PROTEIN_ELEC',
        params: props.params,
        inspectDate: JSON.parse(props.params).inspectDate,
      });
      await pdfPreview(res);
      open.value = false;
      emit('ok');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const cancelModal = () => {
    open.value = false;
    emit('cancel');
  };
</script>

<style lang="less" scoped>
  .print-error-tip {
    text-align: center;
    font-size: 16px;
  }
</style>
