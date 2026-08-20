import { message, UploadProps } from 'ant-design-vue';
import { recordItemUpload } from '../../../services';
import { t } from '@bmos/i18n';
export const customRequest: UploadProps['customRequest'] = (options: any) => {
  const formData = new FormData();
  formData.append('file', options.file);

  recordItemUpload(formData)
    .then(res => {
      if (res.status === 200) {
        if(res.data.code === 0){
          options.onSuccess(res.data.data as any);
        }else{
          message.error(res.data.message)
        }
        
      } else {
        message.error(t('文件上传失败'))
        options.onError(res);
      }
    })
    .catch(error => {
      message.error(t('文件上传失败'))
      options.onError(error);
    });
};
