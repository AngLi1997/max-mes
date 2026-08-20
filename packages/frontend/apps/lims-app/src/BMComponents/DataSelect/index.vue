<template>
  <view :id="selectId" ref="dataSelectContainer" class="data-select-container">
    <view class="label-item">
      {{ label }}
    </view>
    <wd-tooltip
      v-model="show"
      :disabled="disabled"
      placement="bottom-end"
      use-content-slot
    >
      <view class="input-item" @click="showSelect">
        <input
          v-model="searchValue"
          :disabled="disabled"
          :placeholder="placeholder"
          style="flex: 1"
          :style="disabled ? 'pointer-events: none' : ''"
          @input="handleInput"
          @focus="showSelect"
          @click="showSelect"
        >
        <template v-if="!disabled">
          <wd-icon
            v-if="searchValue"
            name="qingchu"
            size="14.06rpx"
            class-prefix="bmos-app-icon"
            @click.stop="handleClear"
          />
          <wd-icon
            v-else
            name="jiantou-xia"
            size="14.06rpx"
            class-prefix="bmos-app-icon"
          />
        </template>
      </view>

      <template #content>
        <view class="select-options-box">
          <view
            v-for="(item, index) in options"
            v-show="
              `${item[customFieldNames.label]} - ${item[customFieldNames.value]}`.includes(searchValue) || !isSearch
            "
            :key="index"
            class="option-item"
            @click="optionClick(item)"
          >
            {{ item[customFieldNames.label] }}-{{
              item[customFieldNames.value]
            }}
          </view>
        </view>
      </template>
    </wd-tooltip>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { computed, nextTick, onMounted, ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
    default: () => t('请选择'),
  },
  options: {
    type: Array,
    default: () => [],
  },
  label: {
    type: String,
    default: '',
  },
  fieldNames: {
    type: Object,
    default: () => ({ value: 'value', label: 'label', id: 'id' }),
  },
});
const emit = defineEmits(['update:modelValue', 'select']);
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const dataSelectContainer = ref();
const selectId = ref('');

const customFieldNames = computed(() => {
  return Object.assign(
    {
      value: 'value',
      label: 'label',
      id: 'id',
    },
    props.fieldNames,
  );
});
const searchValue = ref('');
const isSearch = ref(false);
const show = ref(false);

const handleInput = () => {
  isSearch.value = true;
};
const showSelect = () => {
  if (props.disabled) {
    return;
  }
  setTimeout(() => {
    show.value = true;
  }, 100);
};
const handleClear = () => {
  searchValue.value = '';
  isSearch.value = false;
  emit('update:modelValue', '');
  emit('select', null);
};

const optionClick = (item) => {
  isSearch.value = false;
  show.value = false;
  emit('update:modelValue', item[customFieldNames.value.value]);
  emit('select', item);
};
watch(
  () => value.value,
  (val) => {
    searchValue.value = '';
    props.options.forEach((item) => {
      if (item[customFieldNames.value.value] === val) {
        searchValue.value = `${item[customFieldNames.value.label]} - ${item[customFieldNames.value.value]}`;
        emit('select', item);
      }
    });
  },
  {
    immediate: true,
  },
);
watch(() => show.value, (val) => {
  if (!val) {
    isSearch.value = false;
    searchValue.value = '';
    props.options.forEach((item) => {
      if (item[customFieldNames.value.value] === value.value) {
        searchValue.value = `${item[customFieldNames.value.label]} - ${item[customFieldNames.value.value]}`;
      }
    });
  }
});
const popWidth = ref('257.81rpx');
onMounted(() => {
  selectId.value = `selectID_${new Date().getTime()}`;
  nextTick(() => {
    try {
      const info1 = dataSelectContainer.value.$el;
      if (info1) {
        const width = info1.offsetWidth;
        popWidth.value = `${Number.parseInt(width) - 136}px`;
      }
      else {
        const info = uni.createSelectorQuery().select(`#${selectId.value}`);
        info.boundingClientRect((data) => {
          popWidth.value = `${Number.parseInt(data.width) - 136}px`;
        }).exec();
      }
    }
    catch (error) {
      console.log('error', error);
    }
  });
});
</script>

<style lang="scss" scoped>
.data-select-container {
  width: 100%;
  height: 42.19rpx;
  display: flex;
  align-items: center;
  .label-item {
    line-height: 14.06rpx;
    margin-right: 10.55rpx;
    font-size: 11.72rpx;
    color: var(--bmos-color-text-sub);
  }

  :deep(.wd-tooltip) {
    flex: 1;

    .wd-tooltip__pos {
      right: 30px !important;
      top: 36.46px !important;
    }
    .wd-tooltip__arrow {
      left: 7.62rpx;
      filter: drop-shadow(0px 0px 4px #00000033);
      top: calc(-1 * var(--wot-tooltip-arrow-size, 9px) + 0.5px);
    }
  }
  .input-item {
    line-height: 14.06rpx;
    flex: 1;
    display: flex;
    align-items: center;
    gap: 5.86rpx;
    :deep(.input-placeholder) {
      color: var(--bmos-color-text-placeholder);
    }
    :deep(.uni-input-input) {
      height: 14.06rpx;
      line-height: 14.06rpx;
    }
    :deep(uni-input) {
      font-size: 9.38rpx;
    }
  }

  :deep(.wd-tooltip__container) {
    background-color: var(--bmos-color-white);
    border-radius: 4.69rpx;
  }
}

.select-options-box {
  padding: 0 9.38rpx;
  width: v-bind(popWidth);
  height: 84.38rpx;
  overflow-y: auto;
  box-shadow: 0px 0px 4px 0px #00000033;
  border-radius: 4.69rpx;
  .option-item {
    width: 100%;
    min-height: 28.13rpx;
    padding: 7.03rpx 0;
    box-sizing: border-box;
    line-height: 14.06rpx;
    border-bottom: 0.59rpx solid var(--bmos-color-disabled);
    text-align: left;
    overflow: hidden;
    text-overflow: ellipsis;
    /* 将对象作为弹性伸缩盒子模型显示 */
    display: -webkit-box;
    /* 限制在一个块元素显示的文本的行数 */
    /* -webkit-line-clamp 其实是一个不规范属性，使用了WebKit的CSS扩展属性，该方法适用于WebKit浏览器及移动端；*/
    -webkit-line-clamp: 1;
    /* 设置或检索伸缩盒对象的子元素的排列方式 */
    -webkit-box-orient: vertical;

    display: flex;
    align-items: center;
  }
  font-size: 11.72rpx;
  color: var(--bmos-color-text-sub);
}
</style>
