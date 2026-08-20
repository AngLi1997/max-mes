<template>
  <wd-popup
    v-model="open"
    custom-style="width:375.15rpx;height:291.8rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="material-popup-container">
      <view class="title">
        {{ t("采集点匹配") }}
      </view>
      <view class="select_box">
        <view class="options">
          <wd-radio-group v-model="selectValue" shape="dot">
            <view v-for="item in options" :key="item.acquisitionPointId" class="radio-item">
              <wd-radio :value="item.acquisitionPointId" />
              <text class="name">{{ item.dataPointName }}</text>
              <text v-if="!historyModel" class="value">{{ item.value }}</text>
              <text v-if="!historyModel" class="date">{{ item.timeStamp }}</text>
            </view>
            <view class="radio-item">
              <wd-radio value="manual" />
              <text class="name">{{ t('手动输入') }}</text>
            </view>
          </wd-radio-group>
        </view>
      </view>
      <view class="button-container">
        <wd-row gutter="16">
          <wd-col :span="12">
            <BmosButton type="default" :text="t('取消')" @click="close" />
          </wd-col>
          <wd-col :span="12">
            <BmosButton type="primary" :text="t('确定')" @click="confirm" />
          </wd-col>
        </wd-row>
      </view>
    </view>
  </wd-popup>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { ref, watch, computed } from 'vue';
  import { reqPlatformEquipmentAcquisitionPointDataApi } from '@/api';
  import { timestampToTime } from '@/utils/time.js';

  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    rowData: {
      type: Object,
      default: () => ({})
    },
    equipmentId: {
      type: String,
      default: ''
    },
    historyModel: {
      type: Boolean,
      default: false
    }
  });
  const open = computed({
    get() {
      return props.modelValue;
    },
    set(value) {
      emit('update:modelValue', value);
    }
  });
  const emit = defineEmits(['confirm', 'update:modelValue']);
  const options = ref([]);
  const selectValue = ref('');

  const selectedData = computed(() => {
    return options.value.find((item) => item.acquisitionPointId === selectValue.value);
  });

  // 弹框关闭
  const close = () => {
    open.value = false;
  };
  // 弹框确认
  const confirm = () => {
    emit('confirm', selectValue.value, selectedData.value ? selectedData.value : {
      value: ''
    });
  };
  watch(
    () => props.options,
    () => {
      if (props.options) options.value = props.options;
    },
    {
      immediate: true
    }
  );

  // 根据设备id获取设备绑定采集项的所有数据
  const getOptions = async() => {
    try {
      const { data } = await reqPlatformEquipmentAcquisitionPointDataApi(props.equipmentId);
      options.value = data?.map((item) => {
        return {
          ...item,
          originalTimeStamps: item.timeStamp,
          timeStamp: timestampToTime(item.timeStamp)
        };
      });
    } catch (error) {
      options.value = [];
    }
  };

  watch(
    () => open.value,
    (value) => {
      if (value) {
        const { selectPointId } = props.rowData;
        if (selectPointId) {
          selectValue.value = selectPointId;
        } else {
          selectValue.value = '';
        }
        props.equipmentId && getOptions();
      }
    }
  );
</script>

<style lang="scss" scoped>
.material-popup-container {
  .title {
    height: 41.03rpx;
    line-height: 41.03rpx;
    font-size: 15.24rpx;
    text-align: center;
  }
  .select_box {
    height: 189.84rpx;
    overflow: auto;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    .radio-item {
      display: flex;
      align-items: center;
      height: 35.16rpx;
      .wd-radio {
        margin-top: 0;
        margin-right: 18.75rpx;
      }
      .name {
        width: 101.95rpx;
        color: #242526;
        font-size: 11.72rpx;
      }
      .value {
        width: 101.95rpx;
        color: #242526;
        font-size: 11.72rpx;
      }
      .date {
        width: 128.91rpx;
        color: #6c6e73;
        font-size: 10.55rpx;
      }
    }
  }
  .button-container {
    position: absolute;
    bottom: 12.31rpx;
    left: 0;
    width: 100%;
    padding: 0 9.38rpx;
    box-sizing: border-box;
  }
}
</style>
