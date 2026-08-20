<!-- 大屏显示 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnDataSet">
          {{ t('工艺配置') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('大屏显示') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnDataSet">{{ t('返回') }}</Button>
      <Button v-if="route.query?.allowEdit === '1'" type="primary" :loading="saveLoading" @click="save">
        {{ t('保存') }}
      </Button>
    </template>
    <div class="detailList">
      <span>{{ t('工艺名称') }}:{{ detailList?.processName }}</span>
      <span v-if="detailList?.version" :style="route.query?.allowEdit !== '1' ? 'color: red' : ''">
        {{ t('版本号') }}:{{ detailList?.version }}
      </span>
    </div>
    <BMTable
      ref="tableRef"
      :columns="tableColumns"
      :dataSource="tableDataSource"
      :pagination="false"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 400, y: 200 }"></BMTable>
  </BreadcrumbButton>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { Input, message, Checkbox } from 'ant-design-vue';
  import { BMTable, TableInstance, TableColumn } from '@bmos/components';
  import { reqAllProcessGetDashboardConfig, reqAllProcessSaveDashboardConfig } from '@/services';

  const route = useRoute();
  const router = useRouter();
  const returnDataSet = () => {
    router.push({
      name: 'process-config',
    });
  };

  const detailList = ref<any>();
  const tableRef = ref<TableInstance>();
  const tableDataSource = ref<any[]>([]);
  // 上移
  const moveUp = (record: any) => {
    const index = tableDataSource.value.findIndex(
      (item: { procedureId: any }) => item.procedureId === record.procedureId,
    );
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
  // 下移
  const moveDown = (record: any) => {
    const index = tableDataSource.value.findIndex(
      (item: { procedureId: any }) => item.procedureId === record.procedureId,
    );
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
      title: t('显示'),
      align: 'left',
      dataIndex: 'effect',
      width: 45,
      resizable: true,
      customRender: ({ record }) => (
        <Checkbox
          checked={record.effect ? true : false}
          onClick={() => changeSwitch(record)}
          disabled={route.query?.allowEdit !== '1'}></Checkbox>
      ),
    },
    {
      title: t('序号'),
      width: 60,
      dataIndex: 'sort',
    },
    {
      title: t('工序名称'),
      width: 500,
      dataIndex: 'procedureName',
    },
    {
      title: t('显示名称'),
      width: 500,
      dataIndex: 'customName',
      customRender: ({ record }: any) => {
        return (
          <Input
            v-model:value={record.customName}
            disabled={route.query?.allowEdit !== '1'}
            placeholder={t('请输入')}></Input>
        );
      },
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
  const changeSwitch = (record: any) => {
    record.effect = !record.effect;
  };
  const saveLoading = ref(false);
  const save = async () => {
    try {
      saveLoading.value = true;
      const data = {
        procedureList: tableRef.value?.getTableData(),
        processId: route.query.processId,
        processVersion: route.query.version,
      };
      await reqAllProcessSaveDashboardConfig(data);
      message.success(t('保存成功'));
      router.push({
        name: 'process-config',
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      saveLoading.value = false;
    }
  };
  onMounted(async () => {
    await nextTick();
    try {
      const { processName, processId, version } = route.query;
      detailList.value = {
        processName,
        version,
      };
      const { data } = await reqAllProcessGetDashboardConfig({ processId: processId as string });
      // 获取table数据
      tableDataSource.value = data.procedureList?.map((item: any, index: number) => {
        return {
          ...item,
          customName: item?.customName || item?.procedureName,
          sort: index + 1,
        };
      });
    } catch (error: any) {
      message.error(error.message);
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
  .detailList {
    padding: 8px 8px 16px;
    span:nth-child(1) {
      margin-right: 16px;
    }
  }
</style>
