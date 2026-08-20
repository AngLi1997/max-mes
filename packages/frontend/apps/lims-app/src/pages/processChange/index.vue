<template>
  <BMLayout>
    <BMBasicPage
      :title="type == 3 ? t('工序换班') : t('工艺换班')"
      background-color="#F2F3F5"
      :default-padding="false"
      @left-click="toBack"
    >
      <view class="content">
        <scroll-view
          v-if="type == 4"
          scroll-x="auto"
          class="tag_box_static"
        >
          <view class="tag_box">
            <view
              v-for="item in stepList"
              :key="item.id"
              :class="['item_tag',clickStepData.id == item.id ? 'click_item_tag':'']"
              @click="stepItemClick(item)"
            >
              {{ item.procedureModelName }}
            </view>
          </view>
        </scroll-view>
        <view class="info_box">
          <BMInfoDisplay
            :title="t('工序信息')"
            icon="gongxu2"
            :basic-items="[
              {
                label: t('工序名称'),
                field: 'name',
              },
              {
                label: t('工序阶段编码'),
                field: 'code',
              },
            ]"
            :info-data="topInfoData"
          />
        </view>
        <view
          class="btn_box"
          :style="`top: ${type != 4 ? 0 : '48.06rpx'};`"
        >
          <view class="btn_left_title">{{ t("班组配置") }}</view>
          <view>
            <wd-button size="small" @click="showChangeTeamModal = true">
              {{
                t("工序换班")
              }}
            </wd-button>
          </view>
        </view>
        <view class="table_box_padding">
          <view class="table_box">
            <BMTable
              ref="tableRef"
              v-bind="tableProps"
              :data="tableData"
            />
          </view>
        </view>
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="6">
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
              @click="noChange"
            >
              {{ t("无需换班") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              block
              @click="changeTeams"
            >
              {{ t("确定换班") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <BMCheckboxModal
      v-model="orderTeamValue"
      v-model:open="showChangeTeamModal"
      :options="orderTeamOption"
      :title="t('工序换班')"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      @confirm="allTeamChange"
    />
    <!-- 无需换班 -->
    <BMMessageBox
      v-model="showNochangeModal"
      :title="t('提示')"
      :content="t('是否无需换班')"
    >
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button
              type="info"
              block
              @click="showNochangeModal = false"
            >
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              :disabled="noChangeDisabled"
              block
              @click="noChangeSubmit"
            >
              {{ `${t("确定")}${timeOut ? '(' +timeOut + ')' : ''}` }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMMessageBox>
  </BMLayout>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import {
    BMBasicPage,
    BMInfoDisplay,
    BMTable,
    BMCheckboxModal,
    BMMessageBox,
    BMLayout
  } from '@/BMComponents';
  import { onLoad } from '@dcloudio/uni-app';
  import { ref } from 'vue';
  import { useTable } from './hooks/useTable';
  import { noChangeTeamExecutionApi, noChangeTeamTaskApi } from '@/api/productionApi.js';

  const queryInfo = ref({});
  const type = ref();
  const timeOut = ref(3);
  const noChangeDisabled = ref(true);
  const showNochangeModal = ref(false);
  const noChangeTime = ref();
  const {
    tableRef,
    tableProps,
    showChangeTeamModal,
    orderTeamValue,
    orderTeamOption,
    stepList,
    clickStepData,
    topInfoData,
    tableData,
    stepItemClick,
    allTeamChange,
    changeTeams
  } = useTable(queryInfo);

  const noChange = () => {
    clearInterval(noChangeTime.value);
    showNochangeModal.value = true;
    noChangeDisabled.value = true;
    timeOut.value = 3;
    noChangeTime.value = setInterval(() => {
      timeOut.value--;
      if (timeOut.value === 0) {
        noChangeDisabled.value = false;
        timeOut.value = '';
        clearInterval(noChangeTime.value);
      }
    }, 1000);
  };
  const noChangeSubmit = async() => {
    // 生产管理无需换班
    try {
      if (queryInfo.value.isToDo) {
        // 待办任务无需换班
        const params = {
          taskId: queryInfo.value.taskId,
          procedureChangeNumber: queryInfo.value.procedureChangeNumber,
          procedureStepModelId: queryInfo.value.procedureStepModelId,
          processChangeNumber: queryInfo.value.processChangeNumber,
          processInstanceId: queryInfo.value.processInstanceId,
          productPlanId: queryInfo.value.planId
        };
        await noChangeTeamTaskApi(params);
      } else {
        const params = {
          executionId: queryInfo.value.executionId,
          procedureChangeNumber: queryInfo.value.procedureChangeNumber,
          procedureStepModelId: queryInfo.value.procedureStepModelId,
          processChangeNumber: queryInfo.value.processChangeNumber,
          processInstanceId: queryInfo.value.processInstanceId,
          productPlanId: queryInfo.value.productPlanId,
          state: queryInfo.value.state
        };
        await noChangeTeamExecutionApi(params);
      }
      showNochangeModal.value = false;
      toBack();
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'error',
          duration: 2000,
          mask: true
        });
    }
  };
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map((key) => [
        decodeURIComponent(key),
        decodeURIComponent(e[key])
      ])
    );
    queryInfo.value = query;
    // #endif
    // #ifdef H5
    queryInfo.value = e;
    // #endif
    type.value = queryInfo.value.nodeFunction;
  });
</script>
<style lang="scss" scoped>
  .content {
    padding-bottom: 2.38rpx;
    .tag_box_static{
      position: -webkit-sticky; /* Safari */
      position: sticky;
      top: 0; /* 设置距离页面顶部的距离 */
      z-index: 10; /* 确保sticky定位的元素在最上方 */
    }
    .tag_box {
      padding: 9.38rpx;
      background-color: #fff;
      width: max-content;
      min-width: 100%;
      display: flex;
      .item_tag {
        flex-shrink: 0;
        height: 29.3rpx;
        line-height: 29.3rpx;
        background-color: #f2f3f5;
        color: #6c6e73;
        text-align: center;
        padding: 0 11.72rpx;
        width: max-content;
        border-radius: 4.69rpx;
        box-sizing: border-box;
        margin-right: 9.38rpx;
        border: .59rpx solid #f2f3f5;
        font-size: 11.72rpx;
      }
      .click_item_tag {
        border: .59rpx solid #2871FF;
        background-color: #EBF2FF;
        color: #2871FF;
      }
    }
    .info_box {
      margin-top: 9.38rpx;
      padding: 0 9.38rpx;
    }
    .btn_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background-color: #f2f3f5;
      padding: 9.38rpx;
      position: -webkit-sticky; /* Safari */
      position: sticky;
      z-index: 10; /* 确保sticky定位的元素在最上方 */
      .btn_left_title{
        font-size: 12.89rpx;
      }
    }
    .table_box_padding {
      padding: 0 9.38rpx;
      .table_box {
        padding: 9.38rpx;
        background-color: #fff;
        border-radius: 4.69rpx;
        margin: 0 auto 9.38rpx;
        :deep(.wd-input) {
          border: 0;
        }
      }
    }
  }
</style>
