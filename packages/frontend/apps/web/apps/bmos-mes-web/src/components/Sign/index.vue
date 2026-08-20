<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"
    @cancelModal="cancelModal"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Recordable, UserList, signEmits, signProps } from './type';
  import { sso } from '@bmos/messager';
  import { mesSignatureValidateV2 } from '@/services';
  import { Alert, message } from 'ant-design-vue';
  import { encrypt } from '@bmos/utils';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const props = defineProps(signProps);
  const emit = defineEmits(signEmits);

  const open = computed({
    get: () => {
      return props.open || false;
    },
    set: (val: boolean) => {
      emit('update:open', val);
    },
  });
  const cancelModal = () => {
    emit('cancelModal');
  };

  const submit = async (formValues: Recordable) => {
    try {
      const { getUserInfo } = sso;
      const userInfo = getUserInfo();
      const { receiverPassword, submitter, submitterPassword } = formValues;
      const { data } = await mesSignatureValidateV2({
        ...(props.labelList.length === 1
          ? {
              validates: [
                {
                  loginName: userInfo.loginName,
                  password: encrypt(receiverPassword),
                  signatureAction: props.labelList[0].action ? props.labelList[0].action : props.signatureAction,
                },
              ],
            }
          : {
              validates: [
                {
                  loginName: userInfo.loginName,
                  password: encrypt(receiverPassword),
                  signatureAction: props.labelList[0].action ? props.labelList[0].action : props.signatureAction,
                },
                {
                  loginName: submitter,
                  password: encrypt(submitterPassword),
                  signatureAction: props.labelList[1].action ? props.labelList[1].action : props.signatureAction,
                },
              ],
            }),
        signatureData: props.signatureData,
        signatureType: props.signatureType,
        systemCode: props.systemCode,
        ...(props.remark?.length ? { remark: props.remark } : {}),
      });
      const { failedIndex } = data;
      if (failedIndex.length > 0) {
        if (failedIndex[0] === 0) {
          message.error(props.labelList[0].label + t('密码错误'));
        } else if (failedIndex[0] === 1) {
          message.error(props.labelList[1].label + t('密码错误'));
        } else {
          message.error(t('签名失败'));
        }
        return Promise.reject(false);
      }
      emit('signSuccess', formValues);
      open.value = false;
      return Promise.resolve(true);
    } catch (error) {
      return Promise.reject(false);
    }
  };

  const signModalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'fieldAlert',
        noLabel: true,
        vIf: props.showAlert,
        component: () => (
          <Alert message={props?.alertDesc} type={props?.alertType} showIcon icon={<ExclamationCircleOutlined />} />
        ),
      },
      {
        field: 'receiver',
        component: 'Input',
        label: t('接收人'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'receiverPassword',
        component: 'InputPassword',
        label: t('密码'),
        required: true,
      },
      {
        field: 'divider',
        component: 'Divider',
        label: '',
        colProps: {
          span: 24,
        },
      },
      {
        field: 'submitter',
        component: 'Select',
        label: t('递交人'),
        required: true,
        componentProps: {
          options: [],
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          onSelect: (value: string, option: any) => {
            signModalFormRef.value?.formRef?.setFormModel('submitterId', option.userId);
          },
        },
      },
      {
        field: 'submitterPassword',
        component: 'InputPassword',
        label: t('密码'),
        required: true,
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        try {
          if (props.labelList.length > 0) {
            // 如果length 为 1
            if (props.labelList.length === 1) {
              signModalFormRef.value?.formRef?.removeSchemaByFiled(['divider', 'submitter', 'submitterPassword']);
              signModalFormRef.value?.formRef?.updateSchema([
                {
                  field: 'receiver',
                  label: props.labelList[0].label,
                  componentProps: {
                    placeholder: props.labelList[0].label,
                  },
                },
              ]);
            } else {
              signModalFormRef.value?.formRef?.updateSchema([
                {
                  field: 'receiver',
                  label: props.labelList[0].label,
                  componentProps: {
                    placeholder: props.labelList[0].label,
                  },
                },
                {
                  field: 'submitter',
                  label: props.labelList[1].label,
                  componentProps: {
                    placeholder: props.labelList[1].label,
                    options: props.userList?.map((item: UserList) => {
                      return {
                        label: item.userName + '-' + item.loginName,
                        value: item.loginName,
                        userId: item.userId,
                      };
                    }),
                  },
                },
              ]);
            }
          } else {
            signModalFormRef.value?.formRef?.removeAllSchema();
          }
          const { getUserInfo } = sso;
          const userInfo = getUserInfo();
          signModalFormRef.value?.formRef?.setFieldsValue({
            receiver: userInfo?.userName + '-' + userInfo?.loginName,
          });
          signModalFormRef.value?.formRef?.setFormModel('receiverId', userInfo?.userId);
        } catch (error) {
          //
        }
      }
    },
  );
</script>
<style lang="less"></style>
