import { t } from '@/utils/useBmosI18n.js';

export const useConst = () => {
  const stepList = [
    {
      title: t('物料信息'),
    },
    {
      title: t('模式&设备'),
    },
    {
      title: t('清零&去皮'),
    },
    {
      title: t('称量'),
    },
  ];

  return {
    stepList,
  };
};
