<template>
  <div class="add-flow">
    <div class="container">
      <div class="flow-header">
        <div class="flow-header-title">
          <Breadcrumb class="crumb">
            <BreadcrumbItem class="crumb-allow-click" @click="handleClickReturn">
              {{ t('流程配置') }}
            </BreadcrumbItem>
            <BreadcrumbItem>{{ title }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="flow-header-btn">
          <Space :size="16">
            <Button @click="handleClickReturn">
              {{ t('返回') }}
            </Button>
            <Button :loading="checkoutFlowLoading" :disabled="isView" @click="checkoutFlow">
              {{ t('校验') }}
            </Button>
            <Button type="primary" :disabled="isView" @click="save">{{ t('保存') }}</Button>
            <!-- <Button type="primary" :disabled="!isCheckoutSuccess || isView" @click="handleClickPublish">
              {{ t('发布') }}
            </Button> -->
          </Space>
        </div>
      </div>
      <div class="bottom-container">
        <div class="setting">
          <BMForm ref="setFormRef" v-bind="setFormProps" :disabled="isView"></BMForm>
        </div>
        <div class="flow-content">
          <Flow
            ref="flowInstance"
            :modalJson="modalJson"
            :leftMap="leftMap"
            :isView="isView"
            :showNextIcon="false"
            left-icon="Process2"
            next-icon="ProcessNext"
            :connecting="connecting"
            notLimitStartOrEndTransform
            class="flow"
            @handleClickSet="handleClickSet"
            @flowDataChange="flowDataChange" />
        </div>
      </div>
    </div>
  </div>
  <TaskRightDrawer
    :key="settingNodeId"
    v-model:open="taskRightDrawerOpen"
    :settingNodeId="settingNodeId"
    :settingNodeFormData="settingNodeFormData"
    :isView="isView"
    @updateFormValue="updateFormValue" />

  <StartAndEndRightDrawer
    :key="settingStartAndEndNodeId"
    v-model:open="startAndEndRightDrawerOpen"
    :settingNodeId="settingStartAndEndNodeId"
    :isStartNode="isStartNode"
    :isView="isView"
    :settingNodeFormData="settingStartAndEndNodeFormData"
    @updateFormValue="updateFormValue" />
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import Flow from '@/components/Flow';
  import { FlowLeftToolBar } from '@/components/Flow/type/toolBar';
  import { FlowNodeEnum } from '@/components/Flow/type';
  import { BMIcon, BMForm, Recordable } from '@bmos/components';
  import { useTask } from './hooks/useTask';
  import { Cell } from '@antv/x6';
  import TaskRightDrawer from './components/TaskRightDrawer/index.vue';
  import { useForm } from './hooks/useForm';
  import StartNode from './components/FlowNode/StartNode.vue';
  import EndNode from './components/FlowNode/EndNode.vue';
  import StartAndEndRightDrawer from './components/StartAndEndRightDrawer/index.vue';
  import { useSave } from './hooks/useSave';
  import { FlowNodeType, USER_ROLE_MESSAGE_MARK_TYPE, flow_STATE } from '../enum';
  import { reqDetailFlowAuditReq } from '@/services';

  // 获取 query 参数
  // 获取路由上的 query 参数
  const route = useRoute();
  const { status, versionId, categoryCode } = route.query;
  const modalJson = ref<any>([]);
  const flowDetail = ref<Recordable>({});
  const originalModalJson = ref<any>([]);
  const sourceVersion = ref('');
  const getFlowDetail = async () => {
    try {
      const { data } = await reqDetailFlowAuditReq(versionId as string);
      flowDetail.value = data;
      setFormRef.value?.setFieldsValue({
        ...data,
      });
      sourceVersion.value = data.version;
      originalModalJson.value = JSON.parse(data.flowAuditModel);
      modalJson.value = JSON.parse(data.flowAuditModel).map((item: any) => {
        return {
          ...item.metaInfo,
          data: {
            ...item.metaInfo.data,
            ...(item.type !== FlowNodeType.SEQUENCE_FLOW && {
              formData: {
                name: item.metaInfo.data?.label,
                ...JSON.parse(item?.payload?.settings || '{}'),
                ...(item?.type === FlowNodeType.USER_TASK && {
                  reviewPerson: data?.auditUserList
                    ?.filter(
                      (it: any) =>
                        it?.assigneeType === USER_ROLE_MESSAGE_MARK_TYPE.ALL_USER && it?.nodeId === item?.key,
                    )
                    .map((i: any) => {
                      return {
                        label: i?.assigneeName,
                        value: i?.assignee,
                        id: i?.assignee,
                        name: i?.assigneeName,
                      };
                    }),
                  reviewRole: data?.auditUserList
                    ?.filter(
                      (it: any) =>
                        it?.assigneeType === USER_ROLE_MESSAGE_MARK_TYPE.ALL_ROLE && it?.nodeId === item?.key,
                    )
                    .map((i: any) => i?.assignee),
                  makePerson: data?.auditMegDTOList
                    ?.filter(
                      (it: any) => it?.messageType === USER_ROLE_MESSAGE_MARK_TYPE.MAKE && it?.nodeId === item?.key,
                    )
                    .map((i: any) => {
                      return {
                        label: i?.userName,
                        value: i?.userId,
                        id: i?.userId,
                        name: i?.userName,
                      };
                    }),
                  auditMegDTOList: data?.auditMegDTOList
                    ?.filter(
                      (it: any) => it?.messageType === USER_ROLE_MESSAGE_MARK_TYPE.MESSAGE && it?.nodeId === item?.key,
                    )
                    .map((i: any) => {
                      return {
                        label: i?.userName,
                        value: i?.userId,
                        id: i?.userId,
                        name: i?.userName,
                      };
                    }),
                }),
              },
            }),
          },
        };
      });
    } catch (error) {}
  };

  const watchStatus = ref<flow_STATE>(status as flow_STATE);
  const title = ref<string>(t('新增流程'));
  watch(
    () => watchStatus.value,
    async val => {
      watchStatus.value = val as flow_STATE;
      switch (val) {
        case flow_STATE.editVersion:
          title.value = t('编辑流程');
          break;
        case flow_STATE.viewVersion:
          title.value = t('查看流程');
          break;
        case flow_STATE.addFlow:
          title.value = t('新增流程');
          break;
      }
      await nextTick();
      if (val !== flow_STATE.addFlow) {
        // 获取流程详情
        getFlowDetail();
      } else {
        categoryCode && setFormRef.value?.setFormModel('categoryCode', categoryCode);
      }
    },
    {
      immediate: true,
    },
  );

  const isSaveFlow = ref<boolean>(true);
  const flowDataChange = () => {
    isSaveFlow.value = false;
  };

  const isView = computed(() => {
    return watchStatus.value === flow_STATE.viewVersion;
  });

  // 流程图 左侧 item 配置
  const leftMap: FlowLeftToolBar[] = [
    {
      title: t('工作流'),
      label: t('任务节点'),
      shape: FlowNodeEnum.CUSTOM,
      width: 206,
      height: 44,
      icon() {
        return <BMIcon type={'Process'} />;
      },
    },
  ];
  const flowInstance = ref<any>(null);
  onMounted(async () => {
    try {
      await nextTick();
      flowInstance.value?.register({
        shape: 'custom-vue-start-node',
        width: 120,
        height: 44,
        component: {
          render() {
            return <StartNode onSetting={(cell: Cell) => handleClickStartSet(cell)} />;
          },
        },
      });
      flowInstance.value?.register({
        shape: 'custom-vue-end-node',
        width: 120,
        height: 44,
        component: {
          render() {
            return <EndNode onSetting={(cell: Cell) => handleClickEndSet(cell)} />;
          },
        },
      });
    } catch (error) {}
  });

  const { setFormProps, setFormRef } = useForm({ watchStatus, isSaveFlow });

  const isStartNode = ref<boolean>(false);
  const startAndEndRightDrawerOpen = ref<boolean>(false);
  const settingStartAndEndNodeId = ref<string>('');
  const settingStartAndEndNodeFormData = ref<any>({});
  // 点击开始节点设置
  const handleClickStartSet = (cell: Cell) => {
    startAndEndRightDrawerOpen.value = true;
    settingStartAndEndNodeId.value = cell.id;
    settingStartAndEndNodeFormData.value = cell.data?.formData || {
      name: cell.data.label,
    };
    isStartNode.value = true;
  };
  // 点击结束节点设置
  const handleClickEndSet = (cell: Cell) => {
    startAndEndRightDrawerOpen.value = true;
    settingStartAndEndNodeId.value = cell.id;
    settingStartAndEndNodeFormData.value = cell.data?.formData || {
      name: cell.data.label,
    };
    isStartNode.value = false;
  };

  const updateFormValue = (nodeId: string, formData: Recordable) => {
    flowInstance.value?.updateCellData(nodeId, formData);
  };

  // 任务节点配置
  const { handleClickSet, taskRightDrawerOpen, settingNodeId, settingNodeFormData, connecting } = useTask({});

  // 保存
  const { save, checkoutFlow, checkoutFlowLoading, handleClickReturn } = useSave({
    flowInstance,
    // @ts-ignore
    setFormRef,
    originalModalJson,
    flowDetail,
    watchStatus,
    isSaveFlow,
    sourceVersion,
  });
</script>

<style lang="less" scoped>
  .add-flow {
    height: 100%;
    width: 100%;
  }
  .container {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
  }
  .flow-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--bmos-padding-small);
    width: 100%;
  }

  .bottom-container {
    display: flex;
    width: 100%;
    height: calc(100% - 68px);
    .setting {
      width: 300px;
      height: 100%;
      padding: var(--bmos-padding-small);
      background-color: var(--bmos-primary-color-white);
    }
    .flow-content {
      border-top: 4px solid var(--bmos-primary-color-white);
      border-right: 4px solid var(--bmos-primary-color-white);
      flex: 1;
      overflow: hidden;
    }
  }
</style>
