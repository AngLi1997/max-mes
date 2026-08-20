<template>
  <div class="list-content">
    <div v-for="item in taskList" :key="item.id" class="item">
      <div class="name">
        <BMEllipsis :otherWidth="10">
          <template #title>
            <span>
              {{ item.name }}
            </span>
          </template>
          {{ item.name }}
        </BMEllipsis>
      </div>
      <div class="actions">
        <BMIcons icon="Set" @click="() => handleClickSet(item)" />
        <BMIcons
          v-if="
            item.nodeFunction !== NodeFunctionEnum.ProcedureShift &&
            item.nodeFunction !== NodeFunctionEnum.ProcessShift &&
            item.nodeFunction !== NodeFunctionEnum.Inspection
          "
          icon="File"
          @click="() => handleFile(item)" />
        <BMIcons
          v-if="!isView"
          icon="Delete"
          style="color: var(--bmos-danger-color)"
          @click.stop="() => deleteTask(item)" />
      </div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { BMIcons } from '@bmos/icons';
  import { BMEllipsis, Recordable } from '@bmos/components';
  import { NodeFunctionEnum } from '../types';

  const emit = defineEmits(['deleteTask', 'handleClickNext', 'handleClickSet']);
  defineProps({
    taskList: {
      type: Array as PropType<any>,
      default: () => [],
    },
    isView: {
      type: Boolean as PropType<boolean>,
      default: false,
    },
  });

  const deleteTask = (item: Recordable) => {
    emit('deleteTask', item);
  };
  const handleFile = (item: Recordable) => {
    emit('handleClickNext', item);
  };
  const handleClickSet = (item: Recordable) => {
    emit('handleClickSet', item);
  };
</script>
<style scoped lang="less">
  .list-content {
    display: flex;
    flex-direction: column;
    gap: 10px;
    .item {
      display: flex;
      justify-content: space-between;
      padding: 10px 12px;
      border: 1px solid var(--bmos-first-level-border-color);
      border-radius: 4px;
      align-items: center;
      .name {
        flex: 1;
        max-width: 100%;
        overflow: hidden;
        display: flex;
        align-items: center;
      }
      .actions {
        display: none;
        gap: 12px;
        align-items: center;
      }
    }
    .item:hover {
      background-color: var(--bmos-background-color);
      .actions {
        display: flex;
      }
    }
  }
</style>
