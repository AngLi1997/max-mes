import { t } from '@/utils/useBmosI18n.js';
import { getTime, setSeconds } from 'date-fns';
import { computed, ref } from 'vue';

export const useDatePicker = ({ props, emit }) => {
  // 日期组件的值
  const dateValue = ref(null);
  const secondValue = ref(0);
  const secondColumns = computed(() => {
    return Array.from({ length: 60 }).map((_, index) => {
      return {
        label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
        value: index,
      };
    });
  });

  // 日期组件的class
  const dateClass = computed(() => {
    return {
      'yyyy-MM-dd HH:mm:ss': 'datetime-second',
      'yyyy-MM-dd HH:mm': 'datetime',
      'yyyy-MM-dd HH': 'date-hour',
      'yyyy-MM-dd': 'date',
      'yyyy-MM': 'year-month',
      'yyyy': 'year',
      'MM-dd HH:mm': 'month-day-time',
      'MM-dd HH': 'month-day-hour',
      'MM-dd': 'month-day',
      'MM': 'month',
      'dd HH:mm': 'day-time',
      'dd HH': 'day-hour',
      'dd': 'day',
      'HH:mm': 'time',
      'HH': 'hour',
      'mm': 'minute',
    }[props.formatDate];
  });

  // 时间格式化
  const formatter = (type, value) => {
    switch (type) {
      case 'year':
        return value + t('年');
      case 'month':
        return value + t('月');
      case 'date':
        return value + t('日');
      case 'hour':
        return value + t('时');
      case 'minute':
        return value + t('分');
      default:
        return value;
    }
  };

  const confirm = () => {
    try {
      emit('confirm', getTime(setSeconds(dateValue.value, secondValue.value)));
    }
    catch (error) {
      emit('confirm', dateValue.value);
    }
  };

  const cancel = () => {
    emit('cancel');
  };

  return {
    dateClass,
    dateValue,
    secondValue,
    secondColumns,
    formatter,
    confirm,
    cancel,
  };
};
