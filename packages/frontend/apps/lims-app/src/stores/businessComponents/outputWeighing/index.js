import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useOutputWeighingStore = defineStore('outputWeighing', () => {
  // 产出称量信息详情
  const detailData = ref({});
  // 选中的秤具
  const selectedBalance = ref(null);
  // 称量人列表
  const weighingPersonList = ref([]);
  // 称量复核人列表
  const reCheckerList = ref([]);

  const setDetailData = (data) => {
    detailData.value = data;
  };

  const setSelectedBalance = (data) => {
    selectedBalance.value = data;
  };
  // 设置称量复核人列表
  const setReCheckerList = (data) => {
    reCheckerList.value = data;
  };

  // 设置称量人列表
  const setWeighingPersonList = (data) => {
    weighingPersonList.value = data;
  };

  const initOutputWeighingStore = () => {
    detailData.value = {};
    selectedBalance.value = null;
  };

  return {
    detailData,
    selectedBalance,
    reCheckerList,
    weighingPersonList,
    setDetailData,
    setSelectedBalance,
    setReCheckerList,
    setWeighingPersonList,
    initOutputWeighingStore
  };
});
