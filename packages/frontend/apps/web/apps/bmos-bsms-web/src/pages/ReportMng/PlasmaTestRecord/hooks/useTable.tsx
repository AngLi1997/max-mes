import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 180,
      resizable: true,
    },
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: ['plasmaDonorSex', 'label'],
      width: 100,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: ['plasmaDonorBloodType', 'label'],
      width: 100,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'plasmaWeight',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆公司检测结果'),
      dataIndex: 'plasmaCompanyResult',
      width: 300,
      resizable: true,
      children: [
        {
          title: t('HBsAg'),
          dataIndex: 'hbsAg',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hbsAg?.name ?? '-';
          },
        },
        {
          title: t('抗-HCV'),
          dataIndex: 'hcv',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hcv?.name ?? '-';
          },
        },
        {
          title: t('HIV-Ag/Ab'),
          dataIndex: 'hiv',
          width: 120,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hiv?.name ?? '-';
          },
        },
        {
          title: t('梅毒'),
          dataIndex: 'tp',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.tp?.name ?? '-';
          },
        },
        {
          title: t('ALT(U/L)'),
          dataIndex: 'alt',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.alt?.name ?? '-';
          },
        },
        {
          title: t('蛋白'),
          dataIndex: 'protein',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return record?.protein?.name ?? '-';
          },
        },
      ],
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    labelWidth: 100,
    schemas: [
      {
        label: t('血浆编号'),
        field: 'plasmaNo',
        component: 'Input',
      },
      {
        label: t('血浆箱号'),
        field: 'containerNo',
        component: 'Input',
      },
      {
        label: t('浆站出库批号'),
        field: 'syncBatchNo',
        component: 'Input',
      },
      {
        label: t('献浆者姓名'),
        field: 'plasmaDonorName',
        component: 'Input',
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
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
    ],
    fieldMapToTime: [['slurryDate', ['slurryDateBegin', 'slurryDateEnd'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
