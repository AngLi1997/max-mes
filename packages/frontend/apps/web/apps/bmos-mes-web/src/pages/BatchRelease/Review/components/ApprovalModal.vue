<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :title="t('审核处理')"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { sso } from '@bmos/messager';
  import {
    mesAuditComplete,
    mesAuditCompleteNotApprove,
    mesSignatureValidate,
    getFlowListMakeUser,
    mesAuditBackToPrev,
  } from '@/services';
  import { encrypt } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'action', type: string): void;
  }>();

  const open = defineModel<boolean>('approvalModalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      settings?: string;
      taskId?: string;
      processInstanceId?: string;
      nodeId?: string;
      deploymentId?: string;
      executionId?: string;
    }>(),
    {
      settings: '{}',
      executionId: '',
      nodeId: '',
      deploymentId: '',
      processInstanceId: '',
      taskId: '',
    },
  );

  const jsonSettings = ref(JSON.parse(props.settings || '{}'));

  // 是否需要密码验证
  const needPwdValidate = computed(() => {
    return jsonSettings.value?.needPwdValidate || false;
  });
  // 审核意见是否必填
  const needCommit = computed(() => {
    return jsonSettings.value?.needCommit || false;
  });
  // 备注是否必填
  const needRemark = computed(() => {
    return jsonSettings.value?.needRemark || false;
  });

  // 是都显示抄送人
  const needCopyTo = computed(() => {
    return jsonSettings.value?.buttons?.includes('copyTo') || false;
  });

  const passFn = async (params: any) => {
    try {
      await mesAuditComplete(params);
      emit('action', 'pass');
      message.success(t('审核通过'));
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const rejectFn = async (params: any) => {
    try {
      await mesAuditCompleteNotApprove(params);
      emit('action', 'reject');
      message.success(t('审核不通过'));
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const returnToFn = async (params: any) => {
    try {
      const newParams = {
        ...params,
        executionId: props.executionId,
      };
      await mesAuditBackToPrev(newParams);
      emit('action', 'returnTo');
      message.success(t('回退成功'));
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const titleMap: Map<
    string,
    {
      operation?: (params: any) => Promise<any>;
      defaultValue: string;
    }
  > = new Map([
    [
      'pass',
      {
        operation: passFn,
        defaultValue: t('审核通过'),
      },
    ],
    [
      'reject',
      {
        operation: rejectFn,
        defaultValue: t('审核不通过'),
      },
    ],
    [
      'deliverTo',
      {
        defaultValue: t('转交'),
      },
    ],
    [
      'returnTo',
      {
        operation: returnToFn,
        defaultValue: t('审核退回'),
      },
    ],
    // [
    //   'copyTo',
    //   {
    //     title: t('抄送'),
    //     desc: `${t('是否抄送当前审核任务')}?`,
    //   },
    // ],
  ]);

  const signatureValidate = async (loginName: string, password: string, signatureData: Recordable) => {
    try {
      const { getUserInfo } = sso;
      const userInfo = getUserInfo();
      if (userInfo?.loginName !== loginName) {
        return Promise.reject({ message: t('用户名与当前登录用户不一致') });
      }
      const { code, data } = await mesSignatureValidate({
        validates: [
          {
            loginName,
            password: encrypt(password),
            signatureAction: 12,
          },
        ],
        systemCode: '120',
        signatureType: 0,
        signatureData: JSON.stringify(signatureData),
        signatureAction: 12,
        remark: signatureData.remark,
      });
      if (data === true && code === 0) {
        return Promise.resolve();
      } else {
        return Promise.reject({ message: t('密码验证失败') });
      }
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const submit = async (formModal: Recordable) => {
    try {
      const approvalParams = {
        taskId: props.taskId,
        processInstanceId: props.processInstanceId,
        comment: formModal.comment,
        remark: formModal.remark,
      };
      if (formModal.loginName && formModal.password) {
        await signatureValidate(formModal.loginName, formModal.password, approvalParams);
      }
      if (titleMap.get(formModal.curType)?.operation) {
        const operation = titleMap.get(formModal.curType)?.operation;
        if (operation) {
          await operation(approvalParams);
        }
      }
      sendMessage(MessageType.UpdateMessageCount);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };

  const btnMap = new Map([
    ['pass', t('审核通过')],
    ['reject', t('审核不通过')],
    ['deliverTo', t('转交')],
    ['returnTo', t('回退')],
    // ['copyTo', t('抄送')],
  ]);
  const modalFormRef = ref<ModalFormInstance>();
  const formProps = computed(() => {
    return {
      initialValues: {},
      schemas: [
        {
          field: 'curType',
          component: 'RadioGroup',
          label: t('审核结论'),
          defaultValue: 'pass',
          componentProps: {
            request: async () => {
              const buttons = jsonSettings.value?.buttons;
              return buttons.map((item: string) => {
                return {
                  value: item,
                  label: btnMap.get(item) || '',
                };
              });
            },
            onChange: (e: any) => {
              if (!e.target.value) {
                return;
              }
              modalFormRef.value?.formRef?.setFieldsValue({
                comment: titleMap.get(e.target.value)?.defaultValue,
              });
            },
          },
        },
        {
          field: 'copyTo',
          component: 'Select',
          label: t('抄送人员'),
          vIf: () => needCopyTo.value,
          componentProps: ({ formModel }: RenderCallbackParams) => {
            return {
              maxTagCount: 'responsive',
              mode: 'multiple',
              allowClear: false,
              request: async () => {
                let result: any = [];
                try {
                  const { data } = await getFlowListMakeUser(props.nodeId, props.deploymentId);
                  result = data.map((item: any) => {
                    return {
                      label: item,
                      disabled: true,
                      value: item,
                    };
                  });
                } catch (error) {
                  result = [];
                }
                formModel.copyTo = result.map((item: any) => item.value);
                return result;
              },
            };
          },
        },
        {
          field: 'comment',
          component: 'InputTextArea',
          label: t('审核意见'),
          required: needCommit.value || false,
          componentProps: {
            maxlength: 50,
          },
        },
        {
          field: 'remark',
          component: 'InputTextArea',
          label: t('备注'),
          required: needRemark.value || false,
          componentProps: {
            maxlength: 50,
          },
        },
        {
          field: 'field6',
          component: 'Divider',
          vIf: () => needPwdValidate.value,
          colProps: {
            span: 24,
          },
        },
        {
          field: 'loginName',
          component: 'Input',
          label: t('用户名'),
          vIf: () => needPwdValidate.value,
          required: true,
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'password',
          component: 'InputPassword',
          label: t('密码'),
          vIf: () => needPwdValidate.value,
          required: true,
          componentProps: {
            autocomplete: 'new-password',
          },
        },
      ],
    } as FormProps;
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        jsonSettings.value = JSON.parse(props.settings || '{}');
        await nextTick();
        try {
          const { getUserInfo } = sso;
          const userInfo = getUserInfo();
          modalFormRef.value?.formRef?.setFieldsValue({
            loginName: userInfo?.loginName,
            comment: titleMap.get('pass')?.defaultValue,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
