import { sampleOutWarehouseOut } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Key, Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();

export const useTable = (openCnt: any, enterView: any) => {
  const { warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const fetchData = async (index: number = 0, params?: any) => {
    pageRef.value.fetchData(index, params);
  };

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand(openCnt, fetchData);
    } else {
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('出库批号'),
      dataIndex: 'outPlanBatchNo',
      width: 170,
      resizable: true,
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
      dataIndex: 'outboundType',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.outboundType?.name}</span>;
      },
    },
    {
      title: t('数量'),
      dataIndex: 'number',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('出库人'),
      dataIndex: 'outPlanBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outPlanDate',
      width: 150,
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
      title: t('库存状态'),
      dataIndex: 'warehouseStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouseStatus?.name}</span>;
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 160,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170020011000001'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
        {
          label: t('合并出库'),
          ifShow: hasPermission('170020011000003') && record?.warehouseStatus?.value == 1,
          onClick: () => {
            // look(record);
            Modal.confirm({
              title: t('是否对该数据进行合并出库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await sampleOutWarehouseOut({
                    outPlanBatchNo: record?.outPlanBatchNo,
                  });
                  message.success(t('操作成功'));
                  tableAction.fetchData();
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
    showAdvancedButton: false,
    actionColOptions: {
      span: getWarehouseConfigByCode.value ? 6 : 12,
    },
    fieldMapToTime: [['outPlanDate', ['outPlanDateUp', 'outPlanDateDown'], 'YYYY-MM-DD']],
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
