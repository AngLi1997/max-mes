<template>
  <div class="list-content">
    <div v-for="item in taskList" :key="item.id" :class="['item', getClass(item.state)]">
      <div class="header">
        <div class="name">
          <BMIcons
            :icon="getIcon(item.state)"
            :style="{
              width: '20px',
              height: '20px',
              marginRight: '6px',
              ...(getIconColor(item.state) ? { color: getIconColor(item.state) } : {}),
            }" />
          {{ item.name }}
        </div>
        <div class="icon" @click="() => clickShowUsers(item)">
          <BMIcons
            icon="Users"
            :style="{
              width: '20px',
              height: '20px',
            }" />
        </div>
      </div>
      <template v-if="item.state === StateEnum.COMPLETE || item.state === StateEnum.IS_END">
        <Divider />
        <span class="complete-user">{{ t('完成人') }}：{{ item.updateBy || '-' }}</span>
        <span class="complete-time">{{ t('完成时间') }}：{{ item.completeTime || '-' }}</span>
      </template>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { BMIcons } from '@bmos/icons';
  import { t } from '@bmos/i18n';
  import { Divider } from 'ant-design-vue';
  import { Recordable } from '@bmos/components';
  import { StateEnum } from '../enum';

  const emit = defineEmits(['clickShowUsers']);

  defineProps({
    taskList: {
      type: Array as PropType<any>,
      default: () => [],
    },
  });

  const statusMap: Map<
    StateEnum,
    {
      icon: string;
      class: string;
      color?: string;
    }
  > = new Map([
    [StateEnum.ACTIVE, { icon: 'InProgress', color: '#B3CBFF', class: 'in-progress' }],
    [StateEnum.INACTIVE, { icon: 'NotActivated', class: 'not-activated' }],
    [StateEnum.COMPLETE, { icon: 'Completed', class: 'completed' }],
    [StateEnum.IS_END, { icon: 'ProgressEnd', class: 'completed' }],
    [StateEnum.IS_ACTIVE, { icon: 'Activated', color: '#99E6FF', class: 'activated' }],
  ]);

  const getIcon = (state: StateEnum) => {
    return statusMap.get(state)?.icon || 'NotActivated';
  };
  const getIconColor = (state: StateEnum) => {
    return statusMap.get(state)?.color || '';
  };
  const getClass = (state: StateEnum) => {
    return statusMap.get(state)?.icon || 'NotActivated';
  };

  const clickShowUsers = (item: Recordable) => {
    emit('clickShowUsers', item, 1);
  };
</script>
<style scoped lang="less">
  .list-content {
    display: flex;
    flex-direction: column;
    gap: 10px;
    .item {
      display: flex;
      padding: 10px 12px;
      border: 1px solid var(--bmos-first-level-border-color);
      border-radius: 4px;
      flex-direction: column;
      .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        .name {
          flex: 1;
          max-width: 100%;
          display: flex;
          align-items: center;
        }
      }
      .mes-divider-horizontal {
        margin: var(--bmos-margin-small) 0;
      }
      .complete-user,
      .complete-time {
        color: var(--bmos-third-level-text-color);
      }
    }
    .not-activated {
      background-color: #f0f1f2;
    }
    .in-progress {
      border: 1px solid var(--bmos-primary-color);
    }
    .completed {
    }
  }
</style>
