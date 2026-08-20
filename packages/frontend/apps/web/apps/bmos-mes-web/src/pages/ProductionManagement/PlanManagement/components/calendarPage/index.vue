<!-- 生产计划管理-日历调整页面 -->
<template>
  <div class="calendarPage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('生产计划管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t('日历调整') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
      </template>
      <Calendar :productionPlanId="productionPlanId"></Calendar>
    </BreadcrumbButton>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import Calendar from '@/pages/ProductionManagement/PlanCalendarNew/index.vue';
  const emits = defineEmits(['back']);
  const props = withDefaults(
    defineProps<{
      rowData: any;
    }>(),
    {},
  );
  const productionPlanId = ref<any>();
  // 返回到管理页面
  const back = () => {
    emits('back');
  };

  onMounted(() => {
    productionPlanId.value = props.rowData?.id;
  });
</script>
<style lang="less" scoped>
  .calendarPage {
    width: 100%;
    height: 100%;
    :deep(.content) {
      padding: 0px;
    }
  }
</style>
