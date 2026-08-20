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
      width: 150,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name ?? '-';
      },
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.no ?? '-';
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.name ?? '-';
      },
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.sex?.name ?? '-';
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.bloodType?.name ?? '-';
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 160,
      resizable: true,
    },
    // tips: 后端说去掉
    // {
    //   title: t('血浆外观'),
    //   dataIndex: 'plasmaAppearance',
    //   width: 160,
    //   resizable: true,
    //   customRender: ({ record }) => {
    //     return record?.plasmaAppearance?.name ?? '-';
    //   },
    // },
    {
      title: t('蛋白质含量'),
      dataIndex: 'proteinContentResult',
      width: 140,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.proteinContentResult?.name ?? '-';
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'altResult',
      width: 100,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.altResult?.name ?? '-';
      },
    },
    {
      title: t('HBsAg'),
      dataIndex: 'elisaHbsagResult',
      width: 120,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.elisaHbsagResult?.name ?? '-';
      },
    },
    {
      title: t('抗-HCV'),
      dataIndex: 'elisaHcvResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.elisaHcvResult?.name ?? '-';
      },
    },
    {
      title: t('抗-HIV'),
      dataIndex: 'elisaHivResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.elisaHivResult?.name ?? '-';
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'elisaTpResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.elisaTpResult?.name ?? '-';
      },
    },
    {
      title: t('HBV DNA'),
      dataIndex: 'pcrHbvResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.pcrHbvResult?.name ?? '-';
      },
    },
    {
      title: t('HCV RNA'),
      dataIndex: 'pcrHcvResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.pcrHcvResult?.name ?? '-';
      },
    },
    {
      title: t('HIV RNA'),
      dataIndex: 'pcrHivResult',
      width: 120,
      resizable: true,
      sorter: true,
      customRender: ({ record }) => {
        return record?.pcrHivResult?.name ?? '-';
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.immunityType?.name ?? '-';
      },
    },
    {
      title: t('效价值'),
      dataIndex: 'titerValue',
      width: 100,
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
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
    // rowSelections,
    // operationSelectedRows,
  };
};
