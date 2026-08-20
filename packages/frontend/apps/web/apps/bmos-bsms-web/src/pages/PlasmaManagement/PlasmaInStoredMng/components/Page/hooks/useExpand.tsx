import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useExpand = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'no',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('箱号'),
      dataIndex: 'containerNo',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },
    {
      title: t('血浆重量'),
      dataIndex: 'weight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆类型'),
      dataIndex: 'type',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name ?? '-';
      },
    },
    {
      title: t('血浆免疫类型'),
      dataIndex: 'immunityType',
      hideInSearch: true,
      width: 130,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('限制级血浆'),
      dataIndex: 'restrictedFlag',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.restrictedFlag?.name ?? '-';
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'plasmaDonorSex',
      hideInSearch: true,
      width: 80,
      customRender: ({ record }) => {
        return record?.plasmaDonorSex?.name ?? '-';
      },
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'plasmaDonorBloodType',
      hideInSearch: true,
      width: 80,
      customRender: ({ record }) => {
        return record?.plasmaDonorBloodType?.name ?? '-';
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
