import { t } from '@bmos/i18n';

type ItemType = {
  label: string;
  key: string;
  span?: number;
  img?: boolean;
};

export const useDescriptions = () => {
  const itemFields: ItemType[] = [
    {
      label: t('文件编号'),
      key: 'documentNo',
    },
    {
      label: t('报告编号'),
      key: 'reportNo',
    },
    {
      label: t('检品名称'),
      key: 'jpName',
      span: 2,
    },
    {
      label: t('检品来源'),
      key: 'originOrg',
    },
    {
      label: t('检品批号'),
      key: 'inWarehouseBatchNo',
    },
    {
      label: t('核查批号'),
      key: 'checkNo',
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
      label: t('核查结果'),
      key: 'checkResult',
      span: 2,
    },
    {
      label: t('核查结论'),
      key: 'conclusion',
      span: 2,
    },
    {
      label: t('报告人'),
      key: 'createSignatureId',
      img: true,
    },
    {
      label: t('签发人'),
      key: 'auditSignatureId',
      img: true,
    },
    {
      label: t('报告日期'),
      key: 'reportTime',
    },
    {
      label: t('签发日期'),
      key: 'auditTime',
    },
  ];

  return {
    itemFields,
  };
};
