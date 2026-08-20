<template>
  <view class="item-container">
    <view class="title-box">
      <view class="title-box-left">
        {{ item.productMergeCode }}-{{ item.productName }}
      </view>
      <view
        v-if="!history"
        class="title-box-right"
      >
        {{ item.batchNo }}
      </view>
      <view
        v-else
        class="todo_item_title_code"
      >
        <view style="font-size: 11.72rpx;">{{ item.batchNo }}</view>
      </view>
    </view>
    <view class="process-steps-container">
      <view
        v-if="!history"
        class="process-steps-box"
      >
        <uv-icon
          class="icon"
          :name="icon"
          custom-prefix="bmos-icon"
          size="14.07rpx"
          color="#1A8CFF"
        />
        <text
          v-if="item.activeProcedureName"
          class="process-steps-all"
        >
          {{ item.activeProcedureName }}
        </text>
        <text v-else>
          {{ item.procedureName }}
          <text v-if="item.procedureDuration !== '0'">
            {{ item.procedureDuration }}{{ timeTypes[item.procedureTimeUnit] }}
          </text>
          |{{ item.procedureStepName }}
          <text v-if="item.procedureStepDuration !== '0'">
            {{ item.procedureStepDuration
            }}{{ timeTypes[item.procedureStepTimeUnit] }}
          </text>
        </text>
      </view>
      <view
        v-else
        class="process-steps-none-box"
      />
    </view>
    <view
      class="text-box"
      :style="{ height: showMore ? '99.72rpx' : '39.86rpx' }"
    >
      <text>{{ t("工艺") }}:&nbsp;&nbsp;{{ item.processName }}</text>
      <text>{{ t("版本") }}:&nbsp;&nbsp;{{ item.processVersion }}</text>
      <text>{{ t("规格") }}:&nbsp;&nbsp;{{ item.productSpecification }}</text>
      <text>{{ t("开始时间") }}:&nbsp;&nbsp;{{ item.startTime }}</text>
      <text>{{ t("产线") }}:&nbsp;&nbsp;{{ item.lineName }}</text>
      <view
        class="click-arrow"
        @click.stop="arrowClick"
      >
        <uv-icon
          class="icon-arrow"
          :class="{ 'icon-rotate': showMore }"
          name="jiantou-xia"
          custom-prefix="bmos-icon"
          size="14.07rpx"
          color="#B0B5BF"
        />
      </view>
    </view>
    <view
      v-if="item.executePaused"
      class="node_suspend"
    >
      {{ t("已暂停") }}
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { ref, toRefs } from 'vue';
  const props = defineProps({
    item: {
      type: Object,
      default: () => {
        return {
          productMergeCode: '',
          productName: '',
          batchNo: '',
          name: '',
          activeProcedureName: '',
          processName: '',
          processVersion: '',
          productSpecification: '',
          startTime: ''
        };
      }
    },
    icon: {
      type: String,
      default: ''
    },
    history: {
      type: Boolean,
      default: false
    }
  });

  const { item } = toRefs(props);
  const showMore = ref(false);

  const timeTypes = {
    day: t('日'),
    hour: t('时'),
    minute: t('分')
  };

  function arrowClick() {
    showMore.value = !showMore.value;
  }
</script>

<style scoped lang="scss">
  .item-container {
    max-width: 361.08rpx;
    min-height: 111.96rpx;
    padding: 9.38rpx 11.72rpx;
    border-radius: 7.03rpx;
    background-color: #ffffff;
    box-sizing: border-box;
    margin-bottom: 9.38rpx;
    position: relative;
    .title-box {
      &-left {
        font-size: 14.07rpx;
        font-weight: 500;
        color: #242526;
        margin-bottom: 2.93rpx;
      }

      &-right {
        font-size: 12.9rpx;
        font-weight: 500;
        color: #2871ff;
      }
      .todo_item_title_code {
        width: max-content;
        background: linear-gradient(90deg, #599eff -2.49%, #3274f9 101.99%);
        color: white;
        box-sizing: border-box;
        padding: 0 4.69rpx;
        border-radius: 2.34rpx;
        word-break: break-all;
        display: flex;
        align-items: center;
      }
    }

    .process-steps-container {
      display: flex;
      align-items: center;
      gap: 4.69rpx;

      .pause-tag {
        height: 18.76rpx;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 58.62rpx;
      }

      .process-steps-box {
        width: fit-content;
        max-width: 100%;
        margin: 7.03rpx 0;
        height: 18.76rpx;
        align-items: center;
        display: flex;
        border-radius: 58.62rpx;
        background-color: #e5f0ff;
        font-size: 11.72rpx;
        font-weight: 400;
        color: #1a8dff;
        box-sizing: border-box;
        padding: 2.34rpx 4.69rpx 2.34rpx 2.34rpx;
        .process-steps-all {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .icon {
          margin-right: 4.69rpx;
        }
      }
      .process-steps-none-box {
        height: 10.76rpx;
      }
    }

    .text-box {
      display: flex;
      flex-direction: column;
      color: #545659;
      font-size: 11.72rpx;
      font-weight: 400;
      line-height: 19.93rpx;
      position: relative;
      overflow: hidden;
      transition: 0.3s all;

      .click-arrow {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 60rpx;
        height: 45rpx;
        display: flex;
        align-items: flex-end;
        justify-content: flex-end;

        .icon-arrow {
          width: 14.07rpx;
          height: 14.07rpx;
          transition: 0.3s all;
        }

        .icon-rotate {
          transform: rotate(180deg);
        }
      }
    }

    .node_suspend {
      position: absolute;
      top: 0;
      right: 0;
      padding: 2.34rpx 5.86rpx;
      background-color: #ffd5cc;
      color: #ff4c26;
      border-bottom-left-radius: 2.34rpx;
    }
  }
</style>
