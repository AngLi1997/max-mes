import {
  reqPlatformTagInstanceDeletePOST,
  reqPlatformTagInstanceDisablePOST,
  reqPlatformTagInstanceEnablePOST,
} from '@/api';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';
import { UseTableParams } from '../../../types';

export const useColumns = ({ emits }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const treeField = reactive({
    field: {
      tagTypeId: 'id',
    },
  });
  // 启停状态改变
  const versionStateLoading = ref<boolean>(false);
  const columns: TableColumn[] = [
    {
      title: t('标签名称'),
      dataIndex: 'tagName',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('标签类型'),
      dataIndex: 'tagTypeName',
      hideInSearch: true,
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('业务场景'),
      dataIndex: 'tagSceneName',
      width: 190,
      resizable: true,
    },
    {
      title: t('启停'),
      dataIndex: 'enable',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enable = record?.enable?.value === 'TRUE';
        return (
          <Switch
            v-hasAuth='100020007000005'
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
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('100020007000003'),
          onClick: () => {
            emits('viewTagPage', record);
          },
        },
        {
          label: t('编辑'),
          ifShow: record.enable?.value === 'FALSE' && hasPermission('100020007000002'),
          onClick: () => {
            emits('editTagPage', record);
          },
        },
        {
          label: t('删除'),
          ifShow: record.enable?.value === 'FALSE' && hasPermission('100020007000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该标签'),
              icon: h(ExclamationCircleOutlined),
              content: t('标签删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await reqPlatformTagInstanceDeletePOST({ id: record.id });
                  fetchData();
                  message.success(t('删除成功'));
                  return Promise.resolve();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ];
  const changeVersionState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此版本') : t('是否停用此版本');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}${record.tagName}`,
      onOk: async () => {
        try {
          versionStateLoading.value = true;
          if (checked) {
            await reqPlatformTagInstanceEnablePOST({
              id: record.id,
            });
            message.success(t('启用成功'));
            tableAction.fetchData();
          } else {
            await reqPlatformTagInstanceDisablePOST({
              id: record.id,
            });
            message.success(t('停用成功'));
            tableAction.fetchData();
          }
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.resolve();
        } finally {
          versionStateLoading.value = false;
        }
      },
    });
  };
  return {
    columns,
    treeField,
  };
};
