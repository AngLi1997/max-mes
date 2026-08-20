export const useEditor = (props: any, emit: any, utilsData: any) => {
  const clickElement = ref<Element>();
  const setRightClickElement = ref<Element>();
  const {
    toggleNodeStyle,
    setWidth,
    setAllDomSize,
    changeClickNodeListApi,
    deleteNodeApi,
    editNodeApi,
    getAddOffsetTop,
    editorAddTable,
    deleteNodeByClass,
  } = utilsData;
  const bodyElement = ref(); // 编辑器document
  const horizontalVerticalStyle = ref({}); // 横竖版

  const TEditorContentValue = computed({
    get: () => {
      return props.contentValue;
    },
    set: val => {
      emit('update:contentValue', val);
    },
  });
  const isTableBoxFlag = ref(false); // 当前点击的标签是否是表格标签

  const setNodeStyle = (id: string) => {
    const node = bodyElement.value.getElementById(id);
    if (!node) {
      return;
    }
    toggleNodeStyle(node, id);
  };
  // 切换横竖版
  const changeLayout = async (pattern: number = 0) => {
    horizontalVerticalStyle.value = await setWidth(pattern, bodyElement.value?.body);
  };
  // 给body下每个标签增加size,执行端展示用
  const setAllSize = () => {
    setAllDomSize(bodyElement.value.body);
  };

  const changeClickNodeList = (id: string, isFatherFlag: boolean) => {
    const node = bodyElement.value.getElementById(id);
    if (!node) {
      return;
    }
    changeClickNodeListApi(node, id, isFatherFlag);
  };

  // 删除组件
  const deleteNode = (id: string) => {
    const node = bodyElement.value.getElementById(id);
    if (!node) {
      return;
    }
    deleteNodeApi(node, id);
  };

  // 编辑组件(单选多选)
  const editNode = (oldId: string, newString: string) => {
    if (!oldId) return;
    if (!newString) deleteNode(oldId);
    const node = bodyElement.value.getElementById(oldId);
    if (!node) {
      return;
    }
    editNodeApi(node, oldId, newString);
  };

  // 定位
  const getNodeTop = (id: string) => {
    const node = bodyElement.value.getElementById(id);
    if (!node) {
      return;
    }
    let offsetTop = node.offsetTop;
    if (node.offsetParent.tagName != 'BODY') {
      offsetTop += getAddOffsetTop(node.offsetParent);
    }
    const componentDoc = bodyElement.value.getElementsByTagName('html')[0];
    if (offsetTop < 200) {
      componentDoc!.scrollTop = 0;
    } else {
      componentDoc!.scrollTop = offsetTop;
    }
  };

  // 添加表格组件
  const addTable = (fieldId: string, config: any) => {
    const node = bodyElement.value.getElementById(fieldId) || null;
    editorAddTable(node, fieldId, config);
  };

  // 获取所有内容
  const getAllContent = () => {
    return bodyElement.value.body.innerHTML;
  };

  // 删除页眉页脚线
  const deleteHeader = () => {
    deleteNodeByClass('.fhhr');
  };

  return {
    props,
    emit,
    TEditorContentValue,
    isTableBoxFlag,
    clickElement,
    bodyElement,
    horizontalVerticalStyle,
    setRightClickElement,
    setNodeStyle,
    changeLayout,
    setAllSize,
    changeClickNodeList,
    deleteNode,
    editNode,
    getNodeTop,
    addTable,
    getAllContent,
    deleteHeader,
  };
};
