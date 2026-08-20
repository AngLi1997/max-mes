<template>
  <keep-alive>
    <DictList
      v-if="currentComponent === DictList"
      :type1="type1"
      :rowData="rowData"
      @addDict="addDict"
      @eidtOrLook="eidtOrLook"
      @back="back" />
  </keep-alive>
  <AddDict
    v-if="currentComponent === AddDict"
    :type1="type1"
    :rowData="rowData"
    @addDict="addDict"
    @eidtOrLook="eidtOrLook"
    @back="back" />
</template>

<script lang="ts" setup>
  import DictList from './components/DictList.vue';
  import AddDict from './components/AddDict/index.vue';
  import { ref } from 'vue';

  const currentComponent = shallowRef<any>(DictList);
  const type1 = ref();
  const rowData = ref();
  const addDict = (type: string) => {
    currentComponent.value = AddDict;
    type1.value = type;
  };

  // 编辑查看
  const eidtOrLook = (row: any, type: string) => {
    currentComponent.value = AddDict;
    type1.value = type;
    rowData.value = row;
  };

  const back = () => {
    currentComponent.value = DictList;
  };
</script>
