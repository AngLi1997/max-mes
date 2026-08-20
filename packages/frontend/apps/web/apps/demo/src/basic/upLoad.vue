<script lang="ts" setup>
  import { ref } from 'vue';
  import { UploadOutlined, StarOutlined } from '@ant-design/icons-vue';
  import type { UploadChangeParam } from 'ant-design-vue';
  import { message } from 'ant-design-vue';
  const fileList = ref([]);
  const headers = {
    authorization: 'authorization-text',
  };
  const handleChange = (info: UploadChangeParam) => {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  };
  // 自定义删除方法
  const handleClick = () => {
    console.log('删除...');
  };
</script>
<template>
  <!-- 上传 -->
  <a-upload
    v-model:file-list="fileList"
    name="file"
    action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
    :headers="headers"
    @change="handleChange">
    <a-button>
      <upload-outlined></upload-outlined>
      上传记录
    </a-button>
    <!-- 自定义icon -->
    <template #removeIcon>
      <StarOutlined @click="handleClick"></StarOutlined>
    </template>
    <!-- <template #removeIcon><DeleteOutlined @click="handleClick"></DeleteOutlined></template> -->
  </a-upload>
</template>
