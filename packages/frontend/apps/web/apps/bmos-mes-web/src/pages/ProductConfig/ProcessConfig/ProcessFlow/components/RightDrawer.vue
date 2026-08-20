<template>
  <Drawer
    v-model:open="open"
    class="right-drawer-config"
    root-class-name="right-drawer-config-root"
    :title="t('工序配置')"
    :footer="footer"
    destroyOnClose
    placement="right">
    <Segmented :value="segmentedValue" :options="segmentedData" block>
      <template #label="{ title, value }">
        <div @click.stop="() => segmentedChange(value)">{{ title }}</div>
      </template>
    </Segmented>
    <BMForm v-if="segmentedValue === SegmentedType.FunctionConfig" ref="setFormRef" v-bind="setFormProps">
      <template #label="{ formModel }">
        <InputGroup compact>
          <Input
            v-model:value="formModel['label']"
            style="width: 85%"
            :disabled="disabledLabel || (formModel.id && status?.toString() !== PROCESS_STATE.COPY_VERSION)"
            :placeholder="t('请输入工序名称')"
            @change="e => changeLabel(e, formModel)" />
          <Dropdown :trigger="['click']" overlayClassName="select-procedure-id">
            <Button
              style="width: 15%"
              :icon="h(ClockCircleOutlined)"
              :disabled="formModel.id && status?.toString() !== PROCESS_STATE.COPY_VERSION"></Button>
            <template #overlay>
              <Menu
                v-if="optionsData.length > 0"
                :items="optionsData"
                @click="(info: any) => procedureIdSelect(info, formModel)"></Menu>
              <Empty v-else />
            </template>
          </Dropdown>
        </InputGroup>
        <CloseOutlined
          v-if="disabledLabel && !isView && !(formModel.id && status?.toString() !== PROCESS_STATE.COPY_VERSION)"
          class="clear-label"
          @click="() => clearLabel(formModel)" />
      </template>
    </BMForm>
    <BMForm v-if="segmentedValue === SegmentedType.CompletionCondition" ref="ccFormRef" v-bind="ccFormProps"></BMForm>
  </Drawer>
</template>

<script lang="tsx" setup>
  import { createVNode, h } from 'vue';
  import { Button, Space, Tooltip, message, Menu, Modal, Tag } from 'ant-design-vue';
  import {
    ClockCircleOutlined,
    InfoCircleOutlined,
    CloseOutlined,
    ExclamationCircleOutlined,
  } from '@ant-design/icons-vue';
  import { Recordable, BMForm, FormProps, formInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import {
    reqAllPlanTeamListProcessTeam,
    reqProcedureHistoricListGET,
    reqAllProcedureModelRoleList,
    reqProcedureValidateNameGET,
    reqAllFactoryProcessLineRoom,
    reqProductFormulaModelMaterialList,
  } from '@/services';
  import { PROCESS_STATE } from '../../enum';
  import { isNullOrUnDef, loopSelectableNotValueTree, makeSticky } from '@bmos/utils';
  import { useRightDrawerForm } from './useRightDrawer';
  import { SegmentedType } from '../../ProcedureFlow/types';
  import { SegmentedValue } from 'ant-design-vue/es/segmented/src/segmented';

  const emit = defineEmits(['updateFormValue']);
  const props = defineProps({
    settingNodeId: {
      type: String,
      default: '',
    },
    settingNodeFormData: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    isView: {
      type: Boolean,
      default: false,
    },
    processId: {
      type: String,
      default: '',
    },
    flowDataForDrawer: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    detailProcedures: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
    curProductFormulaVersionId: {
      type: String,
      default: '',
    },
    curProductionLineIds: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    realVersionId: {
      type: [String, undefined],
      default: undefined,
    },
  });

  // 获取路由上的 query 参数
  const route = useRoute();
  const { status } = route.query;

  // computed set gte 监听open变化
  const open = defineModel<boolean>({ default: false });

  // form 是否change
  const isFormChange = ref<boolean>(false);

  const cancelDrawer = () => {
    open.value = false;
  };

  const setFormRef = ref<formInstance>();
  const ccFormRef = ref<formInstance>();

  const { segmentedValue, segmentedData, ccFormInitValue, ccFormProps, ccFormClearValue } = useRightDrawerForm({
    props,
    // @ts-ignore
    ccFormRef,
    isFormChange,
    emit,
  });

  const savaFun = async () => {
    try {
      let res: Recordable = {};
      if (segmentedValue.value === SegmentedType.CompletionCondition) {
        const completionConditionRef = ccFormRef.value?.compRefMap.get('completeCondition.conditionList');
        completionConditionRef?.validateForm();
        await ccFormRef.value?.validate();
        await completionConditionRef?.validateForm();
        res = ccFormRef.value?.getFormModelByField(['completeCondition']);
      } else {
        res = await setFormRef.value?.submit();
        if (res?.procedureId && status?.toString() !== PROCESS_STATE.COPY_VERSION) {
          // res.label = optionsData.value.find(item => item.key === res?.procedureId)?.label;
        } else {
          if (
            status?.toString() !== PROCESS_STATE.ADD_PROCESS &&
            status?.toString() !== PROCESS_STATE.COPY_VERSION &&
            !res?.procedureId
          ) {
            const { data } = await reqProcedureValidateNameGET(props.processId, res?.label);
            if (data) {
              // 工序名称已存在
              message.error(t('工序别名已存在'));
              return Promise.reject();
            }
          }
        }
        const isHasName = props.flowDataForDrawer?.find((item: any) => {
          return item.name === res.name;
        });
        if (isHasName) {
          message.error(t('工序别名已存在'));
          return Promise.reject();
        }
      }
      isFormChange.value = false;
      emit('updateFormValue', props.settingNodeId, res);
      return Promise.resolve();
    } catch (error: any) {
      return Promise.reject(error);
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

  const ok = async () => {
    try {
      await savaFun();
      open.value = false;
    } catch (error) {
      // console.log(error);
    }
  };

  const clear = () => {
    if (segmentedValue.value === SegmentedType.CompletionCondition) {
      ccFormClearValue();
    }
    open.value = false;
  };

  const footer = (
    <Space class='footer-action'>
      {/* 如果 isView 为 true, 不显示 确定按钮 */}
      {!props.isView && (
        <>
          <Button type='primary' onClick={() => ok()}>
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
    if (props.processId.length > 0 && status?.toString() !== PROCESS_STATE.COPY_VERSION) {
      try {
        const { data } = await reqProcedureHistoricListGET(props.processId, name);
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
  const procedureIdSelect = (info: any, formModel: any) => {
    const { item } = info;
    formModel['procedureId'] = item.originItemValue.key;
    formModel['label'] = item.originItemValue.label;
    formModel['name'] = item.originItemValue.label;
    setFormRef.value?.validateFields(['label', 'name']);
    disabledLabel.value = true;
    isFormChange.value = true;
  };

  const clearLabel = (formModel: any) => {
    formModel['procedureId'] = '';
    formModel['label'] = '';
    formModel['name'] = '';
    setFormRef.value?.validateFields(['label', 'name']);
    disabledLabel.value = false;
  };

  const changeLabel = (e: any, formModel: any) => {
    formModel['name'] = e.target.value;
    setFormRef.value?.validateFields(['label', 'name']);
    isFormChange.value = true;
  };

  const setFormProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('工序别名'),
        required: true,
      },
      {
        field: 'label',
        label: () => {
          return (
            <div>
              <span>{t('工序名称')}</span>
              <Tooltip placement='top'>
                {{
                  default: () => <InfoCircleOutlined style={{ marginLeft: '5px' }} />,
                  title: () => (
                    <div class='mes-tooltip-inner'>
                      <p>{t('输入工序名称新建工序')}</p>
                      <p>
                        {t('点击')}
                        <ClockCircleOutlined />
                        {t('图标后，可选择历史的工序')}
                      </p>
                    </div>
                  ),
                }}
              </Tooltip>
            </div>
          );
        },
        required: true,
        slot: 'label',
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (rule, value) => {
                if (!value) {
                  return Promise.reject(t('请输入工序名称'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'stageCode',
        component: 'Input',
        label: t('工序阶段编码'),
        componentProps: {
          onChange: () => {
            isFormChange.value = true;
          },
        },
      },
      {
        field: 'principal',
        component: 'Select',
        label: t('负责人'),
        required: true,
        componentProps: {
          showSearch: true,
          fieldNames: { label: 'roleName', value: 'id' },
          request: async () => {
            const { data } = await reqAllProcedureModelRoleList(props.settingNodeFormData?.id);
            return data;
          },
          filterOption: (input: string, option: any) => {
            return option.roleName.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
      },
      {
        field: 'groupIds',
        component: 'Select',
        label: t('执行班组'),
        required: true,
        componentProps: {
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          mode: 'multiple',
          fieldNames: { value: 'id' },
          request: async () => {
            const { data } = await reqAllPlanTeamListProcessTeam(props.curProductionLineIds, props.realVersionId);
            return data.map((item: any) => ({
              label: item.name + '-' + item.code,
              ...item,
            }));
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
        componentSlots: {
          tagRender: ({ slotData }: any) => {
            if (!slotData) return;
            const { label, closable, onClose, disabled } = slotData;
            return (
              <Tag closable={closable || disabled} class='dynamicSelectTag' onClose={onClose}>
                {label}
              </Tag>
            );
          },
        },
      },
      {
        field: 'roomIdList',
        component: 'TreeSelect',
        label: t('房间'),
        componentProps: {
          showSearch: true,
          treeNodeFilterProp: 'showName',
          multiple: true,
          fieldNames: { value: 'roomIdPath', label: 'showName' },
          request: async () => {
            try {
              if (!props.curProductionLineIds) return [];
              const { data } = await reqAllFactoryProcessLineRoom(
                props.curProductionLineIds,
                props.settingNodeFormData.id,
              );
              return loopSelectableNotValueTree(data, 'roomFlag', true);
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
        componentSlots: {
          tagRender: ({ slotData }: any) => {
            if (!slotData) return;
            const { label, closable, onClose, disabled } = slotData;
            return (
              <Tag closable={closable || disabled} class='dynamicSelectTag' onClose={onClose}>
                {label}
              </Tag>
            );
          },
        },
      },
      {
        field: 'formulaMaterialIdList',
        component: 'Select',
        label: t('生产BOM物料'),
        componentProps: {
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          mode: 'multiple',
          request: async () => {
            try {
              if (!props.curProductFormulaVersionId) return [];
              const { data } = await reqProductFormulaModelMaterialList(
                props.curProductFormulaVersionId,
                props.settingNodeFormData.id,
              );
              return data.map((item: any) => ({
                label: item.materialMergeCode + '-' + item.materialName,
                value: item.id,
                disabled: item.disabled,
              }));
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            isFormChange.value = true;
          },
        },
        componentSlots: {
          tagRender: ({ slotData }: any) => {
            if (!slotData) return;
            const { label, closable, onClose, disabled } = slotData;
            return (
              <Tag closable={closable || disabled} class='dynamicSelectTag' onClose={onClose}>
                {label}
              </Tag>
            );
          },
        },
      },
    ],
  });

  watch(
    () => open.value,
    async val => {
      await nextTick();
      isFormChange.value = false;
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

  const setNodeFormData = async (val: Recordable) => {
    segmentedValue.value = SegmentedType.FunctionConfig;
    await nextTick();
    disabledLabel.value = false;
    const nameOptions = await getOptions();
    Object.keys(val).forEach(key => {
      if (key === 'procedureId' && val[key]) {
        disabledLabel.value = true;
      }
      if (key === 'label' && val['procedureId']) {
        val[key] = nameOptions.find(item => item.key === val['procedureId'])?.label;
      }
      setFormRef.value?.setFormModel(key, isNullOrUnDef(val[key]) ? undefined : val[key]);
    });
    if (props.isView) {
      setFormRef.value?.setFormProps({
        disabled: true,
      });
    }
  };

  watch(
    () => props.settingNodeFormData,
    async val => {
      setNodeFormData(val);
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>

<style lang="less">
  .right-drawer-config {
    .mes-drawer-header-title {
      flex-direction: row-reverse;
      .mes-drawer-close {
        margin-right: 0;
      }
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
    border: 1px solid var(--bmos-first-level-border-color);
    border-radius: 4px;
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
