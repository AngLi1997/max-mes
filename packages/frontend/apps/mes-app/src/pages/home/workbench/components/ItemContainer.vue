<template>
  <view class="item-container">
    <ItemContainerTitle :name="itemContainer.name" />
    <view class="content">
      <Item
        v-for="(item,index) in itemContainer.children"
        :key="item.id"
        :item="item"
        :class="{'no-first-item':index !== 0}"
        @click="to(item)"
      />
    </view>
  </view>
</template>

<script setup>
  import ItemContainerTitle from './ItemContainerTitle.vue';
  import Item from './Item.vue';
  import { toRefs } from 'vue';
  import { codeEnum } from '../../menuCode.js';
  import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
  const props = defineProps({
    itemContainer: {
      type: Object,
      default: (rawProps) => {
        return {};
      }
    }
  });
  const { itemContainer } = toRefs(props);

  function to(item) {
    let url = codeEnum[`${item.id}`].path;
    const query = codeEnum[`${item.id}`].query;
    if (query) {
      url = `${url}${queryParams(query)}`;
    }
    uni.navigateTo({
      url
    });
  }
</script>

<style lang="scss" scoped>
	.item-container {
		border-radius: 4.69rpx;
		height: 142rpx;
		width: 100%;
		box-sizing: border-box;
		background: linear-gradient(154.51deg, #D9E5FF 0.01%, rgba(255, 255, 255, 0) 37.42%),
			linear-gradient(0deg, #FFFFFF, #FFFFFF);

		.content {
			max-width: 100%;
			overflow-x: auto;
			display: flex;
			box-sizing: border-box;
			padding: 0 24.03rpx;

			.no-first-item {
				margin-left: 60rpx;
			}
		}
	}
</style>
