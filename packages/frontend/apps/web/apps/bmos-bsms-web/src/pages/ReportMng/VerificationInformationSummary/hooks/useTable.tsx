import { getTableHeader } from '@/services';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const changeColumns = async () => {
    // await getPlasmaStations();
    const { data } = await getTableHeader();
    const col =
      data?.map((item: any) => {
        return {
          ...item,
          children: [
            {
              title: t('数量'),
              dataIndex: 'num',
              width: 100,
              // sorter: true,
              resizable: true,
              customRender: ({ record }: any) => {
                return record?.sortingDetailMap?.[item.dataIndex]?.num ?? '0';
              },
            },
            {
              title: t('重量'),
              dataIndex: 'weight',
              width: 100,
              // sorter: true,
              resizable: true,
              customRender: ({ record }: any) => {
                return record?.sortingDetailMap?.[item.dataIndex]?.weight ?? '0';
              },
            },
          ],
        };
      }) || [];
    pageRef.value?.getTableRef()?.addColumn(col, 'passNum');
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      width: 170,
      fixed: 'left',
      resizable: true,
    },
    {
      title: t('放行单编号'),
      dataIndex: 'passNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('合格'),
      dataIndex: 'passNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('不合格'),
      dataIndex: 'unPassNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('未检'),
      dataIndex: 'unResNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('放行日期'),
      dataIndex: 'auditTime',
      width: 170,
      sorter: true,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    labelAlign: 'left',
    labelWidth: 100,
    schemas: [
      {
        label: t('核查批号'),
        field: 'checkNo',
        component: 'Input',
      },
      {
        label: t('放行单编号'),
        field: 'passNo',
        component: 'Input',
      },
      {
        label: t('放行日期'),
        field: 'auditTime',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [['auditTime', ['auditTimeUp', 'auditTimeDown'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    changeColumns,
    formFirstProps,
  };
};
