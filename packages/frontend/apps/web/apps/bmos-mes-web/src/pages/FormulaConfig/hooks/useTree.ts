import { ComponentNode, StyleEnum } from '@/components/Record';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { Modal, message } from 'ant-design-vue';
import { recordItemDetail, recordListComponent } from '../../../services';

export const ModalConfirm = (callback: Function) => {
  Modal.confirm({
    title: t('公式配置信息未保存，是否退出'),
    content: t('切换记录项或退出将丢失未保存信息'),
    onOk() {
      try {
        callback();
        return Promise.resolve();
      } catch (error) {}
    },
  });
};

export const useTree = (useEDITOR: any, useNode: any, edit: boolean = true) => {
  const route = useRoute();
  const TREE_DATA = ref<readonly any[]>([]);
  const EXPANDED_KEYS = ref<Array<string | number>>([]);
  const SELECTED_KEYS = ref<KEY[]>([]);
  const CURRENT_NODE = ref();
  const INIT_TREE_DATA = () => {};
  const { INIT_CONTENT, setNodesStyle, clearNodeStyle, cancelCheck } = useEDITOR;
  const { SET_INST_NODE_LIST, CURRENT_COMPONENT, SETCOMPONENT } = useNode;
  const recordVersionId = ref<any>('');

  const GET_RECORD = async (id: string) => {
    try {
      // const res = route.params.implement !== '1' ? await recordRedactRecord({
      //   versionId: id,
      // }) : await recordManageRedactRecord({
      //   versionId: id,
      // });
      const res = await recordItemDetail({ recordVersionId: id });
      recordVersionId.value = id;
      if (res.code === 0) {
        const data = Object.freeze(res.data?.itemList || []);
        EXPANDED_KEYS.value = [id];
        TREE_DATA.value = Object.freeze([
          {
            name: res.data.recordName,
            children: data,
            itemId: id,
            notShowMoreBtn: true,
            selectable: false,
          },
        ]);
        SELECTED_KEYS.value = [data[0]?.itemId];
        setCurrent(data[0]);
        return data;
      }
    } catch (error) {
      throw 'get tree data failed';
    }
  };

  const filterFormulaNodes = (nodes: any[]) => {
    if (!nodes) return;
    const hasFormulaIds: string[] = [];
    nodes.forEach((item: any) => {
      if (item.formulaId !== void 0 && item.formulaId !== null) {
        hasFormulaIds.push(item.fieldId);
      }
    });
    if (hasFormulaIds.length === 0) return;
    setNodesStyle(hasFormulaIds);
  };

  const setCurrent = async (node: any) => {
    const { data } = await recordListComponent({
      itemId: node.itemId,
      recordVersionId: route.params.record_id || recordVersionId.value,
    });
    const component = {
      ...node,
      ...data,
    };
    CURRENT_NODE.value = component;
    const pattern = JSON.parse(component.pageConfig);
    INIT_CONTENT(component, pattern.pattern);
    SET_INST_NODE_LIST(component.componentList || []);
    filterFormulaNodes(component.componentList);
    SETCOMPONENT();
  };

  const TREE_SELECT = debounce((keys: KEY[], { node }: any) => {
    if (keys.length === 0) return;
    // if(CHANGE.status){
    //   ModalConfirm()
    // }
    if (!edit) {
      setCurrent(node.dataRef);
      SELECTED_KEYS.value = keys;
      return;
    }

    if (CURRENT_COMPONENT.value) {
      ModalConfirm(() => {
        setCurrent(node.dataRef);
        SELECTED_KEYS.value = keys;
        CURRENT_COMPONENT.value = void 0;
      });
    } else {
      setCurrent(node.dataRef);
      SELECTED_KEYS.value = keys;
      CURRENT_COMPONENT.value = void 0;
    }
  });

  const updateItemList = (list: ComponentNode[]) => {
    CURRENT_NODE.value.componentList = list;
    SET_INST_NODE_LIST(list || []);
    filterFormulaNodes(list);
  };

  const updateCurrentNodeComponents = async () => {
    if (!CURRENT_NODE.value) return;
    try {
      const res = await recordListComponent({
        itemId: CURRENT_NODE.value.itemId,
        recordVersionId: route.params.record_id || recordVersionId.value,
      });
      updateItemList(res.data.componentList);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const clearFormula = (node: ComponentNode) => {
    if (!node || !node.fieldId) return;
    const formuId = node.fieldId;
    clearNodeStyle([formuId], StyleEnum.formula);
    const fields = (node.formulaDetailList?.map(item => item.fieldId).filter(item => item) as string[]) || [];
    clearNodeStyle(fields, StyleEnum.param);
    updateCurrentNodeComponents();
    cancelCheck();
  };

  return {
    TREE_DATA,
    TREE_SELECT,
    INIT_TREE_DATA,
    CURRENT_NODE,
    EXPANDED_KEYS,
    SELECTED_KEYS,
    GET_RECORD,
    setCurrent,
    updateCurrentNodeComponents,
    clearFormula,
  };
};
