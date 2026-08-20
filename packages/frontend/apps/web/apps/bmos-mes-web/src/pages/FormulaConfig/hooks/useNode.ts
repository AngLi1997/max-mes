import { message } from 'ant-design-vue';
import { NODE, NodeType } from '../../../components/Record';
import RelativeRecord from '../RelativeRecord/index.vue';
import { MODAL_TYPE } from '../enum';
import { useCheckComponent } from '../store/useCheckComponent';
type MODALTYPE = keyof typeof MODAL_TYPE;
type NODE_A = NodeType &
  NODE & {
    used?: boolean;
    componentNumber?: number | string;
    componentDetail?: string;
  };

export const useNode = () => {
  const currentFormula = ref<any>({});
  const INST_NODE_LIST = ref<readonly NODE_A[]>([]);
  const CURRENT_COMPONENT = ref<any>();
  const IS_CHECK = ref(false);
  const MDAAL_RECORD_REF = ref<typeof RelativeRecord>();
  const { startCheck, endCheck } = useCheckComponent();
  const STATUS = reactive<Record<string, boolean>>({
    OPEN: false,
    MODAL: false,
  });
  const SET_INST_NODE_LIST = (value: any[] = []) => {
    INST_NODE_LIST.value = value;
  };
  const formKey = ref(1);

  /**
   * @description 递归查找获取当前组件
   * @param id
   * @param list
   */
  const recursionCurrentComponent = (id: string, nodes: NODE_A[]) => {
    let target: NODE_A | null = null;
    const find = (id: string, nodes: NODE_A[]) => {
      nodes.forEach(item => {
        if (item.fieldId === id) {
          target = item;
          return;
        }
        if (item.children && item.children.length > 0) {
          find(id, item.children);
        }
      });
    };
    find(id, nodes);
    return target;
  };

  const GETCOMPONENT = (id: string) => {
    const current = recursionCurrentComponent(id, INST_NODE_LIST.value);
    return current;
  };

  const SETCOMPONENT = (id: string) => {
    if (!id) CURRENT_COMPONENT.value = null;
    const current = GETCOMPONENT(id);
    CURRENT_COMPONENT.value = current;
  };

  const SAVE_COMPONENT_FORMULA = (formula: any) => {
    IS_CHECK.value = false;
    CURRENT_COMPONENT.value = null;
  };

  // 右键取消选择
  const contextmenuEvent = (event: MouseEvent) => {
    event.preventDefault();
    endCheck();
    window.removeEventListener('contextmenu', contextmenuEvent, true);
  };

  const handleAddParams = (formula_type: boolean, target: any, type: number, formula: any) => {
    startCheck(!!type, target?.key, formula_type);
    currentFormula.value = formula;
    if (type) {
      return (STATUS.MODAL = true);
    } else {
      window.addEventListener('contextmenu', contextmenuEvent);
    }
  };

  const dbConfirm = (tar: any) => {
    if (!tar || !tar.fieldId) {
      message.warning('请选择一个组件');
      return;
    }
    endCheck(tar);
    STATUS.MODAL = false;
  };

  const handleModalCancel = (type: MODALTYPE) => {
    STATUS.OPEN = false;
    endCheck();
  };

  return {
    INST_NODE_LIST,
    SET_INST_NODE_LIST,
    SETCOMPONENT,
    CURRENT_COMPONENT,
    currentFormula,
    IS_CHECK,
    SAVE_COMPONENT_FORMULA,
    handleAddParams,
    handleModalCancel,
    STATUS,
    dbConfirm,
    MDAAL_RECORD_REF,
    GETCOMPONENT,
    formKey,
  };
};
