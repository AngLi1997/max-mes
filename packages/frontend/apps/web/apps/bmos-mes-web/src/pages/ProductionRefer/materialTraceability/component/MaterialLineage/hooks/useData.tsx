import { ExtensionCategory, register, treeToGraphData } from '@antv/g6';
import { t } from '@bmos/i18n';
import { VueNode } from '@zwight/g6-extension-vue';
import { message } from 'ant-design-vue';
import { ref } from 'vue';
import InputDoc from '../component/InputDoc.vue';
import IntermediateNode from '../component/IntermediateNode.vue';
import ProduceNode from '../component/ProduceNode.vue';

export const useData = (props: any) => {
  register(ExtensionCategory.NODE, 'vue_node', VueNode);
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
  const mindMapConfig = ref<any>({});
  const treeData = ref<any>([]);
  const selectBatch = ref();
  const MindMapRef = ref();
  const saveSelectId = ref('');
  const allObjectData = ref<any>({});
  const formatTreeData = (data: any) => {
    let mapTreeData = {
      ...data,
      data: {
        ...data,
        type: '',
      },
      children: [],
    };
    mapTreeData.children.push({
      id: data.id + 'copy',
      data: {
        ...data,
        type: 'out',
      },
    });
    data.children?.forEach((item: any) => {
      mapTreeData.children.push({
        id: item.id,
        data: {
          ...item,
          type: 'input',
        },
      });
    });
    return mapTreeData || {};
  };
  const treeSelect = (_value: any, node: any) => {
    if (node.materialCategoryType.value == 0) {
      message.error(t('原辅包批次无产出信息'));
      selectBatch.value = saveSelectId.value;
      return;
    }
    saveSelectId.value = selectBatch.value;
    const newTreeData = formatTreeData(node);
    MindMapRef.value.setData(treeToGraphData(newTreeData));
  };
  const openNext = (data: any) => {
    selectBatch.value = data.id;
    saveSelectId.value = data.id;
    treeSelect('', data);
  };
  const openManager = (data: any) => {
    const newData = allObjectData.value[data.id];
    selectBatch.value = newData.id;
    saveSelectId.value = newData.id;
    treeSelect('', newData);
  };
  // 初始化流程图
  const arrangementTreeData = (data: any) => {
    let mapTreeData = formatTreeData(data);
    // 流程图配置
    mindMapConfig.value = {
      data: treeToGraphData(mapTreeData),
      node: {
        type: 'vue_node',
        style: (d: any) => {
          const style = {
            size: d.children ? [320, 80] : [260, 140],
            ports: [{ placement: 'left' }, { placement: 'right' }],
          };
          if (d.data.type == 'input') {
            // 左边(产出)
            Object.assign(style, {
              component: <InputDoc data={{ id: d.id, data: d.data }} onOpenNext={openNext} />,
            });
          } else if (d.data.type == 'out') {
            // 右边
            Object.assign(style, {
              component: <ProduceNode data={{ id: d.id, data: d.data }} onOpenManager={openManager} />,
            });
          } else {
            // 中间
            Object.assign(style, {
              component: <IntermediateNode data={{ id: d.id, data: d.data }} />,
            });
          }
          return style;
        },
      },
      layout: {
        type: 'mindmap',
        direction: 'H',
        indent: 300,
        getVGap: () => 30,
        getHGap: () => 240,
        getHeight: () => 100,
        preventOverlap: true, // 防重叠
        getSide: ({ data }: any) => {
          if (data.data.type === 'input') {
            return 'left';
          }
          return 'right';
        },
      },
      behaviors: ['drag-canvas'],
    };
  };
  const getAllObjectData = (data: any) => {
    console.log('=======getAllObjectDatadata', data);
    if (!data.children) {
      return;
    }
    data.children.map((item: any) => {
      allObjectData.value[item.id] = data;
      getAllObjectData(item);
    });
  };
  onMounted(async () => {
    // const { data } = await getMaterialTraceData(props.rowData.id);
    const data = props.rowData.materialTraceData;
    treeData.value = data.traceTree;
    getAllObjectData(treeData.value[0]);
    arrangementTreeData(data.traceTree[0]);
    selectBatch.value = data.traceTree[0]?.id;
    saveSelectId.value = selectBatch.value;
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
    selectBatch,
    treeData,
    MindMapRef,
    treeSelect,
  };
};
