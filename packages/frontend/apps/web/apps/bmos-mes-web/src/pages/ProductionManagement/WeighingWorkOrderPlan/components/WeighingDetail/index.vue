<!-- 工单称量详情 -->
<template>
  <BreadcrumbButton :loading>
    <template #breadcrumb>
      <Breadcrumb class="mes-breadcrumb">
        <breadcrumb-item @click="handleCancel">{{ t('称量工单规划') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('工单称量详情') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="handleCancel">{{ t('返回') }}</Button>
    </template>

    <div class="detail-content">
      <!-- 生产信息 -->
      <div class="section-container">
        <BMTableTitle :title="t('生产信息')" style="margin-bottom: 16px" />
        <BMForm ref="myFormRef" v-bind="formProps" />
      </div>
      <!-- 称量需求 -->
      <div style="flex: 1; overflow: hidden">
        <BMTable
          :dataSource="tableData"
          :columns="columns"
          row-key="requirementId"
          :scroll="{ x: 844, y: 400 }"
          :search="false"
          :showRefresh="false"
          :pagination="false">
          <template #headerTitle>
            <BMTableTitle :title="t('称量需求')" />
          </template>
          <!-- <template #expandColumnTitle>{{}}</template> -->
          <template #expandedRowRender="{ record }">
            <BMTable
              :dataSource="record.list"
              :columns="subColumns"
              row-key="storageMaterialId"
              :scroll="{ x: 844, y: 300 }"
              :search="false"
              :showRefresh="false"
              :pagination="false"></BMTable>
          </template>
        </BMTable>
      </div>
    </div>
  </BreadcrumbButton>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMTable, BMTableTitle, BMForm } from '@bmos/components';
  import { useRouter, useRoute } from 'vue-router';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { message } from 'ant-design-vue';
  import { getWeighingWorkOrderPlanGetWeighRecord } from '@/services';
  import { useForm, useTable } from './hooks';

  const router = useRouter();
  const route = useRoute();

  const requireId = computed(() => {
    return route.query.id as string;
  });

  const { myFormRef, formProps } = useForm();

  const { columns, subColumns, tableData } = useTable();

  // 取消
  const handleCancel = () => {
    router.push({
      name: 'weighing-work-order-plan',
    });
  };

  const loading = ref(false);
  const loadTableData = async () => {
    try {
      loading.value = true;
      const { data } = await getWeighingWorkOrderPlanGetWeighRecord({
        ticketId: requireId.value,
      });
      myFormRef.value?.setFieldsValue({
        ...route.query,
        materialName: `${route.query.materialMergeCode}-${route.query.materialName}`,
      });
      tableData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };

  onMounted(async () => {
    await loadTableData();
  });
</script>

<style scoped lang="less">
  .detail-content {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
    padding-bottom: 8px;
  }

  .expand-detail {
    padding: 16px;
    background-color: #f8f8f9;
  }
</style>
