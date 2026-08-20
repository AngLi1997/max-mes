import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useMaterialWeighingStore = defineStore('materialWeighing', () => {
  // 产出称量信息详情
  const detailData = ref({
    categoryType: '0',
  });
  // 称量人列表
  const weighingPersonList = ref([]);
  // 称量复核人列表
  const reCheckerList = ref([]);

  // 签名人员
  const storeSignValue = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
    remark: '',
  });

  const setStoreSignValue = (data) => {
    storeSignValue.value = data;
  };

  const setDetailData = (data) => {
    detailData.value = data;
  };

  // 设置称量复核人列表
  const setReCheckerList = (data) => {
    reCheckerList.value = data;
  };

  // 设置称量人列表
  const setWeighingPersonList = (data) => {
    weighingPersonList.value = data;
  };

  const initMaterialWeighingStore = () => {
    detailData.value = {
      categoryType: '0',
    };
  };

  return {
    detailData,
    reCheckerList,
    weighingPersonList,
    setDetailData,
    setReCheckerList,
    setWeighingPersonList,
    initMaterialWeighingStore,
    setStoreSignValue,
    storeSignValue,
  };
});
