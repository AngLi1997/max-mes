import { OPERATION } from '@/pages/TemplateEdit/enum';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { ComponentNode, Record } from '../../../components/Record';
import { DATE } from '../enum';
import { useCheckComponent } from '../store/useCheckComponent';
import { EmitFn } from '../type';

export const useEDITOR = (useNode: any, emit?: EmitFn) => {
  const { SETCOMPONENT, GETCOMPONENT, CURRENT_COMPONENT } = useNode;
  const store = useCheckComponent();
  const { endCheck } = store;
  const { CHECK_STATUS } = storeToRefs(store);
  const EDITOR_INSTANCE = ref<typeof Record>(Record);
  const ACTIVEKEYS = ref([]);
  const route = useRoute();
  const IS_SHOW = computed(() => {
    return route.params.record_type === OPERATION.SHOW;
  });

  const NODE_ACTIVES = ref<string[]>([]);

  const INIT_CONTENT = (VAL: any, pattern: number) => {
    let content = VAL.fileContent;
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

  const cancelCheck = () => {
    SETCOMPONENT();
    NODE_ACTIVES.value = [];
  };

  const setNodeStyle = (id: string) => {
    EDITOR_INSTANCE.value?.setNodeStyle(id, {});
  };

  const CHECK_COMPONENT_TYPE = (component: ComponentNode) => {
    if (CURRENT_COMPONENT.value?.componentType === DATE) {
      if (component.componentType !== DATE) {
        return false;
      }
    }
    return true;
  };

  const NODE_CLICK = (target: any, key: string) => {
    if (!key || CURRENT_COMPONENT.value?.fieldId === key) {
      return;
    }

    if (CURRENT_COMPONENT.value?.fieldId === key) {
      CHECK_STATUS.value.status && message.error(t('不能选择同一个组件'));
      return;
    }

    SETCOMPONENT(key);
    const component = GETCOMPONENT(key);
    if (!CHECK_COMPONENT_TYPE(component)) {
      return message.error(t('请选择日期组件'));
    }

    NODE_ACTIVES.value = [key];
  };

  const SAVE_COMPONENT_FORMULA = () => {
    cancelCheck();
  };

  const setNodesStyle = (ids: string[]) => {
    EDITOR_INSTANCE.value?.setNodesStyle(ids);
  };

  return {
    EDITOR_INSTANCE,
    INIT_CONTENT,
    IS_SHOW,
    NODE_CLICK,
    NODE_ACTIVES,
    cancelCheck,
    ACTIVEKEYS,
    SAVE_COMPONENT_FORMULA,
    setNodesStyle,
  };
};
