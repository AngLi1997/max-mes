import { appearanceWarehouseIn, appearanceWarehouseOut } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Key, Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input, Modal, message } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (enterView: any) => {
  const { plasmaAppearanceDict } = getDicts();
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
      expandMap[newKey] = useExpand(enterView);
    } else {
      expandMap[newKey]?.fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.plasmaNoBegin} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.plasmaNoEnd} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.containerNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.containerNoDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        field: 'inWarehouseDate',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('血浆外观'),
      dataIndex: 'applyAppearance',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaAppearanceDict,
        },
      },
    },
    {
      title: t('数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 80,
      sorter: true,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'totalWeight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateBegin',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateEnd',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱号起'),
      dataIndex: 'containerNoUp',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆箱号止'),
      dataIndex: 'containerNoDown',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      hideInSearch: true,
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, { fetchData }: TableActionType) => [
        {
          label: t('整批出库'),
          ifShow: () => {
            return hasPermission('170040006000002') && record?.inWarehouseStatus?.value === 2;
          },
          onClick: () => {
            // look(record);
            Modal.confirm({
              title: t('是否对该数据进行整盘出库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await appearanceWarehouseOut({ inWarehouseBatchNo: record.inWarehouseBatchNo });
                  message.success(t('操作成功'));
                  await fetchData();
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
          label: t('整批回库'),
          ifShow: () => {
            return hasPermission('170040006000003') && record?.inWarehouseStatus?.value === 4;
          },
          onClick: () => {
            // look(record);
            Modal.confirm({
              title: t('是否对该数据进行整批回库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await appearanceWarehouseIn({ inWarehouseBatchNo: record.inWarehouseBatchNo });
                  message.success(t('操作成功'));
                  await fetchData();
                } catch (error: any) {
                  console.log(error);
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
