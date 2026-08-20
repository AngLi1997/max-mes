import { storageConfigDeleteById, storageConfigDisable, storageConfigEnable } from '@/services';

import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

import type { Recordable, TableActionType, TableColumn } from '@bmos/components';

import { Modal, Switch, message } from 'ant-design-vue';

import { usePermissionStore } from '@/stores/permission';
import { modalStatus } from '../enum';

export const useColumns = (useModalForm: any) => {
  const { hasPermission } = usePermissionStore();

  const { storageAdd } = useModalForm;
  // 数据权限modal
  const permissionModalOpen = ref<boolean>(false);
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  // 启停状态改变
  const versionStateLoading = ref<boolean>(false);
  const treeField = reactive({
    field: {
      storageId: 'id',
    },
  });
  const column: TableColumn[] = [
    {
      title: t('货位名称'),
      dataIndex: 'position',
      // fixed: 'left',
      width: 150,
      resizable: true,
    },
    {
      title: t('货位编码'),
      dataIndex: 'code',
      width: 150,
      resizable: true,
    },
    {
      title: t('所属区域'),
      dataIndex: 'path',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('启停'),
      dataIndex: 'enable',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enable = record?.enable || 'false';
        return (
          <Switch
            v-hasAuth='150010002000009'
            checked={enable}
            loading={versionStateLoading.value}
            onChange={checked => {
              changeVersionState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      key: 'ACTION',
      hideInSearch: true,
      width: 190,
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: hasPermission('150010002000005'),
          onClick: () => {
            storageAdd(record, modalStatus.View);
          },
        },
        {
          label: t('编辑'),
          ifShow: !record?.enable && hasPermission('150010002000006'),
          onClick: () => {
            storageAdd(record, modalStatus.Edit);
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('150010002000007'),
          onClick: () => {
            permissionModalOpen.value = true;
            firstRowData.value = record;
          },
        },
        {
          label: t('删除'),
          ifShow: !record?.enable && hasPermission('150010002000008'),
          onClick: () => {
            handleDelete(record, tableAction);
          },
        },
      ],
    },
  ];
  //删除
  const handleDelete = async (params: Recordable, tableAction: TableActionType) => {
    Modal.confirm({
      title: t('删除确认'),
      icon: h(ExclamationCircleOutlined),
      closable: true,
      content: t('是否删除该货位'),
      onOk: async () => {
        try {
          await storageConfigDeleteById({ id: params.id });
          message.success(t('删除成功'));
          tableAction.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  const changeVersionState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此版本') : t('是否停用此版本');

    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}${record.position}`,
      onOk: async () => {
        try {
          versionStateLoading.value = true;
          if (checked) {
            await storageConfigEnable({
              id: record.id,
            });
            message.success(t('启用成功'));
            tableAction.fetchData();
          } else {
            await storageConfigDisable({
              id: record.id,
            });
            message.success(t('停用成功'));
            tableAction.fetchData();
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        } finally {
          versionStateLoading.value = false;
          return Promise.resolve();
        }
      },
      onCancel() {},
    });
  };
  return {
    columns: [column],
    treeField,
    permissionModalOpen,
    firstRowData,
  };
};
