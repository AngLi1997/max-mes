import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openCntModal: any, openCreateReport: any) => {
  const { reportTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 250,
      resizable: true,
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInTable: !getWarehouseConfigByCode.value,
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.warehouse?.name;
      },
    },
    {
      title: t('报告状态'),
      dataIndex: 'reportStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.reportStatus?.name;
      },
    },
    {
      title: t('当前核查次数'),
      dataIndex: 'checkNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('核查数量'),
      dataIndex: 'checkNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('检疫期合格份数'),
      dataIndex: 'passNum',
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.passNum ? <a onClick={() => openCntModal(record, 1)}>{record?.passNum}</a> : 0;
      },
    },
    {
      title: t('检疫期不合格份数'),
      dataIndex: 'unPassNum',
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unPassNum ? <a onClick={() => openCntModal(record, 2)}>{record?.unPassNum}</a> : 0;
      },
    },
    {
      title: t('检疫期未通过份数'),
      dataIndex: 'unResNum',
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unResNum ? <a onClick={() => openCntModal(record, 3)}>{record?.unResNum}</a> : 0;
      },
    },
    {
      title: t('合格率'),
      dataIndex: 'passRate',
      width: 100,
      resizable: true,
    },
    {
      title: t('最近签发日期'),
      dataIndex: 'reportAuditTime',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 130,
      actions: ({ record }) => [
        {
          label: t('修改报告'),
          ifShow: hasPermission('170050002000002') && record?.reportStatus?.value == 1,
          onClick: () => {
            // look(record);
            openCreateReport(record, 'edit');
          },
        },
        {
          label: t('创建报告'),
          ifShow: hasPermission('170050002000001') && record?.reportStatus?.value == 0,
          onClick: () => {
            openCreateReport(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    schemas: [
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('检品批号'),
        field: 'inWarehouseBatchNo',
        component: 'Input',
      },
      {
        label: t('报告状态'),
        field: 'reportStatus',
        component: 'Select',
        componentProps: {
          options: reportTypeDict,
        },
      },
      {
        label: t('所在仓库'),
        field: 'warehouseId',
        vIf: getWarehouseConfigByCode.value,
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    ],
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
