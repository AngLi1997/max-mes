import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openViewEffectPlasmaModal: any, openCreateReport: any) => {
  const { handleStatusDict, inspectTypeDict, unqualifiedProjectDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const unqualifiedTypeMap = {
    1: t('标本'),
    2: t('血浆'),
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
    {
      title: t('不合格编号'),
      dataIndex: 'no',
      width: 190,
      resizable: true,
    },
    {
      title: t('不合格类型'),
      dataIndex: 'unqualifiedType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        // @ts-ignore
        return unqualifiedTypeMap[record?.inspectType?.value];
      },
    },
    {
      title: t('检测类型'),
      dataIndex: 'inspectType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.inspectType?.name ?? '-';
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'name',
      width: 120,
      resizable: true,
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      width: 120,
      resizable: true,
    },
    {
      title: t('阳性不合格'),
      dataIndex: 'isPositive',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.isPositive?.name ?? '-';
      },
    },
    {
      title: t('拒绝日期'),
      dataIndex: 'rejectDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('接收日期'),
      dataIndex: 'receiveTime',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('不合格来源'),
      dataIndex: 'unqualifiedOrigin',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unqualifiedOrigin?.name ?? '-';
      },
    },
    {
      title: t('核酸检测'),
      dataIndex: 'nucleicAcidFlag',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.nucleicAcidFlag?.name ?? '-';
      },
    },
    {
      title: t('同步来源'),
      dataIndex: 'syncOrigin',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.syncOrigin?.name ?? '-';
      },
    },
    // {
    //   title: t('企业检测状态'),
    //   dataIndex: 'detectionStatus',
    //   width: 140,
    //   sorter: true,
    //   resizable: true,
    //   customRender: ({ record }) => {
    //     return record?.detectionStatus?.name ?? '-';
    //   },
    // },
    {
      title: t('处理状态'),
      dataIndex: 'handleStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.handleStatus?.name ?? '-';
      },
    },
    {
      title: t('处理日期'),
      dataIndex: 'handleTime',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('查看影响血浆'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            // look(record);
            openViewEffectPlasmaModal(record);
          },
        },
        {
          label: t('编辑报告'),
          ifShow: hasPermission('170070001000002') && record?.draftFlag === 1,
          onClick: () => {
            // look(record);
            openCreateReport(record, 'edit');
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    schemas: [
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('处理状态'),
        field: 'handleStatus',
        component: 'Select',
        componentProps: {
          options: handleStatusDict,
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
      {
        label: t('拒绝日期'),
        field: 'rejectDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('不合格项目'),
        field: 'unqualifiedItems',
        component: 'Select',
        componentProps: {
          options: unqualifiedProjectDict.filter((item: any) => item.value > 1 && item.value < 8),
        },
      },
      {
        label: t('检测类型'),
        field: 'inspectType',
        component: 'Select',
        componentProps: {
          options: inspectTypeDict,
        },
      },
      // {
      //   label: t('企业检测状态'),
      //   field: 'detectionStatus',
      //   component: 'Select',
      //   componentProps: {
      //     options: detectionStatusDict,
      //   },
      // },
    ],
    fieldMapToTime: [['rejectDate', ['rejectDateBegin', 'rejectDateEnd'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
