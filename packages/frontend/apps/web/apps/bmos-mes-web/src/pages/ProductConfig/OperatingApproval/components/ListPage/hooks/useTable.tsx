import { getOperateRuleVersionPageTodoFlow } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { DataRequestFn, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { removeEmpty } from '@bmos/utils';

export type UseTableParams = { next?: Function };

export const useTable = ({ next }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const tableInstance = ref<TableInstance>();
  const columns: TableColumn[] = [
    {
      title: t('文件名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('文件编号'),
      dataIndex: 'code',
      width: 120,
      resizable: true,
      sorter: true,
    },
    {
      title: t('版本号'),
      dataIndex: 'version',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'remark',
      width: 120,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('审核类型'),
      dataIndex: 'auditType',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => <div>{record.auditTypeEnum.name}</div>,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectDate',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      dataIndex: 'processStartByName',
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
      width: 220,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('处理'),
          ifShow: hasPermission('120020013000001'),
          onClick: () => {
            next &&
              next(2, {
                id: record.id,
                processId: record.processId,
                version: record.version,
                payload: record.payload,
                processInstanceId: record.processInstanceId,
                taskId: record.taskId,
                deploymentId: record.deploymentId,
                nodeId: record.nodeId,
                executionId: record.executionId,
              });
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120020013000002'),
          onClick: () => {
            router.push({
              name: 'operating-audit-schedule',
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
  const loadData: DataRequestFn = async (params): Promise<any> => {
    const newParams = removeEmpty(params as any);
    return getOperateRuleVersionPageTodoFlow(newParams);
  };

  return {
    tableInstance,
    columns,
    loadData,
  };
};
