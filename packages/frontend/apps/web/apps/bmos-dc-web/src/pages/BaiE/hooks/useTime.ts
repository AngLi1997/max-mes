import dayjs, { Dayjs } from 'dayjs';
import 'dayjs/locale/zh-cn'; // 导入中文语言包

export const useTime = () => {
  const timer = ref<any>(null);
  const timeValue = ref<Dayjs>(dayjs());

  const time = computed(() => {
    return timeValue.value.format('HH:mm:ss');
  });
  const date = computed(() => {
    return timeValue.value.format('YYYY-MM-DD');
  });
  const today = computed(() => {
    return timeValue.value.locale('zh-cn').format('dddd');
  });
  const year = computed(() => {
    return timeValue.value.format('YYYY');
  });
  onMounted(() => {
    timer.value = setInterval(() => {
      timeValue.value = dayjs();
    }, 1000);
  });
  onUnmounted(() => {
    clearInterval(timer.value);
    timer.value = null;
  });

  return {
    time,
    date,
    today,
    year,
  };
};
