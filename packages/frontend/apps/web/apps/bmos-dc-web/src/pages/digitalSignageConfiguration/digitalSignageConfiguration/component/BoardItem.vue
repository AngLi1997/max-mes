<template>
  <div :class="{ board_item: true, isClick: clickId == data.filedId }">
    <div>{{ data.title || t('看板') + data.filedId }}</div>
    <BMIcons class="delete-icon" icon="Delete" @click.stop="() => deleteConditionList()" />
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  const props = defineProps({
    clickId: {
      type: Number,
      default: 0,
    },
    data: {
      type: Object,
      default: () => {},
    },
  });
  const emit = defineEmits(['delete']);
  const deleteConditionList = () => {
    emit('delete', props.data.filedId, props.data.id);
  };
</script>
<style scoped lang="less">
  .board_item {
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 12px;
    margin-bottom: 8px;
    cursor: pointer;
    border-radius: 4px;
  }
  .isClick {
    color: #2871ff;
    background: #ebf1ff;
  }
</style>
