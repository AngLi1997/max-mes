import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (enterView: any) => {
  const { plasmaAppearanceDict, plasmaTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
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
      title: t('血浆类型'),
      dataIndex: 'plasmaType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaTypeDict,
        },
      },
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
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
      title: t('待审血浆外观'),
      dataIndex: 'applyAppearance',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaAppearanceDict.filter((item: any) => item.value !== 1),
        },
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInTable: true,
    },
    {
      title: t('入库仓库'),
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
      title: t('待审血浆外观'),
      dataIndex: 'appearanceResult',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.appearanceResult?.name}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'applyByName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applyDate',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name}</span>;
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditByName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditDate',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
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
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170040007000003'),
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
    labelWidth: 105,
    labelAlign: 'left',
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
