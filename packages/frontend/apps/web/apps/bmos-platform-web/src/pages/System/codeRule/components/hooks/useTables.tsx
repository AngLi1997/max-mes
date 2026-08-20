import {
  reqPlatformCodeRuleVersionConfirmPUT,
  reqPlatformCodeRuleVersionDELETE,
  reqPlatformCodeRuleVersionDisabledPUT,
  reqPlatformCodeRuleVersionEnabledPUT,
} from '@/api';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { Recordable, TableInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';
import { VersionStatus } from '../../types/enum';

export type UseTableParams = {
  emits: any;
};

export const useTables = ({ emits }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<TableInstance>();
  const selectCodeRule = ref<Recordable>({});
  const permissionOpen = ref<boolean>(false);
  const resourceId = ref<string>('');

  const VersionStatusClassMap = new Map([
    [VersionStatus.EDIT, 'edit'],
    [VersionStatus.CONFIRM, 'confirm'],
  ]);
  const versionStateLoading = ref<boolean>(false);
  const changeVersionState = (record: Recordable, checked: boolean) => {
    let content = checked ? t('是否启用该编号规则版本') : t('是否停用该编号规则版本');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      closable: true,
      content,
      okText: t('确定'),
      cancelText: t('取消'),
      onOk() {
        confirmSwitch(record, checked as boolean);
      },
    });
  };
  const confirmSwitch = async (row: Recordable, checked: boolean): Promise<void> => {
    versionStateLoading.value = true;
    try {
      if (checked) {
        await reqPlatformCodeRuleVersionEnabledPUT(row.id);
        message.success(t('启用成功'));
      } else {
        await reqPlatformCodeRuleVersionDisabledPUT(row.id);
        message.success(t('停用成功'));
      }
      tableInstance.value?.fetchData(1);
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      versionStateLoading.value = false;
    }
  };

  const columns: TableColumn[][] = [
    [
      {
        title: t('编号规则'),
        dataIndex: 'name',
        fixed: 'left',
        width: 190,
        resizable: true,
      },
      {
        title: t('规则编码'),
        dataIndex: 'code',
        resizable: true,
        width: 190,
      },
      {
        title: t('启用版本'),
        dataIndex: 'version',
        resizable: true,
        width: 190,
        hideInSearch: true,
      },
      // {
      //   title: t('操作'),
      //   align: 'left',
      //   key: 'ACTION',
      //   width: 120,
      //   actions: ({ record }) => [
      //     {
      //       label: t('数据权限'),
      //       onClick: () => {
      //         permissionOpen.value = true;
      //         resourceId.value = record.id;
      //       },
      //     },
      //   ],
      // },
    ],
    [
      {
        title: t('版本号'),
        dataIndex: 'version',
        fixed: 'left',
        width: 190,
        resizable: true,
      },
      {
        title: t('版本描述'),
        dataIndex: 'description',
        width: 190,
        resizable: true,
      },
      {
        title: t('状态'),
        dataIndex: 'versionStatus',
        width: 190,
        resizable: true,
        customRender: ({ record }) => (
          <div class={['status-content', VersionStatusClassMap.get(record.versionStatus.value)]}>
            <div class={['status-icon', VersionStatusClassMap.get(record.versionStatus.value)]}></div>
            <span>{record.versionStatus.label}</span>
          </div>
        ),
      },
      {
        title: t('启停'),
        dataIndex: 'status',
        width: 160,
        resizable: true,
        customRender: ({ record }) => (
          <Switch
            checked={record.status.value}
            loading={versionStateLoading.value}
            disabled={!hasPermission('100020001001007') || record.versionStatus.value === VersionStatus.EDIT}
            onChange={checked => {
              changeVersionState(record, checked as boolean);
            }}
          />
        ),
      },
      {
        title: t('操作'),
        align: 'left',
        key: 'ACTION',
        fixed: 'right',
        width: 200,
        actions: ({ record }, { fetchData }) => [
          {
            label: t('编辑'),
            ifShow: record.versionStatus.value === VersionStatus.EDIT && hasPermission('100020001001003'),
            onClick: () => {
              console.log('editVersion');
              emits('editVersion', record, selectCodeRule);
            },
          },
          {
            label: t('查看'),
            ifShow: hasPermission('100020001001004'),
            onClick: () => {
              emits('viewVersion', record);
            },
          },
          {
            label: t('确认'),
            ifShow: record.versionStatus.value === VersionStatus.EDIT && hasPermission('100020001001005'),
            onClick: () => {
              Modal.confirm({
                title: t('是否完成编号规则配置确认?'),
                icon: h(ExclamationCircleOutlined),
                content: `${t('是否完成编号规则配置确认')}`,
                async onOk() {
                  try {
                    await reqPlatformCodeRuleVersionConfirmPUT(record.id);
                    message.success(t('确认成功'));
                    fetchData();
                    return Promise.resolve();
                  } catch (error: any) {
                    error.message && message.error(error.message);
                    return Promise.reject();
                  }
                },
                onCancel() {},
              });
            },
          },
          {
            label: t('删除'),
            ifShow: record.versionStatus.value === VersionStatus.EDIT && hasPermission('100020001001006'),
            onClick: () => {
              Modal.confirm({
                title: t('是否删除该编号规则版本?'),
                icon: h(ExclamationCircleOutlined),
                content: `${t('是否删除该编号规则版本')}`,
                async onOk() {
                  try {
                    await reqPlatformCodeRuleVersionDELETE(record.id);
                    message.success(t('删除成功'));
                    fetchData();
                    return Promise.resolve();
                  } catch (error: any) {
                    error.message && message.error(error.message);
                    return Promise.reject();
                  }
                },
                onCancel() {},
              });
            },
          },
        ],
      },
    ],
  ];
  const titles = [t('编号规则'), t('版本信息')];

  return {
    columns,
    titles,
    tableInstance,
    selectCodeRule,
    permissionOpen,
    resourceId,
  };
};
