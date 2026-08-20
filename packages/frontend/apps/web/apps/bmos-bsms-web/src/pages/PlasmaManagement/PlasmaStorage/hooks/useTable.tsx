import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

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
      title: t('数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.totalNum ? <a onClick={() => openCnt(record)}>{record?.totalNum ?? 0}</a> : 0;
      },
    },
    {
      title: t('外观检验'),
      dataIndex: 'appearanceInspect',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.appearanceInspect?.name}</span>;
      },
    },

    {
      title: t('血浆箱/托盘号起'),
      dataIndex: 'containerNoUp',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号止'),
      dataIndex: 'containerNoDown',
      hideInSearch: true,
      width: 160,
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
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrg}</span>;
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
      title: t('浆站出库日期'),
      dataIndex: 'outDate',
      hideInSearch: true,
      width: 140,
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
          ifShow: hasPermission('170040003000001') && !record?.inWarehouseStatus?.value,
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
