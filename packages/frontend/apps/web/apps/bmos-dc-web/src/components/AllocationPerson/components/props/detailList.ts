import { t } from '@bmos/i18n';

export const DetailListEmits = ['icon-click'];
export const DetailListProps = {
  list: () => [
    {
      name: t('测试'),
      id: 'demo',
    },
    {
      name: t('测试1'),
      id: 'demo1',
    },
  ],
};
