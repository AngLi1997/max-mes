import { updatePlasmaColor } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { Modal, Switch, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useTable = (editColorRef: any) => {
  const pageRef = ref<any>(null);

  const operation = (record: any) => {
    Modal.confirm({
      title: t('是否确定初始化颜色'),
      icon: h(ExclamationCircleOutlined),
      closable: true,
      async onOk() {
        try {
          await updatePlasmaColor({ id: record.id, colour: record.initialColour });
          message.success(t('确认成功'));
          pageRef.value?.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  const changeStatus = async (record: any) => {
    try {
      if (!hasPermission('170110004000003')) return;
      await updatePlasmaColor({
        id: record.id,
        useFlag: record.useFlag.value,
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
      title: t('状态类型'),
      dataIndex: 'statusType',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.statusType?.name}</span>;
      },
    },
    {
      title: t('颜色'),
      dataIndex: 'colour',
      width: 170,
      resizable: true,
    },
    {
      title: t('颜色展示'),
      dataIndex: 'colorDisplay',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <div style={{ backgroundColor: record.colour, height: '20px', width: '40px' }} />;
      },
    },
    {
      title: t('启用'),
      dataIndex: 'useFlag',
      width: 170,
      resizable: true,
      // @ts-ignore
      customRender: ({ record }) => {
        return (
          <Switch
            v-model:checked={record.useFlag.value}
            checkedValue={1}
            unCheckedValue={0}
            disabled={!hasPermission('170110004000003')}
            onChange={() => changeStatus(record)}
          />
        );
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 120,
      resizable: true,
    },
    {
      title: t('操作时间'),
      dataIndex: 'updateTime',
      width: 170,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: t('颜色更改'),
          key: 'edit',
          ifShow: hasPermission('170110004000001'),
          onClick: () => {
            editColorRef.value.openModal(record);
          },
        },
        {
          label: t('颜色初始化'),
          key: 'init',
          ifShow: hasPermission('170110004000002'),
          onClick: () => {
            operation(record);
          },
        },
      ],
    },
  ];

  return {
    pageRef,
    columnsFirst,
  };
};
