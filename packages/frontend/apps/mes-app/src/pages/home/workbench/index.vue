<template>
  <view class="workbench-container">
    <template v-if="workbenchConfig.length">
      <ItemContainer
        v-for="(item,index) in workbenchConfig"
        :key="index"
        :item-container="item"
        :class="{'no-first-item': index !== 0}"
      />
    </template>
    <BmosNoData v-else :text="t('暂无工作台权限')" type="emptyWorkbench" />
  </view>
</template>

<script setup>
  import BmosNoData from '@/components/BmosNoData/index.vue';
  import ItemContainer from './components/ItemContainer.vue';
  import { getMenusApi } from '@/api/systemApi.js';
  import { ref } from 'vue';
  import { t } from '@/utils/useBmosI18n.js';
  const workbenchConfig = ref([]);
  const getMenusfn = async() => {
    const res = await getMenusApi({
      terminalType: 1
    });
    if (res.data && res.data.length > 0) {
      workbenchConfig.value = res.data[0].children;
    } else {
      workbenchConfig.value = [];
    }
  };
  getMenusfn();
</script>

<style scoped lang="scss">
	.workbench-container {
		padding: 11.72rpx 11.14rpx 11.72rpx 7.62rpx;
		height: 100%;
		width: 100%;
		overflow-y: auto;
		box-sizing: border-box;
		position: relative;

		.no-data {
			padding: 153.58rpx 0 0;
			width: 100%;
			height: 100%;
			box-sizing: border-box;
			display: flex;
			flex-direction: column;
			align-items: center;
			font-size: 11.72rpx;
			font-weight: 400;
			color: #545659;
		}

		.no-first-item {
			margin-top: 9rpx;
		}
	}
</style>
