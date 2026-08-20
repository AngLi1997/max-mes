<template>
  <BMLayout>
    <BMBasicPage
      :title="t('首页手写签名设置')"
      :disabled-confirm="!hasChanged"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <BMHandleWriteSign ref="handleSignRef" :sign-url="signUrl" @change="writeSignChange" />
    </BMBasicPage>
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :label-list="labelList" 
      :signature-data="curParams"
      :title="t('确认保存手写签名')"
      @confirm="signConfirm"
    />
  </BMLayout>
</template>

<script setup>
  import { BMBasicPage, BMSignModal, BMHandleWriteSign, BMLayout } from '@/BMComponents/index.js';
  import { t } from '@/utils/useBmosI18n.js';
  import { ref } from 'vue';
  import { reqSignatureSaveApi, reqSignatureInfoApi } from '@/api/';
  import { useToast } from 'wot-design-uni';
  import { onLoad } from '@dcloudio/uni-app';
  import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
  import { USER_INFO } from '@/utils/uniStorage/const.js';

  const toast = useToast();

  const toBack = () => {
    uni.navigateBack();
  };

  const handleSignRef = ref(null);

  const fileBase64Content = ref('');
  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: ''
  });
  const labelList = ref([
    {
      label: t('操作人'),
      // 签名动作
      signatureAction: 83,
      disabled: true,
      options: null
    }
  ]);
  const curParams = ref({});
  const submit = async() => {
    try {
      const base64Data = await handleSignRef.value.save();
      fileBase64Content.value = base64Data;
      showSign.value = true;
      curParams.value = {
        fileBase64Content: base64Data,
        suffix: '.png'
      };
    } catch (error) {
      fileBase64Content.value = '';
    }
  };

  const signConfirm = async() => {
    try {
      await reqSignatureSaveApi({
        fileBase64Content: fileBase64Content.value,
        suffix: '.png',
        userId: signValue.value.userId1
      });
      showSign.value = false;
      hasChanged.value = false;
      toBack();
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };

  const currentUser = getStorageSync(USER_INFO) || {};
  const { userId } = currentUser;

  const signUrl = ref('');
  const getSignInfo = async() => {
    try {
      const { data } = await reqSignatureInfoApi(userId);
      signUrl.value = data;
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };

  const hasChanged = ref(false);
  const writeSignChange = () => {
    hasChanged.value = true;
  };

  onLoad(() => {
    getSignInfo();
  });
</script>

<style lang="scss" scoped>
</style>
