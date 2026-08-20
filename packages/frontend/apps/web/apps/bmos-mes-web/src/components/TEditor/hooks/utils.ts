import { getParameter } from '@/services';
const pxToPoint = (m: number): number => {
  return (96 / 25.4) * m;
};
export const utils = (tinymce: any, props: any) => {
  const before = ref(''); // 上一个点击的元素id
  const styleMap = ref<Array<string>>([]);
  const defaultAheight = 1123; // 横版宽度
  const defaultAwidth = 794; // 竖版宽度
  // 编辑器中插入内容
  const insertContent = (str: string) => {
    tinymce?.activeEditor?.execCommand('mceInsertContent', false, str);
  };
  const toggleNodeStyle = (node: any, id: string) => {
    if (!before.value && !node) throw 'the property of id or style is required';
    if (!node) {
      console.log('The node does not exist,id:', node);
      return;
    }
    node.classList.add('isClick');
    styleMap.value.push(id);
    nextTick(() => {
      before.value && tinymce.activeEditor.dom?.removeClass(before.value, 'isClick');
      before.value && styleMap.value.splice(styleMap.value.indexOf(before.value), 1);
      before.value = id;
    });
  };
  // 设置横版竖版
  const setWidth = async (pattern: any, dom: HTMLElement) => {
    // 获取默认页面距
    const { data } = await getParameter('mes.record.margin');
    const padding = JSON.parse(data.value);
    const width =
      pattern === 1
        ? `${defaultAwidth - pxToPoint(padding.left) - pxToPoint(padding.right) + 1}px`
        : `${defaultAheight - pxToPoint(padding.left) - pxToPoint(padding.right) + 1}px`;
    const paddingLeft = (padding.left || 10) + 'mm';
    const paddingRight = (padding.right || 10) + 'mm';
    dom.style.width = width;
    dom.style.paddingLeft = paddingLeft;
    dom.style.paddingRight = paddingRight;
    dom.style.paddingTop = (padding.top || 10) + 'mm';
    dom.style.paddingBottom = (padding.bottom || 10) + 'mm';
    dom.style.boxSizing = 'content-box';
    return {
      width: `calc(${width} + ${paddingLeft} + ${paddingRight} + 10px)`,
    };
  };

  // 给所有标签添加size,方便执行端展示
  const setAllDomSize = (body: HTMLElement) => {
    const bodyWidth = body.offsetWidth; //body的宽度
    for (let i = 0; i < body.children.length; i++) {
      // 计算比例
      const size = ((body.children[i] as HTMLElement).offsetWidth / bodyWidth).toFixed(3);
      body.children[i].setAttribute('size', size);
    }
  };

  // 给选中节点添加点击状态
  const changeClickNodeListApi = (dom: HTMLElement, id: string, isFatherFlag: boolean = false) => {
    if (props.ctrlDown || isFatherFlag) {
      // 按下ctrl,进入多选状态
      if (dom.classList.contains('isClick') && !isFatherFlag) {
        // 已经选中,取消勾选
        tinymce.activeEditor.dom?.removeClass(id, 'isClick');
        styleMap.value.splice(styleMap.value.indexOf(id), 1);
      } else {
        // 没有勾选
        dom.classList.add('isClick');
        styleMap.value.push(id);
      }
    } else {
      // 清空之前的状态
      tinymce.activeEditor.dom?.removeClass(styleMap.value, 'isClick');
      // 没有进入多选状态
      nextTick(() => {
        styleMap.value = [];
        dom.classList.add('isClick');
        styleMap.value.push(id);
        before.value = id;
      });
    }
  };

  // 清空之前的所有点击状态
  const clearAllStyle = () => {
    tinymce.activeEditor.dom?.removeClass(styleMap.value, 'isClick');
  };

  // 删除组件
  const deleteNodeApi = (dom: HTMLElement, id: string) => {
    if (styleMap.value.includes(id)) {
      // 删除的组件为点击状态
      styleMap.value.splice(styleMap.value.indexOf(id), 1);
    }
    dom.parentNode?.removeChild(dom);
  };

  // 编辑组件
  const editNodeApi = (node: HTMLElement, oldId: string, newString: string) => {
    let newElement = document.createElement('div');
    newElement.innerHTML = newString;
    // 替换元素
    node.parentNode?.replaceChild(newElement.children[0], node);
  };

  // 计算距离顶部高度
  const getAddOffsetTop = (dom: HTMLElement) => {
    if (!dom.offsetParent) {
      return 0;
    }
    let offsetTop = 0;
    offsetTop = dom.offsetTop;
    if (dom.offsetParent.tagName != 'BODY') {
      offsetTop = offsetTop + getAddOffsetTop(dom.offsetParent as HTMLElement);
    }
    return offsetTop;
  };

  // 添加表格组件
  const editorAddTable = (node: HTMLElement, fieldId: string, { columns, rows, headers, rowHeight }: any) => {
    if (!node) {
      // 没有添加过这个表格组件
      let str = `<table style="border-collapse: collapse;" border="1" contenteditable="false" id="${fieldId}" name="${fieldId}"><tbody><tr style="height: ${rowHeight}px;">`;
      headers.forEach((item: any) => {
        str += `<td style="width: ${item.colWidth}px">${item.colName || '<br data-mce-bogus="1">'}</td>`;
      });
      str += '</tr>';
      for (let key = 0; key < rows; key++) {
        str += `<tr style="height: ${rowHeight}px;">`;
        for (let i = 0; i < columns; i++) {
          str += `<td><br data-mce-bogus="1"></td>`;
        }
        str += '</tr>';
      }
      str += '</tbody></table>';
      tinymce?.activeEditor?.execCommand('mceInsertContent', false, str);
    } else {
      // 已经有这个表格组件
      let str = `<tr style="height: ${rowHeight}px;">`;
      headers.forEach((item: any) => {
        str += `<td style="width: ${item.colWidth}px">${item.colName || '<br data-mce-bogus="1">'}</td>`;
      });
      str += '</tr>';
      for (let key = 0; key < rows; key++) {
        str += `<tr style="height: ${rowHeight}px;">`;
        for (let i = 0; i < columns; i++) {
          str += `<td><br data-mce-bogus="1"></td>`;
        }
        str += '</tr>';
      }
      node.children[0].innerHTML = str;
    }
  };

  // 清空撤回池
  const clearUndoManager = () => {
    tinymce.activeEditor.undoManager.clear();
  };

  const deleteNodeByClass = (className: string) => {
    tinymce.activeEditor.dom.select(className).forEach((element: any) => {
      tinymce.activeEditor.dom.remove(element);
    });
  };

  return {
    insertContent,
    toggleNodeStyle,
    setWidth,
    setAllDomSize,
    changeClickNodeListApi,
    deleteNodeApi,
    clearAllStyle,
    editNodeApi,
    getAddOffsetTop,
    styleMap,
    editorAddTable,
    clearUndoManager,
    deleteNodeByClass,
  };
};
