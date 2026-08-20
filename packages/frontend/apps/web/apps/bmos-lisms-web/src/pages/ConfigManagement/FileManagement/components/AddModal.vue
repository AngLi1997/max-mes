<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('文件新增')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { Button, message } from 'ant-design-vue';
  import { useDict } from '@/stores/dictStore';
  import { UploadOutlined } from '@ant-design/icons-vue';
  import { postConfigFileCreate } from '@/services';
  import { TENMB } from '@/types';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);
  const { getDict } = useDict();
  const props = withDefaults(
    defineProps<{
      rowData?: Recordable;
    }>(),
    {
      rowData: () => ({}),
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue({
          templateName: props.rowData?.templateName,
          versionNumber: props.rowData?.versionNumber,
        });
      }
    },
  );
  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 12,
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD HH:mm:ss') ?? date;
    },
    schemas: [
      {
        field: 'templateName',
        component: 'Input',
        label: t('文件名称'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'versionNumber',
        component: 'Input',
        label: t('文件版本'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'standardNumber',
        component: 'Input',
        label: t('文件编号'),
        required: true,
        componentProps: {
          maxLength: 20,
        },
      },
      {
        field: 'buildNumber',
        component: 'Input',
        label: t('文件版本号'),
        required: true,
        componentProps: {
          maxLength: 20,
        },
      },
      {
        field: 'fileType',
        component: 'Select',
        label: t('文件类型'),
        required: true,
        componentProps: {
          request: async () => {
            return await getDict('文件类型');
          },
        },
      },
      {
        field: 'effectiveDate',
        component: 'DatePicker',
        label: t('生效日期'),
        required: true,
        componentProps: {
          showTime: true,
          disabledDate: (current: any) => {
            return current && current < Date.now() - 86400000;
          },
          style: {
            width: '100%',
          },
        },
      },
      {
        field: 'fileList',
        component: 'Upload',
        label: t('上传文件'),
        required: true,
        colProps: {
          span: 24,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            // 只允许表格文件
            accept: '.doc, .docx',
            maxCount: 1,
            onRemove: () => {
              formModel.fileList = [];
              formModel['body'] = undefined;
              return Promise.resolve();
            },
            beforeUpload: (file: File) => {
              if (file.size > TENMB) {
                // @ts-ignore
                file.status = 'error';
                // @ts-ignore
                file.response = t('上传文件大小不能超过10MB');
                formModel.fileList = [file];
                message.error(t('上传文件大小不能超过10MB'));
              }
              formModel['body'] = file;
              return false;
            },
          };
        },

        componentSlots: {
          default: () => (
            <Button type='default'>
              <UploadOutlined></UploadOutlined>
              {t('选择文件')}
            </Button>
          ),
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请上传文件'),
              trigger: 'change',
              validator: () => {
                if (!formModel['body']) {
                  return Promise.reject(t('请上传文件'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'createRemark',
        component: 'InputTextArea',
        label: t('备注'),
        colProps: {
          span: 24,
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      const { templateName, standardNumber, buildNumber, fileType, effectiveDate, body, createRemark } = formModal;
      await postConfigFileCreate({
        body,
        templateNo: props.rowData?.templateNo,
        templateName,
        standardNumber,
        buildNumber,
        fileType,
        effectiveDate,
        ...(createRemark && { createRemark }),
      });
      emit('ok');
      message.success(`${t('新增')}${t('成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
