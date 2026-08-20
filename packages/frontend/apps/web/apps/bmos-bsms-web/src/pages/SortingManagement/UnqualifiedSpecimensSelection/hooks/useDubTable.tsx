import {
  getSortingUnqualifiedSampleList,
  getSortingUnqualifiedSampleSortedList,
  getSortingUnqualifiedSampleType,
  sortingUnqualifiedSampleRevocation,
} from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  // 获取分拣类型列表
  const categoryOption = ref<any>([]);
  const getSortingCategoryOptions = async () => {
    const { data } = await getSortingUnqualifiedSampleType();

    categoryOption.value =
      data?.map((item: any) => {
        return {
          label: item.typeDescribe,
          value: item.id,
        };
      }) ?? [];
    const topFormRef = dubTableRef.value?.topRef?.getQueryFormRef(0);
    topFormRef.updateSchema({
      field: 'type',
      componentProps: {
        options: categoryOption.value,
      },
    });
  };

  const topLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,
    };

    const res = await getSortingUnqualifiedSampleList(datas);

    return res;
  };

  const topTableProps = reactive({
    requests: [topLoadData as DataRequestFn],
    formProps: [
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      },
    ] as Partial<FormProps>[],
    showHeader: [false],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    titles: [t('不合格标本分拣')],
    rowKeys: ['inWarehouseBatchNo'],
    search: [false],
    columns: [
      [
        {
          title: t('标本基础信息'),
          dataIndex: 'sampleInfo',
          hideInSearch: true,
          children: [
            {
              title: t('标本批号'),
              dataIndex: 'inWarehouseBatchNo',
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
              width: 150,
              sorter: true,
              resizable: true,
            },
            {
              title: t('分拣前箱号'),
              dataIndex: 'primeContainerNo',
              width: 170,
              resizable: true,
            },
          ],
        },
        {
          title: t('献浆者信息'),
          dataIndex: 'donorInfo',
          hideInSearch: true,
          children: [
            {
              title: t('编号'),
              dataIndex: 'plasmaDonorNo',
              width: 170,
              sorter: true,
              resizable: true,
            },
            {
              title: t('姓名'),
              dataIndex: 'plasmaDonorName',
              width: 100,
              resizable: true,
            },
            {
              title: t('性别'),
              dataIndex: 'plasmaDonorSex',
              width: 100,
              resizable: true,
              customRender: ({ record }) => {
                return record?.plasmaDonorSex?.name;
              },
            },
            {
              title: t('血型'),
              dataIndex: 'plasmaDonorBloodType',
              width: 100,
              resizable: true,
              customRender: ({ record }) => {
                return record?.plasmaDonorBloodType?.name;
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  const totalNum = ref(0);

  const bottomLoadData = async (params: any, onChangeParams: any) => {
    const datas = {
      ...params,
    };
    if (!datas.sortingBatchNo) {
      totalNum.value = 0;
      return {
        data: [],
      };
    }
    const res = await getSortingUnqualifiedSampleSortedList(datas);

    totalNum.value = res?.data?.total ?? 0;

    return res;
  };

  const bottomTableProps = reactive({
    requests: [bottomLoadData as DataRequestFn],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        showAdvancedButton: true,
        baseColProps: {
          span: 12,
        },
      },
    ] as Partial<FormProps>[],
    rowKeys: ['sampleOrgNo'],
    showHeader: [false],
    search: [false],
    columns: [
      [
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 190,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
        {
          title: t('分拣前箱号'),
          dataIndex: 'primeContainerNo',
          width: 180,
          resizable: true,
        },
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 160,
          sorter: true,
          resizable: true,
        },
        {
          title: t('献浆者姓名'),
          dataIndex: 'plasmaDonorName',
          width: 140,
          resizable: true,
        },
        {
          title: t('性别'),
          dataIndex: 'plasmaDonorSex',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorSex?.name;
          },
        },
        {
          title: t('不合格项目'),
          dataIndex: 'unqualifiedItem',
          width: 150,
          resizable: true,
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 120,
          actions: ({ record }) => [
            {
              label: t('撤销'),
              // ifShow: hasPermission('111020001000002'),
              onClick: () => {
                Modal.confirm({
                  title: t('是否进行撤销操作?'),
                  icon: h(ExclamationCircleOutlined),
                  async onOk() {
                    try {
                      await sortingUnqualifiedSampleRevocation(record.sampleOrgNo);

                      message.success(t('操作成功'));
                      await fetchDubData();
                    } catch (error: any) {
                      error.message && message.error(error.message);
                      return Promise.reject();
                    }
                  },
                  onCancel() {},
                });
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  // 刷新列表
  const fetchDubData = async (type?: 'top' | 'bottom') => {
    if (type != 'bottom') {
      await dubTableRef.value?.topRef.fetchData();
    }
    if (type != 'top') {
      await dubTableRef.value?.bottomRef.fetchData();
    }
  };

  // 获取总条数
  const getBottomTotal = () => {
    return dubTableRef.value?.bottomRef?.getTableRef()?.paginationRef?.total;
  };

  return {
    dubTableRef,
    topTableProps,
    bottomTableProps,
    getSortingCategoryOptions,
    fetchDubData,
    getBottomTotal,
    totalNum,
  };
};
