import { fileUpload } from '@/services';
import { UploadOutlined } from '@ant-design/icons-vue';
import { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
import { Button, Upload, message } from 'ant-design-vue';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const uploadFile = async (options: any) => {
    const { onSuccess, onError, file, onProgress } = options;

    const formData = new FormData();
    formData.append('file', file);
    try {
      // const res = await fileUpload(formData);
      const response = await fileUpload(formData, (progressEvent: any) => {
        const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
        onProgress(percent);
      });
      if (response.data.code == 0) {
        onSuccess(response.data.data);
      } else {
        onError(response.data.message);
      }
    } catch (error: any) {
      onError(error);
    }
  };

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('分批标识'),
        field: 'batchLog',
        required: true,
        component: 'Input',
      },
      {
        label: t('分箱标识'),
        field: 'subBoxLog',
        required: true,
        component: 'Input',
      },
      {
        label: t('效价值下限'),
        field: 'titerDown',
        component: 'Input',
      },
      {
        label: t('效价值上限'),
        field: 'titerUp',
        component: 'Input',
      },
      {
        label: t('所属类型'),
        field: 'sortingType',
        required: true,
        component: 'Select',
        componentProps: {
          allowClear: true,
          treeData: [],
        },
      },
      {
        label: t('描述'),
        field: 'typeDescribe',
        required: true,
        component: 'Input',
      },
      {
        label: t('启用状态'),
        field: 'useFlag',
        required: true,
        component: 'Switch',
        componentProps: {
          checkedValue: 1,
          unCheckedValue: 0,
        },
      },
      {
        label: t('语音文件'),
        field: 'voiceFile',
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <Upload
              v-model:file-list={formModel.voiceFile}
              maxCount={1}
              // 上传前回调
              beforeUpload={file => {
                // 限制上传文件大小不超过5MB
                if (file.size / 1024 / 1024 > 5) {
                  message.error('文件大小不能超过5MB');
                  return Upload.LIST_IGNORE;
                }
                return true;
              }}
              customRequest={uploadFile}
              // 支持文件格式MP3、WAV、FLAC、OGG、AAC、WMA
              accept='.mp3,.mp,.mp4,.wma,.flac,.aac,.wav,ogg'>
              <Button>
                <UploadOutlined></UploadOutlined>
                {t('上传文件')}
              </Button>
            </Upload>
          );
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
  };
};
