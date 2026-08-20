<template>
  <uv-modal
    ref="modalRef"
    width="468.93rpx"
    :show-confirm-button="false"
    :close-on-click-overlay="false"
  >
    <view class="slot-content">
      <view class="title">
        <text>{{ title }}</text>
      </view>
      <view class="bmos_select_main">
        <view v-if="showSearch" class="search">
          <uv-input
            v-model="searchKey"
            class="search-input"
            :placeholder="placeholder"
            prefix-icon="search"
            prefix-icon-style="font-size: 17.58rpx;color: rgba(182, 185, 191, 1);padding-left: 14.07rpx;"
            border="none"
            font-size="12.9rpx"
            @input="searchKeyChange"
          />
        </view>
        <view class="select_box">
          <view class="options">
            <view
              v-for="(item, index) in options"
              :key="item[componentFieldNames.id]"
              class="options_item" :class="{ checked: item.checked }"
              @click="optionClick(item, index)"
            >
              <view class="option_label">
                {{ item[componentFieldNames.label] }}
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
          </view>
        </view>
      </view>
    </view>
    <template #confirmButton>
      <view class="buttons-box">
        <uv-button :custom-style="customStyleInfo" @click="close">
          {{ t('取消') }}
        </uv-button>
        <uv-button :custom-style="customStylePrimary" @click="confirm">
          {{ t('确定') }}
        </uv-button>
      </view>
    </template>
    <wd-toast />
  </uv-modal>
</template>

<script setup>
import { getCustomStyle } from '@/utils/getCustomStyle.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, watch } from 'vue';
import { useToast } from 'wot-design-uni';

const props = defineProps({
  optionsList: {
    type: Array,
    default: () => [],
  },
  title: {
    type: String,
    default: '',
  },
  fieldNames: {
    type: Object,
    default: () => {
      return {};
    },
  },
  placeholder: {
    type: String,
    default: '',
  },
  required: {
    type: Boolean,
    default: false,
  },
  selectedId: {
    type: String,
    default: '',
  },
  // 是否显示搜索框
  showSearch: {
    type: Boolean,
    default: true,
  },
});
const emit = defineEmits(['confirm', 'cancel']);
const toast = useToast();
const componentFieldNames = computed(() => {
  return {
    id: 'id',
    label: 'label',
    ...props.fieldNames,
  };
});
const customStyleInfo = getCustomStyle('default', {
  width: '211rpx',
  height: '42.2rpx',
});
const customStylePrimary = getCustomStyle('primary', {
  width: '211rpx',
  height: '42.2rpx',
});

const searchKey = ref(''); // 搜索栏
const options = ref([]);
const checkedData = ref(null); // 选中的数据
const modalRef = ref();

// 搜索栏输入
const searchKeyChange = (value) => {
  options.value = props.optionsList.filter((item) => {
    if (item[componentFieldNames.value.label].includes(value)) {
      return item;
    }
    return false;
  });
};
  // 点击选项
const optionClick = (data) => {
  if (
    checkedData.value
    && checkedData.value[componentFieldNames.value.id]
    !== data[componentFieldNames.value.id]
  ) {
    checkedData.value.checked = false;
  }
  data.checked = !data.checked;
  if (data.checked) {
    checkedData.value = data;
  }
  else {
    checkedData.value = null;
  }
};
  // 点击确定
const confirm = () => {
  if (props.required && !checkedData.value) {
    toast.warning(t('请选择一个') + props.placeholder);
    return;
  }
  emit('confirm', checkedData.value);
  modalRef.value.close();
  searchKey.value = '';
  options.value = [...props.optionsList];
};
  // 打开选择生产工艺弹窗
const open = () => {
  modalRef.value.open();
  options.value.forEach((item, index) => {
    if (item[componentFieldNames.value.id] === props.selectedId) {
      item.checked = false;
      optionClick(item, index);
    }
  });
};
  // 关闭弹窗
const close = () => {
  searchKey.value = '';
  options.value = [...props.optionsList];
  modalRef.value.close();
  emit('cancel');
};
watch(
  () => props.optionsList,
  () => {
    options.value = [...props.optionsList];
  },
  {
    immediate: true,
  },
);
defineExpose({
  open,
});
</script>

<style lang="scss" scoped>
.bmos_select_main {
  height: 249.71rpx;
  padding-bottom: 65rpx;

  .search {
    margin: 9.38rpx 0;

    .search-input {
      background: rgba(247, 248, 250, 1);
      height: 32.83rpx;
      border-radius: 4.69rpx;
    }
  }

  .select_box {
    height: 158.26rpx;
    overflow: auto;
    margin-bottom: 50rpx;

    .options_item {
      display: flex;
      justify-content: space-between;
      font-size: 16rpx;
      height: 32.83rpx;
      line-height: 32.83rpx;
      font-weight: 400;
      padding: 0 20rpx;
      border-radius: 5rpx;
    }

    .checked {
      background-color: #d9e5ff;
      color: #2871ff;
    }
  }
}

.slot-content {
  width: 100%;
  height: 249.71rpx;
  display: flex;
  flex-direction: column;

  .title {
    text-align: center;
    font-size: 16rpx;
  }
}

.buttons-box {
  background-color: white;
  width: 100%;
  box-sizing: border-box;
  height: 51.58rpx;
  padding: 0 14.65rpx;
  display: flex;
  justify-content: space-between;
}
</style>
