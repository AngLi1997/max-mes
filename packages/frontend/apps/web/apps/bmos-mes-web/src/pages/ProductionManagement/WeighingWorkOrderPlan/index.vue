<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :columns="[columns]"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id']"
    :search="[true, false]"
    :formProps="[formProps]"
    :requests="[reqWeighingWorkOrderPlanPage as any]">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('称量工单规划')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="120030014000001" type="primary" @click="handleManualPlan">
        {{ t('工单规划') }}
      </Button>
      <Button v-hasAuth="120030014000002" @click="handleAutoPlan">
        {{ t('自动规划') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { reqWeighingWorkOrderPlanPage, weighingWorkOrderPlanAuto } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import router from '@/router';
  import { useWarn } from '@/hooks';
  import { message } from 'ant-design-vue';

  const { warnModal } = useWarn();
  const { columns, pageRef, formProps } = useTable();

  const handleManualPlan = () => {
    router.push({
      name: 'weighing-work-order-manual-plan',
    });
  };

  const handleAutoPlan = () => {
    warnModal(t('请确认是否对称量需求自动规划?'), {
      async onOk() {
        try {
          await weighingWorkOrderPlanAuto();
          message.success(t('操作成功'));
          pageRef.value?.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };
</script>

<style scoped lang="less"></style>
