import { RouteRecordRaw } from 'vue-router';

const ConfigurationManagement: RouteRecordRaw = {
  path: '/configurationManagement',
  redirect: '/configurationManagement/publish-configuration',
  meta: {
    title: '配置管理',
    id: '180060',
    icon: 'ConfigurationManagement',
  },
  children: [
    {
      path: '/configurationManagement/publish-configuration',
      meta: {
        title: '总发布校验配置',
        id: '180060001',
      },
      name: 'PublishConfiguration',
      component: () => import('@/pages/ConfigurationManagement/PublishConfiguration/index.vue'),
    },
  ],
};

export default ConfigurationManagement;
