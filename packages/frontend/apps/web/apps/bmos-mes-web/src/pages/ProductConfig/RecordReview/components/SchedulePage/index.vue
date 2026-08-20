<template>
  <Approval :dataRequest="handleAuditRequest" @back="toRecordApproval">
    <template #breadcrumb>
      <Breadcrumb class="crumb">
        <BreadcrumbItem class="crumb-allow-click" @click="toRecordApproval">
          {{ title || t('记录审核') }}
        </BreadcrumbItem>
        <BreadcrumbItem>{{ t('审核进度') }}</BreadcrumbItem>
      </Breadcrumb>
    </template>
  </Approval>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import Approval from '@/components/Approval/index.vue';
  import { getFlowAuditHistory } from '@/services';

  const router = useRouter();
  const route = useRoute();
  const title = ref();

  const toRecordApproval = () => {
    const fromList = route.query.fromList as string;
    if (fromList === 'fromList') {
      router.push({
        name: 'record-config',
      });
      return;
    }
    router.push({
      name: 'record-review',
    });
  };

  const handleAuditRequest = async () => {
    const processInstanceId = route.query.processInstanceId as string;
    const deploymentId = route.query.deploymentId as string;
    title.value = route.query.title;
    return await getFlowAuditHistory(processInstanceId, deploymentId);
  };
</script>

<style scoped lang="less"></style>
