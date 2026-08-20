<!-- 搜索框组件 -->
<template>
  <wd-search v-model="searchValue" :="attrs" placeholder-left>
    <template #suffix>
      <view class="search-right">
        <text @click="search">{{ t('搜索') }}</text>
      </view>
    </template>
  </wd-search>
</template>
  
  <script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { useAttrs, ref } from 'vue';
  
  const attrs = useAttrs();
  const searchValue = ref('');
  const emit = defineEmits(['search']);
  // 搜索按钮
  const search = () => {
    emit('search', searchValue.value);
  };
  </script>
  
  <style lang="scss" scoped>
  //搜索框
    .wd-search{
        background:var(--bmos-bg-form);
        padding: 0rpx!important;
        height: 35.16rpx;
        border-radius:var(--bmos-border-radius-medium) ;
    }
   .search-right{
    height: 14.06rpx;
    display: flex;
	align-items: center;
    margin-right: 10.55rpx;
    padding-left: 10.55rpx;
    border-left: 1.76rpx solid var(--bmos-color-border);
    text{
        font-size: 11.72rpx;
        color: var(--bmos-color-primary);
    }
   }
  </style>
  
