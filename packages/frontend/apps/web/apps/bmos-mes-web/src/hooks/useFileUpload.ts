import { message, UploadProps } from 'ant-design-vue';
import { fileUpload } from '../services';
export const customRequest: UploadProps['customRequest'] = (options: any) => {
  const formData = new FormData();
  formData.append('file', options.file);

  fileUpload(formData)
    .then(res => {
      if (res.status === 200 && res.data.code === 0) {
        options.onSuccess(res.data.data as any);
      } else {
        message.error(res.data.message)
        options.onError(res);
      }
    })
    .catch(error => {
      message.error(error.message)
      options.onError(error);
      
    });
};
