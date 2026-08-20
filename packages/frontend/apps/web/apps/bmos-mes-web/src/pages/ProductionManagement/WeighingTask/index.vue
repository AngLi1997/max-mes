<!-- 称量任务 -->
<template>
  <keep-alive>
    <ManagementPage
      v-if="currentComponent === ManagementPage"
      :type="type1"
      :rowData="rowData"
      @taskPlanning="taskPlanning"
      @editOrLook="editOrLook"
      @back="back" />
  </keep-alive>
  <component
    :is="currentComponent"
    v-if="currentComponent === EditOrLookPage || currentComponent === TaskPlanningPage"
    :type="type1"
    :rowData="rowData"
    @taskPlanning="taskPlanning"
    @editOrLook="editOrLook"
    @back="back" />
</template>

<script lang="ts" setup>
  import ManagementPage from './components/ManagementPage/index.vue';
  import EditOrLookPage from './components/EditOrLookPage/index.vue';
  import TaskPlanningPage from './components/TaskPlanningPage/index.vue';
  import { ref } from 'vue';

  const currentComponent = shallowRef<any>(ManagementPage);
  const type1 = ref();
  const rowData = ref();
  // 任务规划按钮
  const taskPlanning = () => {
    currentComponent.value = TaskPlanningPage;
  };

  // 编辑查看按钮
  const editOrLook = (row: any, type: string) => {
    currentComponent.value = EditOrLookPage;
    type1.value = type;
    rowData.value = row;
  };

  const back = () => {
    currentComponent.value = ManagementPage;
  };
</script>
