import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = (enterDetail: any, openCnt: any) => {
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
      expandMap[newKey] = useExpand(enterDetail, openCnt);
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      width: 170,
      resizable: true,
      fixed: 'left',
    },
    {
      title: t('总数量'),
      dataIndex: 'checkNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('已拣数量'),
      dataIndex: 'sortedNumber',
      width: 120,
      // sorter: true, // 后端排不了序所以去掉了
      resizable: true,
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouse?.name}</span>;
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 260,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
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
        label: t('计划批号'),
        field: 'planBatchNo',
        component: 'Input',
      },
      {
        label: t('核查批号'),
        field: 'checkNo',
        component: 'Input',
      },
      {
        label: t('入库批号'),
        field: 'inWarehouseBatchNo',
        component: 'Input',
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
