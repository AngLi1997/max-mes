<template>
  <Approval :dataRequest="request" @back="toProcessApproval">
    <template #breadcrumb>
      <Breadcrumb class="crumb">
        <BreadcrumbItem class="crumb-allow-click" @click="toProcessApproval">
          {{ title || t('工艺审核') }}
        </BreadcrumbItem>
        <BreadcrumbItem>{{ t('审核进度') }}</BreadcrumbItem>
      </Breadcrumb>
    </template>
  </Approval>
</template>

<script lang="ts" setup>
  import Approval from '@/components/Approval/index.vue';
  import { getFlowAuditHistory } from '@/services';
  import { t } from '@bmos/i18n';

  const router = useRouter();
  const route = useRoute();
  const title = ref();

  const request = async () => {
    const processInstanceId = route.query.processInstanceId as string;
    const deploymentId = route.query.deploymentId as string;
    title.value = route.query.title as string;
    return await getFlowAuditHistory(processInstanceId, deploymentId);
  };

  const toProcessApproval = () => {
    const fromList = route.query.fromList as string;
    if (fromList === 'fromList') {
      router.push({
        name: 'process-config',
      });
      return;
    }
    router.push({
      name: 'process-approval',
    });
  };
</script>

<style lang="less" scoped></style>
