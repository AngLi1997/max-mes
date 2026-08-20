<!-- 检验详情组件 -->
<template>
  <BMBasicPage
    :title="t('请验详情')"
    :default-padding="false"
    :show-buttons="false"
    @left-click="toBack"
  >
    <view class="content">
      <BMFormTitle>{{ t('生产信息') }}</BMFormTitle>
      <InfoTable style="margin: 0 9.38rpx 9.38rpx;" :details="inspectionInfo" :data="inspectionInfoData" />
      <BMFormTitle>{{ t('物料信息') }}</BMFormTitle>
      <InfoTable style="margin: 0 9.38rpx 9.38rpx;" :details="materialInfo" :data="materialInfoData" />
      <BMFormTitle>{{ t('请验单信息') }}</BMFormTitle>
      <InfoTable style="margin: 0 9.38rpx 9.38rpx;" :details="verifyInfo" :data="verifyInfoData" />
    </view>
  </BMBasicPage>
</template>

<script setup>
import { getInspectInfo, getMesUnitListDownExtendBound, reqProductMaterialDetail } from '@/api';
import { BMBasicPage, BMFormTitle } from '@/BMComponents';
import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, ref } from 'vue';

const props = defineProps({
  resultsData: {
    type: Object,
    default: () => {},
  },
});
const emit = defineEmits(['cancel']);
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
    title: t('批量'),
    dataIndex: 'batchQuantity',
  },
  {
    title: t('单位'),
    dataIndex: 'unitName',
  },
]);
const inspectionInfoData = ref({});
// 物料信息
const materialInfo = ref([
  {
    title: t('物料类型'),
    dataIndex: 'materialType',
  },
  {
    title: t('物料信息'),
    dataIndex: 'materialName',
  },
]);
const materialInfoData = ref({});
// 请验单信息
const verifyInfo = ref([]);
const verifyInfoData = ref({});
const getPleaseVerifyInfoSchema = async (dataList) => {
  dataList.forEach(async (item) => {
    if (item.code === 'sampleQuantityUnit') {
      const { materialId } = inspectionInfoData.value;
      const { data } = await getMesUnitListDownExtendBound({
        materialId,
      });
      const { data: material } = await reqProductMaterialDetail(materialId);
      const unitList = data.map(util => ({
        label: util.extendUnitName,
        id: util.id,
        expression: util.expression,
      }));
      unitList.unshift({
        label: material.unitName,
        id: material.unitId,
        isUnit: true,
        expression: t('标准单位'),
      });
      const sampleQuantityUnit = unitList.find(unit => unit.id === item.value)?.label;
      verifyInfoData.value[item.code] = sampleQuantityUnit;
    }
    else {
      verifyInfoData.value[item.code] = item.value;
    }
    verifyInfo.value.push({
      title: item.showName,
      dataIndex: item.code,
    });
  });
};
// 返回
const toBack = () => {
  emit('cancel');
};
onMounted(async () => {
  const { data } = await getInspectInfo({
    id: props.resultsData.id,
  });
  inspectionInfoData.value = data;
  materialInfoData.value = {
    materialName: data.materialName,
    materialType: data.materialType.label,
  };
  getPleaseVerifyInfoSchema(data.inspectInfoVOList);
});
</script>

<style lang="scss">
  .content {
  padding-bottom: 9.38rpx;
  :deep(.form-title) {
    margin: 0;
  }
  .table_box {
    padding: 0 9.38rpx 9.38rpx;
  }
}
</style>
