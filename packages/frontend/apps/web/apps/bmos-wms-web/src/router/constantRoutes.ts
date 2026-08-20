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
        path: '/manage/inventory-manage/inventory-pieces',
        name: 'inventory-pieces',
        meta: {
          title: '货品件',
          id: '150020002',
          code: '150020002',
          hidden: false,
          parentPath: '/manage/inventory-manage',
        },
        component: () => import('@/pages/Manage/InventoryManage/InventoryPieces.vue'),
      },
      {
        path: '/manage/send-out/batch-delivery',
        name: 'batch-delivery',
        meta: {
          title: '批次发料',
          id: '150020003',
          code: '150020003',
          hidden: false,
          parentPath: '/manage/send-out',
        },
        component: () => import('@/pages/Manage/SendOut/pages/BatchDelivery.vue'),
      },
    ],
  },
];
