<template>
  <div class="flow-basic-node">
    <BMIcon :type="leftIcon" />
    <div class="label">{{ labelName }}</div>
    <Divider v-if="showDivider" type="vertical" />
    <Space>
      <BMIcon v-if="showSetIcon" target="setting" type="Set" class="action-icon" @click.stop="setting" />
      <BMIcon v-if="showNextIcon" :type="nextIcon" class="action-icon" @click.stop="clickNext" />
    </Space>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { Space, Divider } from 'ant-design-vue';
  import { Cell } from '@antv/x6';
  import { BMIcon, BMIconComponentMapType } from '@bmos/components';

  const emit = defineEmits(['setting', 'clickNext', 'clickFile']);
  defineProps({
    leftIcon: {
      type: String as PropType<BMIconComponentMapType>,
      default: 'Process2',
    },
    nextIcon: {
      type: String as PropType<BMIconComponentMapType>,
      default: 'ProcessNext',
    },
    showNextIcon: {
      type: Boolean,
      default: true,
    },
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

  const clickNext = () => {
    emit('clickNext', node.value as Cell);
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

  .action-icon {
    color: var(--bmos-primary-color);
    cursor: pointer;
  }
</style>
