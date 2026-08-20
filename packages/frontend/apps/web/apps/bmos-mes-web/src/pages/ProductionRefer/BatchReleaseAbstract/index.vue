<!-- 批次摘要 -->
<template>
  <KeepAlive>
    <BMPageComponent
      v-if="!showView && !showSearch"
      ref="pageRef"
      :columns="columns"
      :requests="requests"
      :showAllAddIcon="false"
      :showAction="false"
      :rowKeys="['id', 'id']"
      :search="[true, false]"
      :treeData="treeData"
      :formProps="[formFirstProps]"
      :fieldNames="{
        title: 'showName',
        key: 'id',
      }"
      :treeField="{
        field: {
          id: 'id',
        },
      }"
      :tableFields="[
        {},
        {
          field: {
            datasetId: 'id',
          },
        },
      ]"
      @tree-select="treeSelect">
      <template #tableHeaderToolbar0>
        <Button type="primary" @click="() => handleAddDataset()">
          {{ t('新增摘要') }}
        </Button>
      </template>
      <template #tableHeaderTitle0>
        <BMTableTitle :title="t('摘要列表')"></BMTableTitle>
      </template>
    </BMPageComponent>
  </KeepAlive>
  <AddAbstract
    v-if="showView"
    ref="addAbstract"
    :treeData="treeData"
    :rowData="rowData"
    :type="type"
    :disabled="disabled"
    @close="showView = false" />
  <SearchPage v-if="showSearch" :rowData="rowData" @close="showSearch = false" />
</template>

<script setup lang="ts">
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';
  import { useTables } from './hooks';
  import AddAbstract from './component/addAbstract.vue';
  import SearchPage from './component/searchPage.vue';

  const {
    requests,
    columns,
    treeData,
    pageRef,
    formFirstProps,
    showView,
    showSearch,
    type,
    rowData,
    disabled,
    addAbstract,
    treeSelect,
  } = useTables();
  const handleAddDataset = () => {
    type.value = 'add';
    rowData.value = {};
    disabled.value = false;
    showView.value = true;
  };
</script>

<style scoped lang="less"></style>
