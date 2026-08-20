<template>
  <div class="process-deal">
    <ProcessFlow
      v-if="activePage === CurPageActiveKeyEnum.ProcessFlow"
      :pageParams="pageParams"
      @view-next="processViewNext">
      <template #breadcrumb>
        <Breadcrumb class="crumb">
          <BreadcrumbItem class="crumb-allow-click" @click="toProcessApproval">
            {{ t('工艺审核') }}
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
    </ProcessFlow>
    <ProcedureFlow
      v-if="activePage === CurPageActiveKeyEnum.ProcedureFlow"
      :pageParams="procedureParams"
      @view-next="procedureViewNext">
      <template #breadcrumb>
        <Breadcrumb class="crumb">
          <BreadcrumbItem class="crumb-allow-click" @click="toProcessApproval">
            {{ t('工艺审核') }}
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
        <Button @click="toProcessView">{{ t('返回') }}</Button>
      </template>
    </ProcedureFlow>
    <ProcedureStepConfig v-if="activePage === CurPageActiveKeyEnum.ProcedureStep" :pageParams="ProcedureStepParams">
      <template #breadcrumb>
        <Breadcrumb class="crumb">
          <BreadcrumbItem class="crumb-allow-click" @click="toProcessApproval">
            {{ t('工艺审核') }}
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
        <Button @click="toProcedureView">{{ t('返回') }}</Button>
      </template>
    </ProcedureStepConfig>
  </div>
</template>

<script lang="tsx" setup>
  import ProcessFlow from '@/pages/ProductConfig/ProcessConfig/ProcessFlow/index.vue';
  import { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Cell } from '@antv/x6';
  import { CurPageActiveKeyEnum } from '../../types';
  import ApprovalBtns from '@/components/Approval/components/ApprovalBtns/index.vue';
  import ProcedureFlow from '@/pages/ProductConfig/ProcessConfig/ProcedureFlow/index.vue';
  import ProcedureStepConfig from '@/pages/ProductConfig/ProcessConfig/ProcedureStepConfig/index.vue';

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

  const activePage = ref<CurPageActiveKeyEnum>(CurPageActiveKeyEnum.ProcessFlow);

  const procedureParams = ref<Recordable>({});

  const processViewNext = (cell: Cell, procedureId: string) => {
    activePage.value = CurPageActiveKeyEnum.ProcedureFlow;
    procedureParams.value = {
      processId: props.pageParams?.processId,
      version: props.pageParams?.version,
      procedureId,
      versionId: props.pageParams?.versionId,
    };
  };

  const toProcessView = () => {
    activePage.value = CurPageActiveKeyEnum.ProcessFlow;
  };

  const toProcedureView = () => {
    activePage.value = CurPageActiveKeyEnum.ProcedureFlow;
  };

  const ProcedureStepParams = ref<Recordable>({});
  const procedureViewNext = (params: any) => {
    activePage.value = CurPageActiveKeyEnum.ProcedureStep;
    ProcedureStepParams.value = {
      ...params,
      processId: props.pageParams?.processId,
      version: props.pageParams?.version,
    };
  };
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
