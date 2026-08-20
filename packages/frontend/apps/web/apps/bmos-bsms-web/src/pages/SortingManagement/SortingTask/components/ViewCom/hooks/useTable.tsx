import { getSortingTaskDetailList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { TableColumn } from '@bmos/components';

export const useTable = (sortingTaskId: string) => {
  const tableRef = ref<any>(null);

  const loadData = async (params: any, onChangeParams: any) => {
    const datas = {
      ...params,

      sortingTaskId: sortingTaskId,
    };
    const res = await getSortingTaskDetailList(datas);
    return res;
  };

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆原箱/托盘号'),
      dataIndex: 'primeContainerNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      width: 170,
      sorter: true,
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
      title: t('对应编号'),
      dataIndex: 'corrPlasmaNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.corrRelationType?.name;
      },
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearanceResult',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.appearanceResult?.name;
      },
    },
    {
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaStatus?.name;
      },
    },
    {
      title: t('血浆类型'),
      dataIndex: 'type',
      width: 120,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name;
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'titerTypeName',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('限制级血浆'),
      dataIndex: 'restrictedFlag',
      width: 130,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.restrictedFlag?.name;
      },
    },
    {
      title: t('分拣后批号'),
      dataIndex: 'sortingPlanBatchNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('分拣后托盘号'),
      dataIndex: 'bigContainerNo',
      width: 170,
      sorter: true,
      resizable: true,
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
