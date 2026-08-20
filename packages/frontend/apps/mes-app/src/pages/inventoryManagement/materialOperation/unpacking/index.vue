<template>
  <BMLayout>
    <BMBasicPage
      :title="t('拆包出库')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <InfoTable :details="details" :data="urlQuery" :title="t('物料信息')" />
      <BMForm ref="formsRef" v-bind="formProps">
        <template #formHeader>
          <view class="form-title">
            {{ t("出库信息") }}
          </view>
        </template>
      </BMForm>
    </BMBasicPage>
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="jumpOverPrint" />
  </BMLayout>
  <!-- 签名 -->
  <BMSignModal
    v-model:show="showSign"
    v-model="signValue"
    :title="t('签名确认')"
    :signature-data="signatureData"
    :label-list="labelList"
    :field-names="{ value: 'loginName', label: 'userName', id: 'userId' }"
    @confirm="signSubmit"
  />
  <!-- 扫码 -->
  <BMScanNew @success="onScanSuccess" />
</template>

<script setup>
import { getStorageMaterialPrintTag } from '@/api';
import {
  getQueryPositionBoundUserListByPermissionCodeApi,
  getStorageMaterialSplitPackageApi,
} from '@/api/storage.js';
import { scanWeighContainerCodeApi } from '@/api/weighingIngredientsApi.js';
import { BMBasicPage, BMForm, BMLayout, BMScanNew, BMSignModal } from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { nextTick, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useData } from './hooks/useData';

const { showNotify } = useNotify();

const bmosPrinterInstance = ref(null);
const urlQuery = ref({});
const signatureData = ref({});
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
  loginName2: '',
  password2: '',
  userId2: '',
});

// 获取容器信息
const getContainerInfo = async (code) => {
  try {
    const res = await scanWeighContainerCodeApi({
      code,
    });
    formsRef.value.setFormModels({
      container: `${res.data.deviceCode} - ${res.data.deviceName}`,
      containerId: res.data.deviceId,
    });
  }
  catch (error) {
    formsRef.value.setFormModels({
      containerId: '',
      container: '',
    });
    if (error.message) {
      showNotify({ type: 'danger', message: error.message });
    }
  }
};
const onScanSuccess = (res) => {
  if (res.startsWith('04')) {
    const code = res.substring(2);
    code && getContainerInfo(code);
  }
  else {
    showNotify({ type: 'warning', message: t('请扫描正确的容器编号') });
  }
};
const onScanConfirm = (formModel) => {
  if (formModel.container) {
    getContainerInfo(formModel.container);
  }
};
const { details, formsRef, formProps } = useData({
  onScanConfirm,
  urlQuery,
});
const labelList = ref([
  {
    label: t('出库人'),
    signatureAction: 70,
  },
  {
    label: t('领用人'),
    signatureAction: 71,
    options: [],
  },
]);

const toBack = () => {
  uni.navigateBack();
};
  // 物料预定提交
const submit = async () => {
  const values = await formsRef.value.validate();
  signatureData.value = {
    storageMaterialId: urlQuery.value.id, // 暂存物料件id
    ...values,
  };
  showSign.value = true;
};

// 获取签名人员
const getSignUser = async () => {
  try {
    const res = await getQueryPositionBoundUserListByPermissionCodeApi({
      positionId: urlQuery.value.materialPositionId,
      permissionCode: '121020002000012',
    });
    labelList.value[1].options = res.data;
  }
  catch (error) {
    showNotify({ type: 'danger', message: error.message });
  }
};

const jumpOverPrint = () => {
  const query = urlQuery.value.materialPositionId;
  uni.reLaunch({
    url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${query}`,
  });
};
  // 打印
const printing = async (no) => {
  showSign.value = false;
  const res = await bmosPrinterInstance.value.print();
  try {
    if (res) {
      const categoryType = urlQuery.value.categoryType;
      const sceneId = categoryType === '0' ? 121001011 : 121002015;
      const data1 = {
        body: {
          no: urlQuery.value.materialNo,
        },
        deviceId: res.id,
        sceneId,
      };
      const data2 = {
        body: {
          no,
        },
        deviceId: res.id,
        sceneId,
      };
      Promise.all([
        getStorageMaterialPrintTag(data1), // 打印剩余物料标签
        getStorageMaterialPrintTag(data2), // 打印拆包物料标签
      ]);
      jumpOverPrint();
    }
  }
  catch (error) {
    error.message && showNotify({ type: 'warning', message: error.message });
  }
};

const signSubmit = async () => {
  try {
    const { data } = await getStorageMaterialSplitPackageApi({
      senderId: signValue.value.userId1,
      receiverId: signValue.value.userId2,
      ...signatureData.value,
    });
    printing(data);
  }
  catch (error) {
    error.message && showNotify({ type: 'danger', message: error.message });
  }
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  urlQuery.value = {
    ...query,
  };
  // #endif
  // #ifdef H5
  urlQuery.value = {
    ...e,
  };
  // #endif
  nextTick(() => {
    formsRef.value.setFieldsValue({
      unit: urlQuery.value.unit,
    });
    getSignUser();
  });
});
</script>

<style lang="scss" scoped>
.form-title {
  font-size: 14.06rpx;
  color: var(--bmos-text-main);
  margin: 11.72rpx 0;
}
</style>
