import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();

export const useTable = (openCnt: any, openCheck: any, enterView: any) => {
  const { warehouseDict } = getDicts();
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
      expandMap[newKey] = useExpand(openCnt, openCheck);
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
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
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
      title: t('总献浆人数'),
      dataIndex: 'bloodUserNum',
      hideInSearch: true,
      width: 130,
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
      title: t('出库人'),
      dataIndex: 'outPlanBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
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
      title: t('库存状态'),
      dataIndex: 'warehousingStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehousingStatus?.name}</span>;
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 160,
      actions: ({ record }) => [
        {
          label: t('合并出库'),
          ifShow: hasPermission('170100008000002') && record?.warehousingStatus?.value === 1,
          onClick: () => {
            // look(record);
            openCheck(record, 'batchNo');
          },
        },
        {
          label: t('详情'),
          ifShow: hasPermission('170100008000001'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: getWarehouseConfigByCode.value ? 6 : 12,
    },
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
