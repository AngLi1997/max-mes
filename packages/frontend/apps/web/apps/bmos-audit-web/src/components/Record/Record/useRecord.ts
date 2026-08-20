import { EmitFn } from '@bmos/components';
import { isArray, objectEach } from '@bmos/utils';
import { nextTick, onUnmounted, ref, toRef, watch } from 'vue';
import { removeNodeClassName, setNodeClassName } from '../utils';
import { StyleEnum } from './enum';
import { RecordPropsType, emits } from './type';


export const useRecord = (props: RecordPropsType, emit: EmitFn<emits>) => {
  const formulaId = toRef(props, 'formulaId'); //配方ID
  const activeKeys = toRef(props, 'activeKeys', []); //激活节点
  const multiple = toRef(props, 'multiple', true); //是否单选
  const container = ref<HTMLElement>(); //内容容器
  const C_NODE_List = new Map(); //节点缓存
  const C_NODE = new Map<KEY, HTMLElement>(); //点击节点
  const NODE_LIST = ref<Record<string, any>[]>([]); //节点数据
  const IS_EMPTY = ref<boolean>(true);
  let beforeKeys: KEY[] = activeKeys.value;

  /**
   * 内容初始化
   * @param val
   * @returns
   */
  const initContent = (val: string = '<div>dddd</div>'): DocumentFragment => {
    const fragment = document.createDocumentFragment();
    const div = document.createElement('div');
    div.innerHTML = val;
    div.className = StyleEnum.content;
    fragment.appendChild(div);
    return fragment;
  };

  /**
   * 渲染函数
   * @returns
   */
  const render = (val?: string) => {
    const dom: DocumentFragment = initContent(val);
    if (!dom || !val) {
      IS_EMPTY.value = true;
      return;
    }
    if (!container.value) return;
    container.value.innerHTML = '';
    container.value?.appendChild(dom);
    nextTick(() => {
      emit('rendered');
    });
  };

  /**
   * 获取dom节点
   * @param id
   * @returns
   */
  const getNodeById = (id: string): HTMLElement | Element | undefined => {
    let node = C_NODE_List.has(id);
    if (node) return C_NODE_List.get(id);
    const target = document.getElementById(id);
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
  const removeNodeClass = (
    id: string | HTMLElement | Element,
    name: string = StyleEnum.active,
  ) => {
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
  const setStyle = (
    target: HTMLElement | Element,
    style: CSSStyleDeclaration,
    replace: boolean = false,
  ) => {
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
  const findNodeData = (id: KEY) => {
    if (id === void 0) return;
    return NODE_LIST.value.find(item => item.id === id);
  };

  /**
   * 清除节点class
   * @param nodes
   * @param name
   */
  const clearNodesClass = (
    nodes: Array<HTMLElement | Element>,
    name: string = StyleEnum.active,
  ) => {
    nodes.forEach(item => {
      removeNodeClass(item, name);
    });
  };

  /**
   * 根据id清除节点class
   * @param nodes
   * @param name
   */
  const clearNodesClassByIds = (
    ids: Array<KEY>,
    name: string = StyleEnum.active,
  ) => {
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
  const addNodesClass = (
    nodes: Array<KEY>,
    name: string = StyleEnum.active,
  ) => {
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
    const key = nodetarget.getAttribute('id') || '';
    if (key === void 0 || !key) return;
    const target = findNodeData(key);

    if (C_NODE.has(key) && false) {
      const node = C_NODE.get(key);
      emit('node-click', target, []);
      node?.classList.remove('node-actived');
      C_NODE.delete(key);
      return;
    }
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
  const node_click = (e: MouseEvent | PointerEvent) => {
    e.preventDefault();

    if (e.type === 'dblclick') return;
    const { target, key } = handleClick(e) || {};

    emit('node-click', target, key);

    emit('update:activeKeys', [key]);
  };

  const node_dbclick = (e: MouseEvent | PointerEvent) => {
    e.preventDefault();

    const { target, key } = handleClick(e) || {};

    emit('node-dbclick', target, key);
  };

  const setContent = (val: string) => {
    render(val);
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

  const setNodesStyle = (
    ids: Array<string> | string,
    name: string = StyleEnum.formula,
  ) => {
    if (!isArray(ids)) {
      return setNodeClass(ids,name)
    }
    const nodes = container.value?.getElementsByTagName('textarea') || [];
    if (nodes.length === 0) return;
    const setStyle = () => {
      for (const item of nodes) {
        C_NODE_List.set(item.id, item);
        if (ids.includes(item.id)) {
          // item.classList.add(name)
          setNodeClass(item, name);
        }
      }
    };
    const animation = window.requestAnimationFrame || setTimeout;
    animation(setStyle);
  };
  onUnmounted(() => {
    C_NODE_List.clear();
    C_NODE.clear();
  });
  
  watch(activeKeys, value => {
    if (activeKeys.value.length === 0) {
      clearNodesClass([...C_NODE.values()]);
      return;
    }

    removeBoforeNodeClass(beforeKeys, activeKeys.value);
    beforeKeys = value;
    container.value
      ? addNodesClass(activeKeys.value)
      : nextTick(() => addNodesClass(activeKeys.value));
  });

  return {
    setNodeStyle,
    removeNodeClass,
    setNodeClass,
    setContent,
    setNodesStyle,
    container,
    initContent,
    node_dbclick,
    node_click,
    formulaId,
    render
  };
};
