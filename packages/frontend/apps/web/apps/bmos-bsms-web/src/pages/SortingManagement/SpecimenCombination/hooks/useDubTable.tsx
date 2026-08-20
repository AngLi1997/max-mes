import { getSampleAmalgamationList } from '@/services';
import type { DataRequestFn, FormProps, TableColumn } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  const scanList = ref<any>([]);

  // 主箱子类型
  const containerType = ref<any>();

  // 扫描血浆
  const scanPlasma = (data: any) => {
    if (scanList.value.find((item: any) => item === data)) {
      return;
    }
    scanList.value.push(data);
  };

  // 撤销合并
  const cancelMerge = (data: any) => {
    scanList.value = scanList.value.filter((item: any) => item !== data.sampleOrgNo);
  };

  // 重置
  const resetScan = () => {
    scanList.value = [];
  };

  const schemasCmp = reactive([
    {
      label: t('标本箱号'),
      field: 'containerNo',
      component: 'Input',
    },
  ]);

  const columnsCmp = reactive([
    {
      title: t('分拣批号'),
      dataIndex: 'sortingPlanBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('当前箱号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('原始入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
  ]);

  const leftTableData = ref<any>([]);

  const oldContainerNo = ref<any>('');

  const leftLoadData = async (params: any) => {
    if (oldContainerNo.value !== params.containerNo) {
      oldContainerNo.value = params.containerNo;
      scanList.value = [];
      fetchDubData('right');
    }
    const datas = {
      ...params,
      addSampleOrgNoList: scanList.value,
    };
    if (!datas.containerNo) {
      leftTableData.value = [];
      return {
        data: {
          list: [],
        },
      };
    }
    const { data } = await getSampleAmalgamationList(datas);
    containerType.value = data?.[0]?.containerNoType?.value ?? null;

    // 对data进行排序，在scanList中的数据在后面
    data?.sort((a: any, b: any) => {
      if (scanList.value.includes(a.sampleOrgNo)) {
        return 1;
      }
      if (scanList.value.includes(b.sampleOrgNo)) {
        return -1;
      }
      return 0;
    });
    leftTableData.value = data ?? [];

    return {
      data: {
        list: leftTableData.value,
      },
    };
  };

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    // showHeader: [false],
    showToolBars: [true],
    // titles: [t('未核对标本')],
    formProps: [
      {
        showAdvancedButton: false,
        labelWidth: 100,
        actionColOptions: {
          span: 12,
        },
        baseColProps: {
          span: 12,
        },
        schemas: schemasCmp,
      },
    ] as Partial<FormProps>[],
    paginations: [false],
    columns: [
      [
        ...columnsCmp,
        {
          title: t('操作'),
          align: 'left',
          key: 'ACTION',
          fixed: 'right',
          width: 120,
          actions: ({ record }) => [
            {
              label: t('撤销合并'),
              // ifShow: hasPermission('111020001000002'),
              ifShow: scanList.value?.includes(record.sampleOrgNo),
              onClick: () => {
                // look(record);
                cancelMerge(record);
                fetchDubData();
                message.success(t('操作成功'));
              },
            },
          ],
        },
      ] as TableColumn[],
    ],
  });

  const rightTableData = ref<any>([]);

  const rightLoadData = async (params: any) => {
    const datas = {
      ...params,
      decSampleOrgNoList: scanList.value,
    };
    if (!datas.containerNo) {
      rightTableData.value = [];
      return {
        data: {
          list: [],
        },
      };
    }

    const { data } = await getSampleAmalgamationList(datas);

    rightTableData.value = data ?? [];

    return {
      data: {
        list: rightTableData.value,
      },
    };
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    // showHeader: [false],
    showToolBars: [true],
    // titles: [t('已核对标本')],
    paginations: [false],
    formProps: [
      {
        showAdvancedButton: false,
        labelWidth: 100,
        actionColOptions: {
          span: 12,
        },
        baseColProps: {
          span: 12,
        },
        schemas: schemasCmp,
      },
    ] as Partial<FormProps>[],
    columns: [columnsCmp as TableColumn[]],
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

  // 判断箱子是否填满
  const isFull = () => {
    return leftTableData.value.length >= 40;
  };

  return {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    leftTableData,
    rightTableData,
    fetchDubData,
    scanPlasma,
    cancelMerge,
    resetScan,
    scanList,
    isFull,
  };
};
