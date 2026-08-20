import { deletePlasmaStation, enableOrDisablePlasmaStation } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useTable = (openModal: any) => {
  const pageRef = ref<any>(null);
  const changeStatus = async (record: any) => {
    try {
      if (!hasPermission('170110002000002')) return;
      await enableOrDisablePlasmaStation({
        id: record.id,
        useFlag: record.useFlag?.value,
      });
      message.success(t('操作成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      pageRef.value?.fetchData();
    }
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('采浆中心名称'),
      dataIndex: 'name',
      width: 170,
      resizable: true,
    },
    {
      title: t('简称'),
      dataIndex: 'shorterName',
      width: 80,
      resizable: true,
    },
    {
      title: t('联系电话'),
      dataIndex: 'tel',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('中文简称'),
      dataIndex: 'abbr',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('采浆中心系统地址'),
      dataIndex: 'stationUrl',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('系统编码'),
      dataIndex: 'sysNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('启用'),
      dataIndex: 'useFlag',
      width: 80,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: [
            { label: t('启用'), value: 1 },
            { label: t('停用'), value: 0 },
          ],
        },
      },
      customRender: ({ record }: any) => {
        return (
          <Switch
            v-model:checked={record.useFlag.value}
            checkedValue={1}
            disabled={!hasPermission('170110002000002')}
            unCheckedValue={0}
            onChange={() => changeStatus(record)}
          />
        );
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('170110002000003'),
          onClick: () => {
            openModal('edit', record);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('170110002000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deletePlasmaStation(record.id);
                  message.success(t('删除成功'));
                  await tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject(error);
                }
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    labelWidth: 100,
    // labelAlign: 'left',
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
