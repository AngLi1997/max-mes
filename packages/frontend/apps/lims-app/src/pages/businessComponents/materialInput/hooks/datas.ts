import { ref } from 'vue';

// 是否已投料
export const isEquipented = ref(false);

// 签名人信息
export const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: ''
});

// 提交数据
export const submitData = ref({
    componentInstanceId: '', // 组件实例id
    deviceId: '', // 投料设备id
    inputUserId: '', // 投料人id
    remark: '', // 投料备注
    storateMaterialNoList: []// 投料物料件编号列表
});
