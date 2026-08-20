import { getPlasmaCheckStorageList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const syncBatchNo = ref('');
  const containerNo = ref('');

  const pageObj = reactive({
    pageNum: 1,
    pageSize: 50,
  });

  const leftLoadData = async (params: any): Promise<any> => {
    try {
      const datas = {
        ...params,
        verifyStatus: 0,
      };
      if (!datas.syncBatchNo) {
        syncBatchNo.value = '';
        containerNo.value = '';
        return {
          data: [],
          total: 0,
        };
      }

      syncBatchNo.value = datas.syncBatchNo;
      containerNo.value = datas.containerNo;

      if (pageObj.pageNum === datas.pageNum && pageObj.pageSize === datas.pageSize) {
        fetchDubData('right');
      }
      const { data } = await getPlasmaCheckStorageList(datas);

      pageObj.pageNum = datas.pageNum;
      pageObj.pageSize = datas.pageSize;

      return {
        data: data.detailList,
      };
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  };

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('未核对血浆')],
    formProps: [
      {
        showAdvancedButton: false,
        labelWidth: 100,
        labelAlign: 'left',
        baseColProps: {
          span: 9,
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
          dataIndex: 'originOrg',
          width: 220,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆批号'),
          dataIndex: 'syncBatchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('入库仓库'),
          dataIndex: 'warehouse',
          width: 100,
          hideInSearch: true,
          hideInTable: !getWarehouseConfigByCode.value,
          customRender: ({ record }) => {
            return record?.warehouse?.name ?? '-';
          },
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 190,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('核对次数'),
          dataIndex: 'verifyNum',
          width: 120,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 170,
          hideInSearch: true,
          sorter: true,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  const rightLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    try {
      const datas = {
        ...params,

        syncBatchNo: syncBatchNo.value,
        containerNo: containerNo.value,
        verifyStatus: 1,
      };
      if (!syncBatchNo.value) {
        return new Promise(resolve => {
          setTimeout(() => {
            resolve({
              data: [],
              total: 0,
            });
          }, 0);
        });
      }

      const { data } = await getPlasmaCheckStorageList(datas);

      return {
        data: data.detailList,
      };
    } catch (error: any) {
      console.log(error);
      error.message && message.error(error.message);
    }
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('已核对血浆')],
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
    search: [false],
    columns: [
      [
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          width: 220,
          resizable: true,
        },
        {
          title: t('血浆批号'),
          dataIndex: 'syncBatchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          sorter: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          sorter: true,
          resizable: true,
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 170,
          sorter: true,
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
    syncBatchNo,
    containerNo,
  };
};
