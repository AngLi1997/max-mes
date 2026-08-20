<!-- 修订记录 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnProductProgress">
          {{ title }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('修订记录') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnProductProgress">{{ t('返回') }}</Button>
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
      :pagination="{
        pageSize: 20,
      }"
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
    FormProps,
    DataRequestFn,
    TableColumn,
  } from '@bmos/components';
  import { reqExecutePlanModifyList, reqPlanDetail } from '@/services';

  const router = useRouter();

  const props = withDefaults(
    defineProps<{
      productPlanId: string;
      title: string;
      returnRouteName: string;
    }>(),
    {
      productPlanId: '',
      title: '',
      returnRouteName: '',
    },
  );

  const returnProductProgress = () => {
    router.push({
      name: props.returnRouteName,
    });
  };

  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 12,
    },
    // 是否展示更多
    showAdvancedButton: false,
  });

  const columns: TableColumn[] = [
    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
      width: 100,
      fixed: 'left',
    },
    {
      title: t('任务/步骤名称'),
      dataIndex: 'procedureStepName',
      width: 100,
      formItemProps: {
        formItemProps: {
          labelCol: { span: 10 },
        },
      },
    },
    {
      title: t('原值'),
      dataIndex: 'originalValue',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('新值'),
      dataIndex: 'newValue',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('修订人'),
      dataIndex: 'operationUserName',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('复核人'),
      dataIndex: 'reviewUserName',
      width: 140,
      hideInSearch: true,
    },
    {
      title: t('修订时间'),
      dataIndex: 'operationTime',
      width: 140,
      hideInSearch: true,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 100,
      hideInSearch: true,
    },
  ];

  const loadData: DataRequestFn = async (params): Promise<any> => {
    const { productPlanId }: any = params;
    if (productPlanId) {
      return await reqExecutePlanModifyList(params);
    }
    return [];
  };

  const detailList = ref<DescriptionsItemProps[]>([]);
  const extraParams = ref<any>();
  onMounted(async () => {
    extraParams.value = {
      productPlanId: props.productPlanId,
    };
    await nextTick();
    try {
      const { data: planDetail } = await reqPlanDetail(props.productPlanId);
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
