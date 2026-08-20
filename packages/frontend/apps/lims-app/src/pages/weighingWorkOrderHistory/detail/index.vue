<template>
  <BMLayout>
    <BMBasicPage
      :title="t('称量详情')"
      :show-buttons="false"
      :background-color="weighingResultsList.recordVOList.length !== 0 || weighingResultsList.oddmentRecordVOList.length !== 0 ? 'var(--bmos-color-white)' : 'var(--bmos-color-bg)'"
      @left-click="leftClick"
    >
      <view class="list-content">
        <BMInfoDisplay
          :basic-items="basicItems"
          :info-data="weighingResultsList"
          background="#F7F8FA"
          :is-show-title="false"
          style="margin-bottom:11.72rpx"
        />
        <div v-if="weighingResultsList.recordVOList.length !== 0 || weighingResultsList.oddmentRecordVOList.length !== 0" class="table-content">
          <wd-segmented
            v-model:value="currentSegmented"
            style="margin-bottom:11.72rpx"
            :options="[
              {
                value: t('物料称量'),
              },
              {
                value: t('余料称量'),
              },
            ]"
            @change="weighingChange"
          />
          <BMTable ref="tableRef" v-bind="tableProps" />
        </div>
        <BMNoData v-else type="emptyData" :text="t('暂无称量历史')" />
      </view>
    </BMBasicPage>
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" />
  </BMLayout>
</template>

<script setup lang="jsx">
import { queryWeighCenterExecuteTicketWeighRecords, reqPrintStorageMaterialTagApi } from '@/api';
import { BMBasicPage, BMInfoDisplay, BMLayout, BMNoData, BMTable } from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { computed, reactive, ref } from 'vue';
import { useNotify, useToast } from 'wot-design-uni';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
});
const { showNotify } = useNotify();
const toast = useToast();
const basicItems = ref([
  {
    label: t('工单编号'),
    field: 'ticketNo',
  },
  {
    label: t('称量中心'),
    field: 'center',
  },
  {
    label: t('完成时间'),
    field: 'completeTime',
  },
  {
    label: t('下发时间'),
    field: 'sendTime',
  },
]);

const bmosPrinterInstance = ref();

const tableRef = ref();
const weighingResultsList = ref({
  recordVOList: [], // 物料称量列表
  oddmentRecordVOList: [], // 余料称量列表
});// 存总数据

const tableColProps = computed(() => {
  if (currentSegmented.value === t('物料称量')) {
    return [
      {
        prop: 'INDEX',
        label: t('序号'),
        width: 80,
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 250,
      },
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 180,
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 220,
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 220,
      },
      {
        prop: 'netWeight',
        label: t('净重'),
        width: 145,
      },
      {
        prop: 'tareWeight',
        label: t('皮重'),
        width: 120,
      },
      {
        prop: 'grossWeight',
        label: t('毛重'),
        width: 145,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'productMaterialName',
        label: t('产品名称'),
        width: 220,
      },
      {
        prop: 'productMaterialMergeCode',
        label: t('产品编码'),
        width: 280,
      },
      {
        prop: 'batchNo',
        label: t('生产批号'),
        width: 280,
      },
      {
        prop: 'weigherLoginName',
        label: t('称量人'),
        width: 220,
        customRender: ({ row }) => {
          return (
            <span>
              {row.weighUserName}
              -
              {row.weighUserLoginName}
            </span>
          );
        },
      },
      {
        prop: 'reCheckerName',
        label: t('复核人'),
        width: 220,
        customRender: ({ row }) => {
          return (
            <span>
              {row.signUserName}
              -
              {row.signUserLoginName}
            </span>
          );
        },
      },
      {
        prop: 'deviceName',
        label: t('容器'),
        width: 220,
      },
      {
        prop: 'storageName',
        label: t('货位'),
        width: 220,
      },
      {
        prop: 'weighTime',
        label: t('称量时间'),
        width: 420,
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
                    let sceneId = row?.categoryInfoType?.value === 0 ? 121001016 : 121002020;
                    if (currentSegmented.value === t('余料称量')) {
                      sceneId = row?.categoryInfoType?.value === 0 ? 121001019 : 121002023;
                    }
                    await reqPrintStorageMaterialTagApi({
                      deviceId: device.id,
                      sceneId,
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
    ];
  }
  else {
    return [
      {
        prop: 'INDEX',
        label: t('序号'),
        width: 80,
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 250,
      },
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 180,
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 220,
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 220,
      },
      {
        prop: 'netWeight',
        label: t('净重'),
        width: 145,
      },
      {
        prop: 'tareWeight',
        label: t('皮重'),
        width: 120,
      },
      {
        prop: 'grossWeight',
        label: t('毛重'),
        width: 145,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'weigherLoginName',
        label: t('称量人'),
        width: 220,
        customRender: ({ row }) => {
          return (
            <span>
              {row.weighUserName}
              -
              {row.weighUserLoginName}
            </span>
          );
        },
      },
      {
        prop: 'reCheckerName',
        label: t('复核人'),
        width: 220,
        customRender: ({ row }) => {
          return (
            <span>
              {row.signUserName}
              -
              {row.signUserLoginName}
            </span>
          );
        },
      },
      {
        prop: 'deviceName',
        label: t('容器'),
        width: 220,
      },
      {
        prop: 'storageName',
        label: t('货位'),
        width: 220,
      },
      {
        prop: 'weighTime',
        label: t('称量时间'),
        width: 420,
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
                    let sceneId = row?.categoryInfoType?.value === 0 ? 121001016 : 121002020;
                    if (currentSegmented.value === t('余料称量')) {
                      sceneId = row?.categoryInfoType?.value === 0 ? 121001019 : 121002023;
                    }
                    await reqPrintStorageMaterialTagApi({
                      deviceId: device.id,
                      sceneId,
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
    ];
  }
});

const tableProps = reactive({
  pagination: false,
  data: [
  ],
  showIndex: true,
  showNoData: true,
  noDataShowTable: false,
  noDataText: t('无称量历史'),
  tableColProps: [
    {
      prop: 'INDEX',
      label: t('序号'),
      width: 80,
    },
    {
      prop: 'materialMergeCode',
      label: t('物料编码'),
      width: 250,
    },
    {
      prop: 'materialName',
      label: t('物料名称'),
      width: 180,
    },
    {
      prop: 'storageMaterialBatchNo',
      label: t('物料批号'),
      width: 220,
    },
    {
      prop: 'storageMaterialNo',
      label: t('物料件号'),
      width: 220,
    },
    {
      prop: 'netWeight',
      label: t('净重'),
      width: 145,
    },
    {
      prop: 'tareWeight',
      label: t('皮重'),
      width: 120,
    },
    {
      prop: 'grossWeight',
      label: t('毛重'),
      width: 145,
    },
    {
      prop: 'unitName',
      label: t('单位'),
      width: 90,
    },
    {
      prop: 'productMaterialName',
      label: t('产品名称'),
      width: 220,
    },
    {
      prop: 'productMaterialMergeCode',
      label: t('产品编码'),
      width: 280,
    },
    {
      prop: 'batchNo',
      label: t('生产批号'),
      width: 280,
    },
    {
      prop: 'weigherLoginName',
      label: t('称量人'),
      width: 220,
      customRender: ({ row }) => {
        return (
          <span>
            {row.weighUserName}
            -
            {row.weighUserLoginName}
          </span>
        );
      },
    },
    {
      prop: 'reCheckerName',
      label: t('复核人'),
      width: 220,
      customRender: ({ row }) => {
        return (
          <span>
            {row.signUserName}
            -
            {row.signUserLoginName}
          </span>
        );
      },
    },
    {
      prop: 'deviceName',
      label: t('容器'),
      width: 220,
    },
    {
      prop: 'storageName',
      label: t('货位'),
      width: 220,
    },
    {
      prop: 'weighTime',
      label: t('称量时间'),
      width: 420,
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
                  let sceneId = row?.categoryInfoType?.value === 0 ? 121001016 : 121002020;
                  if (currentSegmented.value === t('余料称量')) {
                    sceneId = row?.categoryInfoType?.value === 0 ? 121001019 : 121002023;
                  }
                  await reqPrintStorageMaterialTagApi({
                    deviceId: device.id,
                    sceneId,
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
});
const leftClick = () => {
  uni.navigateBack();
};
const currentSegmented = ref(t('物料称量'));
// 切换称量
const weighingChange = (data) => {
  switch (data.value) {
    case t('物料称量'):
      tableProps.data = weighingResultsList.value?.recordVOList;
      break;
    case t('余料称量'):
      tableProps.data = weighingResultsList.value?.oddmentRecordVOList;
      break;

    default:
      break;
  }

  tableProps.tableColProps = tableColProps.value;
};
  // 根据任务id查询称量结果列表
const getWeighingResultsList = async () => {
  try {
    const { data } = await queryWeighCenterExecuteTicketWeighRecords(props?.id);
    weighingResultsList.value = {
      ...data,
      center: `${data?.centreCode} - ${data?.centreName}`,
    };
    // 表格默认展示物料称量数据
    tableProps.data = data?.recordVOList;
  }
  catch (error) {
    error.message && toast.error(error.message);
  }
};

onShow(() => {
  getWeighingResultsList();
});
</script>

<style lang="scss" scoped>
:deep(.bm-table) {
  overflow: hidden;
  height: calc(100% - 31.64rpx - 11.72rpx);
}
:deep(.bm-table-show-border) {
  border: none;
}
.list-content {
  height: 100%;
  overflow: hidden;
  .table-content {
    height: calc(100% - 65.63rpx - 11.72rpx);
  }
}
:deep(.uni-table) {
  min-width: 2000rpx !important;
}
</style>
