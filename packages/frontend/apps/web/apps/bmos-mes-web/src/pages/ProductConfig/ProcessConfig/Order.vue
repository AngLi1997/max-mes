<!-- 工艺排序 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnDataSet">
          {{ t('工艺配置') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('工序排序') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnDataSet">{{ t('返回') }}</Button>
      <Button v-if="route.query?.allowEdit === '1'" type="primary" :loading="saveLoading" @click="save">
        {{ t('保存') }}
      </Button>
    </template>
    <BMDescriptions :list="detailList" :column="1" :showBottomBorder="false"></BMDescriptions>
    <BMTable
      ref="tableRef"
      :columns="tableColumns"
      :dataSource="tableDataSource"
      :pagination="false"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 400, y: 200 }">
      <template #expandedRowRender="{ record }">
        <BMTable
          :columns="stepTableColumns"
          :dataSource="record.procedureStepSortList"
          :pagination="false"
          :search="false"
          :showToolBar="false"
          :scroll="{ x: 400, y: 300 }" />
      </template>
    </BMTable>
  </BreadcrumbButton>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { BMDescriptions, DescriptionsItemProps, BMTable, TableInstance, TableColumn } from '@bmos/components';
  import { reqProcedureListProcessSortReq, reqProcedureSaveProcessSortReq } from '@/services';

  const route = useRoute();
  const router = useRouter();

  const returnDataSet = () => {
    router.push({
      name: 'process-config',
    });
  };

  const tableRef = ref<TableInstance>();
  const tableDataSource = ref<any[]>([]);

  const moveUp = (record: any) => {
    const index = tableDataSource.value.findIndex((item: { id: any }) => item.id === record.id);
    if (index === 0) return;
    const temp = tableDataSource.value[index];
    const sort = temp.sort;
    tableDataSource.value[index] = {
      ...tableDataSource.value[index - 1],
      sort,
    };
    tableDataSource.value[index - 1] = {
      ...temp,
      sort: sort - 1,
    };
  };
  const moveDown = (record: any) => {
    const index = tableDataSource.value.findIndex((item: { id: any }) => item.id === record.id);
    if (index === tableDataSource.value.length - 1) return;
    const temp = tableDataSource.value[index];
    const sort = temp.sort;
    tableDataSource.value[index] = {
      ...tableDataSource.value[index + 1],
      sort,
    };
    tableDataSource.value[index + 1] = {
      ...temp,
      sort: sort + 1,
    };
  };
  const tableColumns: TableColumn[] = [
    {
      title: t('序号'),
      width: 60,
      dataIndex: 'sort',
      fixed: 'left',
    },
    {
      title: t('工序名称'),
      width: 500,
      dataIndex: 'name',
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      hideInTable: route.query?.allowEdit !== '1',
      actions: ({ record }: any) => [
        {
          label: t('上移'),
          ifShow: route.query?.allowEdit === '1',
          onClick: () => {
            moveUp(record);
          },
        },
        {
          label: t('下移'),
          ifShow: route.query?.allowEdit === '1',
          onClick: () => {
            moveDown(record);
          },
        },
      ],
    },
  ];
  const moveStepUp = (record: any) => {
    const procedureItem = tableDataSource.value.find((item: { procedureStepSortList: any[] }) => {
      return item.procedureStepSortList.some((step: any) => step.id === record.id);
    });
    if (!procedureItem) return;
    const index = procedureItem.procedureStepSortList.findIndex((step: any) => step.id === record.id);
    if (index === 0) return;
    const temp = procedureItem.procedureStepSortList[index];
    const sort = temp.sort;
    procedureItem.procedureStepSortList[index] = {
      ...procedureItem.procedureStepSortList[index - 1],
      sort,
    };
    procedureItem.procedureStepSortList[index - 1] = {
      ...temp,
      sort: sort - 1,
    };
  };
  const moveStepDown = (record: any) => {
    const procedureItem = tableDataSource.value.find((item: { procedureStepSortList: any[] }) => {
      return item.procedureStepSortList.some((step: any) => step.id === record.id);
    });
    if (!procedureItem) return;
    const index = procedureItem.procedureStepSortList.findIndex((step: any) => step.id === record.id);
    if (index === procedureItem.procedureStepSortList.length - 1) return;
    const temp = procedureItem.procedureStepSortList[index];
    const sort = temp.sort;
    procedureItem.procedureStepSortList[index] = {
      ...procedureItem.procedureStepSortList[index + 1],
      sort,
    };
    procedureItem.procedureStepSortList[index + 1] = {
      ...temp,
      sort: sort + 1,
    };
  };
  const stepTableColumns: TableColumn[] = [
    {
      title: t('序号'),
      width: 60,
      dataIndex: 'sort',
      fixed: 'left',
    },
    {
      title: t('工序步骤/任务名称'),
      width: 500,
      dataIndex: 'name',
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      hideInTable: route.query?.allowEdit !== '1',
      actions: ({ record }: any) => [
        {
          label: t('上移'),
          ifShow: route.query?.allowEdit === '1',
          onClick: () => {
            moveStepUp(record);
          },
        },
        {
          label: t('下移'),
          ifShow: route.query?.allowEdit === '1',
          onClick: () => {
            moveStepDown(record);
          },
        },
      ],
    },
  ];
  const saveLoading = ref(false);
  const save = async () => {
    try {
      saveLoading.value = true;
      const data = tableRef.value?.getTableData();
      await reqProcedureSaveProcessSortReq(data);
      message.success(t('保存成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      saveLoading.value = false;
    }
  };
  const detailList = ref<DescriptionsItemProps[]>([]);
  onMounted(async () => {
    await nextTick();
    try {
      const { processName, processVersionId } = route.query;
      detailList.value = [
        {
          label: t('工艺名称'),
          value: processName as string,
        },
      ];
      const { data } = await reqProcedureListProcessSortReq({ processVersionId: processVersionId as string });
      // 获取table数据
      if (data && data.length && data[0].sort) {
        tableDataSource.value = data.map((item: any) => {
          if (item.procedureStepSortList && item.procedureStepSortList.length && item.procedureStepSortList[0].sort) {
            return item;
          }
          return {
            ...item,
            procedureStepSortList: item.procedureStepSortList?.map((step: any, stepIndex: number) => {
              return {
                ...step,
                sort: stepIndex + 1,
              };
            }),
          };
        });
      } else {
        tableDataSource.value = data.map((item: any, index: number) => {
          if (item.procedureStepSortList && item.procedureStepSortList.length && item.procedureStepSortList[0].sort) {
            return {
              ...item,
              sort: index + 1,
            };
          }
          return {
            ...item,
            sort: index + 1,
            procedureStepSortList: item.procedureStepSortList?.map((step: any, stepIndex: number) => {
              return {
                ...step,
                sort: stepIndex + 1,
              };
            }),
          };
        });
      }
    } catch (error) {
      console.log(error);
    }
  });
</script>
<style lang="less" scoped>
  .mes-descriptions {
    padding: 0;
    height: 38px;
  }
  .bmos-table {
    flex: 1;
    overflow: hidden;
  }
</style>
