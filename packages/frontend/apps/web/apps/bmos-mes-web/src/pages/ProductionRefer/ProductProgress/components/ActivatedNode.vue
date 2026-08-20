<template>
  <div :class="['flow-basic-node']">
    <BMIcons
      icon="Activated"
      :style="{
        width: '20px',
        height: '20px',
        color: '#99E6FF',
      }" />
    <div class="label">
      <span class="name">{{ labelName }}</span>
    </div>
    <template v-if="showUsers">
      <Divider type="vertical" />
      <BMIcons icon="Users" class="action-icon" @click.stop="clickShowUsers" />
    </template>
  </div>
</template>

<script lang="tsx" setup>
  import { Cell } from '@antv/x6';
  import { BMIcons } from '@bmos/icons';

  defineProps({
    showUsers: {
      type: Boolean,
      default: false,
    },
  });
  const emit = defineEmits(['clickShowUsers']);
  const getNode = inject('getNode') as () => Cell;
  const node = ref<any>({});
  const labelName = ref<string>('');
  const clickShowUsers = () => {
    emit('clickShowUsers', node.value as Cell);
  };
  onMounted(() => {
    try {
      node.value = getNode();
      labelName.value = node.value.data?.label || '';
    } catch (error) {}
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-basic-node {
    width: 100%;
    height: 100%;
    border-radius: 10px;
    background-color: var(--bmos-primary-color-white);
    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.15);
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 10px;
    .label {
      line-height: 1.3em;
      width: 100px;
      text-align: center;
      flex: 1;
      display: flex;
      flex-direction: column;
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: 18px;
    }
  }
</style>
