import { getSampleOutWarehouseDetail, getSampleOutWarehouseList, sampleOutVerifyRecheck } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, Key, RenderCallbackParams, TableColumn } from '@bmos/components';
import { FormItemRest, Input, Modal, RadioGroup, message } from 'ant-design-vue';
import { useTable } from './useTable';

const { hasPermission } = usePermissionStore();

export const useDubTable = () => {
  const dubTableRef = ref<any>(null);

  // 身份核查状态
  const checkState = ref<boolean>(false);

  // 左边列表选中的数据
  const leftSelectData = ref<any>({});

  const leftLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    if (!checkState.value) {
      return {
        data: [],
      };
    }
    const datas = {
      ...params,
    };

    const res = await getSampleOutWarehouseList(datas);

    const keys = res?.data?.list?.map((item: any) => item.outPlanBatchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandedTableMap[key].fetchData();
      }
    });

    return res;
  };

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandedTableMap = reactive<any>({});

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('出库标本')],
    formProps: [
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
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
    expandFixed: 'left',
    expandedRowsChanges: [
      async (expandedKeys: Key[]) => {
        expandedRowKeys.value = expandedKeys;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandedTableMap[newKey]) {
          expandedTableMap[newKey] = useTable();
        } else {
          expandedTableMap[newKey].fetchData();
        }
      },
    ],
    rowKeys: ['outPlanBatchNo'],
    tableFields: [
      {
        default: {
          pageFlag: 2,
        },
      },
    ],
    columns: [
      [
        {
          title: t('出库批号'),
          dataIndex: 'outPlanBatchNo',
          width: 170,
          fixed: 'left',
          resizable: true,
          customRender: ({ record }) => {
            return (
              <a
                onClick={() => {
                  leftSelectData.value = { ...record };
                  dubTableRef.value?.rightRef.fetchData();
                }}>
                {record.outPlanBatchNo}
              </a>
            );
          },
        },
        {
          title: t('出库仓库'),
          dataIndex: 'warehouse',
          width: 120,
          hideInSearch: true,
          hideInTable: !getWarehouseConfigByCode.value,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouse?.name;
          },
        },
        {
          title: t('出库类型'),
          dataIndex: 'outboundType',
          hideInSearch: true,
          width: 130,
          customRender: ({ record }) => {
            return record?.outboundType?.name;
          },
        },
        {
          title: t('数量'),
          dataIndex: 'number',
          width: 100,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('出库人'),
          dataIndex: 'outPlanBy',
          width: 100,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('出库日期'),
          dataIndex: 'outPlanDate',
          width: 160,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('库存状态'),
          dataIndex: 'warehouseStatus',
          width: 120,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.warehouseStatus?.name;
          },
        },
        {
          title: t('操作'),
          key: 'ACTION',
          fixed: 'right',
          width: 100,
          actions: ({ record }) => [
            {
              label: t('重新核对'),
              ifShow: hasPermission('170020012000002'),
              onClick: () => {
                Modal.confirm({
                  title: t('提示'),
                  icon: h(ExclamationCircleOutlined),
                  content: t('是否重新核对，核对数量将清零'),
                  async onOk() {
                    try {
                      await sampleOutVerifyRecheck(record.outPlanBatchNo);
                      message.success(t('操作成功'));
                      fetchDubData();
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

  // 待核对数量
  const numObj = ref({
    totalNum: 0,
    waitVerifyNum: 0,
  });

  const rightLoadData = async (params: any, onChangeParams: any): Promise<any> => {
    if (!leftSelectData.value.outPlanBatchNo) {
      return {
        data: [],
      };
    } else {
      const datas = {
        ...params,

        outPlanBatchNo: leftSelectData.value.outPlanBatchNo,
      };
      const { data } = await getSampleOutWarehouseDetail(datas);

      numObj.value = {
        totalNum: data?.totalNum ?? 0,
        waitVerifyNum: data?.waitVerifyNum ?? 0,
      };

      return {
        data: data?.voList,
      };
    }
  };

  const radioType = ref('boxId');

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('核对标本')],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        initialValues: {
          // type: 'boxId',
        },
        showAdvancedButton: false,
        // showResetButton: false,
        // showSubmitButton: false,
        actionColOptions: {
          span: 8,
        },
        baseColProps: {
          span: 16,
        },
        labelWidth: 0,
        schemas: [
          {
            // title: 'type',
            field: 'type',
            component: ({ formModel }: RenderCallbackParams) => {
              return (
                <FormItemRest>
                  <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <div style={{ width: '220px' }}>
                      <RadioGroup
                        style={{ width: '220px' }}
                        v-model:value={radioType.value}
                        options={[
                          { label: t('箱号'), value: 'boxId' },
                          { label: t('标本编号'), value: 'sampleNo' },
                        ]}
                        onChange={() => {
                          if (radioType.value == 'sampleNo') {
                            formModel.boxId = '';
                          } else {
                            formModel.sampleNo = '';
                          }
                        }}
                      />
                    </div>

                    <Input v-model:value={formModel[radioType.value]} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        ],
      },
    ] as Partial<FormProps>[],
    // search: [false],
    tableFields: [
      {
        default: {
          verifyStatus: 0,
          warehouseStatus: 1,
        },
      },
    ],
    columns: [
      [
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('分拣批次'),
          dataIndex: 'sortingPlanBatchNo',
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
          title: t('标本类型'),
          dataIndex: 'sampleType',
          width: 140,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleType?.name;
          },
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
        {
          title: t('标本状态'),
          dataIndex: 'sampleStatus',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleStatus?.name;
          },
        },
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.no}</span>;
          },
        },
        {
          title: t('姓名'),
          dataIndex: 'name',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.name;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.sex?.name;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.bloodType?.name;
          },
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
      ] as TableColumn[],
    ],
  });

  const scanList = ref<any[]>([]);

  const cangeCheckState = (data: any) => {
    checkState.value = data;
  };

  const clearScanList = () => {
    scanList.value = [];
  };

  const scanNoFn = (data: any) => {
    scanList.value = data;
  };

  const fetchDubData = (type?: 'left' | 'right') => {
    if (type != 'right') {
      leftSelectData.value = {};
      dubTableRef.value?.leftRef.fetchData();
    }
    if (type != 'left') {
      numObj.value = {
        totalNum: 0,
        waitVerifyNum: 0,
      };
      dubTableRef.value?.rightRef.fetchData();
    }
  };
  return {
    checkState,
    cangeCheckState,
    dubTableRef,
    leftSelectData,
    leftTableProps,
    rightTableProps,
    fetchDubData,
    scanNoFn,
    clearScanList,
    expandedRowKeys,
    expandedTableMap,
    numObj,
  };
};
