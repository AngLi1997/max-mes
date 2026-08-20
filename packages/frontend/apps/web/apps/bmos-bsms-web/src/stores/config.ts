import { getParameterDetailByCode } from '@/services';
import { message } from 'ant-design-vue';
import { defineStore } from 'pinia';
import { reactive } from 'vue';

export const useConfig = defineStore('counter', () => {
  const configs = reactive<Record<string, any>>({});

  const findConfigByCode = async (code: string = 'plasma.sys.warehouse-info.isShow') => {
    try {
      const { data } = await getParameterDetailByCode(code);
      configs[code] = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  return { findConfigByCode, configs };
});
