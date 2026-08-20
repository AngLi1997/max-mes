import { KEY } from '@/components/Layout/type';
import { Empty } from 'ant-design-vue';
import { PropType, defineComponent, nextTick, watch } from 'vue';
import './style.less';
import { configType } from './type';
import { useRecord } from './useRecord';

export default defineComponent({
  name: 'Record',
  props: {
    formulaId: {
      type: String || Number,
      default: '',
      required: false,
    },
    config: {
      type: Object as PropType<configType>,
      default: () => {},
      required: false,
    },
    activeKeys: {
      type: Array<KEY>,
      default: () => [],
      required: false,
    },
    multiple: {
      type: Boolean, //是否单选 true 单选
      required: false,
    },
  },
  emits: ['error', 'rendered', 'node-click', 'update:activeKeys', 'node-dbclick'],
  setup(props, { emit, expose }) {
    const {
      setNodeStyle,
      removeNodeClass,
      clearAllNodesClass,
      clearNodesClassByIds,
      setNodeClass,
      setContent,
      setNodesStyle,
      container,
      render,
      IS_EMPTY,
      node_dbclick,
      node_click,
      formulaId,
      containerId,
      setContentByConfig,
      scrollToNode,
      // @ts-ignore
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
      clearAllNodesClass,
      clearNodesClassByIds,
      setNodeClass,
      setContent,
      setContentByConfig,
      setNodesStyle,
      scrollToNode,
    });

    return () => (
      <div class='formula'>
        {IS_EMPTY.value && (
          <div class='empty-preview'>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}></Empty>
          </div>
        )}
        <div
          v-show={!IS_EMPTY.value}
          class='formula-container'
          id={containerId}
          ref={container}
          onClick={e => node_click(e)}
          onDblclick={e => node_dbclick(e)}></div>
      </div>
    );
  },
});
