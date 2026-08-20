<template>
  <BMLayout>
    <BMBasicPage
      :title="t('请验')"
      background-color="#F2F3F5"
      :default-padding="false"
      @left-click="toBack"
    >
      <view class="please-verify-content">
        <scroll-view
          class="scroll-class"
          scroll-y="true"
          refresher-enabled="true"
          :refresher-triggered="triggered"
          :refresher-threshold="100"
          :lower-threshold="70"
          refresher-default-style="white"
          @refresherrefresh="onRefresh"
          @scrolltolower="onScrolltolower"
        >
          <view v-if="pleaseVerifyList.length">
            <wd-row>
              <wd-col v-for="item in pleaseVerifyList" :key="item.id" :span="12">
                <verifyCard
                  :plan-id="queryInfo.planId"
                  :procedure-model-id="queryInfo.procedureModelId"
                  :title="`${item.materialMergeCode}-${item.materialName}`"
                  :status="item.status"
                  :card-data="item"
                />
              </wd-col>
            </wd-row>
            <uv-load-more
              color="#B6B9BF"
              font-size="11.72rpx"
              :status="loadMoreStatus"
              :loading-text="t('正在加载')"
              :loadmore-text="t('加载更多')"
              :nomore-text="t('没有更多了')"
            />
          </view>
          <BmosNoData
            v-else
            :text="t('暂无生产工艺')"
            type="emptyProductionBefore"
          />
        </scroll-view>
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button
              type="success"
              block
              @click="() => finishWorkCommit({ isCoerceComplete: false })"
            >
              {{ t("完成") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              block
              @click="toPleaseVerify"
            >
              {{ t("发起请验") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <!-- 强制完成弹窗 -->
    <BMSignModal
      v-model:show="signBoxShow"
      v-model="signOpenValue"
      :label-list="labelList"
      :title="t('未满足完成条件')"
      :sub-title="
        `${messageContent
        }, ${
          t('是否完成当前步骤/任务？')}`
      "
      :confirm-text="t('完成')"
      :signature-data="curParams"
      @cancel="cancel"
      @confirm="() => finishWorkCommit({ isCoerceComplete: true })"
    />
  </BMLayout>
</template>

<script setup>
import { getInspectPageApi } from '@/api';
import {
  postCompleteExecuteApi,
  postCompleteStepApi,
} from '@/api/webViewApi.js';
import {
  BMBasicPage,
  BMLayout,
  BMSignModal,
} from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import {
  goBackToTargetPath,
  newSignOptionsRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { computed, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { verifyCard } from './components';

const { showNotify } = useNotify();

const params = reactive({
  pageNum: 1,
  pageSize: 20,
});
const total = ref(0);
const triggered = ref(false);
const loadMoreStatus = ref('loadmore');
// 路由参数
const queryInfo = ref({});
// 请验列表
const pleaseVerifyList = ref([]);

const signBoxShow = ref(false);
const messageContent = ref('');
const signOpenValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    signatureAction: 126,
    currentUser: true,
    options: newSignOptionsRef.value,
  },
]);

// 强制完成签名参数
const curParams = computed(() => {
  return {
    processInstanceId: queryInfo.value.processInstanceId,
    productPlanId: queryInfo.value.productPlanId,
    procedureStepModelId: queryInfo.value.procedureStepModelId,
    isCoerceComplete: true,
  };
});

// 返回
const toBack = () => {
  uni.navigateBack();
};

const cancel = () => {
  H5AppNavigateBack();
};

// 完成作业提交
async function finishWorkCommit({ isCoerceComplete = false }) {
  try {
    const params = {
      ...curParams.value,
      isCoerceComplete,
    };
    if (queryInfo.value.taskId) {
      // 从待办任务进入有taskId
      await postCompleteStepApi({
        taskId: queryInfo.value.taskId,
        procedureChangeNumber: queryInfo.value.procedureChangeNumber,
        processChangeNumber: queryInfo.value.processChangeNumber,
        ...params,
      });
    }
    else {
      // 从生产管理 - 工序流程进入有executionId
      const { data } = await postCompleteExecuteApi({
        executionId: queryInfo.value.executionId,
        procedureChangeNumber: queryInfo.value.procedureChangeNumber,
        processChangeNumber: queryInfo.value.processChangeNumber,
        state: queryInfo.value.state,
        ...params,
      });
      if (data) {
        // #ifdef APP-PLUS
        goBackToTargetPath('pages/production/productionManagement/index');
        // #endif
        // #ifdef H5
        H5AppNavigateBack();
        goBackToTargetPath('pages/production/productionManagement/index');
        // #endif
        return;
      }
    }
    showNotify({
      message: t('操作成功'),
      type: 'success',
    });
    // #ifdef APP-PLUS
    uni.navigateBack({ delta: 2 });
    // #endif
    // #ifdef H5
    H5AppNavigateBack();
    uni.navigateBack();
    // #endif
  }
  catch (error) {
    if (error.code === 8212010) {
      messageContent.value = error.message;
      signBoxShow.value = true;
    }
    else {
      showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  }
}

/**
 * 获取请验列表
 *
 */
const getPleaseVerifyList = async () => {
  try {
    const { data } = await getInspectPageApi({
      ...params,
      planId: queryInfo.value.planId,
      procedureModelId: queryInfo.value.procedureModelId,
      procedureStepModelId: queryInfo.value.procedureStepModelId,
    });
    total.value = data.total;
    if (params.pageNum === 1) {
      pleaseVerifyList.value = data.list;
    }
    else {
      pleaseVerifyList.value = pleaseVerifyList.value.concat(data.list);
    }
  }
  catch (e) {
    console.log(e);
    e.message && showNotify({
      type: 'warning',
      message: e.message,
    });
  }
  finally {
    triggered.value = false;
    loadMoreStatus.value
        = total.value >= pleaseVerifyList.value.length ? 'loadmore' : 'nomore';
  }
};

// 下拉刷新触发
const onRefresh = async () => {
  console.log('下拉触发时，triggered状态', triggered.value);
  params.pageNum = 1;
  triggered.value = true;
  getPleaseVerifyList();
};

// 上拉触底
const onScrolltolower = async () => {
  console.log('上拉触底');
  if (
    params.pageNum * params.pageSize < total.value
    && triggered.value === false
  ) {
    params.pageNum++;
    loadMoreStatus.value = 'loading';
    getPleaseVerifyList();
  }
};

// 发起请验
const toPleaseVerify = () => {
  uni.navigateTo({
    url: `/pages/pleaseVerify/initiateVerification/index${queryParams(queryInfo.value)}`,
  });
};

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
});
onShow(async () => {
  params.pageNum = 1;
  await getPleaseVerifyList();
});
</script>

<style lang="scss" scoped>
.please-verify-content {
  padding: 4.69rpx;
  height: 100%;
}
.scroll-class {
  height: 100%;
}
</style>
