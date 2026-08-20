<template>
  <Modal v-model:open="open" :title="t('修改密码')" :maskClosable="false" @cancel="cancel">
    <template #footer>
      <Button @click="cancel">{{ t('取消') }}</Button>
      <Button type="primary" @click="handleOk">{{ t('确定') }}</Button>
    </template>
    <Form
      ref="formRef"
      :model="formState"
      :rules="rules"
      :label-col="labelCol"
      class="changePassWord"
      autocomplete="off"
      :wrapper-col="wrapperCol">
      <Form.Item ref="oldPassWord" :label="t('旧密码')" name="oldPassWord">
        <BMPasswordInput v-model:value="formState.oldPassWord"></BMPasswordInput>
      </Form.Item>
      <Form.Item ref="newPassWord" :label="t('新密码')" name="newPassWord">
        <BMPasswordInput v-model:value="formState.newPassWord"></BMPasswordInput>
      </Form.Item>
      <Form.Item :label="t('确认密码')" name="checkPassWord">
        <BMPasswordInput v-model:value="formState.checkPassWord"></BMPasswordInput>
      </Form.Item>
    </Form>
  </Modal>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue';
  import type { UnwrapRef } from 'vue';
  import type { Rule } from 'ant-design-vue/es/form';
  import { t } from '@bmos/i18n';
  import { userManagerChangePwd } from '../../../../api/Permissions/menuPermissions';
  import { message, Form, Button, Modal } from 'ant-design-vue';
  import { BMPasswordInput } from '@bmos/components';
  import { encrypt } from '@bmos/utils';

  const props = defineProps({
    userId: {
      type: String,
      default: '',
    },
    id: {
      type: String,
      default: '',
    },
  });
  interface FormState {
    oldPassWord: string;
    newPassWord: string;
    checkPassWord: string;
  }
  const formRef = ref();
  const labelCol = { span: 5 };
  const wrapperCol = { span: 17, offset: 1 };
  const formState: UnwrapRef<FormState> = reactive({
    oldPassWord: '',
    newPassWord: '',
    checkPassWord: '',
  });
  // 验证新密码
  const validatePass1 = async (_rule: Rule, value: string) => {
    if (value === '') {
      return Promise.reject(t('新密码不能为空'));
    } else {
      return Promise.resolve();
    }
  };
  // 验证确认密码
  const validatePass2 = async (_rule: Rule, value: string) => {
    if (value === '') {
      return Promise.reject(t('确认密码不能为空'));
    } else if (value !== formState.newPassWord) {
      return Promise.reject(t('两次密码不一致'));
    } else {
      return Promise.resolve();
    }
  };
  // 表单校验
  const rules: Record<string, Rule[]> = {
    oldPassWord: [{ required: true, message: t('旧密码不能为空'), trigger: 'blur' }],
    newPassWord: [{ required: true, validator: validatePass1, trigger: 'blur' }],
    checkPassWord: [{ required: true, validator: validatePass2, trigger: 'blur' }],
  };

  const open = ref<boolean>(false);
  const showModal = () => {
    open.value = true;
  };
  // 修改密码确定
  const handleOk = async () => {
    const passwordForm = await formRef.value?.validate();
    try {
      const data = {
        id: props.id,
        oldPassword: encrypt(passwordForm.oldPassWord),
        newPassword: encrypt(passwordForm.checkPassWord),
      };
      const res: any = await userManagerChangePwd(data);
      if (res.code === 0) {
        message.success(t('修改成功'));
        formRef.value.resetFields();
        open.value = false;
      }
      if (res.code === 8104001) {
        message.error(t('当前密码不正确'));
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const cancel = () => {
    formRef.value.resetFields();
    open.value = false;
  };
  defineExpose({ showModal });
</script>

<style lang="less" scoped>
  .plat-modal-content {
    width: 430px !important;
    height: 260px !important;
  }
  .plat-modal .plat-modal-content {
    padding-right: 30px !important;
  }
  .changePassWord {
    margin-top: 40px !important;
  }
</style>
