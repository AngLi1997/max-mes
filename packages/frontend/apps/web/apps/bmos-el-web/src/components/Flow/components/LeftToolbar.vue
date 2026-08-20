<template>
  <div class="flow-left-toolbar">
    <div v-for="(item, key) in leftMap" :key="key" class="group">
      {{ item.title }}
      <Divider />
      <div class="drag-icon" :draggable="isView ? false : true" @dragstart="dragstart($event, item)">
        <component :is="item.icon" />
      </div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { Divider } from 'ant-design-vue';
  import { FlowLeftToolBar } from '../type/toolBar';

  const props = defineProps({
    isView: {
      type: Boolean,
      default: false,
    },
    isProcedure: {
      type: Boolean,
      default: false,
    },
    leftMap: {
      type: Array as PropType<FlowLeftToolBar[]>,
      default: () => [],
    },
  });

  const leftMap = computed<FlowLeftToolBar[]>(() => {
    return props.leftMap;
  });

  const dragstart = (e: any, item: FlowLeftToolBar) => {
    if (props.isView) return;
    let data = item;
    e.dataTransfer.setData('data', JSON.stringify(data));
    e.dataTransfer.effectAllowed = 'move';
  };
</script>

<style scoped lang="less">
  .flow-left-toolbar {
    width: 54px;
    box-shadow: 0px 0px 10px 0px #00000040;
    background: #ffffff;
    border-radius: 5px;
    padding: 6px 4px 12px 4px;
    position: absolute;
    top: 20px;
    left: 20px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    .dc-divider-horizontal {
      margin-top: 5px;
      margin-bottom: var(--bmos-margin-medium);
    }
    .group {
      width: 100%;
      height: 50%;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      margin-top: 20px;
    }
    .drag-icon {
      font-size: 16px;
      cursor: grab;
    }
  }
  // 选中 第一个 group
  .flow-left-toolbar .group:nth-child(1) {
    margin-top: 0;
  }
</style>
