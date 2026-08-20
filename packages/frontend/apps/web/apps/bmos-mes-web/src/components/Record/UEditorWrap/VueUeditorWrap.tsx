import { LoadEvent, debounce, isObject, objectEach } from '@bmos/utils';
import {
  PropType,
  VNode,
  VueElement,
  defineComponent,
  nextTick,
  onBeforeUnmount,
  onDeactivated,
  ref,
  toRef,
  watch,
} from 'vue';
import { randomString } from '../../../utils';
import { MODE, STATUS_MAP, defaultLineConfig } from './enum';
import { EditorContent, ModeType, PageBreak, UENode } from './type';
import { usePageBreak } from './usePageBreak';

export default defineComponent({
  name: 'UeditorWrap',
  // expose:['insert','getContent'],
  props: {
    // 手动设置 UEditor ID
    editorId: {
      type: String,
      default: '',
    },
    // 主要用于表单中 http://fex.baidu.com/ueditor/#start-submit
    name: {
      type: String,
      default: '',
    },
    modelValue: {
      type: String,
      default: '',
    },
    // http://fex.baidu.com/ueditor/#start-config
    config: {
      type: Object as PropType<UEDITOR_CONFIG>,
      default: () => {},
    },
    // 监听富文本内容变化的方式
    mode: {
      type: Number as PropType<ModeType>,
      default: 0,
      validator: (value: string) => {
        // 1. observer 借助 MutationObserver API https://developer.mozilla.org/zh-CN/docs/Web/API/MutationObserver
        // 2. listener 借助 UEditor 的 contentChange 事件 https://ueditor.baidu.com/doc/#UE.Editor:contentChange
        return Object.values(MODE).indexOf(value) !== -1;
      },
    },
    // MutationObserver 的配置 https://developer.mozilla.org/en-US/docs/Web/API/MutationObserverInit
    observerOptions: {
      type: Object as PropType<MutationObserverInit>,
      default: () => {
        return {
          attributes: true, // 是否监听 DOM 元素的属性变化
          attributeFilter: ['src', 'style', 'type', 'name'], // 只有在该数组中的属性值的变化才会监听
          characterData: true, // 是否监听文本节点
          childList: true, // 是否监听子节点
          subtree: true, // 是否监听后代元素
        };
      },
    },
    // MutationObserver 的回调函数防抖间隔
    observerDebounceTime: {
      type: Number,
      default: 100,
      validator: (value: number) => {
        return value >= 20;
      },
    },
    //  SSR 项目，服务端实例化组件时组件内部不会对 UEditor 进行初始化，仅在客户端初始化 UEditor，这个参数设置为 true 可以跳过环境检测，直接初始化
    forceInit: Boolean,
    // 是否在组建销毁时销毁 UEditor 实例
    destroy: {
      type: Boolean,
      default: true,
    },
    // 指定 UEditor 依赖的静态资源，js & css
    editorDependencies: {
      type: Array as PropType<string[]>,
      default: null,
    },
    // 检测依赖的静态资源是否加载完成的方法
    editorDependenciesChecker: {
      type: Function as PropType<() => boolean>,
      default: null,
    },
  },

  emits: [
    'update:modelValue',
    'before-init',
    'ready',
    'rendered',
    'change',
    'pattern-change',
    'content-click',
    'contentchange',
  ],

  setup(props, { emit, expose }) {
    let status = STATUS_MAP.UN_READY;
    let editor: any;
    let observer: MutationObserver;
    let innerValue: UENode;
    const container = ref<HTMLElement>();
    const styleMap = new Map<string, string>();
    const { createPageBreak, changeLineByConfig, setConfig, togglePattern, setWidth } = usePageBreak(emit);

    const modelValue = toRef(props, 'modelValue');

    // 创建加载资源的事件通信载体
    if (!window.$loadEventBus) {
      window.$loadEventBus = new LoadEvent();
    }

    // 基于 UEditor 的 contentChange 事件
    const observerContentChangeHandler = () => {
      innerValue = getContent(1);
      emtiUpdate(innerValue, innerValue ? innerValue.toHtml() : null);
    };

    const normalChangeListener = () => {
      editor.addListener('contentChange', observerContentChangeHandler);
    };

    // 基于 MutationObserver API
    const changeHandle = () => {
      if (editor.document.getElementById('baidu_pastebin')) {
        return;
      }
      innerValue = getContent(1);

      emtiUpdate(innerValue, innerValue ? innerValue.toHtml() : null);
    };

    const observerChangeListener = () => {
      observer = new MutationObserver(debounce(changeHandle, props.observerDebounceTime));
      observer.observe(editor.body, props.observerOptions);
    };

    const emtiUpdate = (content: UENode, val: string) => {
      emit('change', content, val);
      emit('update:modelValue', val);
    };

    // 实例化编辑器
    const initEditor = () => {
      const editorId = props.editorId || 'editor_' + randomString(8);
      container.value!.id = editorId;
      emit('before-init', editorId);
      editor = window.UE.getEditor(editorId, props.config);

      editor.addListener('ready', () => {
        createPageBreak(editor);
        if (props.mode === MODE.listener && window.MutationObserver) {
          observerChangeListener();
        } else {
          normalChangeListener();
        }

        if (status === STATUS_MAP.READY) {
          // 使用 keep-alive 组件会出现这种情况
          editor.setContent(props.modelValue);
        } else {
          status = STATUS_MAP.READY;
          emit('ready', editor);
          if (props.modelValue) {
            editor.setContent(props.modelValue);
          }
        }
      });

      editor.addListener('click', (_event: any, o: any) => {
        emit('content-click', o);
      });
      editor.addListener('contentchange', () => {
        const html = getContent(1);
        emit('contentchange', html);
      });
    };

    watch(
      modelValue,
      value => {
        if (status === STATUS_MAP.UN_READY) {
          status = STATUS_MAP.PENDING;
          (props.forceInit || typeof window !== 'undefined') && container.value
            ? initEditor()
            : nextTick(() => initEditor());
        } else if (status === STATUS_MAP.READY) {
          value === innerValue || editor.setContent(value || '');
        }
      },
      {
        immediate: true,
      },
    );

    onDeactivated(() => {
      editor && editor.removeListener('contentChange', observerContentChangeHandler);
      observer && observer.disconnect();
    });

    onBeforeUnmount(() => {
      if (observer && observer.disconnect) {
        observer.disconnect();
      }
      if (props.destroy && editor && editor.destroy) {
        editor.destroy();
      }
    });

    const initContent = (val: string = '   ', config: PageBreak = defaultLineConfig) => {
      setConfig(config);
      setWidth(config);
      editor.setContent(val);
      setTimeout(() => {
        emit('rendered', editor);
      }, 100);
    };

    const insert = (val: string | VNode | VueElement) => {
      if (editor && val !== void 0) {
        const val_type = typeof val;
        let content = val;
        if (val_type === 'object') {
          if (val instanceof VueElement) {
          }
        }
        editor.execCommand('insertHtml', content);
      }
    };

    const getContent = (type: 0 | 1 = 0): EditorContent => {
      let content: EditorContent = '';

      if (editor) {
        content = editor.getContentJSON();
        if (before && content) {
          const node = content.getNodeById(before);
          if (node) {
            // setStyle(node, style);
          }
        }

        if (type === 0 && content) {
          content = content.toHtml();
        }
      }
      return content;
    };

    const getNodeById = (id: string, callback: (root: UENode, nodety: UENode) => void) => {
      const rootNode = editor.getContentJSON();
      const node = rootNode.getNodeById(id);
      callback && callback(rootNode, node);
      return node;
    };

    /**
     * 根据id 删除节点
     * @param id
     * @returns
     */
    const deleteNode = (id: string) => {
      if (!id) throw 'the property id is required';
      if (!editor) return;
      const rootNode = editor.getContentJSON();
      if (!rootNode) return false;
      getNodeById(id, (rootNode: UENode, node: UENode) => {
        if (!node || !rootNode) throw 'delete node error: the node is not find';
        node.children = [];
        try {
          removeClickNode(id);
          node.parentNode.removeChild(node, true);
          initContent(rootNode.toHtml());
        } catch (error) {
          throw 'delete node error';
        }
      });
    };

    const getStyleStr = (style: object | string): String => {
      let attrvalue: string;
      if (isObject(style)) {
        attrvalue = '';
        objectEach(style, (v, k) => {
          attrvalue += k + ':' + v + ';';
        });
      } else {
        attrvalue = style;
      }
      return attrvalue;
    };

    const setStyle = (node: UENode, style: string) => {
      if (!style) {
        return;
      }
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const dom = iframe.contentWindow.document.getElementById(node.attrs.id);
      // 清空原来所有style
      dom.removeAttribute('style');
      style.split(';').map(item => {
        if (!item) {
          return;
        }
        const itemObj = item.split(':');
        dom.style[itemObj[0]?.trim()] = itemObj[1]?.trim();
      });
    };

    const resetNodeStyle = (id: string) => {
      if (!id) throw 'the property of id or style is required';
      getNodeById(id, (rootNode: UENode, node: UENode) => {
        if (!node) {
          console.log('The node does not exist,id:', id);
          return;
        }
        if (!styleMap.has(id)) return;
        const style = styleMap.get(id) || '';
        setStyle(node, style);
      });
    };

    /**
     * 根据id修改样式
     * @param id 唯一ID
     * @param stlye
     * @returns
     */
    const setNodeStyle = (id: string, stlye: CSSStyleDeclaration) => {
      if (!id || !stlye) throw 'the property of id or style is required';
      getNodeById(id, (rootNode: UENode, node: UENode) => {
        if (!node) {
          console.log('The node does not exist,id:', id);
          return;
        }
        const set_style = getStyleStr(stlye);
        const node_style = node.attrs.style;
        styleMap.set(node.attrs.id, node_style);
        setStyle(node, node_style + set_style);
      });
    };

    let before: string;

    const toggleNodeStyle = (cur: string, style: CSSStyleDeclaration, isSave: Boolean) => {
      if (!before && !cur) throw 'the property of id or style is required';
      getNodeById(cur, (rootNode: UENode, node: UENode) => {
        if (!node) {
          console.log('The node does not exist,id:', cur);
          return;
        }
        const set_style = getStyleStr(style);
        const node_style = node.attrs.style;
        let beNode: UENode;
        if (before) {
          beNode = rootNode.getNodeById(before);
          if (beNode) {
            const be_style = styleMap.get(before) || '';
            // 新旧样式对比,目前只获取最新宽高,其他都为旧样式
            const nowStyle = getNowStyle(beNode, be_style);
            setStyle(beNode, nowStyle);
          }
        }
        styleMap.set(node.attrs.id, (isSave ? set_style : '') + node_style);
        setStyle(node, node_style + set_style);
        before = node.attrs.id;
      });
    };

    // 获取最新的样式
    const getNowStyle = (node: UENode, be_style: string) => {
      if (be_style != '') {
        // 获取旧样式对象
        let oldStyle = {} as any;
        be_style.split(';').map(item => {
          if (!item) {
            return;
          }
          const itemObj = item.split(':');
          oldStyle[itemObj[0]?.trim()] = itemObj[1]?.trim();
        });
        // 获取新的样式对象
        let newStyle = {} as any;
        const no_style = node.getAttr('style');
        if (no_style == '') {
          return be_style;
        }
        no_style.split(';').map((item: string) => {
          if (!item) {
            return;
          }
          const itemObj = item.split(':');
          newStyle[itemObj[0]?.trim()] = itemObj[1]?.trim();
        });
        if (newStyle.width != oldStyle.width) {
          oldStyle.width = newStyle.width;
        }
        if (newStyle.height != oldStyle.height) {
          oldStyle.height = newStyle.height;
        }
        if (newStyle['font-size'] != oldStyle['font-size']) {
          oldStyle['font-size'] = newStyle['font-size'];
        }
        if (newStyle['font-family'] != oldStyle['font-family']) {
          oldStyle['font-family'] = newStyle['font-family'];
        }
        return getStyleStr(oldStyle);
      } else {
        return node.getAttr('style');
      }
    };

    // 判断是否是表格中的节点
    const isTableChildren = (cur: string) => {
      if (!before && !cur) throw 'the property of id or style is required';
      let isTable = { flag: false, innerHTML: '' };
      getNodeById(cur, (rootNode: UENode, node: UENode) => {
        if (!node) {
          console.log('The node does not exist,id:', cur);
          return;
        }
        if (node.parentNode.tagName == 'td' || node.parentNode.parentNode?.tagName == 'td') {
          isTable = { flag: true, innerHTML: node.parentNode.innerHTML() };
        }
      });
      return isTable;
    };

    const editNode = (b: string, c: string) => {
      if (!b) return;
      if (!c) deleteNode(b);
      getNodeById(b, (rootNode: UENode, node: UENode) => {
        if (!node || !rootNode) throw 'edit node error: the node is not find';
        node.children = [];
        const newNode = window.UE?.htmlparser(c)?.children[0];
        const parent = node.parentNode;
        parent.replaceChild(newNode, node);
        initContent(rootNode.toHtml());
      });
    };

    // 计算当前标签占总宽度的比例
    const setAllSize = () => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const body = iframe.contentWindow.document.getElementsByTagName('body')[0];
      const bodyWidth = body.offsetWidth; //body的宽度
      for (let i = 0; i < body.children.length; i++) {
        // 计算比例
        const size = (body.children[i].offsetWidth / bodyWidth).toFixed(3);
        body.children[i].setAttribute('size', size);
      }
    };

    const getAllPageNumber = () => {
      const iframe = getIframe();
      if (!iframe) {
        return null;
      }
      const document = iframe.contentWindow.document;
      const pageList = document.getElementsByClassName('pageno_content');
      console.log('===================pageList', pageList, pageList.length);
      if (pageList.length == 0) {
        return null;
      }
      console.log('===================pageList', pageList[0]);
    };

    // 添加ctrl按下松开监听时间
    const listenCtrl = (ctrlDown: any) => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const iframeDocument = iframe.contentWindow.document;
      iframeDocument.addEventListener('keydown', function (event: any) {
        // 检查ctrl键是否按下
        if (event.ctrlKey) {
          ctrlDown.value = true;
          if (event.keyCode == 90) {
            const componentList = iframeDocument.getElementsByTagName('textarea');
            for (let i = 0; i < componentList.length; i++) {
              if (componentList[i]?.style.color == 'rgb(40, 113, 255)') {
                // 该节点为点击状态
                if (iframeDocument.clickNodeList.indexOf(componentList[i]?.id) < 0) {
                  // 一点击列表中不存在,删除点击样式
                  recoveryStyle(componentList[i]?.id);
                }
              }
            }
          }
        }
      });
      iframeDocument.addEventListener('keyup', function (event: any) {
        // 检查ctrl键是否松开
        if (!event.ctrlKey) {
          ctrlDown.value = false;
        }
      });
    };
    // 给编辑器的document添加已选中节点list
    const changeClickNodeList = (id: string, ctrlDown: Boolean) => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const iframeDocument = iframe.contentWindow.document;
      // debugger;
      if (!iframeDocument.clickNodeList) {
        iframeDocument.clickNodeList = [];
      }
      if (!ctrlDown) {
        // ctrl松开并再次点击,清空之前的点击节点和样式
        iframeDocument.clickNodeList.map((filedId: string) => {
          if (iframeDocument.clickNodeList.length > 1 || filedId != id) {
            recoveryStyle(filedId, true);
          }
        });
        iframeDocument.clickNodeList = [id];
        return;
      }
      if (iframeDocument.clickNodeList.indexOf(id) < 0) {
        // 未点击添加到节点中
        iframeDocument.clickNodeList.push(id);
        // 设置该节点样式
        getNodeById(id, (rootNode: UENode, node: UENode) => {
          if (!node) {
            console.log('The node does not exist,id:', id);
            return;
          }
          const set_style = getStyleStr({
            border: '1px solid #2871FF',
            color: '#2871FF',
            background: '#EBF1FF',
          });
          const node_style = node.attrs.style;
          // 保存旧样式
          styleMap.set(node.attrs.id, node_style);
          setStyle(node, node_style + set_style);
        });
      } else {
        // 已点击删除节点
        iframeDocument.clickNodeList.splice(iframeDocument.clickNodeList.indexOf(id), 1);
        // 恢复该节点样式
        recoveryStyle(id);
      }
    };
    // 清空已选中节点list
    const removeClickNodeList = () => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const iframeDocument = iframe.contentWindow.document;
      if (!iframeDocument.clickNodeList) {
        iframeDocument.clickNodeList = [];
        return;
      }
      iframeDocument.clickNodeList.map((filedId: string) => {
        recoveryStyle(filedId, true);
      });
      iframeDocument.clickNodeList = [];
    };

    const removeClickNode = (id: string) => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const iframeDocument = iframe.contentWindow.document;
      if (!!iframeDocument.clickNodeList && iframeDocument.clickNodeList.indexOf(id) >= 0) {
        iframeDocument.clickNodeList.splice(iframeDocument.clickNodeList.indexOf(id), 1);
      }
    };

    // 恢复该节点上次设置的样式
    const recoveryStyle = (id: string, saveWH = false) => {
      getNodeById(id, (rootNode: UENode, node: UENode) => {
        if (!node) {
          console.log('该节点不存在');
          return;
        }
        let node_style = styleMap.get(node.attrs.id) || '';
        if (saveWH) {
          node_style = getNowStyle(node, node_style);
        }
        styleMap.set(node.attrs.id, node_style);
        setStyle(node, node_style);
      });
    };

    // 获取一点击的node列表
    const getIframeClickNodeList = () => {
      const iframe = getIframe();
      if (!iframe) {
        return;
      }
      const iframeDocument = iframe.contentWindow.document;
      if (!iframeDocument.clickNodeList) {
        iframeDocument.clickNodeList = [];
      }
      return iframeDocument.clickNodeList;
    };

    // 获取富文本编辑器的iframe
    const getIframe = () => {
      let iframe = '' as any;
      // 获取iframe标签
      for (let i = 0; i < editor.container.children.length; i++) {
        if (editor.container.children[i].id?.indexOf('iframeholder') >= 0) {
          const body = editor.container.children[i];
          for (let y = 0; y < body?.children.length; y++) {
            if (body.children[y].tagName == 'IFRAME') {
              iframe = body.children[y];
            }
          }
        }
      }
      return iframe;
    };

    // 获取节点距离顶部距离
    const getNodeTop = (id: string) => {
      const iframeDocument = editor.iframe.contentWindow.document;
      const clickDoc = iframeDocument.getElementById(id);
      let offsetTop = clickDoc.offsetTop;
      if (clickDoc.offsetParent.tagName != 'BODY') {
        offsetTop += getAddoffsetTop(clickDoc.offsetParent);
      }
      const componentDoc = document.getElementById('vue-ueditor-component');
      if (offsetTop < 200) {
        componentDoc!.scrollTop = 0;
      } else {
        componentDoc!.scrollTop = offsetTop;
      }
    };

    // 获取父元素距离父元素的距离
    const getAddoffsetTop = (doc: any) => {
      let offsetTop = 0;
      offsetTop = doc.offsetTop;
      if (doc.offsetParent.tagName != 'BODY') {
        offsetTop = offsetTop + getAddoffsetTop(doc.offsetParent);
      }
      return offsetTop;
    };

    const isFocus = () => {
      if (!editor) return false;
      return editor.isFocus();
    };

    const reset = () => {
      editor.execCommand('cleardoc');
    };

    const resetUndo = () => {
      editor.undoManger.reset();
    };

    const convert = (content: string) => {
      if (!content) return void 0;

      return window.UE?.htmlparser(content).toHtml();
    };

    const Focus = () => {
      editor.focus();
    };

    const changePattern = () => {
      setTimeout(() => {
        editor.changePattern();
      }, 300);
    };

    const changeConfig = (config: PageBreak) => {
      changeLineByConfig(config);
    };

    const setEditorListener = (type: string, monitoringEvents: Function) => {
      editor.addListener(type, monitoringEvents);
    };

    expose({
      insert,
      getContent,
      deleteNode,
      setNodeStyle,
      isFocus,
      Focus,
      initContent,
      getNodeById,
      editNode,
      convert,
      resetNodeStyle,
      toggleNodeStyle,
      changeConfig,
      togglePattern,
      isTableChildren,
      setAllSize,
      listenCtrl,
      changeClickNodeList,
      removeClickNodeList,
      getIframeClickNodeList,
      recoveryStyle,
      removeClickNode,
      getNodeTop,
      reset,
      resetUndo,
      getAllPageNumber,
      changePattern,
      setEditorListener,
    });

    return () => (
      <div class='vue-ueditor-component' id='vue-ueditor-component'>
        <div class='vue-ueditor-container'>
          <div ref={container} name={props.name} class='vue-ueditor' />
        </div>
      </div>
    );
  },
});
