<template>
  <BMLayout>
    <view class="container">
      <BmosNavBar @left-click="toBack">
        <template #left>
          <view class="left-content">
            <uv-icon
              color="#797C80"
              name="fanhui"
              size="14.07rpx"
              custom-prefix="bmos-icon"
            />
            <text class="title">{{ t("称量结果") }}</text>
          </view>
        </template>
        <template #right>
          <wd-icon
            name="close"
            size="14.07rpx"
            color="#797C80"
            @click="close"
          />
        </template>
      </BmosNavBar>
      <view class="content">
        <view class="operator">
          <view class="operator_title">{{ t("当前操作人") }}</view>
          <view class="operator_name">
            <text class="label">{{ t("称量人") }}：</text>
            {{ detailData.weigherName }}-{{ detailData.weigherLoginName }}
          </view>
          <view class="operator_name">
            <text class="label">{{ t("复核人") }}：</text>
            {{ detailData.reCheckerName }}-{{ detailData.reCheckerLoginName }}
          </view>
        </view>
        <Section :title="t('配料称量')" />
        <scroll-view class="batching table_box" scroll-y="true">
          <uni-table
            ref="relocationTable"
            class="table-box"
            :empty-text="t('暂无更多数据')"
          >
            <uni-tr class="tr-tab">
              <uni-th
                v-for="(item, index) in tableConfig"
                :key="index"
                :align="item.align"
                class="th-tab"
                :width="item.width"
              >
                {{ item.label }}
              </uni-th>
              <uni-th align="center" class="th-tab" width="50">
                {{ t("标签") }}
              </uni-th>
            </uni-tr>
            <uni-tr
              v-for="(item, index) in detailData.ingredientList || []"
              :key="index"
            >
              <uni-td
                v-for="(sl, ix) in tableConfig"
                :key="ix"
                :width="sl.width"
                :align="sl.align"
              >
                <view
                  v-if="sl.filed == 'signStatus'"
                  :class="`status status_${item[sl.filed].value}`"
                >
                  {{ item[sl.filed].name }}
                </view>
                <view v-else-if="sl.filed == 'weigherName'">
                  {{ item.weigherName }}-{{ item.weigherLoginName }}
                </view>
                <view v-else-if="sl.filed == 'reCheckerName'">
                  {{ item.reCheckerName }}-{{ item.reCheckerLoginName }}
                </view>
                <template v-else>
                  {{ sl.filed == "index" ? index + 1 : item[sl.filed] }}
                </template>
              </uni-td>
              <uni-td width="50" align="center">
                <view
                  class="table_btn"
                  @click="print(item.categoryType.value, item.no)"
                >
                  {{ t("打印") }}
                </view>
              </uni-td>
            </uni-tr>
          </uni-table>
        </scroll-view>
        <Section :title="t('余料称量')" />
        <scroll-view class="oddments table_box" scroll-y="true">
          <uni-table
            ref="relocationTable"
            class="table-box"
            :empty-text="t('暂无更多数据')"
          >
            <uni-tr class="tr-tab">
              <uni-th
                v-for="(item, index) in tableConfig"
                :key="index"
                :align="item.align"
                class="th-tab"
                :width="item.width"
              >
                {{ item.label }}
              </uni-th>
              <uni-th align="center" class="th-tab" width="70">
                {{ t("标签") }}
              </uni-th>
            </uni-tr>
            <uni-tr v-for="(item, index) in detailData.oddList" :key="index">
              <uni-td
                v-for="(sl, ix) in tableConfig"
                :key="ix"
                :width="sl.width"
                :align="sl.align"
              >
                <view
                  v-if="sl.filed == 'signStatus'"
                  :class="`status status_${item[sl.filed].value}`"
                >
                  {{ item[sl.filed].name }}
                </view>
                <view v-else-if="sl.filed == 'weigherName'">
                  {{ item.weigherName }}-{{ item.weigherLoginName }}
                </view>
                <view v-else-if="sl.filed == 'reCheckerName'">
                  {{ item.reCheckerName }}-{{ item.reCheckerLoginName }}
                </view>
                <template v-else>
                  {{ sl.filed == "index" ? index + 1 : item[sl.filed] }}
                </template>
              </uni-td>
              <uni-td width="50" align="center">
                <view
                  class="table_btn"
                  @click="print(item.categoryType.value, item.no)"
                >
                  {{ t("打印") }}
                </view>
              </uni-td>
            </uni-tr>
          </uni-table>
        </scroll-view>
      </view>
      <view class="buttons-box">
        <uv-row justify="space-between" gutter="10">
          <uv-col span="6">
            <BmosButton
              type="default"
              :text="t('更换操作人')"
              @click="replaceOperator"
            />
          </uv-col>
          <uv-col span="6">
            <BmosButton type="primary" :text="t('签名')" @click="sign" />
          </uv-col>
        </uv-row>
      </view>
      <!--更换操作人签名-->
      <BMDoubleSignModal
        v-model:show="signOpen1"
        v-model:value1="signValue1"
        v-model:value2="signValue2"
        :title="t('更换操作人')"
        :label-list1="labelList1"
        :label-list2="labelList2"
        :label-title1="t('当前称量人签名')"
        :label-title2="t('更换操作人签名')"
        :field-names="{
          value: 'loginName',
          label: 'userName',
          id: 'userId',
        }"
        :signature-data="signatureData1"
        @confirm="signConfirm1"
      />
      <!-- 签名 -->
      <BMSignModal
        v-model:show="signOpen3"
        v-model="signValue3"
        :title="t('签名')"
        :label-list="labelList3"
        :signature-data="signatureData3"
        @confirm="signConfirm3"
      />
      <BmosPrinter ref="bmosPrinterInstance" />
      <!-- 退出配料称量组件 -->
      <BmosMessageBox
        v-model="open"
        :title="t('存在未签名物料，是否退出')"
        @confirm="goBackToTargetPath"
        @cancel="open = false"
      />
    </view>
  </BMLayout>
</template>

<script setup>
  import {
    pageBasicDataRef,
    getCurrentCopyRecordItem
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import {
    weighQueryResultApi,
    weighSignApi,
    weighChangeWeigherApi
  } from '@/api/weighingIngredientsApi.js';
  import { goBackToTargetPath } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { reqPrintStorageMaterialTagApi } from '@/api';
  import { t } from '@/utils/useBmosI18n.js';
  import { computed, onMounted, ref } from 'vue';
  import BmosNavBar from '@/components/BmosNavBar/index.vue';
  import BmosMessageBox from '@/components/BmosMessageBox/index.vue';
  import { tableConfig } from './config.js';
  import {
    BMSignModal,
    BMLayout,
    BMDoubleSignModal
  } from '@/BMComponents/index.js';
  import BmosPrinter from '@/components/BmosPrinter/index.vue';
  import BmosButton from '@/components/BmosButton/index.vue';
  import Section from '@/components/Section/index.vue';
  import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
  import { storeToRefs } from 'pinia';
  const weighingIngredientsStore = useWeighingIngredientsStore();
  const {
    selectedIngredients,
    materialInfo,
    weighingIngredientsData,
    reCheckerList,
    weighingPersonList
  } = storeToRefs(weighingIngredientsStore);
  const { queryWeighDetailByPlanIdAndBatchId } = weighingIngredientsStore;

  const data = {
    batchId: materialInfo.value ? materialInfo.value.storageMaterialBatchId : '',
    planId: selectedIngredients.value.id,
    id: weighingIngredientsData.value?.id
  };

  const props = defineProps({
    componentId: {
      type: String,
      default: ''
    }
  });

  const bmosPrinterInstance = ref(null);
  const detailData = ref({}); // 称量详情

  const signOpen1 = ref(false);
  const signOpen3 = ref(false);

  const signValue1 = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: ''
  });
  const signValue2 = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: ''
  });

  const signValue3 = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: ''
  });
  const signatureData1 = computed(() => {
    return {
      ingredientWeighProcessId: data.id,
      weigherId: signValue2.value.userId1 || void 0,
      reCheckerId: signValue2.value.userId2 || void 0
    };
  });
  const signatureData3 = computed(() => {
    return {
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      copyVersion: getCurrentCopyRecordItem().version,
      planId: data.planId,
      weigherId: signValue3.value.userId1 || void 0,
      reCheckerId: signValue3.value.userId2 || void 0
    };
  });
  const labelList1 = computed(() => {
    return [
      {
        label: t('称量人'),
        signatureAction: 65,
        disabled: true,
        options: [
          {
            userName: `${detailData.value.weigherName}`,
            loginName: detailData.value.weigherLoginName,
            userId: detailData.value.weigherId
          }
        ]
      },
      {
        label: t('复核人'),
        signatureAction: 65,
        disabled: true,
        options: [
          {
            userName: `${detailData.value.reCheckerName}`,
            loginName: detailData.value.reCheckerLoginName,
            userId: detailData.value.reCheckerId
          }
        ]
      }
    ];
  });
  const labelList2 = ref([
    {
      label: t('新称量人'),
      signatureAction: 65,
      options: weighingPersonList
    },
    {
      label: t('新复核人'),
      signatureAction: 65,
      options: reCheckerList
    }
  ]);
  const labelList3 = computed(() => {
    return [
      {
        label: t('称量人'),
        signatureAction: 43,
        disabled: true,
        options: [
          {
            label: `${detailData.value.weigherName}`,
            value: detailData.value.weigherLoginName,
            id: detailData.value.weigherId
          }
        ]
      },
      {
        label: t('复核人'),
        signatureAction: 44,
        disabled: true,
        options: [
          {
            label: `${detailData.value.reCheckerName}`,
            value: detailData.value.reCheckerLoginName,
            id: detailData.value.reCheckerId
          }
        ]
      }
    ];
  });

  // 查询称量详情
  const weighQueryResult = async() => {
    const { planId } = data;
    const res = await weighQueryResultApi({
      planId,
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      copyVersion: getCurrentCopyRecordItem().version
    });
    detailData.value = res.data;
  };

  // 打印
  const print = (type, no) => {
    const device = bmosPrinterInstance.value.print();
    if (device) {
      reqPrintStorageMaterialTagApi({
        deviceId: device.id,
        sceneId: type === 0 ? 121001002 : 121002002,
        body: {
          no
        }
      });
    }
  };

  // 更换操作人
  const replaceOperator = () => {
    if (
      detailData.value.ingredientList.some(
        (item) => item.signStatus.value === 0
      ) ||
      detailData.value.oddList.some((item) => item.signStatus.value === 0)
    ) {
      uni.showToast({
        title: t('已称量物料件需签名后才能更换'),
        icon: 'none'
      });
      return;
    }
    signValue1.value = {
      userId1: detailData.value.weigherId,
      userId2: detailData.value.reCheckerId,
      password1: '',
      password2: ''
    };
    signValue2.value = {
      userId1: '',
      userName1: '',
      loginName1: '',
      userId2: '',
      userName2: '',
      loginName2: '',
      password1: '',
      password2: ''
    };
    signOpen1.value = true;
  };

  // 签名
  const sign = () => {
    if (
      detailData.value.ingredientList.every(
        (item) => item.signStatus.value === 1
      ) &&
      detailData.value.oddList.every((item) => item.signStatus.value === 1)
    ) {
      uni.showToast({
        title: t('暂无物料需要签名确认'),
        icon: 'none'
      });
      return;
    }
    signOpen3.value = true;
  };

  // 更换操作人签名确认
  const signConfirm1 = async() => {
    try {
      await weighChangeWeigherApi(signatureData1.value);
      weighQueryResult();
      queryWeighDetailByPlanIdAndBatchId();
      signOpen1.value = false;
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'none'
        });
    }
  };
  // 签名确认
  const signConfirm3 = async() => {
    try {
      await weighSignApi({
        ...signatureData3.value
      });
      weighQueryResult();
      signOpen3.value = false;
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'none'
        });
    }
  };

  // 返回
  const toBack = () => {
    uni.navigateBack();
  };

  const open = ref(false);

  // 关闭配料称量组件
  const close = () => {
    if (
      detailData.value.ingredientList.every(
        (item) => item.signStatus.value === 1
      ) &&
      detailData.value.oddList.every((item) => item.signStatus.value === 1)
    ) {
      goBackToTargetPath();
    } else {
      open.value = true;
    }
  };
  onMounted(() => {
    weighQueryResult();
  });
</script>

<style lang="scss" scoped>
.container {
  padding: 46.89rpx 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  box-sizing: border-box;
  background: linear-gradient(
    to bottom,
    rgba(255, 255, 255, 1),
    rgba(242, 243, 245, 1)
  );
  .left-content {
    display: flex;

    .title {
      font-size: 15.24rpx;
      font-weight: 500;
      line-height: 22.27rpx;
      letter-spacing: 0em;
      color: #18191a;
      margin-left: 14.65rpx;
    }
  }
  .content {
    width: 100%;
    height: 100%;
    border-radius: 8rpx;
    overflow: hidden;
    font-size: 14rpx;
    background-color: #fff;
    padding: 4.69rpx 9.38rpx;
    box-sizing: border-box;
    .label {
      color: #6c6e73;
    }
    .operator {
      height: 30.47rpx;
      line-height: 30.47rpx;
      display: flex;
      background-color: #f7f7f7;
      border-radius: 4.69rpx;
      .operator_title {
        width: 124rpx;
        background-color: #e5efff;
        color: #198cff;
        text-align: center;
      }
      .operator_name {
        margin-left: 20rpx;
      }
    }
    .batching {
      height: 175.78rpx;
    }
    .oddments {
      height: 82.03rpx;
    }
    .table_box {
      .status {
        width: 38.09rpx;
        line-height: 16.41rpx;
        text-align: center;
        border-radius: 5rpx;
      }
      .status_1 {
        color: #59bf78;
        background-color: #dcf2eb;
      }
      .status_0 {
        color: #ff9933;
        background-color: #ffecd9;
      }
      .table_btn {
        color: #2871ff;
      }
    }
  }
  .buttons-box {
    height: 63.31rpx;
    line-height: 63.31rpx;
    width: 100%;
    background-color: #ffffff;
    position: fixed;
    bottom: 0;
    left: 0;
    padding: 10.55rpx 9.38rpx 0;
    box-sizing: border-box;
  }
}
</style>
