import { useConfig } from '@/stores/config';

export const getWarehouseConfigByCode = computed(() => {
  const { configs } = useConfig();
  const val = configs?.['plasma.sys.warehouse-info.isShow'];
  return val?.value ? val?.value === 'true' : false;
});
