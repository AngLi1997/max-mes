<template>
  <div id="container"></div>
</template>

<script setup lang="ts">
  import { onMounted } from 'vue';
  import { Graph } from '@antv/g6';

  const props = defineProps({
    config: {
      type: Object,
      default: null,
    },
    boxId: {
      // 父组件id
      type: String,
      default: null,
    },
  });
  let graph = {} as any;

  const defaultConfig = ref<any>({
    container: 'container',
    trigger: 'click',
    height: 500,
    edge: {
      type: 'cubic-horizontal',
      animation: {
        enter: false,
      },
    },
    animation: {
      duration: 300,
    },
    layout: {
      //  布局
      type: 'indented',
      direction: 'LR',
      dropCap: false,
      indent: 300,
      getHeight: () => 140,
      preventOverlap: true, // 防重叠
    },
    behaviors: ['drag-canvas'], // 允许拖拽画布
  });

  onMounted(() => {
    if (props.boxId) {
      // 自动获取父组件高度并撑满
      const height = document.getElementById(props.boxId)?.clientHeight || 500;
      defaultConfig.value.height = height;
    }
    graph = new Graph({
      ...defaultConfig.value,
      ...props.config,
    });
    graph.render();
  });
  // 收起某个节点
  const collapseElement = (id: any) => {
    graph.collapseElement(id, true);
  };
  // 展开某个节点
  const expandElement = (id: any) => {
    graph.expandElement(id, true);
  };
  // 聚焦到指定元素
  const focusElement = (id: any) => {
    graph.focusElement(id, true);
  };
  // 设置新数据
  const setData = async (data: any) => {
    graph.setData(data);
    await graph.render();
  };
  defineExpose({
    graph,
    collapseElement,
    expandElement,
    focusElement,
    setData,
  });
</script>
