import { RouteRecordRaw } from 'vue-router';

export const DigitalSignageConfiguration: RouteRecordRaw = {
  path: '/digital-signage-configuration',
  redirect: '/digital-signage-configuration/digital-signage-configuration',
  meta: {
    title: t('数字看板配置'),
    id: '200030',
    icon: 'SystemConfiguration', //VisualizationLargeScreen 暂无此图标
  },
  children: [
    {
      path: '/digital-signage-configuration/digital-signage-configuration',
      meta: {
        title: t('数字看板配置'),
        id: '200030001',
      },
      name: 'DigitalSignageConfiguration',
      component: () => import('@/pages/digitalSignageConfiguration/BaiEDigitalSignageConfiguration/index.vue'),
    },
  ],
};
