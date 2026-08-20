<template>
  <BMLayout>
    <BMBasicPage :title="t('物料件信息')" @left-click="toBack" @cancel="toBack" @confirm="submit">
      <view class="top-scan">
        <div style="width: 50%">
          <BMScan
            v-model="scanValue"
            type="input"
            :allow-types="['01', '02']"
            :error-type-placeholder="t('物料件码无法识别，请输入物料件号查询')"
            :placeholder="t('物料件号')"
            @success="onScanSuccess"
            @fail="onScanFail"
            @confirm="onScanSuccess"
          />
        </div>
      </view>
      <scroll-view class="content" scroll-y="true">
        <view class="info-content">
          <view v-for="item in materialDetailRef" :key="item.label" class="material-Info">
            <view class="name">{{ item.label }}</view>
            <view class="value">{{ item.value }}</view>
          </view>
        </view>
      </scroll-view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup lang="jsx">
  import { t } from '@/utils/useBmosI18n.js';
  import { BMBasicPage, BMLayout, BMScan } from '@/BMComponents/index.js';
  import { ref } from 'vue';
  import {
    getCurrentCopyRecordItem,
    urlQueryRef,
    pageBasicDataRef,
    initFillData2
  } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { useToast, useNotify } from 'wot-design-uni';
  import { getStorageMaterialInfoByNo, getInstanceByProps, reqStorageMaterialManageSaveMaterialComponentValue } from '@/api';

  const toast = useToast();
  const { showNotify } = useNotify();
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };

  const queryInfo = ref({});

  const scanValue = ref('');
  const onScanSuccess = async(code) => {
    try {
      const { data } = await getStorageMaterialInfoByNo({
        materialNo: code
      });
      if (data) {
        getDetail(data);
      } else {
        materialDetail.value = {};
        materialDetailRef.value = initDetailList;
        toast.error(t('物料件码无法识别，请输入物料件号查询'));
      }
    } catch (error) {
      materialDetailRef.value = initDetailList;
      materialDetail.value = {};
      error.message && showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };
  const onScanFail = (err) => {
    materialDetailRef.value = initDetailList;
    materialDetail.value = {};
    toast.error(t('物料件码无法识别，请输入物料件号查询'));
  };

  const submit = async() => {
    try {
      if (!materialDetail.value?.materialNo) {
        showNotify({
          type: 'danger',
          message: t('请扫描物料件')
        });
        return;
      }
      const { procedureStepModelId, reusable } = pageBasicDataRef.value;
      const { productPlanId } = urlQueryRef.value;
      const { version } = getCurrentCopyRecordItem();
      const { data } = await getInstanceByProps({
        componentId: queryInfo.value?.id,
        copyVersion: version,
        procedureStepModelId,
        productPlanId,
        reuse: reusable
      });
      if (!data || !data.id) {
        showNotify({
          type: 'danger',
          message: t('未找到对应的组件实例')
        });
        return;
      }
      await reqStorageMaterialManageSaveMaterialComponentValue({
        no: materialDetail.value?.materialNo,
        componentInstanceId: data?.id
      });
      initFillData2();
      toBack();
    } catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };

  const initDetailList = [
    {
      label: t('物料名称'),
      value: ''
    },
    {
      label: t('物料编码'),
      value: ''
    },
    {
      label: t('物料批次'),
      value: ''
    },
    {
      label: t('物料件号'),
      value: ''
    },
    {
      label: t('物料量'),
      value: ''
    },
    {
      label: t('净重'),
      value: ''
    },
    {
      label: t('皮重'),
      value: ''
    },
    {
      label: t('毛重'),
      value: ''
    },
    {
      label: t('单位'),
      value: ''
    }
  ];

  const materialDetailRef = ref();
  // 原始数据
  const materialDetail = ref();
  const getDetail = async(data) => {
    try {
      materialDetail.value = data;
      materialDetailRef.value = [
        {
          label: t('物料名称'),
          value: data.materialName
        },
        {
          label: t('物料编码'),
          value: data.mergeCode
        },
        {
          label: t('物料批次'),
          value: data.materialBatchNo
        },
        {
          label: t('物料件号'),
          value: data.materialNo
        },
        {
          label: t('物料量'),
          value: data.quantity + data.unit
        },
        {
          label: t('净重'),
          value: data.netWeight
        },
        {
          label: t('皮重'),
          value: data.tareWeight
        },
        {
          label: t('毛重'),
          value: data.grossWeight
        },
        {
          label: t('单位'),
          value: data.unit
        }
      ];
      data?.materialBatchCustomFields?.forEach((item) => {
        materialDetailRef.value.push({
          label: item.fieldName,
          value: item.fieldValue
        });
      });
      data?.materialCustomFields?.forEach((item) => {
        materialDetailRef.value.push({
          label: item.fieldName,
          value: item.fieldValue
        });
      });
      data?.materialPieceCustomFields?.forEach((item) => {
        materialDetailRef.value.push({
          label: item.fieldName,
          value: item.fieldValue
        });
      });
    } catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };

  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e).map((key) => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    queryInfo.value = query;
    // #endif
    // #ifdef H5
    queryInfo.value = e;
    // #endif
    materialDetailRef.value = initDetailList;
  });
</script>

<style lang="scss" scoped>
.top-scan {
  margin-top: 9.38rpx;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 9.38rpx;
  .wd-input {
    width: 50%;
  }
}
.content {
  width: 100%;
  height: calc(100% - 9.38rpx - 9.38rpx - 23.44rpx);
  font-size: 11.72rpx;
  font-weight: normal;
  .info-content {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 0;
    grid-row: 9.38rpx;
    border: .59rpx solid var(--bmos-color-border);
    border-bottom: none;
    border-right: none;
    border-left: none;
    .material-Info {
      display: flex;
      align-items: center;
      height: 37.5rpx;
      .name {
        flex: 1;
        background-color: #F5F6F7;
        padding: 11.72rpx;
        border-bottom: 1px solid var(--bmos-color-border);
        border-right: 1px solid var(--bmos-color-border);
        border-left: 1px solid var(--bmos-color-border);
        height: calc(100% - .59rpx - 2 * 11.72rpx);
        font-size: 11.72rpx;
        color: #606266;
      }
      .value {
        flex: 1;
        padding: 11.72rpx;
        font-size: 11.72rpx;
        border-bottom: 1px solid var(--bmos-color-border);
        height: calc(100% - .59rpx - 2 * 11.72rpx);
        border-right: 1px solid var(--bmos-color-border);
        color: var(--bmos-color-text-main);
      }
    }
  }
}
</style>
