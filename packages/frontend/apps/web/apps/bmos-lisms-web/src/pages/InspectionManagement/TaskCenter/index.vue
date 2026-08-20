<!-- 任务中心 -->
<template>
  <div class="task-center-container">
    <div class="card-data">
      <div class="header">
        <BMTableTitle :title="t('数据统计')" />
        <Select v-model:value="dateSelectValue" :options="dateSelectOptions" style="width: 100px" />
      </div>
      <div class="data-card-container">
        <div v-for="item in dataCardList" :key="item.label" class="data-card">
          <BMIcons
            :icon="item.icon"
            :style="{
              fontSize: '40px',
              width: '40px',
              height: '40px',
            }" />
          <div class="value-label">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value ?? '-' }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="table-data">
      <BMPageComponent
        ref="pageRef"
        :search="[true]"
        :rowKeys="['sampleNo']"
        :hideRightTree="true"
        :paginations="[paginationBig]"
        :tableFields="[
          {
            default: { inspectStatus: segmentedValue },
          },
        ]"
        :formProps="[formFirstProps]"
        :requests="[dataRequest as any]"
        :columns="[columnsFirst]"
        :show-indexs="[true]">
        <template #tableHeaderTitle0>
          <Segmented v-model:value="segmentedValue" :options="options" />
        </template>
        <template #tableHeaderToolbar0>
          <Space :size="24">
            <Space v-for="item in inspectionStatusDict" :key="item.value">
              <BMIcons
                :icon="item.icon"
                :style="{
                  fontSize: '16px',
                  width: '16px',
                  height: '16px',
                }" />
              <span
                :style="{
                  color: item.color,
                }">
                {{ item.label }}
              </span>
            </Space>
          </Space>
        </template>
      </BMPageComponent>

      <UnqualifiedModal v-model:modalOpen="unqualifiedModal" :tableData="unqualifiedTableData" />
      <EditModal
        v-model:modalOpen="editModal"
        :fixedInspectItems="firstRowData.fixedInspectItems"
        :specialInspectItems="firstRowData.specialInspectItems"
        :sampleNo="firstRowData.sampleNo"
        @ok="updateTableData" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMTableTitle, BMPageComponent, Recordable } from '@bmos/components';
  import { DateType } from './types';
  import { BMIcons } from '@bmos/icons';
  import { paginationBig } from '@/utils';
  import { useTable } from './hooks';
  import { postInspectTaskList } from '@/services';
  import { ChartTypeEnum } from '@/types';
  import UnqualifiedModal from './components/UnqualifiedModal.vue';
  import EditModal from './components/EditModal.vue';
  import { useWebSocket } from '@vueuse/core';

  defineOptions({
    name: 'TaskCenter',
    inheritAttrs: false,
  });

  const { inspectionStatusDict } = getDicts();

  const dateSelectValue = ref<`${DateType}`>(DateType.DAY);
  const dateSelectOptions = [
    { label: t('当天'), value: DateType.DAY },
    { label: t('当月'), value: DateType.MONTH },
    { label: t('当年'), value: DateType.YEAR },
  ];

  const dataRequest = async (params: any) => {
    return await postInspectTaskList({
      ...params,
      fetchInspectDataDetail: true,
      fetchSampleDetail: true,
    });
  };

  const chartData = ref<Recordable>({});
  const { data } = useWebSocket(`ws://${location.hostname}/api/centralized-lims/ws/statistics/refresh`, {
    autoReconnect: {
      retries: 3,
      delay: 1000,
    },
    heartbeat: {
      message: 'ping',
      interval: 1000 * 60 * 4,
    },
  });
  watch(data, () => {
    try {
      if (data.value) {
        chartData.value = JSON.parse(data.value)?.data;
      }
    } catch (error) {
      console.error(error);
    }
  });
  const dataCardList = computed(() => {
    if (dateSelectValue.value === DateType.DAY) {
      return [
        {
          label: `${t('待检总数')}（${t('批次')}）`,
          icon: 'BatchToBeInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_BATCH_DAY]?.result?.[0]?.value,
        },
        {
          label: `${t('已检总数')}（${t('批次')}）`,
          icon: 'BatchInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.INSPECTED_BATCH_DAY]?.result?.[0]?.value,
        },
        {
          label: `${t('待检数')}（${t('份')}）`,
          icon: 'OneToBeInspectedNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_SINGLE_DAY]?.result?.[0]?.value,
        },
        {
          label: `${t('检验中')}（${t('份')}）`,
          icon: 'OneInspecting',
          value: chartData.value[ChartTypeEnum.INSPECTING_SINGLE_DAY]?.result?.[0]?.value,
        },
        {
          label: `${t('已检验')}（${t('份')}）`,
          icon: 'OneInspected',
          value: chartData.value[ChartTypeEnum.INSPECTED_SINGLE_DAY]?.result?.[0]?.value,
        },
      ];
    } else if (dateSelectValue.value === DateType.MONTH) {
      return [
        {
          label: `${t('待检总数')}（${t('批次')}）`,
          icon: 'BatchToBeInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_BATCH_MONTH]?.result?.[0]?.value,
        },
        {
          label: `${t('已检总数')}（${t('批次')}）`,
          icon: 'BatchInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.INSPECTED_BATCH_MONTH]?.result?.[0]?.value,
        },
        {
          label: `${t('待检数')}（${t('份')}）`,
          icon: 'OneToBeInspectedNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_SINGLE_MONTH]?.result?.[0]?.value,
        },
        {
          label: `${t('检验中')}（${t('份')}）`,
          icon: 'OneInspecting',
          value: chartData.value[ChartTypeEnum.INSPECTING_SINGLE_MONTH]?.result?.[0]?.value,
        },
        {
          label: `${t('已检验')}（${t('份')}）`,
          icon: 'OneInspected',
          value: chartData.value[ChartTypeEnum.INSPECTED_SINGLE_WHOLE_MONTH]?.result?.[0]?.value,
        },
      ];
    } else {
      return [
        {
          label: `${t('待检总数')}（${t('批次')}）`,
          icon: 'BatchToBeInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_BATCH_YEAR]?.result?.[0]?.value,
        },
        {
          label: `${t('已检总数')}（${t('批次')}）`,
          icon: 'BatchInspectedTotalNumber',
          value: chartData.value[ChartTypeEnum.INSPECTED_BATCH_YEAR]?.result?.[0]?.value,
        },
        {
          label: `${t('待检数')}（${t('份')}）`,
          icon: 'OneToBeInspectedNumber',
          value: chartData.value[ChartTypeEnum.TO_INSPECT_SINGLE_YEAR]?.result?.[0]?.value,
        },
        {
          label: `${t('检验中')}（${t('份')}）`,
          icon: 'OneInspecting',
          value: chartData.value[ChartTypeEnum.INSPECTING_SINGLE_YEAR]?.result?.[0]?.value,
        },
        {
          label: `${t('已检验')}（${t('份')}）`,
          icon: 'OneInspected',
          value: chartData.value[ChartTypeEnum.INSPECTED_SINGLE_WHOLE_YEAR]?.result?.[0]?.value,
        },
      ];
    }
  });

  const segmentedValue = ref('TO_INSPECT');
  const options = [...inspectionStatusDict, { label: t('全部'), value: '' }];

  const {
    columnsFirst,
    pageRef,
    formFirstProps,
    unqualifiedTableData,
    unqualifiedModal,
    editModal,
    firstRowData,
    updateTableData,
  } = useTable();
</script>

<style scoped lang="less">
  .task-center-container {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    gap: 12px;
    .card-data {
      height: 130px;
      background-color: #fff;
      padding: var(--bmos-padding-small);
      .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .data-card-container {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16px;
        .data-card {
          display: flex;
          align-items: center;
          gap: 8px;
          justify-content: center;
          .label {
            font-size: 14px;
            color: var(--bmos-fourth-level-text-color);
          }
          .value {
            font-size: 20px;
            color: var(--bmos-first-level-text-color);
          }
        }
      }
    }
    .table-data {
      flex: 1;
      overflow: hidden;
    }
  }
</style>
