<template>
  <Approval :dataRequest="request" notAllowClick @back="toBatchReleaseApproval">
    <template #breadcrumb>
      <Breadcrumb class="crumb">
        <template v-if="route.query.fromList">
          <BreadcrumbItem class="crumb-allow-click" @click="toBatchReleaseMange">
            {{ t('批签发管理') }}
          </BreadcrumbItem>
          <BreadcrumbItem class="crumb-allow-click" @click="toBatchReleaseApproval">
            {{ t('版本管理') }}
          </BreadcrumbItem>
        </template>
        <BreadcrumbItem v-else class="crumb-allow-click" @click="toBatchReleaseApproval">
          {{ t('批签发审核') }}
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

  const request = async () => {
    const processInstanceId = route.query.processInstanceId as string;
    const deploymentId = route.query.deploymentId as string;
    return await getFlowAuditHistory(processInstanceId, deploymentId);
  };

  const toBatchReleaseApproval = () => {
    const fromList = route.query.fromList as string;
    if (fromList === 'fromList') {
      router.back();
      return;
    }
    router.push({
      name: 'BatchReleaseReview',
    });
  };

  const toBatchReleaseMange = () => {
    router.push({
      name: 'BatchReleaseManagement',
    });
  };
</script>

<style lang="less" scoped></style>
