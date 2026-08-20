<template>
  <keep-alive>
    <proceduresPage
      v-if="currentView === proceduresPage"
      v-model:state="state"
      v-model:treeList="treeList"
      v-model:treeCutId="treeCutId"
      :detailsRow="detailsRow"
      @cutProceduresDetails="cutProceduresDetails"
      @back="back" />
  </keep-alive>
  <component
    :is="currentView"
    v-if="currentView === proceduresDetails"
    v-model:state="state"
    v-model:treeList="treeList"
    v-model:treeCutId="treeCutId"
    :detailsRow="detailsRow"
    @cutProceduresDetails="cutProceduresDetails"
    @back="back" />
</template>
<script lang="ts" setup>
  import { modalStatus } from './enum';
  import proceduresPage from './components/proceduresPage/index.vue';
  import proceduresDetails from './components/proceduresDetails/index.vue';
  import { Recordable } from '@bmos/components';
  //当前状态
  const state = ref<modalStatus>(modalStatus.Add);
  //当前树节点ID
  const treeCutId = ref<string>('');
  //设备分类树
  const treeList = ref<Recordable>();
  //当前页
  const currentView = shallowRef<any>(proceduresPage);
  //详情
  const detailsRow = ref<Recordable>();
  //切换页面
  const cutProceduresDetails = (params: any) => {
    switch (state.value) {
      case modalStatus.Add:
        detailsRow.value = {};
        break;
      case modalStatus.Edit:
        detailsRow.value = params;
        break;
      case modalStatus.View:
        detailsRow.value = params;
        break;
      case modalStatus.Copy:
        detailsRow.value = params;
        break;
    }
    currentView.value = proceduresDetails;
  };

  //返回
  const back = () => {
    currentView.value = proceduresPage;
  };
</script>
