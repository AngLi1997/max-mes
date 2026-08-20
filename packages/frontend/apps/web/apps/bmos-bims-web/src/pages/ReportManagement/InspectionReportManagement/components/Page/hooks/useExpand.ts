import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name ?? '-';
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
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
      dataIndex: 'sex',
      width: 90,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sex?.name ?? '-';
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 90,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name ?? '-';
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearance',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.appearance?.name ?? '-';
      },
    },
    {
      title: t('蛋白质含量'),
      dataIndex: 'protein',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.protein?.name ?? '-';
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'alt',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.alt?.name ?? '-';
      },
    },
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
      title: t('抗-HIV'),
      dataIndex: 'hiv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.hiv?.name ?? '-';
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'tp',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.tp?.name ?? '-';
      },
    },
    {
      title: t('HBV DNA'),
      dataIndex: 'pcrHbv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHbv?.name ?? '-';
      },
    },
    {
      title: t('HCV RNA'),
      dataIndex: 'pcrHcv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHcv?.name ?? '-';
      },
    },
    {
      title: t('HIV RNA'),
      dataIndex: 'pcrHiv',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.pcrHiv?.name ?? '-';
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.immunityType?.name ?? '-';
      },
    },
    {
      title: t('检验免疫类型'),
      dataIndex: 'titerType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return record?.titerType?.name ?? '-';
      },
    },
    {
      title: t('效价值'),
      dataIndex: 'titer',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('发布人'),
      dataIndex: 'publishBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('发布日期'),
      dataIndex: 'publishDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
  };
};
