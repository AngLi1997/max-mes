<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验进程')"
    wrapClassName="modalSizeLarge"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    @cancelModal="closeModal">
    <template #formBefore>
      <Steps :current="current" :items="stepsItems" label-placement="vertical" />
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { Steps } from 'ant-design-vue';
  import { BMModalForm } from '@bmos/components';
  import { InspectionProcessMap } from '@/types';
  const open = ref(false);
  const modalFormRef = ref();

  const stepsItems = [
    {
      title: t('标本待接收'),
      disabled: true,
    },
    {
      title: t('检验待执行'),
      disabled: true,
    },
    {
      title: t('检验执行中'),
      disabled: true,
    },
    {
      title: t('数据待签发'),
      disabled: true,
    },
    {
      title: t('报告待签发'),
      disabled: true,
    },
    {
      title: t('报告已签发'),
      disabled: true,
    },
  ];

  const record = ref<any>({});

  const current = computed(() => {
    return InspectionProcessMap[record?.value] ?? 0;
  });

  const openModal = (inspectionProcess: string) => {
    record.value = inspectionProcess;
    open.value = true;
  };

  const closeModal = () => {
    open.value = false;
  };

  defineExpose({
    openModal,
  });
</script>

<style scoped></style>
