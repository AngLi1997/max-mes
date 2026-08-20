import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { ref } from 'vue';

export const nullValueRef = ref('N/A');

export const getSystemNullValue = async () => {
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  const res = getParameterByCode('mes.record.empty-data');
  nullValueRef.value = res.value || 'N/A';
};
