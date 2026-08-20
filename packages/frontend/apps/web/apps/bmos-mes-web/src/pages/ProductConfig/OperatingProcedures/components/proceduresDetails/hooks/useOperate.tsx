import {
  getOperateRuleVersionDetails,
  getOperateRuleVersionDownload,
  getServerTimeApi,
  postOperateRuleSave,
  postOperateRuleUpload,
  postOperateRuleVersionSave,
  postOperateRuleVersionUpdate,
} from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { UploadProps } from 'ant-design-vue';
import { Button, Modal, Space, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import { ActionType, modalStatus, modelName } from '../../../enum';
export const useOperate = ({ UseParams, back }: any) => {
  const {
    isForm,
    isState,
    isBeforeUpload,
    operateId,
    isModalOpen,
    fileList,
    submitForm,
    submitParams,
    permissionModalOpen,
  } = UseParams;
  const uploadTime = ref<any>();
  const beforeUpload: UploadProps['beforeUpload'] = async file => {
    const tenMB = 10 * 1024 * 1024;
    if (file.size > tenMB) {
      message.error(t('上传文件大小不能超过10MB'));
      return Promise.reject();
    }
    const fileName = file.name;
    const fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
    if (fileType !== 'pdf') {
      message.error(t('只支持PDF文件'));
      return Promise.reject();
    }
    fileList.type = fileType;
    fileList.file = URL.createObjectURL(file);
    const formData = new FormData();
    formData.append('file', file);
    fileList.streamingMedia = formData;
    isForm.value = true;
    isBeforeUpload.value = true;
    const { data } = await getServerTimeApi();
    uploadTime.value = data;
    return Promise.resolve();
  };
  //查询详情
  const specificsAPI = async (rec: any) => {
    try {
      const res = await getOperateRuleVersionDetails({ id: rec.id });
      const { data } = res;
      operateId.value = data;
      submitForm.value?.setFieldsValue({ ...data });
      uploadTime.value = data?.uploadTime;
      const pdfRes = await getOperateRuleVersionDownload({ versionId: rec.id });
      fileList.file = pdfRes;
      isBeforeUpload.value = false;
    } catch (error: any) {
      isBeforeUpload.value = true;
      error.message && message.error(error.message);
    }
  };
  //上传文件
  const uploadFile = async (update: any) => {
    try {
      const res = await postOperateRuleUpload(update);
      return Promise.resolve(res);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };
  //编辑
  const editFile = async (update: any) => {
    try {
      const res = await postOperateRuleVersionUpdate(update);
      return Promise.resolve(res);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };
  //新增版本
  const CopeFile = async (update: any) => {
    try {
      const res = await postOperateRuleVersionSave(update);
      return Promise.resolve(res);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };
  //新增文件
  const addFile = async (update: any) => {
    try {
      const res = await postOperateRuleSave(update);
      return Promise.resolve(res);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const isFile: ActionType = {
    [modalStatus.Edit]: editFile,
    [modalStatus.Copy]: CopeFile,
    [modalStatus.Add]: addFile,
  };
  //保存
  const submit = async () => {
    try {
      const res = await submitForm.value.validate();
      if (!fileList.file) return message.error(t('请上传PDF文件'));
      const uploadFileRes = isBeforeUpload.value
        ? await uploadFile(fileList.streamingMedia)
        : { data: operateId.value.url };
      if (!uploadFileRes) return false;
      submitParams.value = {
        ...res,
        url: uploadFileRes.data,
        uploadTime: uploadTime.value,
      };
      Modal.confirm({
        title: t('提示'),
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否保存') + modelName[isState.value],
        onOk: async () => {
          if (isModalOpen.value) {
            permissionModalOpen.value = isModalOpen.value;
            return false;
          }
          await isFile[isState.value]({
            ...submitParams.value,
            operateId: operateId.value.operateId,
            id: operateId.value.id,
          });
          isForm.value = false;
          message.success(`${modelName[isState.value]}${t('成功')}`);
          back();
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //部门权限
  const savePermission = async (deptIds: any) => {
    try {
      await isFile[isState.value]({ ...submitParams.value, deptIds });
      isForm.value = false;
      message.success(t('保存成功'));
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //返回
  const moKen = () => {
    if (isForm.value) {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否保存文件'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
                <Button
                  onClick={() => {
                    Modal.destroyAll();
                    back();
                  }}>
                  {t('不保存')}
                </Button>
                <Button
                  type='primary'
                  onClick={() => {
                    Modal.destroyAll();
                    submit();
                  }}>
                  {t('保存')}
                </Button>
              </Space>
            </>
          );
        },
      });
    } else {
      back();
    }
  };
  return {
    submit,
    beforeUpload,
    savePermission,
    specificsAPI,
    moKen,
  };
};
