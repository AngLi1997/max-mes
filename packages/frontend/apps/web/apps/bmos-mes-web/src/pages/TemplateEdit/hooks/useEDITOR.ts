import { getParameter } from '@/services';
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ComponentNode, PageBreak, Record } from '../../../components/Record';
import { MODAL_BUTTON, OPERATION } from '../enum';
import { EDITOR_NODE, INSERT_CONTENT, NODE_INFO_TYPE, createContent } from '../utils';

export const useEDITOR = (isClick: any) => {
  const route = useRoute();
  const contentValue = ref(''); // 编辑器内容

  // let NODE_I = 0;
  const spinClink = ref<boolean>(false);
  const EDITOR_INSTANCE = ref<any>();
  const RECORD_INSTANCE = ref<typeof Record>(Record);
  const NODE_ACTIVE_KEYS = ref<KEY[]>([]);
  const IS_SHOW = computed(() => {
    return route.params.record_type === OPERATION.SHOW;
  });

  const pageConfig = ref();
  const recordStyle: any = ref({
    fontSize: '16px',
    family: 'SimSun',
  });

  const SET_STYLE = (id: string) => {
    if (!EDITOR_INSTANCE.value) return false;
    EDITOR_INSTANCE.value.setNodeStyle(id);
  };

  // 设置默认字号、字体
  const setRecordStyle = async () => {
    // 获取该节点默认字号
    const { data: size } = await getParameter('mes.record.font.size');
    let fontSize = '16px';
    if (size.value >= 5 && size.value <= 72) {
      fontSize = size.value + 'px';
    }
    // 获取节点默认字体
    const { data: type } = await getParameter('mes.record.default.font');
    const family = type?.value;
    recordStyle.value = { fontSize, family: family || 'SimSun', size: size.value };
  };

  const EDITOR_INSERT = async (data: EDITOR_NODE): Promise<Boolean> => {
    if (!EDITOR_INSTANCE.value) return false;
    // NODE_I++;
    await setRecordStyle();
    const insertData = INSERT_CONTENT(
      data.componentType as NODE_INFO_TYPE,
      data.fieldId,
      data.componentNumber,
      data.componentDetail,
      data.componentName,
      recordStyle.value,
      EDITOR_INSTANCE.value.isTableBoxFlag,
    );
    // 插入组件
    EDITOR_INSTANCE.value.insertContent(insertData);
    if (MODAL_BUTTON.includes(data.componentType)) {
      // 按钮组件不要设置样式
      return true;
    }
    SET_STYLE(`${data.fieldId}`);
    return true;
  };

  const INIT_CONTENT = (VAL: any, config?: PageBreak) => {
    if (!VAL) return;
    pageConfig.value = config;
    if (IS_SHOW.value) {
      let newContent = VAL.fileContent;
      if (newContent && newContent.indexOf('remove_header_flag') < 0) {
        // 没有添加页眉页脚
        // !!!不可以换行,会被编辑器识别添加p标签
        newContent =
          newContent.indexOf('<!-- remove_header_flag -->') < 0
            ? `<!-- remove_header_flag -->${
                getPageNo(VAL.docxHeader?.headerPrimary?.content, true) || ''
              }<!-- remove_header_flag -->${newContent}<!-- remove_footer_flag -->${
                getPageNo(VAL.docxFooter?.footerPrimary?.content, false) || ''
              }<!-- remove_footer_flag -->`
            : VAL.fileContent;
      }
      contentValue.value = newContent.replaceAll('isClick', '');
      // EDITOR_INSTANCE.value.changeLayout(config?.pattern)
      RECORD_INSTANCE.value.setContent(newContent || '', config);
      spinClink.value = false;
      return;
    }
    let content = VAL.fileContent;
    if (!content) {
      contentValue.value = '';
      return EDITOR_INSTANCE.value && EDITOR_INSTANCE.value.changeLayout(config?.pattern);
    }
    const styleRegex = /<style\b[^<]*(?:(?!<\/style>)<[^<]*)*<\/style>/gi;
    const scriptRegx = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;
    const regxs = [styleRegex, scriptRegx];

    regxs.forEach(item => {
      content = VAL.fileContent.replace(item, '');
    });
    if (content.indexOf('<!-- remove_header_flag -->') < 0) {
      // !!!不可以换行,会被编辑器识别添加p标签
      content = `<!-- remove_header_flag -->${getPageNo(
        VAL.docxHeader?.headerPrimary?.content,
        true,
      )}<!-- remove_header_flag -->${content}<!-- remove_footer_flag -->${getPageNo(
        VAL.docxFooter?.footerPrimary?.content,
        false,
      )}<!-- remove_footer_flag -->`;
    }
    contentValue.value = content.replaceAll('isClick', '');
    EDITOR_INSTANCE.value.changeLayout(config?.pattern);
    EDITOR_INSTANCE.value.clearUndoManager();
  };

  const getPageNo = (str: string, flag: boolean) => {
    if (!str) {
      return '';
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

  const DELETE_NODE = (id: string) => {
    if (!id || !EDITOR_INSTANCE.value) return;
    if (contentValue.value?.indexOf(id) > -1) {
      EDITOR_INSTANCE.value.deleteNode(id);
    }
  };

  const EDIT_NODE = (b: EDITOR_NODE, c: EDITOR_NODE): boolean => {
    if (!EDITOR_INSTANCE.value) return false;
    try {
      if (!recordStyle.value) {
        setRecordStyle().then(() => {
          EDITOR_INSTANCE.value.editNode(
            b.fieldId,
            INSERT_CONTENT(
              c.componentType as NODE_INFO_TYPE,
              c.fieldId,
              c.componentNumber,
              c.componentDetail,
              recordStyle.value,
            ),
          );
        });
        return true;
      }
      EDITOR_INSTANCE.value.editNode(
        b.fieldId,
        INSERT_CONTENT(
          c.componentType as NODE_INFO_TYPE,
          c.fieldId,
          c.componentNumber,
          c.componentDetail,
          c.componentName,
          recordStyle.value,
        ),
      );
      return true;
    } catch (error) {
      console.log('==========error', error);

      throw false;
    }
  };

  const createComponentString = (data: ComponentNode & { fieldId: KEY }): string => {
    const res = createContent(
      data.componentType as NODE_INFO_TYPE,
      data.fieldId,
      data.componentNumber,
      data.componentDetail,
    );
    return EDITOR_INSTANCE.value?.convert(res);
  };

  const togglePattern = () => {
    isClick.value = true;
    pageConfig.value.pattern = [1, 0][pageConfig.value.pattern ?? 0];
    EDITOR_INSTANCE.value && EDITOR_INSTANCE.value.changeLayout(pageConfig.value.pattern);
  };

  return {
    contentValue,
    EDITOR_INSTANCE,
    EDITOR_INSERT,
    INIT_CONTENT,
    DELETE_NODE,
    RECORD_INSTANCE,
    IS_SHOW,
    NODE_ACTIVE_KEYS,
    createComponentString,
    SET_STYLE,
    EDIT_NODE,
    togglePattern,
    spinClink,
    pageConfig,
  };
};
