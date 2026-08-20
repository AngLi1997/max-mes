import {
  getSortingUnqualifiedPlasmaList,
  getSortingUnqualifiedPlasmaSortedList,
  getSortingUnqualifiedPlasmaType,
  sortingUnqualifiedPlasmaRevocation,
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
    const { data } = await getSortingUnqualifiedPlasmaType();

    categoryOption.value =
      data?.map((item: any) => {
        return {
          label: `${item.typeDescribe}`,
          value: item.planBatchNo,
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

    const res = await getSortingUnqualifiedPlasmaList(datas);

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
    rowKeys: ['batchNo'],
    search: [false],
    titles: [t('不合格血浆分拣')],
    columns: [
      [
        {
          title: t('血浆基础信息'),
          dataIndex: 'plasmaInfo',
          hideInSearch: true,
          children: [
            {
              title: t('血浆批号'),
              dataIndex: 'inWarehouseBatchNo',
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
              width: 120,
              sorter: true,
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

  const bottomLoadData = async (params: any, onChangeParams: any) => {
    const datas = {
      ...params,
    };
    if (!datas.sortingBatchNo) {
      return {
        data: [],
      };
    }
    return await getSortingUnqualifiedPlasmaSortedList(datas);
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
    rowKeys: ['plasmaOrgNo'],
    showHeader: [false],
    search: [false],
    columns: [
      [
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 120,
          sorter: true,
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
          width: 180,
          resizable: true,
        },
        {
          title: t('编号'),
          dataIndex: 'plasmaDonorNo',
          width: 160,
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
          width: 150,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorSex?.name;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'plasmaDonorBloodType',
          width: 150,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorBloodType?.name;
          },
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
                      await sortingUnqualifiedPlasmaRevocation(record.plasmaOrgNo);

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

  // // 获取当前选中的树节点
  // const getTreeNode = () => {
  //   dubTableRef.value?.bottomRef;
  // };

  // 修改树节点
  const setTreeNode = (node: any) => {
    dubTableRef.value?.bottomRef?.changeCurrentNode(node);
  };

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
    setTreeNode,
  };
};
