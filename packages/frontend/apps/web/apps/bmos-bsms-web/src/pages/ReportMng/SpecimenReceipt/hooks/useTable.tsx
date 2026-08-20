import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const { sampleTypeDict } = getDicts();
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
      title: t('标本批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name ?? '-';
      },
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
      title: t('标本箱号起'),
      dataIndex: 'boxIdUp',
      width: 180,
      resizable: true,
    },
    {
      title: t('标本箱号止'),
      dataIndex: 'boxIdDown',
      width: 180,
      resizable: true,
    },
    {
      title: t('数量(份)'),
      dataIndex: 'totalNum',
      width: 100,
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
        label: t('标本批号'),
        field: 'inWarehouseBatchNo',
        component: 'Input',
      },
      {
        label: t('标本类型'),
        field: 'sampleType',
        component: 'Select',
        componentProps: {
          options: sampleTypeDict,
        },
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
      // {
      //   label: t('采浆日期止'),
      //   field: 'plasmaBoxNo3',
      //   component: 'RangePicker',
      //   componentProps: {
      //     format: 'YYYY-MM-DD',
      //     valueFormat: 'YYYY-MM-DD',
      //   },
      // },
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
