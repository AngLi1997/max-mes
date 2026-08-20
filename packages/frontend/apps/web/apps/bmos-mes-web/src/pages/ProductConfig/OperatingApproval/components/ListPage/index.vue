<template>
  <div class="process-approval-table">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :showToolBar="false"
      :scroll="{ x: 1380, y: 400 }"
      :formProps="
        {
          showAdvancedButton: false,
          actionColOptions: {
            span: 12,
          },
        }
      "
      :pagination="{
        pageSize: 20,
      }"></BMTable>
  </div>
</template>

<script lang="ts" setup>
  import { BMTable } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  const next: Function | undefined = inject('switchGo');
  const { tableInstance, columns, loadData } = useTable({ next });
</script>

<style lang="less" scoped>
  .process-approval-table {
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    .bmos-table {
      height: 100%;
    }
  }
</style>
