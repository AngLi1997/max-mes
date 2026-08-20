<template>
    <BMBasicPage
      :title="t('操作规程')"
      :show-buttons="false"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <view class="rules_list_box">
        <RulesItem v-for="item, index in rulesList" :key="index" :rule-data="item" @click="rulesClick(item)" />
        <BmosNoData v-if="rulesList.length == 0"  type="emptyTodo" :text="t('暂无操作规程')" />
      </view>
    </BMBasicPage>
  </template>
  <script setup>
  import { getOperateVersionDetailsApi } from '@/api';
  import { BMBasicPage } from '@/BMComponents';
  import BmosNoData from '@/components/BmosNoData/index.vue';
  import {
    pageBasicDataRef,
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { t } from '@/utils/useBmosI18n.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import RulesItem from './components/RulesItem.vue';
  import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
  
  const rulesList = ref([]);
  
  // 查询详情
  async function operateVersionDetails(stepModelId) {
    try {
      const { data } = await getOperateVersionDetailsApi({ stepModelId: stepModelId });
      rulesList.value = [...data]
    }
    catch (error) {
      console.log('error:', error);
    }
  }
  function toBack() {
    uni.navigateBack();
  }
  const rulesClick = (data) => {
    uni.navigateTo({
        url: `/pages/webviewPopups/OperatingProcedures/index${queryParams(data)}`
      });
  }
  onLoad(() => {
    if (pageBasicDataRef.value.procedureStepModelId) {
      operateVersionDetails(pageBasicDataRef.value.procedureStepModelId);
    }
  });
  </script>
  <style lang="scss" scoped>
    .content {
      padding-top: 9.38rpx;
      height: 100%;
    }
  
    .rules_list_box{
      display: flex;
      gap: 9.38rpx;
      flex-wrap: wrap;
      width: 100%;
      padding: 9.38rpx 0;
    }
  </style>
  