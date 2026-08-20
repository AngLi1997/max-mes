<template>
  <Spin :spinning>
    <div class="procedure-flow-container">
      <Row class="flow-header">
        <Col :span="8">
          <slot name="breadcrumb">
            <Breadcrumb class="crumb">
              <breadcrumb-item class="crumb-allow-click" @click="toProcessConfig">
                {{ t('工艺配置') }}
              </breadcrumb-item>
              <breadcrumb-item class="crumb-allow-click" @click="noSaveBack">
                {{ t('工艺流程') }}
              </breadcrumb-item>
              <breadcrumb-item>{{ t('工序流程') }}</breadcrumb-item>
            </Breadcrumb>
          </slot>
        </Col>
        <Col :span="8" :offset="8" class="action">
          <Space :size="16">
            <slot name="btn">
              <Button @click="back">{{ t('返回') }}</Button>
              <Button v-if="!isView" type="primary" @click="save">
                {{ t('保存') }}
              </Button>
            </slot>
          </Space>
        </Col>
      </Row>
      <div class="procedure-name">{{ t('工序名称') }}：{{ procedureName }}</div>
      <div class="setting">
        <div class="task-list">
          <div class="title">
            <span>{{ t('任务库') }}</span>
            <div v-if="!isView && !isViewFlow" class="create-task" @click="createTask">
              <BMIcons icon="Add" />
              <span>{{ t('创建任务') }}</span>
            </div>
          </div>
          <div class="list">
            <TaskList
              :isView="isView || isViewFlow"
              :taskList="taskList"
              @deleteTask="deleteTask"
              @handleClickNext="handleClickFile"
              @handleClickSet="(item: any) => handleClickSet(item, NodeType.TaskNode)" />
          </div>
        </div>
        <Flow
          ref="flowInstance"
          :modalJson="modalJson"
          :isView="isView || isViewFlow"
          v-bind="isViewFlowToolBarAttr"
          :leftMap="leftMap"
          nextIcon="File"
          leftIcon="Procedure2"
          taskLeftIcon="ProcedureTask2"
          class="flow"
          @handleClickSet="(item:any) => handleClickSet(item, NodeType.StepNode)"
          @handleClickNext="handleClickFile"
          @nodeClick="handleClickNode"
          @flowDataChange="flowDataChange" />
      </div>
    </div>
  </Spin>
  <RightDrawer
    v-model:open="openDrawer"
    :settingNodeId="settingNodeId"
    :settingNodeFormData="settingNodeFormData"
    :batchRecordItems="batchRecordItems"
    :detailProceduresSteps="detailProceduresSteps"
    :isView="isView || isViewFlow"
    :flowDataForDrawer="flowDataForDrawer"
    :procedureId="realProcedureId"
    :procedureIdOther="(procedureIdOther as string)"
    :processDetail="processDetail"
    :versionId="realVersionId"
    :currentNodeType="currentNodeType"
    @updateFormValue="updateFormValue" />
  <GatewayConfigDrawer
    v-model:open="gatewayOpen"
    :settingNodeId="settingNodeId"
    :gatewaySelectNodes="gatewaySelectNodes"
    :settingNodeFormData="settingNodeFormData"
    :isView="isView || isViewFlow"
    @updateCellDataValue="updateCellDataValue" />
</template>

<script setup lang="tsx">
  import { computed, createVNode, ref, watch } from 'vue';
  import { Row, Col, Breadcrumb, BreadcrumbItem, Space, Button, message, Modal } from 'ant-design-vue';
  import Flow from '@/components/Flow';
  import { Cell } from '@antv/x6';
  import { useRoute, useRouter } from 'vue-router';
  import { FlowNodeType, PROCESS_STATE } from '../enum';
  import RightDrawer from './components/RightDrawer.vue';
  import { BMIcon, Recordable } from '@bmos/components';
  import { FlowInstanceType, FlowNodeEnum } from '@/components/Flow/type';
  import { getGatewaySelectNodes, processFlowData } from '../utils';
  import { getProcedureSteps } from './utils';
  import {
    reqGetDetailUsingGET,
    reqGetProcessModelUsingGET,
    reqProcedureDetailModify,
    reqProcedureDetailSave,
    reqProcedureStepListReq,
  } from '@/services';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import type { BatchRecordItems, NodeTypeValue } from './types';
  import { NodeType } from './types';
  import { t } from '@bmos/i18n';
  import { FlowLeftToolBar } from '@/components/Flow/type/toolBar';
  import GatewayConfigDrawer from './components/GatewayConfigDrawer.vue';
  import { deepMerge, getUUID, isEmpty } from '@bmos/utils';
  import { BMIcons } from '@bmos/icons';
  import TaskList from './components/TaskList.vue';
  import BasicNode from './components/BasicNode.vue';

  // 获取路由上的 query 参数
  const route = useRoute();
  const router = useRouter();
  const { status, version, processId, procedureId, versionId, procedureIdOther } = route.query;

  const props = defineProps({
    pageParams: {
      type: Object as PropType<Recordable>,
      default: () => {},
    },
  });

  const emit = defineEmits<{
    (e: 'viewNext', params: any): void;
  }>();

  const spinning = ref<boolean>(false);

  const flowInstance = ref<FlowInstanceType>();
  const isSaveProcedure = ref<boolean>(true);
  const flowDataChange = () => {
    isSaveProcedure.value = false;
  };

  const realVersion = computed(() => props.pageParams?.version || version);
  const realVersionId = computed(() => props.pageParams?.versionId || versionId);
  const realProcessId = computed(() => props.pageParams?.processId || processId);
  const realProcedureId = computed(() => props.pageParams?.procedureId || procedureId);

  const isView = computed(() => status?.toString() === PROCESS_STATE.VIEW_VERSION);

  // 流程图 左侧 item 配置
  const leftMap: FlowLeftToolBar[] = [
    {
      title: t('步骤节点'),
      label: t('步骤节点'),
      shape: FlowNodeEnum.CUSTOM,
      width: 210,
      height: 44,
      icon() {
        return <BMIcon type={'Procedure'} />;
      },
    },
    {
      title: t('控制器'),
      label: t('控制器'),
      width: 28,
      height: 28,
      shape: FlowNodeEnum.GATEWAY,
      icon() {
        return <BMIcon type='Gateway' />;
      },
    },
  ];

  const saveLoading = ref<boolean>(false);
  const noSaveBack = () => {
    cancelModal();
    router.push({
      name: 'process-flow',
      query: {
        status: status === PROCESS_STATE.VIEW_VERSION ? PROCESS_STATE.VIEW_VERSION : PROCESS_STATE.EDIT_VERSION,
        version,
        versionId,
        processId,
      },
    });
  };

  const saveFun = async () => {
    try {
      saveLoading.value = true;
      spinning.value = true;
      const flowData = flowInstance.value?.getFlowData() as {
        cells: Cell.Properties[];
      };
      const processFlow = processFlowData(flowData, originalModalJson.value, true);
      // 校验至少配置一个工序节点
      if (flowData.cells.find(item => item.shape === 'custom-vue-node') === undefined) {
        message.error(t('请至少配置一个工序步骤节点'));
        return Promise.reject();
      }

      // 校验每个工序节点都有流入和流出的连线
      // const findOutIn = processFlow.find(
      //   (item: any) =>
      //     item.type === FlowNodeType.CALL_ACTIVITY_TASK &&
      //     (item.outgoing.length === 0 || item.incoming.length === 0),
      // );
      // if (findOutIn) {
      //   message.error(t('请为每个工序步骤节点配置流入和流出的连线'));
      //   return Promise.reject();
      // }
      const params = {
        processModel: JSON.stringify(processFlow),
        procedureId,
        procedureSteps: getProcedureSteps(flowData),
        procedureTasks: taskList.value,
      } as any;
      if (procedureModelId.value.toString().length > 0) {
        await reqProcedureDetailModify(params);
      } else {
        const { data } = await reqProcedureDetailSave(params);
        if (data) {
          procedureModelId.value = data;
        }
      }
      saveLoading.value = false;
      message.success(t('保存成功'));
      isSaveProcedure.value = true;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      saveLoading.value = false;
      return Promise.reject(error);
    } finally {
      saveLoading.value = false;
      spinning.value = false;
      Modal.destroyAll();
    }
  };

  const saveFunAndBack = async () => {
    Modal.destroyAll();
    try {
      await saveFun();
      noSaveBack();
    } catch (error) {}
  };

  const back = () => {
    if (isView.value || isSaveProcedure.value) {
      noSaveBack();
    } else {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对工艺的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button onClick={() => noSaveBack()}>{t('不保存')}</Button>
                {!isView.value && (
                  <Button type='primary' loading={saveLoading.value} onClick={() => saveFunAndBack()}>
                    {t('保存')}
                  </Button>
                )}
              </Space>
            </>
          );
        },
      });
    }
  };

  // 保存
  const save = async () => {
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否确认保存工艺'),
      async onOk() {
        await saveFun();
        await getDetail(realProcessId.value as string, realVersion.value as string);
        await getInfo(procedureModelId.value);
        // noSaveBack();
        return Promise.resolve();
      },
    });
  };

  const modalJson = ref<any>([]);

  const flowDataForDrawer = ref<Recordable>({});
  // 当前操作的节点类型
  const currentNodeType = ref<NodeTypeValue>(NodeType.StepNode);
  const handleClickSet = (cell: any, type: NodeTypeValue = NodeType.StepNode) => {
    currentNodeType.value = type;
    if (type === NodeType.TaskNode) {
      settingNodeId.value = cell.nodeId;
      settingNodeFormData.value = cell;
    } else {
      settingNodeId.value = cell.id;
      settingNodeFormData.value = cell.data?.formData || {
        label: cell.data.label,
      };
    }
    const flowData = flowInstance.value?.getFlowData() as {
      cells: Cell.Properties[];
    };
    flowDataForDrawer.value = processFlowData(flowData, [], true)?.filter((item: any) => {
      return item.type === FlowNodeType.USER_TASK && item.key !== cell.id;
    });
    const taskItems = taskList.value?.filter((item: any) => item.nodeId !== cell.nodeId);
    if (taskItems && taskItems.length > 0) {
      flowDataForDrawer.value = flowDataForDrawer.value.concat(taskItems);
    }
    openDrawer.value = true;
  };

  const cancelModal = () => {
    Modal.destroyAll();
  };
  // 下一步
  const saveToProcedureStep = async (cell: any) => {
    try {
      await saveFun();
      toProcedureStep(cell);
    } catch (error) {}
  };
  const toProcedureStep = async (cell: any) => {
    try {
      const params = {
        procedureId: realProcedureId.value,
        recordVersionIds: batchRecordItems.value?.map((item: any) => item.batchRecordVersionId)?.join(','),
      };
      const { data } = await reqProcedureStepListReq(params as unknown as API.ProcedureStepListReq);
      const clickCell: any =
        data.stepList?.find((item: any) => item.nodeId === cell.id) ||
        data.taskList?.find((item: any) => item.nodeId === cell.nodeId);
      cancelModal();
      if (isEmpty(clickCell?.recordItemId) || isEmpty(clickCell?.recordVersionId)) {
        message.error(t('请先配置记录项'));
        return;
      }
      router.push({
        name: 'procedure-step-config',
        query: {
          status,
          version,
          versionId,
          processId,
          procedureId,
          procedureIdOther,
          nodeId: clickCell.nodeId,
          procedureStepId: clickCell.procedureStepId,
          procedureStepModelId: clickCell.id,
          recordItemId: clickCell.recordItemId,
          recordVersionId: clickCell.recordVersionId,
          reusable: clickCell.reusable,
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const handleClickFile = async (cell: any) => {
    if (props.pageParams?.processId) {
      const params = {
        procedureId: realProcedureId.value,
        recordVersionIds: batchRecordItems.value?.map((item: any) => item.batchRecordVersionId)?.join(','),
      };
      const { data } = await reqProcedureStepListReq(params as unknown as API.ProcedureStepListReq);
      const clickCell: any =
        data.stepList?.find((item: any) => item.nodeId === cell.id) ||
        data.taskList?.find((item: any) => item.nodeId === cell.nodeId);
      if (isEmpty(clickCell.recordItemId) || isEmpty(clickCell.recordVersionId)) {
        message.error(t('请先配置记录项'));
        return;
      }
      emit('viewNext', {
        procedureStepId: clickCell.procedureStepId,
        recordItemId: clickCell.recordItemId,
        recordVersionId: clickCell.recordVersionId,
        reusable: clickCell.reusable,
        processId: realProcessId.value,
        version: realVersion.value,
        procedureId: realProcedureId.value,
        procedureStepModelId: clickCell.id,
      });
      return;
    }
    if (isView.value || isSaveProcedure.value) {
      toProcedureStep(cell);
    } else {
      saveToProcedureStep(cell);
    }
  };

  const toProcessConfig = () => {
    router.push({
      name: 'process-config',
    });
  };

  // 右侧抽屉
  const openDrawer = ref<boolean>(false);
  const settingNodeId = ref<string>('');
  const settingNodeFormData = ref<Recordable>({});
  const updateFormValue = (id: string, data: Recordable) => {
    try {
      isSaveProcedure.value = false;
      if (currentNodeType.value === NodeType.TaskNode) {
        const taskItem = taskList.value.find((item: any) => item.nodeId === id);
        taskList.value = taskList.value.map((item: any) => {
          if (item.nodeId === id) {
            const obj = {
              ...deepMerge(taskItem, data),
              historicalName: data.label,
            };
            settingNodeFormData.value = obj;
            return obj;
          }
          return item;
        });
      } else {
        flowInstance.value?.updateCellData(id, data);
        const cellData = flowInstance.value?.getCellDataById(id);
        settingNodeFormData.value = deepMerge(isEmpty(cellData['formData']) ? {} : cellData['formData'], data);
      }
    } catch (error) {}
  };

  const procedureName = ref<string>('');
  const procedureModelId = ref<string>('');
  const batchRecordItems = ref<BatchRecordItems[]>([]);
  const processDetail = ref<Recordable>({});

  const getDetail = async (processId: string, version: string) => {
    try {
      spinning.value = true;
      const { data } = await reqGetDetailUsingGET({
        processId,
        version,
      } as unknown as API.MesProcessDetailReq);
      const curProcedure: any = data.procedures.find((item: any) => item.id === realProcedureId.value);
      procedureName.value = curProcedure.name;
      procedureModelId.value = curProcedure.processModelId || '';
      batchRecordItems.value = data.batchRecordItems || [];
      processDetail.value = data;
      await getInfo(procedureModelId.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      spinning.value = false;
    }
  };
  // 监听 processId 变化，如果有值则请求工序详情
  watch(
    () => realProcessId.value,
    async val => {
      if (val) {
        getDetail(val as string, realVersion.value as string);
      }
    },
    {
      immediate: true,
    },
  );

  // 监听 processModelId 变化，如果有值则请求工序详情
  const detailProceduresSteps = ref<any>([]);
  const originalModalJson = ref<any>([]);
  // 任务List
  const taskList = ref<any>([]);
  const getInfo = async (val: string) => {
    try {
      const { data } = await reqGetProcessModelUsingGET({
        processModelId: val,
      });
      const params = {
        procedureId: realProcedureId.value,
        recordVersionIds: batchRecordItems.value?.map((item: any) => item.batchRecordVersionId)?.join(','),
      };
      const stepRes = await reqProcedureStepListReq(params as unknown as API.ProcedureStepListReq);
      detailProceduresSteps.value = stepRes.data?.stepList;
      originalModalJson.value = JSON.parse(data);
      modalJson.value = JSON.parse(data).map((item: any) => {
        const metaInfo = JSON.parse(item.metaInfo);
        const formData = stepRes.data?.stepList?.find((it: any) => it.nodeId === metaInfo.id);
        return {
          ...metaInfo,
          data: {
            ...metaInfo.data,
            formData: {
              ...formData,
              ...(formData?.nodeFunction && {
                nodeFunction: formData.nodeFunction?.value,
              }),
            },
            ...(item.type.includes('gateway') && {
              gatewayType: item.type,
              conditionOnNodes: item.conditionOnNodes,
            }),
          },
        };
      });
      taskList.value = stepRes.data?.taskList?.map((item: any) => {
        return {
          ...item,
          nodeFunction: item.nodeFunction?.value,
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      spinning.value = false;
    }
  };

  // 创建任务
  const createTask = () => {
    try {
      isSaveProcedure.value = false;
      if (!taskList.value) {
        taskList.value = [];
      }
      taskList.value.push({
        nodeId: getUUID(),
        name: t('任务节点'),
      });
    } catch (error) {
      //
    }
  };
  // 删除任务 item
  const deleteTask = (item: Recordable) => {
    try {
      isSaveProcedure.value = false;
      taskList.value = taskList.value.filter((it: Recordable) => it.nodeId !== item.nodeId);
    } catch (error) {
      //
    }
  };

  // 网关配置
  // 网关抽屉是否打开
  const gatewayOpen = ref<boolean>(false);
  // 网关配置可选择节点
  const gatewaySelectNodes = ref<Recordable[]>([]);
  const handleClickNode = (cell: Cell) => {
    if (cell.shape === FlowNodeEnum.GATEWAY) {
      settingNodeId.value = cell.id;
      settingNodeFormData.value = {
        ...cell.data,
        gatewayType: cell.data.gatewayType || undefined,
      };
      const flowData = flowInstance.value?.getFlowData() as {
        cells: Cell.Properties[];
      };
      gatewaySelectNodes.value = getGatewaySelectNodes(flowData.cells, cell.id);
      gatewayOpen.value = true;
    }
  };
  const updateCellDataValue = (id: string, data: Recordable) => {
    flowInstance.value?.updateCellDataValue(id, data);
  };

  const isViewFlow = ref<boolean>(false);
  const isViewFlowToolBarAttr = computed(() => {
    return isViewFlow.value || isView.value
      ? {
          showUndo: false,
          isView: true,
          isTransform: false,
          showRedo: false,
          showDelete: false,
          isShowLeftToolBar: false,
        }
      : {};
  });

  onMounted(async () => {
    if (props.pageParams?.processId) {
      isViewFlow.value = true;
    }
    await nextTick();
    flowInstance.value?.register({
      shape: 'custom-vue-node',
      width: 214,
      height: 52,
      component: {
        render() {
          return (
            <BasicNode
              onSetting={(cell: Cell) => handleClickSet(cell)}
              onClickNext={(cell: Cell) => handleClickFile(cell)}
            />
          );
        },
      },
    });
  });
</script>
<style scoped lang="less">
  .procedure-flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    .flow-header {
      padding: 0 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
        .crumb-allow-click {
          cursor: pointer;
        }
      }
    }
  }
  .action {
    text-align: right;
  }
  .procedure-name {
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
    border-bottom: 4px solid var(--bmos-background-color);
  }
  .setting {
    width: 100%;
    height: calc(100% - 52px - 56px);
    background-color: var(--bmos-primary-color-white);
    padding: 5px 5px 0 5px;
    display: flex;
    .task-list {
      width: 300px;
      display: flex;
      flex-direction: column;
      .title {
        display: flex;
        justify-content: space-between;
        padding: var(--bmos-padding-mini) var(--bmos-padding-small);
        background-color: var(--bmos-primary-color-white);
        border-bottom: 1px solid var(--bmos-first-level-border-color);
        span {
          font-size: 16px;
          line-height: 24px;
        }
        .create-task {
          display: flex;
          align-items: center;
          cursor: pointer;
          font-size: 14px;
          span {
            color: var(--bmos-primary-color);
            font-size: 14px;
          }
          svg {
            margin-right: 5px;
            color: var(--bmos-primary-color);
          }
        }
      }
      .list {
        flex: 1;
        overflow-x: hidden;
        overflow-y: auto;
        padding: var(--bmos-padding-mini) var(--bmos-padding-small);
      }
    }
    .flow {
      flex: 1;
      width: calc(100% - 300px);
    }
  }
</style>
