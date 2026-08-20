<template>
  <BMBasicPage
    :title="t('新增异常')"
    :defaultPadding="false"
    @left-click="toBack"
    @cancel="toBack"
    @confirm="confirm"
  >
    <BMForm
      ref="formRef"
      v-bind="formProps"
    />
  </BMBasicPage>
  <BMSignModal
    v-model:show="showSign"
    v-model="signValue"
    :signature-data="submitData"
    :label-list="labelList"
    @confirm="signConfirm"
  />
</template>
<script lang="ts" setup>
  import { t } from "@/utils/useBmosI18n.js";
  import { BMBasicPage, BMForm, BMSignModal } from "@/BMComponents";
  import { nextTick, onMounted, reactive, ref } from "vue";
  import { exceptionSave, getProcedureDetailApi } from "@/api";
  import { reqDictDownApi } from "@/api/webViewApi.js";
  import { timestampToTime } from "@/utils/time.js";
  import { urlQueryRef, pageBasicDataRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { getCurrentTime } from '@/utils/time.js';

  const emit = defineEmits(["submit"]);
  const formRef = ref();
  const showSign = ref(false);
  const signValue = ref({
    loginName1: "",
    password1: "",
    userId1: "",
  });
  const labelList = ref([
    {
      label: t("操作人"),
      // 签名动作
      signatureAction: 117,
      menuId: 121040001000007,
      currentUser: true
    },
  ]);
  const submitData = ref();
  const exceptionTypeList = ref<any>({});
  const productId = ref('');

  const confirm = async () => {
    // 表单校验
    formRef.value?.submit();
    const params = await formRef.value?.validate();
    submitData.value = { 
      ...params,
      productId: productId.value,
      processId: urlQueryRef.value.processId,
      processVersion: urlQueryRef.value.processVersion,
      productPlanId: urlQueryRef.value.productPlanId,
      procedureModelId: urlQueryRef.value.procedureModelId,
      procedureStepModelId: urlQueryRef.value.procedureStepModelId
    };
    showSign.value = true;
  };
  const signConfirm = async () => {
    try {
      await exceptionSave({
        ...submitData.value,
        recordUserId: signValue.value.userId1,
        exceptionType:
          exceptionTypeList.value[submitData.value.exceptionTypeCode],
        recordTime: getCurrentTime(),
      });
      showSign.value = false;
      toBack();
      emit("submit");
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: "error",
          duration: 2000,
          mask: true,
        });
    }
  };

  const toBack = () => {
    uni.navigateBack();
  };
  onMounted(async () => {
    // 设置异常类型下拉
    const { data } = await reqDictDownApi({ dictId: "120090001001" });
    data.map((item: any) => {
      exceptionTypeList.value[item.value] = item.label;
    });
    nextTick(() => {
      formRef.value?.updateSchema({
        field: "exceptionTypeCode",
        componentProps: {
          options: [...data],
        },
      });
    });
    // 获取回填信息
    const { data: productData } = await getProcedureDetailApi({procedureStepModelId: urlQueryRef.value.procedureStepModelId, productPlanId: urlQueryRef.value.productPlanId})
    formRef.value.setFieldsValue({
      productFullName: productData.productName,
      processId: productData.processName,
      processVersion: productData.processVersion,
      batchNo: productData.batchNo,
      procedureModelId: productData.procedureName,
      procedureStepModelId: productData.procedureStepName
    });
    productId.value = productData.productId
  });
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: "formTitle1",
        component: "FormTitle",
        label: t("生产信息"),
        colProps: {
          span: 24,
        },
      },
      {
        field: "productFullName",
        component: "Input",
        label: t("产品信息"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "processId",
        component: "Input",
        label: t("所属工艺"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "processVersion",
        component: "Input",
        label: t("工艺版本"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "batchNo",
        component: "Input",
        label: t("生产批次"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "procedureModelId",
        component: "Input",
        label: t("所属工序"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "procedureStepModelId",
        component: "Input",
        label: t("所属工序步骤/任务"),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: "formTitle2",
        component: "FormTitle",
        label: t("异常信息"),
        colProps: {
          span: 24,
        },
      },
      {
        field: "exceptionTypeCode",
        component: "BMFormSelect",
        label: t("异常类型"),
        required: true,
        colProps: {
          span: 12,
        },
      },
      {
        field: "exceptionDescription",
        component: "Textarea",
        label: t("异常描述"),
        required: true,
        colProps: {
          span: 12,
        },
      },
    ],
  });
</script>
<style lang="scss" scoped></style>
