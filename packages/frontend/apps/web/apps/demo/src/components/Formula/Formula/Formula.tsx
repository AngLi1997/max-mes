import { objectEach } from '@bmos/utils';
import { PropType, defineComponent, nextTick, ref, toRef, watch } from 'vue';
import { removeNodeClassName, setNodeClassName } from '../utils';
import { configType } from './type';

export default defineComponent({
  name: 'Formula',
  props: {
    formulaId: {
      type: String,
      required: true,
    },
    config: {
      type: Object as PropType<configType>,
    },
  },
  emits: ['error', 'rendered', 'node-click'],
  setup(props, { emit, expose }) {
    const formulaId = toRef(props, 'formulaId');
    const container = ref<HTMLElement>();
    const C_NODE = ref<HTMLElement>();
    const NODE_LIST = ref<Record<string, any>[]>([]);
    const IS_EMPTY = ref<boolean>(true);

    const initContent = (val: string = '<div>dddd</div>'): HTMLElement => {
      const div = document.createElement('div');
      div.innerHTML = val;
      div.className = 'formula-content';
      return div;
    };

    const render = () => {
      const dom: HTMLElement | void = initContent('');
      if (!dom) {
        IS_EMPTY.value = true;
        return;
      }
      container.value?.replaceChildren(dom);
      nextTick(() => {
        emit('rendered');
      });
    };
    /**
     * 设置节点class
     * @param id
     * @param name
     * @returns
     */
    const setNodeClass = (id: string, name: string) => {
      if (id === void 0 || name === void 0) return;
      try {
        if (C_NODE.value && id === C_NODE.value.id) {
          setNodeClassName(C_NODE.value, name);
          return true;
        }

        const target = container.value?.querySelector(`#${id}`);
        if (!target) return false;

        setNodeClassName(target, name);
        return true;
      } catch (error) {
        console.log(error);
        return false;
      }
    };
    /**
     * 去掉节点class
     * @param id
     * @param name
     * @returns
     */
    const removeNodeClass = (id: string, name: string) => {
      if (id === void 0 || name === void 0) return;
      try {
        let target
        if (C_NODE.value && id === C_NODE.value.id) target = C_NODE.value
        else target = container.value?.querySelector(`#${id}`);

        if (!target) return false;
        removeNodeClassName(target, name);
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

      if (C_NODE.value && id === C_NODE.value.id) {
        setStyle(C_NODE.value, stlye);
        return;
      }
      const target = container.value?.querySelector(`#${id}`);
      if (!target) return;
      setStyle(target, stlye);
    };

    /**
     * 节点点击事件
     * @param e
     */
    const node_click = (e: MouseEvent | PointerEvent) => {
      if (e && e.target) {
        C_NODE.value = e.target as HTMLElement;
      }
      const target = NODE_LIST.value.pop();
      emit('node-click', target);
    };

    watch(
      formulaId,
      value => {
        if (!value) {
          emit('error');
          return;
        }
        container.value ? render() : nextTick(() => render());
      },
      { immediate: true },
    );

    expose({
      setNodeStyle,
      removeNodeClass,
      setNodeClass,
    });

    return () => (
      <div class='formula'>
        <div
          class='formula-container'
          ref={container}
          onClick={e => node_click(e)}></div>
      </div>
    );
  },
});
