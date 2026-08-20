<template>
  <div :class="['flow-end-node', state === 4 ? '' : 'disabled']">
    <BMIcon type="End" class="end-icon" />
    <div class="label">{{ labelName }}</div>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { BMIcon } from '@bmos/components';
  import { Cell } from '@antv/x6';

  defineProps({
    showSetIcon: {
      type: Boolean,
      default: true,
    },
    showDivider: {
      type: Boolean,
      default: true,
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
    } catch (error) {
      //
    }
    node.value.setData({ isAllowClick: state.value === 4 || state.value === 1 });
    node.value.prop('size', { width: 120, height: 44 });
    node.value.on('change:data', ({ current }: any) => {
      const { label } = current;
      labelName.value = label;
    });
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-end-node {
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
    .ems-divider-vertical {
      height: 1.5em;
    }
    .label {
      line-height: 1.3em;
      width: 100px;
      text-align: center;
      flex: 1;
    }
  }
  .end-icon {
    color: var(--bmos-danger-color);
  }
  .action-icon {
    color: var(--bmos-primary-color);
    cursor: pointer;
  }
  .disabled {
    background-color: var(--bmos-second-level-border-color);
    border: 1px solid var(--bmos-second-level-border-color);
  }
</style>
