import { NODE_INFO, NODE_TYPE, StyleEnum } from '@/components/Record';
import { reqRecordListComponentReq, reqStepConfigListReq } from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { cloneDeep, findItemByAttr, isEmpty } from '@bmos/utils';
import { Button, Modal, Space, message } from 'ant-design-vue';
import { ComputedRef, createVNode, onMounted, ref } from 'vue';
import { filterLeafNode } from '../utils';
import { HAS_FORM_BUSINESS_NODE } from './const';

export type UseComponentsParams = {
  recordItemId: string;
  recordVersionId: string;
  procedureStepId: string;
  procedureStepModelId: string;
  version: string;
  processId: string;
  reusable: string;
  isView: ComputedRef<boolean>;
  nodeListRef: Ref<any>;
};

export const useComponents = ({
  recordItemId,
  recordVersionId,
  procedureStepId,
  procedureStepModelId,
  version,
  processId,
  reusable,
  isView,
  nodeListRef,
}: UseComponentsParams) => {
  const nodeList = ref<Array<any>>([]);
  const fileContent = ref<string>('');
  const recordRef = ref<any>(null);
  const configFormRef = ref<any>(null);
  // 模板 active key
  const templateActiveKeys = ref<Array<KEY>>([]);
  // 节点 active key
  const nodeActiveKeys = ref<Array<KEY>>([]);
  // 激活的节点信息 data
  const activeNodeData = ref<any>({});
  // active componentType
  const activeComponentType = ref<NODE_TYPE | HAS_FORM_BUSINESS_NODE | null>(null);
  const getNodeList = async () => {
    try {
      const { data } = await reqRecordListComponentReq({
        itemId: recordItemId,
        recordVersionId,
      } as unknown as API.ListRecordItemRes);
      nodeList.value = filterLeafNode(data?.componentList) || [];
      fileContent.value = data?.fileContent;
      recordRef.value?.setContentByConfig(data);
      return Promise.resolve();
    } catch (error: any) {
      // error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };

  // 根据 key 获取组件类型
  const getComponentTypeByKey = (key: KEY) => {
    return nodeList.value.find(item => item.fieldId === key)?.componentType || null;
  };

  const removeRecordAllDataBindClass = (classStr = StyleEnum.nodeConfig) => {
    recordRef.value?.clearAllNodesClass(classStr);
  };
  const setRecordClassByFieldsTimer = ref<any>(null);
  const setRecordClassByFields = async (fields: any) => {
    try {
      if (!fields.length) {
        removeRecordAllDataBindClass();
        return;
      }
      setRecordClassByFieldsTimer.value && clearTimeout(setRecordClassByFieldsTimer.value);
      removeRecordAllDataBindClass();
      await nextTick();
      setRecordClassByFieldsTimer.value = setTimeout(() => {
        recordRef.value?.setNodesStyle([...new Set(fields)], StyleEnum.nodeConfig);
      }, 500);
    } catch (error) {}
  };
  const setRecordClassByField = async (field: KEY) => {
    try {
      if (!field) {
        return;
      }
      recordRef.value?.setNodesStyle([field], StyleEnum.nodeConfig);
    } catch (error) {}
  };

  const removeRecordClassByField = async (field: KEY) => {
    try {
      if (!field) {
        return;
      }
      recordRef.value?.clearNodesClassByIds([field], StyleEnum.nodeConfig);
    } catch (error) {}
  };

  const setRecordClassByConfig = (config: any) => {
    setRecordClassByFields(
      config
        .map((item: any) => {
          if (Object.values(NODE_TYPE).includes(getComponentTypeByKey(item.fieldId))) {
            return item.fieldId;
          }
          return null;
        })
        .filter(Boolean),
    );
  };
  // 获取所有的节点的配置
  const configList = ref<Array<Record<string, any>>>([]);
  const initFormValue = ref<Record<string, any>>({});
  const getComponentConfig = async () => {
    try {
      const { data } = await reqStepConfigListReq({
        procedureStepId,
        procedureStepModelId,
        processId,
        processVersion: version,
        reusable: reusable.toString() === 'true' ? true : false,
      } as unknown as API.StepConfigListReq);
      configList.value = data;
      setRecordClassByConfig(data);
    } catch (error) {
      //
    }
  };

  const formatPainterModel = ref(false);
  const showFormatPainter = computed(() => {
    return (
      (activeComponentType.value && Object.values(NODE_TYPE).includes(activeComponentType.value as NODE_TYPE)) ||
      formatPainterModel.value
    );
  });
  const handleClickFormatPainter = () => {
    formatPainterModel.value = !formatPainterModel.value;
  };

  const configFormKey = ref<number>(new Date().getTime());
  /**
   * @description: 根据 key 获取表单值
   * @param {KEY} key
   * @returns {void} 返回
   */
  const getFormValueByKey = (key: KEY) => {
    try {
      const values = configList.value.find(item => item.fieldId === key)?.configInfo;
      configFormKey.value = new Date().getTime();
      if (values) {
        initFormValue.value = JSON.parse(values);
      } else {
        initFormValue.value = {};
      }
    } catch (error) {
      //
    }
  };

  const loopGetBasicNodeKeys = (data: any) => {
    const keys: Array<KEY> = [];
    if (data.children && data.children.length) {
      data.children.forEach((item: any) => {
        keys.push(item.fieldId);
        if (item.children && item.children.length) {
          keys.push(...loopGetBasicNodeKeys(item));
        }
      });
    }
    return keys;
  };

  const cancelModal = () => {
    Modal.destroyAll();
  };

  const doNotSave = (key: KEY, activeKey: string, data: any) => {
    configFormRef.value?.setHasChange(false);
    nodeClick(key, activeKey, data);
    cancelModal();
  };

  const saveAndJump = async (key: KEY, activeKey: string, data: any) => {
    await configFormRef.value?.submitForm();
    nodeClick(key, activeKey, data);
    cancelModal();
  };

  const confirmSaveConfigModel = (key: KEY, activeKey: string, data: any) => {
    Modal.confirm({
      title: t('配置信息未保存'),
      wrapClassName: 'procedure-step-config-save',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('组件功能配置信息未保存，是否保存?'),
      footer() {
        return (
          <>
            <Space class='modal-footer'>
              <Button onClick={() => cancelModal()}>{t('取消')}</Button>
              <Button onClick={() => doNotSave(key, activeKey, data)}>{t('不保存')}</Button>
              <Button type='primary' onClick={() => saveAndJump(key, activeKey, data)}>
                {t('保存')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  // 点击节点
  const nodeClick = (key: KEY, activeKey: string, data: any) => {
    formatPainterModel.value = false;
    if (configFormRef.value?.hasChange && !isView.value) {
      nodeActiveKeys.value = cloneDeep(nodeActiveKeys.value);
      confirmSaveConfigModel(key, activeKey, data);
    } else {
      nodeActiveKeys.value = [key];
      activeNodeData.value = data?.data;
      if (data.type && Object.values(HAS_FORM_BUSINESS_NODE).includes(data.type)) {
        const loopGetBasicNodeKeysArray = loopGetBasicNodeKeys(data.data);
        templateActiveKeys.value = loopGetBasicNodeKeysArray?.length ? loopGetBasicNodeKeysArray : [data.data?.fieldId];
        getFormValueByKey(key);
        activeComponentType.value = data.type as HAS_FORM_BUSINESS_NODE;
      } else if (data.type && Object.values(NODE_TYPE).includes(data.type)) {
        templateActiveKeys.value = [key];
        getFormValueByKey(key);
        activeComponentType.value = getComponentTypeByKey(key) as NODE_TYPE;
      } else {
        getFormValueByKey(key);
        activeComponentType.value = data.type;
        const loopGetBasicNodeKeysArray = loopGetBasicNodeKeys(data.data);
        templateActiveKeys.value = loopGetBasicNodeKeysArray?.length ? loopGetBasicNodeKeysArray : [data.data?.fieldId];
      }
    }
    recordRef.value?.scrollToNode(key as string);
  };

  const templateDoNotSave = (target: any, key: KEY) => {
    configFormRef.value?.setHasChange(false);
    templateNodeClick(target, key);
    cancelModal();
  };

  const templateSaveAndJump = async (target: any, key: KEY) => {
    await configFormRef.value?.submitForm();
    templateNodeClick(target, key);
    cancelModal();
  };

  const templateConfirmSaveConfigModel = (target: any, key: KEY) => {
    Modal.confirm({
      title: t('提示'),
      wrapClassName: 'procedure-step-config-save',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否确定组件功能配置信息') + '?',
      footer() {
        return (
          <>
            <Space class='modal-footer'>
              <Button onClick={() => cancelModal()}>{t('取消')}</Button>
              <Button onClick={() => templateDoNotSave(target, key)}>{t('不确定')}</Button>
              <Button type='primary' onClick={() => templateSaveAndJump(target, key)}>
                {t('确定')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  // 点击模板节点
  const templateNodeClick = (target: any, key: KEY) => {
    if (!key) return;
    if (formatPainterModel.value) {
      const activeKey = nodeActiveKeys.value[0];
      const curComponentType = getComponentTypeByKey(activeKey);
      if (getComponentTypeByKey(key) !== curComponentType) {
        if (curComponentType === 'HANDLE_SUBMIT_SIGN') {
          message.error(`${t('请选择')}${t('手写提交签名')}${t('组件')}`);
          return;
        }
        if (curComponentType === 'HANDLE_REVIEW_SIGN') {
          message.error(`${t('请选择')}${t('手写复核签名')}${t('组件')}`);
          return;
        }
        if (curComponentType === 'SUBMIT_SIGN') {
          message.error(`${t('请选择')}${t('电子提交签名')}${t('组件')}`);
          return;
        }
        if (curComponentType === 'REVIEW_SIGN') {
          message.error(`${t('请选择')}${t('电子复核签名')}${t('组件')}`);
          return;
        }
        message.error(`${t('请选择')}${NODE_INFO[curComponentType]?.componentName}${t('组件')}`);
        return;
      } else {
        const curNode = findItemByAttr(nodeList.value, 'fieldId', key);
        const values = configList.value.find(item => item.fieldId === activeKey)?.configInfo;
        if (isEmpty(values)) {
          message.error(`${t('请先配置当前组件')}`);
          return;
        }
        const item = configList.value.find(item => item.fieldId === key);
        if (item) {
          item.configInfo = values;
        } else {
          configList.value.push({
            fieldId: key,
            configInfo: values,
            componentId: curNode?.id,
          });
        }
        setRecordClassByField(key);
      }
      return;
    }

    if (configFormRef.value?.hasChange && !isView.value) {
      nodeActiveKeys.value = cloneDeep(nodeActiveKeys.value);
      templateActiveKeys.value = cloneDeep(templateActiveKeys.value);
      templateConfirmSaveConfigModel(target, key);
    } else {
      activeNodeData.value = findItemByAttr(nodeList.value, 'fieldId', key);
      nodeActiveKeys.value = [key];
      templateActiveKeys.value = [key];
      getFormValueByKey(key);
      activeComponentType.value = getComponentTypeByKey(key) as NODE_TYPE;
      if (
        activeNodeData.value?.componentType &&
        Object.values(HAS_FORM_BUSINESS_NODE).includes(activeNodeData.value?.componentType)
      ) {
        activeComponentType.value = activeNodeData.value.componentType as HAS_FORM_BUSINESS_NODE;
      }
    }
    setTimeout(() => {
      nodeListRef.value?.scrollTo(key as string);
    }, 0);
  };

  // 点击取消 form
  const cancelForm = () => {
    nodeActiveKeys.value = [];
    templateActiveKeys.value = [];
    activeComponentType.value = null;
    formatPainterModel.value = false;
  };

  onMounted(async () => {
    await getNodeList();
    getComponentConfig();
  });
  return {
    configFormRef,
    nodeList,
    fileContent,
    recordRef,
    templateActiveKeys,
    nodeActiveKeys,
    activeComponentType,
    initFormValue,
    configList,
    activeNodeData,
    configFormKey,
    formatPainterModel,
    showFormatPainter,
    handleClickFormatPainter,
    nodeClick,
    templateNodeClick,
    cancelForm,
    setRecordClassByConfig,
    setRecordClassByField,
    removeRecordClassByField,
  };
};
