<template>
  <div class="flow-end-node">
    <BMIcon type="End" class="end-icon" />
    <div class="label">{{ labelName }}</div>
    <Divider type="vertical" v-if="showDivider" />
    <Space>
      <BMIcon
        v-if="showSetIcon"
        @click.stop="setting"
        target="setting"
        type="Set"
        class="action-icon" />
    </Space>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { BMIcon } from '@bmos/components';
  import { Cell } from '@antv/x6';

  const emit = defineEmits(['setting']);
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

  const setting = () => {
    emit('setting', node.value as Cell);
  };

  onMounted(() => {
    node.value = getNode();
    labelName.value = node.value.data?.label || '';
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
    .mes-divider-vertical {
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
</style>
