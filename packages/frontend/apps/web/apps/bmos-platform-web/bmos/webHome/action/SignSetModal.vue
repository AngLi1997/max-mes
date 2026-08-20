<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('签名设置')"
    :submit="okModal"
    destroyOnClose
    @cancelModal="cancelModal"
    wrap-class-name="modalSizeMedium sign-set-modal">
    <div class="upload-content">
      <span class="upload-label">{{ t('手写签名录入') }}:</span>
      <Upload
        :file-list="fileList"
        :before-upload="beforeUpload"
        :maxCount="1"
        :showUploadList="false"
        accept="image/*">
        <Button>
          <upload-outlined></upload-outlined>
          {{ t('上传文件') }}
        </Button>
      </Upload>
    </div>
    <div class="img-content">
      <img v-if="imageUrl.length > 0" :src="imageUrl" alt="" class="image" />
      <Empty v-else>
        <template #description>
          {{ t('暂无签名，请上传') }}
        </template>
      </Empty>
    </div>
  </NormalModalForm>
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';
  import { UploadProps, message } from 'ant-design-vue';
  import { UploadOutlined } from '@ant-design/icons-vue';
  import { reqUserSignInfo, reqUserSignSave } from '../../api/handleSign';

  const open = defineModel<boolean>({ default: false });
  const imageUrl = ref<string>('');
  const fileList = ref<UploadProps['fileList']>([]);

  const beforeUpload: UploadProps['beforeUpload'] = file => {
    fileList.value = [file];
    try {
      const reader = new FileReader();
      reader.onload = e => {
        imageUrl.value = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    } catch (error) {
      imageUrl.value = '';
    }
    return false;
  };

  const okModal = async () => {
    try {
      if (!imageUrl.value) {
        message.error(t('请上传签名'));
        return Promise.reject();
      }
      if (!fileList.value?.length) {
        message.error(t('请上传签名'));
        return Promise.reject();
      }
      await reqUserSignSave({
        fileBase64Content: imageUrl.value,
        suffix: `.${fileList.value[0]?.name?.split('.')?.pop()}`,
      });
      message.success(t('保存成功'));
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };

  const cancelModal = () => {
    open.value = false;
    fileList.value = [];
    imageUrl.value = '';
  };

  const getFileUrl = (path: string) => {
    return document.location.origin + '/' + path;
  };

  const getSignInfo = async () => {
    try {
      const { data } = await reqUserSignInfo();
      if (data) {
        imageUrl.value = getFileUrl(data);
      }
    } catch (error) {}
  };

  watch(
    () => open.value,
    val => {
      if (val) {
        fileList.value = [];
        imageUrl.value = '';
        getSignInfo();
      }
    },
  );
</script>

<style lang="less">
  .sign-set-modal {
    .upload-content {
      .upload-label {
        display: inline-block;
        margin-right: var(--bmos-margin-large);
      }
    }
    .img-content {
      overflow: hidden;
      height: 240px;
      border-radius: 8px;
      background: var(--bmos-background-color);
      margin-top: var(--bmos-margin-large);
      .image {
        object-fit: contain;
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
