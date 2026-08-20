<template>
  <div class="procedure-step-config-container">
    <Row class="step-config-header">
      <Col :span="8">
        <slot name="breadcrumb">
          <Breadcrumb class="crumb">
            <breadcrumb-item class="crumb-allow-click" @click="toProcessConfig">
              {{ t('工艺配置') }}
            </breadcrumb-item>
            <breadcrumb-item class="crumb-allow-click" @click="toProcessFlow">
              {{ t('工艺流程') }}
            </breadcrumb-item>
            <breadcrumb-item class="crumb-allow-click" @click="toProcedureFlow">
              {{ t('工序流程') }}
            </breadcrumb-item>
            <breadcrumb-item>{{ t('批记录配置') }}</breadcrumb-item>
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
    <div class="container">
      <div class="left">
        <div class="title">
          <div>{{ t('实例') }}</div>
          <Tooltip>
            <template #title>
              <span>{{ openFlag ? t('全部展开') : t('全部收起') }}</span>
            </template>
            <img class="retract" :src="openFlag ? open : retract" alt="" @click="otherIconClick()" />
          </Tooltip>
        </div>
        <div id="process_content" class="content">
          <NodeList
            ref="nodeListRef"
            :active-keys="nodeActiveKeys"
            :nodeList="nodeList"
            lookup
            @node-click="nodeClick" />
        </div>
      </div>
      <div class="center">
        <div class="title">
          <span>{{ t('功能配置') }}</span>
          <Button
            v-if="showFormatPainter"
            :class="[formatPainterModel ? 'is-format-painter-model' : '']"
            type="link"
            @click="handleClickFormatPainter">
            {{ t('批量模式') }}
          </Button>
        </div>
        <div class="content">
          <ConfigForm
            ref="configFormRef"
            :key="configFormKey"
            :activeComponentType="activeComponentType"
            :procedureId="curProcedureId"
            :initFormValue="initFormValue"
            :isView="isViewApproval"
            :versionId="curVersionId"
            :configList="configList"
            :activeNodeData="activeNodeData"
            :nodeList="nodeList"
            @submit="submitForm"
            @cancel="cancelForm" />
        </div>
      </div>
      <div class="right">
        <Record
          ref="recordRef"
          formulaId="process-config-record-id"
          style="flex: 1"
          :active-keys="templateActiveKeys"
          @node-click="templateNodeClick"></Record>
      </div>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { computed, createVNode, ref } from 'vue';
  import { Space, Button, Modal, message, Tooltip } from 'ant-design-vue';
  import { useRoute, useRouter } from 'vue-router';
  import { PROCESS_STATE } from '../enum';
  import NodeList, { NODE_TYPE } from '@/components/Record/NodeList';
  import { Record } from '@/components/Record/Record';
  import { useComponents } from './hooks';
  import ConfigForm from './components/ConfigForm.vue';
  import { Recordable } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { reqStepConfigSaveReq } from '@/services';
  import { t } from '@bmos/i18n';
  import { findItemByAttr, isEmpty } from '@bmos/utils';
  import retract from '@/assets/images/retract.png';
  import open from '@/assets/images/open.png';

  const props = defineProps({
    pageParams: {
      type: Object as PropType<Recordable>,
      default: () => {},
    },
  });
  // 获取路由上的 query 参数
  const route = useRoute();
  const router = useRouter();
  const {
    status,
    version,
    processId,
    procedureId,
    procedureIdOther,
    procedureStepId,
    procedureStepModelId,
    recordItemId,
    recordVersionId,
    nodeId,
    versionId,
    reusable,
  } = route.query;

  const openFlag = ref(true);
  const nodeListRef = ref();

  const curProcedureId = computed(() => {
    return procedureId ? procedureId : props.pageParams.procedureId;
  });

  const curVersionId = computed(() => {
    return versionId ? versionId : props.pageParams.versionId;
  });

  const isSaveProcedureStepConfig = ref<boolean>(true);
  const isView = computed(() => {
    return status === PROCESS_STATE.VIEW_VERSION || props.pageParams?.processId;
  });
  const isViewApproval = ref<boolean>(false);
  onMounted(() => {
    if (props.pageParams?.processId) {
      isViewApproval.value = true;
    }
  });
  // 实例展开或收起
  const otherIconClick = () => {
    openFlag.value = !openFlag.value;
    if (openFlag.value) {
      nodeListRef.value.retractAll();
    } else {
      nodeListRef.value.openAll();
    }
  };

  const components = useComponents({
    recordItemId: (recordItemId || props.pageParams.recordItemId) as string,
    recordVersionId: (recordVersionId || props.pageParams.recordVersionId) as string,
    procedureStepId: (procedureStepId || props.pageParams.procedureStepId) as string,
    procedureStepModelId: (procedureStepModelId || props.pageParams.procedureStepModelId) as string,
    version: (version || props.pageParams.version) as string,
    processId: (processId || props.pageParams.processId) as string,
    reusable: (reusable || props.pageParams.reusable) as string,
    isView,
    nodeListRef,
  });

  const {
    configFormRef,
    nodeList,
    recordRef,
    templateActiveKeys,
    nodeActiveKeys,
    activeComponentType,
    initFormValue,
    configList,
    activeNodeData,
    configFormKey,
    nodeClick,
    templateNodeClick,
    cancelForm,
    showFormatPainter,
    handleClickFormatPainter,
    formatPainterModel,
    setRecordClassByField,
    removeRecordClassByField,
  } = components;

  const submitForm = (formValues: Recordable) => {
    try {
      if (isEmpty(formValues)) {
        formatPainterModel.value = false;
      }
      const nodeActiveKey = nodeActiveKeys.value[0];
      const item = configList.value.findIndex(item => item.fieldId === nodeActiveKey);
      const curNode = findItemByAttr(nodeList.value, 'fieldId', nodeActiveKey);
      if (item > -1) {
        configList.value[item] = {
          fieldId: nodeActiveKey,
          componentId: curNode?.id,
          configInfo: JSON.stringify(formValues),
        };
      } else {
        configList.value.push({
          componentId: curNode?.id,
          fieldId: nodeActiveKey,
          configInfo: JSON.stringify(formValues),
        });
      }
      if (Object.values(NODE_TYPE).includes(curNode?.componentType) && !isEmpty(formValues)) {
        setRecordClassByField(nodeActiveKey);
      } else if (Object.values(NODE_TYPE).includes(curNode?.componentType) && isEmpty(formValues)) {
        removeRecordClassByField(nodeActiveKey);
      }
      isSaveProcedureStepConfig.value = false;
    } catch (error) {}
  };

  const cancelModal = () => {
    Modal.destroyAll();
  };

  const saveFunc = async () => {
    try {
      const params = {
        components: configList.value,
        nodeId,
        procedureStepId,
        procedureStepModelId,
        processId,
        recordItemId,
        version,
        reusable: reusable?.toString() === 'true' ? true : false,
      };
      await reqStepConfigSaveReq(params as unknown as API.StepConfigSaveReq);
      isSaveProcedureStepConfig.value = true;
      message.success(t('保存成功'));
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
  const toProcedureFlow = () => {
    cancelModal();
    router.push({
      name: 'procedure-flow',
      query: {
        status: status === PROCESS_STATE.VIEW_VERSION ? PROCESS_STATE.VIEW_VERSION : PROCESS_STATE.EDIT_VERSION,
        version,
        versionId,
        processId,
        procedureId,
        procedureIdOther,
      },
    });
  };
  const saveToProcedureFlow = async () => {
    Modal.destroyAll();
    try {
      await saveFunc();
      toProcedureFlow();
    } catch (error) {}
  };

  const save = async () => {
    Modal.confirm({
      title: t('提示'),
      wrapClassName: 'procedure-step-config-save',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否保存组件功能配置信息') + '?',
      footer() {
        return (
          <>
            <Space class='modal-footer'>
              <Button onClick={() => cancelModal()}>{t('取消')}</Button>
              <Button type='primary' onClick={() => saveToProcedureFlow()}>
                {t('保存')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  const back = () => {
    // if (isSaveProcedureStepConfig.value || isView.value) {
    if (isView.value || isSaveProcedureStepConfig.value) {
      toProcedureFlow();
    } else {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'procedure-step-config-save',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否保存组件功能配置信息') + '?',
        footer() {
          return (
            <>
              <Space class='modal-footer'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button onClick={() => toProcedureFlow()}>{t('不保存')}</Button>
                {!isView.value && (
                  <Button type='primary' onClick={() => saveToProcedureFlow()}>
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

  const toProcessConfig = () => {
    router.push({
      name: 'process-config',
    });
  };

  const toProcessFlow = () => {
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
</script>

<style lang="less">
  .procedure-step-config-save {
    .mes-modal-content {
      .mes-modal-body {
        .modal-footer {
          width: 100%;
          margin-top: 20px;
          justify-content: end;
        }
      }
    }
  }
</style>

<style scoped lang="less">
  .procedure-step-config-container {
    width: 100%;
    height: 100%;
    position: relative;
    .step-config-header {
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
  }
  .container {
    width: 100%;
    height: calc(100% - 56px);
    display: flex;
    background-color: var(--bmos-primary-color-white);
    .left {
      width: 349px;
      min-width: 260px;
      height: 100%;
      border-right: 1px solid var(--bmos-second-level-border-color);
    }
    .center {
      width: 349px;
      height: 100%;
      border-right: 1px solid var(--bmos-second-level-border-color);
      overflow-y: auto;
      min-width: 330px;
      .mes-form {
        padding-right: 10px;
      }
      .is-format-painter-model {
        color: var(--bmos-warning-color);
      }
    }
    .left,
    .center {
      .title {
        height: 44px;
        line-height: 44px;
        font-weight: 400;
        padding-left: var(--bmos-padding-small);
        border-bottom: 1px solid var(--bmos-second-level-border-color);
        display: flex;
        align-items: center;
        justify-content: space-between;
        .retract {
          margin-right: 10px;
          cursor: pointer;
          width: 18px;
          &:hover {
            background: #f2f3f4;
          }
        }
      }
      .content {
        padding: var(--bmos-padding-small);
        height: calc(100% - 44px);
        overflow-y: auto;
        .formula-node-container {
          width: 100%;
        }
      }
    }
    .right {
      flex: 1;
      height: 100%;
      overflow: auto;
      .formula {
        height: 100%;
      }
    }
  }
</style>
