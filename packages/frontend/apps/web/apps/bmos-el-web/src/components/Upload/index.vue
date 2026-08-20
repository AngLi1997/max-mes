<template>
  <Upload
    @change="fileUploadChange($event, model)"
    :before-upload="beforeUpload"
    :accept="accept"
    :file-list="model['fileList']"
    :customRequest="customRequestFn">
    <Button>
      <upload-outlined></upload-outlined>
      {{ label }}
    </Button>
    <template #itemRender="{ file }">
      <Space>
        <LoadingOutlined v-if="file.status === 'uploading'"></LoadingOutlined>
        <SvgIcon
          v-if="file.status === 'success'"
          icon="success"
          style="width: 14px"></SvgIcon>
        <span :style="file.status === 'error' ? 'color: red' : ''">
          {{ file.name }}
        </span>
      </Space>
    </template>
  </Upload>
</template>

<script setup lang="ts">
  import { customRequest } from '@/hooks';
  import { beforeUpload } from '@/utils';
  import { t } from '@bmos/i18n';
  import SvgIcon from '../svg-icon/index.vue'
  import { UploadOutlined, LoadingOutlined } from '@ant-design/icons-vue';
  import { Space, Button, Upload, UploadProps } from 'ant-design-vue';
  import { fileUploadChange } from './utils';
  withDefaults(
    defineProps<{
      model: Record<any, any>;
      label: string;
      accept?: string;
      customRequestFn?: UploadProps['customRequest'];
    }>(),
    {
      model: () => ({}),
      label: t('上传记录'),
      accept: '.docx',
      customRequestFn: () => customRequest,
    },
  );
</script>

<style scoped lang="less"></style>
