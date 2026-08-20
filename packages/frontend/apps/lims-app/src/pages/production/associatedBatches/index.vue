<template>
  <BMLayout>
    <BMBasicPage
      :title="t('关联批次')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirm"
    >
      <AssociatedBatchesComponent v-model:dataList="dataList" />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { BMBasicPage, BMLayout } from '@/BMComponents';
  import AssociatedBatchesComponent from '../components/associatedBatchesComponent/index.vue';
  import {
    getMesPlanRelationList,
    updateRelationApi
  } from '@/api/productionApi.js';
  import { onMounted, ref } from 'vue';
  import { useNotify } from 'wot-design-uni';
  const { showNotify } = useNotify();

  const props = defineProps({
    productPlanId: {
      type: String,
      required: true
    }
  });

  const dataList = ref([]);
  const toBack = () => {
    uni.navigateBack();
  };

  // 获取关联的工艺
  const getMesPlanRelation = async() => {
    try {
      const res = await getMesPlanRelationList({ planId: props.productPlanId });
      dataList.value = res.data;
    } catch (error) {
      showNotify({
        type: 'warning',
        message: error
      });
    }
  };
  const confirm = async() => {
    const relationPlanList = dataList.value.map((item) => ({
      processId: item.processId,
      planIds: item.relationBatchList.map((batch) => batch.planId)
    }));
    try {
      await updateRelationApi({
        productPlanId: props.productPlanId,
        relationPlanList
      });
      showNotify({
        type: 'success',
        message: t('保存成功')
      });
      uni.navigateBack();
    } catch (error) {
      showNotify({
        type: 'warning',
        message: error
      });
    }
  };

  onMounted(() => {
    getMesPlanRelation();
  });
</script>

<style lang="scss" scoped></style>
