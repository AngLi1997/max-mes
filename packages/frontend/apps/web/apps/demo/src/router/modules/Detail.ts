
import { RouteRecordRaw } from "vue-router";

const DetailRouter: RouteRecordRaw = {
  path: '/Detail',
  redirect: '/DetailPage',
  meta: {
    title: '详情页',
  },
  children: [
    {
      path: '/DetailPage',
      component: () => import('../../views/detail/detailPage/index.vue'),
      meta: { title: '详情信息'},
      name: 'DetailPage'
    },
    {
      path: '/DetailRecord',
      component: () => import('../../views/detail/detailRecord/index.vue'),
      meta: { title: '详情记录'},
      name: 'DetailRecord'
    }
  ]
};

export default DetailRouter;

