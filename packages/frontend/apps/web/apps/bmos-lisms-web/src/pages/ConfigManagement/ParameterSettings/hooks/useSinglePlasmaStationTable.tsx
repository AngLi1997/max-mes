import { postStaticDataConfigStationEdit } from '@/services';
import { usePermissionStore } from '@/stores';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message, Switch } from 'ant-design-vue';
import { TableParams } from '../types';

export const useSinglePlasmaStationTable = ({ firstRowData, pageRef }: TableParams) => {
  const editModalOpen = ref<boolean>(false);
  const { getDateFormat } = useConfig();

  const { hasPermission } = usePermissionStore();
  const switchLoading = ref<boolean>(false);
  const changeStatus = async (record: any) => {
    try {
      await postStaticDataConfigStationEdit({
        id: record.id,
        status: record.status === 1 ? 0 : 1,
      });
      pageRef.value?.fetchData(0);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('浆站编码'),
      dataIndex: 'stationCode',
      sorter: true,
      fixed: 'left',
      width: 120,
    },
    {
      title: t('采浆中心名称'),
      dataIndex: 'centreName',
      width: 280,
    },
    {
      title: t('简称（中）'),
      dataIndex: 'shorterName',
      width: 120,
    },
    {
      title: t('简称（英）'),
      dataIndex: 'shorterCode',
      width: 120,
    },
    {
      title: t('联系电话'),
      dataIndex: 'telNumber',
      width: 200,
    },
    {
      title: t('地址'),
      dataIndex: 'address',
      width: 260,
    },
    {
      title: t('采浆中心系统地址'),
      dataIndex: 'url',
      width: 260,
    },
    {
      title: t('启用'),
      dataIndex: 'status',
      width: 100,
      customRender: ({ record }) => {
        return (
          <Switch checked={record.status === 1} loading={switchLoading.value} onClick={() => changeStatus(record)} />
        );
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 2,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      width: 200,
      customRender: ({ record }) => {
        return getDateFormat(record.updateTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080002000001'),
          onClick: () => {
            firstRowData.value = record;
            editModalOpen.value = true;
          },
        },
      ],
    },
  ];

  return {
    singlePlasmaStationColumns: columnsFirst,
    singlePlasmaStationEditModalOpen: editModalOpen,
  };
};
