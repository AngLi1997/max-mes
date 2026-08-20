import { RouteRecordRaw } from 'vue-router';

const SpecimenManagement: RouteRecordRaw = {
  path: '/specimenManagement',
  redirect: '/specimenManagement/sample-reception',
  meta: {
    title: '标本管理',
    id: '210020',
    icon: 'SpecimenManagement',
  },
  children: [
    {
      path: '/specimenManagement/sample-reception',
      meta: {
        title: '标本接收',
        id: '210020001',
      },
      name: 'SampleReception',
      component: () => import('@/pages/SpecimenManagement/SampleReception/index.vue'),
    },
    {
      path: '/specimenManagement/receive-review',
      meta: {
        title: '接收审核',
        id: '210020002',
      },
      name: 'ReceiveReview',
      component: () => import('@/pages/SpecimenManagement/ReceiveReview/index.vue'),
    },
    {
      path: '/specimenManagement/specimen-rejection',
      meta: {
        title: '标本拒收',
        id: '210020003',
      },
      name: 'SpecimenRejection',
      component: () => import('@/pages/SpecimenManagement/SpecimenRejection/index.vue'),
    },
    {
      path: '/specimenManagement/reject-review',
      meta: {
        title: '拒收审核',
        id: '210020004',
      },
      name: 'RejectReview',
      component: () => import('@/pages/SpecimenManagement/RejectReview/index.vue'),
    },
  ],
};

export default SpecimenManagement;
