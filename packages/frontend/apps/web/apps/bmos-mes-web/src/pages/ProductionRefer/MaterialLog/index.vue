<!-- 物料日志 -->
<template>
  <div class="main bg-white">
    <BMTable
      ref="pageRef"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      auto-height
      :autoHeightOffset="24"
      :pageSizeChangeToFirst="true"
      :scroll="{ x: 1144, y: 500 }"
      :formProps="formProps"
      :pagination="{
        pageSize: 20,
      }"
      :show-tool-bar="false"
      showSearchBorder
      @reset="reset"></BMTable>
    <InspectionDetailsModal
      ref="InspectionDetailsModalRef"
      :inspectionRowData="inspectionRowData"></InspectionDetailsModal>
  </div>
</template>

<script setup lang="tsx">
  import { DataRequestFn, BMTable, FormProps, RenderCallbackParams } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import {
    reqMaterialLogPage,
    getMaterialLogTreeApi,
    MaterialBatchListByMaterialId,
    getMaterialListApi,
  } from '@/services';
  import { loopSelectableTree } from '@bmos/utils';
  import dayjs, { Dayjs } from 'dayjs';
  import InspectionDetailsModal from '@/pages/ProductionRefer/BatchTraceability/components/InspectionDetailsModal.vue';

  //物料类型初始值
  const materValue = ref<number>(0);
  const pageRef = ref<any>(null);
  //物料信息Select
  const materialInformSelect = ref([]);
  //物料批号Select
  const materialInform = ref([]);
  //物料批号Select
  const storageMaterialValue = ref([]);
  // 表格配置
  const { columns, InspectionDetailsModalRef, inspectionRowData } = useTable();
  type RangeValue = [Dayjs, Dayjs];

  const dates = ref<any>([dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')]);
  const value = ref<RangeValue>();

  // 表单配置
  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: true,
    initialValues: {
      categoryType: materValue.value,
    },
    schemas: [
      {
        field: 'categoryType',
        component: 'Select',
        label: t('物料类型'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
            onChange: (value: number) => {
              materValue.value = value;
              getMaterialInform();
              materialInform.value = [];
              storageMaterialValue.value = [];
              formInstance.setFieldsValue({
                materialId: undefined,
                materialBatchId: undefined,
                storageMaterialId: undefined,
              });
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'TreeSelect',
        label: t('物料信息'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            treeData: materialInformSelect.value,
            fieldNames: {
              children: 'children',
              label: 'showName',
              value: 'id',
            },
            virtual: false,
            height: 200,
            onChange: async (value: any) => {
              try {
                const res = await MaterialBatchListByMaterialId({
                  materialId: value,
                });
                if (res.code === 0) materialInform.value = res.data;
                await formInstance.setFieldsValue({
                  materialBatchId: null,
                  storageMaterialId: null,
                });
                storageMaterialValue.value = [];
              } catch (error: any) {
                console.log(error);
              }
            },
          };
        },
      },
      {
        //物料批号
        field: 'materialBatchId',
        component: 'Select',
        label: t('物料批号'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: materialInform.value,
            showSearch: true,
            fieldNames: {
              label: 'materialBatchNo',
              value: 'id',
            },
            filterOption: (input: string, option: any) => {
              return option.materialBatchNo.toLowerCase().indexOf(input.toLowerCase()) >= 0;
            },
            onChange: async (value: string) => {
              try {
                const res = await getMaterialListApi({ batchNoId: value });
                if (res.code === 0) storageMaterialValue.value = res.data;
                await formInstance.setFieldsValue({
                  storageMaterialId: undefined,
                });
              } catch (error) {
                console.log(error);
              }
            },
          };
        },
      },
      {
        //物料件号
        field: 'storageMaterialId',
        component: 'Select',
        label: t('物料件号'),
        componentProps: () => {
          return {
            options: storageMaterialValue.value,
            showSearch: true,
            fieldNames: {
              label: 'materialNo',
              value: 'id',
            },
            filterOption: (input: string, option: any) => {
              return option.materialNo.toLowerCase().indexOf(input.toLowerCase()) >= 0;
            },
          };
        },
      },
      {
        //操作类型
        field: 'operationType',
        component: 'Select',
        label: t('操作类型'),
        componentProps: () => {
          return {
            options: [
              { label: t('入库'), value: 'INBOUND' },
              { label: t('出库'), value: 'OUTBOUND' },
              { label: t('盘点'), value: 'CHECK' },
              { label: t('预定'), value: 'RESERVE' },
              { label: t('取消预定'), value: 'CANCEL_RESERVE' },
              { label: t('称量'), value: 'WEIGH' },
              { label: t('新增'), value: 'ADD' },
              { label: t('投料'), value: 'CHARGE' },
              { label: t('回收'), value: 'RECYCLE' },
            ],
          };
        },
      },
      {
        //操作时间
        field: 'operationTime',
        component: 'RangePicker',
        label: t('操作时间'),
        // 默认查单个月
        defaultValue: [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')],
        componentProps: ({ formModel }: any) => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
            style: { width: '100%' },
            value: formModel.operationTime || value.value,
            disabledDate: (current: Dayjs) => {
              if (!dates.value || (dates.value as any).length === 0) {
                return false;
              }
              const tooLate = dates.value[0] && dayjs(current).diff(dates.value[0], 'days') > 30;
              const tooEarly = dates.value[0]
                ? dates.value[1] && dayjs(dates.value[1]).diff(current, 'days') > 30
                : dayjs(dates.value).startOf('month') > current;
              return tooEarly || tooLate;
            },
            onChange: (val: RangeValue) => {
              value.value = val;
            },
            onCalendarChange: (val: RangeValue) => {
              dates.value = val;
            },
          };
        },
      },
    ],
    fieldMapToTime: [['operationTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });
  //获取物料信息
  const getMaterialInform = async () => {
    try {
      const res = await getMaterialLogTreeApi({
        categoryType: materValue.value,
      });
      materialInformSelect.value = loopSelectableTree(res.data, 'categoryFlag', true);
    } catch (error: any) {
      console.log(error);
    }
  };
  // 搜索数据
  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    const data = {
      startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
      endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
      ...params,
    };
    const res = await reqMaterialLogPage(data);
    return res;
  };
  // 重置事件
  const reset = () => {
    dates.value = [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')];
  };
  onMounted(() => {
    getMaterialInform();
  });
</script>

<style scoped lang="less">
  .main {
    height: 100%;
    min-height: 100%;
    background-color: white;
    padding: 0 var(--bmos-padding-small);
    padding-top: 16px;
  }
</style>
