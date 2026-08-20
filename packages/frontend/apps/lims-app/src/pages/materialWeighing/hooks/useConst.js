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

  // 手动产出表格列
  const manualOutputColumns = [{
    prop: 'quantity',
    label: t('物料量'),
    width: 150,
  }, {
    prop: 'unit',
    label: t('单位'),
    width: 150,
  }, {
    prop: 'containerName',
    label: t('容器'),
    width: 240,
  }, {
    prop: 'materialPositionName',
    label: t('暂存货位'),
    width: 240,
  }];
  // 秤具产出表格列
  const weighingMachineOutputColumns = [
    {
      prop: 'netWeight',
      label: t('净重'),
      width: 150,
    },
    {
      prop: 'tareWeight',
      label: t('皮重'),
      width: 150,
    },
    {
      prop: 'grossWeight',
      label: t('毛重'),
      width: 150,
    },
    {
      prop: 'unit',
      label: t('单位'),
      width: 150,
    },
    {
      prop: 'containerName',
      label: t('容器'),
      width: 240,
    },
    {
      prop: 'cargoPositionName',
      label: t('暂存货位'),
      width: 240,
    },
  ];
  return {
    stepList,
    manualOutputColumns,
    weighingMachineOutputColumns,
  };
};
