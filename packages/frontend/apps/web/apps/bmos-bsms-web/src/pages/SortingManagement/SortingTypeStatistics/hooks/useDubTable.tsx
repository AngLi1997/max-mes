import { getSortingTypeStatisticsDetailList, getSortingTypeStatisticsList } from '@/services';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';

const { getPlasmaStations } = usePlasmaStation();

export const useDubTable = () => {
  const { warehouseDict } = getDicts();
  const dubTableRef = ref<any>(null);

  const checkNo = ref<any>('');

  const leftTableProps = reactive({
    requests: [getSortingTypeStatisticsList as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('核查批次')],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 12,
        },
      },
    ] as Partial<FormProps>[],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    columns: [
      [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 220,
          resizable: true,
          customRender: ({ record }) => {
            return record?.originOrgInfo?.originOrg;
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              request: getPlasmaStations,
            },
          },
        },
        {
          title: t('核查批号'),
          dataIndex: 'checkNo',
          width: 170,
          resizable: true,
          customRender: ({ record }) => {
            return (
              <a
                onClick={async () => {
                  checkNo.value = record.checkNo;
                  await fetchDubData('right');
                }}>
                {record.checkNo}
              </a>
            );
          },
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInSearch: !getWarehouseConfigByCode.value,
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouse?.name;
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: t('合格数量'),
          dataIndex: 'qualifiedNumber',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('合格重量'),
          dataIndex: 'qualifiedWeight',
          width: 110,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('已加计划总数'),
          dataIndex: 'joinSortNumber',
          width: 110,
          hideInSearch: true,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  const rightLoadData = async (params: any) => {
    if (!checkNo.value) {
      return {
        data: [],
      };
    }
    const datas = {
      ...params,

      checkNo: checkNo.value,
    };
    return await getSortingTypeStatisticsDetailList(datas);
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('分拣类型详情')],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 8,
        },
      },
    ] as Partial<FormProps>[],
    scrolls: [{ x: 800, y: 220 }],
    search: [false],
    columns: [
      [
        {
          title: t('效价下限'),
          dataIndex: 'titerDown',
          width: 100,
          resizable: true,
        },
        {
          title: t('效价上限'),
          dataIndex: 'titerUp',
          width: 100,
          resizable: true,
        },
        {
          title: t('类型'),
          dataIndex: 'sortingTypeName',
          width: 90,
          resizable: true,
        },
        {
          title: t('描述'),
          dataIndex: 'typeDescribe',
          width: 170,
          resizable: true,
        },
        {
          title: t('分批标识'),
          dataIndex: 'batchLog',
          width: 90,
          resizable: true,
        },
        {
          title: t('分箱标识'),
          dataIndex: 'subBoxLog',
          width: 90,
          resizable: true,
        },
        {
          title: t('数量'),
          dataIndex: 'number',
          width: 80,
          resizable: true,
        },
        {
          title: t('已加计划数量'),
          dataIndex: 'sortedNumber',
          width: 140,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  // 刷新列表
  const fetchDubData = (type?: 'left' | 'right') => {
    if (type != 'right') {
      dubTableRef.value?.leftRef.fetchData();
    }
    if (type != 'left') {
      dubTableRef.value?.rightRef.fetchData();
    }
  };

  return {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    fetchDubData,
  };
};
