<script setup lang="ts">
  import { watch, ref } from 'vue';
  import { Menu, Layout, LayoutSider, LayoutContent } from 'ant-design-vue';
  import type { MenuProps } from 'ant-design-vue';
  import { handleMenuList } from './store';
  import { KEY } from './type';
  import Header from './components/header.vue';
  import { useRouter, RouteLocationNormalizedLoaded } from 'vue-router';
  import BmSubMenu from './components/menu.vue';
  import { cloneDeep } from '@bmos/utils';
  const router = useRouter();
  const props = withDefaults(
    defineProps<{
      list: Array<any>;
    }>(),
    {
      list: () => [],
    },
  );

  const selectedKeys = ref<KEY[]>([]);
  const openKeys = ref<KEY[]>([]);
  const collapsed = ref<boolean>(false);

  const menu_click: MenuProps['onClick'] = ({ key }: any) => {
    if (!selectedKeys.value.includes(key)) {
      selectedKeys.value = [key];
    }
    router.push(key as string);
  };

  const menuList = computed(() => {
    if (props.list.length === 0) return [];
    return handleMenuList(cloneDeep(props.list));
  });

  const parentKeys = (key: KEY, list: typeof menuList.value): KEY[] => {
    let parents: KEY[] = [];
    if ((key + '').indexOf('/') < 0) key = '/' + key;
    for (let index = 0; index < list.length; index++) {
      const cur = list[index];

      if (cur.path === key) {
        parents.push(cur.path);

        return parents;
      }

      if (cur.children && cur.children.length > 0) {
        const res = parentKeys(key, cur.children);

        if (res && res.length > 0) {
          parents.push(cur.path, ...res);
          return parents;
        }
      }
    }
    return parents;
  };

  const handlePath = (route: RouteLocationNormalizedLoaded) => {
    if (menuList.value.length === 0) return;
    const { path, meta } = route;
    if (meta && meta.hidden) return;
    if (!path) return;
    let curPath: string = path;
    if (meta && meta.parentPath) {
      curPath = meta.parentPath as string;
    }
    selectedKeys.value = [curPath];

    const parentKey = parentKeys(curPath, menuList.value);
    parentKey.pop();
    let keys: any = new Set([...openKeys.value, ...parentKey]);
    openKeys.value = [...keys];
  };
  watch(
    () => [router.currentRoute.value, menuList.value],
    () => {
      handlePath(router.currentRoute.value);
    },
    {
      immediate: true,
    },
  );
  const route = useRoute();
  const isKeepAlive = computed(() => route.meta.keepAlive);
  const currentKey = computed(() => route.fullPath);
  const hasHidden = computed(() => {
    return route.meta.hiddenMenu;
  });
</script>

<template>
  <Layout class="dc-layout">
    <LayoutSider v-show="!hasHidden" v-model:collapsed="collapsed" :style="{ background: '#103566' }">
      <Menu v-model:openKeys="openKeys" mode="inline" :selectedKeys="selectedKeys" theme="dark" @click="menu_click">
        <BmSubMenu v-for="(item, index) in menuList" :key="index" :menu="item"></BmSubMenu>
      </Menu>
    </LayoutSider>
    <Layout style="flex: 1">
      <Header v-show="!hasHidden"></Header>
      <LayoutContent>
        <div class="dc-content">
          <router-view v-slot="{ Component }">
            <keep-alive v-if="isKeepAlive">
              <component :is="Component" :key="currentKey" />
            </keep-alive>
            <component :is="Component" v-else :key="currentKey" />
          </router-view>
        </div>
      </LayoutContent>
    </Layout>
  </Layout>
</template>
<style scoped>
  .main-layout {
    height: 100%;
  }
  .dc-content {
    height: inherit;
    height: 100%;
    padding: 12px;
    box-sizing: border-box;
  }
  .dc-layout {
    height: 100%;
    background-color: var(--bmos-background-color);
  }
  :deep(.dc-layout-sider-children) {
    overflow-y: auto;
  }
  :deep(.dc-layout-sider-children)::-webkit-scrollbar {
    width: 0;
  }
</style>
