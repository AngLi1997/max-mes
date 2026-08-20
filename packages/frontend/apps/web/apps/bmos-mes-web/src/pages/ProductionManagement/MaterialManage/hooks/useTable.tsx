import { MaterialTypeMap } from '@/pages/ProductionMaterials/PageComponentNew/const';
import { getMaterialLogTreeApi, postTagInstancePrintBatch, reqGetInspectQueryMaterialBatchNo } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, RenderCallbackParams, TableColumn } from '@bmos/components';
import { BMStateTag } from '@bmos/components';
import { t } from '@bmos/i18n';
import { loopSelectableTree } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { OperationType } from '../types';

export type UseTableParams = {};

export const useTable = () => {
  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const operationType = ref<keyof typeof OperationType>('ADD');
  // 批次
  const materialBatchModalOpen = ref<boolean>(false);
  // 打印弹窗开关
  const printOpen = ref<any>(false);
  //多选时的表格ids
  const selectedRowKeys1 = ref<any>([]);
  //存多选的数据
  const operationSelectedRows = ref<any>([]);
  const InspectionDetailsModalRef = ref<any>();
  const inspectionRowData = ref<any>();
  // 多选
  const rowSelections = reactive([
    {},
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[1]?.selectedRowKeys) {
          rowSelections[1].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
  ]);
  const qualityStatus = {
    QUARANTINE: { label: t('待验'), type: 'warning' },
    QUALIFIED: { label: t('合格'), type: 'success' },
    UNQUALIFIED: { label: t('不合格'), type: 'danger' },
    SAMPLED: { label: t('已取样'), type: 'primary' },
    RESTRICTED_RELEASE: { label: t('限制性放行'), type: 'limit' },
  } as any;
  const addMaterialBatch = () => {
    firstRowData.value = {};
    materialBatchModalOpen.value = true;
    operationType.value = OperationType.ADD;
  };
  // 打印标签
  const print = () => {
    if (operationSelectedRows.value.length === 0) return message.error(t('请先勾选物料件'));
    printOpen.value = true;
  };
  // 确认打印
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      const batchParams = operationSelectedRows.value.map((item: any) => {
        return {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            no: item?.storageMaterialNo,
          },
        };
      });
      await postTagInstancePrintBatch(batchParams);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      fixed: 'left',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('物料类型'),
      dataIndex: 'categoryType',
      width: 100,
      customRender: ({ record }) => (record.categoryType === MaterialTypeMap.RawMaterial ? t('原辅包') : t('中间品')),
      formItemProps: {
        component: 'Select',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('原辅包'), value: MaterialTypeMap.RawMaterial },
              { label: t('中间品'), value: MaterialTypeMap.MiddleProduct },
            ],
            onChange: () => {
              formModel.materialId = undefined;
            },
          };
        },
      },
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialId',
      width: 100,
      hideInTable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            treeData: [
              {
                id: 1,
                name: '1',
                categoryFlag: true,
                children: [
                  {
                    id: 2,
                    name: '2',
                    categoryFlag: null,
                  },
                ],
              },
            ],
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            virtual: false,
            height: 200,
            request: {
              watchFields: ['categoryType'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (formModel.categoryType === undefined) return [];
                  const { data } = await getMaterialLogTreeApi({
                    categoryType: formModel.categoryType,
                  });
                  return loopSelectableTree(data, 'categoryFlag', true);
                } catch (error: any) {
                  return [];
                }
              },
            },
          };
        },
      },
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 100,
      sorter: true,
    },
    // dyingPeriod临期天数 expirationDate临期提醒日期
    {
      title: t('有效期至'),
      dataIndex: 'expiredDate',
      width: 100,
      hideInSearch: true,
      sorter: true,
      customRender: ({ record }) => (
        <div style={{ color: record.expireFlag ? '#FF5633' : record.dyingFlag ? '#FF9A2F' : '' }}>
          {record.expiredDate}
        </div>
      ),
    },
    {
      title: t('初始量'),
      dataIndex: 'initQuantity',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('消耗量'),
      dataIndex: 'consumeQuantity',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('质量状态'),
      dataIndex: 'qualityStatus',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }) => (
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
          }}>
          <BMStateTag type={qualityStatus[record.qualityStatus?.value]?.type}>{record.qualityStatus?.label}</BMStateTag>
        </div>
      ),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 180,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('120030007000002'),
          onClick: () => {
            firstRowData.value = record;
            operationType.value = OperationType.EDIT;
            materialBatchModalOpen.value = true;
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120030007000003'),
          onClick: () => {
            firstRowData.value = record;
            operationType.value = OperationType.VIEW;
            materialBatchModalOpen.value = true;
          },
        },
        {
          label: t('检验信息'),
          ifShow: hasPermission('120030007000007'),
          onClick: () => {
            reqGetInspectQueryMaterialBatchNo(record?.materialBatchNo, record?.materialId)
              .then((res: any) => {
                const { data } = res;
                inspectionRowData.value = data;
                InspectionDetailsModalRef.value.openModal();
              })
              .catch((error: any) => {
                message.error(error.message);
              });
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 6,
    },
  });

  const updateTable = () => {
    pageRef.value?.fetchData(0);
    pageRef.value?.fetchData(1);
  };

  const updateFirstTable = () => {
    pageRef.value?.fetchData(0);
  };

  const updateSecondTable = () => {
    pageRef.value?.fetchData(1);
  };

  // 当前操作行数据
  const secondRowData = ref<Recordable>({});
  const addMaterialModalOpen = ref<boolean>(false);
  const addMaterial = (currentNodes: Array<any>) => {
    if (!currentNodes[0]) return message.error(t('请先选择物料批次'));
    addMaterialModalOpen.value = true;
  };
  const viewMaterialModalOpen = ref<boolean>(false);

  const columnsSecond: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'storageMaterialNo',
      fixed: 'left',
      width: 100,
      headerSearchComponent: 'Input',
      sorter: true,
    },
    {
      title: t('用尽'),
      dataIndex: 'useUp',
      width: 100,
      customRender: ({ record }) => (record.useUp ? t('是') : t('否')),
    },
    {
      title: t('初始量'),
      dataIndex: 'initQuantity',
      width: 100,
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      width: 100,
    },
    {
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      width: 100,
    },
    {
      title: t('消耗量'),
      dataIndex: 'consumeQuantity',
      width: 100,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
    },
    {
      title: t('容器'),
      dataIndex: 'containerName',
      width: 100,
    },
    {
      title: t('暂存货位'),
      dataIndex: 'position',
      width: 100,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120030007000005'),
          onClick: () => {
            secondRowData.value = record;
            viewMaterialModalOpen.value = true;
          },
        },
      ],
    },
  ];
  //判断临期
  const judgingDeadline = (data: any) => {
    return data.expireFlag ? 'row-bg-error' : data.dyingFlag ? 'row-bg-warning' : '';
  };
  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    secondRowData,
    firstRowData,
    pageRef,
    updateTable,
    updateFirstTable,
    updateSecondTable,

    operationType,
    // 批次
    materialBatchModalOpen,
    addMaterialBatch,
    judgingDeadline,
    // 物料件
    addMaterial,
    addMaterialModalOpen,
    viewMaterialModalOpen,
    rowSelections,
    print,
    printOpen,
    printConfirm,
    InspectionDetailsModalRef,
    inspectionRowData,
  };
};
