import { VersionStatusClassMap } from '@/pages/ProductConfig/AuditConfig/utils/common';
import { auditChangeState, reqGetFlowConfigProcessListReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMStateTag, Recordable, type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { VersionStatus, flow_STATE } from '../enum';

export const useTable = () => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();

  const firstRowData = ref<any>({});
  const bindProcessModalOpen = ref<boolean>(false);
  const historyOpen = ref(false);
  const businessId = ref('');

  const checkedProcessIds = ref<string[]>([]);
  const getBindProcessList = async (record: Recordable) => {
    try {
      const { data } = await reqGetFlowConfigProcessListReq({ code: record.code });
      checkedProcessIds.value = data;
      bindProcessModalOpen.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('流程名称'),
      dataIndex: 'name',
      width: 200,
      fixed: 'left',
      resizable: true,
    },
    {
      title: t('流程类型'),
      dataIndex: 'treeName',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('生效版本'),
      dataIndex: 'version',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 150,
      actions: ({ record }) => [
        {
          label: t('绑定工艺'),
          ifShow:
            hasPermission('120040002000006') &&
            (record.categoryCode === '12004000101' || record.categoryCode === '12005000101'),
          onClick: () => {
            firstRowData.value = record;
            getBindProcessList(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  });

  const columnsSecond: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'remark',
      width: 345,
      resizable: true,
    },
    {
      title: t('源版本'),
      dataIndex: 'historyVersion',
      width: 200,
      resizable: true,
    },
    {
      title: t('状态'),
      dataIndex: 'state',
      fixed: 'right',
      width: 200,
      resizable: true,
      customRender: ({ record }) => {
        return <BMStateTag type={VersionStatusClassMap.get(record.state.value)}>{record.state?.label}</BMStateTag>;
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 300,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120020008000004'),
          onClick: () => {
            router.push({
              name: 'audit-config-add-flow',
              query: {
                status: flow_STATE.viewVersion,
                versionId: record.versionId,
              },
            });
          },
        },
        {
          label: t('编辑'),
          ifShow: record.state.value == VersionStatus.EDITING && hasPermission('120020008000002'),
          onClick: () => {
            router.push({
              name: 'audit-config-add-flow',
              query: {
                status: flow_STATE.editVersion,
                versionId: record.versionId,
              },
            });
          },
        },
        {
          label: t('启用'),
          ifShow: record.state.value != VersionStatus.USING && hasPermission('120020008000006'),
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              icon: h(ExclamationCircleOutlined),
              content: t('是否启用该流程版本？'),
              async onOk() {
                try {
                  await auditChangeState({ id: record.versionId, enable: true });
                  await fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
              onCancel() {},
            });
          },
        },
        {
          label: t('停用'),
          ifShow: record.state.value == VersionStatus.USING && hasPermission('120020008000007'),
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              icon: h(ExclamationCircleOutlined),
              content: t('是否停用该流程版本？'),
              async onOk() {
                try {
                  await auditChangeState({ id: record.versionId, enable: false });
                  await fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
              onCancel() {},
            });
          },
        },
        {
          label: t('历史'),
          ifShow: hasPermission('120020008000008'),
          onClick: () => {
            businessId.value = record.versionId;
            historyOpen.value = true;
          },
        },
      ],
    },
  ];

  const addFlow = (currentNode: any) => {
    router.push({
      name: 'audit-config-add-flow',
      query: {
        status: flow_STATE.addFlow,
        ...(currentNode.code && currentNode.isLeaf && { categoryCode: currentNode.code }),
      },
    });
  };

  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    addFlow,
    historyOpen,
    businessId,
    firstRowData,
    bindProcessModalOpen,
    checkedProcessIds,
  };
};
