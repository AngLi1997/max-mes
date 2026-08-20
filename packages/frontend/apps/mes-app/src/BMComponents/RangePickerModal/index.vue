<template>
  <BMModal
    v-model="modalOpen"
    :title="t('请选择区间')"
    size="medium"
    @confirm="confirm"
    @cancel="cancel"
  >
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
                rangeValue[option.index]
                  ? format(rangeValue[option.index], "yyyy-MM-dd HH:mm")
                  : ""
              }}
            </view>
          </view>
        </template>
      </wd-segmented>
    </view>
    <view class="content">
      <wd-datetime-picker-view
        v-if="segmentValue === 'start'"
        v-model="rangeValue[0]"
        :max-date="maxDate"
        :min-date="minDate"
        :columns-height="320"
        :formatter="formatter"
        custom-label-class="label"
        custom-value-class="value"
      />
      <wd-datetime-picker-view
        v-if="segmentValue === 'end'"
        v-model="rangeValue[1]"
        :max-date="maxDate"
        :min-date="minDate"
        :columns-height="320"
        :formatter="formatter"
        custom-label-class="label"
        custom-value-class="value"
      />
    </view>
  </BMModal>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { BMModal } from '@/BMComponents';
  import { useRangePicker } from './hooks/useRangePicker.js';
  import { computed, onMounted, watch } from 'vue';
  import { format } from 'date-fns';
  import { serverTime } from '@/utils/time.js';

  const props = defineProps({
    formatDate: {
      type: String,
      default: 'yyyy-MM-dd HH:mm'
    },
    open: {
      type: Boolean,
      default: false
    },
    modelValue: {
      type: Array,
      default: () => [null, null]
    },
    maxDate: {
      type: [String, Date, Number],
      default: Date.now() + 365 * 24 * 60 * 60 * 10000
    },
    minDate: {
      type: [String, Date, Number],
      default: Date.now() - 365 * 24 * 60 * 60 * 10000
    }
  });

  const emit = defineEmits([
    'update:modelValue',
    'update:open',
    'confirm',
    'cancel'
  ]);

  const modalOpen = computed({
    get: () => props.open,
    set: (val) => emit('update:open', val)
  });

  const { rangeValue, segmentOptions, segmentValue, formatter, confirm, cancel } =
    useRangePicker({ props, emit });

  onMounted(()=>{
    if(!!props.open && !!props.modelValue){
      rangeValue.value[0] = props.modelValue[0] || new Date(serverTime.value);
      rangeValue.value[1] = props.modelValue[1] || new Date(serverTime.value);
    }
  })

  watch(
    () => props.open,
    (val) => {
      if (val) {
        rangeValue.value[0] = props.modelValue[0] || new Date(serverTime.value);
        rangeValue.value[1] = props.modelValue[1] || new Date(serverTime.value);
      }
    }
  );
</script>

<style lang="scss" scoped>
.segment-box {
  width: 100%;
  height: 39.26rpx;
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
      font-weight: 500;
    }
    .name {
      margin-bottom: 2.34rpx;
    }
  }
}
.content {
  width: 100%;
  height: 187.5rpx;
  box-sizing: border-box;
  :deep(.wd-picker-view-column__item) {
    height: 37.5rpx;
    line-height: 37.5rpx !important;
  }
  :deep(.wd-picker-view-column__item--active) {
    color: #2871ff;
    font-weight: 600;
  }
  :deep(.wd-picker-view__roller) {
    background-color: #ebf1ff;
    height: 37.5rpx !important;
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
</style>
