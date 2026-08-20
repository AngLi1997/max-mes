<template>
  <view class="todo_item_box">
    <view class="todo_item_title" @click="changeClick()">
      <view class="todo_item_title_box">
        <view class="todo_item_title_name">
          {{ data.productMergeCode }}-{{ data.productName }}
        </view>
        <view class="todo_item_title_code">
          {{ data.batchNo }}
        </view>
        <view class="todo_item_title_msg">
          {{ t("工艺") }}：{{ data.processName }}-{{ data.processVersion }}
        </view>
        <view class="todo_item_title_msg">
          {{ t("产线") }}：{{ data.lineName }}
        </view>
      </view>
      <view :class="{todo_item_title_icon:true, is_up: isShow}">
        <wd-icon
          name="arrow-down"
          size="14.06rpx"
          color="#434C59"
        />
      </view>
    </view>
    <view v-if="isShow" class="node_box">
      <nodeItem v-for="item in data.todoPageVOList" :key="item.procedureStepId" :node-data="item" @click="nodeItemClick(item)" />
    </view>
  </view>
</template>
<script setup lang="ts">
  import { t } from '@/utils/useBmosI18n.js';
  import nodeItem from './nodeItem.vue';
  import {
    clickItem
  } from '../hooks/useDatas';
  import { computed } from 'vue';
  import pihao from '@/static/images/pihao.png';
	
  const props = defineProps({
    data: {
      type: Object,
      default: () => {}
    }
  });
  const emit = defineEmits(['nodeClick']);
  // const nodeItemList = ref();

  const isShow = computed(() => {
    return clickItem.value.indexOf(props.data.id) >= 0;
  });
	
  const changeClick = async() => {
    if (clickItem.value.indexOf(props.data.id) >= 0) {
      clickItem.value.splice(clickItem.value.indexOf(props.data.id), 1);
    } else {
      clickItem.value.push(props.data.id);
    }
    // getList();
  };
  // const getList = async() => {
  //   if (isShow.value) {
  //     try {
  //       const { data } = await getStepTodoPageApi({ planId: props.data.id });
  //       nodeItemList.value = data;
  //     } catch (error) {
  //       error.message && uni.showToast({
  //         title: error.message,
  //         icon: 'error',
  //         duration: 2000,
  //         mask: true
  //       });
  //     }
  //   }
  // };
  // onShow(() => {
  //   getList();
  // });

  // onMounted(() => {
  //   getList();
  // });

  const nodeItemClick = (data) => {
    emit('nodeClick', data);
  };
</script>
<style lang="scss" scoped>
.todo_item_box {
	padding: 9.38rpx;
	background-color: #fff;
	border-radius: 7.03rpx;
	margin-top: 11.72rpx;
  font-size: 14.06rpx;
	.todo_item_title {
		display: flex;
		align-items: center;
		.todo_item_title_box {
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			width: calc(100% - 21.09rpx);
			.todo_item_title_name {
				font-weight: 600;
        font-size: 14.06rpx;
				max-width: calc(100% - 171.09rpx);
			}
			.todo_item_title_code {
				max-width: 152.34rpx;
        height: 17.58rpx;
				width: fit-content;
				background: linear-gradient(90deg, #599eff -2.49%, #3274f9 101.99%);
				color: white;
				box-sizing: border-box;
				padding: 0 4.69rpx;
				border-radius: 2.34rpx;
				margin-left: 18.75rpx;
				word-break: break-all;
				display: flex;
				align-items: center;
        font-size: 11.72rpx;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
			}
			.todo_item_title_msg {
				width: 100%;
				margin-top: 9.38rpx;
        font-size: 11.72rpx;
				color: #6c6e73;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
			}
		}
		.todo_item_title_icon {
			transform-origin: center center; /* 设置旋转中心为div的中心 */
			transition: transform 0.5s ; /* 平滑过渡效果 */
		}
		.is_up {
			transform: rotate(180deg); 
		}
	}
	.node_box {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}
}
</style>
