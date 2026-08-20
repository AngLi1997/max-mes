import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (openCnt: any, enterView: any) => {
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
      title: t('检品批号'),
      dataIndex: 'inspectionBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('检品数量'),
      dataIndex: 'inspectionNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unqualifiedNumber',
      width: 140,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unqualifiedNumber ? <a onClick={() => openCnt(record)}>{record?.unqualifiedNumber}</a> : 0;
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrg}</span>;
      },
    },
    {
      title: t('收检日期'),
      dataIndex: 'receiveDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('报告人'),
      dataIndex: 'reportBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('报告日期'),
      dataIndex: 'reportDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('签发人'),
      dataIndex: 'issueBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('签发日期'),
      dataIndex: 'issueDate',
      width: 140,
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
          ifShow: hasPermission('170030003000003'),
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
    schemas: [
      {
        label: t('收检日期'),
        field: 'receiveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
      {
        label: t('签发日期'),
        field: 'issueDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('检品批号'),
        field: 'inspectionBatchNo',
        component: 'Input',
      },
    ],
    fieldMapToTime: [
      ['receiveDate', ['receiveDateUp', 'receiveDateDown'], 'YYYY-MM-DD'],
      ['issueDate', ['issueDateUp', 'issueDateDown'], 'YYYY-MM-DD'],
    ],
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
