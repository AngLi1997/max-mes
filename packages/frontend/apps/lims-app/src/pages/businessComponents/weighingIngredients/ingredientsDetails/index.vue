<template>
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
          <text class="title">{{ t("配料单详情") }}</text>
        </view>
      </template>
    </BmosNavBar>
    <view class="content">
      <view class="content_title">
        <view class="material_msg_title_left">
          <uv-icon name="order" color="#2871FF" />
          <text class="label"> {{ t("配料单") }}： </text>
          {{ ingredientsDetails.name }}
        </view>
        <view class="material_msg_title_right" @click="openSelect()">
          {{ t("选择") }}
          <uv-icon name="arrow-right" color="#797C80" />
        </view>
      </view>
      <view class="table_box">
        <uni-table ref="relocationTable" :empty-text="t('暂无更多数据')">
          <uni-tr class="tr-tab">
            <uni-th
              v-for="(item, index) in tableConfig"
              :key="index"
              :align="item.align || 'left'"
              class="th-tab"
              :width="item.width"
            >
              {{ item.label }}
            </uni-th>
          </uni-tr>
          <uni-tr
            v-for="(item, index) in ingredientsDetails.batchList || []"
            :key="index"
          >
            <uni-td
              v-for="(sl, ix) in tableConfig"
              :key="ix"
              :width="sl.width"
              :align="sl.align"
            >
              <view
                v-if="sl.filed == 'weighStatus'"
                :class="`status status_${item[sl.filed].value}`"
              >
                {{ item[sl.filed].name }}
              </view>
              <template v-else>{{ item[sl.filed] }}</template>
            </uni-td>
          </uni-tr>
        </uni-table>
      </view>
      <view class="col-but">
        <button class="but cancel" type="default" plain="true" @click="toBack">
          {{ t("取消") }}
        </button>
        <button class="but" type="primary" @click="submit">
          {{ t("确定") }}
        </button>
      </view>
    </view>
    <BmosSelect
      ref="bmosSelect"
      :options-list="ingredientsOptions"
      :field-names="{ label: 'name' }"
      :title="t('配料单选择')"
      :placeholder="t('配料单')"
      :selected-id="selectedIngredients?.id"
      required
      @confirm="ingredientsChange"
    />
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { onMounted, ref } from 'vue';
  import BmosNavBar from '@/components/BmosNavBar/index.vue';
  import BmosSelect from '@/components/BmosSelect/index.vue';
  import { useTable } from './hooks/useTable';
  import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
  import { storeToRefs } from 'pinia';
  import { pageBasicDataRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';

  const { tableConfig } = useTable();
  const weighingIngredientsStore = useWeighingIngredientsStore();
  const { ingredientsOptions, selectedIngredients, ingredientsDetails } =
    storeToRefs(weighingIngredientsStore);
  const { setSelectedIngredients, getIngredientsDetails, setMaterialInfo } =
    weighingIngredientsStore;

  const bmosSelect = ref();

  const props = defineProps({
    componentId: {
      type: String,
      required: true
    }
  });

  onMounted(async() => {
    getIngredientsDetails({
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId
    });
  });
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  // 确定
  const submit = () => {
    uni.navigateBack();
  };
  const openSelect = () => {
    if (
      ingredientsDetails.value.batchList.some(
        (item) => item.weighStatus.value === 1 || item.weighStatus.value === 2
      )
    ) {
      uni.showToast({
        title: t('已确认配料单，无法切换'),
        icon: 'none'
      });
      return;
    }
    bmosSelect.value.open();
  };
  // 配料单选择确认
  const ingredientsChange = async(data) => {
    setSelectedIngredients(data);
    setMaterialInfo(null);
    getIngredientsDetails({
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId
    });
  };
</script>

<style lang="scss" scoped>
.container {
  padding-top: 46.89rpx;
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
    width: calc(100% - 20rpx);
    height: calc(100% - 20rpx);
    margin: 5rpx auto;
    border-radius: 8rpx;
    overflow: hidden;
    font-size: 14rpx;
    background-color: #fff;

    .label {
      color: #6c6e73;
    }

    .content_title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 40rpx;
      padding: 0 10rpx;
      background: linear-gradient(
        to bottom,
        rgba(229, 239, 255, 1),
        rgba(229, 239, 255, 0)
      );

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

    .table_box {
      height: calc(100% - 40rpx);
      overflow: auto;

      .status {
        line-height: 16.41rpx;
        text-align: center;
        border-radius: 2.34rpx;
      }

      .status_0 {
        background-color: #ffd5cc;
        color: #ff4c26;
      }

      .status_2 {
        background-color: #dcf2eb;
        color: #59bf78;
      }

      .status_1 {
        background-color: #ffecd9;
        color: #ff9933;
      }
    }
    .col-but {
      padding: 11.72rpx;
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      column-gap: 14.07rpx;
      margin: 5rpx auto 0;
      background-color: #fff;

      .but {
        width: 100%;
        padding: 2.38rpx 14.07rpx;
      }

      .cancel {
        color: var(---, #6c6e73);
        border: 1.17rpx solid var(----, #bbbdbf);
      }
    }
  }
}
</style>
