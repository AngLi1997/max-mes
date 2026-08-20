<template>
  <BMLayout>
    <BMBasicPage
      v-if="!showAdd"
      :title="t('配液计划')"
      :default-padding="false"
      :loading="loading"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirmPage"
    >
      <template #titleRight>
        <view class="title">
          {{ `${t("配液单")}：${infoData.name}` }}
        </view>
      </template>
      <BMInfoDisplay
        :title="t('产出中间品')"
        icon="chengpinwuliao"
        background="#f7f8fa"
        :basic-items="[
          {
            label: t('中间品信息'),
            field: 'materialMergeCode',
          },
          {
            label: t('目标体积'),
            field: 'targetVolume',
          },
        ]"
        :info-data="infoData"
      />
      <view class="content">
        <view class="content_left">
          <wd-sidebar v-model="activeType" @change="activeTypeChange">
            <wd-sidebar-item
              v-for="item in infoData.materialList"
              :key="item.id"
              :value="item.id"
              :label="item.materialName"
            />
          </wd-sidebar>
        </view>
        <view class="content_right">
          <BMDataInfoDisplay
            :basic-items="[
              {
                label: t('物料编码'),
                field: 'materialMergeCode',
              },
              {
                label: t('浓度参数'),
                field: 'consistenceParamName',
              },
              {
                label: t('目标浓度'),
                field: 'targetConcentration',
              },
            ]"
            :info-data="activeInfo"
          />
          <view class="addMaterial">
            <wd-button size="small" @click="toAddMaterial">
              {{ t("添加物料") }}
            </wd-button>
          </view>
          <view class="table_box">
            <BMTable
              ref="tableRef"
              align="left"
              v-bind="tableProps"
            />
          </view>
        </view>
      </view>
      <BMModal
        v-model="openDetailFlag"
        :title="t('物料批次详情')"
        size="medium"
        @cancel="openDetailFlag = false"
        @confirm="openDetailFlag = false"
      >
        <BMInfoDisplay
          :is-show-title="false"
          :basic-items="basicItemsData"
          :info-data="showDetailData"
          is-show-one
        />
      </BMModal>
      <!-- 签名 -->
      <BMSignModal
        v-model:show="showSign"
        v-model="signValue"
        :label-list="labelList"
        :title="t('完成计划')"
        show-remark
        :signature-data="curParams"
        @confirm="confirmSignPopup"
      />
      <!-- 未配置提示 -->
      <BMMessageBox
        v-model="showMessageBox"
        :title="t('提示')"
        :content="t('组件未配置配液信息')"
        :show-cancel-button="false"
        @confirm="toBack"
      />
    </BMBasicPage>
    <AddMaterial v-else @config="addMaterialSubmit" @cancel="showAdd = false" />
  </BMLayout>
</template>

<script setup>
import {
  completeApi,
  getPreparationApi,
} from '@/api';
import {
  BMBasicPage,
  BMDataInfoDisplay,
  BMInfoDisplay,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { nextTick, ref } from 'vue';
import AddMaterial from './addMaterial/index.vue';
import { activeInfo, infoData, satisfied } from './hooks/dataHooks';
import { useTable } from './hooks/table.jsx';

const queryInfo = ref({});
const { procedureStepModelId } = pageBasicDataRef.value;
const { productPlanId } = urlQueryRef.value;
const { version } = getCurrentCopyRecordItem();
const activeType = ref(0); // 当前点击配料
const openDetailFlag = ref(false);
const showSign = ref(false);
const showMessageBox = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const loading = ref(false);
const labelList = ref([
  {
    label: t('计划人'),
    signatureAction: 92,
  },
]);
const showAdd = ref(false);
const curParams = ref({});
const completed = ref(false);
// 返回
const toBack = () => {
  uni.navigateBack();
  initFillData2();
};
  // 签名
const confirmSignPopup = async () => {
  showSign.value = false;
  try {
    loading.value = true;
    const { userId1 } = signValue.value;
    await completeApi({
      preparationPlanId: infoData.value.id,
      userId: userId1,
    });
    toBack();
  }
  catch (error) {
    error.message
    && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
  finally {
    loading.value = false;
  }
};
  // 点击查看
const opDetail = () => {
  openDetailFlag.value = true;
};
const { tableRef, tableProps, getTableList, showDetailData, basicItemsData } = useTable(opDetail, activeInfo);

// 点击跳转至添加物料批次页面
const toAddMaterial = () => {
  if (completed.value) {
    uni.showToast({
      title: t('配液计划已完成'),
      icon: 'none',
    });
    return;
  }
  showAdd.value = true;
};

// 切换列表类型
const activeTypeChange = async ({ value }) => {
  const data = infoData.value.materialList.find(item => item.id === value) || {
    materialMergeCode: '',
    consistenceParamName: '',
    targetConcentration: '',
  };
  // 查询列表数据
  await getTableList({
    formulaMaterialId: data.id,
    preparationPlanId: infoData.value.id,
  });
  // 判断是否满足浓度
  satisfied.value = tableProps.data.length !== 0;
  activeInfo.value = {
    ...data,
    materialMergeCode: { value: data.materialMergeCode },
    consistenceParamName: { value: data.consistenceParamName },
    targetConcentration: { value: data.targetConcentration, waring: !satisfied.value, success: satisfied.value },
  };
};

const addMaterialSubmit = () => {
  showAdd.value = false;
  nextTick(() => {
    activeTypeChange({ value: activeType.value });
  });
};
  // 点击确定
const confirmPage = () => {
  if (completed.value) {
    uni.showToast({
      title: t('配液计划已完成'),
      icon: 'none',
    });
    return;
  }
  curParams.value = {
    preparationPlanId: infoData.value.id,
  };
  showSign.value = true;
};
const initData = async () => {
  const params = {
    componentId: queryInfo.value.id,
    copyVersion: version,
    procedureStepModelId,
    productPlanId,
  };
  const { data } = await getPreparationApi(params);
  if (data.noConfig) {
    showMessageBox.value = true;
    return;
  }
  completed.value = data.completed;
  infoData.value = {
    ...data,
    materialMergeCode: `${data.materialMergeCode}-${data.materialName}`,
    materialName: data.materialName,
  };
  activeType.value = data.materialList[0]?.id || '';
  activeTypeChange({ value: activeType.value });
};
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  initData();
});
</script>

<style lang="scss" scoped>
  .title {
  font-size: 11.72rpx;
  color: #6c6e73;
}
.content {
  padding-top: 9.38rpx;
  height: calc(100% - 2.2rem);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e1e3e5;
  .content_left {
    height: 100%;
  }
  .content_right {
    height: 100%;
    width: calc(100% - 6.4rem);
    padding: 0 9.38rpx;
    .addMaterial {
      margin: 9.38rpx 0;
      text-align: right;
    }
    .table_box {
      height: calc(100% - 100rpx);
    }
  }
}
</style>
