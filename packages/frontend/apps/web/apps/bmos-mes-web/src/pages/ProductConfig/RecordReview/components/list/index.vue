<template>
  <div class="record-review">
    <BMTable
      :columns="columns"
      :showToolBar="false"
      :data-request="(reqRecordAuditPage as DataRequestFn)"
      :pagination="{
        pageSize: 20,
      }"
      :scroll="{ x: 1144, y: 500 }"
      :formProps="{
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      }"></BMTable>
  </div>
</template>

<script setup lang="tsx">
  import { BMTable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import type { DataRequestFn, TableColumn } from '@bmos/components';
  import { reqRecordAuditPage } from '@/services';
  import { usePermissionStore } from '@/stores/permission';
  import router from '@/router';

  const { hasPermission } = usePermissionStore();
  const next: Function | undefined = inject('switchGo');
  const columns: TableColumn[] = [
    {
      title: t('记录名称'),
      dataIndex: 'name',
      resizable: true,
      fixed: 'left',
      width: 100,
    },
    {
      title: t('版本号'),
      dataIndex: 'version',
      resizable: true,
      width: 60,
      hideInSearch: true,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      resizable: true,
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('审核节点名称'),
      dataIndex: 'nodeName',
      resizable: true,
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      dataIndex: 'processStartByName',
      resizable: true,
      width: 60,
      hideInSearch: true,
    },
    {
      title: t('发起时间'),
      dataIndex: 'processStartTime',
      resizable: true,
      width: 100,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('处理'),
          ifShow: hasPermission('120020002000001'),
          onClick: () => {
            next && next(1, { ...record });
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120020002000002'),
          onClick: () => {
            router.push({
              name: 'record-audit-schedule',
              query: {
                processInstanceId: record.processInstanceId,
                deploymentId: record.deploymentId,
              },
            });
          },
        },
      ],
    },
  ];
</script>

<style scoped lang="less">
  .record-review {
    background-color: #fff;
    height: 100%;
    padding: var(--bmos-padding-small);
  }
</style>
