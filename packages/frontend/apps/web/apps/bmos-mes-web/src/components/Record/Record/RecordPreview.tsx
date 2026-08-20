import { KEY } from '@/components/Layout/type';
import { Empty } from 'ant-design-vue';
import { PropType, defineComponent } from 'vue';
import RecordPrint from './RecordPrint';
import './style.less';
import { configType } from './type';
import { printRecord } from './useNewPrint';
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
      required: false,
    },
    activeKeys: {
      type: Array<KEY>,
      required: false,
    },
    multiple: {
      type: Boolean, //是否单选 true 单选
      required: false,
    },
    processId: String,
    processVersion: String,
    productPlanId: String,
    node: {
      type: Object as PropType<configType>,
    },
    index: {
      type: Number,
    },
    getApi: {
      type: Function,
    },
    params: {
      type: Object
    }
  },
  emits: ['error', 'rendered', 'node-click', 'update:activeKeys', 'node-dbclick'],
  setup(props, { emit, expose }) {
    const record = useRecord(props, emit);
    const { setNodeStyle, removeNodeClass, setNodeClass, setNodesStyle, container, node_dbclick, node_click } = record;
    const { getList, printRecordStart, reacrdList, AllItems } = useRender(record, emit, props);
    let loaded = 0;
    const setContent = (val: string) => {};

    const printReocrd = () => {
      if (reacrdList.value.length === 0) return;
      // printStart && printStart(container.value?.innerHTML)
      printRecord(reacrdList.value);
    };

    const recordRendered = (node: any, id: number) => {
      const record: any = reacrdList.value.find(
        (item: any) => item.recordItemId === node.recordItemId && item.procedureStepId === node.procedureStepId,
      );
      if (record) record.printID = id;
      if (AllItems.value === 0) {
        return emit('rendered');
      } else if (loaded === AllItems.value) {
        return emit('rendered');
      }
    };

    expose({
      setNodeStyle,
      removeNodeClass,
      setNodeClass,
      setContent,
      setNodesStyle,
      node_dbclick,
      node_click,
      setContentByParams: getList,
      print: printReocrd,
    });

    watch(
      () => props.node,
      val => {
        if (val) {
          // getPagePadding()
          getList(val);
        }
      },
      { immediate: true },
    );

    return () => (
      <div class='formula formula-print-preview'>
        {reacrdList.value.length === 0 && (
          <div class='empty-preview'>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}></Empty>
          </div>
        )}

        <div class='formula-container' ref={container} onClick={e => node_click(e)} onDblclick={e => node_dbclick(e)}>
          {reacrdList.value.map((item: any, index: number) => (
            <RecordPrint
              key={new Date().getTime() + ''}
              node={item}
              index={index}
              onRendered={(node: any, id: number) => recordRendered(node, id)}></RecordPrint>
          ))}
        </div>
      </div>
    );
  },
});
