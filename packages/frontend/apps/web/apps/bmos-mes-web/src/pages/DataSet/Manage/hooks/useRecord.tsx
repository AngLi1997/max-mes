import { ALL_DYNAMIC_TABLE_NODE, NODE_TYPE, StyleEnum } from '@/components/Record';
import { reqProcessVersionRecordOrderReq, reqRecordListComponentReq } from '@/services';
import { Recordable } from '@bmos/components';
import { copyToClipboard, debounce, findItemByAttr } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { useCheckComponent } from './useCheckComponent';

export type UseRecordParams = {
  showRecord: Ref<number>;
  formRef: Ref<any>;
  pointTableRef: Ref<any>;
  batchModel: Ref<boolean>;
  isView: Ref<boolean>;
  hasChange: Ref<boolean>;
};
export const useRecord = ({ showRecord, formRef, pointTableRef, batchModel, isView, hasChange }: UseRecordParams) => {
  const recordRef = ref();

  // 组件list
  const curComponentList = ref<any[]>([]);
  const setRecordContent = async (id: string, versionId: string) => {
    if (!recordRef.value) return;
    await nextTick();
    try {
      const { data } = await reqRecordListComponentReq({
        itemId: id,
        recordVersionId: versionId,
      } as any);
      recordRef.value?.setContentByConfig(data);
      curComponentList.value = data.componentList;
      return Promise.resolve();
    } catch (error: any) {
      return Promise.reject(error);
    }
  };

  const getComponent = (fieldId: string) => {
    const current = findItemByAttr(curComponentList.value, 'fieldId', fieldId);
    return current;
  };

  const removeRecordAllDataBindClass = () => {
    recordRef.value?.clearAllNodesClass(StyleEnum.dataBind);
  };

  const getUniqueKey = (item: any) => {
    if (item?.reusable) {
      return item.recordItemId + '0';
    }
    return item?.recordItemId + item?.procedureStepId;
  };

  const curSelectRecord = ref<string>('');
  const recordList = ref<any[]>([]);
  const getRecordList = async () => {
    try {
      const formModal = formRef.value.getFormValues();
      if (!formModal?.processId || !formModal?.activeVersion) {
        showRecord.value = 0;
        message.warning(t('该工艺没有生效版本'));
        return;
      }
      const { data } = await reqProcessVersionRecordOrderReq(formModal?.processId, formModal?.activeVersion);
      recordList.value = data.map((item: any) => {
        return {
          ...item,
          uniqueKey: getUniqueKey(item),
        };
      });
      if (!recordList.value.length) {
        showRecord.value = 0;
        return;
      }
      const storageUniqueKey = sessionStorage.getItem('dataSetManageDetailUniqueKey');
      if (storageUniqueKey) {
        const storageRecord = recordList.value.find((item: any) => item.uniqueKey === storageUniqueKey);
        if (storageRecord) {
          await setRecordContent(storageRecord.recordItemId, storageRecord.recordVersionId);
          curSelectRecordItem.value = storageRecord;
          curSelectRecord.value = storageUniqueKey;
          const formFieldValue = formRef.value.getFormModelByField('datasetPointList');
          setRecordAllDataBindClassByConfig(cloneDeep(formFieldValue));
          return Promise.resolve();
        }
      }
      await setRecordContent(recordList.value[0].recordItemId, recordList.value[0].recordVersionId);
      curSelectRecordItem.value = recordList.value[0];
      curSelectRecord.value = getUniqueKey(recordList.value[0]);

      const formFieldValue = formRef.value.getFormModelByField('datasetPointList');
      setRecordAllDataBindClassByConfig(cloneDeep(formFieldValue));
    } catch (error: any) {
      // error.message && message.error(error.message);
    }
  };
  const curSelectRecordItem = ref<any>({});
  const handleClickRecord = async (uniqueKey: string) => {
    curSelectRecordItem.value = recordList.value?.find((item: any) => item.uniqueKey === uniqueKey);
    if (!curSelectRecordItem.value) {
      recordRef.value?.setContentByConfig({
        fileContent: null,
      });
      curComponentList.value = [];
      return Promise.reject();
    }
    curSelectRecord.value = uniqueKey;
    await setRecordContent(curSelectRecordItem.value.recordItemId, curSelectRecordItem.value.recordVersionId);
    const formFieldValue = formRef.value.getFormModelByField('datasetPointList');
    setRecordAllDataBindClassByConfig(cloneDeep(formFieldValue));
    return Promise.resolve();
  };
  const getComponentConfig = (componentStr: string) => {
    try {
      if (!componentStr) return {};
      return JSON.parse(componentStr);
    } catch (error) {
      return {};
    }
  };

  const getFields = (config: Recordable[]) => {
    const results: any[] = [];
    config?.forEach((item: any) => {
      const componentConfig = getComponentConfig(item.extra);
      const { fieldId } = componentConfig;
      if (fieldId) {
        results.push({
          fieldId,
          ...componentConfig.recordItem,
        });
      }
    });
    return [...new Set(results)];
  };

  const setRelationComponentDataBindTimer = ref<any>(null);
  const setRelationComponentDataBind = async (relationComponentDataBindList: any) => {
    try {
      setRelationComponentDataBindTimer.value && clearTimeout(setRelationComponentDataBindTimer.value);
      removeRecordAllDataBindClass();
      await nextTick();
      // 如果是复用
      let ids: string[] = [];
      ids = relationComponentDataBindList
        .filter((item: any) => {
          return getUniqueKey(item) === curSelectRecord.value;
        })
        .map((item: any) => item.fieldId)
        .filter((item: any) => item);
      setRelationComponentDataBindTimer.value = setTimeout(() => {
        recordRef.value?.setNodesStyle([...new Set(ids)], StyleEnum.dataBind);
      }, 500);
    } catch (error) {}
  };

  const setRecordAllDataBindClassByConfig = (config: Recordable[]) => {
    const fields = getFields(config);
    if (!fields.length) {
      removeRecordAllDataBindClass();
      return;
    }
    setRelationComponentDataBind(fields);
  };
  const setRecordDataBindClassByNewConfig = async (newConfig: Recordable[]) => {
    const fields = getFields(newConfig);
    try {
      setRelationComponentDataBindTimer.value && clearTimeout(setRelationComponentDataBindTimer.value);
      let ids: string[] = [];
      ids = fields
        .filter((item: any) => {
          return getUniqueKey(item) === curSelectRecord.value;
        })
        .map((item: any) => item.fieldId)
        .filter((item: any) => item);
      recordRef.value?.setNodesStyle([...new Set(ids)], StyleEnum.dataBind);
      hasChange.value = true;
    } catch (error) {}
  };

  const {
    CHECK_STATUS,
    endCheck,
    relationComponentIconClick,
    relationComponentAddClick,
    // @ts-ignore
  } = useCheckComponent(formRef, setRecordAllDataBindClassByConfig, batchModel, setRecordDataBindClassByNewConfig);

  const templateActiveKeys = ref<string[]>([]);
  const copyDataIndex = async (text: string) => {
    try {
      await copyToClipboard(text);
      message.success(`${text} ${t('复制成功')}`);
    } catch (error) {
      message.error(`${text} ${t('复制失败')}`);
    }
  };
  const copyKey = (key: string) => {
    try {
      const formValue = formRef.value.getFormValues();
      const result: any = [];
      if (formValue.datasetPointList?.length) {
        formValue.datasetPointList.forEach((item: any) => {
          const componentConfig = getComponentConfig(item.extra);
          if (componentConfig.fieldId === key && getUniqueKey(componentConfig.recordItem) === curSelectRecord.value) {
            result.push(item);
          }
        });
      }
      if (result.length > 1) {
        message.warning(t('该组件绑定了') + result.length + t('个数据点'));
      } else if (result.length === 1) {
        try {
          if (
            JSON.parse(result[0]?.extra).componentType === NODE_TYPE.RADIO ||
            JSON.parse(result[0]?.extra).componentType === NODE_TYPE.CHECKBOX
          ) {
            copyDataIndex('${(' + formValue.datasetKey + '.' + result[0].datasetPointKey + ')[][][][][]}');
          } else {
            copyDataIndex('${(' + formValue.datasetKey + '.' + result[0].datasetPointKey + ')[][][][]}');
          }
        } catch (error) {
          copyDataIndex('${(' + formValue.datasetKey + '.' + result[0].datasetPointKey + ')[][][][]}');
        }
      }
    } catch (error) {}
  };

  const scrollToPointTableNode = (key: string) => {
    try {
      if (!pointTableRef.value) return;
      pointTableRef.value?.scrollTo(key);
    } catch (error) {}
  };

  // 点击组件
  const templateNodeClick = debounce((target: any, key: string) => {
    if (!key) return;
    templateActiveKeys.value = [key];
    const component = getComponent(key);
    if (ALL_DYNAMIC_TABLE_NODE.includes(component?.componentType)) return;
    scrollToPointTableNode(key);
    if (key && isView.value) {
      copyKey(key);
      return;
    }
    if (key && CHECK_STATUS.status) {
      endCheck(component, {
        ...curSelectRecordItem.value,
      });
    }
  }, 50);

  const setNodeActiveByTarget = async (target: any) => {
    try {
      const { recordItem } = target;
      if (!recordItem) return;
      await handleClickRecord(getUniqueKey(recordItem));
      templateActiveKeys.value = [target.fieldId];
      recordRef.value?.scrollToNode(target.fieldId);
    } catch (error) {}
  };

  watch(
    () => showRecord.value,
    val => {
      if (val) {
        getRecordList();
      }
    },
    { immediate: true },
  );

  return {
    curSelectRecord,
    curSelectRecordItem,
    curComponentList,
    getRecordList,
    recordList,
    recordRef,
    handleClickRecord,
    templateActiveKeys,
    templateNodeClick,

    CHECK_STATUS,
    relationComponentIconClick,
    relationComponentAddClick,
    setNodeActiveByTarget,
    endCheck,
    getUniqueKey,
  };
};
