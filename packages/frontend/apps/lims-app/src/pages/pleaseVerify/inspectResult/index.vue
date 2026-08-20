<template>
  <BMLayout>
    <BMBasicPage
      :title="t('检验结果')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <BMForm ref="infoFormRef" v-bind="infoFormProps" />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMForm,
  BMLayout,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { getInspectResultApi } from '@/api'
import { nextTick, ref } from 'vue';
import { useForm } from './hooks/useForm.jsx';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

const queryInfo = ref({});

const toBack = () => {
  uni.navigateBack();
};

const { infoFormRef, infoFormProps, setTableData, setFormModel } = useForm();

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
    const { data } = await getInspectResultApi({id: queryInfo.value.id});
    const formModel = {
      ...data,
      materialType: data.materialType?.label,
      inspectResultLabel: data.inspectResult?.label,
      inspectResultValue: data.inspectResult?.value
    };
    nextTick(() => {
      setFormModel(formModel)
      setTableData(data.inspectProgramResultVOList || []);
    });
  } catch (error) {
    error.message && showNotify({
      type: 'warning',
      message: error.message,
    });
  }
  
});
</script>

<style lang="scss" scoped>

</style>
