<template>
  <NormalModalForm wrapClassName="modalSizeExtraLarge" v-model:open="open" :title="t('导入')" destroyOnClose>
    <Steps
      style="width: 100%"
      :current="current"
      :items="[
        {
          title: t('上传文件'),
        },
        {
          title: t('导入确认'),
        },
        {
          title: t('结果查看'),
        },
      ]"
      labelPlacement="horizontal"
      size="small"></Steps>
    <component ref="stepRef" :count="successCount" style="height: 462px" :is="currentComponent"></component>
    <template #footer>
      <template v-if="current === 0">
        <Button @click="open = false">{{ t('取消') }}</Button>
        <Button type="primary" @click="nextStepHandle(1)">{{ t('下一步') }}</Button>
      </template>
      <template v-if="current === 1">
        <Button @click="upStepHandle">{{ t('上一步') }}</Button>
        <Button @click="checkHandle">{{ t('校验') }}</Button>
        <Button type="primary" @click="nextStepHandle(2)">{{ t('下一步') }}</Button>
      </template>
      <template v-if="current === 2">
        <Button @click="upStepHandle">{{ t('上一步') }}</Button>
        <Button type="primary" @click="okHandle">{{ t('确定') }}</Button>
      </template>
    </template>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import type { Component } from 'vue';
  import StepOne from './StepOne.vue';
  import StepTwo from './StepTwo.vue';
  import StepThree from './StepThree.vue';
  import { NormalModalForm } from '@bmos/components';

  const open = defineModel<boolean>('open', { default: false });
  const current = ref<number>(0);
  const successCount = ref<number>(0);
  const componentArr: Component[] = [StepOne, StepTwo, StepThree];
  const currentComponent = computed(() => {
    return componentArr[current.value];
  });
  const upStepHandle = () => {
    current.value--;
  };

  const stepRef = ref<StepOne | StepTwo | StepThree>();
  const nextStepHandle = async (num: number) => {
    if (current.value === 0) {
      console.log(stepRef.value);
      const formValue = await stepRef.value?.getFormValue();
      console.log(formValue);
      current.value = num;
      return;
    }
    current.value = num;
  };

  const checkHandle = () => {
    open.value = false;
  };
  const okHandle = () => {
    open.value = false;
    current.value = 0;
  };
</script>

<style lang="less" scoped></style>
