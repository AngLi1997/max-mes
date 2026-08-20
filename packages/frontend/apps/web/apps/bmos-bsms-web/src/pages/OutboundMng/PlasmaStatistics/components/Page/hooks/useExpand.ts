import { usePermissionStore } from '@/stores/permission';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useExpand = (enterView: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('出库批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('出库类型'),
      dataIndex: 'type',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name;
      },
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('总重量'),
      dataIndex: 'weight',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('出库人'),
      dataIndex: 'outPlanBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outPlanDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('库存状态'),
      dataIndex: 'warehousingStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.warehousingStatus?.name;
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170100012000001'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
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
