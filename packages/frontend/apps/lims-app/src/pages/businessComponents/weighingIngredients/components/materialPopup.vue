<template>
  <wd-popup
    v-model="open"
    custom-style="width:375.15rpx;height:375.15rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="material-popup-container">
      <view class="title">
        {{ title }}
      </view>
      <view class="select_box">
        <view class="options">
          <view
            v-for="(item, index) in unWeighedOptions"
            :key="item.id"
            :class="{ options_item: true, checked: item.checked }"
            @click="optionClick(item, index)"
          >
            <view class="option_label">
              <view class="label">
                {{ `${item.mergeCode}-${item.materialName}` }}
              </view>
              <view class="sub-label">
                {{ t("批号") }}: {{ item.storageMaterialBatchNo }}
              </view>
            </view>
            <uv-icon
              v-if="item.checked"
              name="xuanze"
              custom-prefix="bmos-icon"
              size="14.07rpx"
              color="#3F5DF1"
              class="icon"
            />
          </view>
          <BmosNoData
            v-if="unWeighedOptions.length === 0"
            type="emptyProductionBefore"
            :text="t('无配料批次未称量')"
          />
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
  import BmosNoData from '@/components/BmosNoData/index.vue';
  import { ref, watch, computed } from 'vue';
  const props = defineProps({
    title: {
      type: String,
      default: ''
    },
    options: {
      type: Array,
      default: () => []
    },
    modelValue: {
      type: Boolean,
      default: false
    },
    selectedId: {
      type: String,
      default: ''
    }
  });

  const unWeighedOptions = computed(() => {
    return props.options.filter((item) => item.weighStatus.value === 0);
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
  const checkedData = ref(null);

  const optionClick = (data) => {
    checkedData.value = null;
    unWeighedOptions.value.forEach((item) => {
      if (item.storageMaterialBatchId === data.storageMaterialBatchId) {
        item.checked = !item.checked;
      } else {
        item.checked = false;
      }
      if (item.checked) {
        checkedData.value = item;
      }
    });
  };
  // 物料批次弹框关闭
  const close = () => {
    open.value = false;
  };
  // 物料批次弹框确认
  const confirm = () => {
    emit('confirm', checkedData.value);
  };
  watch(
    () => open.value,
    () => {
      if (open.value) {
        unWeighedOptions.value.forEach((item) => {
          item.checked = props.selectedId === item.storageMaterialBatchId;
        });
      }
    },
    {
      immediate: true
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
    height: 273.15rpx;
    overflow: auto;
    margin-bottom: 60.96rpx;
    padding: 0 9.38rpx;

    .options_item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 51.58rpx;
      line-height: 51.58rpx;
      padding: 0 9.38rpx;
      border-radius: 5rpx;

      .label {
        font-size: 11.72rpx;
        line-height: 14.07rpx;
        color: #242526;
        margin-bottom: 7.03rpx;
      }

      .sub-label {
        font-size: 10.55rpx;
        line-height: 11.72rpx;
        color: #6c6e73;
      }
    }

    .checked {
      background-color: #d9e5ff;
      color: #2871ff;
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
