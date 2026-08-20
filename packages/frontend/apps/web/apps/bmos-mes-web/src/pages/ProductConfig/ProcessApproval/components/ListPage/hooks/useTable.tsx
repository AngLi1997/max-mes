import { getProcessAuditTodoReq } from '@/services';
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
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 160,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productCode',
      width: 120,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 160,
      resizable: true,
    },
    {
      title: t('版本号'),
      dataIndex: 'version',
      width: 120,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'description',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectDate',
      width: 160,
      hideInSearch: true,
      customRender: ({ record }) => {
        return record.effectDate === '-' ? t('立即生效') : record.effectDate;
      },
    },
    {
      title: t('审核节点名称'),
      dataIndex: 'nodeName',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      dataIndex: 'startByUsername',
      width: 120,
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
      actions: ({ record }) => [
        {
          label: t('处理'),
          ifShow: hasPermission('120020007000001'),
          onClick: () => {
            next &&
              next(2, {
                processId: record.processId,
                version: record.version,
                payload: record.payload,
                processInstanceId: record.processInstanceId,
                taskId: record.taskId,
                deploymentId: record.deploymentId,
                nodeId: record.nodeId,
                executionId: record.executionId,
                versionId: record.id,
              });
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120020007000002'),
          onClick: () => {
            router.push({
              name: 'process-audit-schedule',
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
    return getProcessAuditTodoReq(newParams);
  };

  return {
    tableInstance,
    columns,
    loadData,
  };
};
