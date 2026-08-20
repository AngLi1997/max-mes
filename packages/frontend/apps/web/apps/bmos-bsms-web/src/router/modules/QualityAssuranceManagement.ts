import { RouteRecordRaw } from 'vue-router';

const QualityAssuranceManagement: RouteRecordRaw = {
  path: '/qualityAssuranceManagement',
  redirect: '/qualityAssuranceManagement/specimen-examination-review',
  meta: {
    title: '质保管理',
    id: '170060',
    icon: 'QualityAssuranceManagement',
  },
  children: [
    {
      path: '/qualityAssuranceManagement/specimen-examination-review',
      meta: {
        title: '标本请验审核',
        id: '170060001',
      },
      name: 'SpecimenExaminationReview',
      component: () => import('@/pages/QualityAssuranceManagement/SpecimenExaminationReview/components/Page/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/release-management',
      meta: {
        title: '放行单管理',
        id: '170060002',
      },
      name: 'ReleaseManagement',
      component: () => import('@/pages/QualityAssuranceManagement/ReleaseManagement/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/release-review',
      meta: {
        title: '放行单审核',
        id: '170060003',
      },
      name: 'ReleaseReview',
      component: () => import('@/pages/QualityAssuranceManagement/ReleaseReview/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/release-request',
      meta: {
        title: '放行单查询',
        id: '170060004',
      },
      name: 'ReleaseRequest',
      component: () => import('@/pages/QualityAssuranceManagement/ReleaseRequest/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/feeding-audit',
      meta: {
        title: '投料质保审核',
        id: '170060005',
      },
      name: 'FeedingAudit',
      component: () => import('@/pages/QualityAssuranceManagement/FeedingAudit/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/quality-assurance-audit',
      meta: {
        title: '科研调用质保审核',
        id: '170060006',
      },
      name: 'QualityAssuranceAudit',
      component: () => import('@/pages/QualityAssuranceManagement/QualityAssuranceAudit/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/destruction-trial',
      meta: {
        title: '销毁出库初审',
        id: '170060007',
      },
      name: 'DestructionTrial',
      component: () => import('@/pages/QualityAssuranceManagement/DestructionTrial/index.vue'),
    },
    {
      path: '/qualityAssuranceManagement/destruction-review',
      meta: {
        title: '销毁出库复审',
        id: '170060008',
      },
      name: 'DestructionReview',
      component: () => import('@/pages/QualityAssuranceManagement/DestructionReview/index.vue'),
    },
  ],
};

export default QualityAssuranceManagement;
