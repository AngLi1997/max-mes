import { RouteRecordRaw } from 'vue-router';

const InspectionManage: RouteRecordRaw = {
  path: '/inspectionManage',
  redirect: '/inspectionManage/PleaseCheckConfig',
  meta: {
    title: '检验管理',
    id: '120100',
    icon: 'InspectionManage',
  },
  children: [
    {
      path: '/inspectionManage/PleaseCheckConfig',
      meta: {
        title: '请验单配置',
        id: '120100001',
      },
      name: 'PleaseCheckConfig',
      component: () => import('@/pages/InspectionManage/PleaseCheckConfig/index.vue'),
    },
  ],
};

export default InspectionManage;
