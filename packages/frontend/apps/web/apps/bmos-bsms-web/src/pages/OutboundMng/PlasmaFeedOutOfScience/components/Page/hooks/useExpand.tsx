import { usePermissionStore } from '@/stores/permission';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useExpand = (openCnt: any, openCheck: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('分拣后批号'),
      dataIndex: 'sortingPlanBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('大托盘编号'),
      dataIndex: 'bigContainerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.num ? <a onClick={() => openCnt(record)}>{record?.num}</a> : 0;
      },
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 120,
      resizable: true,
    },
    {
      title: t('箱/托盘号起'),
      dataIndex: 'containerNoUp',
      width: 190,
      resizable: true,
    },
    {
      title: t('箱/托盘号止'),
      dataIndex: 'containerNoDown',
      width: 190,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 150,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 150,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('整盘出库'),
          ifShow: hasPermission('170100008000003') && record?.currentInventoryStatus?.value === 2,
          onClick: () => {
            openCheck(record, 'trayNo');
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
