<template>
  <Approval :dataRequest="request" @back="toPlanApproval">
    <template #breadcrumb>
      <Breadcrumb class="crumb">
        <BreadcrumbItem class="crumb-allow-click" @click="toPlanApproval">
          <!-- {{ t('计划审核') }} -->
          {{ t(props.source) }}
        </BreadcrumbItem>
        <BreadcrumbItem v-if="props.source == t('生产指令单')">{{ t('审核进度') }}</BreadcrumbItem>
      </Breadcrumb>
    </template>
  </Approval>
</template>

<script lang="ts" setup>
  import Approval from '@/components/Approval/index.vue';
  import { getFlowAuditHistory } from '@/services';
  import { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  const emit = defineEmits(['toPlanApproval']);

  const props = defineProps({
    // 来源为生产计划或计划审核
    source: {
      type: String,
      default: () => '',
    },

    pageParams: {
      type: Object as PropType<Recordable>,
      default: () => {},
    },
  });

  const request = async () => {
    return await getFlowAuditHistory(props.pageParams.processInstanceId, props.pageParams.deploymentId);
  };
  // 返回
  const toPlanApproval = () => {
    emit('toPlanApproval');
  };
</script>

<style lang="less" scoped></style>
