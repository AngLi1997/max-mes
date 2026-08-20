<!-- 签名 -->
<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="t('电子签名校验')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { encrypt } from '@bmos/utils';
  import { Recordable, signEmits, signProps } from './type';
  import { sso } from '@bmos/messager';
  import { getSignatureImg, mesSignatureValidateV2 } from '@/services';
  import { Modal, message } from 'ant-design-vue';
  import { computed, nextTick, ref } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    inheritAttrs: false,
  });

  const props = defineProps(signProps);
  const emit = defineEmits(signEmits);

  const { getUserInfo } = sso;
  const userInfo = getUserInfo();

  const open = ref<boolean>(false);

  const signatureData = ref<any>({});

  const singUrl = ref('');

  const openSign = async (record: any) => {
    try {
      const { data } = await getSignatureImg();
      if (!data) {
        Modal.warning({
          title: t('电子签名校验'),
          icon: h(ExclamationCircleOutlined),
          content: () => (
            <>
              <p>{t('该功能需要使用电子签名')}</p>
              <p>{t('请点击右上角头像，进入「签名设置」上传电子签名！')}</p>
            </>
          ),
        });
        return;
      }
      const path = data.split('/') ?? [];
      singUrl.value = path[path.length - 1];
      signatureData.value = record;
      open.value = true;
      await nextTick();
      signModalFormRef.value?.formRef?.setFormModel('loginName', userInfo?.userName + '-' + userInfo?.loginName);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const formProps = computed<FormProps>(() => ({
    baseColProps: {
      span: 24,
    },
    schemas: [
      // ...(props.extraSchemas as FormSchema<string>[] & FormSchema<any>[]),
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户名'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'password',
        component: 'InputPassword',
        label: t('验证密码'),
        required: true,
        componentProps: {
          autocomplete: 'new-password',
        },
      },
    ],
  }));

  const submit = async (formValues: Recordable) => {
    try {
      const { data } = await mesSignatureValidateV2({
        validates: [
          {
            loginName: userInfo.loginName,
            password: encrypt(formValues.password),
            signatureAction: props.signatureAction,
          },
        ],
        signatureData: JSON.stringify(signatureData.value),
        signatureType: props.signatureType,
        systemCode: props.systemCode,
        // remark: props.remark,
      });
      const { failedIndex } = data;
      if (failedIndex.length > 0) {
        if (failedIndex[0] === 0) {
          message.error(t('密码错误'));
        } else if (failedIndex[0] === 1) {
          message.error(t('密码错误'));
        } else {
          message.error(t('签名失败'));
        }
        return Promise.reject(false);
      }
      await props.afterSign(singUrl.value);
      emit('signSuccess', singUrl.value);
      open.value = false;
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const signModalFormRef = ref<ModalFormInstance>();

  const setFormModel = async (key: string, value: any) => {
    await nextTick();
    signModalFormRef.value?.formRef?.setFormModel(key, value);
  };

  defineExpose({
    signModalFormRef,
    openSign,
    setFormModel,
  });
</script>
<style lang="less"></style>
