<template>
  <div class="board-card">
    <div class="card-header">
      <BMIcons
        :icon="icon"
        :style="{
          fontSize: '20px',
          width: '20px',
          height: '20px',
        }" />
      <div class="card-title">{{ title }}</div>
    </div>
    <div class="card-data">
      <span class="data">{{ data ?? 0 }}</span>
      <span :class="percentClass">{{ percent ?? '-' }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMIcons } from '@bmos/icons';

  const props = defineProps({
    icon: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    data: {
      type: [String, Number],
      default: 0,
    },
    percent: {
      type: String,
      default: '',
    },
  });

  // computed 判断 percent 是否为 正数百分比 还是 负数百分比
  const percentClass = computed(() => {
    return props.percent.includes('-') ? 'percent negative' : 'percent positive';
  });
</script>

<style scoped lang="less">
  .board-card {
    // height: 80px;
    background: #fff;
    display: flex;
    flex-direction: column;
    gap: 4px;
    padding: 4px;
    .card-header {
      display: flex;
      gap: var(--bmos-margin-small);
      .card-title {
        font-size: 14px;
        color: var(--bmos-fourth-level-text-color);
      }
    }
    .card-data {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
      .data {
        font-size: 36px;
        font-weight: 500;
        line-height: 44px;
        color: var(--bmos-first-level-text-color);
      }
      .percent {
        font-size: 14px;
        color: var(--bmos-success-color);
      }
      .negative {
        color: #ff1313;
      }
      .positive {
        color: var(--bmos-success-color);
      }
    }
  }
</style>
