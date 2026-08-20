<template>
  <BMModal v-model="open" size="large" @confirm="confirm" @cancel="cancel">
    <view class="checkbox-modal-content">
      <BMInputSearch
        v-if="options.length > 7"
        v-model="searchValue"
        :placeholder="placeholder"
      />
      <scroll-view
        v-if="options.length"
        scroll-y="auto"
        :class="[
          options.length > 7 ? 'checkbox-box-search' : '',
        ]"
        class=" checkbox-box"
      >
        <wd-cell-group v-if="showOptions.length" border>
          <wd-checkbox-group v-model="value" shape="square">
            <wd-cell
              v-for="(option, index) in showOptions"
              :key="option[customFieldNames.value]"
              :title="option[customFieldNames.label]"
              :label="option[customFieldNames.subLabel]"
              center
              clickable
              @click="handleCheck(index)"
            >
              <view @click.stop="() => {}">
                <wd-checkbox
                  :ref="setItemRef"
                  shape="square"
                  :model-value="option[customFieldNames.value]"
                />
              </view>
            </wd-cell>
          </wd-checkbox-group>
        </wd-cell-group>
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
    type: Array,
    default: () => [],
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
});
const emit = defineEmits([
  'update:modelValue',
  'update:open',
  'confirm',
  'cancel',
]);

const value = ref([]);
const searchValue = ref('');
const itemRefs = ref([]);
const customFieldNames = computed(() => {
  return {
    label: props.fieldNames.label || 'label',
    value: props.fieldNames.value || 'value',
    subLabel: props.fieldNames.subLabel || 'subLabel',
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

// 设置ref的函数，用于收集DOM元素
const setItemRef = (el) => {
  if (el) {
    itemRefs.value.push(el);
  }
};
const handleCheck = (index) => {
  const item = itemRefs.value[index];
  item && item.toggle();
};

const confirm = () => {
  let selectedData = [];
  if (value.value) {
    selectedData = props.options.filter((option) => {
      return value.value.includes(option[customFieldNames.value.value]);
    },
    );
  }
  // 如果是必填项，且没有选择数据，则不允许关闭
  if (props.required && !selectedData.length) {
    return;
  }
  open.value = false;
  emit('update:modelValue', value.value);
  emit('confirm', selectedData);
};

const cancel = () => {
  open.value = false;
  emit('cancel');
};

watch(
  () => props.open,
  (val) => {
    if (val) {
      value.value = props.modelValue || [];
      searchValue.value = '';
    }
  },
);
</script>

<style lang="scss" scoped>
.checkbox-modal-content {
  min-height: 116.02rpx;
  .checkbox-box {
    height: 280.08rpx;
    margin-top: 0;
  }
  .checkbox-box-search {
    height: 235.55rpx;
    margin-top: 9.38rpx;
  }
  :deep(.wd-cell__left) {
    max-width: calc(100% - 58.59rpx);
    .wd-cell__label {
      white-space: normal;
    }
  }
  :deep(.wd-cell__right) {
    flex: unset;
  }
}
</style>
