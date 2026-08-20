<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit">
    <template #title>
      {{ titleMap.get(curType)?.title }}
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { message, Alert } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
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
    (e: 'update:open', open: boolean): void;
    (e: 'action', type: string): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      curType: string;
      needPwdValidate?: boolean;
      taskId: string;
      processInstanceId: string;
      nodeId: string;
      deploymentId: string;
      needCommit?: boolean;
      needRemark?: boolean;
      needCopyTo?: boolean;
      executionId?: string;
      mesAuditCompleteRequest?: (params: any) => Promise<any>;
      mesAuditCompleteNotApproveRequest?: (params: any) => Promise<any>;
      mesAuditBackToPrevRequest?: (params: any) => Promise<any>;
    }>(),
    {
      needPwdValidate: false,
      needCommit: false,
      needRemark: false,
      needCopyTo: false,
      executionId: '',
      mesAuditCompleteRequest: undefined,
      mesAuditCompleteNotApproveRequest: undefined,
      mesAuditBackToPrevRequest: undefined,
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const passFn = async (params: any) => {
    try {
      if (props.mesAuditCompleteRequest) {
        await props.mesAuditCompleteRequest(params);
      } else {
        await mesAuditComplete(params);
      }
      emit('action', 'pass');
      open.value = false;
      message.success(t('审核通过'));
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const rejectFn = async (params: any) => {
    try {
      if (props.mesAuditCompleteNotApproveRequest) {
        await props.mesAuditCompleteNotApproveRequest(params);
      } else {
        await mesAuditCompleteNotApprove(params);
      }
      emit('action', 'reject');
      open.value = false;
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
      if (props.mesAuditBackToPrevRequest) {
        await props.mesAuditBackToPrevRequest(newParams);
      } else {
        await mesAuditBackToPrev(newParams);
      }
      emit('action', 'returnTo');
      open.value = false;
      message.success(t('回退成功'));
      return Promise.resolve();
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const titleMap: Map<
    string,
    {
      title: string;
      desc: string;
      operation?: (params: any) => Promise<any>;
      alertType: 'info' | 'warning' | 'error' | 'success';
      defaultValue: string;
    }
  > = new Map([
    [
      'pass',
      {
        title: t('审核通过'),
        desc: `${t('是否通过当前节点审核')}?`,
        operation: passFn,
        alertType: 'info',
        defaultValue: t('审核通过'),
      },
    ],
    [
      'reject',
      {
        title: t('审核不通过'),
        desc: `${t('是否不通过当前审核任务')}?`,
        operation: rejectFn,
        alertType: 'warning',
        defaultValue: t('审核不通过'),
      },
    ],
    [
      'deliverTo',
      {
        title: t('转交'),
        desc: `${t('是否转交当前审核任务')}?`,
        alertType: 'warning',
        defaultValue: t('转交'),
      },
    ],
    [
      'returnTo',
      {
        title: t('回退'),
        desc: `${t('是否退回上一节点')}?`,
        operation: returnToFn,
        alertType: 'warning',
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
      if (titleMap.get(props.curType)?.operation) {
        const operation = titleMap.get(props.curType)?.operation;
        if (operation) {
          await operation(approvalParams);
        }
      }
      sendMessage(MessageType.UpdateMessageCount);
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const formProps: Ref<FormProps> = ref({
    initialValues: {},
    schemas: [
      {
        field: 'field1',
        noLabel: true,
        component: () => (
          <Alert
            class='approval-alert'
            message={titleMap.get(props.curType)?.desc}
            type={titleMap.get(props.curType)?.alertType}
            showIcon
            icon={<ExclamationCircleOutlined />}
          />
        ),
      },
      {
        field: 'copyTo',
        component: 'Select',
        label: t('抄送人员'),
        vIf: () => props.needCopyTo,
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
        required: props.needCommit || false,
        componentProps: {
          maxlength: 50,
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: props.needRemark || false,
        componentProps: {
          maxlength: 50,
        },
      },
      {
        field: 'field6',
        component: 'Divider',
        vIf: () => props.needPwdValidate,
        colProps: {
          span: 24,
        },
      },
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户名'),
        vIf: () => props.needPwdValidate,
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'password',
        component: 'InputPassword',
        label: t('密码'),
        vIf: () => props.needPwdValidate,
        required: true,
        componentProps: {
          autocomplete: 'new-password',
        },
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
          const { getUserInfo } = sso;
          const userInfo = getUserInfo();
          modalFormRef.value?.formRef?.setFieldsValue({
            loginName: userInfo?.loginName,
            comment: titleMap.get(props.curType)?.defaultValue,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
