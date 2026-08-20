<template>
  <BMLayout>
    <BMBasicPage
      :title="t('请验详情')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <BMForm ref="infoFormRef" v-bind="infoFormProps" />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { getInspectDetailApi, getProductionInfoApi } from '@/api';
import {
  BMBasicPage,
  BMForm,
  BMLayout,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useForm } from './hooks/useForm.jsx';

const { showNotify } = useNotify();

const queryInfo = ref({});

const toBack = () => {
  uni.navigateBack();
};

const { infoFormRef, infoFormProps, getPleaseVerifyInfoSchema, setFormModel } = useForm();

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  try {
    const { data } = await getProductionInfoApi(queryInfo.value?.planId);
    let formModel = data;
    const { data: configData } = await getInspectDetailApi({ id: queryInfo.value?.id });
    formModel.materialType = configData.materialType?.label;
    formModel.materialName = queryInfo.value?.materialName;
    formModel.materialId = configData.materialId;
    for (let i = 0; i < configData.inspectInfoVOList.length; i++) {
      const item = configData.inspectInfoVOList[i];
      formModel[item.code] = item.value;
    }
    setFormModel(formModel);
    await getPleaseVerifyInfoSchema(configData.inspectInfoVOList);
  }
  catch (error) {
    error.message && showNotify({
      type: 'warning',
      message: error.message,
    });
  }
});
</script>

<style lang="scss" scoped>

</style>
