import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openView: any, enterView: any) => {
  const { reportAuditStatusDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('报告单编号'),
      dataIndex: 'reportBillNo',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return record?.originOrg;
      },
      formItemProps: {
        order: 1,
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      sorter: true,
      resizable: true,
      formItemProps: {
        order: 4,
      },
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },
    {
      title: t('受影响份数'),
      dataIndex: 'affectedCount',
      hideInSearch: true,
      width: 130,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.affectedCount ? <a onClick={() => openView(record)}>{record?.affectedCount}</a> : 0;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'applyByName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applyDate',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name}</span>;
      },
      formItemProps: {
        order: 3,
        component: 'Select',
        componentProps: {
          options: reportAuditStatusDict.filter((item: any) => item.value > 1),
        },
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditByName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditDate',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        order: 2,
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170070006000004'),
          onClick: () => {
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['auditDate', ['auditDateBegin', 'auditDateEnd'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
