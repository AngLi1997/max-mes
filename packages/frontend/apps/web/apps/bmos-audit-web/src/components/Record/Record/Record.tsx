import { KEY } from '@/components/Layout/type';
import { PropType, defineComponent, nextTick, watch } from 'vue';
import './style.css';
import { configType } from './type';
import { useRecord } from './useRecord';

export default defineComponent({
  name: 'Record',
  props: {
    formulaId: {
      type: String || Number,
      required: false,
    },
    config: {
      type: Object as PropType<configType>,
    },
    activeKeys: {
      type: Array<KEY>,
    },
    multiple: {
      type: Boolean, //是否单选 true 单选
    },
  },
  emits: [
    'error',
    'rendered',
    'node-click',
    'update:activeKeys',
    'node-dbclick',
  ],
  setup(props, { emit, expose }) {
    const {
      setNodeStyle,
      removeNodeClass,
      setNodeClass,
      setContent,
      setNodesStyle,
      container,
      render,
      node_dbclick,
      node_click,
      formulaId,
    } = useRecord(props, emit);

    watch(
      formulaId,
      value => {
        if (!value) {
          // emit('error');
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
      setContent,
      setNodesStyle,
    });

    return () => (
      <div class='formula'>
        <div
          class='formula-container'
          ref={container}
          onClick={e => node_click(e)}
          onDblclick={e => node_dbclick(e)}></div>
      </div>
    );
  },
});
