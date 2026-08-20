<template>
  <NormalModalForm
    v-model:open="historyOpen"
    :title="t('操作历史')"
    wrapClassName="modalSizeMedium common-history-modal"
    :footer="null">
    <div class="history-container">
      <Steps v-if="StepList.length" direction="vertical" progress-dot :current="current">
        <Step v-for="item in StepList" :key="item.createTime" :title="item.createTime">
          <template #description>
            <Info
              :data="item"
              :showDetail="showDetail"
              :detail-label="detailLabel"
              :downFileApi="downFileApi"
              :downFileName="downFileName"
              :downFileType="downFileType" />
          </template>
        </Step>
      </Steps>
      <Empty v-else />
    </div>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { message } from 'ant-design-vue';
  import Info from './info/index.vue';
  import { t } from '@bmos/i18n';
  import { reqHistoryList } from '@/services';
  import { NormalModalForm } from '@bmos/components';

  const props = withDefaults(
    defineProps<{
      historyOpen: boolean;
      businessId: string;
      getApi?: (id: string) => Promise<any>;
      showDetail?: boolean;
      detailLabel?: any;
      downFileApi?: Function;
      downFileType?: string;
      downFileName?: string;
    }>(),
    {
      businessId: '',
      getApi: undefined,
      showDetail: false,
      detailLabel: () => [],
      downFileApi: undefined,
      downFileType: '',
      downFileName: '',
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
      if (props.getApi) {
        const { data } = await props.getApi(id);
        StepList.value = data.map((item: any) => {
          if (item.detail) {
            item.detail = JSON.parse(item.detail);
          }
          return item;
        });
        return;
      }
      const { data } = await reqHistoryList(id);
      StepList.value = data.map((item: any) => {
        if (item.detail) {
          item.detail = JSON.parse(item.detail);
        }
        return item;
      });
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
      .mes-steps {
        .mes-steps-item-container .mes-steps-item-title {
          color: var(--bmos-fourth-level-text-color);
        }
      }
    }
  }
</style>
