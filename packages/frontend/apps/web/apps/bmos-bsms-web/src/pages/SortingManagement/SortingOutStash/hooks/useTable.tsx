import { sortingOutWarehouse } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Key, Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();

export const useTable = (openCnt: any) => {
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
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('所在仓库'),
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
      title: t('来源单位'),
      dataIndex: 'originOrg',
      hideInSearch: true,
      width: 220,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('合并出库'),
          ifShow: hasPermission('170080004000001'),
          onClick: () => {
            // look(record);
            Modal.confirm({
              title: t('是否对该数据进行合并出库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await sortingOutWarehouse(record?.checkNo);
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
      span: getWarehouseConfigByCode.value ? 12 : 18,
    },
    fieldMapToTime: [['outPlanDate', ['startDate', 'endDate'], 'YYYY-MM-DD']],
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
