import { PropType, defineComponent } from 'vue';
import './style.less';

import { useRecord } from './useRecord';
import { useRender } from './useRender';

export default defineComponent({
  name: 'Record',
  props: {
    processId: String,
    processVersion: String,
    productPlanId: String,
    node: {
      type: Object as PropType<Record<string, any>>,
    },
    index: {
      type: Number,
      default: 0,
    },
  },
  emits: ['error', 'rendered', 'node-click', 'update:activeKeys', 'node-dbclick'],
  setup(props, { emit, expose }) {
    const node = toRef(props, 'node'); //生产BOM ID
    const record = useRecord(props, emit);
    const { container, node_dbclick, node_click, containerId } = record;
    const { renderRecordByData, LoadItems, isRendered } = useRender(record, emit, props);

    watch(
      node,
      val => {
        if (val) {
          container.value
            ? renderRecordByData(val)
            : nextTick(() => {
                renderRecordByData(val);
              });
        }
      },
      { immediate: true },
    );
    watch(LoadItems, val => {
      if (LoadItems.value === 0) return;
      if (isRendered(LoadItems.value)) {
        return emit('rendered', props.node, containerId);
      }
    });
    return () => (
      <div class='formula formula-print-item'>
        <div class='copyVersion-discard'>
          {props.node?.copyVersion !== '0' ? <div class='copy-version'>{t('副本')}</div> : null}
          {props.node?.discard ? <div class='obsolete-class'>{t('作废')}</div> : null}
        </div>
        <div
          id={containerId}
          class='formula-container'
          ref={container}
          onClick={e => node_click(e, props.node)}
          onDblclick={e => node_dbclick(e, props.node)}></div>
      </div>
    );
  },
});
