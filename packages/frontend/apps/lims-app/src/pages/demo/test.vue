<template>
  <view style="height: 100%;background-color: antiquewhite;" @click="closeOutside">
    <BMSign
      ref="BMSignRef"
      v-model="signValue"
      :field-names="{
        value: 'value',
        label: 'text',
        id: 'userId',
      }"
      :label-list="labelList"
    />

    <wd-button size="small" @click="submit">提交</wd-button>
    <wd-notify />
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  import { BMSign } from '@/BMComponents';
  import { useQueue } from 'wot-design-uni';

  const { closeOutside } = useQueue();
  const BMSignRef = ref();
  const signValue = ref({
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
    remark: ''
  });
  const labelList = ref([
    {
      label: '撒上',
      // 签名动作
      signatureAction: 0,
      options: null,
      disabled: false,
      menuId: 121010001002005
    },
    {
      label: '大大',
      // 签名动作
      signatureAction: 0,
      options: null,
      disabled: true,
      menuId: 121010001002006
    }
  ]);

  const submit = async() => {
    try {
      const res = await BMSignRef.value.checkSign();
      console.log(111111, res);
    } catch (error) {
      console.log('catch', error);
    }
  };
</script>

<style lang="scss" scoped></style>
