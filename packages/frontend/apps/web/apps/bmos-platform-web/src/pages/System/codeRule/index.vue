<template>
  <keep-alive>
    <CodeRuleList
      v-if="currentComponent === CodeRuleList"
      :selectCodeRuleVersion="selectCodeRuleVersion"
      :selectCodeRule="selectCodeRule"
      :currentStatus="currentStatus"
      @addCodeRule="addCodeRule"
      @addVersion="addVersion"
      @viewVersion="viewVersion"
      @editVersion="editVersion" />
  </keep-alive>
  <AddCodeRule
    v-if="currentComponent === AddCodeRule"
    :selectCodeRuleVersion="selectCodeRuleVersion"
    :selectCodeRule="selectCodeRule"
    :currentStatus="currentStatus"
    @back="back"
    @addCodeRule="addCodeRule"
    @addVersion="addVersion"
    @viewVersion="viewVersion"
    @editVersion="editVersion" />
</template>

<script lang="ts" setup>
  import CodeRuleList from './components/CodeRuleList.vue';
  import AddCodeRule from './components/AddCodeRule/index.vue';
  import { AddRuleDataStatus } from './types';
  import { Recordable } from '@bmos/components';

  const currentComponent = shallowRef<any>(CodeRuleList);

  // 当前状态
  const currentStatus = ref<AddRuleDataStatus>(AddRuleDataStatus.ADD);

  // 选中的规则id
  const selectCodeRule = ref<Recordable>({});

  const addCodeRule = () => {
    currentComponent.value = AddCodeRule;
    currentStatus.value = AddRuleDataStatus.ADD;
  };

  const selectCodeRuleVersion = ref<Recordable>({});
  const addVersion = (selectRow: Recordable, selectCodeRuleRow: Recordable) => {
    currentComponent.value = AddCodeRule;
    selectCodeRuleVersion.value = selectRow;
    selectCodeRule.value = selectCodeRuleRow;
    currentStatus.value = AddRuleDataStatus.ADD_VERSION;
  };

  const viewVersion = (selectRow: Recordable) => {
    currentComponent.value = AddCodeRule;
    selectCodeRuleVersion.value = selectRow;
    currentStatus.value = AddRuleDataStatus.VIEW;
  };

  const editVersion = (selectRow: Recordable, selectCodeRuleRow: Recordable) => {
    currentComponent.value = AddCodeRule;
    selectCodeRuleVersion.value = selectRow;
    selectCodeRule.value = selectCodeRuleRow;
    currentStatus.value = AddRuleDataStatus.EDIT_VERSION;
  };

  const back = () => {
    currentComponent.value = CodeRuleList;
  };
</script>
