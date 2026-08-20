import { KEY } from '@/components/Layout/type';
import { PropType, defineComponent } from 'vue';
import './style.css';
import { configType } from './type';

import { useRecord } from './useRecord';
import { useRender } from './useRender';

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
    const record = useRecord(props, emit);
    const {
      setNodeStyle,
      removeNodeClass,
      setNodeClass,
      setNodesStyle,
      container,
      node_dbclick,
      node_click,
    } = record;
    const { render } = useRender(record, emit);
    const setContent = (val: string) => {
      render(val);
    };

    expose({
      setNodeStyle,
      removeNodeClass,
      setNodeClass,
      setContent,
      setNodesStyle,
      node_dbclick,
      node_click,
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
