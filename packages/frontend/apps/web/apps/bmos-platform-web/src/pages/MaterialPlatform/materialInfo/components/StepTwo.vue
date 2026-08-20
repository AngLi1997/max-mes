<template>
  <div>
    <!-- <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      auto-height
      :autoHeightOffset="24"
      :scroll="{ x: 1144 }"
      :showRefresh="true"
      :formProps="formProps"
      :isSelect="true"
      :rowSelection="{
        selectedRowKeys: [2],
      }"
      :extraParams="{
        materialCategoryId: '',
      }"
      @change="handleTableChange"
      @handleClickRow="handleClickRow"
      showSearchBorder>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'name'">
          {{ record.name }}
          <a>[测试]</a>
        </template>
      </template>
    </BMTable> -->
    表格
  </div>
</template>

<script setup lang="tsx"></script>

<style lang="less" scoped></style>
