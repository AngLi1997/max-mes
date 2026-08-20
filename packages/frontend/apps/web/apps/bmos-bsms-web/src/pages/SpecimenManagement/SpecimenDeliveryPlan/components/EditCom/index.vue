<!-- 编辑标本出库计划 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('标本管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('编辑标本出库计划') }}</breadcrumb-item>
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
        <Button type="primary" style="margin-top: 12px" @click="submit">{{ t('保存') }}</Button>
      </Card>
      <Card style="height: calc(100% - 295px)">
        <DubRowTable
          ref="dubTableRef"
          :leftTitle="t('在库标本')"
          :rightTitle="t('已选择标本')"
          :leftTableProps="{
            ...leftTableProps,
            tableFields: [
              {
                default: {
                  qualityStatus: formRef?.formModel?.qualityStatus?.value,
                  warehouseId: formRef?.formModel?.warehouseId,
                },
              },
            ],
          }"
          :rightTableProps="{
            ...rightTableProps,
            tableFields: [
              {
                default: {
                  outPlanBatchNo,
                },
              },
            ],
          }">
          <template #lefttableHeaderTitle>
            <Button type="primary" :disabled="!leftSelectedAllRows?.flag" @click="addNos(leftSelectedAllRows)">
              {{ t('批量添加') }}
            </Button>
          </template>
          <template #righttableHeaderTitle>
            <div class="table-header">
              <Button style="margin-right: 8px" @click="openReturnModal('tray')">{{ t('按大托盘退回') }}</Button>
              <Button style="margin-right: 8px" @click="openReturnModal('batch')">{{ t('按批次退回') }}</Button>
              <Button :disabled="rightSelectedRows.length === 0" @click="returnNos">{{ t('批量撤回') }}</Button>
            </div>
          </template>
          <template #leftexpandColumnTitle>{{}}</template>
          <template #leftexpandedRowRender="{ record, instance }">
            <BMPageComponent
              :ref="el => setExpandRef(record.sortingPlanBatchNo, el)"
              :rowKeys="['orgSampleNo']"
              :search="[false]"
              :hideRightTree="true"
              :tableFields="[
                {
                  default: {
                    ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
                    sortingPlanBatchNo: record.sortingPlanBatchNo,
                    qualityStatus: formRef?.formModel?.qualityStatus?.value,
                    warehouseId: formRef?.formModel?.warehouseId,
                  },
                },
              ]"
              :isExtraParamsChangeQuerys="[false]"
              :rowSelections="expandedTableMap[record.sortingPlanBatchNo].rowSelections"
              :showHeader="[false]"
              :showToolBars="[false]"
              :requests="[getSampleDeliveryPlanEditDetailList as DataRequestFn]"
              :paginations="[paginationBig]"
              :columns="[expandedTableMap[record.sortingPlanBatchNo].columnsFirst]" />
          </template>
        </DubRowTable>
      </Card>
    </div>
  </div>
  <!-- 退回弹窗 -->
  <ReturnModal ref="returnModalRef" @submitSuccess="() => fetchDubData()" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import {
    getSampleDeliveryPlanEditDetailList,
    getSampleDeliveryPlanByBatchNo,
    batchInsertSampleDeliveryPlan,
    batchBackSampleDeliveryPlan,
    updateSampleDeliveryPlanInfo,
  } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { BMPageComponent, BMForm, DataRequestFn } from '@bmos/components';
  import { useForm, useDubTable } from './hooks';
  import Card from '@/components/Card/index.vue';
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { ReturnModal } from '../index';
  import { useRouter } from 'vue-router';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const router = useRouter();

  const route = useRoute();
  const outPlanBatchNo = computed(() => {
    return route.params.outPlanBatchNo as string;
  });

  const comRouter = computed(() => {
    return t(router.currentRoute.value.query.fromRouteId as string);
  });

  const back = () => {
    router.back();
  };

  const addNos = async (params: any) => {
    Modal.confirm({
      title: t('是否将这些数据加入计划?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            outPlanBatchNo: outPlanBatchNo.value,
            ...params,
            flag: undefined,
          };
          await batchInsertSampleDeliveryPlan(data);
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

  const { formProps, formRef, setFormModels } = useForm();
  const {
    dubTableRef,
    leftSelectedAllRows,
    rightSelectedRows,
    leftTableProps,
    rightTableProps,
    expandedTableMap,
    fetchDubData,
  } = useDubTable(addNos);

  //批量退回
  const returnNos = () => {
    Modal.confirm({
      title: t('是否将这些数据退回?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            outPlanBatchNo: outPlanBatchNo.value,
            orgSampleNoList: rightSelectedRows.value?.map((item: any) => item.orgSampleNo),
          };
          await batchBackSampleDeliveryPlan(data);

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

  // 退回弹窗
  const returnModalRef = ref();
  const openReturnModal = (type: 'batch' | 'tray') => {
    returnModalRef.value?.openModal(outPlanBatchNo.value, type);
  };

  // 保存基础信息
  const submit = async () => {
    try {
      await formRef.value?.validate();
      const data = formRef.value?.formModel;
      await updateSampleDeliveryPlanInfo({
        outPlanDate: data?.outPlanDate,
        remark: data?.remark,
        outPlanBatchNo: outPlanBatchNo.value,
      });

      message.success(t('保存成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandedTableMap[key].setRef(ref);
  };

  onMounted(async () => {
    try {
      // await nextTick();
      const { data } = await getSampleDeliveryPlanByBatchNo(outPlanBatchNo.value);
      setFormModels({
        ...data,
        outStorage: data?.warehouse?.name,
        warehouseId: data?.warehouse?.value,
        outType: data?.outboundType?.name,
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

  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
