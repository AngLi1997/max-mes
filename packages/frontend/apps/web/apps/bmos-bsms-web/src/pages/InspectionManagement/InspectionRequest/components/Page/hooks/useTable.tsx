import { sampleExaminationInfoOutWarehouse, sampleExaminationInfoSyncLims } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input, Modal, message } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const { auditResultDict, censorshipStatusDict, pleaseVerifyStatusDict, sampleTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand();
    } else {
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.sampleNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.sampleNoDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('请验批号'),
      dataIndex: 'inspectionBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('请验数量'),
      dataIndex: 'inspectionNum',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name ?? '-'}</span>;
      },
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouse',
      hideInSearch: true,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouse?.name ?? '-'}</span>;
      },
    },
    {
      title: t('请验人'),
      dataIndex: 'inspectionBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('请验日期'),
      dataIndex: 'inspectionDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('请验状态'),
      dataIndex: 'inspectionStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.inspectionStatus?.name ?? '-'}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: pleaseVerifyStatusDict,
        },
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('送检状态'),
      dataIndex: 'censorshipStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: censorshipStatusDict,
        },
      },
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name ?? '-'}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('送检状态'),
      dataIndex: 'censorshipStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.censorshipStatus?.name ?? '-'}</span>;
      },
    },
    {
      title: t('收检人'),
      dataIndex: 'receiveBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('收检日期'),
      dataIndex: 'receiveDate',
      width: 140,
      sorter: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: sampleTypeDict,
        },
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg ?? '-'}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170030001000003'),
          onClick: () => {
            enterView(record);
          },
        },
        {
          label: t('数据同步'),
          ifShow: hasPermission('170030001000002') && record?.censorshipStatus?.value === 2,
          onClick: () => {
            Modal.confirm({
              title: t('是否进行数据同步?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await sampleExaminationInfoSyncLims({ inspectionBatchNo: record?.inspectionBatchNo });

                  message.success(t('操作成功'));
                  fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() {},
            });
          },
        },
        {
          label: t('标本出库'),
          ifShow: hasPermission('170030001000002') && [0, 1].includes(record?.censorshipStatus?.value),
          onClick: () => {
            Modal.confirm({
              title: t('是否进行标本出库?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await Promise.all([
                    sampleExaminationInfoOutWarehouse({ inspectionBatchNo: record?.inspectionBatchNo }),
                    sampleExaminationInfoSyncLims({ inspectionBatchNo: record?.inspectionBatchNo }),
                  ]);
                  // await sampleExaminationInfoOutWarehouse({ inspectionBatchNo: record?.inspectionBatchNo });
                  // await sampleExaminationInfoSyncLims({ inspectionBatchNo: record?.inspectionBatchNo });
                  message.success(t('操作成功'));
                  fetchData();
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
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    fieldMapToTime: [
      ['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD'],
      ['receiveDate', ['receiveDateUp', 'receiveDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    rowData,
    expandMap,
    expandedRowKeys,
    columnsFirst,
    formFirstProps,
    expandChange,
  };
};
