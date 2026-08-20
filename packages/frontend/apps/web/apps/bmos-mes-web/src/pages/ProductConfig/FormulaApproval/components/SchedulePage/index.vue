<template>
  <Approval :dataRequest="request" @back="toFormulaApproval">
    <template #breadcrumb>
      <Breadcrumb class="crumb">
        <BreadcrumbItem class="crumb-allow-click" @click="toFormulaApproval">
          {{ title || t('生产BOM审核') }}
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

  const toFormulaApproval = () => {
    const fromList = route.query.fromList as string;
    if (fromList === 'fromList') {
      router.push({
        name: 'formula-configuration',
      });
      return;
    }
    router.push({
      name: 'formula-approval',
    });
  };
</script>

<style lang="less" scoped></style>
