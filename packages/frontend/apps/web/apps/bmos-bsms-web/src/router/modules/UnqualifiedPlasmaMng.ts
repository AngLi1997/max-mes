import { RouteRecordRaw } from 'vue-router';

const UnqualifiedPlasmaMng: RouteRecordRaw = {
  path: '/unqualifiedPlasmaMng',
  redirect: '/unqualifiedPlasmaMng/pulp-station-plasma-mng',
  meta: {
    title: '不合格血浆管理',
    id: '170070',
    icon: 'UnqualifiedPlasmaMng',
  },
  children: [
    {
      path: '/unqualifiedPlasmaMng/pulp-station-plasma-mng',
      meta: {
        title: '浆站不合格血浆管理',
        id: '170070001',
      },
      name: 'PulpStationPlasmaMng',
      component: () => import('@/pages/UnqualifiedPlasmaMng/PulpStationPlasmaMng/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/enterprise-plasma-mng',
      meta: {
        title: '企业不合格血浆管理',
        id: '170070002',
      },
      name: 'EnterprisePlasmaMng',
      component: () => import('@/pages/UnqualifiedPlasmaMng/EnterprisePlasmaMng/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/checkRecord-review',
      meta: {
        title: '不合格核查记录审核',
        id: '170070003',
      },
      name: 'CheckRecordReview',
      component: () => import('@/pages/UnqualifiedPlasmaMng/CheckRecordReview/components/Page/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/check-record-query',
      meta: {
        title: '不合格核查记录查询',
        id: '170070004',
      },
      name: 'CheckRecordQuery',
      component: () => import('@/pages/UnqualifiedPlasmaMng/CheckRecordQuery/components/Page/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/verification-report-submit-review',
      meta: {
        title: '不合格核查报告送审',
        id: '170070005',
      },
      name: 'VerificationReportSubmitReview',
      component: () => import('@/pages/UnqualifiedPlasmaMng/VerificationReportSubmitReview/components/Page/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/verification-report-review',
      meta: {
        title: '不合格核查报告审核',
        id: '170070006',
      },
      name: 'VerificationReportReview',
      component: () => import('@/pages/UnqualifiedPlasmaMng/VerificationReportReview/components/Page/index.vue'),
    },
    {
      path: '/unqualifiedPlasmaMng/verification-report-query',
      meta: {
        title: '不合格核查报告查询',
        id: '170070007',
      },
      name: 'VerificationReportQuery',
      component: () => import('@/pages/UnqualifiedPlasmaMng/VerificationReportQuery/components/Page/index.vue'),
    },
  ],
};

export default UnqualifiedPlasmaMng;
