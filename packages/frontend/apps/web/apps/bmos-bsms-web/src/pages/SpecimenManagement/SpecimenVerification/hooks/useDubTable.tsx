import { getSampleInStorageVerifyList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const syncBatchNo = ref('');
  const boxId = ref('');

  const pageObj = reactive({
    pageNum: 1,
    pageSize: 50,
  });

  const leftLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    try {
      const datas = {
        ...params,

        verifyStatus: 0,
      };
      if (!datas.syncBatchNo) {
        syncBatchNo.value = '';
        boxId.value = '';
        return new Promise(resolve => {
          setTimeout(() => {
            resolve({
              data: [],
              total: 0,
            });
          }, 200);
        });
      }

      syncBatchNo.value = datas.syncBatchNo;
      boxId.value = datas.boxId;

      if (pageObj.pageNum === datas.pageNum && pageObj.pageSize === datas.pageSize) {
        fetchDubData('right');
      }
      const { data } = await getSampleInStorageVerifyList(datas);

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
    titles: [t('未核对标本')],
    formProps: [
      {
        showAdvancedButton: false,
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
          title: t('标本批号'),
          dataIndex: 'syncBatchNo',
          hideInTable: true,
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.originOrgInfo?.originOrg;
          },
        },
        {
          title: t('入库仓库'),
          dataIndex: 'warehouse',
          hideInTable: !getWarehouseConfigByCode.value,
          hideInSearch: true,
          width: 120,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouse?.name;
          },
        },
        {
          title: t('标本批号'),
          dataIndex: 'sampleBatchNo',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 190,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('核对次数'),
          dataIndex: 'verifyNum',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('标本箱号'),
          dataIndex: 'boxId',
          width: 170,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 170,
          hideInSearch: true,
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
        boxId: boxId.value,
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

      const { data } = await getSampleInStorageVerifyList(datas);

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
    titles: [t('已核对标本')],
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
          dataIndex: 'originOrgCode',
          width: 170,
          resizable: true,
          customRender: ({ record }) => {
            return record?.originOrgInfo?.originOrg;
          },
        },
        {
          title: t('标本批号'),
          dataIndex: 'sampleBatchNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          resizable: true,
        },
        {
          title: t('标本箱号'),
          dataIndex: 'boxId',
          width: 170,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 170,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  const scanList = ref<any[]>([]);

  const clearScanList = () => {
    scanList.value = [];
  };

  const scanNoFn = (data: any) => {
    scanList.value = data;
  };

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
    scanNoFn,
    clearScanList,
    syncBatchNo,
    boxId,
  };
};
