import { RouteRecordRaw } from 'vue-router';

const QualityRouter: RouteRecordRaw = {
  path: '/Quality',
  redirect: '/ExamineTraceability',
  meta: {
    title: '质量追溯',
    id: '111020',
    icon: 'QualityTraceability',
  },
  children: [
    {
      path: '/ExamineTraceability',
      component: () => import('../../pages/Quality/ExamineTraceability/index.vue'),
      meta: { title: '审核流追溯', id: '111020001' },
      name: 'ExamineTraceability',
    },
    {
      path: '/QualityTraceability',
      component: () => import('../../pages/Quality/QualityTraceability/index.vue'),
      meta: { title: '质量追溯', id: '111020002' },
      name: 'QualityTraceability',
    },
    {
      path: '/SignatureTraceability',
      component: () => import('../../pages/Quality/SignatureTraceability/index.vue'),
      meta: { title: '签名追溯', id: '111020003' },
      name: 'SignatureTraceability',
    },
  ],
};

export default QualityRouter;
