import { usePermissionStore } from '@/stores/permission';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Switch } from 'ant-design-vue';

export type UseTableParams = {
  openDecompose: Function;
  changeEnabled: Function;
};
export const useTable = ({ openDecompose, changeEnabled }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const firstRowData = ref<any>({});
  const permissionDeptModalOpen = ref<boolean>(false);
  const productionLine = ref<any>({});
  const productionLineModalOpen = ref<boolean>(false);
  const columns: TableColumn[] = [
    {
      title: t('班组名称'),
      align: 'left',
      dataIndex: 'name',
      width: 200,
      resizable: true,
    },
    {
      title: t('班组编码'),
      align: 'left',
      dataIndex: 'code',
      resizable: true,
      sorter: true,
    },
    {
      title: t('班组描述'),
      align: 'left',
      resizable: true,
      hideInSearch: true,
      dataIndex: 'description',
    },
    {
      title: t('班组人数'),
      align: 'left',
      resizable: true,
      hideInSearch: true,
      dataIndex: 'peopleNum',
    },
    {
      title: t('创建时间'),
      align: 'left',
      resizable: true,
      hideInSearch: true,
      dataIndex: 'createTime',
      sorter: true,
    },
    {
      title: t('启停'),
      align: 'left',
      fixed: 'right',
      width: 100,
      hideInSearch: true,
      dataIndex: 'status',
      customRender: ({ record }) => (
        <Switch
          v-model:checked={record.status.value}
          v-hasAuth='120030005000004'
          checkedValue='TRUE'
          unCheckedValue='FALSE'
          onClick={() => changeEnabled(record)}
          loading={record.loading}
        />
      ),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('120030005000002') && record.status.value == 'FALSE',
          onClick: () => {
            openDecompose('update', record);
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120030005000003'),
          onClick: () => {
            openDecompose('view', record);
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('120030005000005'),
          onClick: () => {
            firstRowData.value = record;
            permissionDeptModalOpen.value = true;
          },
        },
        {
          label: t('绑定产线'),
          ifShow: hasPermission('120030005000006'),
          onClick: () => {
            productionLine.value = record;
            productionLineModalOpen.value = true;
          },
        },
      ],
    },
  ];

  return {
    columns,
    permissionDeptModalOpen,
    firstRowData,
    productionLine,
    productionLineModalOpen,
  };
};
