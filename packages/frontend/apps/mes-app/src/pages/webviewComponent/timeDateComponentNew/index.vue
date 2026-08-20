<template>
  <view v-if="showComponent" class="dateMask">
    <view
      v-if="componentRef"
      class="container"
      :style="{ height: isDate ? '287.11rpx' : '331.64rpx' }"
    >
      <view class="title-box">
        <view class="title-left" />
        <view class="title-center">{{ title }}</view>
        <view class="title-right" @click="closeComponent">
          <wd-icon name="close" color="#434C59" size="14.06rpx" />
        </view>
      </view>
      <!-- 时间组件选择区域 -->
      <template v-if="!isDate">
        <view class="segment-box">
          <wd-segmented
            v-model:value="segmentValue"
            :options="segmentOptions"
            custom-class="segment-class"
          >
            <template #label="{ option }">
              <view class="section-slot">
                <view class="name">
                  {{ option.label }}
                </view>
                <view class="value">
                  {{
                    option.dateTime
                      ? `${format(option.dateTime, "yyyy-MM-dd HH:mm")}:${
                        option.secondValue < 10
                          ? "0" + option.secondValue
                          : option.secondValue
                      }`
                      : ""
                  }}
                </view>
              </view>
            </template>
          </wd-segmented>
        </view>
        <view class="content">
          <view style="width: 100%;">
            <wd-datetime-picker-view
              v-if="segmentValue === 'start'"
              v-model="segmentOptions[0].dateTime"
              :max-date="timeMax"
              :columns-height="320"
              :formatter="formatter"
              custom-label-class="label"
              custom-value-class="value"
            />
            <wd-datetime-picker-view
              v-if="segmentValue === 'end'"
              v-model="segmentOptions[1].dateTime"
              :max-date="timeMax"
              :columns-height="320"
              :formatter="formatter"
              custom-label-class="label"
              custom-value-class="value"
            />
          </view>
          <view style="width: 20%;">
            <wd-picker-view
              v-if="segmentValue === 'start'"
              v-model="segmentOptions[0].secondValue"
              custom-class="second-picker"
              :columns="startTimeSecondColumns"
              :columns-height="320"
            />
            <wd-picker-view
              v-if="segmentValue === 'end'"
              v-model="segmentOptions[1].secondValue"
              custom-class="second-picker"
              :columns="endTimeSecondColumns"
              :columns-height="320"
            />
          </view>
        </view>
      </template>
      <!-- 日期组件选择区域 -->
      <view v-else class="content" :class="dateClass">
        <view style="width: 100%;">
          <wd-datetime-picker-view
            ref="datePicker"
            v-model="dateValue"
            :max-date="maxDate"
            :min-date="minDate"
            :columns-height="320"
            :formatter="formatter"
            custom-label-class="label"
            custom-value-class="value"
          />
        </view>
        <view v-if="dateClass === 'datetime-second'" style="width: 20%;">
          <wd-picker-view
            v-model="secondValue"
            custom-class="second-picker"
            :columns="dateSecondColumns"
            :columns-height="320"
          />
        </view>
      </view>
      <wd-row class="button-box" :gutter="16">
        <wd-col v-if="!isRevise" :span="6">
          <wd-button block type="info" @click="reset">
            {{ t("重置") }}
          </wd-button>
        </wd-col>
        <wd-col :span="isRevise ? 12 : 6">
          <wd-button block type="info" @click="enterNull">
            {{ t("录入空值") }}
          </wd-button>
        </wd-col>
        <wd-col :span="12">
          <wd-button block @click="confirm">{{ t("确定") }}</wd-button>
        </wd-col>
      </wd-row>
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
  import { useTimeDate } from './hooks/useTimeDate.js';
  import { format } from 'date-fns';
  import { watch } from 'vue';

  const props = defineProps({
    // 是否为修订
    isRevise: {
      type: Boolean,
      default: false
    },
    component: {
      type: Object,
      default: () => ({})
    }
  });
  const emit = defineEmits(['close', 'confirm', 'enterNull']);
  const {
    componentRef,
    datePicker,
    dateClass,
    minDate,
    maxDate,
    title,
    formatter,
    segmentOptions,
    segmentValue,
    isDate,
    dateValue,
    secondValue,
    dateSecondColumns,
    startTimeSecondColumns,
    endTimeSecondColumns,
    timeMax,
    showComponent,
    closeComponent,
    reset,
    enterNull,
    confirm,
    setSegmentOptionsDateTime
  } = useTimeDate({
    props,
    emit
  });

  watch(
    () => props.component,
    (val) => {
      if (val && val.fieldId) {
        componentRef.value = val;
        setSegmentOptionsDateTime();
      }
    },
    {
      immediate: true
    }
  );

  useSubNvueLinster('page-timeDateComponent', (component) => {
    componentRef.value = component;
    setSegmentOptionsDateTime();
  });
</script>

<style>
page {
  background: transparent;
}
</style>
<style lang="scss" scoped>
.dateMask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 9999;
}
.container {
  background: #fff;
  width: 375rpx;
  position: relative;
  border-radius: 7.03rpx;

  .title-box {
    width: 100%;
    height: 41.02rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    .title-left {
      width: 14.06rpx;
      height: 14.06rpx;
    }
    .title-center {
      font-size: 15.23rpx;
      font-weight: 400;
      color: #242526;
    }
  }

  .segment-box {
    width: 100%;
    height: 39.26rpx;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    .segment-class {
      background-color: #f2f3f5;
      border-radius: 4.69rpx;
      color: #6c6e73;
      :deep(.is-active) {
        .section-slot {
          color: #2871ff;
        }
      }
    }
    .section-slot {
      height: 35.16rpx;
      padding: 2.34rpx 0;
      box-sizing: border-box;
      border-radius: 3.52rpx;
      font-weight: 500;
      color: #6c6e73;
      font-size: 11.72rpx;
      .name,
      .value {
        font-size: 11.72rpx;
        line-height: 14.06rpx;
        font-weight: 400;
      }
      .name {
        margin-bottom: 2.34rpx;
      }
    }
  }

  .content {
    width: 100%;
    height: 187.5rpx;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    display: flex;
    .second-picker {
      height: 100%;
      box-sizing: border-box;
    }
    :deep(.wd-picker-view-column__item) {
      height: 64px;
      line-height: 64px !important;
      font-size: 11.72rpx;
    }
    :deep(.wd-picker-view-column__item--active) {
      color: #2871ff;
      font-weight: 600;
    }
    :deep(.wd-picker-view__roller) {
      background-color: #ebf1ff;
      height: 64px !important;
    }
  }
  .date-hour {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .date {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .year-month {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(3),
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .year {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(2),
      > :nth-child(3),
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .month-day-time {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1) {
        display: none;
      }
    }
  }
  .month-day-hour {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .month-day {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .month {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(3),
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .day-time {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2) {
        display: none;
      }
    }
  }
  .day-hour {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .day {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2),
      > :nth-child(4),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .time {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2),
      > :nth-child(3) {
        display: none;
      }
    }
  }
  .hour {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2),
      > :nth-child(3),
      > :nth-child(5) {
        display: none;
      }
    }
  }
  .minute {
    :deep(.uni-picker-view-wrapper) {
      > :nth-child(1),
      > :nth-child(2),
      > :nth-child(3),
      > :nth-child(4) {
        display: none;
      }
    }
  }

  .button-box {
    width: 100%;
    height: 58.59rpx;
    padding: 8.2rpx 4.69rpx 8.2rpx 9.38rpx;
    box-sizing: border-box;
    position: absolute;
    bottom: 0;
    left: 0;
    margin: 0 !important;
  }
}
</style>
