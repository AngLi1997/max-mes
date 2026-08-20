<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :closable="false"
    :title="t('系统激活')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <div class="footer">
        <Button style="margin-right: 15px" @click="cancel">{{ t('关闭') }}</Button>
      </div>
    </template>
    <div class="authCodeContent">
      <Segmented v-model:value="value6" :options="data" block @change="changeTab" />
      <div class="systemState">{{ t('系统状态') }}</div>
      <!-- loading...效果区 -->
      <Spin :spinning="spinning">
        <div class="tagState">
          <Tag :color="colorList">{{ stateName }}</Tag>
          <span v-if="date && date !== 'ALL'" style="color: #2871ff">
            {{ date }}
            <span>{{ t('到期') }}</span>
          </span>
        </div>
      </Spin>

      <Form ref="formRef" :model="formState" :rules="rules">
        <FormItem ref="authCode" :labelCol="{ span: 24 }" :label="t('授权码')" name="authCode">
          <Textarea v-model:value="formState.authCode" :rows="3" :placeholder="t('请输入')"></Textarea>
        </FormItem>
      </Form>
      <div style="text-align: right">
        <Button type="primary" @click="activation">
          {{ t('激活') }}
        </Button>
      </div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import type { Rule } from 'ant-design-vue/es/form';
  import type { UnwrapRef } from 'vue';
  import {
    determinePlatformActived,
    determineMesActived,
    determineLimsActived,
    determineWmsActived,
    determineBsmsActived,
    determineBimsActived,
    determineLismsActived,
    platformActived,
    mesActived,
    limsActived,
    wmsActived,
    bsmsActived,
    bimsActived,
    lismsActived,
  } from '../../../../../login/api';
  import { ref, reactive, watch, nextTick } from 'vue';
  import { message, Form, Button, Segmented, Tag, Textarea, Spin } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import dayjs from 'dayjs';
  const props = defineProps({
    activateVerification: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits([
    'updatePlatform',
    'updateMes',
    'updateLims',
    'updateWms',
    'updateBsms',
    'updateBims',
    'updateLisms',
  ]);
  interface FormState {
    authCode: string;
  }
  const open = ref<boolean>(false);
  const data = ref<any>([]); //分段选择器
  const value6 = ref('');
  const formRef = ref();
  const spinning = ref<boolean>(false);
  const authCode = ref(); //授权码
  const date = ref(''); //日期 ALL代表永久激活 时间格式 yyyy-MM-dd HH:mm:ss 未激活为null
  const colorList = ref(''); //tag颜色
  const stateName = ref(''); //状态名称 永久授权 临时授权 未授权
  const canClosed = ref<boolean>(true); //关闭按钮是否可以关闭弹窗
  const currentTab = ref(t('制药管理平台')); //当前tab

  const formState: UnwrapRef<FormState> = reactive({
    authCode: '',
  });
  // 表单校验
  const rules: Record<string, Rule[]> = {
    authCode: [{ required: true, trigger: 'blur' }],
  };
  // 更新显示状态
  const determineState = () => {
    if (date.value === 'ALL') {
      colorList.value = '#59BF78';
      stateName.value = t('永久授权');
    }
    if (date.value && date.value !== 'ALL') {
      colorList.value = '#2871FF';
      stateName.value = t('临时授权');
    }
    if (!date.value) {
      colorList.value = '#FF5633';
      stateName.value = t('未授权');
    }
  };
  const showActivationMadal = () => {
    open.value = true;
  };
  // 关闭按钮
  const cancel = () => {
    if (canClosed.value) {
      open.value = false;
    } else {
      message.error(t('平台未授权或其授权码已过期'));
    }
  };
  // 激活按钮
  const activation = async () => {
    const contentForm = await formRef.value?.validate();
    try {
      if (currentTab.value == t('制药管理平台')) {
        const res: any = await platformActived(contentForm.authCode);
        date.value = res.data;
        determineTime(res.data);
        loading();
        emit('updatePlatform');
      }
      if (currentTab.value == t('制造执行系统')) {
        const res: any = await mesActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateMes');
      }
      if (currentTab.value == t('实验室信息管理系统')) {
        const res: any = await limsActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateLims');
      }
      if (currentTab.value == t('仓储管理系统')) {
        const res: any = await wmsActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateWms');
      }
      if (currentTab.value == t('170')) {
        const res: any = await bsmsActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateBsms');
      }
      if (currentTab.value == t('180')) {
        const res: any = await bimsActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateBims');
      }
      if (currentTab.value == t('210')) {
        const res: any = await lismsActived(contentForm.authCode);
        date.value = res.data;
        loading();
        emit('updateLisms');
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // loading效果
  const loading = () => {
    spinning.value = true;
    setTimeout(() => {
      spinning.value = false;
      message.success(t(' 激活成功'));
      determineState();
    }, 1000);
  };
  // 切换激活的tab
  const changeTab = async (val: any) => {
    console.log('切换');

    formState.authCode = '';
    formRef.value.resetFields();
    currentTab.value = val;
    switch (val) {
      case t('制药管理平台'):
        await determinePlatformActived1();
        determineState();
        break;
      case t('制造执行系统'):
        await determineMesActived1();
        determineState();
        break;
      case t('实验室信息管理系统'):
        await determineLimsActived1();
        determineState();
        break;
      case t('仓储管理系统'):
        await determineWmsActived1();
        determineState();
        break;
      case t('170'):
        await determineBsmsActived1();
        determineState();
        break;
      case t('180'):
        await determineBimsActived1();
        determineState();
        break;
      case t('210'):
        await determineLismsActived1();
        determineState();
        break;
      default:
        break;
    }
  };

  // 查平台的激活
  const determinePlatformActived1 = async () => {
    try {
      const res: any = await determinePlatformActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
        determineTime(res.data.date);
      } else {
        // 未激活
        date.value = '';
        canClosed.value = false;
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查mes的激活
  const determineMesActived1 = async () => {
    try {
      const res: any = await determineMesActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查lims的激活
  const determineLimsActived1 = async () => {
    try {
      const res: any = await determineLimsActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查wms的激活
  const determineWmsActived1 = async () => {
    try {
      const res: any = await determineWmsActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查Bsms的激活
  const determineBsmsActived1 = async () => {
    try {
      const res: any = await determineBsmsActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查Bims的激活
  const determineBimsActived1 = async () => {
    try {
      const res: any = await determineBimsActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查Lisms的激活
  const determineLismsActived1 = async () => {
    try {
      const res: any = await determineLismsActived({});
      if (res.data.active == true) {
        date.value = res.data.date;
      } else {
        date.value = '';
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 判断平台的授权码是否已过期
  const determineTime = (date: any) => {
    // 永久授权
    if (date === 'ALL') {
      canClosed.value = true;
      return;
    }
    const currentTime = dayjs(); //当前时间
    const backendTime = dayjs(date); //后端返回时间
    if (currentTime.isAfter(backendTime)) {
      canClosed.value = false; // (后端返回时间在当前时间之前)  授权码已经过期
    } else {
      canClosed.value = true; //授权码未过期
    }
  };
  const addSystem = (str: any) => {
    if (str.includes('PMP')) {
      data.value.push(t('制药管理平台'));
    }
    if (str.includes('MES')) {
      data.value.push(t('制造执行系统'));
    }
    if (str.includes('LIMS')) {
      data.value.push(t('实验室信息管理系统'));
    }
    if (str.includes('WMS')) {
      data.value.push(t('仓储管理系统'));
    }
    if (str.includes('BSMS')) {
      data.value.push(t('170'));
    }
    if (str.includes('BIMS')) {
      data.value.push(t('180'));
    }
    if (str.includes('LISMS')) {
      data.value.push(t('210'));
    }
    data.value = [...new Set(data.value)];
  };
  defineExpose({ showActivationMadal });
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        formState.authCode = '';
        addSystem(props.activateVerification);
        value6.value = data.value[0];
        await determinePlatformActived1();
        determineState();
      }
    },
    { immediate: true },
  );
</script>

<style lang="less" scoped>
  .authCodeContent {
    .systemState {
      margin: 10px 0 5px 0;
    }
    .tagState {
      padding: 10px 0;
    }
    :deep(.plat-form-item-label) {
      margin-bottom: -10px;
    }
  }
  .footer {
    padding-top: 15px;
    border-top: 1px solid #e4e4e4;
  }
</style>
