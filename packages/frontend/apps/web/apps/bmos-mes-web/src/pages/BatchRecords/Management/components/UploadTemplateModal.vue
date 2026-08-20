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
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams, Rule } from '@bmos/components';
  import { Button, message, Space, UploadProps } from 'ant-design-vue';
  import { reqLotReleaseManageUpdateExcelFile, reqLotReleaseManageUploadExcel } from '@/services';
  import { LoadingOutlined, UploadOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';

  defineOptions({
    inheritAttrs: false,
  });

  const emit = defineEmits(['ok']);

  const open = defineModel<boolean>('uploadModalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      rowData: Recordable;
    }>(),
    {
      rowData: () => ({}),
    },
  );

  const customRequest: UploadProps['customRequest'] = (options: any) => {
    const formData = new FormData();
    formData.append('file', options.file);
    reqLotReleaseManageUploadExcel(formData)
      .then((res: any) => {
        options.onSuccess(res.data as any);
      })
      .catch((error: any) => {
        error.message && message.error(error.message);
        options.onError(error);
      });
  };

  const formProps: Ref<FormProps> = ref({
    schemas: [
      {
        field: 'fileList',
        component: 'Upload',
        label: t('上传模板'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            accept: '.doc, .docx',
            maxCount: 1,
            customRequest,
            onChange: (info: any) => {
              formModel.fileList = info.fileList;
              if (info.file.status === 'done') {
                formModel.fileUrl = info.file.response;
              } else {
                formModel.fileUrl = undefined;
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
        dynamicRules: ({ formModel }: RenderCallbackParams): Rule[] => {
          return [
            {
              required: true,
              message: t('请上传模板'),
              trigger: 'blur',
              validator: () => {
                if (!formModel['fileUrl']) {
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
      await reqLotReleaseManageUpdateExcelFile({
        fileUrl: formModal.fileUrl,
        remark: formModal.remark,
        lotReleaseId: props.rowData.id,
      });
      message.success(t('上传成功'));
      emit('ok');
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>
