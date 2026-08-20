<template>
  <BMModal
    v-model="showSortModal"
    :default-padding="false"
    :title="t('排序')"
    size="small"
    position="right"
    closable
    :cancel-text="t('重置')"
    @confirm="confirm"
    @cancel="reset"
  >
    <view class="filter_form_box">
      <BMForm
        ref="formRef"
        v-bind="formProps"
      />
    </view>
  </BMModal>
</template>
<script setup>
  import { ref, computed, reactive } from 'vue';
  import { t } from '@/utils/useBmosI18n.js';
  import { BMModal, BMForm } from '@/BMComponents';

  const props = defineProps({
    open: {
      type: Boolean,
      default: false
    }
  });
  const emit = defineEmits([
    'update:open',
    'confirm',
    'reset'
  ]);
  const showSortModal = computed({
    get: () => props.open,
    set: (val) => {
      emit('update:open', val);
    }
  });

  const formRef = ref();

  const confirm = async() => {
    const data = await formRef.value?.validate();
    showSortModal.value = false;
    emit('confirm', data);
  };
  const reset = () => {
    formRef.value?.resetForm();
    showSortModal.value = false;
    emit('reset');
  };
  
  // 筛选表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'orderBy',
        component: 'BMFormRadio',
        label: t('开始时间'),
        colProps: {
          span: 24
        },
        componentProps: {
          options: [
            {
              label: t('顺序排列'),
              value: 'start_time asc'
            },
            {
              label: t('逆序排列'),
              value: 'start_time desc'
            }
          ]
        }
      },
      {
        field: 'orderBy',
        component: 'BMFormRadio',
        label: t('生产批号'),
        colProps: {
          span: 24
        },
        componentProps: {
          options: [
            {
              label: t('顺序排列'),
              value: 'batch_no asc'
            },
            {
              label: t('逆序排列'),
              value: 'batch_no desc'
            }
          ]
        }
      }
    ]
  });
  </script>
  <style scoped lang="scss">
  .filter_form_box{
    padding: 11.72rpx 0 0 5rpx;
    width: 260.53rpx;
    margin-right: -11.72rpx;
    :deep(.wd-cell__left){
      margin-bottom: 14.06rpx !important;
    }
    :deep(.wd-radio){
      width: 50%;
    }
    :deep(.wd-radio__label){
      text-align: center;
    }
  }
</style>
