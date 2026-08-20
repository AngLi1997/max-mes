<template>
  <view class="verify-card">
    <wd-card
      type="rectangle"
      :custom-class="`card-mode status-${statusType}`"
    >
      <template #title>
        <view class="card-title">
          <view class="card-title-text">
            {{ title }}
          </view>
          <wd-tag :type="statusType">
            {{ status?.label }}
          </wd-tag>
        </view>
      </template>
      <view class="card-content">
        <view v-for="item in cardInfo" :key="item.field" class="card-content-item">
          <view class="card-content-item-label">
            {{ item.label }}：
          </view>
          <view class="card-content-item-value">
            {{ cardData?.[item.field] }}
          </view>
        </view>
      </view>
      <template #footer>
        <view class="card-footer">
          <wd-button v-if="statusType === statusMap[3]" style="width: 100%;" type="text" size="small" @click="reVerify">
            {{ t('重新发起请验') }}
          </wd-button>
          <wd-button v-else style="width: 100%;" type="text" size="small" @click="toVerify">
            {{ t('检验结果') }}
          </wd-button>
          <wd-divider vertical />
          <wd-button style="width: 100%;" type="text" size="small" @click="toDetail">
            {{ t('请验详情') }}
          </wd-button>
        </view>
      </template>
    </wd-card>
    <BMModal v-model="open" :title="t('是否重新发起请验')" :cancel-text="t('取消')" @confirm="confirm" @cancel="cancel">
      <view class="reason-label">
        {{ t('退回原因') }}:
      </view>
      <view class="reason-text">
        {{ cardData.reason }}
      </view>
    </BMModal>
  </view>
</template>

<script setup>
import { BMModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { computed, reactive, ref } from 'vue';
import { formProps } from './cardProps';

const props = defineProps(formProps);

const cardInfo = reactive([
  {
    label: t('请验单号'),
    field: 'inspectNo',
  },
  {
    label: t('物料批号'),
    field: 'materialBatchNo',
  },
  {
    label: t('请验人'),
    field: 'inspector',
  },
  {
    label: t('请验时间'),
    field: 'inspectTime',
  },
]);

const statusMap = reactive({
  1: 'primary',
  2: 'success',
  3: 'danger',
});

const statusType = computed(() => {
  return statusMap?.[props.status?.value] ?? 'primary';
});

const open = ref(false);

const reVerify = () => {
  open.value = true;
};

const cancel = () => {
  open.value = false;
};

const confirm = () => {
  open.value = false;
  uni.navigateTo({
    url: `/pages/pleaseVerify/initiateVerification/index${queryParams({ id: props.cardData.id, planId: props.planId, procedureModelId: props.procedureModelId, reVerify: true })}`,
  });
};

const toVerify = () => {
  uni.navigateTo({
    url: `/pages/pleaseVerify/inspectResult/index${queryParams({ id: props.cardData.id, planId: props.planId, procedureModelId: props.procedureModelId, materialName: props.title })}`,
  });
};

const toDetail = () => {
  uni.navigateTo({
    url: `/pages/pleaseVerify/detailPage/index${queryParams({ id: props.cardData.id, planId: props.planId, procedureModelId: props.procedureModelId, materialName: props.title })}`,
  });
};
</script>

<style lang="scss" scoped>
$COLORS: (
  primary: #ebf2ff,
  success: #dcf2eb,
  danger: #fff2e5,
);

@each $key, $value in $COLORS {
  .status-#{$key} {
    :deep(.wd-card__title-content) {
      background-color: $value;
    }
  }
}

.reason-label {
  color: #6c6e73;
  font-size: 12.89rpx;
  margin-bottom: 9.38rpx;
}
.reason-text {
  color: #242526;
  font-size: 12.89rpx;
}

.verify-card {
  // width: 100%;
  margin: 4.69rpx;
  .card-mode {
    height: 157.03rpx;
    margin-bottom: 0;
    border-radius: 4.69rpx;
    border: 1px solid #e1e3e5;
    padding: 0;
    :deep(.wd-card__title-content) {
      padding: 9.38rpx 0;
      border-radius: 4.69rpx 4.69rpx 0 0;
    }
    :deep(.wd-card__content),
    :deep(.wd-card__footer) {
      padding: 9.38rpx 0;
    }
  }
  .card-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 9.38rpx;
    &-text {
      font-size: 14.06rpx;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .card-content {
    height: 100%;
    padding: 0 9.38rpx;
    &-item {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      font-size: 11.72rpx;
      &-value {
        color: var(--bmos-color-text-main);
      }
    }
  }
  .card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-grow: 1;
    // gap: 9.38rpx;
    color: #e1e3e5;
    .wd-button.is-text {
      height: 14.06rpx;
    }
    .wd-divider--vertical {
      height: 14.06rpx;
    }
  }
}
</style>
