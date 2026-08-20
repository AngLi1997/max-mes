<!-- 免疫类型选择 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('免疫类型选择')]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :expandedRowsChanges="[expandChange]"
    :paginations="[paginationBig]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.id, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              sampleOrgNo: record.sampleOrgNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :rowClassNames="[rowClassName]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getImmunityTypeSecondList as DataRequestFn]"
        :columns="[expandMap[record.id].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- <ImportModal ref="importRef" /> -->
</template>

<script setup lang="ts">
  // import ImportModal from './components/ImportModal/index.vue';
  import { getImmunityTypeList, getImmunityTypeSecondList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'ImmuneTypeSelection',
  });

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getImmunityTypeList(datas);

    const keys = res?.data?.list?.map((item: any) => item.id) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandMap[key].fetchData();
      }
    });
    return res;
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandMap[key].setRef(ref);
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable();

  const rowClassName = (record: any) => {
    return record?.maintainer?.value === 1 ? 'maintainer' : '';
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  :deep(.maintainer) {
    color: #59bf78;
  }
</style>
