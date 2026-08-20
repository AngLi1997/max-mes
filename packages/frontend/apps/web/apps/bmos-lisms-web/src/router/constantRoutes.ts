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
      },
      {
        path: '/detail',
        component: () => import('@/pages/Home/index.vue'),
      },
      {
        path: '/inspectionManagement/task-center/detail',
        component: () => import('@/pages/InspectionManagement/TaskCenter/Detail.vue'),
        meta: {
          title: '任务中心',
          code: '0201',
          id: '0201',
          hidden: false,
          parentPath: '/inspectionManagement/task-center',
        },
        name: 'inspection-management-task-center-detail',
      },
    ],
  },
];
