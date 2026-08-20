<template>
  <div :class="['flow-basic-node']">
    <BMIcons
      :icon="icon"
      :style="{
        width: '20px',
        height: '20px',
      }" />
    <div class="label">
      <span class="name">{{ labelName }}</span>
      <span class="complete-user">{{ t('完成人') }}：{{ nodeData?.completeBy }}</span>
      <span class="complete-time">{{ t('完成时间') }}：{{ nodeData?.endTime }}</span>
    </div>
    <template v-if="showNext">
      <Divider type="vertical" />
      <BMIcon type="ProcessNext" class="action-icon" @click.stop="clickNext" />
    </template>
    <template v-if="showUsers">
      <Divider type="vertical" />
      <BMIcons icon="Users" class="action-icon" @click.stop="clickShowUsers" />
    </template>
  </div>
</template>

<script lang="tsx" setup>
  import { inject, ref, onMounted } from 'vue';
  import { Cell } from '@antv/x6';
  import { BMIcon } from '@bmos/components';
  import BMIcons from '@bmos/icons';
  import { t } from '@bmos/i18n';

  const emit = defineEmits(['nextClick', 'clickShowUsers']);
  defineProps({
    icon: {
      type: String,
      default: 'Completed',
    },
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
  const nodeData = ref<any>({});

  const clickNext = () => {
    emit('nextClick', node.value as Cell);
  };
  const clickShowUsers = () => {
    emit('clickShowUsers', node.value as Cell);
  };
  onMounted(() => {
    try {
      node.value = getNode();
      labelName.value = node.value.data?.label || '';
      nodeData.value = node.value.data;
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
    border: 1px solid var(--bmos-primary-color-white);
    .mes-divider-vertical {
      height: 1.5em;
    }
    .label {
      line-height: 1.3em;
      width: 100px;
      flex: 1;
      display: flex;
      flex-direction: column;
      font-size: 14px;
      font-style: normal;
      font-weight: 400;
      line-height: 18px;
      margin-left: var(--bmos-margin-small);
      .name {
      }
      .complete-user,
      .complete-time {
        color: var(--bmos-third-level-text-color);
      }
    }
  }
  .is-running {
    border: 2px dashed var(--bmos-primary-color) !important;
    background-color: var(--bmos-primary-color-background);
  }
</style>
