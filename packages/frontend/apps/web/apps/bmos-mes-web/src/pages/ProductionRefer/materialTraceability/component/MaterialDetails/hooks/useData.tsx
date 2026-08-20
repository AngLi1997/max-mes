import { ExtensionCategory, register, treeToGraphData } from '@antv/g6';
import { t } from '@bmos/i18n';
import { VueNode } from '@zwight/g6-extension-vue';
import { ref } from 'vue';
import MindMapItem from '../component/MindMapItem.vue';
export const useData = (props: any) => {
  const descData = ref<any>([
    {
      label: t('产品名称'),
      value: 'productName',
    },
    {
      label: t('产品编码'),
      value: 'productMergeCode',
    },
    {
      label: t('产品规格'),
      value: 'productSpecification',
    },
    {
      label: t('工艺名称'),
      value: 'processName',
    },
    {
      label: t('生产批号'),
      value: 'batchNo',
    },
    {
      label: t('生产产线'),
      value: 'productionLine',
    },
    {
      label: t('生产开始时间'),
      value: 'startTime',
    },
    {
      label: t('生产结束时间'),
      value: 'endTime',
    },
  ]);
  const segmentedValue = ref<any>('tree');
  const allCollapseId = ref<any>([]);
  const treeSelectData = ref<any>(null);
  const segmentedData = ref([
    { value: 'tree', title: t('列表') },
    { value: 'atlas', title: t('图谱') },
  ]);
  const MindMapRef = ref();
  const showTree = ref(false);
  const defaultExpandAllValue = ref(false);
  register(ExtensionCategory.NODE, 'vue', VueNode);
  const treeData = ref<any>([]);
  const mindMapConfig = ref<any>({});
  const selectedKeys = ref<any>([]);
  const treeSelect = (_value: any, { node }: any) => {
    if (node.isFather) {
      treeSelectData.value = null;
      return;
    }
    treeSelectData.value = node;
  };
  const segmentedChange = (value: any) => {
    if (value == 'atlas') {
      setTimeout(() => {
        MindMapRef.value.focusElement(treeData.value.id);
      }, 500);
    }
  };
  onMounted(async () => {
    // 流程图配置
    // const { data } = await getMaterialTraceData(props.rowData.id);
    const data = props.rowData.materialTraceData;
    treeData.value = {
      ...data,
      isFather: true,
      id: data.processId,
      mergeCode: data.processId,
      children: data.traceTree,
    };
    defaultExpandAllValue.value = true;
    nextTick(() => {
      showTree.value = true;
    });
    // 默认选中第一个
    selectedKeys.value = [data.traceTree.id];
    mindMapConfig.value = {
      data: treeToGraphData(treeData.value, {
        getNodeData: datum => {
          if (!datum.style) datum.style = {};
          datum.style.collapsed = false;
          if (!datum.children) return datum;
          const { children, ...restDatum } = datum;
          return { ...restDatum, children: children.map(child => child.id) };
        },
      }),
      node: {
        type: 'vue',
        style: {
          size: [260, 140],
          ports: [{ placement: 'left' }, { placement: 'right' }],
          component: (data: any) => (
            <MindMapItem
              data={data}
              allCollapseId={allCollapseId.value}
              onUpdateSate={id => {
                if (allCollapseId.value.indexOf(id) < 0) {
                  // 收起
                  allCollapseId.value.push(id);
                  MindMapRef.value.collapseElement(id);
                  setTimeout(() => {
                    MindMapRef.value.focusElement(id);
                  }, 500);
                } else {
                  // 展开
                  allCollapseId.value.splice(allCollapseId.value.indexOf(id), 1);
                  MindMapRef.value.expandElement(id);
                  setTimeout(() => {
                    MindMapRef.value.focusElement(id);
                  }, 500);
                }
              }}
            />
          ),
        },
      },
    };
    // 生产信息回显
    descData.value = descData.value.map((item: any) => {
      if (item.value == 'productionLine') {
        item.value = `${props.rowData.productionLineCode}-${props.rowData.productionLineName}`;
      } else {
        item.value = props.rowData[item.value];
      }
      return item;
    });
  });
  return {
    descData,
    mindMapConfig,
    MindMapRef,
    treeSelect,
    selectedKeys,
    treeData,
    segmentedValue,
    segmentedData,
    segmentedChange,
    treeSelectData,
    defaultExpandAllValue,
    showTree,
  };
};
