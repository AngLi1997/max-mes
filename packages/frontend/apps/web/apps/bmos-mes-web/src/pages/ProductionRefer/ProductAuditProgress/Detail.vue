<!-- 审核详情 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnProcessProgress">
          {{ t('生产审核进度') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('审核详情') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnProcessProgress">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('生产信息')"></BMTableTitle>
    <BMDescriptions :list="detailList" :column="4" :showBottomBorder="false"></BMDescriptions>
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      :formProps="formProps"
      :show-tool-bar="false"
      :show-search-border="false"
      row-key="id"
      :extra-params="extraParams"
      :pagination="false"
      :scroll="{ x: 844, y: 400 }"></BMTable>
  </BreadcrumbButton>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    BMTableTitle,
    BMDescriptions,
    DescriptionsItemProps,
    BMTable,
    BMStateTag,
    FormProps,
    TableColumn,
  } from '@bmos/components';
  import { reqPlanDetail, reqPlanInfoAuditProgressDetail } from '@/services';
  import { AuditStatusClassMap } from './type';
  import { NodeFunctionEnumMap } from '@/pages/ProductConfig/ProcessConfig/ProcedureFlow/types';

  const route = useRoute();
  const router = useRouter();

  const returnProcessProgress = () => {
    router.push({
      name: 'product-audit-progress',
    });
  };

  const formProps: Ref<Partial<FormProps>> = ref({
    actionColOptions: {
      span: 12,
    },
    // 是否展示更多
    showAdvancedButton: false,
  });

  const columns: TableColumn[] = [
    {
      title: t('工序节点'),
      dataIndex: 'procedureName',
      width: 100,
    },
    {
      title: t('任务/步骤节点'),
      dataIndex: 'procedureStepName',
      width: 100,
      formItemProps: {
        labelWidth: 100,
      },
    },
    {
      title: t('节点类型'),
      dataIndex: 'nodeFunction',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return NodeFunctionEnumMap.get(record.nodeFunction);
      },
    },
    {
      title: t('工艺班次'),
      dataIndex: 'processChangeNumber',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => t('班次') + record.processChangeNumber,
    },
    {
      title: t('工序班次'),
      dataIndex: 'procedureChangeNumber',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => t('班次') + record.procedureChangeNumber,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <BMStateTag type={AuditStatusClassMap.get(record.auditStatus?.value)?.type}>
          {record.auditStatus?.name}
        </BMStateTag>
      ),
    },
    {
      title: t('开始时间'),
      dataIndex: 'startTime',
      width: 140,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('结束时间'),
      dataIndex: 'completeTime',
      width: 140,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('审核人'),
      dataIndex: 'auditUserName',
      width: 100,
      hideInSearch: true,
    },
  ];

  const loadData = async (params: any) => {
    const { productPlanId }: any = params;
    if (productPlanId) {
      return await reqPlanInfoAuditProgressDetail({
        ...params,
        orderBy: params.orderBy == 'completeTime' ? 'complete_time' : params.orderBy,
      });
    }
    return [];
  };

  const detailList = ref<DescriptionsItemProps[]>([]);
  const extraParams = ref<any>();
  onMounted(async () => {
    await nextTick();
    try {
      const { id } = route.query;
      const { data: planDetail } = await reqPlanDetail(id as string);
      extraParams.value = {
        productPlanId: id,
      };
      detailList.value = [
        {
          label: t('产品名称'),
          value: planDetail.productName as string,
        },
        {
          label: t('产品编码'),
          value: planDetail.productMergeCode as string,
        },
        {
          label: t('产品规格'),
          value: planDetail.productSpecification as string,
        },
        {
          label: t('工艺名称'),
          value: planDetail.processName as string,
        },
        {
          label: t('生产批号'),
          value: planDetail.batchNo as string,
        },
        {
          label: t('生产开始时间'),
          value: planDetail.startTime as string,
        },
      ];
    } catch (error) {}
  });
</script>
<style lang="less" scoped>
  .bmos-table {
    margin-top: var(--bmos-margin-large);
    flex: 1;
    overflow: hidden;
  }
</style>
