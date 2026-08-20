import StateTag from '@/components/StateTag/index.vue';
import { loopTree } from '@/pages/Manage/StorageManage/utils';
import {
  reqCargoCategoryQueryTreeWithCargo,
  reqInventoryListByCargoIdAndBatchNo,
  reqStorageCargoPage,
} from '@/services';
import type { BMFormType, FormProps, RenderCallbackParams, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import dayjs from 'dayjs';

export type UseTableParams = {};

export const useTable = () => {
  const columnsFirst: TableColumn[] = [
    {
      title: t('操作时间'),
      dataIndex: 'operateTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作类型'),
      dataIndex: ['operateType', 'label'],
      resizable: true,
      width: 140,
    },
    {
      title: t('具体操作'),
      dataIndex: 'operateInfo',
      resizable: true,
      hideInSearch: true,
      width: 140,
    },
    {
      title: t('操作人员'),
      dataIndex: 'operatorName',
      hideInSearch: true,
      resizable: true,
      width: 140,
    },
    {
      title: t('货品名称'),
      dataIndex: 'cargoName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //货品编码
      title: t('货品编码'),
      dataIndex: 'mergeCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //货品批号
      title: t('货品批号'),
      dataIndex: 'inventoryBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //货品件号
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //预定量
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //可用量
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //单位
      title: t('单位'),
      dataIndex: 'unit',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //货品状态
      title: t('货品状态'),
      dataIndex: 'available',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => (
        <StateTag type={record.available ? 'success' : 'danger'}>{record.available ? t('可用') : t('不可用')}</StateTag>
      ),
    },
    {
      //有效日期
      title: t('有效日期'),
      dataIndex: 'effectiveDate',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //产品名称
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //产品编码
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //生产批号
      title: t('生产批号'),
      dataIndex: 'productBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('领料单'),
      dataIndex: 'pullOrderNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('货位名称'),
      dataIndex: 'position',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('货位编码'),
      dataIndex: 'positionCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('所属位置'),
      dataIndex: 'positionPath',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //供应商
      title: t('供应商'),
      dataIndex: 'supplier',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //生产商
      title: t('生产商'),
      dataIndex: 'producer',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //原厂批号
      title: t('原厂批号'),
      dataIndex: 'factoryBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //备注
      title: t('备注'),
      dataIndex: 'remark',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //请验单号
      title: t('请验单号'),
      dataIndex: 'validateOrderNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //报告单号
      title: t('报告单号'),
      dataIndex: 'reportOrderNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //
      title: t('放行单编号'),
      dataIndex: 'licenseOrderNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('检验信息'),
      dataIndex: 'checkInfo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
  ];

  const getCargoCategoryQueryTree = async () => {
    try {
      const { data } = await reqCargoCategoryQueryTreeWithCargo();
      return loopTree(data) || [];
    } catch (error) {
      return [];
    }
  };

  const getInventoryListByCargoIdAndBatchNo = debounce(async (value: any, formInstance: BMFormType) => {
    try {
      const { data } = await reqInventoryListByCargoIdAndBatchNo(value);
      formInstance.updateSchema({
        field: 'inventoryBatchNo',
        componentProps: {
          options: data,
        },
      });
    } catch (error) {}
  }, 500);

  const getStorageCargoPage = debounce(async (value: any, formInstance: BMFormType, inventoryNo?: string) => {
    try {
      const { data } = await reqStorageCargoPage({
        inventoryBatchId: value,
        pageNum: 1,
        pageSize: 20,
        ...(inventoryNo && { inventoryNo }),
      });
      formInstance.updateSchema({
        field: 'inventoryNo',
        componentProps: {
          options: data.list,
        },
      });
    } catch (error) {}
  }, 500);

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: true,
    schemas: [
      {
        field: 'cargoId',
        component: 'TreeSelect',
        label: t('货品信息'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            fieldNames: {
              children: 'children',
              label: 'name',
              value: 'id',
            },
            request: async () => {
              return await getCargoCategoryQueryTree();
            },
            onChange: async (value: any) => {
              try {
                if (value) {
                  getInventoryListByCargoIdAndBatchNo(value, formInstance);
                } else {
                  formInstance.updateSchema({
                    field: 'inventoryBatchNo',
                    componentProps: {
                      options: [],
                    },
                  });
                }
                formInstance.setFieldsValue({
                  inventoryBatchNo: undefined,
                  inventoryNo: undefined,
                });
                formInstance.updateSchema({
                  field: 'inventoryNo',
                  componentProps: {
                    options: [],
                  },
                });
              } catch (error: any) {
                console.log(error);
              }
            },
          };
        },
      },
      {
        //货品批号
        field: 'inventoryBatchNo',
        component: 'Select',
        label: t('货品批号'),
        componentProps: ({ formInstance, formModel }: RenderCallbackParams) => {
          return {
            options: [],
            showSearch: true,
            fieldNames: {
              label: 'inventoryBatchNo',
              value: 'inventoryBatchNo',
            },
            filterOption: (input: string, option: any) => {
              return option.inventoryBatchNo.toLowerCase().indexOf(input.toLowerCase()) >= 0;
            },
            onChange: async (value: string) => {
              if (!value) {
                formInstance.updateSchema({
                  field: 'inventoryNo',
                  componentProps: {
                    options: [],
                  },
                });
                formModel.inventoryBatchId = undefined;
              }
            },
            onSelect: (_value: any, option: any) => {
              getStorageCargoPage(option.id, formInstance);
              formInstance.setFieldsValue({
                inventoryNo: undefined,
              });
              formModel.inventoryBatchId = option.id;
            },
          };
        },
      },
      {
        //货品件号
        field: 'inventoryNo',
        component: 'Select',
        label: t('货品件号'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [],
            showSearch: true,
            fieldNames: {
              label: 'inventoryNo',
              value: 'inventoryNo',
            },
            filterOption: false,
            onSearch: (value: string) => {
              getStorageCargoPage(formModel.inventoryBatchId, value);
            },
            onChange: (value: string) => {
              getStorageCargoPage(formModel.inventoryBatchId, value);
            },
          };
        },
      },
      {
        //操作类型
        field: 'operateType',
        component: 'Select',
        label: t('操作类型'),
        componentProps: () => {
          return {
            options: [
              { label: t('入库'), value: 1 },
              { label: t('出库'), value: 2 },
              { label: t('新增'), value: 3 },
              { label: t('盘点'), value: 4 },
            ],
          };
        },
      },
      {
        //操作时间
        field: 'operationTime',
        component: 'RangePicker',
        label: t('操作时间'),
        defaultValue: [dayjs().subtract(29, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')],
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    ],
    fieldMapToTime: [['operationTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });

  return {
    columnsFirst,
    formFirstProps,
  };
};
