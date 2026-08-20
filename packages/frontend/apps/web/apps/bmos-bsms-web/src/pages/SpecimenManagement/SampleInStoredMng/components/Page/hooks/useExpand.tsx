import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 190,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name}</span>;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },

    {
      title: t('献浆者编号'),
      dataIndex: 'no',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.no}</span>;
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.name}</span>;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'plasmaDonorSex',
      hideInSearch: true,
      width: 80,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.sex?.name}</span>;
      },
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      hideInSearch: true,
      width: 80,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.bloodType?.name}</span>;
      },
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
  };
};
