import { t } from '@/utils/useBmosI18n.js';
import { ref, reactive } from 'vue';

export const useInvest = ({ props }) => {
    const showliquidMonad = ref(false);
    const infoItems = [
      {
        label: t('配液单'),
        field: 'planName',
        type: 'text'
      },
      { label: t('切换配液单'), type: 'button', click: () => showliquidMonad.value = true }
    ];
    
  return {
    infoItems,
    showliquidMonad
  };
};
