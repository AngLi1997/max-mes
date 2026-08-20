import { LogoBackgroundMode } from '@bmos/components';
import { sendMessage } from '@bmos/utils';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import QualityRouter from './modules/Log';
import MaterialRouter from './modules/Material';
import OutsideRouter from './modules/Outside';
import LogRouter from './modules/Quality';

export const asyncRoutes: RouteRecordRaw[] = [LogRouter, QualityRouter, MaterialRouter, OutsideRouter];

const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: '/',
    meta: {
      title: '首页',
      code: '首页',
    },
    component: () => import('../pages/Home/index.vue'),
    children: [
      {
        path: 'dashboard',
        component: () => import('../pages/System/index.vue'),
        name: 'Dashboard',
        meta: {
          title: '组件',
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
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
