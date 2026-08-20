<template>
  <BMModal
    v-model="open"
    closable
    position="right"
    hidden-button
    :title="t('操作历史')"
  >
    <view class="content_box">
      <view
        v-for="(item, index) in dataList"
        :key="index"
        class="history_item"
      >
        <view class="history_title">
          <view class="history_title_left">
            <view class="history_title_left_block" />
            <view class="history_title_left_title">
              {{
                item.operationTypeName
              }}
            </view>
          </view>
          <view class="history_title_right">
            <wd-icon
              class-prefix="bmos-app-icon"
              name="caozuoren"
              size="11.72rpx"
              color="#B6B9BF"
              @click="openScreen"
            />
            <view class="history_title_left_name">
              {{
                item.createUsername
              }}
            </view>
          </view>
        </view>
        <view class="history_title_time">
          {{ item.createTime }}
        </view>
        <view class="detail_box">
          <view
            v-for="(msg, feild) in item.detail"
            :key="feild"
            class="detail_item"
          >
            <view class="detail_label">{{ fieldName[feild] }}:</view>
            <view class="detail_value">{{ msg }}</view>
          </view>
        </view>
      </view>
    </view>
  </BMModal>
</template>
<script lang="ts" setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { BMModal } from '@/BMComponents';
  import { ref, computed } from 'vue';
  // import { reqProcessListAll } from '@/api';

  const props = defineProps({
    open: {
      type: Boolean,
      default: false
    },
    dataList: {
      type: Object,
      default: () => {}
    },
    fieldName: {
      type: Object,
      default: () => {}
    }
  });
  const open = computed({
    get: () => props.open,
    set: (val) => {
      emit('update:open', val);
    }
  });
  const emit = defineEmits(['update:open']);
  const showDataList = ref([]);
</script>
<style lang="scss" scoped>
.content_box{
  padding-top: 9.38rpx;
  min-width: 117.19rpx;
}
  .history_item {
    width: 204.38rpx;
    padding: 9.38rpx 0;
    background-color: #f5f6f7;
    margin-bottom: 9.38rpx;
    border-radius: 5.86rpx;
    font-size: 12.89rpx;
    .history_title {
      font-size: 12.89rpx;
      height: 11.72rpx;;
      margin: 5.86rpx 0;
      box-sizing: border-box;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 9.38rpx;
      .history_title_left {
        .history_title_left_block {
          position: absolute;
          left: 0;
          top: 0;
          width: 1.76rpx;
          height: 11.72rpx;
          background-color: #2871ff;
        }
        .history_title_left_title{
          font-size: 11.72rpx;
        }
        .history_title_left_title{
          font-size: 11.72rpx;
        }
      }
      .history_title_right {
        display: flex;
        .history_title_left_name {
          margin-left: 6.72rpx;
        }
      }
    }
    .history_title_time {
      color: #6c6e73;
      font-size: 9.38rpx;
      padding: 0 9.38rpx 9.38rpx;
      border-bottom: 0.59rpx solid #e1e3e5;
    }
    .detail_box {
      padding: 9.38rpx;
      .detail_item {
        display: flex;
        margin-top: 7.03rpx;
        font-size: 10.55rpx;
        .detail_label {
          color: #6c6e73;
          margin-right: 5.86rpx;
          font-size: 10.55rpx;
          white-space: nowrap;
        }
        .detail_value{
          word-break: break-all;
        }
      }
    }
  }
</style>
