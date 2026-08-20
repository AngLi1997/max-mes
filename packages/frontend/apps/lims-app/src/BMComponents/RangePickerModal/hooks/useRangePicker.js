import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useRangePicker = ({ props, emit }) => {
  const rangeValue = ref([Date.now(), Date.now()]);
  // 分段器value
  const segmentValue = ref('start');
  // 分段器options
  const segmentOptions = reactive([
    { label: t('开始时间'), value: 'start', index: 0 },
    { label: t('结束时间'), value: 'end', index: 1 },
  ]);

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

  // 确认
  const confirm = async () => {
    emit('confirm', rangeValue.value);
  };

  // 取消
  const cancel = () => {
    emit('cancel');
  };

  return {
    rangeValue,
    segmentOptions,
    segmentValue,
    formatter,
    confirm,
    cancel,
  };
};
