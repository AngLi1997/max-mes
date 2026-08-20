<template>
  <div class="node-container node-container-bordered" @click.stop="nodeClick">
    <div class="node-content-container">
      <span class="node-content-icon"><BMIcons :icon="item?.icon" /></span>
      <span>{{ item?.componentName }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMIcons } from '@bmos/icons';
  import { nodePropsType, NodeDataType } from '../type';

  const emit = defineEmits(['node-click']);
  // eslint-disable-next-line vue/no-reserved-props
  const props = withDefaults(defineProps<nodePropsType & { showIcon: boolean; lookup: boolean }>(), {
    title: 'No.1',
    type: t('数字'),
    actived: false,
    rounded: true,
    bordered: true,
    key: '',
    icon: '',
    showIcon: true,
    componentNumber: 0,
    lookup: false,
  });

  const NODE_DATA = computed<NodeDataType>(() => {
    const { title, type, item, actived, nodeKey } = props;
    const clickData: NodeDataType = {
      title,
      type,
      data: item,
      actived,
      key: nodeKey,
    };

    return clickData;
  });

  const nodeClick = () => {
    emit('node-click', NODE_DATA.value);
  };
</script>

<style scoped lang="less">
  .node-content-icon {
    font-size: 20px;
    line-height: 1;
    font-weight: 400;
  }
  .node-container-bordered {
    border: 1px solid rgba(212, 215, 217, 1);
    border-radius: 4px;
  }
</style>
