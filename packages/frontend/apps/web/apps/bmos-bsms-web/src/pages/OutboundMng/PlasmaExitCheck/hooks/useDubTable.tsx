import { getOutboundCheckBatchDetail, getOutboundCheckList, outboundCheckBack } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { DataRequestFn, FormProps, Key, RenderCallbackParams, TableColumn } from '@bmos/components';
import { FormItemRest, Input, Modal, RadioGroup, message } from 'ant-design-vue';
import { useTable } from './useTable';

const { hasPermission } = usePermissionStore();

export const useDubTable = () => {
  const { warehouseDict } = getDicts();
  const dubTableRef = ref<any>(null);

  // 身份核查状态
  const checkState = ref<boolean>(false);

  const changeCheckState = (value: boolean) => {
    checkState.value = value;
  };

  // 左边列表选中的数据
  const leftSelectData = ref<any>({});

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandedTableMap = reactive<any>({});

  const leftLoadData = async (params: any): Promise<any> => {
    if (!checkState.value) {
      return {
        data: [],
      };
    }
    const datas = {
      ...params,
    };

    const res = await getOutboundCheckList(datas);

    const keys = res?.data?.list?.map((item: any) => item.batchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandedTableMap[key].fetchData();
      }
    });

    return res;
  };

  const leftTableProps = reactive({
    requests: [leftLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('出库血浆')],
    rowKeys: ['batchNo'],
    formProps: [
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: getWarehouseConfigByCode.value ? 8 : 16,
        },
        baseColProps: {
          span: 8,
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
    columns: [
      [
        {
          title: t('出库批号'),
          dataIndex: 'batchNo',
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
                {record.batchNo}
              </a>
            );
          },
        },
        {
          title: t('出库仓库'),
          dataIndex: 'warehouseId',
          hideInSearch: !getWarehouseConfigByCode.value,
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.warehouse?.name}</span>;
          },
          formItemProps: {
            component: 'Select',
            componentProps: {
              options: warehouseDict,
            },
          },
        },
        {
          title: t('出库类型'),
          dataIndex: 'type',
          hideInSearch: true,
          customRender: ({ record }) => {
            return <span>{record?.type?.name}</span>;
          },
        },
        {
          title: t('数量'),
          dataIndex: 'num',
          width: 100,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('总重量'),
          dataIndex: 'weight',
          width: 120,
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
          width: 140,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('库存状态'),
          dataIndex: 'warehousingStatus',
          width: 170,
          hideInSearch: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.warehousingStatus?.name}</span>;
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
              ifShow: hasPermission('170100010000001') && [1, 2].includes(record?.warehousingStatus?.value),
              onClick: () => {
                Modal.confirm({
                  title: t('提示'),
                  icon: h(ExclamationCircleOutlined),
                  content: t('是否重新核对，核对数量将清零'),
                  async onOk() {
                    try {
                      await outboundCheckBack({ batchNo: record.batchNo });
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
  const waitCnt = ref(0);

  const rightLoadData = async (params: any): Promise<any> => {
    if (!leftSelectData.value.batchNo) {
      return {
        data: [],
      };
    } else {
      const datas = {
        ...params,

        batchNo: leftSelectData.value.batchNo,
      };
      const { data } = await getOutboundCheckBatchDetail(datas);

      waitCnt.value = data?.noCheckNum || 0;

      return {
        data: data.checkPage,
      };
    }
  };

  const rightTableProps = reactive({
    requests: [rightLoadData as DataRequestFn],
    showHeader: [false],
    showToolBars: [true],
    titles: [t('核对血浆')],
    paginations: [
      {
        ...paginationBig,
      },
    ],
    formProps: [
      {
        initialValues: {
          type: 'containerNo',
        },
        showAdvancedButton: false,
        // showResetButton: false,
        // showSubmitButton: false,
        actionColOptions: {
          span: 12,
        },
        baseColProps: {
          span: 12,
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
                        v-model:value={formModel.type}
                        options={[
                          { label: t('箱/托盘号'), value: 'containerNo' },
                          { label: t('血浆编号'), value: 'no' },
                        ]}
                        onChange={() => {
                          if (formModel.type == 'no') {
                            formModel.containerNo = '';
                          } else {
                            formModel.no = '';
                          }
                        }}
                      />
                    </div>

                    <Input v-model:value={formModel[formModel.type]} allowClear placeholder={t('请输入')} />
                  </div>
                </FormItemRest>
              );
            },
          },
        ],
      },
    ] as Partial<FormProps>[],
    scrolls: [{ x: 800, y: 220 }],
    // search: [false],
    columns: [
      [
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('类型'),
          dataIndex: 'typeDescribe',
          width: 120,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 150,
          sorter: true,
          hideInSearch: true,
          resizable: true,
        },
        {
          title: t('血浆箱号'),
          dataIndex: 'containerNo',
          width: 150,
          resizable: true,
        },
        {
          title: t('免疫类别'),
          dataIndex: 'immunityType',
          width: 120,
          resizable: true,
        },
        {
          title: t('效价'),
          dataIndex: 'titer',
          width: 80,
          sorter: true,
          hideInSearch: true,
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

  const fetchDubData = (type?: 'left' | 'right') => {
    if (type != 'right') {
      leftSelectData.value = {};
      dubTableRef.value?.leftRef.fetchData();
    }
    if (type != 'left') {
      waitCnt.value = 0;
      dubTableRef.value?.rightRef.fetchData();
    }
  };

  return {
    checkState,
    changeCheckState,
    dubTableRef,
    leftSelectData,
    leftTableProps,
    rightTableProps,
    fetchDubData,
    scanNoFn,
    clearScanList,
    expandedRowKeys,
    expandedTableMap,
    waitCnt,
  };
};
