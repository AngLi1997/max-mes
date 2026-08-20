import { RemarkDetail } from '@/components/RemarkModal';
import { useWarn } from '@/hooks';
import { deleteStaticDataConfig, postStaticDataConfigEdit } from '@/services';
import { usePermissionStore } from '@/stores';
import { OperationStatusMap } from '@/types';
import type { TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Switch, message } from 'ant-design-vue';
import { TableParams } from '../types';

export const useReceivingLibraryTable = ({ firstRowData, updateTableData }: TableParams) => {
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const addEditModalOpen = ref<boolean>(false);
  const receivingLibraryOperationStatus = ref<OperationStatusMap>(OperationStatusMap.ADD);

  const { warnModal } = useWarn();

  const switchLoading = ref<boolean>(false);
  const changeStatus = async (record: any) => {
    try {
      await postStaticDataConfigEdit({
        id: record.id,
        status: record.status === '1' ? '0' : '1',
        enumsValue: record.enumsValue,
      });
      message.success(t('操作成功'));
      updateTableData && updateTableData();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const receivingLibraryRemarkModalOpen = ref<boolean>(false);
  const receivingLibraryRemarkDetails = ref<RemarkDetail[]>([]);

  const columnsFirst: TableColumn[] = [
    {
      title: t('领用库编号'),
      dataIndex: 'substNo',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('领用库名称'),
      dataIndex: 'enumsValue',
      width: 100,
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      width: 100,
    },
    {
      title: t('启用'),
      dataIndex: 'status',
      width: 100,
      customRender: ({ record }) => {
        return (
          <Switch checked={record.status === '1'} loading={switchLoading.value} onClick={() => changeStatus(record)} />
        );
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 100,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      sorter: true,
      width: 100,
      customRender: ({ record }) => {
        return getDateFormat(record.updateTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }: any, { fetchData }: TableActionType) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080002000001'),
          onClick: () => {
            firstRowData.value = record;
            receivingLibraryOperationStatus.value = OperationStatusMap.EDIT;
            addEditModalOpen.value = true;
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('210080002000002'),
          danger: true,
          onClick: () => {
            warnModal(t('是否删除该数据?'), {
              async onOk() {
                try {
                  await deleteStaticDataConfig({
                    receiveStoreNo: record.substNo,
                    ids: [record.id],
                  });
                  message.success(t('操作成功'));
                  fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
        {
          label: t('备注'),
          onClick: () => {
            receivingLibraryRemarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('备注'),
              },
            ];
            receivingLibraryRemarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  const addReceivingLibrary = () => {
    receivingLibraryOperationStatus.value = OperationStatusMap.ADD;
    addEditModalOpen.value = true;
  };

  return {
    receivingLibraryColumns: columnsFirst,
    receivingLibraryAddEditModalOpen: addEditModalOpen,
    receivingLibraryOperationStatus,
    receivingLibraryRemarkModalOpen,
    receivingLibraryRemarkDetails,
    addReceivingLibrary,
  };
};
