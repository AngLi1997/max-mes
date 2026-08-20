import { reqCalculateDateApi } from '@/api/webViewApi.js';
import {
  setComponentNull,
  setComponentReset,
  setComponentValue,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { getTimestamp, serverTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, ref, watch } from 'vue';

// entryMethod 0:录入当前时间 1: 手动选择时间 未配置时默认为1

import { format, getSeconds, isSameMinute, setSeconds } from 'date-fns';

export const useTimeDate = ({ props, emit }) => {
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  const componentRef = ref({});
  const secondValue = ref('0');
  const showComponent = ref(true);

  // 默认日期格式
  const defaultFormat = ref('yyyy-MM-dd HH:mm:ss');
  // 日期组件实例
  const datePicker = ref(null);
  // 是否是日期组件
  const isDate = computed(() => {
    return componentRef.value?.componentType === 'DATE';
  });
  // 日期组件的值
  const dateValue = ref();
  // 日期组件秒数
  const dateSecondColumns = computed(() => {
    if (
      componentRef.value.configInfo
      && componentRef.value.configInfo.entryMethod === 0
    ) {
      return [
        {
          label:
            `${secondValue.value < 10
              ? `0${secondValue.value}`
              : `${secondValue.value}`}${t('秒')}`,
          value: secondValue.value,
        },
      ];
    }
    return Array.from({ length: 60 }).map((_, index) => {
      return {
        label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
        value: index,
      };
    });
  });
  // 日期组件格式化
  const dateFormat = computed(() => {
    return componentRef.value.configInfo?.format || defaultFormat.value;
  });
  // 日期组件的class
  const dateClass = computed(() => {
    // {
    //     'yyyy-MM-dd HH:mm:ss': 'datetime-second',
    //     'yyyy-MM-dd HH:mm': 'datetime',
    //     'yyyy-MM-dd HH': 'date-hour',
    //     'yyyy-MM-dd': 'date',
    //     'yyyy-MM': 'year-month',
    //     yyyy: 'year',
    //     'MM-dd HH:mm:ss': 'month-day-time-second',
    //     'MM-dd HH:mm': 'month-day-time',
    //     'MM-dd HH': 'month-day-hour',
    //     'MM-dd': 'month-day',
    //     'MM': 'month',
    //     'dd HH:mm:ss': 'day-time-second',
    //     'dd HH:mm': 'day-time',
    //     'dd HH': 'day-hour',
    //     dd: 'day',
    //     'HH:mm:ss': 'time-second',
    //     'HH:mm': 'time',
    //     HH: 'hour',
    //     'mm:ss': 'minute-second',
    //     mm: 'minute'
    //     'ss': 'second'
    //   }
    if (dateFormat.value.includes('s')) {
      return 'datetime-second';
    }
    else if (dateFormat.value.includes('m')) {
      return 'datetime';
    }
    else if (dateFormat.value.includes('H')) {
      return 'date-hour';
    }
    else if (dateFormat.value.includes('d')) {
      return 'date';
    }
    else if (dateFormat.value.includes('M')) {
      return 'year-month';
    }
    else if (dateFormat.value.includes('y')) {
      return 'year';
    }
    else {
      return 'datetime-second';
    }
  });

  // 时间组件日期最大值
  const timeMax = ref(new Date(serverTime.value));

  // 日期组件最小日期
  const minDate = computed(() => {
    return componentRef.value.configInfo
      && componentRef.value.configInfo.entryMethod === 0
      ? new Date(serverTime.value)
      : undefined;
  });

  // 日期组件最大日期
  const maxDate = computed(() => {
    return componentRef.value.configInfo
      && componentRef.value.configInfo.entryMethod === 0
      ? new Date(serverTime.value)
      : undefined;
  });

  // 分段器value
  const segmentValue = ref('start');
  // 分段器options
  const segmentOptions = reactive([
    {
      label: t('开始时间'),
      value: 'start',
      dateTime: new Date(serverTime.value),
      secondValue: 0,
    },
    {
      label: t('结束时间'),
      value: 'end',
      dateTime: new Date(serverTime.value),
      secondValue: 0,
    },
  ]);
  // 开始时间组件秒数
  const startTimeSecondColumns = computed(() => {
    if (isSameMinute(segmentOptions[0].dateTime, timeMax.value)) {
      if (segmentOptions[0].secondValue > getSeconds(timeMax.value)) {
        segmentOptions[0].secondValue = 0;
      }
      return Array.from({ length: getSeconds(timeMax.value) + 1 }).map(
        (_, index) => {
          return {
            label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
            value: index,
          };
        },
      );
    }
    return Array.from({ length: 60 }).map((_, index) => {
      return {
        label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
        value: index,
      };
    });
  });

  // 结束时间组件秒数
  const endTimeSecondColumns = computed(() => {
    if (isSameMinute(segmentOptions[1].dateTime, timeMax.value)) {
      if (segmentOptions[1].secondValue > getSeconds(timeMax.value)) {
        segmentOptions[1].secondValue = 0;
      }
      return Array.from({ length: getSeconds(timeMax.value) + 1 }).map(
        (_, index) => {
          return {
            label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
            value: index,
          };
        },
      );
    }
    return Array.from({ length: 60 }).map((_, index) => {
      return {
        label: `${index < 10 ? `0${index}` : `${index}`}${t('秒')}`,
        value: index,
      };
    });
  });
  // 设置分段器时间为当前时间
  const setSegmentOptionsDateTime = (time = new Date(serverTime.value)) => {
    segmentOptions.forEach((item) => {
      item.dateTime = time;
      item.secondValue = getSeconds(time);
    });
  };

  // 时间格式化
  const formatter = (type, value) => {
    switch (type) {
      case 'year':
        return `${value}${t('年')}`;
      case 'month':
        return `${value}${t('月')}`;
      case 'date':
        return `${value}${t('日')}`;
      case 'hour':
        return `${value}${t('时')}`;
      case 'minute':
        return `${value}${t('分')}`;
      default:
        return value;
    }
  };
  // 弹框标题
  const title = computed(() => {
    return isDate.value ? t('请选择时间') : t('请选择区间');
  });

  // 关闭组件
  const closeComponent = () => {
    if (props.isRevise) {
      emit('close');
    }
    else {
      H5AppNavigateBack();
    }
  };
  // 重置
  const reset = () => {
    setComponentReset(componentRef.value);
  };
  // 录入空值
  const enterNull = () => {
    if (props.isRevise) {
      emit('confirm', nullValueRef.value);
    }
    else {
      setComponentNull(componentRef.value);
    }
  };

  // 确认
  const confirm = async () => {
    let value = '';
    let valueExtension = '';
    if (isDate.value) {
      // 日期组件
      const timeValue = setSeconds(dateValue.value, secondValue.value);
      value = format(timeValue, dateFormat.value);
      valueExtension = { timeStamp: getTimestamp(timeValue, dateFormat.value) };
      valueExtension = JSON.stringify(valueExtension);
    }
    else {
      const start = setSeconds(
        segmentOptions[0].dateTime,
        segmentOptions[0].secondValue,
      );
      const end = setSeconds(
        segmentOptions[1].dateTime,
        segmentOptions[1].secondValue,
      );
      const params = {
        startTime: format(start, 'yyyy-MM-dd HH:mm:ss'),
        endTime: format(end, 'yyyy-MM-dd HH:mm:ss'),
        dateType: componentRef.value.configInfo?.format || undefined,
        roundingRule:
          !(componentRef.value.configInfo
            && componentRef.value.configInfo.round === 'roundingDown'),
      };
      // 调用接口获取组件值
      const res = await reqCalculateDateApi(params);
      if (res.code === 0) {
        value = res.data.calculateResult;
        valueExtension = JSON.stringify({
          timeSeconds: res.data.timeSeconds,
        });
      }
    }
    if (props.isRevise) {
      emit('confirm', value, valueExtension);
    }
    else {
      setComponentValue({ ...componentRef.value, value, valueExtension, emptyValue: false });
    }
  };

  // 设置日期组件值
  const setDateDefaultValue = () => {
    if (
      componentRef.value.value
      && componentRef.value.value !== nullValueRef.value
      && !props.isRevise
    ) {
      const extension = JSON.parse(componentRef.value.valueExtension);
      dateValue.value = new Date(extension.timeStamp);
      secondValue.value = getSeconds(extension.timeStamp);
    }
    else {
      dateValue.value = serverTime.value;
      secondValue.value = getSeconds(dateValue.value);
    }
  };

  // 获取系统默认日期格式值
  const getSystemDateValue = async () => {
    try {
      const res1 = getParameterByCode('platform.sys.time-format');
      const res2 = getParameterByCode('platform.sys.time.default-format');
      defaultFormat.value
          = JSON.parse(res1.value)[res2.value] || 'yyyy-MM-dd HH:mm:ss';
      showComponent.value = true;
      return '';
    }
    catch (error) {
      console.error(error);
      uni.showToast({
        title: t('参数配置获取失败，请重新点击'),
        icon: 'none',
      });
    }
  };

  watch(
    () => componentRef.value,
    () => {
      // 初始化日期组件的值
      if (isDate.value) {
        showComponent.value = false;
        setDateDefaultValue();
        getSystemDateValue();
      }
    },
  );

  return {
    componentRef,
    datePicker,
    dateClass,
    minDate,
    maxDate,
    isDate,
    dateValue,
    secondValue,
    dateSecondColumns,
    startTimeSecondColumns,
    endTimeSecondColumns,
    title,
    segmentOptions,
    segmentValue,
    timeMax,
    showComponent,
    formatter,
    closeComponent,
    reset,
    enterNull,
    confirm,
    setSegmentOptionsDateTime,
  };
};
