import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useWeighingMachineStore = defineStore('weighingMachine', () => {
  // 选中的秤具
  const selectedBalance = ref({});

  const setSelectedBalance = (data) => {
    selectedBalance.value = data;
  };

  return {
    selectedBalance,
    setSelectedBalance,
  };
});
