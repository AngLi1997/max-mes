import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (openReceive: any, cancelSync: any, openCount: any) => {
  const { SYNC_TYPE, receiveStatusDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    // {
    //   title: t('同步人'),
    //   dataIndex: 'syncByName',
    //   hideInSearch: true,
    //   width: 170,
    //   sorter: true,
    //   resizable: true,
    // },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
      formItemProps: {
        order: 1,
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('同步日期'),
      dataIndex: 'syncTime',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        order: 4,
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('同步数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('成功数量'),
      dataIndex: 'successNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return !record?.successNum ? (
          <span>{record?.successNum || 0}</span>
        ) : (
          <a onClick={() => openCount(record, 'success')}>{record?.successNum}</a>
        );
      },
    },
    {
      title: t('失败数量'),
      dataIndex: 'failNum',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return !record?.failNum ? (
          <span>{record?.failNum || 0}</span>
        ) : (
          <a onClick={() => openCount(record, 'fail')}>{record?.failNum}</a>
        );
      },
    },
    {
      title: t('导入批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
      formItemProps: {
        order: 2,
      },
    },
    {
      title: t('同步方式'),
      dataIndex: 'syncType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.syncType?.name}</span>;
      },
      formItemProps: {
        order: 3,
        component: 'Select',
        componentProps: {
          options: SYNC_TYPE,
        },
      },
    },
    {
      title: t('接收状态'),
      dataIndex: 'receiveStatus',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.receiveStatus?.name}</span>;
      },
      formItemProps: {
        order: 5,
        component: 'Select',
        componentProps: {
          options: receiveStatusDict,
        },
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('修改信息'),
          ifShow: hasPermission('170040001000005') && record?.receiveStatus?.value == 1,
          onClick: () => {
            // look(record);
            openReceive([{ ...record }], 'edit');
          },
        },
        {
          label: t('撤销同步'),
          ifShow: hasPermission('170040001000004') && record?.syncType?.value == 2 && record?.receiveStatus?.value == 1,
          onClick: () => {
            cancelSync([{ ...record }]);
          },
        },
        {
          label: t('确认接收'),
          ifShow: hasPermission('170040001000003') && record?.receiveStatus?.value == 0,
          onClick: () => {
            // look(record);
            openReceive([{ ...record }], 'receive');
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['syncTime', ['syncDateBegin', 'syncDateEnd'], 'YYYY-MM-DD']],
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
