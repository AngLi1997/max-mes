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
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sex?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name;
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
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearance',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.appearance?.name;
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      hideInSearch: true,
      width: 140,
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
