<template>
  <div class="item">
    <div class="content">
      <span class="title">{{ msgContent.title }}</span>
      <template v-if="!upDown && msgContent.content">
        <div class="msg-content">{{ msgContent.content }}</div>
      </template>
      <span class="time">{{ setTime(msgContent.time) }}</span>
    </div>
    <div v-if="showReadIcon" class="read">
      <BMIcons icon="SuccessGray" class="read-icon" @click="handleClickRead"></BMIcons>
    </div>
    <div class="right-icon">
      <BMIcons v-if="upDown" icon="DownOutlined" class="up-down-icon" @click="handleClickUpDown"></BMIcons>
      <BMIcons v-else icon="Up" class="up-icon" @click="handleClickUpDown"></BMIcons>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { NotifyMessageItemType } from '../../types';
  import { BMIcons } from '@bmos/icons';
  import { reqPlasmaNoticeAllRead } from '../../../api/info';
  import { t } from '@bmos/i18n';

  const props = withDefaults(
    defineProps<{
      item: NotifyMessageItemType;
      showReadIcon?: boolean;
    }>(),
    {
      item: () => ({}) as NotifyMessageItemType,
      showReadIcon: true,
    },
  );
  const emit = defineEmits(['read']);

  const msgContent = computed(() => {
    try {
      return JSON.parse(props.item.msgContent);
    } catch (error) {
      return '';
    }
  });

  const upDown = ref<boolean>(true);
  const handleClickUpDown = () => {
    upDown.value = !upDown.value;
  };

  const setTime = (time: string) => {
    // 时间戳 判断是否为今天 如果是 显示 为 '今天 23:59:59', 否则显示 '2024-12-31 23:59:59'
    try {
      const today = new Date().toLocaleDateString();
      const date = new Date(time).toLocaleDateString();
      const timeString = new Date(time).toTimeString().slice(0, 8); // 获取时间部分

      if (today === date) {
        return `${t('今天')} ${timeString}`;
      }
      return `${date} ${timeString}`;
    } catch (error) {
      return time;
    }
  };

  const handleClickRead = async () => {
    try {
      await reqPlasmaNoticeAllRead({
        all: false,
        ids: [props.item.id],
      });
      emit('read', props.item);
    } catch (error) {
      console.log(error);
    }
  };
</script>
<style lang="less">
  .item {
    border-bottom: 1px solid var(--bmos-second-level-border-color);
    padding: var(--bmos-padding-small);
    display: flex;
    justify-content: space-between;
    gap: 20px;
  }
  .item:hover {
    background-color: var(--bmos-table-td-color);
  }
  .content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 10px;
    .title {
      color: var(--bmos-second-level-text-color);
    }
    .detail {
      color: var(--bmos-third-level-text-color);
      display: flex;
      gap: 4px;
      .label {
        width: 70px;
      }
      .value {
        flex: 1;
      }
    }
    .time {
      color: var(--bmos-third-level-text-color);
    }
    .msg-content {
      white-space: pre-wrap;
    }
  }
  .read {
    width: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    .read-icon {
      width: 20px;
      height: 20px;
      color: var(--bmos-second-level-border-color);
    }
    .read-icon:hover {
      color: var(--bmos-success-color);
    }
  }
  .right-icon {
    width: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    .up-down-icon {
      width: 12px;
      height: 12px;
    }
    .up-icon {
      width: 24px;
      height: 24px;
    }
  }
</style>
