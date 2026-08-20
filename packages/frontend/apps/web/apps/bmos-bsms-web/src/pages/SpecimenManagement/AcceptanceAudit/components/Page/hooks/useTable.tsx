import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const { acceptanceResultDict, sampleTypeDict, warehouseDict } = getDicts();
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
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
    },
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
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.boxIdUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.boxIdDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouse',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouse?.name}</span>;
      },
      formItemProps: {
        component: 'Select',
        field: 'warehouseId',
        componentProps: {
          options: warehouseDict,
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
      title: t('数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号起'),
      dataIndex: 'boxIdUp',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号止'),
      dataIndex: 'boxIdDown',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('验收结果'),
      dataIndex: 'acceptanceResult',
      width: 100,
      customRender: ({ record }) => {
        return <span>{record?.acceptanceResult?.name}</span>;
      },
      formItemProps: {
        label: t('标本验收'),
        component: 'Select',
        componentProps: {
          options: acceptanceResultDict,
        },
      },
    },
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      hideInTable: true,
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditResult',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditResult?.name}</span>;
      },
    },
    {
      title: t('验收人'),
      dataIndex: 'acceptanceBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('验收日期'),
      dataIndex: 'acceptanceDate',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('170020007000003'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    fieldMapToTime: [['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD']],
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
