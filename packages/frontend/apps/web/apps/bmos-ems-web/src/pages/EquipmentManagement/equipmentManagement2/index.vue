<template>
  <keep-alive>
    <ManagementPage
      v-if="currentView === ManagementPage"
      :treeList="treeList"
      :treeDataId="treeDataId"
      :equipmentTagData="equipmentTagData"
      :detailsRow="detailsRow"
      :state="state"
      :rowData="rowData"
      @addManagementPage="addManagementPage"
      @matchPoint="matchPoint"
      @treeData="treeDataFile"
      @selectTree="selectTree"
      @equipmentTag="equipmentTag"
      @back="back" />
  </keep-alive>
  <component
    :is="currentView"
    v-if="currentView === AddManagementPage || currentView === MatchCollectionPoints"
    :treeList="treeList"
    :treeDataId="treeDataId"
    :equipmentTagData="equipmentTagData"
    :detailsRow="detailsRow"
    :state="state"
    :rowData="rowData"
    @addManagementPage="addManagementPage"
    @matchPoint="matchPoint"
    @treeData="treeDataFile"
    @selectTree="selectTree"
    @equipmentTag="equipmentTag"
    @back="back" />
</template>

<script lang="ts" setup>
  import { modalStatus } from './enum';
  import ManagementPage from './components/managementPage/index.vue';
  import AddManagementPage from './components/addManagement/index.vue';
  import MatchCollectionPoints from './components/matchCollectionPoints/index.vue'; //匹配采集点页面

  import { Recordable } from '@bmos/components';
  const currentView = shallowRef<any>(ManagementPage);
  //当前状态
  const state = ref<modalStatus>(modalStatus.Add);
  //设备分类树
  const treeList = ref<Recordable>();
  //当前选择的树
  const treeDataId = ref<string>();
  //设备标签
  const equipmentTagData = ref<Object[]>([]);
  //树赋值
  const treeDataFile = (params?: Recordable) => {
    treeList.value = params;
  };
  //选择树
  const selectTree = (params?: string) => {
    treeDataId.value = params;
  };
  //设备标签列表
  const equipmentTag = (params: Object[]) => {
    equipmentTagData.value = params;
  };
  //详情
  const detailsRow = ref<Recordable>();
  // 当行数据
  const rowData = ref<any>();
  //新增
  const addManagementPage = (status: modalStatus, params: any) => {
    switch (status) {
      case modalStatus.Add:
        state.value = status;
        detailsRow.value = {};
        break;
      case modalStatus.Edit:
        state.value = status;
        detailsRow.value = params;
        break;
      case modalStatus.View:
        state.value = status;
        detailsRow.value = params;
        break;
    }
    currentView.value = AddManagementPage;
  };
  // 匹配采集点
  const matchPoint = (row: any) => {
    rowData.value = row;
    currentView.value = MatchCollectionPoints;
  };
  //返回
  const back = () => {
    currentView.value = ManagementPage;
  };
</script>
