<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('上传')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { Button, message, Space, UploadProps } from 'ant-design-vue';
  import { reqBatchRecordsTemplateFileUpload, reqBatchRecordsTemplateVersionUpload } from '@/services';
  import { LoadingOutlined, UploadOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('uploadModalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      secondRowData: Recordable;
    }>(),
    {
      secondRowData: () => ({}),
    },
  );

  const customRequest: UploadProps['customRequest'] = (options: any) => {
    const formData = new FormData();
    formData.append('file', options.file);
    reqBatchRecordsTemplateFileUpload(formData)
      .then((res: any) => {
        options.onSuccess(res.data as any);
      })
      .catch((error: any) => {
        error.message && message.error(error.message);
        options.onError(error);
      });
  };

  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'fileList',
        component: 'Upload',
        label: t('上传模板'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            accept: '.docx, .doc,',
            maxCount: 1,
            customRequest,
            onChange: (info: any) => {
              formModel.fileList = info.fileList;
              if (info.file.status === 'done') {
                formModel.path = info.file.response;
              } else {
                formModel.path = undefined;
              }
            },
          };
        },
        componentSlots: {
          default: () => (
            <Button type='default'>
              <UploadOutlined></UploadOutlined>
              {t('上传模板')}
            </Button>
          ),
          itemRender: ({ values }: any) => {
            const { fileList } = values;
            const file = fileList?.[0] || {};
            return (
              <Space>
                {file.status === 'uploading' && <LoadingOutlined></LoadingOutlined>}
                {file.status === 'done' && <BMIcons icon='Success' style='width: 14px'></BMIcons>}
                <span style={file.status === 'error' ? 'color: red' : ''}>{file.name}</span>
              </Space>
            );
          },
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请上传模板'),
              trigger: 'blur',
              validator: () => {
                if (!formModel['path']) {
                  return Promise.reject(t('请上传模板'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await reqBatchRecordsTemplateVersionUpload({
        templateVersionId: props.secondRowData.id,
        remark: formModal.remark,
        path: formModal.path,
      });
      emit('ok');
      message.success(t('上传成功'));
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
