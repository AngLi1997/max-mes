<template>
  <BMModal v-model="modalOpen" :title="title" size="medium" @confirm="confirm" @cancel="cancel">
    <view class="content">
      <view style="width: 100%;">
        <wd-datetime-picker-view
          v-if="modalOpen"
          v-model="dateValue"
          :max-date="maxDate"
          :min-date="minDate"
          :columns-height="320"
          :formatter="formatter"
          custom-label-class="label"
          custom-value-class="value"
          :class="dateClass"
        />
      </view>
      <view v-if="dateClass === 'datetime-second'" style="width: 20%;">
        <wd-picker-view
          v-model="secondValue"
          custom-class="second-picker"
          :columns="secondColumns"
          :columns-height="320"
        />
      </view>
    </view>
    <template v-if="slots.buttons" #buttons>
      <slot name="buttons" />
    </template>
  </BMModal>
</template>

<script setup>
import { BMModal } from '@/BMComponents';
import { serverTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { getSeconds } from 'date-fns';
import { computed, useSlots, watch } from 'vue';
import { useDatePicker } from './hooks/useDatePicker.js';

const props = defineProps({
  formatDate: {
    type: String,
    default: 'yyyy-MM-dd HH:mm',
  },
  open: {
    type: Boolean,
    default: false,
  },
  modelValue: {
    type: [String, Date, Number],
    default: '',
  },
  maxDate: {
    type: [String, Date, Number],
    default: Date.now() + 365 * 24 * 60 * 60 * 10000,
  },
  minDate: {
    type: [String, Date, Number],
    default: Date.now() - 365 * 24 * 60 * 60 * 10000,
  },
  title: {
    type: String,
    default: () => t('请选择时间'),
  },
});

const emit = defineEmits(['update:modelValue', 'update:open', 'confirm', 'cancel']);

const slots = useSlots();

const modalOpen = computed({
  get: () => props.open,
  set: val => emit('update:open', val),
});

const {
  dateValue,
  dateClass,
  formatter,
  secondValue,
  secondColumns,
  confirm,
  cancel,
} = useDatePicker({ props, emit });

watch(() => modalOpen.value, (val) => {
  if (val) {
    dateValue.value = props.modelValue || new Date(serverTime.value);
    secondValue.value = getSeconds(dateValue.value);
  }
});

const restoreDefault = () => {
  dateValue.value = props.modelValue || new Date(serverTime.value);
  secondValue.value = getSeconds(dateValue.value);
};
defineExpose({
  restoreDefault,
  confirm,
});
</script>

<style lang="scss" scoped>
  .content {
  width: 100%;
  height: 187.5rpx;
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
</style>
