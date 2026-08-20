<template>
  <BMLayout>
    <BMBasicPage
      :title="t('房间清场执行')"
      :confirm-text="t('完成')"
      background-color="#F2F3F5"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <view class="container">
        <scroll-view class="room-content" scroll-y="true">
          <BMInfoDisplay
            :title="t('清场房间信息')"
            icon="fangjian"
            :basic-items="[
              {
                label: t('房间名称'),
                field: 'name',
              },
              {
                label: t('房间编码'),
                field: 'code',
              },
            ]"
            :info-data="paramsData"
          />
          <wd-card custom-class="room-content-card">
            <BMForm
              ref="roomFormsRef"
              v-bind="formProps"
            />
          </wd-card>
        </scroll-view>
        <BMSignModal
          v-model:show="signOpen"
          v-model="signValue"
          :title="t('房间状态修改签名')"
          :signature-data="signatureParams"
          :label-list="labelList"
          :field-names="{
            value: 'loginName',
            label: 'userName',
            id: 'userId',
          }"
          @confirm="submitSign"
        />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMForm, BMInfoDisplay, BMLayout, BMSignModal } from '@/BMComponents/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useForm, useParams } from './hooks';

const UseParams = useParams();
const { signOpen, labelList, paramsData } = UseParams;
const { roomFormsRef, signValue, formProps, submit, submitSign, getRoomAuthUser } = useForm({ UseParams });
const signatureParams = ref({});
const toBack = () => {
  uni.navigateBack();
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  paramsData.value = query;
  // #endif
  // #ifdef H5
  paramsData.value = e;
  // #endif
  getRoomAuthUser();
  signatureParams.value = {
    status: 3,
    id: paramsData.value.id,
    procedureId: paramsData.value.procedureId,
    productId: paramsData.value.productId,
    batchNo: paramsData.value.batchNo,
  };
});
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  padding-top: 9.38rpx;
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  :deep(.form-title) {
    color: #000;
  }
  :deep(.wd-card__title-content) {
    padding: 0;
  }
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

  .room-content {
    :deep .uni-scroll-view-content {
      display: flex;
      flex-direction: column;
      overflow-y: scroll;
    }

    :deep .room-content-card {
      margin: 9.38rpx 0;

      .wd-card__title {
        color: var(---, #242526);
        font-size: 14.06rpx;
        font-style: normal;
        font-weight: 513;
      }

      .room-content-card-text {
        flex: 1 0 0;
        color: var(---, #242526);
        font-size: 11.72rpx;
        font-style: normal;
        font-weight: 513;

        span {
          color: var(---, #6c6e73);
        }
      }

      .room-content-form {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        column-gap: 18.76rpx;

        .uni-forms-item__label {
          display: none;
        }

        .input-view {
          padding: 8.79rpx;
          border: 0.59rpx solid #c8c7cc;
          border-radius: 0.11rem;
          display: flex;
          flex-direction: row;
          align-items: center;
          justify-content: space-between;
        }
      }
    }
  }

  .complete-but {
    position: fixed;
    bottom: 0;
    left: 0;
    width: calc(100% - 18.76rpx);
    display: grid;
    align-items: center;
    grid-template-columns: repeat(2, 1fr);
    column-gap: 14.07rpx;
    padding: 9.38rpx 9.38rpx;
    background: #fff;

    :deep .uv-button {
      padding: 9.38rpx 14.07rpx;
      height: 42.2rpx;
    }
  }
}
</style>
