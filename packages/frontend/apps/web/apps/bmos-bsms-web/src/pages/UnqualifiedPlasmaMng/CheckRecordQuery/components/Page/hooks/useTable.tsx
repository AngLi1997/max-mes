import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openView: any, enterView: any) => {
  const { auditResultDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
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
      title: t('不合格编号'),
      dataIndex: 'no',
      width: 190,
      hideInSearch: true,
      resizable: true,
    },
    // {
    //   title: t('血浆编号'),
    //   dataIndex: 'plasmaOrgNo',
    //   width: 170,
    //   hideInSearch: true,
    //   sorter: true,
    //   resizable: true,
    // },
    // {
    //   title: t('标本编号'),
    //   dataIndex: 'orgSampleNo',
    //   hideInSearch: true,
    //   width: 190,
    //   sorter: true,
    //   resizable: true,
    // },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      sorter: true,
      width: 160,
      resizable: true,
      formItemProps: {
        order: 4,
      },
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'name',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('受影响份数'),
      dataIndex: 'affectedPlasmaNumber',
      hideInSearch: true,
      width: 130,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.affectedPlasmaNumber ? (
          <a onClick={() => openView(record)}>{record?.affectedPlasmaNumber}</a>
        ) : (
          0
        );
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'applyBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applyDate',
      hideInSearch: true,
      width: 150,
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
          options: auditResultDict,
        },
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
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
          ifShow: hasPermission('170070004000002'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['auditDate', ['auditDateUp', 'auditDateDown'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
