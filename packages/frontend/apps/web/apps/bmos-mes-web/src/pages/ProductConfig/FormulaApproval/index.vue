<template>
  <!-- 生产BOM审核页 -->
  <div class="container">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :scroll="{ x: 1200, y: 400 }"
      :showRefresh="false"
      :pagination="{
        pageSize: 20,
      }"
      :formProps="formProps"></BMTable>
  </div>
</template>

<script lang="tsx" setup>
  import type { DataRequestFn, FormProps } from '@bmos/components';
  import { BMTable, TableColumn } from '@bmos/components';
  import {
    reqFormulaApproveList, //生产BOM审核分页
  } from '@/services';
  import { t } from '@bmos/i18n';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const tableInstance = ref<any>();
  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 12,
    },
    baseColProps: {
      span: 6,
    },
    showAdvancedButtonBadge: false,
    // 是否展示更多
    showAdvancedButton: false,
  });
  const loadData: DataRequestFn = async (params): Promise<any> => {
    const data = { ...params, formulaName: params?.name, name: undefined };
    return reqFormulaApproveList(data);
  };
  const columns: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 190,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('BOM名称'),
      dataIndex: 'name',
      width: 190,
      resizable: true,
    },
    {
      title: t('版本号'),
      dataIndex: 'versionNo',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'description',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('审核节点名称'), //后端暂时无此字段
      dataIndex: 'elementName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      dataIndex: 'processStartUser',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起时间'),
      dataIndex: 'processStartTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('处理'),
          ifShow: hasPermission('120020005000001'),
          onClick: () => {
            router.push({
              name: 'formula-config-formula-approval',
              query: {
                status: 'formulaApprove',
                versionId: record.id,
                taskId: record.taskId,
                deploymentId: record.deploymentId,
                elementKey: record.elementKey,
                executionId: record.executionId,
                processInstanceId: record.processInstanceId,
                settings: record.payload.settings,
              },
            });
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120020005000002'),
          onClick: () => {
            router.push({
              name: 'formula-audit-schedule',
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
  // 审核进度
  // const approvalProgress = (row: any) => {
  //   console.log(row, '处理');
  // };
</script>
<style lang="less" scoped>
  .container {
    padding: 16px;
    background-color: #fff;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
</style>
