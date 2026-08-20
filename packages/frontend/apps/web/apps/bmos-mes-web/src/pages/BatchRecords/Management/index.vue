<template>
  <BMPageComponent
    ref="pageRef"
    :columns="columns"
    :requests="requests"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'templateInfoId']"
    :search="[true, false]"
    :treeData="treeData"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :treeField="{
      field: {
        productId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          planId: 'id',
        },
      },
    ]">
    <template #tableHeaderTitle0="{ treeNode }">
      <Space :size="60">
        <span>
          {{ t('产品名称') }}: {{ treeNode.categoryFlag || isEmpty(treeNode.categoryFlag) ? '-' : treeNode.name }}
        </span>
        <span>
          {{ t('产品编码') }}: {{ treeNode.categoryFlag || isEmpty(treeNode.categoryFlag) ? '-' : treeNode.mergeCode }}
        </span>
        <span>
          {{ t('产品规格') }}:
          {{ treeNode.categoryFlag || isEmpty(treeNode.categoryFlag) ? '-' : treeNode.specification }}
        </span>
      </Space>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('批记录列表')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { useTables } from './hooks';
  import { Space } from 'ant-design-vue';
  import { isEmpty } from '@bmos/utils';

  const { requests, columns, treeData, pageRef, formFirstProps } = useTables();
</script>

<style scoped lang="less"></style>
