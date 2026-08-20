<template>
  <keep-alive>
    <TabPage
      v-if="currentView === TabPage"
      :keyTreeData="keyTreeData"
      :clickOnRow="clickOnRow"
      :treeDataId="treeDataId"
      :state="state"
      @addTagPage="addTagPage"
      @back="back"
      @treeData="treeDataFile"
      @selectTree="selectTree"
      @editTagPage="editTagPage"
      @viewTagPage="viewTagPage" />
  </keep-alive>
  <TabAddPage
    v-if="currentView === TabAddPage"
    :keyTreeData="keyTreeData"
    :clickOnRow="clickOnRow"
    :treeDataId="treeDataId"
    :state="state"
    @addTagPage="addTagPage"
    @back="back"
    @treeData="treeDataFile"
    @selectTree="selectTree"
    @editTagPage="editTagPage"
    @viewTagPage="viewTagPage" />
</template>
<script lang="ts" setup>
  import TabPage from './components/tagPage/index.vue';
  import TabAddPage from './components/tagAddPage/index.vue';
  import { Recordable } from '@bmos/components';
  import { modalStatus } from './types';
  const currentView = shallowRef<any>(TabPage);
  //树
  const keyTreeData = ref<Recordable>();
  //当前选择的树
  const treeDataId = ref<string>();
  //当前状态
  const state = ref<modalStatus>(modalStatus.Add);
  //点击行数据
  const clickOnRow = ref<Recordable>();
  //树赋值
  const treeDataFile = (params?: Recordable) => {
    keyTreeData.value = params;
  };
  //选择树
  const selectTree = (params?: string) => {
    console.log(params);
    treeDataId.value = params;
  };
  //新增
  const addTagPage = () => {
    currentView.value = TabAddPage;
    state.value = modalStatus.Add;
  };
  //编辑
  const editTagPage = (params: Recordable) => {
    currentView.value = TabAddPage;
    clickOnRow.value = params;
    state.value = modalStatus.Edit;
  };
  //查看
  const viewTagPage = (params: Recordable) => {
    currentView.value = TabAddPage;
    clickOnRow.value = params;
    state.value = modalStatus.View;
  };
  const back = () => {
    currentView.value = TabPage;
  };
</script>
