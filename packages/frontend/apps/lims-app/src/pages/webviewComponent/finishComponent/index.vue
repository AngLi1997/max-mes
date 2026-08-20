<template>
  <BMLayout>
    <BMMessageBox
      v-model="show"
      :title="t('完成作业')"
      :content="
        (isFilled
          ? t('是否完成当前步骤')
          : t('记录内容未录入完成,是否完成当前步骤')) + '？'
      "
      :close-on-click-modal="false"
    >
      <template v-if="isFilled" #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="info" block @click="cancel">
              {{ t("否") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              :disabled="showSuccessTime != 0"
              block
              @click="finishWorkCommit({ isCoerceComplete: false })"
            >
              {{ t("是") + (showSuccessTime ? `(${showSuccessTime})` : "") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
      <template v-else #buttons>
        <wd-row style="margin-bottom: 9.38rpx;">
          <wd-col :span="24">
            <wd-button type="info" block @click="batchHandleClick">
              {{ t("批量录入空值") }}
            </wd-button>
          </wd-col>
        </wd-row>
        <wd-row>
          <wd-col :span="12">
            <wd-button
              type="info"
              style="margin-right: 4.69rpx;"
              block
              @click="cancel"
            >
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              block
              style="margin-left: 4.69rpx;"
              :disabled="showSuccessTime != 0"
              @click="finishWorkCommit({ isCoerceComplete: false })"
            >
              {{ t("完成") + (showSuccessTime ? `(${showSuccessTime})` : "") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMMessageBox>
    <!-- 强制完成弹窗 -->
    <BMSignModal
      v-model:show="signBoxShow"
      v-model="signOpenValue"
      :label-list="labelList"
      :title="t('未满足完成条件')"
      :sub-title="
        messageContent +
          ', ' +
          t('是否完成当前步骤/任务？')
      "
      :confirm-text="t('完成')"
      :signature-data="curParams"
      @cancel="cancel"
      @confirm="finishWorkCommit({ isCoerceComplete: true })"
    />
  </BMLayout>
</template>
<script setup>
  import { BMMessageBox, BMSignModal, BMLayout } from '@/BMComponents';
  import { t } from '@/utils/useBmosI18n.js';
  import { ref, onMounted, computed } from 'vue';
  import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
  import {
    setEmptyValueForComponents,
    newSignOptionsRef,
    urlQueryRef,
    pageBasicDataRef,
    goBackToTargetPath
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import {
    postCompleteStepApi,
    postCompleteExecuteApi
  } from '@/api/webViewApi.js';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import { useNotify } from 'wot-design-uni';

  const { showNotify } = useNotify();
  const isFilled = ref(true);
  const show = ref(true);
  useSubNvueLinster('page-finishComponent', (data) => {
    isFilled.value = data.isFilled;
  });

  const showSuccessTime = ref(3);
  const signBoxShow = ref(false);
  const signOpenValue = ref({
    loginName1: '',
    password1: '',
    userId1: ''
  });
  const labelList = ref([
    {
      label: t('操作人'),
      signatureAction: 126,
      currentUser: true,
      options: newSignOptionsRef.value
    }
  ]);
  const messageContent = ref('');

  // 强制完成签名参数
  const curParams = computed(() => {
    return {
      processInstanceId: urlQueryRef.value.processInstanceId,
      productPlanId: urlQueryRef.value.productPlanId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      isCoerceComplete: true
    };
  });
  onMounted(() => {
    const timer = setInterval(() => {
      showSuccessTime.value--;
      if (showSuccessTime.value === 0) {
        clearInterval(timer);
      }
    }, 1000);
  });

  const batchHandleClick = () => {
    setEmptyValueForComponents();
    H5AppNavigateBack();
  };
  const cancel = () => {
    H5AppNavigateBack();
  };

  // 完成作业提交
  async function finishWorkCommit({ isCoerceComplete = false }) {
    try {
      const params = {
        ...curParams.value,
        isCoerceComplete
      };
      if (urlQueryRef.value.taskId) {
        // 从待办任务进入有taskId
        await postCompleteStepApi({
          taskId: urlQueryRef.value.taskId,
          procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
          processChangeNumber: urlQueryRef.value.processChangeNumber,
          ...params
        });
      } else {
        // 从生产管理 - 工序流程进入有executionId
        const { data } = await postCompleteExecuteApi({
          executionId: urlQueryRef.value.executionId,
          procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
          processChangeNumber: urlQueryRef.value.processChangeNumber,
          state: urlQueryRef.value.state,
          ...params
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
        type: 'success'
      });
      // #ifdef APP-PLUS
      uni.navigateBack({ delta: 2 });
      // #endif
      // #ifdef H5
      H5AppNavigateBack();
      uni.navigateBack();
      // #endif
    } catch (error) {
      if (error.code === 8212010) {
        messageContent.value = error.message;
        show.value = false;
        signBoxShow.value = true;
      } else {
        showNotify({
          type: 'warning',
          message: error.message
        });
      }
    }
  }
</script>

<style>
page {
  background: transparent;
}
</style>

<style lang="scss" scoped></style>
