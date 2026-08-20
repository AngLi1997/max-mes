<template>
  <BMModalForm
    ref="signModalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Recordable, UserList, signEmits, signProps } from './type';
  import { sso } from '@bmos/messager';
  import { mesSignatureValidateV2, reqPlatformUserListByMenuId } from '@/services';
  import { Alert, message } from 'ant-design-vue';
  import { encrypt, isNullOrUnDef } from '@bmos/utils';
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

  const submit = async (formValues: Recordable) => {
    try {
      const validates = props.labelList.map((item, index) => {
        return {
          loginName: formValues[`username${index}`],
          password: encrypt(formValues[`password${index}`]),
          signatureAction: item.action ? item.action : '1',
        };
      });
      const { data } = await mesSignatureValidateV2({
        validates,
        signatureData: props.signatureData,
        signatureType: props.signatureType,
        systemCode: props.systemCode,
        ...(props.remark?.length ? { remark: props.remark } : {}),
      });
      const { failedIndex } = data;
      if (failedIndex.length > 0) {
        const errorMessage = props.labelList[failedIndex[0]].label + t('密码错误');
        if (errorMessage) {
          message.error(errorMessage);
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
        field: 'remark',
        required: props.remarkRequired,
        component: 'InputTextArea',
        vIf: props.showRemark,
        label: t('备注'),
      },
    ],
  });

  const setUser = () => {
    const { getUserInfo } = sso;
    const userInfo = getUserInfo();
    signModalFormRef.value?.formRef?.setFormModels({
      [`userId0`]: userInfo?.userId,
      [`username0`]: userInfo?.loginName,
    });
  };

  defineExpose({
    setUser,
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        try {
          if (props.labelList.length < 1) {
            signModalFormRef.value?.formRef?.removeAllSchema();
            return;
          }
          const { getUserInfo } = sso;
          const userInfo = getUserInfo();
          props.labelList.forEach((item, index) => {
            signModalFormRef.value?.formRef?.appendSchemaByField({
              field: 'userId' + index,
              label: item.label,
              component: 'Select',
              required: true,
              componentProps: ({ formModel }: RenderCallbackParams) => {
                return {
                  filterOption: (input: string, option: any) => {
                    return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
                  },
                  ...(isNullOrUnDef(item.disabled) && index === 0
                    ? {
                        disabled: true,
                      }
                    : { disabled: item.disabled }),
                  options: item.options,
                  onSelect: (_value: string, option: any) => {
                    signModalFormRef.value?.formRef?.setFormModel('username' + index, option.loginName);
                  },
                  showSearch: true,
                  ...(item.menuId
                    ? {
                        request: async () => {
                          const { data } = await reqPlatformUserListByMenuId(item.menuId as string);
                          if (!isNullOrUnDef(item.currentUser) && item.currentUser) {
                            if (data.findIndex((userItem: UserList) => userItem.userId === userInfo?.userId) !== -1) {
                              formModel[`userId${index}`] = userInfo?.userId;
                              formModel[`username${index}`] = userInfo?.loginName;
                            }
                          }
                          return data.map((userItem: UserList) => {
                            return {
                              label: userItem.userName + '-' + userItem.loginName,
                              value: userItem.userId,
                              ...userItem,
                            };
                          });
                        },
                      }
                    : {}),
                };
              },
            });
            signModalFormRef.value?.formRef?.appendSchemaByField({
              field: 'password' + index,
              label: t('密码'),
              component: 'InputPassword',
              required: true,
            });
            // 如果是最后一个
            if (index !== props.labelList.length - 1) {
              signModalFormRef.value?.formRef?.appendSchemaByField({
                field: 'divider',
                component: 'Divider',
                label: '',
                colProps: {
                  span: 24,
                },
              });
            }
            if ((isNullOrUnDef(item.disabled) && index === 0) || item.disabled) {
              signModalFormRef.value?.formRef?.updateSchema({
                field: 'userId' + index,
                componentProps: {
                  options: [
                    {
                      label: userInfo?.userName + '-' + userInfo?.loginName,
                      value: userInfo?.userId,
                      ...userInfo,
                    },
                  ],
                  disabled: true,
                },
              });
              signModalFormRef.value?.formRef?.setFormModels({
                [`userId${index}`]: userInfo?.userId,
                [`username${index}`]: userInfo?.loginName,
              });
            }
          });
        } catch (error) {
          //
        }
      }
    },
  );
</script>
<style lang="less"></style>
