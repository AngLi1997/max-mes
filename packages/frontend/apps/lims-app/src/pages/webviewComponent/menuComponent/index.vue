<template>
  <BMLayout>
    <BMModal
      v-model="open"
      :title="t('更多功能')"
      size="small"
      position="right"
      closable
      :close-on-click-modal="false"
      hidden-button
      @close="close"
    >
      <view class="menu-container">
        <view
          v-for="(item, index) in menuList"
          v-show="item.show"
          :key="index"
          class="menu-item"
          @click="menuItemClick(item)"
        >
          <BMIcon
            :name="item.icon"
            size="18.76rpx"
            class-prefix="bmos-app-icon"
          />
          <view class="text-box">
            <text class="text">
              {{ item.text }}
            </text>
          </view>
        </view>
      </view>
    </BMModal>
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      v-model:current-time="currentTime"
      :label-list="labelList"
      :title="signTitle"
      :signature-data="signatureData"
      @cancel="cancelSign"
      @confirm="confirmSign"
    />

    <BMMessageBox
      v-model="showInputNull"
      :content="t('是否批量录入空值')"
      :cancel-text="t('否')"
      :confirm-text="t('是')"
      @cancel="showInputNull = false"
      @confirm="inputNullCongig"
    />
  </BMLayout>
</template>

<script setup>
import {
  postCopyRecordItemApi,
  postDiscardRecordItemApi,
} from '@/api/webViewApi.js';
import { BMMessageBox } from '@/BMComponents';
import { BMIcon, BMLayout, BMModal, BMSignModal } from '@/BMComponents/index.js';
import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
import {
  checkBusinessComponentData,
  formulaCalculate,
  getCacheComponentsData,
  getCopyRecordItemList,
  getCurrentCopyRecordItem,
  getNotInvalidCopyRecordCount,
  initFillData2,
  newSignOptionsRef,
  pageBasicDataRef,
  productionRevision,
  setEmptyValueForComponents,
  urlQueryRef,
  viewOnly,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  H5AppNavigateBack,
  showTakePhotoPopupRef,
} from '@/pages/webview/utils/index.js';
import { usePermissionStore } from '@/stores/permission.js';
import { isEmptyObject } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

const { hasPermission } = usePermissionStore();
const open = ref(true);
const labelList = ref([
  {
    label: t('操作人'),
    signatureAction: 5,
    disabled: true,
  },
  {
    label: t('复核人'),
    signatureAction: 6,
    options: [],
  },
]);

useSubNvueLinster('page-menuComponent', () => {
  labelList.value[1].options = newSignOptionsRef.value;
});

const activeKey = ref('photograph');
const loading = ref(false);
const showInputNull = ref(false);
const showSign = ref(false);
const currentTime = ref('');

const signValue = ref({
  loginName1: '',
  loginName2: '',
  userName1: '',
  userName2: '',
  password1: '',
  password2: '',
  userId1: '',
  userId2: '',
});
const signTitleMap = new Map([
  ['photograph', t('拍照取证')],
  ['copy', t('是否复制记录')],
  ['invalid', t('是否作废记录')],
]);
const signTitle = computed(() => {
  return signTitleMap.get(activeKey.value);
});

const signatureData = computed(() => {
  const copyRecordItem = getCurrentCopyRecordItem();
  if (activeKey.value === 'copy') {
    return {
      batchNo: urlQueryRef.value.batchNo,
      copyVersion: copyRecordItem.version,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      processId: urlQueryRef.value.processId,
      processVersion: urlQueryRef.value.processVersion,
      productPlanId: urlQueryRef.value.productPlanId,
      recordItemId: pageBasicDataRef.value.recordItemId,
      reuse: pageBasicDataRef.value.reusable,
      processChangeNumber: urlQueryRef.value.processChangeNumber,
      procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
      recordVersionId: pageBasicDataRef.value.recordVersionId,
    };
  }
  if (activeKey.value === 'invalid') {
    return {
      copyVersion: copyRecordItem.version,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      productPlanId: urlQueryRef.value.productPlanId,
      recordItemId: pageBasicDataRef.value.recordItemId,
      reuse: pageBasicDataRef.value.reusable,
    };
  }

  return {};
});

const menuList = ref([
  {
    key: 'photograph',
    icon: 'zhaoxiang',
    text: t('拍照取证'),
    show: hasPermission(121010001001006) || productionRevision.value,
  },
  //  {
  // 	key: 'modify',
  // 	icon: 'chakan',
  // 	text: t('修改审阅')
  // }, {
  // {
  //   key: 'viewProgress',
  //   icon: 'chakangongxu',
  //   text: t('查看工序'),
  //   show: hasPermission(121010001001010)
  // },
  {
    key: 'viewTechnology',
    icon: 'chakangongyi',
    text: t('查看工艺'),
    show: hasPermission(121010001001011) && !viewOnly.value,
  },
  // 	key: 'manual',
  // 	icon: 'caozuoshouce',
  // 	text: t('操作手册')
  // },
  {
    key: 'copy',
    icon: 'fuzhi',
    text: t('复制记录'),
    show: !viewOnly.value && hasPermission(121010001001007),
  },
  // {
  // 	key: 'deviation',
  // 	icon: 'piancha',
  // 	text: t('偏差上报')
  // },
  {
    key: 'invalid',
    icon: 'zuofei',
    text: t('记录作废'),
    show: !viewOnly.value && hasPermission(121010001001008),
  },
  {
    key: 'operating',
    icon: 'caozuoshouce',
    text: t('操作规程'),
    show: hasPermission(121010001001009) || productionRevision.value,
  },
  {
    key: 'exception',
    icon: 'piancha',
    text: t('异常填报'),
    show: hasPermission(121010001001012),
  },
  {
    key: 'inputNull',
    icon: 'lurukongzhi',
    text: t('录入空值'),
    show: hasPermission(121010001001015) && !viewOnly.value,
  },
  {
    key: 'calculation',
    icon: 'gongshijisuan',
    text: t('公式试算'),
    show: !viewOnly.value,
  },
]);

const close = () => {
  H5AppNavigateBack();
};

const menuActions = {
  photograph: () => {
    // #ifdef APP-PLUS
    uni.navigateTo({
      url: '/pages/webviewPopups/TakePhotosToCollectEvidencePage/index',
    });
    // #endif
    // #ifdef H5
    showTakePhotoPopupRef.value = true;
    // #endif
  },
  operating: () => {
    uni.navigateTo({
      url: '/pages/webviewPopups/OperatingProceduresList/index',
    });
  },
  modify: () => {
    console.log('修改审阅');
  },
  manual: () => {
    console.log('操作手册');
  },
  copy: () => {
    const cacheData = getCacheComponentsData();
    if (!isEmptyObject(cacheData)) {
      showNotify({
        type: 'warning',
        message: t('数据未保存，请先保存'),
      });
      return;
    }
    activeKey.value = 'copy';
    labelList.value[0].signatureAction = 5;
    labelList.value[1].signatureAction = 6;
    showSign.value = true;
  },
  deviation: () => {
    console.log('偏差上报');
  },
  invalid: () => {
    const copyRecordItem = getCurrentCopyRecordItem();
    if (copyRecordItem.discard) {
      showNotify({
        type: 'warning',
        message: t('该记录项已作废'),
      });
      return;
    }
    const count = getNotInvalidCopyRecordCount();
    if (count === 1) {
      showNotify({
        type: 'warning',
        message: t('仅当前记录页未作废，无法执行作废操作'),
      });
      return;
    }
    activeKey.value = 'invalid';
    labelList.value[0].signatureAction = 7;
    labelList.value[1].signatureAction = 8;
    showSign.value = true;
  },
  exception: () => {
    uni.navigateTo({
      url: '/pages/webviewPopups/addException/index',
    });
  },
  viewProgress: () => {
    uni.navigateTo({
      url: '/pages/webview/quickEntry',
    });
  },
  viewTechnology: () => {
    uni.navigateTo({
      url: '/pages/webview/quickEntry',
    });
  },
  inputNull: () => {
    showInputNull.value = true;
  },
  // 公式试算
  calculation: async () => {
    if (loading.value) {
      return;
    }
    loading.value = true;
    await formulaCalculate();
    loading.value = false;
    showNotify({
      type: 'success',
      message: t('公式试算成功'),
    });
    setTimeout(() => {
      close();
    }, 700);
  },
};
const menuItemClick = (item) => {
  menuActions[item.key]();
};
const inputNullCongig = () => {
  setEmptyValueForComponents();
  H5AppNavigateBack();
};

const copyConfirm = async () => {
  try {
    await postCopyRecordItemApi(signatureData.value);
    showNotify({
      type: 'success',
      message: t('复制记录项成功'),
    });
    return true;
  }
  catch (e) {
    return false;
  }
};

const invalidConfirm = async () => {
  try {
    await postDiscardRecordItemApi(signatureData.value);
    showNotify({
      type: 'success',
      message: t('作废记录项成功'),
    });
    return true;
  }
  catch (e) {
    return false;
  }
};

const actionToApi = {
  copy: copyConfirm,
  invalid: invalidConfirm,
};
const cancelSign = () => {
  showSign.value = false;
};
const confirmSign = async () => {
  const res = await actionToApi[activeKey.value]();
  if (res) {
    await getCopyRecordItemList({
      lastPage: activeKey.value === 'copy',
    });
    await checkBusinessComponentData();
    initFillData2();
    showSign.value = false;
    setTimeout(() => {
      H5AppNavigateBack();
    }, 500);
  }
};
</script>

<style>
page {
  background: transparent;
}
</style>

<style lang="scss" scoped>
.menu-container {
  width: 280px;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: auto;
  .menu-item {
    height: 43.96rpx;
    width: 100%;
    display: flex;
    align-items: center;

    .text-box {
      flex: 1;
      height: 100%;
      margin-left: 9.38rpx;
      border-bottom: 1px solid #e1e3e5;
      display: flex;
      align-items: center;

      .text {
        font-size: 14.07rpx;
        color: #545659;
      }
    }
  }
}
</style>
