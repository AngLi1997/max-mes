import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
    {
      title: t('血浆批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱号起'),
      dataIndex: 'containerNoUp',
      width: 180,
      resizable: true,
    },
    {
      title: t('血浆箱号止'),
      dataIndex: 'containerNoDown',
      width: 180,
      resizable: true,
    },
    {
      title: t('数量(份)'),
      dataIndex: 'totalNum',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('重量(Kg)'),
      dataIndex: 'totalWeight',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      width: 100,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    labelWidth: 100,
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
        label: t('血浆批号'),
        field: 'inWarehouseBatchNo',
        component: 'Input',
      },
      {
        label: t('入库日期'),
        field: 'inWarehouseDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('采浆日期'),
        field: 'slurryDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['inWarehouseDate', ['inWarehouseDateUp', 'inWarehouseDateDown'], 'YYYY-MM-DD'],
      ['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
