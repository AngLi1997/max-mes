<!-- 新增生产计划-计划日历调整 -->
<template>
  <div class="addPlan-calendar">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item>
            {{ t('生产计划管理') }}
          </breadcrumb-item>
          <breadcrumb-item @click="back">{{ t('新建生产计划') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('日历调整') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
      </template>
      <Calendar :source="source" :tableDataList="tableDataList"></Calendar>
    </BreadcrumbButton>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import Calendar from '@/pages/ProductionManagement/PlanCalendarNew/index.vue';
  const emits = defineEmits(['backAdd']);
  const props = withDefaults(
    defineProps<{
      source: string; //标注计划带过去的
      tableDataList: any[]; //所带的数组
    }>(),
    {
      source: '',
      tableDataList: () => [],
    },
  );
  // 返回到新增页
  const back = () => {
    emits('backAdd');
  };
</script>
<style lang="less" scoped>
  .addPlan-calendar {
    width: 100%;
    height: 100%;
    :deep(.content) {
      padding: 0px;
    }
  }
</style>
