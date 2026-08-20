<template>
  <BMBasicPage
    :title="t('称量详情')"
    :show-buttons="false"
    :background-color="weighingResultsList.mainList.length !== 0 || weighingResultsList.oddList.length !== 0 ? 'var(--bmos-color-white)' : 'var(--bmos-color-bg)'"
    @left-click="leftClick"
  >
    <view class="list-content">
      <div v-if="weighingResultsList.mainList.length !== 0 || weighingResultsList.oddList.length !== 0">
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
  <!-- 筛选 -->
</template>

<script setup lang="jsx">
import { reqWeighCenterExecuteQueryRecordResultByTaskId } from '@/api';
import { BMBasicPage, BMNoData, BMTable } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';
import { useToast } from 'wot-design-uni';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
});
const toast = useToast();
const tableRef = ref();
const weighingResultsList = ref({
  mainList: [], // 物料称量列表
  oddList: [], // 余料称量列表
});// 存总数据
const tableProps = reactive({
  pagination: false,
  data: [
  ],
  border: true,
  tableColProps: [
    {
      prop: 'INDEX',
      label: t('序号'),
      width: 80,
    },
    {
      prop: 'materialMergeCode',
      label: t('物料编码'),
      width: 210,
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
      prop: 'unit',
      label: t('单位'),
      width: 90,
    },
    {
      prop: 'productName',
      label: t('产品名称'),
      width: 220,
    },
    {
      prop: 'productMergeCode',
      label: t('产品编码'),
      width: 200,
    },
    {
      prop: 'processName',
      label: t('工艺名称'),
      width: 300,
    },
    {
      prop: 'batchNo',
      label: t('生产批号'),
      width: 220,
    },
    {
      prop: 'weigherLoginName',
      label: t('称量人'),
      width: 220,
      customRender: ({ row }) => {
        return (
          <span>
            {row.weigherName}
            -
            {row.weigherLoginName}
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
            {row.reCheckerName}
            -
            {row.reCheckerLoginName}
          </span>
        );
      },
    },
    {
      prop: 'containerName',
      label: t('容器'),
      width: 220,
    },
    {
      prop: 'materialPositionName',
      label: t('货位'),
      width: 220,
    },
    {
      prop: 'weighTime',
      label: t('称量时间'),
      width: 270,
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
      tableProps.data = weighingResultsList.value?.mainList;
      break;
    case t('余料称量'):
      tableProps.data = weighingResultsList.value?.oddList;
      break;

    default:
      break;
  }
};
  // 根据任务id查询称量结果列表
const getWeighingResultsList = async () => {
  try {
    const res = await reqWeighCenterExecuteQueryRecordResultByTaskId({ taskId: props?.id });
    weighingResultsList.value = res.data || {};
    // 表格默认展示物料称量数据
    tableProps.data = res.data?.mainList;
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
.list-content {
  height: 100%;
  :deep(.uni-table) {
    width: 2104.69rpx;
  }
}
</style>
