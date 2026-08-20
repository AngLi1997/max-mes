<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配料投入')"
      :confirm-text="confirmText"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirm"
    >
      <view class="container">
        <view class="ingredients-msg">
          <view class="left">
            <view class="icon-view">
              <wd-icon name="list" size="11.72rpx" color="#2871FF" />
            </view>
            <text class="label">
              {{ t("配料单") }}：
            </text>
            <view class="ingredients">
              {{ ingredientsValue.name }}
            </view>
          </view>
          <view class="right">
            <wd-button
              type="text"
              size="small"
              @click="handleClickIngredients"
            >
              {{ t('切换配料单') }}
            </wd-button>
          </view>
        </view>
        <view class="top-scan">
          <BMScan
            v-model="scanValue"
            type="input"
            :allow-types="['01', '02', '04']"
            :error-type-placeholder="t('请扫描物料件或容器')"
            @success="onScanSuccess"
            @fail="onScanFail"
            @confirm="onScanSuccess"
          />
        </view>
        <view class="table-container">
          <BMTable ref="tableRef" v-bind="tableProps" />
        </view>
      </view>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :label-list="labelList"
      :title="t('签名确认')"
      :signature-data="curParams"
      @confirm="confirmSignPopup"
    />
    <!-- 设备 -->
    <BmosEquipment
      v-model="showEquipment"
      :title="t('确认投入设备')"
      :component-id="queryInfo.id"
      @confirm="equipmentPopupConfirm"
    />
    <!-- 选择配料单 -->
    <BMRadioModal
      v-model="ingredientsValueId"
      v-model:open="ingredientsModal"
      :title="t('配料单选择')"
      :options="ingredientsOptions"
      :required="true"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      @confirm="ingredientsConfirm"
      @cancel="ingredientsCancel"
    />
  </BMLayout>
</template>

<script setup lang="jsx">
import {
  reqMesIngredientInputInputApi,
  reqMesIngredientInputInstanceApi,
  reqMesQueryInputListByPlanIdApi,
  reqMesQueryPendingInputPlanListApi,
  reqMesScanWeighMaterialCodeWithIngredientPlanIdApi,
} from '@/api';
import { BMBasicPage, BMLayout, BMRadioModal, BMScan, BMSignModal, BMTable } from '@/BMComponents/index.js';
import BmosEquipment from '@/components/BmosEquipment/index.vue';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import {
  onLoad,
} from '@dcloudio/uni-app';
import { computed, ref } from 'vue';
import { useMessage, useToast } from 'wot-design-uni';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

const toast = useToast();
const message = useMessage();
// 返回
const toBack = () => {
  uni.navigateBack();
};
const tableRef = ref();
// table数据
const tableData = ref([]);
const queryInfo = ref({});

const componentInstanceId = ref('');
// 进入直接完成的情况，第一次获取数据时，判断是否完成
const updateFirstGetHasDone = ref(false);
const ingredientsModal = ref(false);
const ingredientsValue = ref({});
const ingredientsValueId = ref('');

const done = () => {
  if (Number(queryInfo.value?.isUpdate) === 2 && updateFirstGetHasDone.value) {
    toast.warning(t('配料投入已完成'));
    return;
  }
  message
    .confirm({
      msg: t('是否完成配料投入'),
      title: t('完成配料投入'),
    })
    .then(() => {
      toBack();
    });
};
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('投料人'),
    signatureAction: 48,
    disabled: true,
  },
]);
  // 是否可以完成
const canFinish = ref(false);
const confirmText = computed(() => {
  return canFinish.value ? t('完成') : t('投料');
});

const ingredientsOptions = ref([]);
const detailObj = ref({});

// 判断是否完成函数
const isFinishFun = (firstGet = false) => {
  const hasDone = tableData.value?.find(item => item.inputStatus?.value === 1 || item.inputStatus?.value === 2 || item.weighSignStatus?.value !== 1);
  if (detailObj.value?.weighStatus?.value !== 2 || hasDone) {
    canFinish.value = false;
  }
  else {
    if (firstGet) {
      updateFirstGetHasDone.value = true;
    }
    canFinish.value = true;
  }
};
const queryInputListByPlanId = async (firstGet = false) => {
  try {
    const { data } = await reqMesQueryInputListByPlanIdApi({
      ingredientPlanId: ingredientsValue.value.id,
      componentInstanceId: componentInstanceId.value,
    });
    const { inputList } = data;
    detailObj.value = data;
    tableData.value = inputList;
    isFinishFun(firstGet);
  }
  catch (error) {
    error.message && toast.error(error.message);
  }
};

const ingredientsConfirm = (data) => {
  if (!data || !data.id) {
    return;
  }
  ingredientsValue.value = data;
  queryInputListByPlanId();
};
// 获取未完成的配料单
const getIngredientsOptions = async (changeFlag) => {
  const { productPlanId } = urlQueryRef.value;
  const res = await reqMesQueryPendingInputPlanListApi({
    productPlanId,
  });
  ingredientsOptions.value = res.data;
  // 只有一个配料单时，默认选中
  if (ingredientsOptions.value.length === 1 && !changeFlag) {
    ingredientsValueId.value = ingredientsOptions.value[0].id;
    ingredientsConfirm(ingredientsOptions.value[0]);
    return;
  }
  ingredientsModal.value = true;
};
const handleClickIngredients = () => {
  const item = tableData.value?.find(item => item.inputStatus.value === 3);
  if (item) {
    toast.error(t('已确认配料单，无法切换'));
    return;
  }
  getIngredientsOptions(1);
};

const ingredientsCancel = () => {
  if (!ingredientsValue.value.id) {
    toBack();
  }
};

const inputValue = ref('');

const getMaterialByCode = async (code) => {
  try {
    const { data } = await reqMesScanWeighMaterialCodeWithIngredientPlanIdApi({
      ingredientPlanId: ingredientsValue.value.id,
      no: code,
      componentInstanceId: componentInstanceId.value,
    });
    const item = tableData.value?.find(item => item.storageMaterialNo === data.materialNo);
    if (item && item.inputStatus.value === 1) {
      // 把当前物料的状态改为投料中， 修改tableData中的此项
      tableData.value?.map((item) => {
        if (item.storageMaterialNo === data.materialNo) {
          item.inputStatus = {
            value: 2,
            name: t('投料中'),
          };
        }
        return item;
      });
      isFinishFun();
    }
  }
  catch (error) {
    error.message && toast.error(error.message);
  }
};

// 状态map 待投料\投料中\已投料\已失效
const statusMap = new Map([
  [1, {
    text: t('待投料'),
    type: 'primary',
  }],
  [2, {
    text: t('投料中'),
    type: 'success',
  }],
  [3, {
    text: t('已投料'),
    type: 'default',
  }],
  [4, {
    text: t('已失效'),
    type: 'danger',
  }],
]);

const tableProps = computed(() => {
  return {
    pagination: false,
    data: tableData.value,
    tableColProps: [
      {
        label: t('物料名称'),
        prop: 'materialName',
      },
      {
        label: t('物料编码'),
        prop: 'materialMergeCode',
      },
      {
        label: t('物料批号'),
        prop: 'storageMaterialBatchNo',
      },
      {
        label: t('物料件号'),
        prop: 'storageMaterialNo',
      },
      {
        label: t('物料量'),
        prop: 'quantity',
      },
      {
        label: t('单位'),
        prop: 'unit',
      },
      {
        label: t('状态'),
        prop: 'inputStatus',
        customRender: ({ row }) => {
          // 未签名 row['weighSignStatus']?.value !== 1
          if (row.weighSignStatus?.value !== 1) {
            return <WdTag plain type="warning">{ t('未签名') }</WdTag>;
          }
          return <WdTag type={statusMap.get(row.inputStatus?.value)?.type} plain>{ row.inputStatus?.name }</WdTag>;
        },
      },
      {
        label: t('投料人'),
        prop: 'importerName',
      },
      {
        label: t('投料时间'),
        prop: 'inputTime',
      },
      {
        label: t('设备名称'),
        prop: 'deviceName',
      },
      {
        label: t('设备编码'),
        prop: 'deviceCode',
      },
    ],
  };
});

const curParams = ref({});
const showEquipment = ref(false);
const submit = (data) => {
  const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
  const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
  const { version } = getCurrentCopyRecordItem();
  const list = tableData.value.map((item) => {
    if (item.inputStatus?.value === 2) {
      return item.storageMaterialNo;
    }
    else {
      return null;
    }
  }).filter(item => item);
  if (!list.length) {
    toast.error(t('请先扫描物料件或容器'));
    return;
  }
  curParams.value = {
    deviceId: data.deviceId,
    ingredientPlanId: ingredientsValue.value.id,
    storateMaterialNoList: list,
    batchNo,
    componentId: queryInfo.value.id,
    copyVersion: version,
    procedureStepId,
    procedureStepModelId,
    processId,
    processVersion,
    productPlanId,
    recordItemId,
    recordVersionId,
    reuse: reusable,
  };
  showEquipment.value = false;
  showSign.value = true;
};
const handleSubmit = () => {
  showEquipment.value = true;
};
const equipmentPopupConfirm = (data) => {
  if (!data.deviceId) {
    toast.error(t('请确认投入设备'));
    return;
  }
  submit(data);
};

const confirm = () => {
  if (canFinish.value) {
    done();
  }
  else {
    handleSubmit();
  }
};
const saveReq = async (operatorId) => {
  try {
    curParams.value.inputUserId = operatorId;
    await reqMesIngredientInputInputApi(curParams.value);
    initFillData2();
    queryInputListByPlanId();
  }
  catch (error) {
    queryInputListByPlanId();
    error.message && toast.error(error.message);
  }
};
const confirmSignPopup = () => {
  const { userId1 } = signValue.value;
  saveReq(userId1);
  showSign.value = false;
};

const scanValue = ref('');
const onScanSuccess = (code) => {
  if (!code) {
    toast.error(t('扫码失败'));
    return;
  }
  inputValue.value = code;
  getMaterialByCode(code);
};
const onScanFail = () => {
  toast.error(t('扫码失败'));
};

const getDetail = async (query) => {
  try {
    queryInfo.value = query;
    const { procedureStepModelId } = pageBasicDataRef.value;
    const { productPlanId } = urlQueryRef.value;
    const { version } = getCurrentCopyRecordItem();
    const { data } = await reqMesIngredientInputInstanceApi({
      componentId: query.id,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
    });
    componentInstanceId.value = data.componentInstanceId;
    if (data.ingredientPlanId) {
      ingredientsValueId.value = data.ingredientPlanId;
      ingredientsValue.value = {
        id: data.ingredientPlanId,
        name: data.ingredientPlanName,
      };
      queryInputListByPlanId(true);
    }
    else {
      getIngredientsOptions();
    }
  }
  catch (error) {
    //
    console.log(error);
  }
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  getDetail(query);
  // #endif
  // #ifdef H5
  getDetail(e);
  // #endif
});
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 9.38rpx;
  .table-container {
    flex: 1;
    overflow: hidden;
  }
}
.ingredients-msg {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f2f7ff;
  line-height: 32.81rpx;
  padding: 0 10rpx;
  border-radius: var(--bmos-border-radius-medium);
  .left {
    display: flex;
    align-items: center;
    .icon-view {
      height: 18.75rpx;
      width: 18.75rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #d9e5ff;
      border-radius: 2.34rpx;
    }
    .label {
      margin-left: 10rpx;
      color: var(--bmos-color-text-sub);
      font-size: 11.72rpx;
    }
    .ingredients {
      flex: 1;
      display: flex;
      align-items: center;
      font-size: 11.72rpx;
      .arrow-right {
        margin-left: 5rpx;
      }
    }
  }
}
.top-scan {
  display: flex;
  justify-content: flex-end;
  .wd-input {
    width: 50%;
  }
}
</style>
