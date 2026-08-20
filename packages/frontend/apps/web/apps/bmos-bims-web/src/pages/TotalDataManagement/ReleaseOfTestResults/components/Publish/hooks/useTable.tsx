import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('采样日期'),
      dataIndex: 'slurryDate',
      width: 160,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
      resizable: true,
    },
    {
      title: t('蛋白质含量'),
      dataIndex: 'protein',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.protein?.name;
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'alt',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.alt?.name;
      },
    },
    {
      title: t('HBsAg'),
      dataIndex: 'hbsAg',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.hbsAg?.name;
      },
    },
    {
      title: t('抗-HCV'),
      dataIndex: 'hcv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.hcv?.name;
      },
    },
    {
      title: t('抗-HIV'),
      dataIndex: 'hiv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.hiv?.name;
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'tp',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.tp?.name;
      },
    },
    {
      title: t('HBV DNA'),
      dataIndex: 'pcrHbv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHbv?.name;
      },
    },
    {
      title: t('HCV RNA'),
      dataIndex: 'pcrHcv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHcv?.name;
      },
    },
    {
      title: t('HIV RNA'),
      dataIndex: 'pcrHiv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHiv?.name;
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return record?.immunityType?.name;
      },
    },
    {
      title: t('检验免疫类型'),
      dataIndex: 'titerType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.titerType?.name;
      },
    },
    {
      title: t('效价值'),
      dataIndex: 'titer',
      width: 100,
      resizable: true,
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  // const setRef = (el: any) => {
  //   pageRef.value = el;
  // };

  // const fetchData = async (params: any) => {
  //   pageRef.value.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
