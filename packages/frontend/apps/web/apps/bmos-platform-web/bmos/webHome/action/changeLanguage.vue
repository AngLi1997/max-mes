<template>
  <Modal v-model:open="open" :title="t('语言设置')" @cancel="cancel">
    <template #footer>
      <Button @click="cancel">{{ t('取消') }}</Button>
      <Button type="primary" @click="handleOk">{{ t('确定') }}</Button>
    </template>
    <Form
      :model="formState"
      ref="formRef"
      :label-col="labelCol"
      autocomplete="off"
      :wrapper-col="wrapperCol">
      <Form.Item
        ref="languageValue"
        :label="t('系统语言')"
        name="languageValue">
        <Select
          :placeholder="t('请选择')"
          v-model:value="formState.languageValue"
          :options="languageList"
          @change="ChangeLanguage"
          style="width: 280px"></Select>
      </Form.Item>
    </Form>
  </Modal>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { t, changeLanguage } from '@bmos/i18n';
  import { Modal, message, Button, Select, Form } from 'ant-design-vue';
  import { getParameter } from '../../../src/api/Permissions/menuPermissions';
  const props = defineProps({
    userId: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['changeLang']);
  const formRef = ref();
  const labelCol = { span: 6 };
  const wrapperCol = { span: 19, offset: 0 };
  const formState = reactive({
    languageValue: '',
    // languageValue: '',
  });
  const languageList = ref([
    // { label: t('中文'), value: 'zh_CN' },
    // { label: t('英文'), value: 'en' },
    // { label: t('俄语'), value: 'ru_RU' },
  ]);
  const open = ref<boolean>(false);
  const showModal = () => {
    open.value = true;
  };
  // 切换语言下拉
  const ChangeLanguage = async (val: any) => {
    console.log(val);
  };
  // 切换语言确定
  const handleOk = async (e: MouseEvent) => {
    const res = await formRef.value?.validate();
    const data = res.languageValue;
    changeLanguage(res.languageValue);
    emit('changeLang', res.languageValue);
    open.value = false;
  };
  const cancel = (e: MouseEvent) => {
    formRef.value.resetFields();
    open.value = false;
  };
  // 获取语言下拉框
  const getlanguageList = async () => {
    try {
      const res: any = await getParameter('platform.sys.language');
      const obj = JSON.parse(res.data.value);
      languageList.value = Object.keys(obj).map((item, i) => {
        return {
          label: t(item),
          value: obj[item],
        };
      });
    } catch (error: any) {
      message.error(error.message);
      // console.log(error.message);
    }
  };
  defineExpose({ showModal });
  onMounted(() => {
    getlanguageList();
    // 回显下拉框语言
    const language = localStorage.getItem('LANG') || 'zh_CN';
    formState.languageValue = language;
  });
</script>

<style lang="less" scoped>
  .plat-form {
    margin-top: 30px;
    margin-bottom: 60px;
  }
  .plat-modal-content {
    width: 430px !important;
    height: 260px !important;
  }
  .plat-modal .plat-modal-content {
    padding-right: 30px !important;
  }
</style>
