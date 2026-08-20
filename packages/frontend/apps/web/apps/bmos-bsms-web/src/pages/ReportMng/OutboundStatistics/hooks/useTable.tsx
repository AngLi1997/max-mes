import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('浆站名称'),
      dataIndex: 'originOrgName',
      width: 220,
      resizable: true,
      fixed: 'left',
    },
    ...new Array(12).fill(0).map((_, index) => {
      return {
        title: t(`${index + 1}月`),
        dataIndex: `${index + 1}month`,
        width: 100,
        resizable: true,
        children: [
          {
            title: t('份数'),
            dataIndex: `${index + 1}Quantity`,
            width: 100,
            resizable: true,
            customRender: ({ record }: any) => {
              return record?.detailList?.[index]?.totalNum ?? '0';
            },
          },
          {
            title: t('重量'),
            dataIndex: `${index + 1}Weight`,
            width: 100,
            resizable: true,
            customRender: ({ record }: any) => {
              return record?.detailList?.[index]?.totalWeight ?? '-';
            },
          },
        ],
      };
    }),
  ];

  const formFirstProps: Partial<FormProps> = {
    initialValues: {
      year: new Date().getFullYear() + '',
    },
    showAdvancedButton: false,
    actionColOptions: { span: 12 },
    labelAlign: 'left',
    schemas: [
      {
        label: t('来源'),
        field: 'orgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('参考年份'),
        field: 'year',
        component: 'DatePicker',
        componentProps: {
          allowClear: false,
          picker: 'year',
          format: 'YYYY',
          valueFormat: 'YYYY',
          disabledDate: (current: any) => {
            return current && current > Date.now();
          },
        },
      },
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
