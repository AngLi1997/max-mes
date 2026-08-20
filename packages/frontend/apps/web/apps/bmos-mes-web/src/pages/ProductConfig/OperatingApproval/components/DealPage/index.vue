<template>
  <div class="process-deal">
    <ProceduresDetails :state="activePage" :detailsRow="pageParams">
      <template #breadcrumb>
        <Breadcrumb class="crumb">
          <BreadcrumbItem class="crumb-allow-click" @click="toProcessApproval">
            {{ t('操作规程审核') }}
          </BreadcrumbItem>
          <BreadcrumbItem>{{ t('审核处理') }}</BreadcrumbItem>
        </Breadcrumb>
      </template>
      <template #btn>
        <ApprovalBtns
          :settings="settings"
          :taskId="pageParams.taskId"
          :deploymentId="pageParams.deploymentId"
          :nodeId="pageParams.nodeId"
          :executionId="pageParams.executionId"
          :processInstanceId="pageParams.processInstanceId"
          @action="action" />
        <Button @click="toProcessApproval">{{ t('返回') }}</Button>
      </template>
    </ProceduresDetails>
  </div>
</template>

<script lang="tsx" setup>
  import ProceduresDetails from '@/pages/ProductConfig/OperatingProcedures/components/proceduresDetails/index.vue';
  import { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Cell } from '@antv/x6';
  import { modalStatus } from '../../types';
  import ApprovalBtns from '@/components/Approval/components/ApprovalBtns/index.vue';

  const next: Function | undefined = inject('switchGo');

  const props = defineProps({
    pageParams: {
      type: Object as PropType<Recordable>,
      default: () => {},
    },
  });

  const settings = computed(() => {
    try {
      return JSON.parse(props.pageParams.payload?.settings || {});
    } catch (error) {
      return {};
    }
  });

  const action = () => {
    next?.(0);
  };

  const toProcessApproval = () => {
    next?.(0);
  };

  const activePage = ref<modalStatus>(modalStatus.View);
</script>

<style lang="less" scoped>
  :deep(.process-flow-container) {
    padding: 0;
  }
  :deep(.procedure-flow-container) {
    padding: 0;
  }
  :deep(.procedure-step-config-container) {
    padding: 0;
  }
</style>
