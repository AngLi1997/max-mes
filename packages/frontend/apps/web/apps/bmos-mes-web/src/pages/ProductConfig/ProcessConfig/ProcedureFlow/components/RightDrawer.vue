<template>
  <Drawer
    v-model:open="open"
    class="procedure-right-drawer-config"
    root-class-name="right-drawer-config-root"
    :title="drawerTitle"
    :footer="footer"
    destroyOnClose
    :maskClosable="false"
    placement="right"
    @afterOpenChange="afterOpenChange">
    <Segmented :value="segmentedValue" :options="segmentedData" block>
      <template #label="{ title, value }">
        <div @click.stop="() => segmentedChange(value)">{{ title }}</div>
      </template>
    </Segmented>
    <BMForm v-if="segmentedValue === SegmentedType.FunctionConfig" ref="setFormRef" v-bind="setFormProps">
      <template #duration="{ formModel, field }">
        <InputGroup compact>
          <form-item-rest>
            <InputNumber
              v-model:value="formModel[field]"
              style="width: 70%"
              :placeholder="t('时长')"
              :min="1"
              :step="1"
              :precision="0"
              :max="9999999999"
              @change="() => (isFormChange = true)" />
          </form-item-rest>
          <form-item-rest>
            <Select
              v-model:value="formModel['timeUnit']"
              style="width: 30%"
              allowClear
              :placeholder="t('单位')"
              @change="() => (isFormChange = true)">
              <SelectOption value="day">{{ t('日') }}</SelectOption>
              <SelectOption value="hour">{{ t('时') }}</SelectOption>
              <SelectOption value="minute">{{ t('分') }}</SelectOption>
            </Select>
          </form-item-rest>
        </InputGroup>
      </template>
      <template #label="{ formModel }">
        <InputGroup compact>
          <Input
            v-model:value="formModel['label']"
            :placeholder="currentNodeType === NodeType.StepNode ? t('请输入步骤名称') : t('请输入任务名称')"
            :disabled="disabledLabel || formModel.id"
            style="width: 85%"
            @change="e => changeLabel(e, formModel)" />
          <Dropdown :trigger="['click']" overlayClassName="select-procedure-id">
            <Button :disabled="!!formModel.id" style="width: 15%" :icon="h(ClockCircleOutlined)"></Button>
            <template #overlay>
              <Menu
                v-if="optionsData.length > 0"
                :items="optionsData"
                @click="info => procedureStepIdSelect(info, formModel)"></Menu>
              <Empty v-else />
            </template>
          </Dropdown>
        </InputGroup>
        <CloseOutlined
          v-if="disabledLabel && !isView && !formModel.id"
          class="clear-label"
          @click="() => clearLabel(formModel)" />
      </template>
      <template #recordItem>
        <Cascader
          v-model:value="recordItemNameId"
          :options="treeData"
          :placeholder="t('请选择记录项')"
          @change="recordItemCascaderChange" />
      </template>
    </BMForm>
    <BMForm v-if="segmentedValue === SegmentedType.ExecutionCondition" ref="ecFormRef" v-bind="ecFormProps"></BMForm>
    <BMForm v-if="segmentedValue === SegmentedType.CompletionCondition" ref="ccFormRef" v-bind="ccFormProps"></BMForm>
  </Drawer>

  <SelectRecordModal
    v-model:selectRecordOpen="selectRecordOpen"
    :curSelectRecordItemId="curSelectRecordItemId"
    :batchRecordItems="batchRecordItems"
    :treeData="treeData"
    :expandedKeys="expandedKeys"
    :selectedKeys="selectedKeys"
    :fileContent="fileContent"
    @selectRecordItemId="selectRecordItemIdChange" />
</template>

<script lang="tsx" setup>
  import { createVNode, h } from 'vue';
  import { Button, Space, message, Menu, Modal, Drawer } from 'ant-design-vue';
  import { ClockCircleOutlined, CloseOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Recordable, BMForm, formInstance } from '@bmos/components';
  import { NodeType, SegmentedType, type RightDrawerProps } from '../types';
  import SelectRecordModal from './SelectRecordModal.vue';
  import { reqProcedureStepHistoricListGET, reqProcedureStepValidateNameGET, reqRecordListItem } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { getItemByNodeId, getParentIdByNodeId } from '../utils';
  import { t } from '@bmos/i18n';
  import { isArray, isNullOrUnDef, cloneDeep, makeSticky } from '@bmos/utils';
  import { useForm } from '../hooks/useForm';
  import { SegmentedValue } from 'ant-design-vue/es/segmented/src/segmented';

  const emit = defineEmits(['update:open', 'updateFormValue']);
  const props = withDefaults(defineProps<RightDrawerProps>(), {
    open: false,
    settingNodeId: '',
    settingNodeFormData: () => ({}),
    isView: false,
    batchRecordItems: () => [],
    procedureId: '',
    procedureIdOther: '',
    flowDataForDrawer: () => ({}),
    detailProceduresSteps: () => [],
    processDetail: () => ({}),
    currentNodeType: NodeType.StepNode,
    versionId: '',
  });

  // computed set gte 监听open变化
  const open = computed({
    get() {
      return props.open;
    },
    set(val) {
      emit('update:open', val);
    },
  });

  const drawerTitle = computed(() => {
    return props.currentNodeType === NodeType.StepNode ? t('步骤配置') : t('任务配置');
  });

  const setFormRef: Ref<formInstance> = ref({});
  const ecFormRef: Ref<formInstance> = ref({});
  const ccFormRef: Ref<formInstance> = ref({});

  const cancelDrawer = () => {
    open.value = false;
  };

  const savaFun = async () => {
    try {
      let res: Recordable = {};
      if (segmentedValue.value === SegmentedType.ExecutionCondition) {
        const executionConditionRef = ecFormRef.value?.compRefMap.get('executeCondition.conditionList');
        executionConditionRef?.validateForm();
        await ecFormRef.value?.validate();
        await executionConditionRef?.validateForm();
        res = ecFormRef.value?.getFormModelByField(['executeCondition']);
      } else if (segmentedValue.value === SegmentedType.CompletionCondition) {
        const completionConditionRef = ccFormRef.value?.compRefMap.get('completeCondition.conditionList');
        completionConditionRef?.validateForm();
        await ccFormRef.value?.validate();
        await completionConditionRef?.validateForm();
        res = ccFormRef.value?.getFormModelByField(['completeCondition']);
      } else {
        const allRes = await setFormRef.value?.validate();
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { completeCondition, executeCondition, ...curRes } = allRes;
        res = curRes;
        if (res?.procedureStepId) {
          res.label = optionsData.value.find(item => item.key === res?.procedureStepId)?.label;
        } else {
          const { data } = await reqProcedureStepValidateNameGET(props.procedureIdOther, res?.label);
          if (data) {
            // 工序步骤名称已存在
            message.error(t('工序步骤别名或任务别名已存在'));
            return Promise.reject();
          }
        }
        const isHasName = props.flowDataForDrawer?.find((item: any) => item.name === res?.name);
        if (isHasName) {
          message.error(t('工序步骤别名或任务别名已存在'));
          return Promise.reject();
        }
      }
      if (res?.recordItemId && isArray(res.recordItemId)) {
        res.recordItemId = res.recordItemId[res.recordItemId.length - 1];
      }
      emit('updateFormValue', props.settingNodeId, res);
      isFormChange.value = false;
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const ok = async () => {
    try {
      await savaFun();
      open.value = false;
    } catch (error) {
      // console.log(error);
    }
  };

  const saveModal = ref();
  const cancelModal = () => {
    saveModal.value?.destroy();
  };

  const changeInit = (val: SegmentedValue) => {
    switch (val) {
      case SegmentedType.FunctionConfig:
        setNodeFormData(props.settingNodeFormData);
        break;
      case SegmentedType.ExecutionCondition:
        ecFormInitValue(val);
        nextTick(() => {
          makeSticky('.mes-drawer-body', '.condition-label');
        });
        break;
      case SegmentedType.CompletionCondition:
        ccFormInitValue(val);
        nextTick(() => {
          makeSticky('.mes-drawer-body', '.condition-label');
        });
        break;
      default:
        break;
    }
  };

  const saveAndChange = async (val: SegmentedValue) => {
    try {
      await savaFun();
      changeInit(val);
      isFormChange.value = false;
    } catch (error) {
      // console.log(error);
    } finally {
      cancelModal();
    }
  };

  const segmentedChange = (val: SegmentedValue) => {
    if (!isFormChange.value || props.isView) {
      changeInit(val);
      return;
    }
    saveModal.value = Modal.confirm({
      wrapClassName: 'config-return-modal',
      icon: createVNode(ExclamationCircleOutlined),
      title: t('是否保存当前配置项'),
      closable: true,
      footer() {
        return (
          <>
            <Space class='footer-btns'>
              <Button onClick={() => cancelModal()}>{t('取消')}</Button>
              <Button
                onClick={() => {
                  changeInit(val);
                  isFormChange.value = false;
                  setRecordVal(props.settingNodeFormData, 'recordItemId');
                  cancelModal();
                }}>
                {t('不保存')}
              </Button>
              <Button type='primary' onClick={() => saveAndChange(val)}>
                {t('保存')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  const clear = () => {
    if (segmentedValue.value === SegmentedType.ExecutionCondition) {
      ecFormClearValue();
    } else if (segmentedValue.value === SegmentedType.CompletionCondition) {
      ccFormClearValue();
    }
    open.value = false;
  };

  const recordItemCascaderChange = (value: any, selectedOptions: any) => {
    isFormChange.value = true;
    if (!value) {
      setFormRef.value?.setFormModel('recordItemId', '');
      setFormRef.value?.setFormModel('recordVersionId', '');
      curSelectRecordItemId.value = '';
      fileContent.value = '';
      selectedKeys.value = [];
      recordItemName.value = '';
      curSelectRecordItemId.value = '';
    } else {
      recordItemName.value = selectedOptions.map((item: any) => item.label).join('/');
      setFormRef.value?.setFormModel('recordItemId', value[value.length - 1]);
      setFormRef.value?.setFormModel('recordVersionId', value[0]);
      selectedKeys.value = [value[value.length - 1]];
      const itemNode = getItemByNodeId(treeData.value, value[value.length - 1]);
      if (itemNode.key) {
        fileContent.value = itemNode.fileContent;
        selectedKeys.value = [itemNode.itemId];
        recordItemName.value = `${itemNode.recordName}/${itemNode.name}`;
      }
      curSelectRecordItemId.value = value[value.length - 1];
    }
  };

  const {
    segmentedValue,
    segmentedData,
    setFormProps,
    isFormChange,
    selectRecordOpen,
    selectedKeys,
    curSelectRecordItemId,
    ecFormProps,
    ccFormProps,
    ecFormInitValue,
    ccFormInitValue,

    ecFormClearValue,
    ccFormClearValue,
  } = useForm({
    props,
    setFormRef,
    ecFormRef,
    ccFormRef,
    recordItemCascaderChange,
    emit,
  });

  const okBtnLoading = ref<boolean>(false);
  const footer = (
    <Space class='footer-action'>
      {/* 如果 isView 为 true, 不显示 确定按钮 */}
      {!props.isView && (
        <>
          <Button type='primary' loading={okBtnLoading.value} onClick={() => ok()}>
            {t('确定')}
          </Button>
          {segmentedValue.value !== SegmentedType.FunctionConfig && (
            <Button danger onClick={() => clear()}>
              {t('清空')}
            </Button>
          )}
        </>
      )}
      <Button onClick={() => cancelDrawer()}>{t('取消')}</Button>
    </Space>
  );

  const optionsData = ref<any[]>([]);

  const getOptions = async (name?: string) => {
    if (props.procedureIdOther.length > 0) {
      try {
        const { data } = await reqProcedureStepHistoricListGET(props.procedureIdOther, props.procedureId, name);
        optionsData.value = data.map((item: any) => ({
          label: item.name,
          key: item.id,
        }));

        return Promise.resolve(optionsData.value);
      } catch (error) {
        return Promise.resolve([]);
      }
    } else {
      return Promise.resolve([]);
    }
  };

  const disabledLabel = ref<boolean>(false);
  const procedureStepIdSelect = (info: any, formModel: any) => {
    const { item } = info;
    formModel['procedureStepId'] = item.originItemValue.key;
    formModel['label'] = item.originItemValue.label;
    formModel['name'] = item.originItemValue.label;
    setFormRef.value?.validateFields(['label', 'name']);
    disabledLabel.value = true;
    isFormChange.value = true;
  };

  const clearLabel = (formModel: any) => {
    formModel['procedureStepId'] = '';
    formModel['label'] = '';
    formModel['name'] = '';
    setFormRef.value?.validateFields(['label', 'name']);
    disabledLabel.value = false;
    isFormChange.value = true;
  };

  const changeLabel = (e: any, formModel: any) => {
    formModel['name'] = e.target.value;
    setFormRef.value?.validateFields(['label', 'name']);
    isFormChange.value = true;
  };

  // 级联选择
  const recordItemName = ref<string>('');
  const recordItemNameId = ref<string[]>([]);
  const selectRecordItemIdChange = (id: string, node: Recordable) => {
    isFormChange.value = true;
    recordItemNameId.value = [getParentIdByNodeId(treeData.value, id), id];
    setFormRef.value?.setFormModel('recordItemId', id);
    setFormRef.value?.setFormModel('recordVersionId', node.recordVersionId);
    curSelectRecordItemId.value = id;
    selectedKeys.value = [id];
    const itemNode = getItemByNodeId(treeData.value, id);
    if (itemNode.key) {
      fileContent.value = itemNode.fileContent;
      selectedKeys.value = [itemNode.itemId];
      recordItemName.value = `${itemNode.recordName}/${itemNode.name}`;
    }
  };

  const setRecordVal = (val: Recordable, key: string) => {
    if (!val[key]) {
      recordItemNameId.value = [];
      return;
    }
    curSelectRecordItemId.value = val[key];
    const id = getParentIdByNodeId(treeData.value, val[key]);
    if (id) {
      recordItemNameId.value = [id, val[key]];
      setFormRef.value?.setFormModel(key, [id, val[key]]);
    }
    const itemNode = getItemByNodeId(treeData.value, val[key]);
    if (itemNode.key) {
      fileContent.value = itemNode.fileContent;
      selectedKeys.value = [itemNode.itemId];
      recordItemName.value = `${itemNode.recordName}/${itemNode.name}`;
    }
  };
  const setNodeFormData = async (val: Recordable) => {
    segmentedValue.value = SegmentedType.FunctionConfig;
    await nextTick();
    disabledLabel.value = false;
    Object.keys(cloneDeep(val)).forEach(key => {
      if (key === 'label' && !val['name']) {
        setFormRef.value?.setFormModel('name', val[key]);
      }
      if (key === 'name' && !val['label'] && !val['procedureStepId']) {
        setFormRef.value?.setFormModel('label', val[key]);
      }
      if (key === 'procedureStepId' && val[key]) {
        setFormRef.value?.setFormModel('label', optionsData.value.find(item => item.key === val[key])?.label);
        disabledLabel.value = true;
      }
      // 如果 key 为 recordItemId 时, 设置 curSelectRecordItemId
      if (key === 'recordItemId' && val[key]) {
        setRecordVal(val, key);
      } else if (key === 'nodeFunction' && val[key] === '记录作业') {
        setFormRef.value?.setFormModel('nodeFunction', '0');
      } else {
        setFormRef.value?.setFormModel(key, isNullOrUnDef(val[key]) ? undefined : val[key]);
      }
    });
  };

  watch(
    () => props.open,
    async val => {
      isFormChange.value = false;
      await nextTick();
      if (val) {
        if (props.isView) {
          setFormRef.value?.setFormProps({
            disabled: true,
          });
        }
      }
    },
    {
      immediate: true,
    },
  );

  // TREE
  const treeData = ref<DataNode[]>([]);
  const expandedKeys = ref<string[]>([]);
  const fileContent = ref<any>();
  const afterOpenChange = async (open: boolean) => {
    if (open) {
      // 清空数据
      recordItemNameId.value = [];
      await getOptions();
      try {
        okBtnLoading.value = true;
        treeData.value = [];
        const { data } = await reqRecordListItem(
          props.batchRecordItems.map(item => item.batchRecordVersionId) as unknown as API.ListRecordItemReq,
        );
        data.forEach((item: any) => {
          treeData.value.push({
            title: item.recordName,
            key: item.versionId,
            value: item.versionId,
            label: item.recordName,
            selectable: false,
            children: item.recordItemList
              ? item.recordItemList.map((record: any) => {
                  if (curSelectRecordItemId.value === record.itemId) {
                    fileContent.value = record.fileContent;
                    selectedKeys.value = [record.itemId];
                    recordItemName.value = `${item.recordName}/${record.name}`;
                  }
                  return {
                    ...record,
                    title: record.name,
                    key: record.itemId,
                    value: record.itemId,
                    label: record.name,
                  };
                })
              : [],
          });
        });
        setNodeFormData(props.settingNodeFormData);
        expandedKeys.value = data.map((item: any) => item.versionId);
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        okBtnLoading.value = false;
      }
    } else {
      curSelectRecordItemId.value = '';
    }
  };
</script>

<style lang="less">
  .procedure-right-drawer-config {
    .mes-drawer-header-title {
      flex-direction: row-reverse;
      .mes-drawer-close {
        margin-right: 0;
      }
    }
    .select-record-item {
      margin-left: 15px;
    }
    .display-record-item-input.mes-input-disabled {
      background-color: var(--bmos-primary-color-white);
      color: #242526;
    }
    .mes-drawer-footer {
      .footer-action {
        display: flex;
        justify-content: end;
        flex-direction: row-reverse;
      }
    }
    .clear-label {
      position: absolute;
      right: 20%;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
      z-index: 99;
    }
    .condition {
      background-color: var(--bmos-background-color);
      margin-top: 4px;
      padding: var(--bmos-padding-mini);
      font-size: 12px;
    }
  }
  .select-procedure-id {
    min-width: 340px !important;
    max-height: 180px;
    overflow-y: auto;
    border-radius: 4px;
    border: 1px solid var(--bmos-first-level-border-color);
    background-color: #fff;
    .mes-dropdown-menu-item {
      text-align: left;
    }
  }
  .mes-tooltip-inner {
    p {
      margin-bottom: 0;
    }
  }
  .operator-help-popover {
    color: aqua;
    .container {
      margin-left: 5px;
      .operators {
        background-color: var(--bmos-primary-color-tab);
        color: var(--bmos-primary-color);
        border: none;
        display: inline-block;
        font-size: 14px;
        margin-inline-end: 9px;
        border-radius: 3px;
        line-height: 20px;
        padding: 2px 8px 2px 8px;
        margin-bottom: 5px;
      }
    }
  }
</style>
<style scoped lang="less">
  :deep(.operator-help) {
    display: flex;
    align-items: center;
    justify-content: center;
    .operator-help-icon {
      margin-left: 5px;
    }
  }
  :deep(.condition-label) {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 342px;
    z-index: 101;
    background: #fff;
    top: 0;
  }
</style>
