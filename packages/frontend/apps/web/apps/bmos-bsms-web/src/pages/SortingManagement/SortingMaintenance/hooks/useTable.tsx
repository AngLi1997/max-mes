import { getSortingMaintainPerson } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { FormProps, TableColumn } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useTable = () => {
  const pageRef = ref<any>(null);

  // 获取分拣人
  const getSortingMaintainPersonList = async (type: any) => {
    try {
      const { data } = await getSortingMaintainPerson({
        itemType: type,
      });
      pageRef.value?.getQueryFormRef()?.updateSchema({
        field: 'person',
        componentProps: {
          options: data || [],
        },
      });
      // await pageRef.value?.fetchData();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('分拣后批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('编号'),
      dataIndex: 'itemNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('分拣前箱/托盘号'),
      dataIndex: 'primeContainerNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('分拣后箱/托盘号'),
      dataIndex: 'containerNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('分拣人'),
      dataIndex: 'personName',
      width: 100,
      resizable: true,
    },
    {
      title: t('分拣日期'),
      dataIndex: 'sortTime',
      width: 150,
      sorter: true,
      resizable: true,
    },
    // {
    //   title: t('回库状态'),
    //   dataIndex: 'useFlag',
    //   width: 80,
    //   sorter: true,
    //   resizable: true,
    // },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 120,
    labelAlign: 'left',
    schemas: [
      {
        label: t('分拣后批号'),
        field: 'planBatchNo',
        component: 'Input',
      },
      {
        label: t('分拣前箱/托盘号'),
        field: 'primeContainerNo',
        component: 'Input',
      },
      {
        label: t('分拣人'),
        field: 'person',
        component: 'Select',
        componentProps: {
          fieldNames: { label: 'personName', value: 'personId' },
          option: [],
        },
      },
      {
        label: t('编号'),
        field: 'no',
        component: 'Input',
      },
      {
        label: t('分拣后箱/托盘号'),
        field: 'containerNo',
        component: 'Input',
      },
      {
        label: t('分拣日期'),
        field: 'sortTime',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [['sortTime', ['sortTimeBegin', 'sortTimeEnd'], 'YYYY-MM-DD']],
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    paginationFirst,
    getSortingMaintainPersonList,
  };
};
