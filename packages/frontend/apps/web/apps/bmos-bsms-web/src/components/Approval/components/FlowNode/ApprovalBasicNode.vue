<template>
  <div :class="['flow-basic-node', state === 4 ? '' : 'disabled']">
    <BMIcon :type="leftIcon" />
    <div class="label">{{ labelName }}</div>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { Cell } from '@antv/x6';
  import { BMIcon, BMIconComponentMapType } from '@bmos/components';

  defineProps({
    leftIcon: {
      type: String as PropType<BMIconComponentMapType>,
      default: 'Process2',
    },
    nextIcon: {
      type: String as PropType<BMIconComponentMapType>,
      default: 'ProcessNext',
    },
  });

  const getNode = inject('getNode') as () => Cell;

  const node = ref<any>({});
  const labelName = ref<string>('');
  const state = ref<number>(0);

  onMounted(() => {
    node.value = getNode();
    labelName.value = node.value.data?.label || '';
    try {
      state.value = node.value.data?.state || 0;
    } catch (error) {}
    node.value.setData({ isAllowClick: state.value === 4 || state.value === 1 });
    node.value.on('change:data', ({ current }: any) => {
      const { label } = current;
      labelName.value = label;
    });
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-basic-node {
    width: 100%;
    height: 100%;
    border-radius: 10px;
    background-color: var(--bmos-primary-color-white);
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 10px;
    border: 1px solid var(--bmos-primary-color-white);
    .bsms-divider-vertical {
      height: 1.5em;
    }
    .label {
      line-height: 1.3em;
      width: 100px;
      text-align: center;
      flex: 1;
    }
  }

  .disabled {
    background-color: var(--bmos-second-level-border-color);
    border: 1px solid var(--bmos-second-level-border-color);
  }
</style>
