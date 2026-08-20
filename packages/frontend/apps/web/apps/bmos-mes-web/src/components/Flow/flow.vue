<template>
  <div :key="flowKey" class="flow-container">
    <div id="graph-container" @setting="handleClickSet"></div>
    <LeftToolbar
      v-if="isShowLeftToolBar"
      ref="leftToolRef"
      :isView="isView"
      :leftMap="leftMap"
      @dragstartNode="dragstartNode" />

    <slot name="custom" v-bind="instance"></slot>
    <TopToolBar
      v-if="isShowTopToolBar"
      :isView="isView"
      :showUndo="showUndo"
      :showRedo="showRedo"
      :showReset="showReset"
      :showZoomIn="showZoomIn"
      :showZoomOut="showZoomOut"
      :showDelete="showDelete"
      @undo="undo"
      @redo="redo"
      @reset="reset"
      @zoomIn="zoomIn"
      @zoomOut="zoomOut"
      @delete="deleteNode" />
  </div>
</template>

<script setup lang="tsx">
  import { onMounted, ref, useAttrs, onUnmounted, watch } from 'vue';
  import { flowProps } from './type';
  import BasicNode from './components/BasicNode.vue';
  import StartNode from './components/StartNode.vue';
  import EndNode from './components/EndNode.vue';
  import GatewayNode from './components/GatewayNode.vue';
  import { Cell, Graph } from '@antv/x6';
  import { register } from '@antv/x6-vue-shape';
  import { useFlowState } from './hooks/useFlowState';
  import { flowEmits } from './type';
  import { useEventListener } from './hooks/useEventListener';
  import { useFlowMethods } from './hooks/useFlowMethods';
  import LeftToolbar from './components/LeftToolbar.vue';
  import TopToolBar from './components/TopToolBar.vue';
  import { BMFlowType } from './hooks';
  import { createFlowContext } from './hooks/useFlowContext';
  import { isEmpty } from '@bmos/utils';
  import { Dnd } from '@antv/x6-plugin-dnd';

  const props = defineProps(flowProps);
  const emit = defineEmits(flowEmits);
  const attrs = useAttrs();
  register({
    shape: 'custom-vue-node',
    component: {
      render() {
        return (
          <BasicNode
            nextIcon={props.nextIcon}
            leftIcon={props.leftIcon}
            showNextIcon={props.showNextIcon}
            showSetIcon={props.showSetIcon}
            showDivider={props.showDivider}
            onSetting={(cell: Cell) => handleClickSet(cell, 'custom-vue-node')}
            onClickNext={(cell: Cell) => handleClickNext(cell, 'custom-vue-node')}
          />
        );
      },
    },
  });
  register({
    shape: 'custom-task-node',
    component: {
      render() {
        return (
          <BasicNode
            nextIcon={props.taskNextIcon}
            leftIcon={props.taskLeftIcon}
            showNextIcon={props.showTaskNextIcon}
            showSetIcon={props.showTaskSetIcon}
            showDivider={props.showTaskDivider}
            onSetting={(cell: Cell) => handleClickSet(cell, 'custom-task-node')}
            onClickNext={(cell: Cell) => handleClickNext(cell, 'custom-task-node')}
          />
        );
      },
    },
  });
  register({
    shape: 'custom-vue-start-node',
    component: StartNode,
    width: 102,
  });
  register({
    shape: 'custom-vue-end-node',
    component: EndNode,
  });
  register({
    shape: 'custom-vue-gateway-node',
    component: GatewayNode,
  });

  const graph = ref<Graph>({} as Graph);

  const flowState = useFlowState({ props, attrs });
  const { defaultGraphConfig, isView, fromJSON, leftToolRef, dnd } = flowState;

  // @ts-ignore
  const flowMethods = useFlowMethods({ ...flowState, emit, graph });
  const { dragstartNode, initGraph, undo, redo, reset, zoomIn, zoomOut, deleteNode, handleClickSet, handleClickNext } =
    flowMethods;

  const eventListener = useEventListener({
    ...flowState,
    ...flowMethods,
    emit,
    // @ts-ignore
    graph,
    props,
  });

  const { setEventListener } = eventListener;

  const initFn = async () => {
    await nextTick();
    const options = {
      interacting: {
        edgeMovable: !isView.value,
        edgeLabelMovable: !isView.value,
        arrowheadMovable: !isView.value,
        vertexMovable: !isView.value,
        vertexAddable: !isView.value,
        vertexDeletable: !isView.value,
        useEdgeTools: !isView.value,
        nodeMovable: true,
        magnetConnectable: !isView.value,
        stopDelegateOnDragging: !isView.value,
        toolsAddable: !isView.value,
      },
      autoResize: true,
      container: document.getElementById('graph-container')!,
      ...(defaultGraphConfig.value as Graph.Options),
      ...(isEmpty(props.connecting) ? {} : { connecting: props.connecting }),
    };
    if (graph.value.dispose) {
      graph.value.dispose();
    }
    graph.value = new Graph(options) as Graph;
    initGraph();
    if (fromJSON.value && fromJSON.value.length > 0) {
      await nextTick();
      graph.value?.fromJSON(fromJSON.value);
      await nextTick();
      setTimeout(() => {
        reset();
      }, 100);
    }
    setEventListener();
    dnd.value = new Dnd({ target: graph.value as Graph });
  };
  const flowKey = ref(Math.random());
  onMounted(async () => {
    await initFn();
  });

  watch(
    () => fromJSON.value,
    async val => {
      if (val && val.length > 0) {
        await nextTick();
        graph.value.fromJSON(val);
        await nextTick();
        setTimeout(() => {
          reset();
        }, 100);
      }
    },
    {
      deep: true,
    },
  );

  // 当前组件所有的状态和方法
  const instance = {
    ...flowState,
    ...flowMethods,
    ...eventListener,
    graph,
    register,
  } as BMFlowType & {
    register: typeof register;
    graph: Ref<Graph>;
  };
  createFlowContext(instance);

  defineExpose(instance);

  onUnmounted(() => {
    graph.value.dispose();
  });

  onActivated(() => {
    flowKey.value = Math.random();
    initFn();
  });
</script>

<style scoped lang="less">
  .flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    .graph-container {
      width: 100%;
      height: 100%;
    }
    :deep(.x6-node-selected) {
      .flow-basic-node {
        border: 1px solid var(--bmos-primary-color);
      }
      .flow-start-node {
        border: 1px solid var(--bmos-success-color);
      }
      .flow-end-node {
        border: 1px solid var(--bmos-danger-color);
      }
    }
  }
</style>
