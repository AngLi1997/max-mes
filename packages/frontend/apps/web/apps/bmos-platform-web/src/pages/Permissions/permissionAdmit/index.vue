<template>
  <div class="container">
    <div class="topTagsNav">
      <Tabs v-if="tabs.length > 0" style="height: 100%" :destroyInactiveTabPane="true">
        <TabPane v-for="tab in tabs" :key="tab.key" :tab="tab.title">
          <PermissionTab :activeKey="tab.key"></PermissionTab>
        </TabPane>
      </Tabs>
      <Empty v-else></Empty>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import PermissionTab from './permissionTab.vue';
  import { ref, onMounted } from 'vue';
  import { getTerminalType } from '../../../api/Permissions/authorization';
  import Empty from '../../../components/Empty/index.vue';
  import { Tabs, TabPane } from 'ant-design-vue';
  const tabs = ref<any[]>([]);

  const getTerminalTypeApi = async (params: any) => {
    try {
      const res: any = await getTerminalType(params);
      if (res.code === 0 && res.data) {
        tabs.value = res.data?.map((item: any) => {
          return {
            key: item.id,
            title: item.name,
          };
        });
      }
    } catch (error) {}
  };

  onMounted(() => {
    getTerminalTypeApi({ type: 1 });
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

  :deep(.bmos-search-tree) {
    height: 100%;
    width: 600px;
  }
  :deep(.plat-tabs-nav) {
    margin-bottom: 0px;
  }
  :deep(.plat-tabs-content) {
    height: 100%;
  }
</style>
