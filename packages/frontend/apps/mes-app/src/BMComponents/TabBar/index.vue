<template>
  <view class="bmos-tabBar">
    <view class="navigator">
      <view
        v-for="item in tabBarList"
        :key="item.pagePath"
        class="navigator-item" :class="[
          {
            'navigator-item-active': selectedId === item.id,
          },
        ]"
        @click="switchTab(item.id)"
      >
        <wd-badge
          :model-value="total"
          :hidden="item.id !== 0"
          number-type="overflow"
          type="danger"
          :max="99"
          :top="3"
          :right="-18"
        />
        <wd-img
          v-if="selectedId !== item.id"
          width="30.47rpx"
          height="30.47rpx"
          :src="item.iconPath"
        />
        <wd-img
          v-if="selectedId === item.id"
          width="30.47rpx"
          height="30.47rpx"
          :src="item.selectedIconPath"
        />
        <text class="item-text">
          {{ item.text }}
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { useTabbarStore } from '@/stores/tabbar.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { getPermissions } from '@/utils/usePermission.js';
import { onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import { nextTick, ref, watch } from 'vue';

defineProps({
  total: {
    type: Number,
    default: 0,
  },
});
const tabBarStore = useTabbarStore();
const { selectedId, tabBars, showFlag } = storeToRefs(tabBarStore);
const { setSelectedId, updateTabBarShow } = tabBarStore;

const tabBarList = ref([]);

const switchTab = (id) => {
  setSelectedId(id);
};

watch(() => tabBarStore.tabBars, () => {
  tabBarList.value = tabBarStore.tabBars.filter(item => item.show);
  if (tabBarList.value?.findIndex(item => item.id === selectedId.value) === -1) {
    setSelectedId(tabBarList.value[0]?.id);
  }
}, {
  deep: true,
});

onShow(async () => {
  await nextTick();
  await getPermissions();
  const data = await getStorageSync('tabBars');
  updateTabBarShow(Array.isArray(data) ? data : []);
  if (showFlag.value) {
    tabBarList.value = tabBars.value.filter(item => item.show);
    setSelectedId(tabBarList.value[0]?.id);
  }
  showFlag.value = false;
});
</script>

<style lang="scss" scoped>
@import '@/static/wot/common.scss';
.bmos-tabBar {
  width: 70.34rpx;
  height: 100%;
  background: var(--bmos-color-white);
  box-shadow: 2px 0px 10px 0px rgba(0, 0, 0, 0.1);

  .navigator {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    height: 100%;

    .navigator-item {
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      width: 70.34rpx;
      height: 72.66rpx;
      box-sizing: border-box;
      border-left: 3.52rpx solid #ffffff;
      border-right: 3.52rpx solid #ffffff;
      position: relative;
    }

    .navigator-item-active {
      background: var(--bmos-color-tag);
      border-left: 3.52rpx solid var(--bmos-color-primary);
      border-right: 3.52rpx solid var(--bmos-color-tag);
      .item-text {
        color: var(--bmos-color-primary);
      }
    }

    .navigator-second-item {
      margin: 55.69rpx 0;
    }
  }
}

.item-text {
  width: 100%;
  color: var(--bmos-color-text-sub);
  font-size: var(--bmos-font-size-desc);
  line-height: 11.72rpx;
  text-align: center;
  margin-top: 7.03rpx;
  padding: 0 5.27rpx;
  box-sizing: border-box;
  @extend .bmos-ellipsis;
}

.icon {
  width: 30.48rpx;
  height: 30.48rpx;
}
</style>
