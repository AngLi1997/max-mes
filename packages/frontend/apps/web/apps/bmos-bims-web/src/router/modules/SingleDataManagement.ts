import { RouteRecordRaw } from 'vue-router';

const SingleDataManagement: RouteRecordRaw = {
  path: '/singleDataManagement',
  redirect: '/singleDataManagement/protein-content',
  meta: {
    title: '单项数据管理',
    id: '180020',
    icon: 'SingleDataManagement',
  },
  children: [
    {
      path: '/singleDataManagement/protein-content',
      meta: {
        title: '蛋白质含量',
        id: '180020001',
      },
      name: 'ProteinContent',
      component: () => import('@/pages/SingleDataManagement/ProteinContent/components/Page/index.vue'),
    },
    {
      path: '/singleDataManagement/alt',
      meta: {
        title: 'ALT',
        id: '180020002',
      },
      name: 'ALT',
      component: () => import('@/pages/SingleDataManagement/ALT/index.vue'),
    },
    {
      path: '/singleDataManagement/hbsag',
      meta: {
        title: 'HBsAg',
        id: '180020003',
      },
      name: 'HBsAg',
      component: () => import('@/pages/SingleDataManagement/HBsAg/index.vue'),
    },
    {
      path: '/singleDataManagement/anit-hcv',
      meta: {
        title: '抗-HCV',
        id: '180020004',
      },
      name: 'AnitHCV',
      component: () => import('@/pages/SingleDataManagement/AnitHCV/index.vue'),
    },
    {
      path: '/singleDataManagement/anti-hiv',
      meta: {
        title: '抗-HIV',
        id: '180020005',
      },
      name: 'AntiHIV',
      component: () => import('@/pages/SingleDataManagement/AntiHIV/index.vue'),
    },
    {
      path: '/singleDataManagement/anit-tp',
      meta: {
        title: '抗-TP',
        id: '180020006',
      },
      name: 'AnitTP',
      component: () => import('@/pages/SingleDataManagement/AnitTp/index.vue'),
    },
    {
      path: '/singleDataManagement/pcr',
      meta: {
        title: 'PCR',
        id: '180020007',
      },
      name: 'PCR',
      component: () => import('@/pages/SingleDataManagement/PCR/index.vue'),
    },
    {
      path: '/singleDataManagement/hepatitis-a-antibody-titer',
      meta: {
        title: '甲肝抗体效价',
        id: '180020008',
      },
      name: 'HepatitisAAntibodyTiter',
      component: () => import('@/pages/SingleDataManagement/HepatitisAAntibodyTiter/index.vue'),
    },
    {
      path: '/singleDataManagement/hepatitis-b-antibody-titer',
      meta: {
        title: '乙肝抗体效价',
        id: '180020009',
      },
      name: 'HepatitisBAntibodyTiter',
      component: () => import('@/pages/SingleDataManagement/HepatitisBAntibodyTiter/index.vue'),
    },
    {
      path: '/singleDataManagement/rabies-antibody-titer',
      meta: {
        title: '狂犬病抗体效价',
        id: '180020010',
      },
      name: 'RabiesAntibodyTiter',
      component: () => import('@/pages/SingleDataManagement/RabiesAntibodyTiter/index.vue'),
    },
    {
      path: '/singleDataManagement/tetanus-antibody-titer',
      meta: {
        title: '破伤风抗体效价',
        id: '180020011',
      },
      name: 'TetanusAntibodyTiter',
      component: () => import('@/pages/SingleDataManagement/TetanusAntibodyTiter/index.vue'),
    },
  ],
};

export default SingleDataManagement;
