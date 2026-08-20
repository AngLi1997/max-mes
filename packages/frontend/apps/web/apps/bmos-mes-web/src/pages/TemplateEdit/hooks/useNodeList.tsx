import { useCheckComponent } from '@/pages/FormulaConfig/store/useCheckComponent';
import { FormulaParsesType } from '@/pages/FormulaConfig/type';
import { cloneDeep, findItemByAttr, isEmpty } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { storeToRefs } from 'pinia';
import { onMounted, ref, watch } from 'vue';
import { ComponentNode, NODE, NodeDataType, NodeType, StyleEnum } from '../../../components/Record';
import {
  ALL_BUTTON_INFO,
  BUSINESS_NODE,
  BUTTON_CHILDREN,
  OUTPUT_BUTTON_INFO,
} from '../../../components/Record/NodeList/enum';
import { getRecordProductionId } from '../../../services';
import { MODAL_NODE } from '../enum';

type NODE_A = NodeType &
  NODE & {
    used?: Boolean;
    componentNumber?: number | string | null;
    componentDetail?: string;
    data?: any;
  };

export const useNodeList = (editor: any) => {
  let NODE_ID = ref<string[]>([]);
  // const { contentChange } = useContentChange(editor)
  const { EDITOR_INSERT, DELETE_NODE, IS_SHOW, RECORD_INSTANCE, NODE_ACTIVE_KEYS, INIT_CONTENT, EDIT_NODE } = editor;
  let max_number = 0;
  const INST_NODE_LIST = ref<NODE_A[]>([]);
  const ctrlDown = ref(false);

  const INST_ACTIVE_KEYS = ref<KEY[]>([]);
  const NODE_MAP = new Map();
  const openFlag = ref(true);
  const formKey = ref(1);

  const SET_INST_NODE_LIST = (value: any) => {
    max_number = value.maxNumber || 0;
    if (!value.componentList) {
      INST_NODE_LIST.value = [];
      return;
    }
    INST_NODE_LIST.value = value.componentList?.map((item: any) => {
      // item.used = true;
      return item;
    });
    filterFormulaNodes(INST_NODE_LIST.value);
  };

  const changeNodeStatus = () => {
    INIT_CONTENT('');
    INST_NODE_LIST.value.forEach(item => {
      item.used = false;
    });
  };

  const clearNodeListStatus = (nodeList: NODE_A[]) => {
    INIT_CONTENT('');
    nodeList.forEach(item => {
      item.used = false;
      if (item.children && item.children.length > 0) {
        clearNodeListStatus(item.children);
      }
    });
  };

  /**
   * @description 组件更新递归判断是否存在
   * @param node
   * @param nodeList
   */
  const contrastNodeDeep = (nodeList: NODE_A[]) => {
    const str = editor.EDITOR_INSTANCE.value?.getAllContent();

    nodeList.forEach(item => {
      const is_del = str.indexOf(item.fieldId) < 0;
      if (is_del) {
        item.used = false;
      } else {
        item.used = true;
      }
      // 删除节点选中效果
      if (!item.used) {
        if (NODE_ACTIVE_KEYS.value.indexOf(item.fieldId) >= 0) {
          NODE_ACTIVE_KEYS.value.splice(NODE_ACTIVE_KEYS.value.indexOf(item.fieldId), 1);
        }
      }
      // if (MODAL_NODE.includes(item.componentType!)) {
      //   const component_str = createComponentString(item);
      //   if (str.indexOf(component_str) < 0) {
      //     if (item.componentType == 'RADIO' || item.componentType == 'CHECKBOX') {
      //       // createComponentString(item)会把radio变为span,导致无法匹配,删除原单选组件
      //       return;
      //     }
      //     item.used = false;
      //     DELETE_NODE(item.fieldId);
      //     return;
      //   }
      // }
      if (item.children && item.children.length > 0) {
        contrastNodeDeep(item.children);
      }
    });
  };

  /**
   * @description 组件更新判断是否存在
   * @param node
   */
  const contrastNode = () => {
    nextTick(() => {
      contrastNodeDeep(INST_NODE_LIST.value);
    });
  };

  const ADD_NODE = (node: NODE_A) => {
    const key = NODE_ID.value.pop();
    if (!key) {
      getNodeIdAndCallback(node, ADD_NODE);
      return;
    }
    max_number++;
    INST_NODE_LIST.value.push({
      ...node,
      used: false,
      fieldId: key!,
      componentNumber: max_number,
      componentDetail: node.componentDetail ? JSON.stringify(node.componentDetail) : void 0,
    });
    // EDITOR_INSERT(node);
  };

  // 添加自定义字段
  const ADD_CUSTOM_FIELD_NODE = async (node: NODE_A, currentNode: NODE_A) => {
    let key = NODE_ID.value.pop();
    if (!key) {
      await getNodeId();
      key = NODE_ID.value.pop();
    }
    if (currentNode.componentType === 'CUSTOM_FIELD_BUTTON') {
      let parent: any = null;
      INST_NODE_LIST.value.forEach(item => {
        if (item.children && item.children.length > 0) {
          item.children.forEach(child => {
            if (child.fieldId === currentNode.fieldId) {
              parent = item;
              return;
            }
            if (child.children && child.children.length > 0) {
              child.children.forEach((option: any) => {
                if (option.fieldId === currentNode.fieldId) {
                  parent = child;
                }
              });
            }
          });
        }
      });
      if (parent) {
        const index = parent.children.findLastIndex((child: any) => child.componentType === 'CUSTOM_FIELD_BUTTON');
        index > -1 &&
          max_number++ &&
          parent.children.splice(index, 0, {
            ...node,
            fieldId: NODE_ID.value.pop()!,
            used: false,
            componentNumber: max_number,
            // @ts-ignore
            componentName: node.componentDetail.fieldName,
            componentDetail: JSON.stringify(node.componentDetail),
          });
      }
    }
    if (currentNode.componentType === 'CUSTOM_FIELD') {
      // @ts-ignore
      currentNode.data.componentName = node.componentDetail.fieldName;
      currentNode.data.componentDetail = node.componentDetail;
      nodeEditClick(currentNode.data, false);
    }
  };
  // 添加自定义扩展表格
  const ADD_EDA_DYNAMIC_TABLE_NODE = async (node: NODE_A, currentNode: NODE_A) => {
    let key = NODE_ID.value.pop();
    if (!key) {
      await getNodeId();
      key = NODE_ID.value.pop();
    }
    if (currentNode.componentType === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON') {
      let parent: any = null;
      INST_NODE_LIST.value.forEach(item => {
        if (item.children && item.children.length > 0) {
          item.children.forEach(child => {
            if (child.fieldId === currentNode.fieldId) {
              parent = item;
              return;
            }
            if (child.children && child.children.length > 0) {
              child.children.forEach((option: any) => {
                if (option.fieldId === currentNode.fieldId) {
                  parent = child;
                }
              });
            }
          });
        }
      });
      if (parent) {
        const index = parent.children.findLastIndex(
          (child: any) => child.componentType === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON',
        );
        index > -1 &&
          max_number++ &&
          parent.children.splice(index, 0, {
            ...node,
            fieldId: NODE_ID.value.pop()!,
            used: false,
            componentNumber: max_number,
            componentDetail: JSON.stringify(node.componentDetail),
          });
      }
    }
    if (currentNode.componentType === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE') {
      currentNode.data.componentDetail = node.componentDetail;
      nodeEditClick(currentNode.data, false, false);
      if (currentNode.used) {
        try {
          if (!node.componentDetail) {
            return;
          }
          const { rowNum, tableList, rowHeight } = node.componentDetail as any;
          if (!rowNum || !tableList) {
            return;
          }
          editor.EDITOR_INSTANCE.value.addTable(currentNode.fieldId, {
            columns: tableList.length,
            rows: rowNum,
            rowHeight,
            headers: tableList.map((item: any) => {
              return {
                colName: item.colName,
                colWidth: item.colWidth,
              };
            }),
          });
        } catch (error) {
          //
        }
      }
    }
  };

  // 构建业务子组件数据
  const contrastChildrenData = (nodes: NODE_A[], useMaxNumber: boolean = true) => {
    nodes.forEach((item: NODE_A) => {
      const key = NODE_ID.value.pop();
      const flag = !ALL_BUTTON_INFO.includes(item.componentType || '') && useMaxNumber;
      if (flag) {
        max_number++;
      }
      Object.assign(item, {
        used: false,
        fieldId: key!,
        componentNumber: OUTPUT_BUTTON_INFO[item.componentType || ''] ? '' : flag ? max_number : 1,
        componentDetail: item.componentDetail ? JSON.stringify(item.componentDetail) : void 0,
      });
      if (item.children && item.children.length > 0) {
        contrastChildrenData(item.children);
      }
    });
    return nodes;
  };

  // 添加业务组件
  const addBusinessNode = (node: NODE_A) => {
    switch (node.componentType) {
      case 'BUSINESS_PRODUCT_INFO':
        contrastChildrenData(node.children!);
        break;
      case 'EQUIPMENT_INFO':
        contrastChildrenData(node.children!);
        break;
      case 'CLEAN_INFO':
        contrastChildrenData(node.children!);
        break;
      case 'MATERIAL_INFO':
        contrastChildrenData(node.children!);
        break;
      case 'EQUIPMENT_DATA_DRAW_LIST':
        contrastChildrenData(node.children!);
        break;
      default:
        contrastChildrenData(node.children!, false);
        break;
    }
    const key = NODE_ID.value.pop();
    if (!key) {
      getNodeIdAndCallback(node, addBusinessNode);
      return;
    }
    INST_NODE_LIST.value.push({
      ...node,
      used: false,
      fieldId: key!,
      // componentNumber: max_number,
      componentDetail: node.componentDetail ? JSON.stringify(node.componentDetail) : void 0,
    });
  };

  // 根据fieldId查找父节点并根据type添加一组数据
  const addGroupData = (data: any, groupData: BUSINESS_NODE & { children: any }) => {
    let parent: any = null;
    INST_NODE_LIST.value.forEach(item => {
      if (item.children && item.children.length > 0) {
        item.children.forEach(child => child.fieldId === data.key && (parent = item));
      }
    });
    if (parent && parent.children) {
      groupData.children && contrastChildrenData(groupData.children);
      if (!NODE_ID.value.length) {
        getNodeIdAndCallback(data, addBusinessGroup);
        return;
      }
      // 多个按钮时，需找到当前添加的按钮，并将数据插入到其前面,componentNumber为当前按钮之前组件的componentNumber+1
      const index = parent.children.findIndex((child: any) => child.componentType === data.type);
      index > -1 &&
        parent.children.splice(index, 0, {
          ...groupData,
          componentNumber: OUTPUT_BUTTON_INFO[groupData?.componentType || '']
            ? ''
            : groupData.children // 产出按钮不显示编号
            ? (ALL_BUTTON_INFO.includes(parent.children[index - 1]?.componentType)
                ? 1
                : Number(parent.children[index - 1]?.componentNumber) + 1) || 1 // 取当前按钮之前的组件的componentNumber+1(如果当前按钮之前也是按钮，取1)
            : ++max_number, // 如果没有子节点，说明它是基础组件，componentNumber为max_number
        });
    }
  };

  // 业务组件添加组
  const addBusinessGroup = (node: NODE_A) => {
    const key = NODE_ID.value.pop();
    if (!key) {
      getNodeIdAndCallback(node, addBusinessGroup);
      return;
    }
    const groupData: BUSINESS_NODE & {
      used?: boolean;
      fieldId: any;
      componentNumber: any;
      componentDetail: any;
      children: any;
    } = {
      ...cloneDeep(BUTTON_CHILDREN[node.type]),
      used: false,
      fieldId: key!,
      componentDetail: node.componentDetail ? JSON.stringify(node.componentDetail) : void 0,
    };
    addGroupData(node, groupData);
  };

  // 业务组件复制组
  const copyBusinessGroup = async (node: any, type: string) => {
    if (NODE_ID.value.length < 20) {
      await getNodeId();
    }
    const key = NODE_ID.value.pop();
    if (!key) {
      getNodeIdAndCallback(node, copyBusinessGroup, type);
      return;
    }
    let newNodeInfo = cloneDeep(BUTTON_CHILDREN[type]) as any;
    if (node.data.children.length > newNodeInfo.children.length) {
      node.data.children.map((item: any) => {
        if (item.componentType == 'CUSTOM_FIELD') {
          let componentDetail = '';
          if (item.componentDetail && typeof item.componentDetail == 'string') {
            componentDetail = JSON.parse(item.componentDetail);
          } else {
            componentDetail = item.componentDetail;
          }
          newNodeInfo.children.splice(newNodeInfo.children.length - 1, 0, { ...item, componentDetail, id: null });
        }
      });
    }
    const groupData: BUSINESS_NODE & {
      used?: boolean;
      fieldId: any;
      key: any;
      componentNumber: any;
      componentDetail: any;
      children: any;
    } = {
      ...newNodeInfo,
      used: false,
      fieldId: key!,
      componentDetail: node.componentDetail ? JSON.stringify(node.componentDetail) : void 0,
    };
    let btnNode = null;
    INST_NODE_LIST.value.forEach(item => {
      let isThisFlag = false;
      if (item.children && item.children.length > 0) {
        item.children.forEach(child => {
          if (child.fieldId === node.key) {
            isThisFlag = true;
          }
          if (child.componentType === type && isThisFlag) {
            btnNode = { ...child, key: child.fieldId, type: child.componentType };
          }
        });
      }
    });
    addGroupData(btnNode, groupData);
  };

  // 递归查找节点,找到节点后取消递归，返回节点，第三个参数为回调函数，在找到节点后执行
  const findNode = (id: string, nodes: NODE_A[], callback?: Function) => {
    let target: NODE_A | null = null;
    const find = (id: string, nodes: NODE_A[]) => {
      nodes.forEach(item => {
        if (item.fieldId === id) {
          target = item;
          if (callback) {
            callback(item, nodes);
          }
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
  // 记录中的组件点击
  const ueditorWrapClick = (key: string, ctrlDown: boolean) => {
    const item: NODE_A | null = findNode(key, INST_NODE_LIST.value);
    if (item?.used) {
      // 没有按下ctrl
      if (!ctrlDown) {
        if (NODE_ACTIVE_KEYS.value.length <= 1 && NODE_ACTIVE_KEYS.value[0] == item.fieldId) {
          NODE_ACTIVE_KEYS.value = [key];
          return;
        }
        NODE_ACTIVE_KEYS.value = [key];
        return;
      }
      // 按下了ctrl
      if (NODE_ACTIVE_KEYS.value.indexOf(key) < 0) {
        NODE_ACTIVE_KEYS.value.push(key);
      } else {
        NODE_ACTIVE_KEYS.value.splice(NODE_ACTIVE_KEYS.value.indexOf(key), 1);
      }
    }
  };

  const NODE_CLICK = async (key: string, keys: [], { data }: NodeDataType) => {
    if (IS_SHOW.value && RECORD_INSTANCE.value) {
      NODE_ACTIVE_KEYS.value = keys;
      return;
    }
    const item: NODE_A | null = findNode(data.fieldId!, INST_NODE_LIST.value);
    // 判断节点是否使用过
    if (item?.used) {
      editor.EDITOR_INSTANCE.value?.changeClickNodeList(data.fieldId);
      NODE_ACTIVE_KEYS.value = keys;
      editor.EDITOR_INSTANCE.value.getNodeTop(data.fieldId);
      return;
    }
    await EDITOR_INSERT(data);
    if (item) {
      item.used = true;
    }
    editor.EDITOR_INSTANCE.value?.changeClickNodeList(data.fieldId);
    NODE_ACTIVE_KEYS.value = keys;
  };

  const FATHER_NODE_CLICK = (nodeList: any) => {
    if (!nodeList.length) {
      return;
    }
    NODE_ACTIVE_KEYS.value = [];
    editor.EDITOR_INSTANCE.value?.clearAllStyle();
    nodeList.map(async (item: any) => {
      if (item?.used) {
        NODE_ACTIVE_KEYS.value.push(item.fieldId);
        await editor.EDITOR_INSTANCE.value?.changeClickNodeList(item.fieldId, true);
      }
    });
  };

  // 递归删除所有的子节点 删除调用DELETE_NODE方法
  const deleteChildrenNode = (nodes: NODE_A[]) => {
    nodes.forEach(item => {
      if (item.children && item.children.length > 0) {
        deleteChildrenNode(item.children);
      }
      if (item.used) {
        DELETE_NODE(item.fieldId);
      }
    });
  };

  const DELETE_LIST_NODE = (id: any) => {
    try {
      findNode(id.data.fieldId, INST_NODE_LIST.value, (item, nodes) => {
        const i = nodes.findIndex(node => node.fieldId === item.fieldId);
        if (i >= 0) {
          const [delNode] = nodes.splice(i, 1);
          if (delNode.children && delNode.children.length > 0) {
            deleteChildrenNode(delNode.children);
          } else {
            DELETE_NODE(delNode.fieldId);
          }
        }
      });
    } catch (error) {
      throw 'delete node failed';
    }
  };

  const getNodeId = async () => {
    try {
      const res = await getRecordProductionId();
      if (res.code === 0) {
        NODE_ID.value = res.data;
        return;
      }
      message.error(res.message);
      throw 'get NODE_ID failed';
    } catch (error: any) {
      message.error(error?.message);
      throw 'get NODE_ID failed';
    }
  };

  // NODE_ID用完后,重新执行获取NODE_ID，并执行callback
  const getNodeIdAndCallback = async (node: any, callback: Function, type: string = '') => {
    await getNodeId();
    callback(node, type);
  };

  // 向编辑器中添加表格
  const addTable = (fieldId: string, columns: number, rows: number, headers: Array<string>) => {
    editor.EDITOR_INSTANCE.value?.addTable(fieldId, { columns, rows, headers });
  };

  const nodeEditClick = (
    data: NODE_A & { componentDetail: Array<any> | string },
    needCheck = true,
    needEditNode = true,
  ) => {
    let target: NODE_A;
    if (NODE_MAP.has(data.fieldId)) {
      target = NODE_MAP.get(data.fieldId);
    } else {
      target = findNode(data.fieldId, INST_NODE_LIST.value);
      NODE_MAP.set(data.fieldId, target);
    }
    const str = JSON.stringify(data.componentDetail);
    data.componentDetail = str;
    if (target.componentDetail === str && needCheck) return;
    try {
      let res = true;
      if (data.used && needEditNode) {
        res = EDIT_NODE(target, data);
      }
      target.componentDetail = str;
      return res;
    } catch (error) {
      return false;
    }
  };

  const getNumber = () => {
    return max_number;
  };

  const store = useCheckComponent();
  const { setFormulaParses } = store;
  const { formulaParses } = storeToRefs(store);
  const currentComponent = ref<any>();
  const setNodesStyle = (ids: string[]) => {
    RECORD_INSTANCE.value?.setNodesStyle?.(ids);
  };
  const filterFormulaNodes = (nodes: any[]) => {
    if (!nodes) return;
    const hasFormulaIds: string[] = [];
    const loop = (nodes: any[]) => {
      nodes.forEach((item: any) => {
        if (!isEmpty(item.formulaId)) {
          hasFormulaIds.push(item.fieldId);
        }
        if (item.children && item.children.length > 0) {
          loop(item.children);
        }
      });
    };
    loop(nodes);
    if (hasFormulaIds.length === 0) return;
    setNodesStyle(hasFormulaIds);
  };

  const setCurrentComponent = (key: string) => {
    const item = findItemByAttr(INST_NODE_LIST.value, 'fieldId', key);
    currentComponent.value = item;
    formKey.value++;
  };
  const recordNodeClick = (_target: any, key: string) => {
    if (!key) return;
    setCurrentComponent(key);
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
  const setFormulaNodeStyle = (ids: string[], name: string = StyleEnum.formula) => {
    RECORD_INSTANCE.value?.setNodesStyle(ids, name);
  };

  /**
   * @description 清除节点样式
   * @param id
   */
  const clearNodeStyle = (ids: string[], name: string = StyleEnum.param) => {
    if (ids.length === 0) return;
    RECORD_INSTANCE.value?.removeNodeClass(ids, name);
  };
  /**
   * @description 清除之前选中参数的样式
   */
  const clearBeforeParamStyle = () => {
    const parsesIds = getFormulaParsesIds();
    clearNodeStyle(parsesIds);
  };
  const setNodeFormulaStyle = (component: ComponentNode | any) => {
    let list;
    if (component.formulaId !== void 0) {
      list = component.formulaDetailList.map((item: any) => {
        return {
          ...item,
          target: JSON.parse(item.detail),
        };
      });
      setFormulaParses(list);
    }
    const ids: string[] = (list.map((item: FormulaParsesType) => item.target?.fieldId) as string[]) || [''];

    // 设置公式参数样式
    ids.length > 0 && setFormulaNodeStyle(ids, StyleEnum.param);
  };

  watch(
    currentComponent,
    (newVal, oldVal) => {
      if (IS_SHOW.value) {
        if (oldVal) {
          clearBeforeParamStyle();
        }
        if (newVal) {
          setNodeFormulaStyle(newVal as ComponentNode);
        }
      }
    },
    { immediate: true },
  );

  onMounted(() => {
    getNodeId();
  });

  watch(
    NODE_ACTIVE_KEYS,
    (val: KEY[]) => {
      INST_ACTIVE_KEYS.value = val;
    },
    { immediate: true },
  );

  return {
    INST_NODE_LIST,
    changeNodeStatus,
    clearNodeListStatus,
    contrastNode,
    SET_INST_NODE_LIST,
    ADD_NODE,
    ADD_CUSTOM_FIELD_NODE,
    max_number,
    NODE_CLICK,
    DELETE_LIST_NODE,
    INST_ACTIVE_KEYS,
    nodeEditClick,
    MODAL_NODE,
    getNumber,
    addBusinessNode,
    addBusinessGroup,
    ueditorWrapClick,
    NODE_ID,
    currentComponent,
    recordNodeClick,
    setCurrentComponent,
    FATHER_NODE_CLICK,
    openFlag,
    copyBusinessGroup,
    formKey,
    ctrlDown,
    ADD_EDA_DYNAMIC_TABLE_NODE,
    addTable,
    findNode,
  };
};
