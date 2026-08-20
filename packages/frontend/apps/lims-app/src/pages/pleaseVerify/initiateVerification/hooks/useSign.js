import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';

export const useSign = () => {
  // 签名
  const signOpen = ref(false);

  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
  });
  // 签名内容框
  const labelList = ref([
      {
        label: t('请验人'),
        // 签名动作
        signatureAction: 0,
        options: null,
        menuId: 121010001002005
      },
  ]);

  const signatureParams = ref({});
  return {
    signOpen,
    signValue,
    labelList,
    signatureParams
  }
}