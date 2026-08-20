import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const { auditResultDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand();
    } else {
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('出库批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('出库仓库'),
      dataIndex: 'warehouseId',
      width: 100,
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: !getWarehouseConfigByCode.value,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouse?.name}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('出库类型'),
      dataIndex: 'type',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.type?.name}</span>;
      },
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('总重量'),
      dataIndex: 'weight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
    },
    {
      title: t('出库日期'),
      dataIndex: 'outPlanDate',
      width: 150,
      sorter: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('质量状态'),
      dataIndex: 'qualityStatus',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.qualityStatus?.name}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'approveBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'approveTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name}</span>;
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
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170100002000003'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: getWarehouseConfigByCode.value,
    fieldMapToTime: [['outPlanDate', ['startDate', 'endDate'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    expandMap,
    expandedRowKeys,
    columnsFirst,
    formFirstProps,
    expandChange,
  };
};
