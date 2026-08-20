<template>
  <!-- 新增编辑查看框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="props.showTitle"
    :rowData="props.rowData"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <Button v-if="props.showTitle !== t('查看用户')" @click="cancel">
        {{ t('取消') }}
      </Button>
      <Button type="primary" @click="ok">{{ t('确定') }}</Button>
    </template>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { addUser, editUser, validateUser } from '../../../../api/Permissions/userManagement';
  import { message } from 'ant-design-vue';
  const props = defineProps({
    showTitle: {
      type: String,
      default: '',
    },
    rowData: {
      type: Object,
      default: () => {},
    },
  });

  const emits = defineEmits(['loadData']);
  // 用户名称格式验证（）
  const validatorName = async (_rule: any, value: string) => {
    if (!value) {
      return Promise.reject(t('请输入用户名称'));
    } else if (!/^.{0,30}$/.test(value)) {
      return Promise.reject(t('输入内容过长，不能超过30个字符'));
    } else {
      return Promise.resolve();
    }
  };
  // 用户账号格式验证（指令集需要）
  const validatorAccount = async (_rule: any, value: string) => {
    if (!value) {
      return Promise.reject(t('请输入用户账号'));
    } else if (!/^[a-zA-Z0-9]{2,18}$/.test(value)) {
      return Promise.reject(t('只能包含英文字母、数字，长度限制2~18'));
    } else if (props.showTitle == t('新增用户')) {
      const res = await validateUser({ userName: value });
      if (res.data == true) {
        return Promise.reject(t('用户账号已存在!'));
      } else {
        return Promise.resolve();
      }
    } else {
      return Promise.resolve();
    }
  };
  // 手机格式验证
  const validatorPhone = async (_rule: any, value: string) => {
    if (!value) {
    } else if (!/^1[3456789]\d{9}$/.test(value)) {
      return Promise.reject(t('手机号格式不正确!'));
    } else {
      return Promise.resolve();
    }
  };
  // 邮箱验证
  const validatorEmail = async (_rule: any, value: string) => {
    if (!value) {
    } else if (
      !/^([a-zA-Z0-9_-]{1,16})@([a-zA-Z0-9]{1,9})(\.[a-zA-Z0-9]{1,9}){0,3}(\.(?:com|net|org|edu|gov|mil|cn|us)){1,4}$/.test(
        value,
      )
    ) {
      return Promise.reject(t('邮箱格式不正确!'));
    } else {
      return Promise.resolve();
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const openModal = () => {
    open.value = true;
  };
  // 新增编辑的表单
  const formProps = reactive<any>({
    initialValues: {},
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'userName',
        component: 'Input',
        label: t('用户名称'),
        rules: [{ required: true, validator: validatorName, trigger: 'blur' }],
      },
      {
        field: 'loginName',
        component: 'Input',
        label: t('用户账号'),
        componentProps: { disabled: false },
        rules: [{ required: true, validator: validatorAccount, trigger: 'blur' }],
      },
      {
        field: 'gender',
        component: 'RadioGroup',
        label: t('性别'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('男'),
              value: 0,
            },
            {
              label: t('女'),
              value: 1,
            },
          ],
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'number',
              message: t('请选择性别'),
            },
          ];
        },
      },
      {
        field: 'phone',
        component: 'Input',
        label: t('手机号'),
        required: false,
        rules: [{ required: false, validator: validatorPhone, trigger: 'blur' }],
      },
      {
        field: 'email',
        component: 'Input',
        label: t('用户邮箱'),
        required: false,
        rules: [{ required: false, validator: validatorEmail, trigger: 'blur' }],
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: false,
        componentProps: {
          maxlength: 255,
        },
      },
    ],
  });
  // 取消按钮
  const cancel = () => {
    open.value = false;
  };

  const ok = async () => {
    if (props.showTitle == t('新增用户')) {
      const defaultAddData = { status: 0, state: 0 };
      try {
        const res = await modalFormRef.value?.validate();
        const addData = { ...res, ...defaultAddData };
        await addUser(addData);
        open.value = false;
        message.success(t('新增成功'));
        emits('loadData');
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    }
    if (props.showTitle == t('编辑用户')) {
      try {
        const res = await modalFormRef.value?.validate();
        const editData = { ...res, id: props.rowData.id };
        await editUser(editData);
        open.value = false;
        message.success(t('编辑成功'));
        emits('loadData');
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    }
    if (props.showTitle == t('查看用户')) {
      open.value = false;
    }
  };

  const resetForm = () => {
    modalFormRef.value?.resetForm();
  };
  defineExpose({ openModal, formProps, resetForm });
</script>
<style lang="less" scoped>
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
</style>
