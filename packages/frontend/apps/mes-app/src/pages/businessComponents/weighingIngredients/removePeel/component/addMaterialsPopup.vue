<template>
  <wd-popup
    v-model="open"
    custom-style="width:575.15rpx;height:375.15rpx;border-radius:7.03rpx;"
    :z-index="999"
    @close="open = false"
  >
    <view class="material-popup-container">
      <view class="title">
        {{ t("添加物料") }}
      </view>
      <scroll-view scroll-y>
        <view class="input-class">
          <view class="right">
            <wd-input
              v-model="materialPartId"
              :placeholder="t('物料件号')"
              no-border
              custom-class="custom-input"
              use-suffix-slot
              @confirm="confirmPart(materialPartId)"
            />
            <!-- #ifdef APP-PLUS -->
            <view class="scan-icon-box" @click.stop="iconClick">
              <uv-icon name="scan" size="16.41rpx" color="#434C59" />
            </view>
            <!-- #endif -->
            <!-- #ifdef H5 -->
            <view class="scan-icon-box">
              <wd-button type="text" @click="iconClick">
                {{ t("确定") }}
              </wd-button>
            </view>
            <!-- #endif-->
          </view>
        </view>
        <uni-table
          ref="relocationTable"
          class="table-box"
          :loading="loading"
          :empty-text="t('暂无更多数据')"
        >
          <uni-tr class="tr-tab">
            <uni-th
              v-for="(item, index) in tableLabel"
              :key="index"
              :align="item.align"
              class="th-tab"
              :width="item.width"
            >
              {{ item.label }}
            </uni-th>
          </uni-tr>
          <uni-tr v-for="(item, index) in tableData" :key="index">
            <uni-td
              v-for="(sl, ix) in tableLabel"
              :key="ix"
              :width="sl.width"
              :align="sl.align"
            >
              {{ sl.dataIndex !== "BMOSDelete" ? item[sl.dataIndex] : null }}
              <button
                v-if="sl.dataIndex === 'BMOSDelete'"
                class="mini-btn"
                type="primary"
                size="mini"
                plain="true"
                @click="viewDelete(item)"
              >
                <uni-icons type="close" size="25" color="#FF4C26" />
              </button>
            </uni-td>
          </uni-tr>
        </uni-table>
      </scroll-view>
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
    <wd-toast />
    <BmosMessageBox
      v-model="messageOpen"
      :title="t('提示')"
      :sub-title="t('是否取消该物料件')"
      @confirm="deleteConfirm"
      @cancel="messageOpen = false"
    />
  </wd-popup>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { ref, watch, computed } from 'vue';
  import {
    scanWeighMaterialCodeApi,
    addConsumeStorageMaterialApi
  } from '@/api/weighingIngredientsApi.js';
  import { useToast } from 'wot-design-uni';
  import BmosMessageBox from '@/components/BmosMessageBox/index.vue';
  import { useScan } from '@/utils/useScan.js';
  const { bmosScanCode } = useScan();
  const toast = useToast();

  const props = defineProps({
    modelValue: {
      type: Boolean,
      default: false
    },
    detailData: {
      type: Object,
      default: () => ({})
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

  // 表格标题
  const tableLabel = ref([
    {
      label: '',
      align: 'center',
      dataIndex: 'BMOSDelete',
      width: '80'
    },
    {
      label: t('物料件号'),
      align: 'left',
      dataIndex: 'no',
      width: '190'
    },
    {
      label: t('物料量'),
      align: 'left',
      dataIndex: 'quantity',
      width: '190'
    },
    {
      label: t('单位'),
      align: 'left',
      dataIndex: 'unit',
      width: '190'
    },
    {
      label: t('水分%'),
      align: 'left',
      dataIndex: 'hydration',
      width: '190'
    },
    {
      label: t('含量%'),
      align: 'left',
      dataIndex: 'noHydrationContent',
      width: '190'
    },
    {
      label: t('有效期至'),
      align: 'left',
      dataIndex: 'expiredDate',
      width: '190'
    },
    {
      label: t('原厂批号'),
      align: 'left',
      dataIndex: 'factoryBatchNo',
      width: '190'
    },
    {
      label: t('供应商'),
      align: 'left',
      dataIndex: 'supplier',
      width: '190'
    }
  ]);

  // 表格数据
  const tableData = ref([]);
  const loading = ref(false);

  const materialPartId = ref('');
  // 物料件号搜索
  const searchMaterialCode = async(params) => {
    try {
      const res = await scanWeighMaterialCodeApi(params);
      if (tableData.value.some((item) => item.id === res.data.id)) {
        toast.show(t('物料件已添加，不能重复添加'));
        return;
      }
      tableData.value.push(res.data);
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'none'
        });
    }
  };
  // 扫描物料件号
  const iconClick = async() => {
    const success = async(res) => {
      const { result } = res;
      if (!result) {
        return;
      }
      const type = result.slice(0, 2);
      const code = result.slice(2);
      if (type === '03' || !code) {
        uni.showToast({
          title: t('请扫描正确的物料件号'),
          icon: 'none'
        });
        return;
      }
      // #ifdef APP-PLUS
      materialPartId.value = code;
      // #endif
      confirmPart(code);
    };
    // #ifdef APP-PLUS
    bmosScanCode({
      success,
      fail: (err) => {
        uni.showToast({
          title: t('扫码失败'),
          icon: 'none'
        });
      }
    });
    // #endif
    // #ifdef H5
    success({ result: materialPartId.value });
  // #endif
  };

  const confirmPart = (no) => {
    if (!materialPartId.value) {
      toast.show(t('请输入物料件号'));
      return;
    }
    searchMaterialCode({
      ingredientPlanId: props.detailData.ingredientPlanId,
      materialBatchId: props.detailData.storageMaterialBatchId,
      no: no
    });
  };
  const messageOpen = ref(false);
  const deleteData = ref({});
  // 删除
  const viewDelete = (data) => {
    deleteData.value = data;
    messageOpen.value = true;
  };
  // 确认删除
  const deleteConfirm = () => {
    tableData.value = tableData.value.filter(
      (item) => item.id !== deleteData.value.id
    );
    deleteData.value = {};
    messageOpen.value = false;
  };
  // 物料批次弹框关闭
  const close = () => {
    open.value = false;
  };
  // 物料批次弹框确认
  const confirm = async() => {
    if (!tableData.value.length) {
      toast.show(t('请添加物料件'));
      return;
    }
    const res = await addConsumeStorageMaterialApi({
      consumeStorateMaterialIdList: tableData.value.map((item) => item.id),
      ingredientPlanId: props.detailData.ingredientPlanId,
      storageMaterialBatchId: props.detailData.storageMaterialBatchId
    });
    close();
    emit('confirm');
  };
  watch(
    () => open.value,
    () => {
      if (open.value) {
        tableData.value = [];
        materialPartId.value = '';
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

  .input-class {
    padding: 0 9.38rpx;
    box-sizing: border-box;
    display: flex;
    justify-content: end;
    margin-bottom: 9.38rpx;
    .right {
      border-radius: 4.69rpx;
      background: #f7f8fa;
      padding: 7.38rpx 9.38rpx;
      align-items: center;
      display: flex;

      .custom-input {
        flex: 1;
        :deep(.wd-input__body) {
          height: 100%;
          padding: 7.03rpx 9.38rpx;
          box-sizing: border-box;
          border-bottom: none;
          background-color: #f7f8fa;
          .wd-input__value {
            height: 100%;
          }
        }
      }
      .scan-icon-box {
        width: 31.64rpx;
        height: 21.09rpx;
        display: flex;
        align-items: center;
        border-left: 1px solid #e1e3e5;
        padding-left: 9.38rpx;
        box-sizing: border-box;
      }
    }
  }
  .table-box {
    width: 100%;
    height: calc(100% - 75.92rpx);

    :deep .uni-table {
      height: 100%;
      display: flex;
      flex-direction: column;

      .uni-table-tr {
        border-bottom: 1.17rpx #ebeef5 solid;
      }

      .tr-tab {
        background-color: #fafafa;

        .th-tab {
          padding: 7.03rpx 9.38rpx;
          font-size: 11.72rpx;
          font-style: normal;
          border: 0;
          font-weight: 513;
          /* 次要文字20px */
          font-family: "思源黑体 CN";
          color: var(---, #606266);
        }
      }

      .uni-table-td {
        word-wrap: break-word;
        padding: 7.03rpx 9.38rpx;
        border: 0;
        color: var(---, #242526);
        /* 次要文字20px */
        font-family: "思源黑体 CN";
        font-size: 11.72rpx;
        font-style: normal;
        font-weight: 513;
        word-break: break-word;

        .mini-btn {
          display: flex;
          align-items: center;
          justify-content: center;
          border: 0;
          padding: 0;
          font-size: 11.72rpx;
          font-style: normal;
          font-weight: 513;
          line-height: normal;
        }
      }

      .uni-table-loading {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
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
