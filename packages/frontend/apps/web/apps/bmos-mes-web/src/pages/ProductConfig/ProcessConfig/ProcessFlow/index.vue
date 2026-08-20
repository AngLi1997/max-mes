<template>
  <Spin :spinning>
    <div class="process-flow-container">
      <Row class="process-flow-header">
        <Col :span="8">
          <slot name="breadcrumb">
            <Breadcrumb class="crumb">
              <breadcrumb-item class="crumb-allow-click" @click="toProcessConfig">
                {{ t('工艺配置') }}
              </breadcrumb-item>
              <breadcrumb-item>{{ t('工艺流程') }}</breadcrumb-item>
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
      <div class="setting">
        <BMForm ref="leftFormRef" class="form" v-bind="formProps">
          <template #batchRecordItems="{ formModel, field }">
            <template v-for="(_item, index) in formModel[field]" :key="_item.toString()">
              <div class="record-select-item">
                <div class="record-select-item-record">
                  <form-item-rest>
                    <Select
                      v-model:value="formModel[field][index].batchRecordId"
                      :options="optionsRecordList"
                      allow-clear
                      required
                      :field-names="{
                        label: 'name',
                      }"
                      :disabled="!formModel.productId || isView"
                      :filter-option="filterOption"
                      :placeholder="t('请选择批记录')"
                      showSearch
                      class="bmos-wrap-select"
                      :dropdownMatchSelectWidth="false"
                      popupClassName="record-select-popup"
                      @select="(value: any) => recordSelect(value, formModel[field], index, formModel)"
                      @change="(value: any) => recordChange(index, formModel, formModel[field])">
                      <template v-if="recordFetching" #notFoundContent>
                        <Spin size="small" />
                      </template>
                    </Select>
                  </form-item-rest>

                  <form-item-rest>
                    <Select
                      v-model:value="formModel[field][index].batchRecordVersionId"
                      :options="optionsRecordVersionList[index]"
                      :disabled="!formModel.productId || isView"
                      allow-clear
                      :field-names="{
                        label: 'name',
                      }"
                      :filter-option="filterOption"
                      required
                      showSearch
                      :placeholder="t('请选择版本')"
                      @select="(val: any) => versionSelect(val, formModel, index)"
                      @change="versionChange" />
                  </form-item-rest>
                </div>
                <BMIcons
                  v-if="index > 0 && !isView"
                  :style="{
                    cursor: 'pointer',
                    marginLeft: '8px',
                    color: 'var(--bmos-danger-color)',
                  }"
                  icon="Delete"
                  @click="() => removeRecord(formModel, index)" />
              </div>
            </template>
          </template>
        </BMForm>
        <Flow
          ref="flowInstance"
          :modalJson="modalJson"
          :isView="isView"
          v-bind="isViewFlowToolBarAttr"
          :leftMap="leftMap"
          left-icon="Process2"
          next-icon="ProcessNext"
          class="flow"
          @flowDataChange="flowDataChange"
          @handleClickSet="handleClickSet"
          @handleClickNext="handleClickProcessNext"
          @nodeClick="handleClickNode" />
      </div>
    </div>
  </Spin>
  <RightDrawer
    v-model="rightDrawerOpen"
    :isView="isView"
    :settingNodeId
    :settingNodeFormData
    :processId="(realProcessId as string)"
    :detailProcedures
    :flowDataForDrawer
    :curProductFormulaVersionId
    :curProductionLineIds
    :realVersionId
    @updateFormValue="updateFormValue" />
  <PermissionModal
    v-model:permissionOpen="permissionOpen"
    :processId="(realProcessId as string)"
    @ok="savePermission" />

  <GatewayConfigDrawer
    v-model:open="gatewayOpen"
    :settingNodeId="settingNodeId"
    :gatewaySelectNodes="gatewaySelectNodes"
    :settingNodeFormData="settingNodeFormData"
    :isView="isView"
    @updateCellDataValue="updateCellDataValue" />
</template>

<script setup lang="tsx">
  import { ref } from 'vue';
  import {
    Row,
    Col,
    Breadcrumb,
    BreadcrumbItem,
    Space,
    Button,
    Select,
    FormItemRest,
    Spin,
    message,
  } from 'ant-design-vue';
  import { BMIcons } from '@bmos/icons';
  import Flow from '@/components/Flow';
  import { BMForm, BMIcon, Recordable } from '@bmos/components';
  import { FlowInstanceType, FlowNodeEnum } from '@/components/Flow/type';
  import { useLeftForm, useHeadAction } from './hooks';
  import RightDrawer from './components/RightDrawer.vue';
  import { Cell } from '@antv/x6';
  import { useRoute, useRouter } from 'vue-router';
  import { reqGetDetailUsingGET } from '@/services';
  import { FlowNodeType, PROCESS_STATE } from '../enum';
  import { t } from '@bmos/i18n';
  import PermissionModal from './components/PermissionModal.vue';
  import { FlowLeftToolBar } from '@/components/Flow/type/toolBar';
  import { getGatewaySelectNodes, processFlowData } from '../utils';
  import GatewayConfigDrawer from './components/GatewayConfigDrawer.vue';
  import { deepMerge, isEmpty } from '@bmos/utils';

  const router = useRouter();
  // 获取路由上的 query 参数
  const route = useRoute();
  const props = defineProps({
    pageParams: {
      type: Object as PropType<Recordable>,
      default: () => {},
    },
  });
  const emit = defineEmits<{
    (e: 'viewNext', cell: Cell, procedureId: string): void;
  }>();

  const version = ref<string>('');
  const processId = ref<string>('');
  const versionId = ref<string>('');
  const status = ref<string>('');
  const productId = ref<string>('');

  // 监听路由变化， 更新 watchStatus
  watch(
    () => route.query,
    async query => {
      if (query.version) {
        version.value = query.version as string;
      }
      if (query.version) {
        processId.value = query.processId as string;
      }
      if (query.versionId) {
        versionId.value = query.versionId as string;
      }
      if (query.productId) {
        productId.value = query.productId as string;
      }
      if (query.status) {
        status.value = query.status as string;
        await nextTick();
        if (query.status === PROCESS_STATE.ADD_VERSION || query.status === PROCESS_STATE.EDIT_VERSION) {
          leftFormRef.value?.updateSchema([
            {
              field: 'name',
              componentProps: {
                disabled: true,
              },
            },
            {
              field: 'productId',
              componentProps: {
                disabled: true,
              },
            },
          ]);
        }
      }
    },
    {
      immediate: true,
    },
  );
  const realVersion = computed(() => props.pageParams?.version || version.value);
  const realProcessId = computed(() => props.pageParams?.processId || processId.value);
  const realVersionId = computed(() => props.pageParams?.versionId || versionId.value);

  const flowInstance = ref<FlowInstanceType>();

  // 是否保存 process
  const isSaveProcess = ref<boolean>(true);
  const flowDataChange = () => {
    isSaveProcess.value = false;
  };

  // 权限
  const permissionOpen = ref<boolean>(false);
  const isNextProcedure = ref<boolean>(false);

  // 流程图 左侧 item 配置
  const leftMap: FlowLeftToolBar[] = [
    {
      title: t('工作流'),
      label: t('工序节点'),
      shape: FlowNodeEnum.CUSTOM,
      width: 206,
      height: 44,
      icon() {
        return <BMIcon type={'Process'} />;
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

  const leftFormState = useLeftForm({
    realVersion,
    realProcessId,
    status,
    productId,
    isSaveProcess,
    realVersionId,
  });
  const {
    leftFormRef,
    formProps,
    optionsRecordList,
    optionsRecordVersionList,
    modalJson,
    isView,
    removeRecord,
    versionChange,
    versionSelect,
    recordSelect,
    recordFetching,
    productTree,
    watchStatus,
    detailProcedures,
    originalModalJson,
    getProcessInfo,
    filterOption,
    recordChange,
    spinning,
  } = leftFormState;

  // 保存权限
  const permissionCheckedKeys = ref<string[]>([]);
  const savePermission = async (checkedKeys: string[]) => {
    permissionCheckedKeys.value = checkedKeys;
    if (isNextProcedure.value) {
      await saveFunc();
      toProcedure(curClickCell.value as Cell);
    } else {
      await saveFunc();
      if (isBack.value) {
        noSaveBack();
      } else {
        if (watchStatus.value !== PROCESS_STATE.EDIT_VERSION) {
          router.push({
            name: 'process-flow',
            query: {
              status: PROCESS_STATE.EDIT_VERSION,
              ...unref(processIdAndVersion),
            },
          });
        } else {
          getProcessInfo(
            // @ts-ignore
            processIdAndVersion.value.processId,
            processIdAndVersion.value.version,
          );
        }
      }
    }
  };
  // 点击工艺下一步
  const curClickCell = ref<Cell>();
  const clickToProcedure = async (cell: Cell) => {
    if (watchStatus.value === PROCESS_STATE.ADD_PROCESS || watchStatus.value === PROCESS_STATE.COPY_VERSION) {
      isNextProcedure.value = true;
      curClickCell.value = cell;
      permissionOpen.value = true;
    } else {
      await saveFunc();
      toProcedure(cell);
    }
  };

  const { save, saveFunc, back, cancelModal, processIdAndVersion, toProcessConfig, isBack, noSaveBack } = useHeadAction(
    {
      ...leftFormState,
      // @ts-ignore
      flowInstance,
      isSaveProcess,
      productTree,
      permissionCheckedKeys,
      isNextProcedure,
      permissionOpen,
      originalModalJson,
      versionId,
      realProcessId,
      realVersion,
      getProcessInfo,
      spinning,
    },
  );

  // 右侧抽屉是否打开
  const curProductFormulaVersionId = ref<string>('');
  const curProductionLineIds = ref<string[]>([]);
  const rightDrawerOpen = ref<boolean>(false);
  const settingNodeId = ref<string>('');
  const settingNodeFormData = ref<Recordable>({});
  const flowDataForDrawer = ref<Recordable>({});
  const handleClickSet = (cell: Cell) => {
    settingNodeId.value = cell.id;
    settingNodeFormData.value = {
      ...cell.data,
      duration: cell.data.duration || undefined,
      timeUnit: cell.data.timeUnit || undefined,
    };
    const flowData = flowInstance.value?.getFlowData() as {
      cells: Cell.Properties[];
    };
    flowDataForDrawer.value = processFlowData(flowData).filter((item: any) => {
      return item.type === FlowNodeType.CALL_ACTIVITY_TASK && item.key !== cell.id;
    });

    // 获取当前form表单的值
    curProductFormulaVersionId.value = leftFormRef.value?.getFormModelByField('productFormulaVersionId') || '';
    curProductionLineIds.value = leftFormRef.value?.getFormModelByField('productionLineIds') || [];

    rightDrawerOpen.value = true;
  };
  const updateFormValue = (id: string, data: Recordable) => {
    flowInstance.value?.updateFormValue(id, data);
    const cellData = flowInstance.value?.getCellDataById(id);
    settingNodeFormData.value = deepMerge(isEmpty(cellData) ? {} : cellData, data);
  };

  const toProcedure = async (cell: Cell) => {
    try {
      const params = {
        processId: processIdAndVersion.value.processId ? processIdAndVersion.value.processId : realProcessId.value,
        version: processIdAndVersion.value.version ? processIdAndVersion.value.version : realVersion.value,
      };
      const { data } = await reqGetDetailUsingGET(params as unknown as API.MesProcessDetailReq);
      const clickCell: any = data.procedures.find((item: any) => item.nodeId === cell.id);
      cancelModal();
      if (clickCell) {
        router.push({
          name: 'procedure-flow',
          query: {
            status:
              watchStatus.value === PROCESS_STATE.VIEW_VERSION
                ? PROCESS_STATE.VIEW_VERSION
                : PROCESS_STATE.EDIT_VERSION,
            version: data.version,
            versionId: data.processVersionId,
            processId: params.processId,
            procedureId: clickCell.id,
            procedureIdOther: clickCell.procedureId,
          },
        });
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const handleClickProcessNext = async (cell: Cell) => {
    if (props.pageParams?.processId) {
      const { data } = await reqGetDetailUsingGET({
        processId: props.pageParams?.processId,
        version: props.pageParams?.version,
      } as unknown as API.MesProcessDetailReq);
      const clickCell: any = data.procedures.find((item: any) => item.nodeId === cell.id);
      emit('viewNext', cell, clickCell.id);
      return;
    }
    if (isView.value || (isSaveProcess.value && watchStatus.value === PROCESS_STATE.EDIT_VERSION)) {
      toProcedure(cell);
    } else {
      clickToProcedure(cell);
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

  const isViewFlowToolBarAttr = computed(() => {
    return isView.value
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
    isSaveProcess.value = true;
    if (props.pageParams?.processId) {
      isView.value = true;
      leftFormRef.value?.setFormProps({
        disabled: true,
      });
      await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
    }
  });
</script>
<style lang="less">
  .record-select-popup {
    max-width: 460px;
  }
</style>
<style scoped lang="less">
  .record-select-popup {
    max-width: 460px;
  }
  .process-flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    .process-flow-header {
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
  .vertical-group-divider {
    padding: 0;
    margin: 0;
  }
  .setting {
    width: 100%;
    height: calc(100% - 56px);
    display: flex;
    background-color: var(--bmos-primary-color-white);
    padding-top: 5px;
    .form {
      width: 300px;
      min-width: 300px;
      height: 100%;
      padding: var(--bmos-padding-small);
      overflow-y: auto;
      .delete-record-button {
        line-height: 36px;
        margin-left: var(--bmos-margin-small);
      }
      .add-record-button {
        padding-top: 0;
        padding-left: 0;
      }
      .mes-input-group {
        margin-bottom: var(--bmos-margin-large);
      }
      .record-select-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px;
        background-color: var(--bmos-background-color);
        margin-bottom: var(--bmos-margin-medium);
        .record-select-item-record {
          flex: 1;
          overflow: hidden;
        }
        .record-select-item-record .mes-select:first-child {
          margin-bottom: 4px;
        }
      }
    }
    .flow {
      flex: 1;
      overflow: hidden;
    }
  }
</style>
