<!-- 待检验任务 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :requests="[postStatisticsUnchecked as any]"
    :titles="[t('待检验数据')]"
    :search="[false]"
    :tableFields="[
      {
        default: { inspectItemCode: dateSelectValue },
      },
    ]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Select v-model:value="dateSelectValue" :options="InspectionProjectDict" style="width: 200px" />
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postStatisticsUnchecked } from '@/services';
  import { paginationBig } from '@/utils';
  import { t } from '@bmos/i18n';
  import { InspectionProjectEnum } from '@/types';

  defineOptions({
    name: 'PendingInspectionTasks',
    inheritAttrs: false,
  });

  const { InspectionProjectDict } = getDicts();

  const { columnsFirst, pageRef } = useTable();

  const dateSelectValue = ref<`${InspectionProjectEnum}`>(InspectionProjectEnum.ProteinContent);
</script>
