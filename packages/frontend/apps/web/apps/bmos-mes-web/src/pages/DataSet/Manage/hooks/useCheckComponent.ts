import { ComponentNode } from '@/components/Record';
import { formInstance, Recordable } from '@bmos/components';
import { cloneDeep, isEmpty } from '@bmos/utils';

const endStatus = {
  status: false,
  row: void 0,
};

let cursorElement: HTMLStyleElement | undefined;
const changeCursor = () => {
  let styleElement = document.createElement('style');

  // 将样式规则添加到<style>元素中
  styleElement.textContent = `body { 
    cursor: url(/app/bmos-mes/ownhand32X32.ico) 2 4, auto !important;
  }
  body .formula-content .record-component:hover{ 
    cursor: url(/app/bmos-mes/ownhand32X32.ico) 2 4, auto !important; 
  }
  body .formula-content .radio-component:hover{ 
    cursor: url(/app/bmos-mes/ownhand32X32.ico) 2 4, auto !important; 
  }
  body .formula-content .checkbox-component:hover{ 
    cursor: url(/app/bmos-mes/ownhand32X32.ico) 2 4, auto !important; 
  }
  body .formula-content .record-component:hover{ 
    cursor: url(/app/bmos-mes/ownhand32X32.ico) 2 4, auto !important; 
  }`;

  // 将<style>元素添加到<head>中
  document.head.appendChild(styleElement);
  cursorElement = styleElement;
};

const clearCursor = () => {
  if (!cursorElement) return;
  document.head.removeChild(cursorElement);
  cursorElement = void 0;
};

export const useCheckComponent = (
  formRef: Ref<formInstance>,
  setRecordAllDataBindClassByConfig: (config: any) => void,
  batchModel: Ref<boolean>,
  setRecordDataBindClassByNewConfig: (config: any) => void,
) => {
  const CHECK_STATUS = reactive<{
    status: boolean;
    row: Recordable | undefined;
  }>({ ...endStatus });

  const startCheck = (row: Recordable) => {
    Object.assign(CHECK_STATUS, {
      status: true,
      row,
    });
    changeCursor();
  };

  const endCheck = (target?: ComponentNode, curSelectRecordItem?: any) => {
    if (!target) {
      Object.assign(CHECK_STATUS, endStatus);
      clearCursor();
      return;
    }
    if (!CHECK_STATUS.row) {
      Object.assign(CHECK_STATUS, endStatus);
      clearCursor();
      return;
    }
    if (curSelectRecordItem) {
      const { procedureStepId, reusable } = curSelectRecordItem;
      const fieldValue = {
        extra: JSON.stringify({
          id: target?.id,
          fieldId: target?.fieldId,
          componentName: target?.componentName,
          componentType: target?.componentType,
          componentNumber: target?.componentNumber,
          ...{ recordItem: curSelectRecordItem },
        }),
        fieldId: target?.fieldId,
        procedureStepId: reusable ? '0' : procedureStepId,
      };
      const formFieldValue = formRef.value?.getFormModelByField('datasetPointList');
      let newConfig: any = cloneDeep(formFieldValue || []);
      let newAddConfig: any = [];
      if (batchModel.value) {
        if (isEmpty(formFieldValue)) {
          newConfig = [
            {
              key: new Date().getTime(),
              ...fieldValue,
              name: `${target?.componentName} ${target?.componentNumber}`,
            },
          ];
          newAddConfig = newConfig;
          formRef.value.setFieldsValue({
            datasetPointList: newConfig,
          });
        } else {
          newConfig = [
            ...newConfig,
            {
              key: new Date().getTime(),
              ...fieldValue,
              name: `${target?.componentName} ${target?.componentNumber}`,
            },
          ];
          newAddConfig = [
            {
              key: new Date().getTime(),
              ...fieldValue,
              name: `${target?.componentName} ${target?.componentNumber}`,
            },
          ];
          formRef.value.setFieldsValue({
            datasetPointList: newConfig,
          });
        }
      } else {
        if (isEmpty(formFieldValue)) {
          newConfig = [
            {
              key: new Date().getTime(),
              ...fieldValue,
            },
          ];
          newAddConfig = newConfig;
          formRef.value.setFieldsValue({
            datasetPointList: newConfig,
          });
        } else {
          newConfig = newConfig.map((item: Recordable) => {
            if (item.id && item.id === CHECK_STATUS.row?.id) {
              item = {
                ...item,
                ...fieldValue,
              };
              newAddConfig = [item];
            }
            if (!isEmpty(item.key) && item.key === CHECK_STATUS.row?.key) {
              item = {
                ...item,
                ...fieldValue,
              };
              newAddConfig = [item];
            }
            return item;
          });
          formRef.value.setFieldsValue({
            datasetPointList: newConfig,
          });
        }
        Object.assign(CHECK_STATUS, endStatus);
        clearCursor();
      }
      // setRecordAllDataBindClassByConfig(newConfig);
      setRecordDataBindClassByNewConfig(newAddConfig);
    }
  };

  const RESET_CHECK_STATUS = () => {
    Object.assign(CHECK_STATUS, endStatus);
  };

  const deleteRelationComponent = (row: Recordable) => {
    try {
      const formFieldValue = formRef.value?.getFormModelByField('datasetPointList');
      let newConfig: any = cloneDeep(formFieldValue);
      newConfig = newConfig.map((item: Recordable) => {
        if (item.id && item.id === row.id) {
          item = {
            ...item,
            extra: void 0,
            fieldId: void 0,
            procedureStepId: void 0,
          };
        }
        if (!isEmpty(item.key) && item.key === row.key) {
          item = {
            ...item,
            extra: void 0,
            fieldId: void 0,
            procedureStepId: void 0,
          };
        }
        return item;
      });
      formRef.value.setFieldsValue({
        datasetPointList: newConfig,
      });
      setRecordAllDataBindClassByConfig(newConfig);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const relationComponentIconClick = (row: Recordable) => {
    deleteRelationComponent(row);
  };

  watch(
    () => batchModel.value,
    val => {
      if (!val) clearCursor();
    },
  );

  // 右键取消选择
  const contextmenuEvent = (event: MouseEvent) => {
    event.preventDefault();
    endCheck();
    window.removeEventListener('contextmenu', contextmenuEvent, true);
  };
  const relationComponentAddClick = (row: Recordable = {}) => {
    endCheck();
    startCheck(row);
    window.addEventListener('contextmenu', contextmenuEvent);
  };

  return {
    CHECK_STATUS,
    startCheck,
    endCheck,
    deleteRelationComponent,
    RESET_CHECK_STATUS,

    relationComponentIconClick,
    relationComponentAddClick,
  };
};
