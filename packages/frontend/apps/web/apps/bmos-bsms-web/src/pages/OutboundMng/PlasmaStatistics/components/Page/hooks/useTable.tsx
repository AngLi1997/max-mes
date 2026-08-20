import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = (enterView: any) => {
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
      expandMap[newKey] = useExpand(enterView);
    } else {
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'stationId',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return record.name;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('总数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('在库数量'),
      dataIndex: 'inStockNum',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('投料出库数量'),
      dataIndex: 'feedNum',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('科研出库数量'),
      dataIndex: 'scientificNum',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('调用出库数量'),
      dataIndex: 'callNum',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('销毁出库数量'),
      dataIndex: 'destructionNum',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
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
