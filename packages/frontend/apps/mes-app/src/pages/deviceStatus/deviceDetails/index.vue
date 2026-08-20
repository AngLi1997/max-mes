<template>
  <BMLayout>
    <BMBasicPage
      :title="t('设备详情')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="container">
        <view class="status-content">
          <BMInfoDisplay
            v-if="specifics"
            :basic-items="basicItems"
            :info-data="specifics"
            background
          >
            <template #title>
              <view class="title_box">
                <view class="title_label_box">
                  <BMIcon
                    name="shebei"
                    size="18.75rpx"
                    color="#2871FF"
                  />
                  <text class="title">
                    {{ specifics?.name || '' }}
                  </text>
                </view>
                <view>
                  <wd-button v-if="specifics?.status !== 4" type="info" size="small" @click="equipmentAppFault(3)">
                    {{ t('故障') }}
                  </wd-button>
                  <wd-button v-if="specifics?.status === 3" size="small" @click="equipmentAppFault(1)">
                    {{ t('释放') }}
                  </wd-button>
                  <wd-button v-if="specifics?.status === 1" size="small" @click="equipmentAppFault(2)">
                    {{ t('占用') }}
                  </wd-button>
                  <wd-button v-if="specifics?.status === 4" size="small" @click="equipmentAppFault(4)">
                    {{ t('释放') }}
                  </wd-button>
                </view>
              </view>
            </template>
          </BMInfoDisplay>
          <view class="line" />
          <BMInfoDisplay
            v-if="specifics"
            :is-show-title="false"
            :basic-items="[
              {
                label: t('所属产线'),
                field: 'productionLineName',
              },
              {
                label: t('所属房间'),
                field: 'roomName',
              },
              {
                label: t('所属工位'),
                field: 'stationName',
              },
            ]"
            :info-data="specifics"
            background
          />
          <!-- 使用状态 -->
          <view class="function-list">
            <view class="list">
              <view class="list_title">
                <view class="list_header_box">
                  <view class="list_header">
                    {{ t('使用状态') }}
                  </view>
                  <wd-tag plain :type="currentState[specifics?.status]?.hex">
                    {{ currentState[specifics?.status]?.name }}
                  </wd-tag>
                </view>
              </view>
              <view v-if="specifics?.status === 3" class="list_msg">
                {{ `${t('生产批号')}: ${specifics?.batchNo || '-'} | ${t('绑定工位')}: ${specifics?.stationName || '-'}` }}
              </view>
            </view>
            <!-- 状态 -->
            <view
              v-for="(item, index) in specifics?.equipmentStatusAppVOList"
              :key="index"
              class="list"
            >
              <view class="list_title">
                <view class="list_header_box">
                  <view class="list_header">
                    {{ item.name + t(`状态`) }}
                  </view>
                  <view class="list_msg">
                    {{ item.expireDateTime ? `${t('有效期至')} ${item.expireDateTime}` : '' }}
                  </view>
                </view>
                <view class="list_right swiper-state">
                  <ModelSwitch
                    :id="item"
                    :is-show-modal="true"
                    bj_color="transparent"
                    checked_bj_color="#FFFFFF"
                    checked_color="#2871FF"
                    :switch-list="[t('已') + item.name, t('未') + item.name]"
                    :default-switch="item.finishStatus"
                    @change="tabsChange"
                  />
                </view>
              </view>
            </view>
          </view>
        </view>
        <!-- 占用表单 -->
        <view class="form_box">
          <BMModal v-model="showModelFrom" :title="t('占用设备')" size="large" @cancel="showModelFrom = false" @confirm="submitSuccess">
            <BMForm ref="ModelFromRef" v-bind="formProps" />
          </BMModal>
        </view>
        <!-- 签名 -->
        <BMSignModal
          v-model:show="showSign"
          v-model="signValue"
          show-remark
          :signature-data="signatureData"
          :label-list="labelList"
          @confirm="signSubmit"
          @cancel="signCancel"
        />
        <!-- 提示 -->
        <BMMessageBox
          v-model="showMessageBox"
          :title="msgBoxTitle"
          :content="msgContent"
          :confirm-text="t('是')"
          :cancel-text="t('否')"
          @confirm="msgBoxConfirm"
        />
        <!-- 日期选择 -->
        <BMDatePickerModal
          ref="datePickerRef"
          v-model="datePickerValue"
          v-model:open="showDatePicker"
          closable
          :title="t('选择有效期至')"
          format-date="yyyy-MM-dd HH:mm"
          :min-date="Date.now()"
          @close="cancelDatePicker"
          @confirm="submitDate"
        >
          <template #buttons>
            <wd-row :gutter="16">
              <wd-col :span="6">
                <wd-button type="info" block @click="restoreDefault">
                  {{ t('恢复默认') }}
                </wd-button>
              </wd-col>
              <wd-col :span="18">
                <wd-button block @click="datePickerRef.confirm()">
                  {{ t('确定') }}
                </wd-button>
              </wd-col>
            </wd-row>
          </template>
        </BMDatePickerModal>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMDatePickerModal,
  BMForm,
  BMIcon,
  BMInfoDisplay,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMSignModal,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n';
import {
  onLoad,
} from '@dcloudio/uni-app';
import { useMessage } from 'wot-design-uni';
import ModelSwitch from './components/modelSwitch';
import {
  currentState,
} from './enum';
import {
  useColumns,
  useParams,
} from './hooks';

const message = useMessage();
const UseParams = useParams();
const {
  specifics,
  signatureData,
} = UseParams;
const UseColumns = useColumns({
  UseParams,
  message,
});
const {
  labelList,
  IDQuery,
  showDatePicker,
  ModelFromRef,
  showSign,
  signValue,
  showMessageBox,
  msgBoxTitle,
  formProps,
  showModelFrom,
  datePickerValue,
  datePickerRef,
  basicItems,
  msgContent,
  msgBoxConfirm,
  equipmentAppInfo,
  equipmentAppFault,
  tabsChange,
  signSubmit,
  signCancel,
  submitSuccess,
  submitDate,
  cancelDatePicker,
  restoreDefault,
} = UseColumns;
  // 返回
const toBack = () => {
  uni.navigateBack();
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    // console.log(query)
  IDQuery.value = query;
  equipmentAppInfo(query);
  // #endif
  // #ifdef H5
  IDQuery.value = e;
  equipmentAppInfo(e);
  // #endif
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  .line {
    width: 98%;
    margin: auto;
    border-top: 1px solid #e1e3e5;
    margin-bottom: -5.86rpx;
  }
  .form_box {
    :deep(.wd-popup) {
      height: 351.56rpx;
    }
    :deep(.modal-content) {
      height: calc(100% - 99.8rpx);
    }
  }
  // background: transparent;
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

  .status-content {
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
    padding: 7.03rpx 9.38rpx;
    display: flex;
    flex-direction: column;
    position: relative;
    border-top-left-radius: 11.72rpx;
    border-top-right-radius: 11.72rpx;
    background: linear-gradient(180deg, #ecf3fe 0%, #f2f3f5 35%);

    .title_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      text-align: right;
      .title_label_box {
        display: flex;
        align-items: center;
        .title {
          margin-left: 1.17rpx;
          color: #2871ff;
          font-size: 14.06rpx;
        }
      }
      .wd-button {
        margin: 0 0 0 9.38rpx;
      }
    }
    .title-header {
      display: flex;
      flex-direction: column;
      gap: 5.86rpx;
    }

    .right-footer {
      display: flex;
      align-items: center;
      gap: 18.76rpx;
    }
  }

  .function-list {
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    gap: 9.38rpx;

    .list {
      padding: 9.38rpx;
      background-color: #fff;
      border-radius: 4.69rpx;
      .list_title {
        height: 33.98rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .list_header {
          color: #242526;
          font-size: 12.89rpx;
        }
        .list_header_box {
          display: flex;
          align-items: center;
          gap: 18.75rpx;
        }
      }
      .list_msg {
        color: #6c6e73;
        font-size: 11.72rpx;
      }
    }

    .title-header {
      display: flex;
      flex-direction: column;
      gap: 5.86rpx;

      .title {
        display: flex;
        flex-direction: row;
        align-items: center;
        gap: 18.76rpx;
      }
    }

    .title-text {
      flex: none;
      width: auto;
    }

    .title-miam {
      display: flex;
      flex-direction: row;
      align-items: center;
      gap: 18.76rpx;
    }

    .swiper-state {
      display: flex;
      align-items: center;
      padding: 2.34rpx;
      border-radius: 58.62rpx;
      background: #edeff2;
      // box-shadow:
      // inset 0.59rpx 0.59rpx 1.76rpx #3232326e;

      :deep .is-active {
        color: #2871ff;
      }

      :deep .is-middle {
        font-size: 11.72rpx;
        font-style: normal;
        font-weight: 513;
        padding: 5.86rpx 16.41rpx;
      }

      :deep .wd-segmented__item--active {
        border-radius: 58.62rpx;
      }
    }
  }
}
</style>
