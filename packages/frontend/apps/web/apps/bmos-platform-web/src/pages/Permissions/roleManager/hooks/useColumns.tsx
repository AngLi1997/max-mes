import { deleteRole } from '@/api/Permissions/roleManagement';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { modalStatus } from '../enum';

export const useColumns = (useModalForm: any, useTree: any) => {
  const { hasPermission } = usePermissionStore();

  const { storageAdd } = useModalForm;
  const { pageRoleManager } = useTree;
  // 角色id
  const roleId = ref<string>('');
  // '1'为菜单权限 '2'为权限授权
  const type = ref<string>('');
  // 人员分配
  const openPeople = ref<boolean>(false);
  // 菜单分配
  const openMenu = ref<boolean>(false);
  const DepartmentAllocationRef = ref<any>();
  const treeField = reactive({
    field: {
      roleTypeId: 'id',
    },
  });
  const columns: TableColumn[] = [
    {
      title: t('角色名称'),
      dataIndex: 'roleName',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('分类'),
      dataIndex: 'roleTypeName',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 280,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }) => [
        {
          label: t('人员分配'),
          ifShow: hasPermission('100030003000005'),
          onClick: () => {
            openPeople.value = true;
            roleId.value = record.id;
          },
        },
        {
          label: t('权限授权'),
          ifShow: hasPermission('100030003000009'),
          onClick: () => {
            openMenu.value = true;
            roleId.value = record.id;
            type.value = '2';
          },
        },
        {
          label: t('菜单分配'),
          ifShow: hasPermission('100030003000006'),
          onClick: () => {
            openMenu.value = true;
            roleId.value = record.id;
            type.value = '1';
          },
        },
        {
          label: t('部门分配'),
          ifShow: hasPermission('100030003000010'),
          onClick: () => {
            roleId.value = record.id;
            DepartmentAllocationRef.value.openModal();
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('100030003000007'),
          onClick: () => {
            storageAdd(record, modalStatus.Edit);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('100030003000008'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除角色信息'),
              icon: h(ExclamationCircleOutlined),
              content: t('角色信息删除后无法恢复，是否继续?'),
              async onOk() {
                try {
                  await deleteRole(record);
                  message.success(t('删除成功'));
                  pageRoleManager.value?.fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ];
  return {
    openMenu,
    openPeople,
    roleId,
    type,
    columns,
    treeField,
    DepartmentAllocationRef,
  };
};
