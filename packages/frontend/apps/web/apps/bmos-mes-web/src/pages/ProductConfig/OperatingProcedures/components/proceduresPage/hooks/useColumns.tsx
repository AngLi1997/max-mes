import StateTag from '@/components/StateTag/index.vue';
import {
  getOperateRuleVersionUpdateEffect,
  postOperateRuleVersionStartFlow,
  postOperateRuleVersionUpdateState,
  putRuleVersionUpdateValid,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined, InfoCircleOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { Modal, Tag, message } from 'ant-design-vue';
import dayjs from 'dayjs';
import { createVNode } from 'vue';
import { VersionStatus, circularStatus, modalStatus } from '../../../enum';
export const useColumns = ({ UseParams, emits }: any) => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const { pageProcedures, flowName, historyOpen, isVersionId, flowStatus, permissionModalOpen, firstRowData } =
    UseParams;
  //规程
  const rulesColumns: TableColumn[] = [
    {
      title: t('文件名称'),
      dataIndex: 'name',
      width: 190,
      resizable: true,
    },
    {
      title: t('文件编号'),
      dataIndex: 'code',
      width: 190,
      resizable: true,
      sorter: true,
    },
    {
      title: t('生效版本'),
      dataIndex: 'version',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('数据权限'),
          ifShow: hasPermission('120020011000014'),
          onClick: () => {
            permissionModalOpen.value = true;
            firstRowData.value = { ...record };
          },
        },
      ],
    },
  ];
  //版本
  const verColumns: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'remark',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('状态'),
      dataIndex: 'state',
      hideInSearch: true,
      resizable: true,
      width: 100,
      customRender: ({ record }) => (
        <StateTag type={circularStatus[record.stateName.value]}>{record.stateName.name}</StateTag>
      ),
    },
    {
      title: t('版本生效日期'),
      dataIndex: 'effectDate',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('文件生效日期'),
      dataIndex: 'fileEffectDate',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('文件上传日期'),
      dataIndex: 'uploadDate',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }, { fetchData }) => {
        let buttons: any[] = [
          {
            label: t('查看'),
            ifShow: hasPermission('120020011000007'),
            onClick: () => {
              emits('update:state', modalStatus.View);
              emits('cutProceduresDetails', record);
            },
          },
          {
            label: t('历史'),
            ifShow: hasPermission('120020011000011'),
            onClick: () => {
              isVersionId.value = { ...record };
              historyOpen.value = true;
            },
          },
        ];
        const buttonsList = [
          {
            label: t('编辑'),
            ifShow: hasPermission('120020011000006'),
            onClick: () => {
              emits('update:state', modalStatus.Edit);
              emits('cutProceduresDetails', record);
            },
          },
          {
            label: t('确认'),
            ifShow: hasPermission('120020011000008'),
            onClick: () => {
              Modal.confirm({
                title: t('确认'),
                icon: createVNode(ExclamationCircleOutlined),
                content: t('确认后文件无法编辑，是否确认?'),
                onOk: async () => {
                  const param = {
                    id: record.id,
                    state: VersionStatus.confirm,
                    stateName: t('确认文件版本'),
                  };
                  await postOperateRuleVersionUpdateState(param);
                  fetchData();
                },
              });
            },
          },
          {
            label: t('启用'),
            ifShow: hasPermission('120020011000009'),
            onClick: () => {
              Modal.confirm({
                title: t('启用'),
                icon: createVNode(ExclamationCircleOutlined),
                content: t('是否发起启用审核?'),
                onOk: async () => {
                  flowName.value = t('是否发起启用审核?');
                  isEffectDate.value = false;
                  flowStatus.value = true;
                  isVersionId.value = { ...record, isStartStop: true };
                },
              });
            },
          },
          {
            label: t('审核进度'),
            ifShow: hasPermission('120020011000013'),
            onClick: () => {
              router.push({
                name: 'operating-procedures-schedule',
                query: {
                  processInstanceId: record.instanceId,
                  fromList: 'fromList',
                  title: t('操作规程'),
                },
              });
            },
          },
          {
            label: t('停用'),
            ifShow: hasPermission('120020011000010'),
            onClick: () => {
              Modal.confirm({
                title: t('停用'),
                icon: createVNode(ExclamationCircleOutlined),
                content: t('是否停用该文件版本？'),
                onOk: async () => {
                  const param = {
                    id: record.id,
                    state: VersionStatus.invalid,
                    stateName: t('停用文件版本'),
                  };
                  await postOperateRuleVersionUpdateState(param);
                  fetchData();
                  // flowName.value = t('是否发起停用审核?');
                  // flowStatus.value = true;
                  // isVersionId.value = { ...record, isStartStop: false };
                },
              });
            },
          },
          {
            label: t('立即生效'),
            ifShow: hasPermission('120020011000012'),
            onClick: async () => {
              Modal.confirm({
                title: t('提示'),
                icon: createVNode(ExclamationCircleOutlined),
                content: t('是否立即生效当前文件版本'),
                onOk: async () => {
                  const param = {
                    id: record.id,
                  };
                  await getOperateRuleVersionUpdateEffect(param);
                  fetchData();
                },
              });
            },
          },
          {
            label: t('直接生效'),
            ifShow: hasPermission('120020011000015'),
            onClick: async () => {
              Modal.confirm({
                title: t('提示'),
                icon: createVNode(ExclamationCircleOutlined),
                content: t('是否直接生效当前文件版本'),
                onOk: async () => {
                  try {
                    await putRuleVersionUpdateValid(record.id);
                    fetchData();
                  } catch (error: any) {
                    error.message && message.error(error.message);
                  }
                },
              });
            },
          },
        ];
        switch (record.state) {
          case VersionStatus.edit:
            buttons.push(buttonsList[0]);
            buttons.push(buttonsList[1]);
            buttons.push(buttonsList[6]);
            break;
          case VersionStatus.confirm:
            buttons.push(buttonsList[2]);
            buttons.push(buttonsList[6]);
            break;
          case VersionStatus.audit:
            buttons.push(buttonsList[3]);
            break;
          case VersionStatus.wait_valid:
            buttons.push(buttonsList[4]);
            buttons.push(buttonsList[5]);
            break;
          case VersionStatus.valid:
            buttons.push(buttonsList[4]);
            break;
          case VersionStatus.invalid:
            buttons.push(buttonsList[2]);
            break;
        }
        return buttons;
      },
    },
  ];
  const isEffectDate = ref<boolean>(false);
  const ofRadioGroupText = ref<string>(t('审核通过后，立即生效'));
  //启用
  const flowProps = computed(() => {
    return {
      initialValues: {},
      schemas: [
        {
          field: 'fieId1',
          component: 'RadioGroup',
          label: t('生效类型'),
          required: true,
          defaultValue: '1',
          noFormItemMarginBottom: true,
          componentProps: {
            options: [
              {
                label: t('立即生效'),
                value: '1',
              },
              {
                label: t('生效日期'),
                value: '2',
              },
            ],
            onChange: (e: any) => {
              if (e.target?.value === '1') {
                ofRadioGroupText.value = t('审核通过后，立即生效');
                isEffectDate.value = false;
                return false;
              }
              if (e.target?.value === '2') {
                ofRadioGroupText.value = t('审核通过后，在指定生效日期生效');
                isEffectDate.value = true;
                return false;
              }
            },
          },
        },
        {
          field: 'field2',
          noLabel: true,
          component: () => {
            return (
              <Tag color={'#F2F3F4'} icon={<InfoCircleOutlined />} style={'color:#909398'}>
                {ofRadioGroupText.value}
              </Tag>
            );
          },
          colProps: {
            span: 24,
          },
        },
        {
          field: 'effectDate',
          component: 'DatePicker',
          label: t('生效日期'),
          required: true,
          vIf: () => {
            return isEffectDate.value === true;
          },
          componentProps: {
            format: 'YYYY-MM-DD',
          },
        },
      ],
    };
  });
  //新增文件
  const addFiles = () => {
    emits('update:state', modalStatus.Add);
    emits('cutProceduresDetails');
  };

  //新增版本
  const addVersion = async (currentNode: any) => {
    if (currentNode?.[1]?.version) {
      emits('update:state', modalStatus.Copy);
      emits('cutProceduresDetails', currentNode?.[1]);
    } else {
      message.error(t('请选择版本信息'));
    }
  };
  //数据绑定
  const savePermission = () => {
    pageProcedures.value.fetchData();
  };
  //启停弹出框
  const flowSubmit = async (data: any): Promise<any> => {
    const date = data.fieId1 === '1' ? void 0 : dayjs(data.effectDate).format('YYYY-MM-DD');
    try {
      const flowParams = {
        auditType: isVersionId.value.isStartStop,
        versionId: isVersionId.value.id,
        effectDate: date,
      };
      await postOperateRuleVersionStartFlow(flowParams);
      sendMessage(MessageType.UpdateMessageCount);
      pageProcedures.value.fetchData(1);
      flowStatus.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  return {
    flowProps,
    rulesColumns,
    verColumns,
    addFiles,
    addVersion,
    savePermission,
    flowSubmit,
  };
};
