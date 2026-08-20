import { RouteRecordRaw } from 'vue-router';

const SystemRouter: RouteRecordRaw = {
  path: '/System',
  redirect: '/SystemPage',
  meta: {
    title: '系统设置',
    id: '100020',
    icon: 'SystemConfiguration',
  },
  children: [
    {
      path: '/codeRule',
      component: () => import('../../pages/System/codeRule/index.vue'),
      meta: { title: '编号规则', id: '100020001' },
      name: 'codeRule',
    },
    {
      path: '/expression',
      component: () => import('../../pages/System/expressionConfig/index.vue'),
      meta: { title: '公式配置', id: '100020006' },
      name: 'expression',
    },
    {
      path: '/tagConfig',
      component: () => import('../../pages/System/tagConfig/index.vue'),
      meta: { title: '标签配置', id: '100020007' },
      name: 'tagConfig',
    },
    {
      path: '/dict',
      component: () => import('../../pages/System/dict/index.vue'),
      meta: { title: '字典管理', id: '100020009' },
      name: 'dict',
    },
    {
      path: '/systemInfo',
      component: () => import('../../pages/System/systemInfo/index.vue'),
      meta: { title: '系统信息', id: '100020010' },
      name: 'systemInfo',
    },
  ],
};
export default SystemRouter;
