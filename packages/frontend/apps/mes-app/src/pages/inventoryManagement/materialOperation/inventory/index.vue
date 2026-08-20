<!-- 物料盘点 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料盘点')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <InfoTable :details="details" :data="urlQuery" :title="t('物料信息')" />
      <BMForm ref="formsRef" v-bind="formProps">
        <template #formHeader>
          <view class="form-title">{{ t("物料盘点") }}</view>
        </template>
      </BMForm>
      <!-- 签名 -->
      <BMSignModal
        v-model:show="showSign"
        v-model="signValue"
        :label-list="labelList"
        :title="t('签名确认')"
        :signature-data="signatureData"
        :field-names="{ value: 'loginName', label: 'userName', id: 'userId' }"
        @confirm="signSubmit"
      />
    </BMBasicPage>
  </BMLayout>
</template>
<script setup>
  import { ref, nextTick } from 'vue';
  import {
    BMLayout,
    BMSignModal,
    BMBasicPage,
    BMForm
  } from '@/BMComponents/index.js';
  import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
  import { t } from '@/utils/useBmosI18n.js';
  import {
    getStorageMaterialCheckApi,
    getQueryPositionBoundUserListByPermissionCodeApi
  } from '@/api/storage.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { useData } from './hooks/useData';
  import { useNotify } from 'wot-design-uni';
  import { useMathJs } from '@/utils/useMathJs.js';

  const { showNotify } = useNotify();
  const { math } = useMathJs();
  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
    loginName2: '',
    password2: '',
    userId2: ''
  });
  const labelList = ref([
    {
      label: t('盘点人'),
      signatureAction: 17,
      disabled: true
    },
    {
      label: t('复核人'),
      signatureAction: 74,
      options: []
    }
  ]);
  const loading = ref(false);
  const signatureData = ref({});
  const toBack = () => {
    uni.navigateBack();
  };
  const urlQuery = ref({});
  const { details, formsRef, formProps } = useData();
  onLoad((e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map((key) => [
        decodeURIComponent(key),
        decodeURIComponent(e[key])
      ])
    );
    urlQuery.value = {
      ...query
    };
    // #endif
    // #ifdef H5
    urlQuery.value = {
      ...e
    };
    // #endif
    nextTick(() => {
      formsRef.value.setFormModels({
        consumeQuantity: urlQuery.value.consumeQuantity, // 消耗量
        initQuantity: urlQuery.value.initQuantity // 初始量
      });
      getSignUser();
    });
  });

  // 物料盘点提交
  const submit = async() => {
    const values = await formsRef.value.validate();
    signatureData.value = {
      storageMaterialId: urlQuery.value.id, // 暂存物料件id
      availableQuantity: math.subtract(math.bignumber(values.initQuantity), math.bignumber(values.consumeQuantity)).toString(), // 可用量:初始量 - 消耗量// 可用量:初始量 - 消耗量
      ...values
    };
    console.log(signatureData.value);
    showSign.value = true;
  };
  // 签名点击提交,校验签名
  const signSubmit = async() => {
    if (loading.value) {
      return;
    }
    try {
      loading.value = true;
      await getStorageMaterialCheckApi({
        checkerId: signValue.value.userId1,
        reCheckerId: signValue.value.userId2,
        ...signatureData.value
      });
      showNotify({ type: 'success', message: t('提交成功') });
      showSign.value = false;
      uni.reLaunch({
        url: `/pages/inventoryManagement/inventoryInfo/index?materialPositionId=${
          urlQuery.value.materialPositionId
        }`
      });
    } catch (e) {
      showNotify({ type: 'danger', message: e.message });
    } finally {
      loading.value = false;
    }
  };
  // 获取签名人员
  async function getSignUser() {
    try {
      const res = await getQueryPositionBoundUserListByPermissionCodeApi({
        positionId: urlQuery.value.materialPositionId,
        permissionCode: '121020002000010'
      });
      labelList.value[1].options = res.data;
    } catch (error) {
      showNotify({ type: 'danger', message: error.message });
    }
  }
</script>

<style lang="scss" scoped>
.form-title {
  font-size: 14.06rpx;
  color: var(--bmos-text-main);
  margin: 11.72rpx 0;
}
</style>
