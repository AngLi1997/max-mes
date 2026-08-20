import { RouteRecordRaw } from 'vue-router';

const BasicData: RouteRecordRaw = {
  path: '/basic-data',
  redirect: '/basic-data/test-article',
  meta: {
    title: '基本数据',
    id: '130010',
    icon: 'BasicData',
  },
  children: [
    {
      path: '/basic-data/test-article',
      component: () =>
        import('@/pages/BasicData/TestArticle/index.vue'),
      meta: { title: '检品管理', id: '130010001' },
      name: '',
    },
    {
      path: '/basic-data/analysis-item',
      component: () =>
        import('@/pages/BasicData/AnalysisItem/index.vue'),
      meta: { title: '分析项目管理', id: '130010002' },
      name: '',
    },
    {
      path: '/basic-data/inspection-item',
      component: () =>
        import('@/pages/BasicData/InspectionItem/index.vue'),
      meta: { title: '检验项目管理', id: '130010003' },
      name: '',
    },
    {
      path: '/basic-data/experimental-package',
      component: () =>
        import('@/pages/BasicData/ExperimentalPackage/index.vue'),
      meta: { title: '实验包管理', id: '130010004' },
      name: '',
    }
  ],
};
export default BasicData;
