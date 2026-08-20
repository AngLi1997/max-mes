<template>
  <BMLayout>
    <BMBasicPage
      :title="t('发起请验')"
      background-color="#F2F3F5"
      :default-padding="false"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <BMForm ref="infoFormRef" v-bind="infoFormProps" />
    </BMBasicPage>
    <!-- <BMSignModal
      v-model:show="signOpen"
      v-model="signValue"
      :title="t('签名确认')"
      :label-list="labelList"
      :signature-data="signatureParams"
      :field-names="{
        value: 'loginName',
        label: 'userName',
        id: 'userId',
      }"
      @confirm="submitSign"
    /> -->
  </BMLayout>
</template>

<script setup>
import { getInspectDetailApi, getProductionInfoApi, reStartInspectApi, startInspectApi } from '@/api';
import {
  BMBasicPage,
  BMForm,
  BMLayout,
  // BMSignModal
} from '@/BMComponents';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { nextTick, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import {
  useDetails,
  useForm,
  // useSign
} from './hooks';

const { showNotify } = useNotify();

// 路由参数
const queryInfo = ref({});
// 返回
const toBack = () => {
  uni.navigateBack();
};

const { details, detailData, detailsApi } = useDetails();
const { infoFormRef, infoFormProps, pleasVerifyInfoSchema, getPleaseVerifyInfoSchema, getMaterialTreeModalData, setFormModel } = useForm(details);
// const { signOpen, signValue, labelList, signatureParams } = useSign();

const submit = async () => {
  try {
    const res = await infoFormRef.value.validate();
    const initiateInspectInfoDTOList = [];
    let inspectNo = '';
    let materialBatchNo = '';
    for (let i = 1; i < pleasVerifyInfoSchema.value.length; i++) {
      const item = pleasVerifyInfoSchema.value[i];
      if (item.field === 'pleaseCheckNo') {
        inspectNo = res[item.field];
      }
      else if (item.field === 'materialBatchNo') {
        materialBatchNo = res[item.field];
      }
      initiateInspectInfoDTOList.push({
        code: item.field,
        dataName: item.dataName,
        inspectConfigDataId: item.inspectConfigDataId,
        required: item.required,
        showName: item.label,
        sort: item.sort,
        value: res[item.field],
      });
    }
    const params = {
      formulaMaterialId: res.formulaMaterialId,
      initiateInspectInfoDTOList,
      inspectConfigId: res.inspectConfigId,
      inspectNo,
      materialBatchNo,
      planId: queryInfo.value.planId,
      procedureModelId: queryInfo.value.procedureModelId,
      procedureStepModelId: queryInfo.value.procedureStepModelId,
      processChangeNumber: queryInfo.value.processChangeNumber,
      procedureChangeNumber: queryInfo.value.procedureChangeNumber,
    };
    if (queryInfo.value.reVerify) {
      await reStartInspectApi({ ...params, id: queryInfo.value.id });
    }
    else {
      await startInspectApi(params);
    }
    toBack();
    // signatureParams.value = res;
    // signOpen.value = true;
  }
  catch (error) {
    console.log(error.message);
    error.message && showNotify({
      type: 'warning',
      message: error.message,
    });
  }
};

// const submitSign = async () => {
//   try {
//     console.log('签名完成');
//     await startInspectApi(signatureParams.value);
//     signOpen.value = false;
//     toBack();
//   } catch(e) {
//     console.log(e);
//   }

// }

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
  await nextTick();
  setFormModel({ procedureModelId: queryInfo.value.procedureModelId });
  if (queryInfo.value.reVerify) {
    const { data } = await getProductionInfoApi(queryInfo.value?.planId);
    const formModel = data;
    const { data: configData } = await getInspectDetailApi({ id: queryInfo.value.id });
    formModel.materialType = `${configData.materialType?.value}`;
    formModel.materialName = configData.materialName;
    formModel.materialId = configData.materialId;
    formModel.formulaMaterialId = configData.formulaMaterialId;
    await getMaterialTreeModalData(formModel.materialType);
    await getPleaseVerifyInfoSchema(formModel.formulaMaterialId);
    infoFormRef.value?.appendSchemasByField(pleasVerifyInfoSchema.value, 'materialInfo');
    for (let i = 0; i < configData.inspectInfoVOList.length; i++) {
      const tem = configData.inspectInfoVOList[i];
      if (tem.code === 'inspector') { // 请验人
        const currentUser = getStorageSync(USER_INFO) || {};
        const { loginName, userName } = currentUser;
        formModel.inspector = `${userName}-${loginName}`;
      }
      else {
        formModel[tem.code] = tem.value;
      }
    }
    setFormModel(formModel);
  }
  else {
    await getMaterialTreeModalData('1');
    await detailsApi(queryInfo.value.planId);
    setFormModel(detailData.value);
  }
});
</script>

<style lang="scss" scoped>

</style>
