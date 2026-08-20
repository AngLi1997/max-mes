<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="goBack">{{ t('批次摘要') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('批次查询') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="goBack">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('摘要信息')"></BMTableTitle>
    <BMDescriptions :list="descData" :column="3" :showBottomBorder="false" hasTitle></BMDescriptions>
    <div class="form_box">
      <BMForm ref="myFormRef" v-bind="formProps" @submit="searchClick" @formModelChange="formModelChange"></BMForm>
    </div>
    <view class="tableTitle">
      <Button type="primary" @click="exportFile">{{ t('导出') }}</Button>
    </view>
    <div style="height: calc(100% - 250px)">
      <BMTable
        v-if="showTable"
        ref="tableInstance"
        :columns="columns"
        :show-tool-bar="false"
        :show-search-border="false"
        :data-request="loadData"
        row-key="id"
        :search="false"
        :pagination="{
          pageSize: 20,
        }"
        :scroll="{ x: 844, y: 400 }"></BMTable>
    </div>
  </BreadcrumbButton>
</template>
<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { BMForm, BMTable, FormProps, TableColumn, BMDescriptions, BMTableTitle } from '@bmos/components';
  import { Breadcrumb, BreadcrumbItem, Button } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { reqPlanInfoListUnTerminatePlanByProcessId, queryProductDataPage, exportProductDataPage } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';

  const props = withDefaults(defineProps<{ rowData: any }>(), { rowData: {} });
  const emit = defineEmits(['close']);
  const myFormRef = ref();
  const goBack = () => {
    emit('close');
  };
  const tableInstance = ref();
  const showTable = ref(true);

  const exportFile = async () => {
    const params = await myFormRef.value?.validate();
    const res = await exportProductDataPage({
      startDate: params.date?.[0] || '',
      endDate: params.date?.[1] || '',
      batchNos: params.batchNos,
      lotSummaryId: props.rowData.id,
    });
    fileStreamDownload(res, props.rowData.name);
  };
  const tableData = ref<any>([]);
  const formModelChange = () => {
    tableData.value = [];
  };

  const loadData = async (params: any): Promise<any> => {
    // 默认查最近30天
    const formData = await myFormRef.value?.validate();
    const req = {
      startDate: formData.date?.[0] || '',
      endDate: formData.date?.[1] || '',
      batchNos: formData.batchNos,
      lotSummaryId: props.rowData.id,
      ...params,
    };
    const res = await queryProductDataPage(req);
    tableData.value = res.data.page.list.map((item: any) => {
      item.data.map((field: any) => {
        item[`${field.fieldId}_${field.procedureStepId}`] = field.value;
      });
      return item;
    });
    res.data.page.list = tableData.value;
    const deleteColumn = [] as any;
    tableInstance.value.innerColumns.forEach((item: any) => {
      if (item.dataIndex != 'batchNo') {
        deleteColumn.push(item.dataIndex);
      }
    });
    await tableInstance.value.removeColumn(deleteColumn);
    const newColumn = [] as any;
    res.data.titles.forEach((item: any) => {
      newColumn.push({
        title: item.name,
        dataIndex: `${item.fieldId}_${item.procedureStepId}`,
        width: 100,
        resizable: true,
        hideInSearch: true,
      });
    });
    await tableInstance.value.addColumn(newColumn);
    return {
      ...res,
      data: {
        ...res.data.page,
      },
    };
  };

  // 查询按钮点击
  const searchClick = async () => {
    tableInstance.value.fetchData();
  };

  const descData = ref<any>([
    {
      label: t('摘要名称'),
      value: 'name',
    },
    {
      label: t('产品信息'),
      value: 'productName',
    },
    {
      label: t('工艺名称'),
      value: 'processName',
    },
  ]);
  onMounted(async () => {
    descData.value = descData.value.map((item: any) => {
      item.value = props.rowData[item.value];
      return item;
    });
    // 获取生产批次
    const { data } = await reqPlanInfoListUnTerminatePlanByProcessId(props.rowData.processId);
    myFormRef.value?.updateSchema({
      field: 'batchNos',
      componentProps: {
        options: [...data],
      },
    });
  });

  // 表单属性
  const formProps: Ref<FormProps> = ref({
    initialValues: {
      //默认值
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 130,
    schemas: [
      {
        field: 'date',
        label: t('生产时间'),
        component: 'RangePicker',
        colProps: {
          span: 6,
        },
        componentProps: () => {
          return {
            order: 7,
            componentProps: {
              valueFormat: 'YYYY-MM-DD',
            },
          };
        },
      },
      {
        field: 'batchNos',
        component: 'Select',
        label: t('生产批号'),
        colProps: {
          span: 12,
        },
        componentProps: () => {
          return {
            fieldNames: {
              label: 'batchNo',
              value: 'batchNo',
            },
            mode: 'multiple',
            options: [],
          };
        },
      },
    ],
  });
  const columns: TableColumn[] = [
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 100,
      resizable: true,
      hideInSearch: true,
      fixed: 'left',
    },
  ];
</script>
<style scoped lang="less">
  .form_box {
    border-top: 5px solid #f5f7fa;
    border-bottom: 5px solid #f5f7fa;
    margin-bottom: 20px;
    padding-top: 10px;
  }
  .tableTitle {
    text-align: right;
    margin-bottom: 16px;
  }
</style>
