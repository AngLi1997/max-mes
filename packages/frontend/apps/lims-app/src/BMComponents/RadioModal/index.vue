<template>
  <BMModal v-model="open" size="large" :cancel-text="cancelText" @confirm="confirm" @cancel="cancel">
    <view class="radio-modal-content">
      <BMInputSearch
        v-if="options.length > 7 && hasSearch"
        v-model="searchValue"
        :placeholder="placeholder"
      />
      <scroll-view
        v-if="options.length"
        scroll-y="auto"
        :class="[(options.length > 7 && hasSearch) ? 'radio-box-search' : '']"
        class=" radio-box"
      >
        <template v-if="showOptions.length">
          <wd-radio-group v-if="subLabels?.length" v-model="value">
            <wd-radio-group v-model="value" shape="dot">
              <wd-cell
                v-for="option in showOptions"
                :key="option[customFieldNames.value]"
                :title="option[customFieldNames.label]"
                center
                clickable
                @click="value = option[customFieldNames.value]"
              >
                <template #label>
                  <view class="sub-label-box">
                    <view v-for="item in subLabels" :key="item.key">
                      <view class="sub-label">
                        {{ item.label }}: {{ option[item.key] }}
                      </view>
                    </view>
                  </view>
                </template>
                <wd-radio
                  :key="option[customFieldNames.value]"
                  :value="option[customFieldNames.value]"
                />
              </wd-cell>
            </wd-radio-group>
          </wd-radio-group>
          <wd-radio-group v-else-if="subLabel" v-model="value">
            <wd-radio-group v-model="value" shape="dot">
              <wd-cell
                v-for="option in showOptions"
                :key="option[customFieldNames.value]"
                :title="option[customFieldNames.label]"
                center
                clickable
                @click="value = option[customFieldNames.value]"
              >
                <template #label>
                  <view class="sub-label-box">
                    <view class="sub-label">
                      {{ option[subLabel] }}
                    </view>
                  </view>
                </template>
                <wd-radio
                  :key="option[customFieldNames.value]"
                  :value="option[customFieldNames.value]"
                />
              </wd-cell>
            </wd-radio-group>
          </wd-radio-group>
          <wd-radio-group v-else v-model="value">
            <wd-radio
              v-for="(option, index) in showOptions"
              :key="option[customFieldNames.value] + index"
              :value="option[customFieldNames.value]"
            >
              {{ option[customFieldNames.label] }}
            </wd-radio>
          </wd-radio-group>
        </template>
        <view v-else>
          <BMNoData type="emptySearch" :text="t('暂无搜索结果')" />
        </view>
      </scroll-view>
      <view v-else>
        <BMNoData type="emptyData" :text="t('暂无数据')" />
      </view>
    </view>
  </BMModal>
</template>

<script setup>
import BMInputSearch from '@/BMComponents/InputSearch/index.vue';
import BMModal from '@/BMComponents/Modal/index.vue';
import BMNoData from '@/BMComponents/NoData/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  open: {
    type: Boolean,
    default: false,
  },
  options: {
    type: Array,
    default: () => [],
  },
  fieldNames: {
    type: Object,
    default: () => ({ label: 'label', value: 'value' }),
  },
  placeholder: {
    type: String,
    default: () => t('请输入关键字'),
  },
  required: {
    type: Boolean,
    default: false,
  },
  subLabels: {
    type: Array,
    default: () => [],
  },
  subLabel: {
    type: String,
    default: '',
  },
  cancelText: {
    type: String,
    default: () => t('取消'),
  },
  hasSearch: {
    type: Boolean,
    default: true,
  },
});
const emit = defineEmits([
  'update:modelValue',
  'update:open',
  'confirm',
  'cancel',
]);

const value = ref('');
const searchValue = ref('');
const customFieldNames = computed(() => {
  return {
    label: props.fieldNames.label || 'label',
    value: props.fieldNames.value || 'value',
  };
});

const showOptions = computed(() => {
  if (searchValue.value === '') {
    return props.options;
  }
  return props.options.filter((option) => {
    return option[customFieldNames.value.label].includes(searchValue.value);
  });
});

const open = computed({
  get: () => props.open,
  set: (val) => {
    emit('update:open', val);
  },
});

const confirm = () => {
  let selectedData;
  if (value.value) {
    selectedData = props.options.find(
      option => option[customFieldNames.value.value] === value.value,
    );
  }
  emit('update:modelValue', value.value);
  emit('confirm', selectedData);
  // 如果是必填项，且没有选择数据，则不允许关闭
  if (props.required && !selectedData) {
    return;
  }
  open.value = false;
};

const cancel = () => {
  open.value = false;
  emit('cancel');
};

watch(
  () => props.open,
  (val) => {
    if (val) {
      value.value = props.modelValue || '';
      searchValue.value = '';
    }
  },
);
</script>

<style lang="scss" scoped>
.radio-modal-content {
  min-height: 116.02rpx;
  .radio-box {
    max-height: 280.08rpx;
    margin-top: 0;
  }
  .radio-box-search {
    height: 232.03rpx;
    margin-top: 9.38rpx;
  }
  .sub-label-box {
    display: flex;
    flex-wrap: wrap;
    color: var(--bmos-color-text-sub);
    font-size: var(--bmos-font-size-desc);
    column-gap: 14.06rpx;
    row-gap: 4.69rpx;
    margin-top: 4.69rpx;
  }
  :deep(.wd-cell__left) {
    max-width: calc(100% - 58.59rpx);
    .wd-cell__label {
      white-space: normal;
    }
  }
  :deep(.wd-cell__right) {
    flex: unset;
    .wd-radio {
      padding: 0;
      height: 100%;
    }
  }
}
</style>
