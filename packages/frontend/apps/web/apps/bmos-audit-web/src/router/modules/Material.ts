
import { RouteRecordRaw } from "vue-router";

const MaterialRouter: RouteRecordRaw = {
  path: '/Material',
  redirect: '/MaterialTraceability',
  meta: {
    title: '物料追溯',
    id:'111040',
    icon: 'MaterialTraceability',
  },
  children: [
    {
      path: '/MaterialTraceability',
      component: () => import('../../pages/Material/index.vue'),
      meta: { title: '物料追溯',id:'111040001'},
      name: 'MaterialTraceability'
    },
  ]
};

export default MaterialRouter;

