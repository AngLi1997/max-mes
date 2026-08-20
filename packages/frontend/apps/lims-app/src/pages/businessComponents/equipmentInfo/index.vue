<template>
  <BMLayout>
    <BMBasicPage
      :title="t('设备信息详情')"
      background-color="#F2F3F5"
      @left-click="toBack"
      @confirm="confirm"
      @cancel="toBack"
    >
      <view class="equipment-info">
        <view class="name">
          <wd-icon
            class-prefix="bmos-icon"
            name="fuwuqi"
            size="18.75rpx"
            color="#2871FF"
          />
          <text>{{ equipmentStatusDetail.name }}</text>
        </view>
        <view class="info-details">
          <view
            v-for="field in detailsFields"
            :key="field.key"
            class="details-item"
          >
            <view class="label">{{ field.label }}:</view>
            <text class="value">{{ field.key }}</text>
          </view>
        </view>
      </view>
      <view class="equipment-use">
        <view class="title">{{ t("设备信息") }}</view>
        <BMTable
          ref="tableInfoRef"
          v-bind="tableProps"
        />
      </view>
      <view class="equipment-use">
        <view class="title">{{ t("设备使用") }}</view>
        <BMTable
          ref="tableUseRef"
          v-bind="tableUseProps"
        />
      </view>
      <view class="equipment-use equipment-status">
        <view class="title">{{ t("设备状态") }}</view>
        <BMTable
          ref="tableStatusRef"
          v-bind="tableStatusProps"
        />
      </view>
      <wd-form ref="remarkForm" :model="remarkModel">
        <wd-input
          v-if="queryInfo.isUpdate == 2"
          v-model="remarkModel.remark"
          label-width="13%"
          :placeholder="t('请输入修改原因')"
          :label="t('修改原因')"
          required
          prop="remark"
          :rules="[{ required: true, message: '请输入修改原因' }]"
        />
      </wd-form>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { BMLayout, BMBasicPage, BMTable } from '@/BMComponents';
  import { useDetailsInfo } from './hooks/index.js';
  import { ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import { getCurrentCopyRecordItem, urlQueryRef, pageBasicDataRef,
           initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { reqMesEquipmentEquipmentComponentInfoApi, reqUpdateMesEquipmentEquipmentComponentInfoApi } from '@/api';
  import { useNotify } from 'wot-design-uni';
  const { showNotify } = useNotify();

  const queryData = ref();
  const { equipmentStatusDetail, getDetail, detailsFields, queryInfo, tableProps, tableUseProps, tableStatusProps, tableInfoRef, tableUseRef, tableStatusRef } =
    useDetailsInfo(queryData);
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };

  const remarkModel = ref({
    remark: ''
  });
  const remarkForm = ref();

  const confirm = async() => {
    try {
      const { valid } = await remarkForm.value.validate();
      if (!valid) {
        showNotify({ type: 'warning', message: t('请输入修改原因') });
        return;
      }
      const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
      const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
      const { version } = getCurrentCopyRecordItem();
      const data = {
        batchNo,
        componentId: queryInfo.value.id,
        copyVersion: version,
        equipmentId: queryInfo.value.equipmentId,
        procedureStepId,
        procedureStepModelId,
        processId,
        processVersion,
        productPlanId,
        recordItemId,
        recordVersionId,
        reuse: reusable
      };
      if (parseInt(queryInfo.value.isUpdate) === 2) {
        await reqUpdateMesEquipmentEquipmentComponentInfoApi({
          ...data,
          remark: remarkModel.value.remark
        });
      } else {
        await reqMesEquipmentEquipmentComponentInfoApi(data);
      }
      initFillData2();
      if (queryInfo.value.returnData === 1) {
        uni.navigateBack();
      } else {
        uni.navigateBack({
          delta: 2
        });
      }
    } catch (error) {
      error.message && uni.showToast({
        title: error.message,
        icon: 'error',
        duration: 2000,
        mask: true
      });
    }
  };

  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    getDetail(query);
    queryData.value = query;
    // #endif
    // #ifdef H5
    getDetail(e);
    queryData.value = e;
    // #endif
  });
</script>

<style lang="scss" scoped>
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

  .right-content {
    font-size: 15.24rpx;
    color: #2871ff;
  }
    .equipment-info {
      margin-top: 9.38rpx;
      width: 100%;
      // height: 96rpx;
      border-radius: 4.69rpx;
      background: #ffffff;
      padding: 9.38rpx;
      box-sizing: border-box;
      overflow: hidden;

      .name {
        display: flex;
        align-items: center;
        font-weight: 500;
        font-size: 14.06rpx;
        color: #2871ff;
        gap: 4.69rpx;
      }
      .info-details {
        display: flex;
        flex-wrap: wrap;
        font-size: 11.72rpx;
        margin-top: 11.72rpx;
        row-gap: 11.72rpx;
        .details-item {
          width: 50%;
          // height: 14.06rpx;
          display: flex;
          .label {
            color: #6c6e73;
            min-width: 58.59rpx;
          }
          .value {
            color: #242526;
          }
        }
      }
    }
    .equipment-use {
      width: 100%;
      border-radius: 4.69rpx;
      background: #ffffff;
      padding: 9.38rpx;
      box-sizing: border-box;
      margin-top: 9.38rpx;
      .title {
        font-size: 14.06rpx;
        color: #18191a;
        margin-bottom: 9.38rpx;
      }
      :deep(.wd-table__cell){
        border-bottom: 1px solid #E1E3E5;
      }
    }
    .equipment-status {
      height: unset;
      min-height: 110.16rpx;
    }
  .col-but {
    padding: 11.72rpx;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    column-gap: 14.07rpx;
    margin: 5rpx auto 0;
    background-color: #fff;
    position: fixed;
    bottom: 0;
    width: 100%;
    box-sizing: border-box;

    .but {
      width: 100%;
      padding: 2.38rpx 14.07rpx;
    }

    .cancel {
      color: var(---, #6c6e73);
      border: 1.17rpx solid var(----, #bbbdbf);
    }
  }
</style>
