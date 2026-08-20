<template>
  <div class="item-main">
    <img class="item-icon" :src="props.icon && iconMap[props.icon]" />
    <div>
      <Statistic :title="props.title" :value="props.value" :valueStyle="{ fontSize: '20px', lineHeight: '20px' }" />
      <component :is="props.slot" v-if="props.slot"></component>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { currentPlasma, currentSample, outbound, sortingPlasma, waitingPlasma, waitingSample } from './icons';
  import { Statistic } from 'ant-design-vue';

  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    value: {
      type: [Number, String],
      default: 0,
    },
    icon: {
      type: String as () =>
        | ''
        | 'currentPlasma'
        | 'currentSample'
        | 'outbound'
        | 'sortingPlasma'
        | 'waitingPlasma'
        | 'waitingSample',
      default: '',
    },
    slot: {
      type: Object,
      default: null,
    },
  });

  const iconMap = {
    currentPlasma,
    currentSample,
    outbound,
    sortingPlasma,
    waitingPlasma,
    waitingSample,
  };
</script>

<style lang="less" scoped>
  .item-main {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 100%;
    // height: 60px;
    // margin-bottom: 16px;
    .item-icon {
      width: 40px;
      height: 40px;
      margin-right: 8px;
    }
    :deep(.bsms-statistic-title) {
      margin: 0;
    }
  }
</style>
