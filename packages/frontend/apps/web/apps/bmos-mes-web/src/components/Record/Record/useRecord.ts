import { useConfig } from '@/stores/config';
import { EmitFn } from '@bmos/components';
import { isArray, objectEach } from '@bmos/utils';
import { storeToRefs } from 'pinia';
import { getPxByConfig, removeNodeClassName, setNodeClassName } from '../utils';
import { pageA4Height, pageA4Width, printparamsConst } from '../utils/const';
import { pxToPoint } from '../utils/printUtils';
import { StyleEnum } from './enum';
import { RecordPropsType, emits } from './type';
export const useRecord = (props: RecordPropsType, emit: EmitFn<emits>) => {
  const formulaId = toRef(props, 'formulaId'); //生产BOM ID
  const activeKeys = toRef(props, 'activeKeys', []); //激活节点
  const multiple = toRef(props, 'multiple', true); //是否单选
  const container = ref<HTMLElement | null>(); //内容容器
  const C_NODE_List = new Map(); //节点缓存
  const C_NODE = new Map<KEY, HTMLElement>(); //点击节点
  const IS_EMPTY = ref<boolean>(true);
  let beforeKeys: KEY[] = activeKeys.value;
  const containerId = new Date().getTime() + `${props?.formulaId}`;
  let containerElemnt: HTMLElement | null;

  const configStore = useConfig();
  const { configs } = storeToRefs(configStore);

  /**
   * 内容初始化
   * @param val
   * @returns
   */
  const initContent = (val: string = '<div>dddd</div>', id: string = ''): HTMLElement => {
    const fragment = document.createElement('div');
    const div = document.createElement('div');
    div.innerHTML = val;
    div.id = id;
    div.className = StyleEnum.content;
    fragment.appendChild(div);
    return fragment;
  };

  /**
   * 渲染函数
   * @returns
   */
  const render = (val?: string, pattern: number = 1) => {
    const dom: HTMLElement = initContent(val);
    if (!dom || !val) {
      IS_EMPTY.value = true;
      return;
    }
    if (!container.value) return;
    container.value.innerHTML = '';
    container.value?.appendChild(dom);
    // 设置页面纸张大小
    const { left, right } = getPxByConfig(JSON.parse(configs.value[printparamsConst].value) || {});
    const pattWid = pattern === 1 ? pageA4Width : pageA4Height;
    const width = pxToPoint(pattWid) - left - right;
    container.value.style.width = width + 'px';
    IS_EMPTY.value = false;
  };
  /**
   * 获取dom节点
   * @param id
   * @returns
   */
  const getNodeById = (id: string): HTMLElement | Element | undefined => {
    let node = C_NODE_List.has(id);
    if (node) return C_NODE_List.get(id);
    const queryTarget = (containerElemnt as unknown as DocumentFragment) || window.document;
    const target = queryTarget?.querySelector(`[id="${id}"]`);
    if (!target) return;
    C_NODE_List.set(id, target);
    return target;
  };

  /**
   * 设置节点class
   * @param id
   * @param name
   * @returns
   */
  const setNodeClass = (id: string | HTMLElement | Element, name: string) => {
    if (id === void 0 || name === void 0) return;
    if (typeof id === 'string') id = (getNodeById(id) as HTMLElement) || '';
    if (!id) return;
    if (!(id instanceof HTMLElement)) return;
    try {
      setNodeClassName(id, name);
    } catch (error) {
      console.log(error);
      return false;
    }
    return true;
  };
  /**
   * 去掉节点class
   * @param id
   * @param name
   * @returns
   */
  const removeNodeClass = (id: string | HTMLElement | Element, name: string = StyleEnum.active) => {
    if (isArray(id)) return clearNodesClassByIds(id, name);
    if (id === void 0 || name === void 0) return;
    if (typeof id === 'string') id = (getNodeById(id) as HTMLElement) || '';
    if (!id) return;
    if (!(id instanceof HTMLElement)) return;
    try {
      removeNodeClassName(id, name);
    } catch (error) {
      console.log(error);

      return false;
    }
  };

  /**
   * 修改dom样式
   * @param target
   * @param style
   * @returns
   */
  const setStyle = (target: HTMLElement | Element, style: CSSStyleDeclaration, replace: boolean = false) => {
    if (!target) return;
    try {
      const oldStyle = target.getAttribute('style') || '';
      let newStyle: string = oldStyle;

      if (replace) newStyle = '';

      objectEach(style, (v, k) => {
        newStyle += k + ':' + v;
      });

      target.setAttribute('style', newStyle);
    } catch (error) {
      throw 'Set style faild';
    }
  };

  /**
   * 根据id修改样式
   * @param id 唯一ID
   * @param stlye
   * @returns
   */
  const setNodeStyle = (id: string, stlye: CSSStyleDeclaration) => {
    if (id === void 0) return;

    const target = getNodeById(id);
    if (!target) return;
    setStyle(target, stlye);
  };

  /**
   * 获取节点数据
   * @param id
   * @returns
   */
  // const findNodeData = (id: KEY) => {
  //   if (id === void 0) return;
  //   return NODE_LIST.value.find(item => item.id === id);
  // };

  /**
   * 清除节点class
   * @param nodes
   * @param name
   */
  const clearNodesClass = (nodes: Array<HTMLElement | Element>, name: string = StyleEnum.active) => {
    nodes.forEach(item => {
      removeNodeClass(item, name);
    });
  };

  /**
   * 清除所有节点class
   * @param nodes
   * @param name
   */
  const clearAllNodesClass = (name: string = StyleEnum.active) => {
    clearNodesClass([...C_NODE_List.values()], name);
  };

  /**
   * 根据id清除节点class
   * @param nodes
   * @param name
   */
  const clearNodesClassByIds = (ids: Array<KEY>, name: string = StyleEnum.active) => {
    console.log(ids, name);
    ids.forEach(item => {
      const node = getNodeById(item as string);
      if (!node) return;
      removeNodeClass(node, name);
    });
  };

  /**
   * 为节点添加类名
   * @param nodes
   * @param name
   */
  const addNodesClass = (nodes: Array<KEY>, name: string = StyleEnum.active) => {
    nodes.forEach(item => {
      const node = getNodeById(item as string);
      if (!node) return;
      setNodeClass(node, name);
    });
  };

  /**
   * @description 节点点击
   * @param e
   * @returns
   */
  const handleClick = (e: MouseEvent | PointerEvent) => {
    const nodetarget = e.target as HTMLElement;
    let key = nodetarget.getAttribute('id') || '';
    if (key === void 0 || !key) return;
    key = key.split('_').shift()!;
    const target = getNodeById(key);
    if (!C_NODE_List.has(key)) C_NODE_List.set(key, nodetarget);
    if (multiple.value) {
      clearNodesClass([...C_NODE.values()]);
      C_NODE.clear();
    }

    C_NODE.set(key, nodetarget);
    return { target, key };
  };

  /**
   * 节点点击事件
   * @param e
   */
  const node_click = (e: MouseEvent | PointerEvent, node?: any) => {
    e.preventDefault();
    if (e.type === 'dblclick') return;
    const { target, key } = handleClick(e) || {};

    emit('node-click', target, key, node);

    emit('update:activeKeys', [key], node);
  };

  const node_dbclick = (e: MouseEvent | PointerEvent, node?: any) => {
    e.preventDefault();

    const { target, key } = handleClick(e) || {};

    emit('node-dbclick', target, key, node);
  };

  const setContent = (val: string, config: any = {}) => {
    C_NODE_List.clear();
    C_NODE.clear();
    const pattern = config.pattern;
    render(val, pattern);
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

  const setContentByConfig = (config: Record<string, any>) => {
    if (!config) {
      setContent('');
      return;
    }
    if (!config.fileContent) {
      setContent('');
      return;
    }
    let newContent = config.fileContent || '';
    if (config.fileContent.indexOf('remove_header_flag') < 0) {
      // 没有添加页眉页脚
      // !!!不可以换行,会被编辑器识别添加p标签
      newContent =
        config.fileContent.indexOf('<!-- remove_header_flag -->') < 0
          ? `<!-- remove_header_flag -->${
              getPageNo(
                config.docxHeader?.headerPrimary?.content,
                config.docxHeader?.headerPrimary?.pageCodeHorizontalAlignment,
                true,
              ) || ''
            }<hr style="margin-top:5px;"/><!-- remove_header_flag -->${
              config.fileContent
            }<!-- remove_footer_flag --><hr style="margin-bottom:5px;"/>${
              getPageNo(
                config.docxFooter?.footerPrimary?.content,
                config.docxFooter?.footerPrimary?.pageCodeHorizontalAlignment,
                false,
              ) || ''
            }<!-- remove_footer_flag -->`
          : config.fileContent;
    }
    const pageConfig = JSON.parse(config.pageConfig || {});
    setContent(newContent, pageConfig);
  };

  const differenceSet = (
    set: any[] | KEY[],
    tar: any[] | KEY[] = [],
    func?: (item: any | KEY, tar: any[] | KEY[]) => boolean,
  ) => {
    return set.filter(item => {
      if (func) {
        return func(item, tar);
      }
      return !tar.includes(item);
    });
  };

  const removeBoforeNodeClass = (bef: KEY[], cur?: KEY[]) => {
    if (bef.length === 0) return;
    const keys = differenceSet(bef, cur);
    for (const item of keys) {
      removeNodeClass(item);
    }
  };

  const setNodesStyle = (ids: Array<string> | string, name: string = StyleEnum.formula) => {
    if (!isArray(ids)) {
      return setNodeClass(ids, name);
    }
    const setStyle = () => {
      ids.forEach(item => {
        const node = getNodeById(item);
        if (!node) return;
        C_NODE_List.set(item, node);
        setNodeClass(item, name);
      });
    };
    const animation = window.requestAnimationFrame || setTimeout;
    animation(setStyle);
  };

  const setContainerPadding = () => {
    const { top, bottom, left, right } = getPxByConfig(JSON.parse(configs?.value[printparamsConst]?.value) || {});
    container.value!.style.paddingTop = top + 'px';
    container.value!.style.paddingBottom = bottom + 'px';
    container.value!.style.paddingLeft = left + 'px';
    container.value!.style.paddingRight = right + 'px';
  };
  const handleMounted = () => {
    containerElemnt = document.getElementById(containerId);
    setContainerPadding();
  };

  const scrollToNode = (fieldId: string) => {
    try {
      const target = document.getElementsByName(fieldId)[0];
      const containerElement = document.getElementById(containerId);
      if (target && containerElement) {
        containerElement.scrollTop = target.offsetTop;
      }
    } catch (error) {}
  };

  onMounted(() => {
    container.value
      ? handleMounted()
      : nextTick(() => {
          handleMounted();
        });
  });

  onUnmounted(() => {
    C_NODE_List.clear();
    C_NODE.clear();
    containerElemnt = null;
  });

  watch(activeKeys, value => {
    if (activeKeys.value.length === 0) {
      clearNodesClass([...C_NODE.values()]);
      return;
    }

    removeBoforeNodeClass(beforeKeys, activeKeys.value);
    beforeKeys = value;
    container.value ? addNodesClass(activeKeys.value) : nextTick(() => addNodesClass(activeKeys.value));
  });

  return {
    setNodeStyle,
    removeNodeClass,
    clearAllNodesClass,
    clearNodesClassByIds,
    setNodeClass,
    setContent,
    setNodesStyle,
    container,
    initContent,
    node_dbclick,
    node_click,
    formulaId,
    render,
    containerId,
    configs,
    IS_EMPTY,
    setContentByConfig,
    scrollToNode,
  };
};
