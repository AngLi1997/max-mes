import { t } from '@bmos/i18n';

export const DetailItemEmits = ['icon-click'];

export const DetailItemProps = {
  item: () => ({
    name: t('测试'),
    id: 'demo',
  }),
  // icon: CloseCircleOutlined,
};
