<!-- 任务看板 -->
<template>
  <div class="task-board">
    <div style="display: flex; justify-content: flex-start; align-items: center; gap: 12px">
      <BMTableTitle :title="t('任务看板')" />
      <span>{{ `${t('更新时间')}: ${dayjs().format('YYYY-MM-DD HH:mm:ss')}` }}</span>
    </div>
    <div style="height: 94%">
      <BMTable
        ref="tableInstance"
        :data-source="curTableData"
        :columns="columns"
        row-key="id"
        :auto-height="true"
        :autoHeightOffset="24"
        :scroll="{ x: 1380, y: 300 }"
        :showToolBar="false"
        :showRefresh="false"
        :search="false"
        :pagination="false"></BMTable>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMTableTitle, BMTable, TableInstance, type TableColumn } from '@bmos/components';
  import { getAuditTaskList } from '@/services';
  import { t } from '@bmos/i18n';
  import dayjs from 'dayjs';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'TaskBoard',
  });

  const tableInstance = ref<TableInstance>();
  const router = useRouter();
  const curTableData = ref<any>([]);

  const routerMap = ref<any>({});

  const columns = ref<TableColumn[]>([
    {
      title: t('功能模块'),
      dataIndex: 'moduleName',
      width: 200,
      resizable: true,
    },
    {
      title: t('页面'),
      dataIndex: 'pageName',
    },
    {
      title: t('待审数量'),
      dataIndex: 'totalTask',
      width: 120,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }: any) => [
        {
          label: t('前往页面'),
          onClick: () => {
            router.push({ name: routerMap.value[record.menuId] });
          },
        },
      ],
    },
  ]);

  const initData = async () => {
    try {
      const { data } = await getAuditTaskList();
      curTableData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onActivated(async () => {
    await initData();
  });

  onMounted(async () => {
    router.getRoutes().forEach((item: any) => {
      if (item.meta?.id) {
        routerMap.value[item.meta?.id] = item.name;
      }
    });
    await initData();
  });
</script>

<style lang="less" scoped>
  .task-board {
    background-color: #fff;
    padding: 12px;
    height: 100%;
    overflow: auto;
  }
</style>
