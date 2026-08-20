import { RouteRecordRaw } from 'vue-router';

const SpecimenManagement: RouteRecordRaw = {
  path: '/specimenManagement',
  redirect: '/specimenManagement/specimen-exchange',
  meta: {
    title: '标本管理',
    id: '180010',
    icon: 'SpecimenManagement',
  },
  children: [
    {
      path: '/specimenManagement/specimen-exchange',
      meta: {
        title: '标本交接',
        id: '180010001',
      },
      name: 'SpecimenExchange',
      component: () => import('@/pages/SpecimenManagement/SpecimenExchange/index.vue'),
    },
  ],
};

export default SpecimenManagement;
