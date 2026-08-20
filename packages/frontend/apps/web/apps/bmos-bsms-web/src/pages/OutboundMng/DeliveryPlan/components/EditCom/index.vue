<!-- 编辑出库计划 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('出库管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('编辑出库计划') }}</breadcrumb-item>
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
      <Card style="height: calc(100% - 225px)">
        <DubRowTable
          ref="dubTableRef"
          :leftTitle="t('在库血浆')"
          :rightTitle="t('已选择血浆')"
          :leftTableProps="{
            ...leftTableProps,
            tableFields: [
              {
                default: {
                  warehouseId: formRef?.formModel?.warehouseId,
                  qualityStatus: formRef?.formModel?.qualityStatus?.value,
                  outboundType: formRef?.formModel?.outboundType?.value,
                },
              },
            ],
          }"
          :rightTableProps="{
            ...rightTableProps,
            tableFields: [
              {
                default: { batchNo: formRef?.formModel?.batchNo },
              },
            ],
          }">
          <template #lefttableHeaderTitle>
            <Button type="primary" :disabled="!leftSelectedAllRows?.flag" @click="addNos">{{ t('批量添加') }}</Button>
          </template>
          <template #righttableHeaderTitle="{ instance }">
            <div class="table-header">
              <Button type="primary" style="margin-right: 8px" @click="enterImport">{{ t('导入') }}</Button>
              <Button
                :loading="exportLoading"
                @click="exportFile(instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel))">
                {{ t('导出') }}
              </Button>
            </div>
          </template>
          <template #rightHeaderToolbar>
            <div class="table-header">
              <Button style="margin-right: 8px" @click="openReturnModal('tray')">{{ t('按大托盘退回') }}</Button>
              <Button style="margin-right: 8px" @click="openReturnModal('batch')">{{ t('按批次退回') }}</Button>
              <Button :disabled="rightSelectedRows?.length === 0" @click="returnNos">{{ t('批量撤回') }}</Button>
            </div>
          </template>
          <template #leftexpandColumnTitle>{{}}</template>
          <template #leftexpandedRowRender="{ record, instance }">
            <BMPageComponent
              :ref="el => setExpandRef(record.batchNo, el)"
              :rowKeys="['orgNo']"
              :search="[false]"
              :hideRightTree="true"
              :tableFields="[
                {
                  default: {
                    ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
                    batchNo: record.batchNo,
                    warehouseId: formRef?.formModel?.warehouseId,
                    qualityStatus: formRef?.formModel?.qualityStatus?.value,
                    outboundType: formRef?.formModel?.outboundType?.value,
                  },
                },
              ]"
              :isExtraParamsChangeQuerys="[false]"
              :paginations="[paginationBig]"
              :rowSelections="expandedTableMap[record.batchNo].rowSelections"
              :showHeader="[false]"
              :showToolBars="[false]"
              :requests="[getDeliveryPlanPlasmaInfoList as DataRequestFn]"
              :columns="[expandedTableMap[record.batchNo].columnsFirst]" />
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
    getDeliveryPlanById,
    updateDeliveryPlan,
    getDeliveryPlanPlasmaInfoList,
    batchAddOutboundPlasma,
    batchBackOutboundPlasma,
    exportDeliveryPlanSelectedList,
  } from '@/services';
  import { BMPageComponent, BMForm, DataRequestFn } from '@bmos/components';
  import { useForm, useDubTable } from './hooks';
  import Card from '@/components/Card/index.vue';
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { useRouter } from 'vue-router';
  import { ReturnModal } from '../index';
  import { paginationBig } from '@/utils/paginationConfig';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { fileStreamDownload } from '@bmos/utils';

  const router = useRouter();
  const route = useRoute();

  const comRouter = computed(() => {
    return t(route.query.fromRouteId as string);
  });

  const back = () => {
    router.back();
  };

  const enterImport = () => {
    router.push({ name: 'DeliveryPlanImportExcel', params: { id: route.params.id } });
  };

  const { formRef, formProps, setFormModels } = useForm();
  const {
    dubTableRef,
    fetchDubData,
    rightSelectedRows,
    leftSelectedAllRows,
    leftTableProps,
    rightTableProps,
    expandedTableMap,
  } = useDubTable();
  // const { pageRef, myselectedRows, rowSelections, columnsFirst } = useTable();

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandedTableMap[key].setRef(ref);
  };

  // 保存基础信息
  const submit = async () => {
    try {
      await formRef.value?.validate();
      const data = formRef.value?.formModel;
      await updateDeliveryPlan({
        deliveryPlasmaType: data?.deliveryPlasmaType,
        outPlanDate: data?.outPlanDate,
        remark: data?.remark,
        id: route.params.id,
      });

      message.success(t('保存成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 批量添加
  const addNos = () => {
    // dubTableRef.value?.addNos();
    Modal.confirm({
      title: t('是否将这些数据加入计划?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            batchNo: formRef.value?.formModel?.batchNo,
            ...leftSelectedAllRows.value,
            flag: undefined,
          };
          await batchAddOutboundPlasma(data);
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
            batchNo: formRef.value?.formModel?.batchNo,
            plasmaNos: rightSelectedRows.value?.map((item: any) => item.plasmaOrgNo),
          };
          await batchBackOutboundPlasma(data);

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
    returnModalRef.value?.openModal(formRef.value?.formModel?.batchNo, type);
  };

  onMounted(async () => {
    try {
      // await nextTick();
      // await getSortingCategoryOptions();
      const { data } = await getDeliveryPlanById(route.params.id);
      setFormModels({
        ...data,
        warehouseId: data?.warehouse?.value,
        warehouse: data?.warehouse?.name,
        outboundType: data?.type,
        type: data?.type?.name,
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });

  // 导出相关
  const exportLoading = ref(false);

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const exportFile = async (params: any) => {
    exportLoading.value = true;
    const res = await exportDeliveryPlanSelectedList({
      ...params,
      batchNo: formRef.value?.formModel?.batchNo,
    });
    let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
    // 文件名解码
    fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
    downloadFn(res.data, fileName);
    exportLoading.value = false;
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
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
