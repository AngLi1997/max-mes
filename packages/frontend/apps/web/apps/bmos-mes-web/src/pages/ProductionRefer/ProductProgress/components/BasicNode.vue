<template>
  <div :class="['flow-basic-node', statusIcon]">
    <BMIcons
      icon="NotActivated"
      :style="{
        width: '20px',
        height: '20px',
      }" />
    <div class="label">
      <span class="name">{{ labelName }}</span>
    </div>
    <BMIcons
      icon="Video"
      :style="{
        width: '20px',
        height: '20px',
      }"
      @click.stop="showVideo" />
    <template v-if="showNext">
      <Divider type="vertical" />
      <BMIcon type="ProcessNext" class="action-icon" @click.stop="clickNext" />
    </template>
    <template v-if="showUsers">
      <Divider type="vertical" />
      <BMIcons icon="Users" class="action-icon" @click.stop="clickShowUsers" />
    </template>
  </div>
  <VideoModal v-model:open="videoOpen" />
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { Cell } from '@antv/x6';
  import { BMIcon } from '@bmos/components';
  import { BMIcons } from '@bmos/icons';
  import VideoModal from './VideoModal.vue';

  defineProps({
    showNext: {
      type: Boolean,
      default: true,
    },
    showUsers: {
      type: Boolean,
      default: false,
    },
  });
  const emit = defineEmits(['nextClick', 'clickShowUsers']);

  const getNode = inject('getNode') as () => Cell;

  const node = ref<any>({});
  const labelName = ref<string>('');

  const statusIcon = ref<string>('NotActivated');

  const clickNext = () => {
    emit('nextClick', node.value as Cell);
  };
  const clickShowUsers = () => {
    emit('clickShowUsers', node.value as Cell);
  };

  const videoOpen = ref<boolean>(false);
  const showVideo = () => {
    videoOpen.value = true;
  };

  onMounted(() => {
    try {
      node.value = getNode();
      labelName.value = node.value.data?.label || '';
      node.value.setData({ isAllowClick: false });
    } catch (error) {}
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-basic-node {
    width: 100%;
    height: 100%;
    border-radius: 10px;
    background-color: #f0f1f2;
    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.15);
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 10px;
    .mes-divider-vertical {
      height: 1.5em;
    }
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
