import DepartMent from '@/components/DepartMent/index.vue';
import ModalBtn from '@/components/ModalBtn/index.vue';

import { resourcePermissionSave, storageConfigDeleteById, storageConfigDisable, storageConfigEnable } from '@/services';

import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

import type { Recordable, TableActionType, TableColumn } from '@bmos/components';

import { Button, Modal, Switch, message } from 'ant-design-vue';

import { modalStatus } from '../enum';

export const useColumns = (useModalForm: any) => {
  const { storageAdd } = useModalForm;
  const depart = ref();
  // 启停状态改变
  const versionStateLoading = ref<boolean>(false);
  const treeField = reactive({
    field: {
      storageId: 'id',
    },
  });
  const column: TableColumn[] = [
    {
      title: t('暂存货位'),
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
        const enable = record?.enable?.value !== 'FALSE';
        return (
          <Switch
            v-hasAuth='120020010000009'
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
      width: 220,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        return (
          <div class='operation-area'>
            <Button
              v-hasAuth='120020010000005'
              style='max-width: 100px; min-width: 40px'
              type='link'
              onClick={() => storageAdd(record, modalStatus.View)}>
              {t('查看')}
            </Button>

            {record?.enable?.value !== 'FALSE' ? (
              ''
            ) : (
              <Button
                v-hasAuth='120020010000006'
                style='max-width: 100px; min-width: 40px'
                type='link'
                onClick={() => storageAdd(record, modalStatus.Edit)}>
                {t('编辑')}
              </Button>
            )}
            <ModalBtn submit={() => permissions(record, tableAction)} title={t('部门权限')}>
              {{
                default: () =>
                  h(DepartMent, {
                    ref: depart,
                    isAdd: false,
                    record: record.id,
                  }),
                trigger: () => <a v-hasAuth='120020010000007'>{t('数据权限')}</a>,
              }}
            </ModalBtn>
            {record?.enable?.value === 'FALSE' ? (
              <Button
                v-hasAuth='120020010000008'
                style='max-width: 100px; min-width: 40px'
                type='link'
                danger
                onClick={() => handleDelete(record, tableAction)}>
                {t('删除')}
              </Button>
            ) : null}
          </div>
        );
      },
    },
  ];
  //删除
  const handleDelete = async (params: Recordable, tableAction: TableActionType) => {
    Modal.confirm({
      title: t('删除确认'),
      icon: h(ExclamationCircleOutlined),
      closable: true,
      content: t('是否删除该暂存货位？'),
      onOk: async () => {
        try {
          await storageConfigDeleteById({ id: params.id });
          message.success(t('删除成功！'));
          tableAction.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  //部门权限
  const permissions = async (record: any, tableAction: TableActionType) => {
    try {
      const data = {
        resourceId: record.id,
        deptIds: depart.value.getSelectKeys(),
      };
      if (data.deptIds.length === 0) {
        message.error(t('请选择部门'));
        return Promise.reject();
      }
      const res = await resourcePermissionSave(data);
      if (res.code === 0) {
        message.success(t('保存数据权限成功'));
        tableAction.fetchData();
        return Promise.resolve();
      }
    } catch (error: any) {
      message.error(error.message);
      return Promise.reject();
    }
  };
  const changeVersionState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用') : t('是否停用');
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
  };
};
