import { getPrethawingErrorPage } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { TableColumn } from '@bmos/components';

export const useTable = (batchNo: string) => {
  const tableRef = ref<any>(null);

  const loadData = async (params: any) => {
    const datas = {
      ...params,
      batchNo,
    };
    const res = await getPrethawingErrorPage(datas);
    return res;
  };

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'orgNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('异常类型'),
      dataIndex: 'errorType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.errorType?.name ?? '-';
      },
    },
    {
      title: t('血浆重量'),
      dataIndex: 'weight',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.corrRelationType?.name ?? '-';
      },
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearanceResult',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.appearanceResult?.name ?? '-';
      },
    },
    {
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaStatus?.name ?? '-';
      },
    },
    {
      title: t('血浆类型'),
      dataIndex: 'type',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name ?? '-';
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
      title: t('限制级血浆'),
      dataIndex: 'restrictedFlag',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return record?.restrictedFlag?.name ?? '-';
      },
    },
  ];

  const pagination = reactive({
    ...paginationBig,
  });

  return {
    tableRef,
    columns,
    loadData,
    pagination,
  };
};
