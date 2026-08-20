import { defineStore } from 'pinia';
import { reactive } from 'vue';

export const useChangeStatus = defineStore('changeStatus', () => {
  const CHANGE = reactive({
    status: false,
  });

  const changeStatus = (val: boolean = true) => {
    CHANGE.status = val;
  };

  return { changeStatus, CHANGE_STATUS: CHANGE };
});
