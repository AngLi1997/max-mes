export const constantRoutes = [
  {
    path: '/',
    name: 'Index',
    meta: {
      title: '首页',
      code: '首页',
    },
    redirect: '/home',
    component: () => import('@/pages/Main/index.vue'),
    children: [
      { path: '/home', component: () => import('@/pages/Home/index.vue') },
      {
        path: '/room-manage/config-environment-params',
        component: () => import('@/pages/RegionalManagement/roomManagement/configEnvironmentParams/index.vue'),
        meta: {
          title: '配置环境变量',
          hidden: true,
          id: '160030002',
          parentPath: '/regional-manage/room-manage',
        },
        name: 'configEnvironmentParams',
      },
    ],
  },
];
