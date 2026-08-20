import { t } from '@bmos/i18n';

type ItemType = {
  label: string;
  key: string;
  span?: number;
  type?: string;
};

export const useDescriptions = () => {
  const itemFields: ItemType[] = [
    {
      label: t('报告单编号'),
      key: 'reportBillNo',
    },
    {
      label: t('检品名称'),
      key: 'checkArticleName',
    },
    {
      label: t('检品来源'),
      key: 'originOrg',
    },
    {
      label: t('献浆者编号'),
      key: 'plasmaDonorNo',
    },
    {
      label: t('献浆者姓名'),
      key: 'plasmaDonorName',
    },
    {
      label: t('核查份数'),
      key: 'checkNumber',
    },
    {
      label: t('判定依据'),
      key: 'checkBase',
      span: 2,
    },
    {
      label: t('核查结论'),
      key: 'checkResult',
      span: 2,
    },
    {
      label: t('核查结果'),
      key: 'conclusion',
      span: 2,
    },
    {
      label: t('报告人'),
      key: 'reportByName',
      type: 'image',
    },
    {
      label: t('签发人'),
      key: 'signByName',
      type: 'image',
    },
    {
      label: t('报告日期'),
      key: 'reportDate',
    },
    {
      label: t('签发日期'),
      key: 'signDate',
    },
  ];

  return {
    itemFields,
  };
};
