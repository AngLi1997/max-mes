<template>
  <view class="node_content">
    <view class="node_index">
      {{
        index + 1 < 10 ? `0${index + 1}` : index + 1
      }}
    </view>
    <view class="node_title_box">
      <view class="node_title">{{ node.name }}</view>
      <view class="node_msg_box">
        <view class="team_box">
          {{ t('班次') }}{{ node.processChangeNumber + 1 }} - {{ node.procedureChangeNumber + 1 }}
        </view>
        <view class="node_line" />
        <view v-if="node.stateEnum.value === 4" class="node_time">
          {{ node.endTime }}
        </view>
      </view>
    </view>
    <view class="right_box">
      <view
        class="node_status node_status_norun"
        :class="{
          node_status_running: node.stateEnum.value === 1,
          node_status_active: node.stateEnum.value === 2,
          node_status_ending: node.stateEnum.value === 3,
          node_status_success: node.stateEnum.value === 4,
        }"
      >
        {{ node.stateEnum.name }}
      </view>
      <wd-icon
        class-prefix="bmos-app-icon"
        name="jiantou-you"
        size="11.72rpx"
        color="#B6B9BF"
      />
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  const props = defineProps({
    node: {
      type: Object,
      default: () => {
        return {};
      }
    },
    index: {
      type: Number,
      default: 0
    },
    completed: {
      type: Array,
      default: () => []
    },
    completedIndex: {
      type: Number,
      default: -1
    },
    end: {
      type: Boolean,
      default: true
    }
  });
  const label = [
    t('未激活'),
    t('进行中'),
    t('已激活'),
    t('已结束'),
    t('已完成'),
    t('已完成')
  ];
</script>

<style lang="scss" scoped>
  .node_content {
    background-color: #fff;
    font-size: 9.38rpx;
    border-radius: 4.69rpx;
    padding: 9.38rpx 5rpx 9.38rpx 9.38rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.05);
    .node_index {
      color: #9da0a6;
      padding-right: 9.38rpx;
      border-right: 0.59rpx solid #e1e3e5;
    }
    .node_title_box {
      width: calc(100% - 87.3rpx);
      margin-left: 7rpx;
      .node_title {
        font-size: 12.06rpx;
        color: #242526;
        margin-bottom: 7.38rpx;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
      }
      .node_msg_box{
        display: flex;
        align-items: center;
        color: #6C6E73;
        .team_box{
          border-right: 1px solid #E1E3E5;
          padding-right: 7.03rpx;
          margin-right: 7.03rpx;
        }
      }
    }
    .right_box {
        display: flex;
        align-items: center;
        .node_status {
          padding: 2.93rpx 4rpx;
          border-radius: 2.34rpx;
					margin-right: 5.86rpx;
        }
				.node_status_norun{
					color: #9DA0A6;
					background-color: #F0F1F2;
				}
				.node_status_success{
          background-color: #dcf2eb;
					color: #59BF78;
				}
				.node_status_running{
          background-color: #EBF2FF;
					color: #2871FF;
				}
        .node_status_ending{
          background: #FFECD9;
					color: #FF9933;
        }
        .node_status_active{
          background: #EFEEFD;
					color: #574EFA;
        }
				.node_time{
					color: #6C6E73;
				}
      }
  }
</style>
