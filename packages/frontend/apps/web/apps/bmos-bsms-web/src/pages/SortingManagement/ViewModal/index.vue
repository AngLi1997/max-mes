<!-- 扫描弹窗 -->
<template>
  <BMModalForm
    ref="modelFormRef"
    v-model:open="open"
    :title="props.type == 1 ? t('当前血浆') : t('当前标本')"
    :show-cancel-button="false"
    wrapClassName="modalSizeLarge"
    @cancel="cancelModal"
    @okModal="cancelModal">
    <div :style="{ color: info.colour ?? 'black' }" class="info-main">
      <div class="info-type">{{ info.typeDescribe ?? '' }}</div>
      <div class="info-batchNo">{{ !info.planBatchNo || info.planBatchNo == '-1' ? '' : info.planBatchNo }}</div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { ref } from 'vue';

  const props = defineProps({
    type: {
      type: Number,
      default: 1,
    },
  });

  const open = ref(false);

  const emit = defineEmits(['checkSuccess']);

  const modelFormRef = ref<ModalFormInstance>();

  const cancelModal = () => {
    if (timer.value) {
      clearTimeout(timer.value);
    }
    open.value = false;
    info.value = {};
  };

  const info = ref<any>({});

  const timer = ref<any>(null);

  const openModal = (data: any) => {
    info.value = data;
    open.value = true;
    timer.value = setTimeout(() => {
      cancelModal();
    }, 1000);
  };

  defineExpose({
    openModal,
  });
</script>

<style lang="less" scoped>
  .info-main {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    height: 300px;
    font-size: 20px;
  }

  .info-type {
    font-size: 96px;
  }

  .info-batchNo {
    font-size: 68px;
  }
</style>
