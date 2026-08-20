import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (openCnt: any, openBatchStorage: any) => {
  const { warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      resizable: true,
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
      title: t('数量(袋)'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.totalNum ? <a onClick={() => openCnt(record)}>{record?.totalNum}</a> : 0;
      },
    },
    {
      title: t('标本箱号起'),
      dataIndex: 'boxIdUp',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('标本箱号止'),
      dataIndex: 'boxIdDown',
      hideInSearch: true,
      width: 160,
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
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('浆站出库日期'),
      dataIndex: 'beginTime',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('公司入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInSearch: true,
      width: 140,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('整批入库'),
          ifShow: hasPermission('170020004000001') && !record?.currentInventoryStatus?.value,
          onClick: () => {
            openBatchStorage(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    labelWidth: 105,
    actionColOptions: {
      span: getWarehouseConfigByCode.value ? 6 : 12,
    },
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
