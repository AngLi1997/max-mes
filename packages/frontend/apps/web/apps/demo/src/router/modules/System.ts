import { RouteRecordRaw } from "vue-router";

const SystemRouter: RouteRecordRaw = {
  path: '/System',
  redirect: '/SystemPage', 
  meta: {
    title: '系统设置',
  },
  children: [
    {
      path: '/SystemPage',
      component: () => import('../../views/system/user/index.vue'),
      meta: { title: '用户管理'},
      name:''
    },
    {
      path: '/SystemRecord',
      component: () => import('../../views/system/employee/index.vue'),
      meta: { title: '角色管理'},
      name:''
    }
  ]
}
export default SystemRouter;
