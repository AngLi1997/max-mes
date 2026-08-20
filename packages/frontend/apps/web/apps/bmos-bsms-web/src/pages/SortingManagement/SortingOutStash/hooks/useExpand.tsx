import { sortingOutBigContainer } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useExpand = (openCnt: any, fetchParentData: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('货位号'),
      dataIndex: 'cargoSpaceNo',
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
      title: t('总数量'),
      dataIndex: 'totalNumber',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('待分拣数量'),
      dataIndex: 'toSortNumber',
      width: 120,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.toSortNumber ? <a onClick={() => openCnt(record)}>{record?.toSortNumber}</a> : 0;
      },
    },
    {
      title: t('重量'),
      dataIndex: 'totalWeight',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('整盘出库'),
          ifShow: hasPermission('170080004000002'),
          onClick: () => {
            Modal.confirm({
              title: t('是否对该数据进行整盘出库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await sortingOutBigContainer(record.checkNo, record.bigContainerNo);
                  message.success(t('操作成功'));
                  fetchParentData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() {},
            });
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
