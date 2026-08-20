<template>
  <Modal
    v-model:open="historyOpen"
    :title="t('操作历史')"
    wrapClassName="modalSizeMedium"
    class="common-history-modal"
    :footer="null">
    <div class="history-container">
      <Steps
        direction="vertical"
        progress-dot
        :current="current"
        v-if="StepList.length">
        <Step
          :title="item.createTime"
          v-for="item in StepList"
          :key="item.createTime">
          <template #description>
            <Info :data="item" />
          </template>
        </Step>
      </Steps>
      <Empty v-else />
    </div>
  </Modal>
</template>

<script setup lang="tsx">
  import { message } from 'ant-design-vue';
  import Info from './info/index.vue';
  import { t } from '@bmos/i18n';
  import { reqHistoryList } from '@/services';

  const props = withDefaults(
    defineProps<{
      historyOpen: boolean;
      businessId: string;
    }>(),
    {
      businessId: '',
    },
  );

  const emits = defineEmits(['update:historyOpen']);

  const historyOpen = computed<boolean>({
    get() {
      return props.historyOpen;
    },
    set(val) {
      emits('update:historyOpen', val);
    },
  });

  // 监听 open
  watch(
    () => historyOpen.value,
    val => {
      if (val && props.businessId) {
        getLog(props.businessId);
      }
    },
  );

  const StepList = ref<any[]>([]);

  const getLog = async (id: string) => {
    try {
      const { data } = await reqHistoryList(id);
      StepList.value = data;
    } catch (error) {
      message.error(t('查询失败'));
      StepList.value = [];
    }
  };
  const current = computed(() => {
    return StepList.value.length;
  });
</script>

<style lang="less">
  .common-history-modal {
    .history-container {
      .lims-steps {
        .lims-steps-item-container .lims-steps-item-title {
          color: var(--bmos-fourth-level-text-color);
        }
      }
    }
  }
</style>
