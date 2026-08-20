<template>
  <view class="home-container">
    <BMTabBar class="diy-tab-bar" :total="todoCount" />
    <view class="home-content">
      <Todo v-show="selectedId === 0" />
      <Workbench v-show="selectedId === 1" />
      <PerSonal v-show="selectedId === 2" />
      <Kanban v-if="selectedId === 3" />
    </view>
  </view>
</template>

<script setup>
import { BMTabBar } from '@/BMComponents';
import { useTabbarStore } from '@/stores/tabbar.js';
import { getSystemNullValue } from '@/utils/systemConfig/index.js';
import { onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import Kanban from './kanban/index.vue';
import PerSonal from './personal/index.vue';
import Todo from './todo/index.vue';
import Workbench from './workbench/index.vue';

const tabBarStore = useTabbarStore();
const { selectedId, todoCount } = storeToRefs(tabBarStore);
onShow(() => {
  getSystemNullValue();
});
</script>

<style lang="scss" scoped>
.home-container {
  display: flex;
  height: 100%;
  width: 100%;
}

.diy-tab-bar {
  flex-shrink: 0;
}

.home-content {
  flex: 1;
  height: 100%;
  overflow-y: auto;
}
</style>
