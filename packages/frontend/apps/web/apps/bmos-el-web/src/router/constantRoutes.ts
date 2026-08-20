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
      {
        path: '/home',
        component: () => import('@/pages/Home/index.vue'),
        meta: {
          title: '首页',
          hiddenMenu: true,
        },
      },
    ],
  },
];
