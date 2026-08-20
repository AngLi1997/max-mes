<template>
  <BMLayout>
    <BMBasicPage
      :title="t('设备使用日志填报')"
      :default-padding="false"
      @left-click="toBack"
    >
      <view class="content">
        <BMForm
          ref="formRef"
          v-bind="formProps"
        />
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button
              type="info"
              block
              @click="toBack"
            >
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="6">
            <wd-button
              type="info"
              block
              @click="saveData"
            >
              {{ t("保存") }}
            </wd-button>
          </wd-col>
          <wd-col :span="6">
            <wd-button
              block
              @click="confirm"
            >
              {{ t("完成") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <!-- 产品名称树弹窗 -->
    <BMTreeModal
      v-model="formData.batchNo"
      v-model:open="showBatchTree"
      :title="t('产品名称')"
      :tree-data="treeModalData"
      :field-names="{
        name: 'showName',
        key: 'id',
        checkKey: 'categoryFlag',
        checkKeyValue: true,
        parentId: 'parentId',
        children: 'children',
      }"
      mode="single"
      @confirm="selectProdect"
    />
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :signature-data="submitData"
      :label-list="labelList"
      @confirm="signConfirm"
    />
  </BMLayout>
</template>

<script lang="ts" setup>
import { fillEquipmentLog, getUseLogTemplate, incompleteEquipmentLog, listAllPlanByProductId, saveEquipmentLog } from '@/api';
import { getProductTreeApi } from '@/api/productionApi.js';
import {
  BMBasicPage,
  BMForm,
  BMLayout,
  BMSignModal,
  BMTreeModal,
} from '@/BMComponents';
import { timestampToTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  equipmentData: {
    type: Object,
    default: () => {},
  },
});

const emit = defineEmits(['close']);

const { showNotify } = useNotify();

const showBatchTree = ref(false);
const treeModalData = ref();
const formData = ref({
  batchNo: '',
});
const formRef = ref();
const showSign = ref(false);
const selectProdectName = ref('');
const incompleteEquipmentLogId = ref('');
const signValue = ref<any>({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('签名人'),
    signatureAction: 1,
    menuId: 121030003,
  },
  {
    label: t('复核人'),
    signatureAction: 4,
    menuId: 121030003,
  },
]);
const submitData = ref();

const saveData = async () => {
  try {
    const params = formRef.value.getFormValues();
    await fillEquipmentLog({
      ...params,
      equipmentId: props.equipmentData.id,
      code: props.equipmentData.code,
      changeType: '0',
      id: incompleteEquipmentLogId.value,
      beginTime: params.beginTime ? timestampToTime(params.beginTime) : '',
      endTime: params.endTime ? timestampToTime(params.endTime) : '',
    });
    toBack();
  }
  catch (error) {
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};

const confirm = async () => {
  // 表单校验
  formRef.value?.submit();
  const params = await formRef.value?.validate();
  submitData.value = {
    ...params,
    equipmentId: props.equipmentData.id,
    code: props.equipmentData.code,
  };
  showSign.value = true;
};
const toBack = () => {
  emit('close');
};
const signConfirm = async () => {
  try {
    showSign.value = false;
    await saveEquipmentLog({
      ...submitData.value,
      operator: signValue.value.userId1,
      reviewer: signValue.value.userId2,
      changeType: '0',
      id: incompleteEquipmentLogId.value,
      beginTime: timestampToTime(submitData.value.beginTime),
      endTime: timestampToTime(submitData.value.endTime),
    });
    toBack();
  }
  catch (error) {
    error.message && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
const selectProdect = async (product) => {
  // 回显值
  formRef.value.setFieldsValue({
    productName: product.showName,
    batchNo: '',
  });
  // 获取生产批次
  const { data } = await listAllPlanByProductId({
    productId: product.id,
  });
  formRef.value?.updateSchema({
    field: 'batchNo',
    componentProps: {
      options: [...data],
    },
  });
};

// 表单配置
const formProps = reactive({
  schemas: [
    {
      field: 'equipmentName',
      component: 'Input',
      label: t('设备名称'),
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'code',
      component: 'Input',
      label: t('设备编号'),
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'productName',
      component: 'Input',
      label: t('产品名称'),
      colProps: {
        span: 12,
      },
      componentProps: {
        readonly: true,
        placeholder: t('请选择'),
        onClick: () => {
          showBatchTree.value = true;
        },
      },
    },
    {
      field: 'batchNo',
      component: 'BMFormSelect',
      label: t('生产批次'),
      colProps: {
        span: 12,
      },
      componentProps: {
        placeholder: t('请选择'),
        options: [],
        title: t('生产批次'),
        fieldNames: {
          label: 'batchNo',
          value: 'batchNo',
        },
      },
    },
    {
      field: 'beginTime',
      component: 'BMFormDatePicker',
      label: t('开始使用时间'),
      colProps: {
        span: 12,
      },
      dynamicRules: ({ formModel }: any) => {
        return [
          {
            required: true,
            message: t('请选择开始使用时间'),
          },
          {
            validator: async () => {
              if (!formModel.beginTime) {
                return Promise.reject(t('请选择开始使用时间'));
              }
              if (formModel.beginTime > formModel.endTime) {
                return Promise.reject(t('开始时间<结束时间'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'endTime',
      component: 'BMFormDatePicker',
      label: t('结束使用时间'),
      colProps: {
        span: 12,
      },
      dynamicRules: ({ formModel }: any) => {
        return [
          {
            required: true,
            message: t('请选择开始使用时间'),
          },
          {
            validator: async () => {
              if (!formModel.beginTime) {
                return Promise.reject(t('请选择结束使用时间'));
              }
              if (formModel.beginTime > formModel.endTime) {
                return Promise.reject(t('开始时间<结束时间'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'templateId',
      component: 'BMFormSelect',
      label: t('操作名称'),
      colProps: {
        span: 12,
      },
      componentProps: {
        options: [],
        title: t('操作名称'),
        fieldNames: {
          label: 'operateName',
          value: 'id',
        },
        onConfirm: (value) => {
          formRef.value.setFieldsValue({
            operateContent: value.template,
          });
        },
      },
    },
    {
      field: 'operateContent',
      component: 'Textarea',
      label: t('操作内容'),
      required: true,
      colProps: {
        span: 24,
      },
      noUseMaxLengthRule: true,
      componentProps: {
        autoHeight: true,
        maxlength: 1000,
      },
    },
  ],
});
const getChildrenData = (arr: any) => {
  const newArr = [] as any;
  arr.forEach((item: any) => {
    item.categoryFlag = !item.categoryFlag;
    if (selectProdectName.value && selectProdectName.value === item.showName) {
      formData.value.batchNo = item.id;
    }
    if (item.children.length > 0) {
      item.children = getChildrenData(item.children);
    }
    newArr.push(item);
  });
  return newArr;
};
  // 获取产品树数据
const getProductTree = async () => {
  const { data } = await getProductTreeApi({ categoryType: 2 });
  treeModalData.value = getChildrenData(data);
};

onMounted(async () => {
  formRef.value.setFieldsValue({
    equipmentName: props.equipmentData.name,
    code: props.equipmentData.code,
  });
  // 获取设备绑定信息
  const { data } = await getUseLogTemplate(props.equipmentData.id);
  formRef.value?.updateSchema({
    field: 'templateId',
    componentProps: {
      options: [...data],
    },
  });
  // 获取设备之前填报的信息
  const { data: info } = await incompleteEquipmentLog({ equipmentId: props.equipmentData.id });
  if (info) {
    selectProdectName.value = info.productName;
    await getProductTree();
    if(info.productName){
      await selectProdect({
        productName: info.productName,
        id: formData.value.batchNo,
      });
    }
    formRef.value.setFieldsValue({
      ...info,
      beginTime: info.beginTime ? new Date(info.beginTime) : '',
      endTime: info.endTime? new Date(info.endTime) : '',
    });
    incompleteEquipmentLogId.value = info.id;
    return;
  }
  getProductTree();
});
</script>

<style lang="scss" scoped>
  .content {
  border-top: 1px solid #e1e3e5;
  padding: 11.72rpx 9.38rpx 0;
  :deep(.uni-textarea-compute) {
    padding: 9.38rpx;
  }
  :deep(.uni-textarea-textarea) {
    width: calc(100% - 18.75rpx);
    margin-left: 9.38rpx;
  }
  :deep(.wd-textarea__placeholder) {
    padding-left: 9.38rpx;
  }
  :deep(.wd-textarea::after) {
    display: none;
  }
}
</style>
