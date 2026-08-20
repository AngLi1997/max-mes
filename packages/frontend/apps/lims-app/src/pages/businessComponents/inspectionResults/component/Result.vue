<!-- 检验结果组件 -->
<template>
  <BMBasicPage
    :title="t('检验结果')"
    :default-padding="false"
    @left-click="toBack"
    @confirm="showMessageBox = true"
    @cancel="toBack"
  >
    <view class="content">
      <BMFormTitle>{{ t('检验信息') }}</BMFormTitle>
      <InfoTable style="margin: 0 9.38rpx 9.38rpx;" :details="inspectionInfo" :data="inspectionInfoData" />
      <BMFormTitle>{{ t('请验结果') }}</BMFormTitle>
      <InfoTable style="margin: 0 9.38rpx 9.38rpx;" :details="inspectionResult" :data="inspectionResultData" />
      <view class="table_box">
        <BMTable
          ref="tableUseRef"
          v-bind="tableUseProps"
        />
      </view>
    </view>
  </BMBasicPage>
  <BMMessageBox
    v-model="showMessageBox"
    :content="t('是否确认批次检验结果')"
    @confirm="confirm"
  />
</template>

<script lang="jsx" setup>
import { getInspectProgramResult } from '@/api';
import { BMBasicPage, BMFormTitle, BMMessageBox, BMTable } from '@/BMComponents';
import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';

const props = defineProps({
  resultsData: {
    type: Object,
    default: () => {},
  },
});
const emit = defineEmits(['cancel', 'submit']);
const allColor = {
  init: '#000',
  QUALIFIED: '#59BF78',
  UNQUALIFIED: '#FF4C26',
};
const showMessageBox = ref(false);
// 检验信息列表
const inspectionInfo = ref([
  {
    title: t('产品名称'),
    dataIndex: 'productName',
  },
  {
    title: t('产品编码'),
    dataIndex: 'productMergeCode',
  },
  {
    title: t('指令单编号'),
    dataIndex: 'planNo',
  },
  {
    title: t('生产批号'),
    dataIndex: 'batchNo',
  },
  {
    title: t('物料类型'),
    dataIndex: 'materialType',
  },
  {
    title: t('物料信息'),
    dataIndex: 'materialName',
  },
  {
    title: t('物料批号'),
    dataIndex: 'materialBatchNo',
  },
  {
    title: t('请验时间'),
    dataIndex: 'inspectTime',
  },
]);
const inspectionInfoData = ref({});
// 检验结果列表
const inspectionResult = ref([
  {
    title: t('请验单号'),
    dataIndex: 'inspectNo',
  },
]);
const inspectionResultData = ref({});
const tableUseRef = ref();

const tableUseProps = reactive({
  pagination: false,
  border: false,
  tableColProps: [
    {
      prop: 'inspectProgramNo',
      label: t('检项代码'),
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'inspectProgramName',
      label: t('检项名称'),
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'inspectResult',
      label: t('检项结果'),
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'inspectConclusion',
      label: t('检项结论'),
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return (
          <view>{row.inspectConclusion.label}</view>
        );
      },
    },
  ],
});
// 返回
const toBack = () => {
  emit('cancel');
};
const confirm = () => {
  emit('submit');
};
onMounted(async () => {
  const { data } = await getInspectProgramResult({ id: props.resultsData.id });
  inspectionInfoData.value = {
    ...data,
    materialType: data.materialType.name,
  };
  inspectionResult.value.push({
    title: t('汇总检验结果'),
    dataIndex: 'inspectResult',
    color: allColor[data.inspectResult?.value || 'init'],
  });
  inspectionResultData.value = {
    inspectNo: data.inspectNo,
    inspectResult: data.inspectResult?.label || '',
  };
  tableUseRef.value.tableData = data.inspectProgramResultVOList;
});
</script>

<style lang="scss">
  .content {
  :deep(.form-title) {
    margin: 0;
  }
  .table_box {
    padding: 0 9.38rpx 9.38rpx;
  }
}
</style>
