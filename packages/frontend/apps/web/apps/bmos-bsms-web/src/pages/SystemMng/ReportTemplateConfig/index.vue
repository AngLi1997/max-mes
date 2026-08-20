<!-- 报告模板配置 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[false]"
    :hideRightTree="true"
    :showAllAddIcon="false"
    :showAction="false"
    :showHeader="[true]"
    :showToolBars="[false]"
    :formProps="[formFirstProps]"
    :paginations="[paginationFirst]"
    :tableFields="[
      {
        default: { reportType: activeKey },
      },
    ]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableTopHeaderTitle0>
      <div style="width: 100%">
        <Tabs v-model:activeKey="activeKey" @change="changeType">
          <TabPane :key="1" :tab="t('检疫期核查报告')"></TabPane>
          <TabPane :key="2" :tab="t('不合格血浆核查报告')"></TabPane>
        </Tabs>
      </div>
    </template>
    <template #tableTopHeaderToolbar0>
      <Button type="primary" @click="openModal('create')">{{ t('新增') }}</Button>
    </template>
  </BMPageComponent>
  <AddDialog ref="addDialogRef" @submitSuccess="submitSuccess" />
  <Criteria ref="criteriaRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { Tabs, TabPane } from 'ant-design-vue';
  import { useTable } from './hooks/useTable';
  import { AddDialog, Criteria } from './components';
  import { getReportTemplatePage } from '@/services';

  defineOptions({
    name: 'ReportTemplateConfig',
  });

  // 新增编辑
  const addDialogRef = ref<any>();
  const openModal = (type: 'create' | 'edit', data?: any) => {
    addDialogRef.value?.openModal(type, { ...data, reportType: activeKey.value });
  };

  // 查看依据详情
  const criteriaRef = ref<any>();
  const showCriteria = (data: any) => {
    criteriaRef.value.showDrawer(data, activeKey.value);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(openModal, showCriteria);

  const activeKey = ref<any>(1);

  // 选项变化时重新获取数据
  const changeType = async (_val: any) => {
    await pageRef.value?.fetchData();
  };

  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    return await getReportTemplatePage(datas);
  };

  const submitSuccess = () => {
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
