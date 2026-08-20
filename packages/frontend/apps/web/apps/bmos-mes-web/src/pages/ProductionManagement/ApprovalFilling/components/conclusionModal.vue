<template>
  <!-- 审核结论编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('审核结论')"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @cancel="cancel">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
      <Button type="primary" @click="ok">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { reactive, ref, nextTick, watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';
  import { postUpdateProcess, postUpdateProcedure } from '@/services';

  const emit = defineEmits(['updateTable']);

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    type: {
      type: String,
      default: () => '',
    },
  });
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const openModal = () => {
    open.value = true;
  };
  // 查看的表单
  const formProps = reactive<any>({
    initialValues: {},
    schemas: [
      {
        field: 'confirmOpinion',
        component: 'RadioGroup',
        label: t('审核意见'),
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('合格'),
                value: 'ELIGIBLE',
              },
              {
                label: t('不合格'),
                value: 'NOT_ELIGIBLE',
              },
              {
                label: t('其他'),
                value: 'RESTS',
              },
            ],
            onChange: () => {
              // modalFormRef.value?.formRef?.validateFields(['remark']);
              modalFormRef.value?.formRef?.setFormModels({
                remark: undefined,
              });
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              // type: 'number',
              message: t('请选择意见'),
            },
          ];
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        componentProps: {
          maxlength: 255,
        },
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: formModel['confirmOpinion'] == 'ELIGIBLE' ? false : true,
              trigger: 'blur',
              message: t('请输入备注'),
            },
          ];
        },
      },
    ],
  });

  const cancel = () => {};

  const ok = async () => {
    const res = await modalFormRef.value?.validate();
    try {
      const data = { ...res, id: props.rowData.id, opinion: res?.confirmOpinion, confirmOpinion: undefined };
      props.type === 'process' ? await postUpdateProcess(data) : await postUpdateProcedure(data);
      message.success(t('操作成功'));
      open.value = false;
      emit('updateTable');
    } catch (error: any) {
      message.error(error.message);
    }
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          ...props.rowData,
          confirmOpinion: props.rowData.confirmOpinion?.value || '',
        }); //回显编辑框
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal });
</script>
<style lang="less" scoped></style>
