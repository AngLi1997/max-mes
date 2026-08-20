<template>
  <div class="flow-container">
    <div
      id="graph-container"
      @drop="drop"
      @dragover="allowDrop"
      @setting="handleClickSet"></div>
    <LeftToolbar v-if="isShowLeftToolBar" :isView="isView" :leftMap="leftMap" />
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
    <TeleportContainer />
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
  import { register, getTeleport } from '@antv/x6-vue-shape';
  import { useFlowState } from './hooks/useFlowState';
  import { flowEmits } from './type';
  import { useEventListener } from './hooks/useEventListener';
  import { useFlowMethods } from './hooks/useFlowMethods';
  import LeftToolbar from './components/LeftToolbar.vue';
  import TopToolBar from './components/TopToolBar.vue';
  import { BMFlowType } from './hooks';
  import { createFlowContext } from './hooks/useFlowContext';
  import { isEmpty } from '@bmos/utils';

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
            onSetting={(cell: Cell) => handleClickSet(cell)}
            onClickNext={(cell: Cell) => handleClickNext(cell)}
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
  const TeleportContainer = getTeleport();

  const graph = ref<Graph>({} as Graph);

  const flowState = useFlowState({ props, attrs });
  const { defaultGraphConfig, isView, fromJSON } = flowState;

  // @ts-ignore
  const flowMethods = useFlowMethods({ ...flowState, emit, graph });
  const {
    allowDrop,
    initGraph,
    drop,
    undo,
    redo,
    reset,
    zoomIn,
    zoomOut,
    deleteNode,
    handleClickSet,
    handleClickNext,
  } = flowMethods;

  const eventListener = useEventListener({
    ...flowState,
    ...flowMethods,
    emit,
    // @ts-ignore
    graph,
    props,
  });

  const { setEventListener } = eventListener;

  onMounted( async() => {
    await nextTick();
    const options = {
      interacting: !isView.value,
      autoResize: true,
      container: document.getElementById('graph-container')!,
      ...(defaultGraphConfig.value as Graph.Options),
      ...(isEmpty(props.connecting) ? {} : { connecting: props.connecting })
    }
    graph.value = new Graph(options) as Graph;
    initGraph();
    if (fromJSON.value && fromJSON.value.length > 0) {
      await nextTick();
      graph.value.fromJSON(fromJSON.value);
    }
    setEventListener()
  });

  watch(
    () => fromJSON.value,
    async val => {
      if (val && val.length > 0) {
        await nextTick();
        graph.value.fromJSON(val);
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
  };

  createFlowContext(instance);

  defineExpose(instance);

  onUnmounted(() => {
    graph.value.dispose();
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
