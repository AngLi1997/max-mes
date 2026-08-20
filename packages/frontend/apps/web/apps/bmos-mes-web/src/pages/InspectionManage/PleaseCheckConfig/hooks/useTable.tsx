import { useWarn } from '@/hooks';
import { reqInspectConfigDelete, reqInspectConfigDisable, reqInspectConfigEnable } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Switch, message } from 'ant-design-vue';
import { OperationType } from '../type';

export const useTable = () => {
  const router = useRouter();
  const { hasPermission } = usePermissionStore();
  const { warnModal } = useWarn();

  const pageRef = ref<any>(null);
  const updateTable = () => {
    pageRef.value?.fetchData(0);
  };
  // 启停
  const switchLoading = ref<boolean>(false);
  const changeStatus = async (record: any) => {
    switchLoading.value = true;
    try {
      warnModal(record.enable ? t('是否停用该请验单') : t('是否启用该请验单'), {
        onOk: async () => {
          try {
            if (record.enable) {
              await reqInspectConfigDisable(record.id);
              message.success(t('停用成功'));
            } else {
              await reqInspectConfigEnable(record.id);
              message.success(t('启用成功'));
            }
            updateTable();
          } catch (error: any) {
            error.message && message.error(error.message);
          } finally {
            switchLoading.value = false;
          }
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      switchLoading.value = false;
    }
  };

  // 绑定物料
  const bindMaterialModalOpen = ref<boolean>(false);

  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('请验单名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 150,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('创建时间'),
      dataIndex: 'createTime',
      width: 150,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('最后更新人'),
      dataIndex: 'updateShowName',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('最后更新时间'),
      dataIndex: 'updateTime',
      width: 150,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('启停'),
      dataIndex: 'enable',
      width: 100,
      fixed: 'right',
      hideInSearch: true,
      customRender: ({ record }) => {
        return (
          <Switch
            disabled={!hasPermission('120100001000006')}
            checked={record.enable}
            loading={switchLoading.value}
            onClick={() => changeStatus(record)}
          />
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: !record.enable,
          code: '120100001000002',
          onClick: () => {
            router.push({
              name: 'PleaseCheckConfigDetail',
              query: {
                status: OperationType.Edit,
                id: record.id,
              },
            });
          },
        },
        {
          label: t('查看'),
          code: '120100001000004',
          onClick: () => {
            router.push({
              name: 'PleaseCheckConfigDetail',
              query: {
                status: OperationType.View,
                id: record.id,
              },
            });
          },
        },
        {
          label: t('绑定物料'),
          code: '120100001000005',
          onClick: () => {
            bindMaterialModalOpen.value = true;
            rowData.value = record;
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enable,
          code: '120100001000003',
          danger: true,
          onClick: () => {
            warnModal(t('是否删除该请验单'), {
              onOk: async () => {
                try {
                  await reqInspectConfigDelete(record.id);
                  message.success(t('删除成功'));
                  updateTable();
                } catch (error: any) {
                  error.message && message.error(error.message);
                } finally {
                  switchLoading.value = false;
                }
              },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    rowData,
    bindMaterialModalOpen,
    updateTable,
  };
};
