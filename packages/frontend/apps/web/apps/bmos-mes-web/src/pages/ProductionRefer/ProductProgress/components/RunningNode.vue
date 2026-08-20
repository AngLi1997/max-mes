<template>
  <div :class="['flow-basic-node']">
    <BMIcons
      icon="InProgress"
      :style="{
        width: '20px',
        height: '20px',
        color: '#B3CBFF',
      }" />
    <div class="label">
      <span class="name">{{ labelName }}</span>
    </div>
    <BMIcons
      icon="Video"
      :style="{
        width: '20px',
        height: '20px',
        color: '#B3CBFF',
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
  import { Cell } from '@antv/x6';
  import { BMIcon } from '@bmos/components';
  import { BMIcons } from '@bmos/icons';
  import VideoModal from './VideoModal.vue';

  const emit = defineEmits(['nextClick', 'clickShowUsers']);
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
  const getNode = inject('getNode') as () => Cell;

  const node = ref<any>({});
  const labelName = ref<string>('');

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
    } catch (error) {}
  });
</script>

<style scoped lang="less">
  /* Your CSS code here */
  .flow-basic-node {
    width: 100%;
    height: 100%;
    border-radius: 10px;
    background-color: var(--bmos-primary-color-background);
    display: flex;
    align-items: center;
    justify-content: space-around;
    line-height: 44px;
    padding: 0 10px;
    border: 1px solid var(--bmos-primary-color);
    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.15);
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
      .name {
      }
    }
  }
</style>
