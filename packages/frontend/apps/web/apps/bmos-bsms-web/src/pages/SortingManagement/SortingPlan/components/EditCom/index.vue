<!-- 编辑分拣计划 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('出库管理') }}</breadcrumb-item>
          <breadcrumb-item @click="back">{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('编辑分拣计划') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <!-- <div class="header-btn"> -->
        <Button @click="back">{{ t('返回') }}</Button>
        <!-- </div> -->
      </Col>
    </Row>
    <div style="height: calc(100% - 48px); overflow: auto">
      <Card :title="t('计划信息')" type="item">
        <BMForm ref="formRef" v-bind="formProps"></BMForm>
      </Card>
      <Card style="height: calc(100% - 225px); overflow: auto; padding: 0">
        <DubRowTable
          ref="dubTableRef"
          :leftTitle="query.itemType === 1 ? t('库存血浆') : t('库存标本')"
          :rightTitle="t('计划明细')"
          :leftTableProps="{
            ...leftTableProps,
            tableFields: [
              {
                default: {
                  planBatchNo: query.planBatchNo,
                },
              },
            ],
          }"
          :rightTableProps="{
            ...rightTableProps,
            tableFields: [
              {
                default: {
                  planBatchNo: query.planBatchNo,
                },
              },
            ],
          }">
          <template #lefttableHeaderTitle>
            <Button type="primary" :disabled="!leftSelectedAllRows?.flag" @click="addNos">{{ t('批量添加') }}</Button>
          </template>
          <template #rightHeaderToolbar>
            <Button :disabled="rightSelectedRows.length === 0" @click="returnNos">{{ t('批量撤回') }}</Button>
          </template>
          <template #leftexpandColumnTitle>{{}}</template>
          <template #leftexpandedRowRender="{ record, instance }">
            <!-- <div></div> -->
            <BMPageComponent
              :ref="el => setExpandRef(record.batchNo, el)"
              :rowKeys="['itemOrgNo']"
              :search="[false]"
              :hideRightTree="true"
              :tableFields="[
                {
                  default: {
                    ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
                    planBatchNo: query.planBatchNo,
                    batchNo: record.batchNo,
                  },
                },
              ]"
              :isExtraParamsChangeQuerys="[false]"
              :paginations="[paginationBig]"
              :rowSelections="expandedTableMap[record.batchNo].rowSelections"
              :showHeader="[false]"
              :showToolBars="[false]"
              :bordereds="[false]"
              :requests="[loadData as DataRequestFn]"
              :columns="[expandedTableMap[record.batchNo].columnsFirst]" />
          </template>
        </DubRowTable>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import {
    getSortingPlanDetail,
    getSortingPlanSelectableDetailList,
    getSampleSortingPlanSelectableDetailList,
    sortingPlanBatchAdd,
    sortingPlanBatchBack,
  } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { BMPageComponent, BMForm, DataRequestFn } from '@bmos/components';
  import { useForm, useDubTable } from './hooks';
  import Card from '@/components/Card/index.vue';
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { useRouter } from 'vue-router';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const router = useRouter();
  const route = useRoute();

  const query = computed(() => {
    return {
      planBatchNo: route.query.planBatchNo as string,
      itemType: parseInt(route.query.itemType as string) as 1 | 2,
    };
  });

  // 二级列表接口
  const loadData = (params: any) => {
    const datas = {
      ...params,
    };
    if (query.value.itemType === 1) {
      return getSortingPlanSelectableDetailList(datas);
    } else {
      return getSampleSortingPlanSelectableDetailList(datas);
    }
  };

  const comRouter = computed(() => {
    return t(route.query.fromRouteId as string);
  });

  const back = () => {
    router.back();
  };

  const { formProps, formRef, setFormModels } = useForm();

  const {
    dubTableRef,
    rightSelectedRows,
    leftSelectedAllRows,
    leftTableProps,
    rightTableProps,
    expandedTableMap,
    fetchDubData,
  } = useDubTable(query.value.planBatchNo, query.value.itemType);

  // 批量添加
  const addNos = () => {
    Modal.confirm({
      title: t('是否将这些数据加入计划?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            planBatchNo: formRef.value?.formModel?.batchNo,
            ...leftSelectedAllRows.value,
            flag: undefined,
          };
          await sortingPlanBatchAdd(data);
          message.success(t('操作成功'));
          fetchDubData();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  //批量退回
  const returnNos = () => {
    Modal.confirm({
      title: t('是否将这些数据退回?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            planBatchNo: formRef.value?.formModel?.batchNo,
            itemOrgNoList: rightSelectedRows.value?.map((item: any) => item.itemOrgNo),
          };
          await sortingPlanBatchBack(data);

          message.success(t('操作成功'));
          fetchDubData();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandedTableMap[key].setRef(ref);
  };

  onMounted(async () => {
    try {
      const { data } = await getSortingPlanDetail(query.value.planBatchNo);
      setFormModels({
        ...data,
        warehouseName: data?.warehouse?.name,
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style lang="less" scoped>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    .crumb {
      line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }
</style>
