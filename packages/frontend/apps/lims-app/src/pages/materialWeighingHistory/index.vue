<template>
  <BMLayout>
    <BMBasicPage :title="t('物料称量历史')" :show-buttons="false" @left-click="toBack">
      <template #titleRight>
        <view class="action">
          <BMFilter
            v-model="filterData"
            :form-props="filterFormProps"
            @confirm="filterConfirmOrReset"
            @reset="filterConfirmOrReset"
          />
          <BMFilter
            v-model="sortData"
            :title="t('排序')"
            icon="paixu"
            :form-props="sortFormProps"
            @confirm="filterConfirmOrReset"
            @reset="filterConfirmOrReset"
          />
        </view>
      </template>
      <BMTable
        ref="tableRef"
        :extra-params="extraParams"
        v-bind="tableProps"
      />
    </BMBasicPage>
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" />
  </BMLayout>
</template>

<script setup lang="jsx">
import { reqFreeWeighQueryHistory, reqPrintStorageMaterialTagApi } from '@/api';
import { BMBasicPage, BMFilter, BMLayout, BMTable } from '@/BMComponents/index.js';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();
const toBack = () => {
  uni.navigateBack();
};
const bmosPrinterInstance = ref();
const tableRef = ref();
const filterData = ref({});
const sortData = ref({});
const extraParams = ref({});
// 筛选表单配置
const filterFormProps = reactive({
  schemas: [
    {
      field: 'storageMaterialBatchNo',
      component: 'Input',
      label: t('物料批号'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'storageMaterialNo',
      component: 'Input',
      label: t('物料件号'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'weighDateStart',
      component: 'BMFormDatePicker',
      label: t('称量开始日期'),
      colProps: {
        span: 24,
      },
      componentProps: ({ formInstance, formModel }) => {
        return {
          formatDate: 'yyyy-MM-dd',
          title: t('称量开始日期'),
          onChange: (val) => {
            formModel.weighDateStart = val;
            formInstance.validate(['weighDateEnd']);
          },
        };
      },
    },
    {
      field: 'weighDateEnd',
      component: 'BMFormDatePicker',
      label: t('称量结束日期'),
      colProps: {
        span: 24,
      },
      componentProps: () => {
        return {
          formatDate: 'yyyy-MM-dd',
          title: t('称量结束日期'),
        };
      },
      dynamicRules: ({ formModel }) => {
        return [
          {
            validator: async (value) => {
              if (value && formModel.weighDateStart) {
                if (value < formModel.weighDateStart) {
                  return Promise.reject(t('称量结束日期不能小于称量开始日期'));
                }
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
  ],
});
  // 排序表单配置
const sortFormProps = reactive({
  schemas: [
    {
      field: 'orderSql',
      component: 'BMFormRadio',
      label: t('称量时间'),
      colProps: {
        span: 24,
      },
      componentProps: {
        options: [
          {
            label: t('顺序排列'),
            value: 'weighTime asc',
          },
          {
            label: t('逆序排列'),
            value: 'weighTime desc',
          },
        ],
      },
    },
  ],
});

const filterConfirmOrReset = () => {
  extraParams.value = {
    ...filterData.value,
    ...sortData.value,
  };
};

const tableProps = {
  pagination: {
    pageSize: 20,
    showJumper: true,
    hideIfOnePage: false,
  },
  showIndex: true,
  showNoData: true,
  noDataShowTable: false,
  noDataText: t('无称量历史'),
  dataRequest: async (params) => {
    return await reqFreeWeighQueryHistory({
      ...params,
      ...(params?.weighDateStart && {
        weighDateStart: format(params.weighDateStart, 'yyyy-MM-dd'),
      }),
      ...(params?.weighDateEnd && {
        weighDateEnd: format(params.weighDateEnd, 'yyyy-MM-dd'),
      }),
    });
  },
  tableColProps: [
    {
      prop: 'INDEX',
      label: t('序号'),
      width: 100,
    },
    {
      prop: 'weighTime',
      label: t('称量时间'),
      width: 150,
    },
    {
      prop: 'mergeCode',
      label: t('物料编码'),
      width: 150,
    },
    {
      prop: 'materialName',
      label: t('物料名称'),
      width: 150,
    },
    {
      prop: 'storageMaterialBatchNo',
      label: t('物料批号'),
      width: 150,
    },
    {
      prop: 'storageMaterialNo',
      label: t('物料件号'),
      width: 120,
    },
    {
      prop: 'netWeight',
      label: t('净重'),
      width: 100,
    },
    {
      prop: 'tareWeight',
      label: t('皮重'),
      width: 100,
    },
    {
      prop: 'grossWeight',
      label: t('毛重'),
      width: 100,
    },
    {
      prop: 'unit',
      label: t('单位'),
      width: 100,
    },
    {
      prop: 'weigherName',
      label: t('称量人'),
      width: 120,
    },
    {
      prop: 'reCheckerName',
      label: t('复核人'),
      width: 120,
    },
    {
      prop: 'containerName',
      label: t('容器'),
      width: 160,
    },
    {
      prop: 'positionName',
      label: t('货位'),
      width: 160,
    },
    {
      prop: 'ACTION',
      label: t('标签'),
      width: 100,
      fixed: 'right',
      actions: ({ row }) => {
        return [
          {
            label: t('打印'),
            onClick: async () => {
              const device = bmosPrinterInstance.value.print();
              if (device) {
                try {
                  await reqPrintStorageMaterialTagApi({
                    deviceId: device.id,
                    sceneId: row?.categoryType?.value === 0 ? 121001013 : 121002017,
                    body: {
                      no: row.storageMaterialNo,
                    },
                  });
                }
                catch (error) {
                  error.message && showNotify({
                    type: 'danger',
                    message: error.message,
                  });
                }
              }
            },
          },
        ];
      },
    },
  ],
};
</script>

<style lang="scss" scoped>
:deep(.action) {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 11.72rpx;
}
:deep(.uni-table) {
  width: 1490.04rpx;
}
:deep(.bm-table-show-border) {
  border: none;
}
</style>
