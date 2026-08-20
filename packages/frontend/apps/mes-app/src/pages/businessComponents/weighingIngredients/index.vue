<template>
  <BMLayout :loading="loading">
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
            <text class="title">
              {{ t("配料称量") }}
            </text>
          </view>
        </template>
        <template #right>
          <view class="right-content" @click="toResult()">
            {{ t("称量结果") }}
          </view>
        </template>
      </BmosNavBar>
      <view class="content">
        <uv-steps current="0">
          <uv-steps-item :title="t('物料信息')" />
          <uv-steps-item :title="t('模式&设备')" />
          <uv-steps-item :title="t('清零&去皮')" />
          <uv-steps-item :title="t('称量')" />
        </uv-steps>
        <view class="material_msg">
          <view class="material_msg_title">
            <view class="material_msg_title_left">
              <uv-icon name="order" color="#2871FF" size="14.06rpx" />
              <text class="label">
                {{ t("配料单") }}：
              </text>
              {{
                weighingIngredientsData?.ingredientPlanName
                  || selectedIngredients?.name
                  || ""
              }}
            </view>
            <view class="material_msg_title_right" @click="toDetail">
              {{ t("查看详情") }}
              <uv-icon name="arrow-right" color="#797C80" />
            </view>
          </view>
          <view class="material_msg_content">
            <uv-row custom-style="flex:1;">
              <uv-col span="6">
                <view>
                  <text class="label">
                    {{ t("物料信息") }}：
                  </text>
                  <text>
                    {{
                      weighingIngredientsData?.pendingStorageMaterialFullName
                        ? weighingIngredientsData.pendingStorageMaterialFullName
                        : materialInfo
                          ? `${materialInfo.mergeCode}-${materialInfo.materialName}`
                          : ""
                    }}
                  </text>
                </view>
              </uv-col>
              <uv-col span="4">
                <view>
                  <text class="label">
                    {{ t("物料批次") }}：
                  </text>
                  <text>
                    {{
                      weighingIngredientsData?.pendingStorageMaterialBatchNo
                        ? weighingIngredientsData.pendingStorageMaterialBatchNo
                        : materialInfo
                          ? materialInfo.storageMaterialBatchNo
                          : ""
                    }}
                  </text>
                </view>
              </uv-col>
              <uv-col span="2">
                <view class="material_msg_content_btn">
                  <view class="mater" @click="chooseMaterial">
                    {{ t("选择") }}
                  </view>
                </view>
              </uv-col>
            </uv-row>
          </view>
        </view>
        <scroll-view class="table_box" scroll-y="true">
          <view class="demo-uni-row flex-zy">
            <view class="left">
              <view class="tab-box">
                <view class="head">
                  <image class="svg-frame" src="@/static/svg/Frame.svg" />
                  {{ t("统计信息") }}
                </view>
                <view class="content">
                  <view class="rh">
                    <span>{{ t("总件数") }}:</span>
                    {{ tableData.length }}件
                  </view>
                  <view class="rh">
                    <span>{{ t("总量") }}:</span> {{ totalQuantity }}
                  </view>
                  <view class="rh">
                    <span>{{ t("单位") }}:</span> {{ materialInfo?.unit || "" }}
                  </view>
                </view>
              </view>
            </view>
            <view class="right">
              <BMScan
                v-model="materialPartId"
                type="input"
                :placeholder="t('物料件/容器')"
                :allow-types="['01', '02', '04']"
                :error-type-placeholder="t('请扫描物料件或容器标签')"
                @success="onScanSuccess"
                @fail="onScanFail"
                @complete="onScanComplete"
                @confirm="onScanConfirm"
              />
            </view>
          </view>
          <uni-table
            ref="relocationTable"
            class="table-box"
            :empty-text="t('暂无更多数据')"
          >
            <uni-tr class="tr-tab">
              <uni-th
                v-for="(item, index) in tableLabel"
                :key="index"
                :align="item.align"
                class="th-tab"
                :width="item.width"
              >
                {{ item.label }}
              </uni-th>
            </uni-tr>
            <uni-tr v-for="(item, index) in tableData" :key="index">
              <uni-td
                v-for="(sl, ix) in tableLabel"
                :key="ix"
                :width="sl.width"
                :align="sl.align"
              >
                {{ sl.dataIndex !== "BMOSDelete" ? item[sl.dataIndex] : null }}
                <button
                  v-if="sl.dataIndex === 'BMOSDelete'"
                  class="mini-btn"
                  type="primary"
                  size="mini"
                  plain="true"
                  @click="viewDelete(item)"
                >
                  <uni-icons type="close" size="25" color="#FF4C26" />
                </button>
              </uni-td>
            </uni-tr>
          </uni-table>
        </scroll-view>
      </view>
      <view class="buttons-box">
        <uv-row justify="space-between" gutter="10">
          <uv-col span="6">
            <BmosButton type="default" :text="t('取消')" @click="toBack" />
          </uv-col>
          <uv-col span="6">
            <BmosButton type="primary" :text="t('下一步')" @click="submit" />
          </uv-col>
        </uv-row>
      </view>

      <!-- 选择配料单 -->
      <BmosSelect
        ref="ingredientsSelect"
        :options-list="ingredientsOptions"
        :title="t('配料单选择')"
        :field-names="{ label: 'name' }"
        :placeholder="t('配料单')"
        required
        @confirm="ingredientsConfirm"
        @cancel="ingredientsCancel"
      />

      <!-- 选则称量物料 -->
      <MaterialPopup
        v-model="showMaterialPopup"
        :title="t('选择称量物料')"
        :options="ingredientsDetails?.batchList || []"
        :selected-id="materialInfo?.storageMaterialBatchId || ''"
        @confirm="materialPopupConfirm"
      />
      <!-- 签名组件 -->
      <BMSignModal
        v-model:show="signOpen"
        v-model="signValue"
        :title="t('称量人员确认')"
        :label-list="labelList"
        :signature-data="signatureData"
        :field-names="{
          value: 'loginName',
          label: 'userName',
          id: 'userId',
        }"
        show-remark
        @confirm="signConfirm"
      />
      <BMMessageBox
        v-model="isOpenMessage"
        :title="t('提示')"
        :content="t('是否移除当前物料？')"
        @cancel="viewDeleteCancel"
        @confirm="viewDeleteConfirm"
      />
    </view>
  </BMLayout>
</template>

<script setup>
import { BMLayout, BMMessageBox, BMScan, BMSignModal } from '@/BMComponents/index.js';
import BmosButton from '@/components/BmosButton/index.vue';
import BmosNavBar from '@/components/BmosNavBar/index.vue';
import BmosSelect from '@/components/BmosSelect/index.vue';
import { goBackToTargetPath } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { parseUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useMessage, useToast } from 'wot-design-uni';

import MaterialPopup from './components/materialPopup.vue';
import { usePageInfo } from './hooks/index.js';

const toast = useToast();
const message = useMessage();

const isOpenMessage = ref(false);
const deleteRow = ref(null);

const {
  loading,
  query,
  tableLabel,
  tableData,
  ingredientsSelect,
  ingredientsOptions,
  selectedIngredients,
  ingredientsDetails,
  showMaterialPopup,
  materialInfo,
  totalQuantity,
  signOpen,
  signValue,
  signatureData,
  weighingIngredientsData,
  reCheckerList,
  ingredientsConfirm,
  ingredientsCancel,
  materialPopupConfirm,
  searchMaterialCode,
  signConfirm,
  chooseMaterial,
  goModeDevice,
} = usePageInfo({
  message,
  toast,
});

// 物料件号
const materialPartId = ref('');

const labelList = ref([
  {
    label: t('称量人'),
    signatureAction: 43,
    disabled: true,
  },
  {
    label: t('复核人'),
    signatureAction: 44,
    options: reCheckerList,
  },
]);

// 返回
const toBack = () => {
  goBackToTargetPath();
};

const toDetail = () => {
  uni.navigateTo({
    url: `/pages/businessComponents/weighingIngredients/ingredientsDetails/index?componentId=${query.value.componentId}`,
  });
};
  // 跳转称量结果页
const toResult = () => {
  console.log('query.value', query.value);
  uni.navigateTo({
    url: `/pages/businessComponents/weighingIngredients/weighingResults/index?componentId=${query.value.componentId}`,
  });
};
const confirmPart = (no) => {
  if (!no) {
    return;
  }
  if (!materialInfo.value) {
    toast.show(t('请先选择物料信息'));
    materialPartId.value = '';
    return;
  }
  searchMaterialCode({
    ingredientPlanId: selectedIngredients?.value.id,
    materialBatchId: materialInfo.value.storageMaterialBatchId,
    no,
    unitId: materialInfo.value.unitId,
  });
};

const onScanSuccess = (code) => {
  confirmPart(code);
};
const onScanConfirm = (code) => {
  confirmPart(code);
};
const onScanFail = () => {
  showNotify({
    type: 'danger',
    message: t('扫描失败'),
  });
};
const onScanComplete = (result) => {
  console.log('onScanComplete', result);
};

// 删除
const viewDelete = (data) => {
  deleteRow.value = data;
  isOpenMessage.value = true;
};

const viewDeleteCancel = () => {
  deleteRow.value = null;
  isOpenMessage.value = false;
};

const viewDeleteConfirm = () => {
  tableData.value = tableData.value.filter(item => item.id !== deleteRow.value.id);
};

// 下一步
const submit = () => {
  if (!materialInfo.value) {
    toast.show(t('请选择物料批次'));
    return;
  }
  if (
    weighingIngredientsData.value
    && weighingIngredientsData.value.pendingStorageMaterialBatchId
    && tableData.value.length === 0
  ) {
    materialPartId.value = '';
    goModeDevice();
    return;
  }
  if (tableData.value.length === 0) {
    toast.show(t('请添加物料件'));
    return;
  }
  signValue.value = {
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
    remark: '',
  };
  // 第一次需要确认称量人
  if (!weighingIngredientsData.value) {
    signOpen.value = true;
    return;
  }
  signConfirm();
};

onLoad((e) => {
  query.value = parseUrlQuery(e);
});
</script>

<style lang="scss" scoped>
.container {
  padding-top: 46.89rpx;
  width: 100%;
  overflow: hidden;
  box-sizing: border-box;
  background: #ffffff;

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

  & > .content {
    width: 100%;
    padding: 5.86rpx 9.38rpx;
    box-sizing: border-box;
    font-size: 14rpx;
    font-weight: normal;
    background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(234, 235, 236, 1));

    .material_msg {
      width: 100%;
      margin: 4.69rpx auto;
      border-radius: 4.69rpx;
      min-height: 80.86rpx;

      .label {
        color: #6c6e73;
      }

      .material_msg_title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 33.98rpx;
        padding: 0 16rpx;
        background: linear-gradient(to bottom, rgba(229, 239, 255, 1), rgba(229, 239, 255, 0));

        .material_msg_title_left {
          display: flex;
          align-items: center;

          .label {
            margin-left: 10rpx;
          }
        }

        .material_msg_title_right {
          color: #2871ff;
          display: flex;
          align-items: center;
        }
      }

      .material_msg_content {
        min-height: 46.88rpx;
        line-height: 1.2;
        padding: 0 9.38rpx;
        box-sizing: border-box;
        background-color: #ffffff;
        display: flex;
        align-items: center;

        .material_msg_content_btn {
          display: flex;
          justify-content: flex-end;

          view {
            width: 60rpx;
            height: 30rpx;
            line-height: 30rpx;
            border-radius: 30rpx;
            background-color: #2871ff;
            color: #fff;
            text-align: center;
          }
        }
      }
    }

    .table_box {
      height: 230.86rpx;
      padding: 9.38rpx;
      box-sizing: border-box;
      width: 100%;
      margin: auto;
      background-color: #fff;
      border-radius: 4.69rpx;
    }

    .demo-uni-row {
      display: flex;
      width: 100%;
      padding: 9.38rpx 0;

      .left {
        width: 100%;
        display: flex;
        align-items: center;

        .tab-box {
          display: flex;
          align-items: center;
          background: #f2f3f5;
          height: 40rpx;

          .head {
            height: 40rpx;
            box-sizing: border-box;
            display: flex;
            padding: 7.03rpx;
            justify-content: center;
            align-items: center;
            border-radius: 4.69rpx;
            background: #e5efff;
            color: #198cff;
            font-size: 11.72rpx;
            font-style: normal;
            font-weight: 400;
            margin-right: 18.76rpx;
            white-space: nowrap;

            .svg-frame {
              width: 11.72rpx;
              height: 11.72rpx;
              margin-right: 4.69rpx;
            }
          }

          .content {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            column-gap: 32.83rpx;

            .rh {
              color: var(---, #242526);
              font-size: 11.72rpx;
              font-style: normal;
              font-weight: 513;

              span {
                color: var(---, #6c6e73);
              }
            }
          }
        }
      }

      .right {
        border-radius: 4.69rpx;
        background: #f7f8fa;
        padding-right: 9.38rpx;
        align-items: center;
        display: flex;

        .custom-input {
          flex: 1;
          border: none;
          :deep(.wd-input__body) {
            height: 100%;
            padding: 7.03rpx 9.38rpx;
            box-sizing: border-box;
            border-bottom: none;
            background-color: #f7f8fa;
            .wd-input__value {
              height: 100%;
            }
          }
        }
        .scan-icon-box {
          width: 31.64rpx;
          height: 21.09rpx;
          display: flex;
          align-items: center;
          border-left: 1px solid #e1e3e5;
          padding-left: 9.38rpx;
          box-sizing: border-box;
        }
      }
    }

    .table-box {
      width: 100%;
      height: calc(100% - 65.92rpx);

      :deep .uni-table {
        height: 100%;
        display: flex;
        flex-direction: column;

        .uni-table-tr {
          border-bottom: 1.17rpx #ebeef5 solid;
        }

        .tr-tab {
          background-color: #fafafa;

          .th-tab {
            padding: 7.03rpx 9.38rpx;
            font-size: 11.72rpx;
            font-style: normal;
            border: 0;
            font-weight: 513;
            /* 次要文字20px */
            font-family: '思源黑体 CN';
            color: var(---, #606266);
          }
        }

        .uni-table-td {
          word-wrap: break-word;
          padding: 7.03rpx 9.38rpx;
          border: 0;
          color: var(---, #242526);
          /* 次要文字20px */
          font-family: '思源黑体 CN';
          font-size: 11.72rpx;
          font-style: normal;
          font-weight: 513;
          word-break: break-word;

          .mini-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            border: 0;
            padding: 0;
            font-size: 11.72rpx;
            font-style: normal;
            font-weight: 513;
            line-height: normal;
          }
        }

        .uni-table-loading {
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }

  .form-content {
    width: 100%;
    display: grid;
    grid-template-columns: repeat(1, 1fr);
    column-gap: 14.07rpx;

    .col-box {
      padding: 12.9rpx 9.38rpx;
      align-items: center;
      border-radius: 7.03rpx;
      background: #f7f8fa;

      .box-location {
        display: grid;
        grid-template-columns: calc(100% - 14.07rpx - 19.34rpx - 21.1rpx) 21.1rpx;
        column-gap: 19.34rpx;
        align-items: center;
        justify-content: flex-start;
        width: 100%;
        height: 100%;

        .location-data {
          position: relative;
          color: var(---, #6c6e73);
          font-size: 12.9rpx;
          font-style: normal;
          font-weight: 513;
          display: flex;
          align-items: center;
          justify-content: space-between;

          &::after {
            content: '';
            position: absolute;
            right: -8.79rpx;
            top: 0;
            width: 1.17rpx;
            height: 100%;
            background-color: #e1e3e5;
          }
        }

        .location-ts {
          position: relative;
          color: var(---, #c2c5cc);
          font-size: 12.9rpx;
          font-style: normal;
          font-weight: 513;
          display: flex;
          align-items: center;
          justify-content: space-between;

          &::after {
            content: '';
            position: absolute;
            right: -8.79rpx;
            top: 0;
            width: 1.17rpx;
            height: 100%;
            background-color: #e1e3e5;
          }
        }
      }

      :deep .uni-forms-item {
        margin-bottom: 0px;

        .uni-forms-item__label {
          color: var(---, #6c6e73);
          font-size: 12.9rpx;
          font-style: normal;
          font-weight: 513;
        }

        .is-input-border {
          border: 0;
          background-color: transparent !important;

          .uni-easyinput__content-input {
            color: var(---, #6c6e73);
            font-size: 12.9rpx;
            font-style: normal;
            font-weight: 513;

            .uni-input-placeholder {
              color: var(---, #c2c5cc);
              font-size: 12.9rpx;
              font-style: normal;
              font-weight: 400;
            }
          }

          .uni-icons {
            font-size: 14.07rpx !important;
            color: var(---, #797c80) !important;
          }
        }
      }
    }

    .dark {
      background-color: #d3dce6;
    }
  }

  .flex-zy {
    justify-content: space-between;
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
