import { LogoBackgroundMode } from '@bmos/components';
import { sendMessage } from '@bmos/utils';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import BasicData from './modules/BasicData';
import InspectionManage from './modules/InspectionManage';

export const asyncRoutes: RouteRecordRaw[] = [BasicData, InspectionManage];

export const constantRoutes = [
  {
    path: '/',
    name: 'Index',
    meta: {
      title: '首页',
      code: '首页',
    },
    component: () => import('../pages/Main/index.vue'),
    children: [{ path: '', component: () => import('../pages/Home/index.vue') }],
  },
];

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  // routes:[]
  routes: [...constantRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;
router.afterEach(to => {
  routerChangeFlag && sendMessage('routerChange', { fullPath: to.fullPath.slice(1) });
  routerChangeFlag = true;

  if (to.meta && to.meta.hiddenMenu) {
    sendMessage('logoColor', { color: LogoBackgroundMode.WHITE });
  } else {
    sendMessage('logoColor', { color: LogoBackgroundMode.BLUE });
  }
});

export default router;
