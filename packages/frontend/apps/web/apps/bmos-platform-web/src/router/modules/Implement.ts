import { RouteRecordRaw } from 'vue-router';

const ImplementRouter: RouteRecordRaw = {
  path: '/Implement',
  redirect: '/Implement/parameter',
  meta: {
    title: '实施配置',
    id: '100010',
    icon: 'ImplementConfiguration',
  },
  children: [
    {
      path: '/Implement/parameter',
      component: () => import('../../pages/Implement/parameter/index.vue'),
      meta: { title: '参数配置', id: '100010002' },
      name: 'parameter',
    },
    {
      path: '/Implement/project-config',
      component: () => import('../../pages/Implement/projectConfig/index.vue'),
      meta: { title: '项目配置', id: '100010005' },
      name: 'projectConfig',
    },
  ],
};
export default ImplementRouter;
