<template>
  <view>
    <wd-popup
      v-model="showEquipment"
      custom-style="width: 375rpx;height: 375rpx;border-radius: 11.72rpx;background:#fff;"
      :z-index="99"
      :close-on-click-modal="false"
      @close="showEquipment = false"
    >
      <view class="content">
        <view class="title">
          {{ t('选择打印机') }}
        </view>
        <view class="equipment-list">
          <view class="search">
            <uv-input
              v-model="searchValue"
              class="search-input"
              :placeholder="t('设备编号/设备名称')"
              prefix-icon="search"
              prefix-icon-style="font-size: 17.58rpx;color: rgba(182, 185, 191, 1);padding-left: 14.07rpx;"
              border="none"
              font-size="12.9rpx"
            />
          </view>
          <scroll-view class="list" scroll-y="true">
            <template v-for="item in equipmentList" :key="item.id">
              <wd-checkbox
                v-if="showDevice(item)"
                :model-value="equipmentId === item.id"
                custom-class="equipment-item"
                @change="equipmentChange(item.id, $event)"
              >
                <view>
                  <view class="equipment-name">
                    {{ item.name }}
                  </view>
                  <view class="equipment-position">
                    {{ item.position }}
                  </view>
                </view>
              </wd-checkbox>
            </template>
          </scroll-view>
        </view>
        <view class="button-box">
          <wd-row :gutter="16">
            <wd-col :span="12">
              <BmosButton :text="t('取消')" @click="showEquipment = false" />
            </wd-col>
            <wd-col :span="12">
              <BmosButton type="primary" :text="t('确定')" @click="equipmentConfirm" />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </wd-popup>
    <wd-popup
      v-model="showNoEquipment"
      custom-style="width: 375rpx;height: 210.94rpx;border-radius: 11.72rpx;background:#fff;"
      :close-on-click-modal="false"
      @close="showNoEquipment = false"
    >
      <view class="content">
        <view class="title">
          {{ t('标签打印') }}
        </view>
        <view class="sub-title">
          {{ t('未绑定标签打印机，是否配置标签打印机？') }}
        </view>
        <view class="button-box">
          <BmosButton :text="t('配置标签打印机')" @click="choosePrinter" />
          <wd-row :gutter="16">
            <wd-col :span="12">
              <BmosButton :text="t('取消')" @click="cancel" />
            </wd-col>
            <wd-col :span="12">
              <BmosButton type="primary" :text="t('跳过')" @click="jumpOver" />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </wd-popup>
  </view>
</template>

<script setup>
import { listEquipmentInfoApi } from '@/api/systemApi.js';
import BmosButton from '@/components/BmosButton/index.vue';
import { DEVICE_PRINTER } from '@/utils/uniStorage/const.js';
import {
  getStorageSync,
  setStorageSync,
} from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';

const emit = defineEmits(['jumpOver', 'cancel', 'choosePrinterConfirm']);

const showEquipment = ref(false);
const showNoEquipment = ref(false);
const equipmentId = ref('');
const equipmentList = ref([]);
const searchValue = ref('');

const equipmentChange = (id, { value }) => {
  if (value) {
    equipmentId.value = id;
  }
  else {
    equipmentId.value = '';
  }
};
const showDevice = (item) => {
  if (!searchValue.value) {
    return true;
  }
  return item.name?.includes(searchValue.value) || item.position?.includes(searchValue.value);
};

const equipmentConfirm = () => {
  const devicePrint = equipmentList.value.find(item => item.id === equipmentId.value) || null;
  if (!devicePrint) {
    uni.showToast({
      title: t('请选择打印设备'),
      icon: 'none',
    });
    return;
  }
  setStorageSync(DEVICE_PRINTER, devicePrint);
  emit('choosePrinterConfirm', devicePrint);
  showEquipment.value = false;
  showNoEquipment.value = false;
};
const choosePrinter = () => {
  open();
};

const cancel = () => {
  emit('cancel');
  showNoEquipment.value = false;
};

const jumpOver = () => {
  emit('jumpOver');
  showNoEquipment.value = false;
};
const open = async () => {
  searchValue.value = '';
  const devicePrint = getStorageSync(DEVICE_PRINTER);
  const res = await listEquipmentInfoApi();
  equipmentList.value = res.data;
  if (devicePrint) {
    equipmentId.value = devicePrint.id;
  }
  showEquipment.value = true;
};
const print = () => {
  const devicePrint = getStorageSync(DEVICE_PRINTER);
  if (devicePrint) {
    return devicePrint;
  }
  showNoEquipment.value = true;
  return null;
};

defineExpose({
  open,
  print,
});
</script>

<style lang="scss" scoped>
.content {
  .title {
    font-size: 26px;
    color: #242526;
    text-align: center;
    font-weight: 500;
    height: 41.02rpx;
    line-height: 41.02rpx;
  }
  .equipment-list {
    padding: 0 9.38rpx;
    box-sizing: border-box;
    .search {
      margin: 9.38rpx 0 0;

      .search-input {
        background: rgba(247, 248, 250, 1);
        height: 32.83rpx;
        border-radius: 4.69rpx;
      }
    }
    .list {
      height: 240.23rpx;
      .equipment-item {
        padding: 9.38rpx 0;
      }
      .equipment-name {
        color: #242526;
        font-size: 11.72rpx;
        font-weight: 500;
      }
      .equipment-position {
        color: #6c6e73;
        font-size: 10.55rpx;
        font-weight: 500;
      }
    }
  }
  .sub-title {
    font-size: 12.89rpx;
    font-weight: 500;
    height: 15.23rpx;
    line-height: 15.23rpx;
    color: #6c6e73;
    text-align: center;
    margin-top: 18.16rpx;
    margin-bottom: 35.16rpx;
  }
  .button-box {
    display: grid;
    padding: 0 9.38rpx;
    row-gap: 9.38rpx;
  }
}
</style>
