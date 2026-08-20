<template>
  <uv-modal
    ref="modalRef"
    width="468.93rpx"
    :show-confirm-button="false"
    :close-on-click-overlay="false"
  >
    <view class="content">
      <view class="title">{{ t("结果确认") }}</view>
      <view class="msg_box">
        <view v-for="(item, index) in msgList" :key="index" class="msg_item">
          <view v-if="index != 0" class="line" />
          <view class="msg">
            <view class="label">{{ item.label }}</view>
            <view :class="`msg_data ${item.class}`">
              {{ detailData[item.filed] }}
            </view>
          </view>
        </view>
      </view>
      <view class="table_box">
        <Section :title="t('称量信息')" />
        <scroll-view class="table" scroll-y="true">
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
            </uni-tr>
            <uni-tr v-for="(item, index) in tableData" :key="index">
              <uni-td
                v-for="(sl, ix) in tableConfig"
                :key="ix"
                :width="sl.width"
                :align="sl.align"
              >
                {{ sl.filed == "index" ? index + 1 : item[sl.filed] }}
              </uni-td>
            </uni-tr>
          </uni-table>
        </scroll-view>
      </view>
    </view>
    <template #confirmButton>
      <view class="sign-buttons-box">
        <uv-row justify="space-between" gutter="10">
          <uv-col span="6">
            <BmosButton type="default" :text="t('签名')" @click="toSign" />
          </uv-col>
          <uv-col span="6">
            <BmosButton
              type="primary"
              :text="
                detailData.nextProcess?.value === 2 && currentProcess === 2
                  ? t('继续称量')
                  : ['', t('继续称量'), t('余料称量'), t('更换物料批次')][
                    detailData.nextProcess?.value
                  ]
              "
              @click="continueWeighing"
            />
          </uv-col>
        </uv-row>
      </view>
    </template>
  </uv-modal>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { ref, watch } from 'vue';
  import BmosButton from '@/components/BmosButton/index.vue';
  import Section from '@/components/Section/index.vue';

  import { msgList, tableConfig } from './config.js';

  const emit = defineEmits(['continueWeighing']);
  const props = defineProps({
    detailData: {
      type: Object,
      default: () => ({})
    },
    currentProcess: {
      type: Number,
      default: 0
    },
    componentId: {
      type: String,
      default: ''
    }
  });
  const modalRef = ref(null);

  const tableData = ref([]);

  const open = () => {
    modalRef.value.open();
  };
  const toSign = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/weighingIngredients/weighingResults/index?componentId=${props.componentId}`
    });
  };

  const continueWeighing = () => {
    emit('continueWeighing');
    modalRef.value.close();
  };
  watch(
    () => props.detailData,
    (val) => {
      tableData.value = props.detailData.resultItemList || [];
    },
    {
      deep: true,
      immediate: true
    }
  );
  defineExpose({
    open
  });
</script>

<style lang="scss" scoped>
.sign-buttons-box {
  background-color: white;
  width: 100%;
  box-sizing: border-box;
  height: 51.58rpx;
  padding: 0 14.65rpx;
}
.content {
  font-size: 12.89rpx;
  width: 100%;
  .title {
    text-align: center;
    color: #242526;
    font-size: 11.72rpx;
    margin-bottom: 5.86rpx;
  }
  .msg_box {
    height: 53.91rpx;
    padding: 9.38rpx;
    box-sizing: border-box;
    border-radius: 4.69rpx;
    background-color: #f2f3f5;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .msg_item {
      width: 20%;
      text-align: center;
      position: relative;
      .line {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        margin: auto;
        height: 11.72rpx;
        width: 0.59rpx;
        background-color: #e1e3e5;
      }
      .msg {
        color: #242526;
        .label {
          color: #6c6e73;
          margin-bottom: 4.69rpx;
        }
        .green {
          color: #59bf78;
        }
        .orange {
          color: #ff9933;
        }
      }
    }
  }

  .table_box {
    .table {
      padding-bottom: 9.38rpx;
      box-sizing: border-box;
      border-top: 1rpx solid #e1e3e5;
      margin: auto;
      height: 182.81rpx;
      :deep(.uni-table-tr) {
        height: 40rpx;
      }
    }
  }
  .remark_box {
    margin-top: 10rpx;
  }
  .group-box {
    background-color: #f7f8fa;
    border-radius: 7.03rpx;
    padding: 9.38rpx 9.38rpx 0;
  }
  :deep(.uni-input-placeholder) {
    height: 40rpx;
    line-height: 40rpx;
  }
}
</style>
