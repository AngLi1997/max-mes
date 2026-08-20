import { DATE_COMPONENT, NUMBER_COMPONENT, SELECT_COMPONENT, TEXT_COMPONENT } from '@/components/Record/NodeList/enum';
import { OPERATION } from '@/pages/TemplateEdit/enum';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { storeToRefs } from 'pinia';
import { onUnmounted, ref } from 'vue';
import { ComponentNode, Record, StyleEnum } from '../../../components/Record';
import { CHECK_FORMULA, COMPUTE_FORMULA, ERROR_MESSAGE, NODE_MATH } from '../enum';
import { useChangeStatus } from '../store/useChangeStatus';
import { useCheckComponent } from '../store/useCheckComponent';
import { FormulaParsesType } from '../type';
import { ModalConfirm } from './useTree';

export const useEDITOR = (useNode: any, show: boolean = false) => {
  const statusStore = useChangeStatus();
  const { changeStatus } = statusStore;
  // const { CHANGE_STATUS } = storeToRefs(statusStore)
  const { SETCOMPONENT, GETCOMPONENT, CURRENT_COMPONENT, formKey } = useNode;
  const store = useCheckComponent();
  const { setFormulaParses, endCheck } = store;
  const { CHECK_STATUS, CURRENT_FORMULA, formulaParses } = storeToRefs(store);
  const EDITOR_INSTANCE = ref<typeof Record>(Record);
  const ACTIVEKEYS = ref([]);
  const route = useRoute();
  const IS_SHOW = computed(() => {
    return route.params.record_type === OPERATION.SHOW;
  });

  const NODE_ACTIVES = ref<string[]>([]);

  const INIT_CONTENT = (VAL: any, pattern: number) => {
    NODE_ACTIVES.value = [];
    let content = VAL.fileContent || '';
    if (content.indexOf('<!-- remove_header_flag -->') < 0) {
      // !!!不可以换行,会被编辑器识别添加p标签
      content = `<!-- remove_header_flag -->${
        getPageNo(
          VAL.docxHeader?.headerPrimary?.content,
          VAL.docxHeader?.headerPrimary?.pageCodeHorizontalAlignment,
          true,
        ) || ''
      }<!-- remove_header_flag -->${content}<!-- remove_footer_flag -->${
        getPageNo(
          VAL.docxFooter?.footerPrimary?.content,
          VAL.docxFooter?.footerPrimary?.pageCodeHorizontalAlignment,
          false,
        ) || ''
      }<!-- remove_footer_flag -->`;
    }
    EDITOR_INSTANCE.value.setContent(content || ' ', { pattern });
  };

  const getPageNo = (str: string, style: number, flag: boolean) => {
    if (!str) {
      return;
    }
    if (str != '' && str.indexOf('{@pageNumber}') > 0) {
      str = str.replace('{@pageNumber}', ``);
    }
    if (flag) {
      return str + '<hr class="fhhr" style="margin:5px 0;"/>';
    } else {
      return '<hr class="fhhr" style="margin:5px 0;"/>' + str;
    }
  };

  /**
   * @description 获取当前组件选中的参数组件
   * @returns
   */
  const getFormulaParsesIds = () => {
    return (formulaParses.value.map((item: FormulaParsesType) => item.target?.fieldId) as string[]) || [''];
  };

  /**
   * @description 设置节点样式
   * @param id
   */
  const setNodeStyle = (ids: string[], name: string = StyleEnum.formula) => {
    EDITOR_INSTANCE.value?.setNodesStyle(ids, name);
  };

  /**
   * @description 清除节点样式
   * @param id
   */
  const clearNodeStyle = (ids: string[], name: string = StyleEnum.param) => {
    if (ids.length === 0) return;
    EDITOR_INSTANCE.value?.removeNodeClass(ids, name);
  };

  /**
   * @description 获取组件类型
   * @param component
   */
  const getComponentType = (component: ComponentNode) => {
    if (component.componentType === 'CUSTOM_FIELD') {
      try {
        const detail = JSON.parse(component.componentDetail!);
        return detail?.dataType;
      } catch (error) {
        return component.componentType;
      }
    }
    return component.componentType;
  };

  const CHECK_COMPONENT_TYPE = (component: ComponentNode) => {
    let result = false;
    const componentType = getComponentType(component);
    switch (CURRENT_COMPONENT.value.componentType) {
      case 'TEXT':
        if (CURRENT_FORMULA.value.formulaId === '9') {
          result = ![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        } else if (CURRENT_FORMULA.value.formulaId === '11') {
          result = ![...TEXT_COMPONENT, ...NUMBER_COMPONENT, ...DATE_COMPONENT, ...SELECT_COMPONENT].includes(
            componentType!,
          );
        } else {
          result = ![...TEXT_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        }
        break;
      case 'RADIO':
        if (CURRENT_FORMULA.value.formulaId === '9') {
          result = ![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        }
        break;
      case 'CHECKBOX':
        if (CURRENT_FORMULA.value.formulaId === '9') {
          result = ![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        }
        break;
      case 'DATE':
        result = ![...DATE_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        break;
      case 'TIME':
        result = !DATE_COMPONENT.includes(componentType!);
        break;
      case 'NUMBER':
        result = ![...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(componentType!);
        break;
      case 'HANDLE_SUBMIT_SIGN':
        result = false; //签名组件与任意组件关联
        break;
      case 'HANDLE_REVIEW_SIGN':
        result = false; //签名组件与任意组件关联
        break;
      case 'SUBMIT_SIGN':
        result = false; //签名组件与任意组件关联
        break;
      case 'REVIEW_SIGN':
        result = false; //签名组件与任意组件关联
        break;
      default:
        result = CURRENT_COMPONENT.value.componentType !== componentType;
        break;
    }
    return result;
  };

  /**
   * @description 清除之前选中参数的样式
   */
  const clearBeformParamStyle = () => {
    const parsesIds = getFormulaParsesIds();
    clearNodeStyle(parsesIds);
  };

  /**
   * @description 取消公式配置
   */
  const cancelCheck = () => {
    clearBeformParamStyle();
    SETCOMPONENT();
    endCheck();
    NODE_ACTIVES.value = [];
    setFormulaParses([]);
    changeStatus(false);
  };

  const NODE_CLICK = debounce((target: any, key: string) => {
    if (show !== undefined && show) {
      SETCOMPONENT(key);
      NODE_ACTIVES.value = [key];
      return;
    }
    if (!key || CURRENT_COMPONENT.value?.fieldId === key) {
      return;
    }

    if (CURRENT_COMPONENT.value?.fieldId === key) {
      CHECK_STATUS.value.status && message.error(t('不能选择同一个组件'));
      return;
    }

    const component = GETCOMPONENT(key);
    if (component === null) {
      return;
    }
    // 选择关联参数

    if (CHECK_STATUS.value.status) {
      // 判断是否计算公式
      if (!NODE_MATH.includes(CURRENT_FORMULA.value.formulaId)) {
        // 判断组件类型
        if (![...COMPUTE_FORMULA, ...NUMBER_COMPONENT, ...SELECT_COMPONENT].includes(component.componentType)) {
          return message.error(ERROR_MESSAGE[CURRENT_COMPONENT.value.componentType]);
        }
      } else if (CHECK_COMPONENT_TYPE(component)) {
        if (CURRENT_FORMULA.value.formulaId === '9') {
          return message.error(t('请选择数值、选择组件'));
        }
        if (CURRENT_FORMULA.value.formulaId === '11') {
          return message.error(t('请选择文字、数值、日期、选择组件'));
        }
        return message.error(ERROR_MESSAGE[CURRENT_COMPONENT.value.componentType]);
      }

      if (!CHECK_STATUS.value.outside) {
        endCheck(component);
        setNodeStyle([component.fieldId], StyleEnum.param);
      }
      // SETCOMPONENT(key);
    } else {
      const checkNode = () => {
        // 切换组件前先取消之前的组件节点状态
        clearBeformParamStyle();
        // 特定组件类型才能配置公式
        if (!CHECK_FORMULA.includes(component.componentType)) {
          return message.error(t('请选择数值、文字、时间、单选、多选、提交电子签名或日期组件进行公式配置'));
        }
        formKey.value++;
        NODE_ACTIVES.value = [key];
        SETCOMPONENT(key);
        changeStatus(false);
      };

      if (statusStore.CHANGE_STATUS.status) {
        return ModalConfirm(checkNode);
      }
      checkNode();
    }
  }, 100);

  const SAVE_COMPONENT_FORMULA = () => {
    setNodeStyle([CURRENT_COMPONENT.value.fieldId]);
    cancelCheck();
  };

  const setNodesStyle = (ids: string[]) => {
    EDITOR_INSTANCE.value?.setNodesStyle(ids);
  };

  onUnmounted(() => {
    endCheck();
    changeStatus(false);
  });

  const setNodeFormulaStyle = (component: ComponentNode | any) => {
    let list;
    if (component.formulaId !== void 0) {
      list = component.formulaDetailList.map((item: any) => {
        return {
          ...item,
          target: JSON.parse(item.detail),
        };
      });
      setFormulaParses(list, component);
    }
    const ids: string[] = (list.map((item: FormulaParsesType) => item.target?.fieldId) as string[]) || [''];

    // 设置公式参数样式
    ids.length > 0 && setNodeStyle(ids, StyleEnum.param);
  };

  const deleteParam = (target: ComponentNode['fieldId']) => {
    if (!target) return;
    clearNodeStyle([target!]);
  };

  const formulaChange = () => {
    clearBeformParamStyle();
  };

  watch(
    CURRENT_COMPONENT,
    (newval, oldval) => {
      if (oldval) {
        clearBeformParamStyle();
      }
      if (newval) {
        setNodeFormulaStyle(newval as ComponentNode);
      }
    },
    { immediate: true },
  );

  return {
    EDITOR_INSTANCE,
    INIT_CONTENT,
    IS_SHOW,
    NODE_CLICK,
    NODE_ACTIVES,
    cancelCheck,
    ACTIVEKEYS,
    setNodesStyle,
    SAVE_COMPONENT_FORMULA,
    deleteParam,
    clearNodeStyle,
    formulaChange,
  };
};
