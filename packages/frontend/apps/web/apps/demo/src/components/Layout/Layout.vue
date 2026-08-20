<script setup lang="ts">
  import { watch, ref } from 'vue';
  import { Menu, Layout, LayoutSider, LayoutContent, SubMenu, MenuItem } from 'ant-design-vue';
  import type { MenuProps } from 'ant-design-vue';
  import { useMenuStore } from './store';
  import { storeToRefs } from 'pinia';
  import { KEY, fieldProps } from './type';
  import Header from './components/header.vue';

  const store = useMenuStore();
  const { menus } = storeToRefs(store);
  const selectedKeys = ref<KEY[]>([]);
  const openKeys = ref<KEY[]>([]);
  const collapsed = ref<boolean>(false);

  const menu_click: MenuProps['onClick'] = ({ item, key, keyPath }) => {
    console.log(item, key, keyPath, 'item, key, keyPath');
    if (!selectedKeys.value.includes(key)) {
      selectedKeys.value.push(key);
    }
  };

  withDefaults(
    defineProps<{
      menus: string[];
      field: fieldProps;
    }>(),
    {
      menus: () => [''],
      field: () => ({
        key: 'key',
        label: 'label',
        title: 'title',
        children: 'children',
      }),
    },
  );
  watch(menus, () => {
    console.log(menus, 'menusmenusmenusmenus');
  });
</script>

<template>
  <Layout style="min-height: 100vh">
    <LayoutSider v-model:collapsed="collapsed" collapsible>
      <Menu @click="menu_click" mode="inline" :selectedKeys="selectedKeys" theme="dark" v-model:openKeys="openKeys">
        <SubMenu :key="items.path" v-for="items in menus">
          <template #icon>
            <file-text-outlined />
          </template>
          <template #title>{{ items.meta.title }}</template>
          <MenuItem v-for="item in items.children" :key="item.path">
            <router-link :to="{ path: item.path }">
              {{ item.meta.title }}
            </router-link>
          </MenuItem>
        </SubMenu>
      </Menu>
    </LayoutSider>
    <Layout style="flex: 1">
      <Header></Header>
      <LayoutContent>
        <router-view></router-view>
      </LayoutContent>
    </Layout>
  </Layout>
</template>
