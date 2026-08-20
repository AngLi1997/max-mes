<template>
  <div class="layout_nav">
    <a-layout>
      <!-- sidebar -->
      <a-layout-sider class="custom-sider">
        <div class="logo">
          <img src="../../../src/assets/logo.svg" alt="" />
        </div>
        <a-menu
          @click="menuChange"
          v-model:openKeys="openKeys"
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="inline">
          <a-sub-menu :key="items.path" v-for="items in routes">
            <template #icon>
              <file-text-outlined />
            </template>
            <template #title>{{ items.meta.title }}</template>
            <a-menu-item v-for="item in items.children" :key="item.path">
              <router-link :to="{ path: item.path }">
                {{ item.meta.title }}
              </router-link>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>

      <!-- navbar -->
      <a-layout>
        <NavBar></NavBar>
        <!-- menu -->
        <div class="breadcrumbHeight">
          <div class="breadcrumbSort">
            <DoubleLeftOutlined
              class="left-icon"
              :style="{ fontSize: '15px' }" />
            <a-dropdown :trigger="['contextmenu']">
              <div>
                <a-tooltip>
                  <Item
                    v-for="menu in keepAliveComponentsName"
                    :key="menu.meta.title"
                    :menu="menu"
                    :active="activeMenu.path === menu.path"
                    @close="delMenu(menu)"
                    @reload="pageReload"></Item>
                </a-tooltip>
              </div>
              <template #overlay>
                <a-menu
                  v-model:selectedKeys="selectedKeys"
                  v-model:openKeys="openKeys">
                  <a-menu-item key="1" @click="closeOtherRoute">
                    关闭右侧
                  </a-menu-item>
                  <a-menu-item key="2" @click="closeAllRoute">
                    关闭所有
                  </a-menu-item>
                  <a-menu-item key="3" @click="closeCurrentRoute(menu)">
                    关闭当前
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
            <div class="breadcrumb-right">
              <DoubleRightOutlined :style="{ fontSize: '15px' }" />
              <div style="color: #ececec;">|</div>
              <RedoOutlined :style="{ fontSize: '15px' }" />
            </div>
          </div>
        </div>

        <a-layout-content
          class="layout-content"
          :style="{ margin: '16px 16px', minHeight: '280px' }">
          <router-view></router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>
<script setup lang="ts">
  // import SideBar from './components/SideBar/side-bar.vue'
  import NavBar from './components/NavBar/nav-bar.vue';
  import { useKeepAlive } from '../../store/index';
  import { storeToRefs } from 'pinia';
  import {
    FileTextOutlined,
    DoubleLeftOutlined,
    DoubleRightOutlined,
    RedoOutlined,
  } from '@ant-design/icons-vue';
  import { useRoute } from 'vue-router';

  import { computed, ref, reactive } from 'vue';
  import Item from './components/item.vue';
  import router from '../../router/index';
  import './layout.css';
  const routes = ref(router.options.routes);
  const route = useRoute();

  const keepAlive = useKeepAlive();
  const { keepAliveComponentsName } = storeToRefs(keepAlive);

  const selectedKeys = computed(() => {
    const { meta, path } = route;
    if (meta.activeMenu) {
      return meta.activeMenu;
    }
    return [path];
  }); // 当前选中的菜单

  const openKeys = computed(() => {
    const { path } = route;
    const parentPath = findParentPath(routes.value, path);
    return [parentPath];
  }); // 当前展开的菜单

  // const collapsed = ref<boolean>(false);
  const menuChange = (e: any) => {
    sessionStorage.setItem('openKeys', e.keyPath[0]);
    sessionStorage.setItem('selectedKeys', e.key);
  };

  const findParentPath = (arr: any, targetPath: string) => {
    for (const item of arr) {
      if (item.path === targetPath) {
        return null; // 说明当前路径就是父级路径
      }
      if (item.children) {
        for (const child of item.children) {
          if (child.path === targetPath) {
            return item.path;
          }
          if (child.children) {
            const parentPath: string = findParentPath(
              child.children,
              targetPath,
            );
            if (parentPath) {
              return parentPath;
            }
          }
        }
      }
    }
    return null;
  };

  //新加代码
  const defaultMenu = {
    path: '/dashboard',
    meta: { title: '首页', hideClose: true },
  }; // 默认的首页

  router.afterEach(() => {
    addMenu(route);
    initMenu(route);
  });

  // 关闭当前标签，首页不关闭
  const closeCurrentRoute = menu => {
    if (route.path !== defaultMenu.path) {
      const tab = document.getElementById('vueAdminBoxTabCloseSelf');
      const nextPath = tab?.getAttribute('nextPath') as string | undefined;
      delMenu(route, nextPath);
    }
  };
  // 当前页面组件重新加载
  const pageReload = () => {
    const self: any = route.matched[route.matched.length - 1].instances.default;
    self.handleReload();
  };
  // 关闭右侧标签
  const closeOtherRoute = () => {
    let index = keepAliveComponentsName.value.findIndex(
      item => item.path === route.path,
    );
    if (index != -1) {
      keepAliveComponentsName.value = keepAliveComponentsName.value.slice(
        0,
        index + 1,
      );
    }
  };
 
  // 关闭所有的标签，除了首页
  const closeAllRoute = () => {
    keepAliveComponentsName.value = [defaultMenu];
    router.push(defaultMenu.path);
  };

  // 添加新的菜单项
  const addMenu = (menu: any) => {
    let { path, meta, name, query } = menu;
    if (meta.hideTabs) {
      return;
    }

    let hasMenu = keepAliveComponentsName.value.some((obj: any) => {
      return obj.path === path;
    });

    if (!hasMenu) {
      keepAliveComponentsName.value.push({ path, meta, name, query });
    }
  };

  // 删除菜单项
  const delMenu = (menu: any, nextPath?: string) => {
    let index = 0;
    if (!menu.meta.hideClose) {
      index = keepAliveComponentsName.value.findIndex(
        (item: any) => item.path === menu.path,
      );
      keepAliveComponentsName.value.splice(index, 1);
    }
    if (nextPath) {
      router.push(nextPath);
      return;
    }
    // 若删除的是当前页面，回到前一页，若为最后一页，则回到默认的首页
    if (menu.path === activeMenu.path) {
      const prePage =
        index - 1 > 0
          ? keepAliveComponentsName.value[index - 1]
          : { path: defaultMenu.path };
      router.push({ path: prePage.path, query: prePage.query || {} });
    }
  };
  let activeMenu: any = reactive({ path: '' }); // 当前激活的菜单
  // 初始化activeMenu
  const initMenu = (menu: object) => {
    activeMenu = menu;
  };
  // 初始化时调用：1. 新增菜单 2. 初始化activeMenu
  // addMenu(route);
  initMenu(route);
  if (keepAliveComponentsName.value.length === 0) {
    // 判断之前有没有调用过
    addMenu(defaultMenu);
  } // 之前没有调用过，添加默认的首页
</script>
<style scoped lang="less">
  /* sidebar */

  :deep(.custom-sider) {
    width: 224px !important;
    min-width: 224px !important;
    background-color: #0e2f5b !important;
  }

  :deep :where(.css-dev-only-do-not-override-kqecok).ant-menu-dark.ant-menu-inline .ant-menu-sub.ant-menu-inline {
    background: #0a2343 !important;
  }

  :where(.css-dev-only-do-not-override-kqecok).ant-menu-dark {
    color: rgb(255 255 255 / 65%);
    background: #0e2f5b;
  }

  /* navbar */

  .ant-layout-header {
    box-sizing: border-box !important;
    height: 56px;
    padding: 0 16px 0 8px !important;
    background-color: #fff;
  }

  .tags-view-item.active[data-v-73f34752] {
    margin-top: 0;
    color: #2871ff;
    background: #f5f7fa;
    border-color: #e8e8e8;
  }

  :deep(.tags-view-item.active a) {
    color: #2871ff;
  }

  .tags-view-item[data-v-73f34752]:first-of-type {
    margin-left: 16px;
  }

  .tags-view-item {
    box-sizing: border-box; /* 添加此行 */
    max-width: 150px;
    height: 40px;
    padding-right: 16px;
    margin-left: 0;
    // text-overflow: ellipsis;
    // white-space: nowrap;
    overflow: hidden;
    font-size: 14px;
    border-left: 1px solid #e8e8e8;
  }

  .left-icon {
    margin-left: 8px;
  }

  .layout-content {
    height: 100%;
    padding: 16px;
    overflow: auto;
    overflow-y: scroll;
    background-color: #fff;
  }
</style>
