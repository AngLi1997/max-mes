import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const { plasmaAppearanceDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
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
      width: 170,
      resizable: true,
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
      title: t('待审标本外观'),
      dataIndex: 'waitAuditAppearance',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaAppearanceDict,
        },
      },
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
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
      title: t('待审标本外观'),
      dataIndex: 'waitAuditAppearance',
      hideInSearch: true,
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.waitAuditAppearance?.name}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'appearanceCheckBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'appearanceCheckDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditResult',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditResult?.name}</span>;
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
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170020013000003'),
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
