<!-- 物料预定 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料预定')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <InfoTable :details="details" :data="urlQuery" :title="t('物料信息')" />
      <BMForm ref="formsRef" v-bind="formProps">
        <template #formHeader>
          <view class="form-title">{{ t("预定生产信息") }}</view>
        </template>
      </BMForm>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :title="t('签名确认')"
      :signature-data="signatureData"
      :label-list="labelList"
      :field-names="{ value: 'loginName', label: 'userName', id: 'userId' }"
      @confirm="signSubmit"
    />
  </BMLayout>
</template>
<script setup>
  import { ref } from 'vue';
  import { BMBasicPage, BMLayout, BMSignModal, BMForm } from '@/BMComponents';
  import { t } from '@/utils/useBmosI18n.js';
  import InfoTable from '@/pages/inventoryManagement/components/infoTable/index.vue';
  import {
    getStorageMaterialReserveApi,
    getQueryPositionBoundUserListByPermissionCodeApi
  } from '@/api/storage.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { useData } from './hooks/useData';
  import { useNotify } from 'wot-design-uni';

  const { details, formsRef, formProps } = useData();
  const { showNotify } = useNotify();

  const labelList = ref([
    {
      label: t('操作人'),
      signatureAction: 72,
      disabled: true
    },
    {
      label: t('复核人'),
      signatureAction: 73,
      options: []
    }
  ]);
  const urlQuery = ref({});
  const signatureData = ref({});
  const loading = ref(false);
  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: ''
  });
  const toBack = () => {
    uni.navigateBack();
  };
  onLoad((e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map((key) => [
        decodeURIComponent(key),
        decodeURIComponent(e[key])
      ])
    );
    urlQuery.value = { ...query };
    // #endif
    // #ifdef H5
    urlQuery.value = { ...e };
    // #endif
    getSignUser();
  });
  // 物料预定提交
  const submit = async() => {
    const values = await formsRef.value.validate();
    signatureData.value = {
      storageMaterialId: urlQuery.value.id,
      ...values
    };
    showSign.value = true;
  };
  const signSubmit = async() => {
    try {
      loading.value = true;
      await getStorageMaterialReserveApi({
        operatorId: signValue.value.userId1,
        reCheckerId: signValue.value.userId2,
        storageMaterialId: urlQuery.value.id,
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
        permissionCode: '121020002000019'
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
