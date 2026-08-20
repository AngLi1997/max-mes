import { RouteRecordRaw } from 'vue-router';

export const OutsideRouter: RouteRecordRaw = {
  path: '/outside',
  component: () => import('@/pages/Outside/index.vue'),
  meta: { title: t('外链') },
  name: 'outside',
};
