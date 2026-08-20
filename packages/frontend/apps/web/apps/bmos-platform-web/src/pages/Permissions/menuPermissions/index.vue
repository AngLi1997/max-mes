<template>
  <div class="container">
    <div class="topTagsNav">
      <Tabs v-if="tabs.length > 0" style="height: 100%" :destroyInactiveTabPane="true">
        <TabPane v-for="tab in tabs" :key="tab.id" :tab="tab.name">
          <MenuList :activeKey="tab.id" :treeList="tab.children"></MenuList>
        </TabPane>
      </Tabs>
      <Empty v-else></Empty>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { Tabs, TabPane } from 'ant-design-vue';
  import MenuList from './menuList.vue';
  import Empty from '../../../components/Empty/index.vue';
  import { ref, onMounted } from 'vue';
  import { getTreePermissionManage } from '../../../api/Permissions/roleManagement';

  const tabs = ref<any[]>([]);

  const getTerminalTypeApi = async (params?: any) => {
    const res: any = await getTreePermissionManage(params);
    if (res.code === 0 && res.data) {
      // 菜单权限tab过滤掉isMenu为0的项
      tabs.value = res.data.filter((item: any) => item.isMenu !== 0);
      return;
    }
    tabs.value = [];
  };

  onMounted(() => {
    getTerminalTypeApi({ containsFunc: true });
  });
</script>
<style lang="less" scoped>
  .container {
    display: flex;
    padding: 16px;
    background-color: #fff;
    height: 100%;
    width: 100%;
  }
  .topTagsNav {
    width: 100%;
  }
  :deep(.plat-tabs-nav) {
    margin-bottom: 0px;
  }
  :deep(.plat-tabs-content) {
    height: 100%;
  }
</style>
