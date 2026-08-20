import { getSampleSortingPlanDetailList, getSortingPlanDetailList } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { FormProps, TableColumn } from '@bmos/components';

export const useTable = (itemType: Number) => {
  const { sampleTypeDict, sortingStatusDict } = getDicts();
  const pageRef = ref<any>(null);

  const loadData = async (params: any): Promise<any> => {
    const data = {
      ...params,
    };
    return itemType === 1 ? await getSortingPlanDetailList(data) : await getSampleSortingPlanDetailList(data);
  };

  const columnsFirst = reactive<TableColumn[]>([
    {
      title: t('血浆编号'),
      dataIndex: 'itemNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('批次号'),
      dataIndex: 'batchNo',
      width: 150,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 130,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('分拣状态'),
      dataIndex: 'sortingStatus',
      width: 120,
      resizable: true,
      // customRender: ({ record }) => {
      //   return record?.sortingStatus?.name;
      // },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: sortingStatusDict,
        },
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name;
      },
    },
  ]);

  const sampleColumns = reactive<TableColumn[]>([
    {
      title: t('标本编号'),
      dataIndex: 'itemNo',
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: sampleTypeDict,
        },
      },
    },
    {
      title: t('分拣状态'),
      dataIndex: 'sortingStatus',
      hideInSearch: true,
    },
  ]);

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    columnsFirst,
    sampleColumns,
    formFirstProps,
    paginationFirst,
    loadData,
  };
};
