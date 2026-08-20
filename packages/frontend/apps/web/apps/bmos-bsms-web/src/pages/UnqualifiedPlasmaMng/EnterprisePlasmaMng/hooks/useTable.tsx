import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openViewEffectPlasmaModal: any, openCreateReport: any) => {
  const { unqualifiedProjectDict, warehouseDict } = getDicts();
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
      customRender: ({ record }) => {
        return record?.originOrgInfo?.originOrg ?? '-';
      },
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
        return unqualifiedTypeMap[record?.inspectType?.value] ?? '-';
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      sorter: true,
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.no ?? '-';
      },
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'name',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.name ?? '-';
      },
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      width: 140,
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
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('不合格来源'),
      dataIndex: 'unqualifiedOrigin',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unqualifiedOrigin?.name ?? '-';
      },
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouse',
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.warehouse?.name ?? '-';
      },
    },
    {
      title: t('反馈状态'),
      dataIndex: 'feedbackStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.feedbackStatus?.name ?? '-';
      },
    },
    {
      title: t('反馈日期'),
      dataIndex: 'feedbackTime',
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
          ifShow: hasPermission('170070002000002') && record?.draftFlag === 1,
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
      // {
      //   label: t('反馈状态'),
      //   field: 'feedbackStatus',
      //   component: 'Select',
      //   componentProps: {
      //     options: feedbackStatusDict,
      //   },
      // },
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
          options: unqualifiedProjectDict,
        },
      },
      {
        label: t('所在仓库'),
        field: 'warehouseId',
        vIf: getWarehouseConfigByCode.value,
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
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
