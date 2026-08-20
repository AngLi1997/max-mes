import { t } from '@bmos/i18n';
import { cloneDeep } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { ComponentNode } from '../../../components/Record/NodeList/type';
import { BEHAVIOR } from '../enum';
import { FormulaParsesType } from '../type';

const endStatus = {
  status: false,
  outside: false,
  currentField: void 0,
  behavior: void 0,
};

let behavior_type = false;
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

export const useCheckComponent = defineStore('checkComponent', () => {
  const CHECK_STATUS = reactive({ ...endStatus });

  const CHECKED_COMPONENTS = ref<any[]>([]);

  const formulaParses = ref<FormulaParsesType[]>([]);

  let ADD_FORMULAS: FormulaParsesType | null;

  const CURRENT_FORMULA = ref<any>({});

  const startCheck = (isOut: boolean, currentField: any, type: boolean) => {
    let behavior: any = void 0;

    if (type) {
      const count = Date.now() + '';
      ADD_FORMULAS = {
        key: count,
        value: t('参数'),
      };
      currentField = count;
      behavior = BEHAVIOR;
    }
    behavior_type = true;
    Object.assign(CHECK_STATUS, {
      status: true,
      outside: isOut,
      currentField,
      behavior,
    });

    if (!isOut) {
      changeCursor();
      // document.body.style.cursor = 'url(/app/bmos-mes/ownhand32X32.ico) 2 4,auto !important';
    }
  };

  const setFormulaParses = (val: FormulaParsesType[], formula?: any) => {
    formulaParses.value = cloneDeep(val);
    formula && (CURRENT_FORMULA.value = formula);
  };

  const endCheck = (target?: ComponentNode) => {
    // document.body.style.cursor = 'auto';
    clearCursor();
    if (!target) Object.assign(CHECK_STATUS, endStatus);
    if (CHECK_STATUS.currentField === void 0) return;

    const item = formulaParses.value.find((item: FormulaParsesType) => item.key === CHECK_STATUS.currentField);

    if (!item && !ADD_FORMULAS) return;

    if (CHECK_STATUS.behavior === BEHAVIOR) {
      ADD_FORMULAS!.target = target;
      formulaParses.value.push(ADD_FORMULAS!);
      ADD_FORMULAS = null;
    } else {
      item!.target = target;
    }
    Object.assign(CHECK_STATUS, endStatus);
  };

  const RESET_CHECK_STATUS = () => {
    Object.assign(CHECK_STATUS, endStatus);
  };

  const addCheckedComponents = (component: FormulaParsesType) => {
    if (formulaParses.value.length > 99) {
      message.error(t('参数最多为100个'));
      return;
    }
    formulaParses.value.push(component);
  };

  const IS_INNER = computed(() => {
    return CHECK_STATUS.status && !CHECK_STATUS.outside;
  });

  const deleteFormulaParam = (key: string) => {
    formulaParses.value = formulaParses.value.filter(item => item.key !== key);
  };

  const deleteFormulaParamTarget = (key: string) => {
    formulaParses.value.forEach(item => {
      if (item.key === key) {
        item.target = void 0;
      }
    });
  };

  const setCurrentFormula = (formula: any) => {
    CURRENT_FORMULA.value = formula;
  };

  return {
    CHECK_STATUS,
    startCheck,
    endCheck,
    CHECKED_COMPONENTS,
    addCheckedComponents,
    formulaParses,
    setFormulaParses,
    IS_INNER,
    deleteFormulaParam,
    deleteFormulaParamTarget,
    RESET_CHECK_STATUS,
    setCurrentFormula,
    CURRENT_FORMULA,
  };
});
