<template>
  <div :bordered="false" style="width: 100%; height: 100%; padding: 0 10%">
    <div class="importCom">
      <div class="line">
        <div class="icon">
          <img
            src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNzgiIGhlaWdodD0iNzgiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZD0iTTQuOTAyIDM4LjgyNGEzNC4zNCAzNC4zNCAwIDEgMCA2OC42NzkgMCAzNC4zNCAzNC4zNCAwIDAgMC02OC42NzkgMFoiIGZpbGw9IiNGRkY3REMiLz48cGF0aCBkPSJNNTcuNjU4IDIyLjkyNGgtMTguNjNhMS41IDEuNSAwIDAgMS0xLjAyNi0uNDA2bC00LjI0NC0zLjk3NGExLjQ5MiAxLjQ5MiAwIDAgMC0xLjAyNS0uNDA1aC04LjE1MWE1LjEzIDUuMTMgMCAwIDAtNS4xMjkgNS4xMjhWNTMuOTZhNS4xMyA1LjEzIDAgMCAwIDUuMTI5IDUuMTI5aDMzLjA3MmE1LjEzIDUuMTMgMCAwIDAgNS4xMjgtNS4xMjlWMjguMDU2YTUuMTI1IDUuMTI1IDAgMCAwLTUuMTI0LTUuMTMyWm0tOC42MjMgMjIuMDIzLTYuMDMgNS44MTVhMi43ODggMi43ODggMCAwIDEtMy44NDkuMDI0bC01LjkyLTUuNTc0YTIuNzk1IDIuNzk1IDAgMCAxIC43NjYtNC41OCAyLjc5MiAyLjc5MiAwIDAgMSAzLjA2LjUxM2wxLjI0IDEuMTY2di03Ljk1MmEyLjc5MiAyLjc5MiAwIDEgMSA1LjU4NSAwdjcuOGwxLjI3NS0xLjIyOWEyLjc4NSAyLjc4NSAwIDAgMSAxLjkzOC0uNzg0IDIuNzcgMi43NyAwIDAgMSAyLjAwOS44NTUgMi43OTggMi43OTggMCAwIDEtLjA3NCAzLjk0NloiIGZpbGw9IiNGNjk5MzYiLz48cGF0aCBkPSJNNjIuNzg2IDI4LjA1NmE1LjEzIDUuMTMgMCAwIDAtNS4xMjgtNS4xMjhoLTE4LjYzYTEuNSAxLjUgMCAwIDEtMS4wMjYtLjQwNmwtNC4yNDQtMy45NzhhMS40OTIgMS40OTIgMCAwIDAtMS4wMjUtLjQwNWgtOC4xNTFhNS4xMyA1LjEzIDAgMCAwLTUuMTI5IDUuMTI4VjUzLjk2YTUuMTMgNS4xMyAwIDAgMCA1LjEyOSA1LjEyOWgxMi43MjFjMTIuMDE2LTIuNTUgMjEuNzEyLTExLjM4NCAyNS40OC0yMi45MDV2LTguMTI4aC4wMDNaTTQ5LjAzNSA0NC45NDdsLTYuMDMgNS44MTVhMi43ODggMi43ODggMCAwIDEtMy44NDkuMDI0bC01LjkyLTUuNTc0YTIuNzk1IDIuNzk1IDAgMCAxIC43NjYtNC41OCAyLjc5MiAyLjc5MiAwIDAgMSAzLjA2LjUxM2wxLjI0IDEuMTY2di03Ljk1MmEyLjc5MiAyLjc5MiAwIDEgMSA1LjU4NSAwdjcuOGwxLjI3NS0xLjIyOWEyLjc4NSAyLjc4NSAwIDAgMSAxLjkzOC0uNzg0IDIuNzcgMi43NyAwIDAgMSAyLjAwOS44NTUgMi43OTggMi43OTggMCAwIDEtLjA3NCAzLjk0NloiIGZpbGw9IiNGRkIyMzgiLz48cGF0aCBkPSJNMzkuMDI3IDIyLjkyNGExLjUgMS41IDAgMCAxLTEuMDI1LS40MDZsLTQuMjQ0LTMuOTc0YTEuNDkyIDEuNDkyIDAgMCAwLTEuMDI1LS40MDVoLTguMTUxYTUuMTMgNS4xMyAwIDAgMC01LjEyOSA1LjEyOFY0Ny42MmEzNC4yMzYgMzQuMjM2IDAgMCAwIDEzLjg0OS0yLjM0bC0uMDc0LS4wN2EyLjc5NSAyLjc5NSAwIDAgMSAuNzY2LTQuNTggMi43OTIgMi43OTIgMCAwIDEgMy4wNi41MTJsMS4yNCAxLjE2NnYtNy45NDhhMi43OTIgMi43OTIgMCAxIDEgNS41ODUgMHY0LjMyOWEzNC4zNjUgMzQuMzY1IDAgMCAwIDkuODQtMTUuNzZIMzkuMDI3di0uMDA0WiIgZmlsbD0iI0ZGQzg2RiIvPjxwYXRoIGQ9Ik0zOC43MDQgMjIuODg1YTEuNDYgMS40NiAwIDAgMS0uNjk5LS4zNjdsLTQuMjQ3LTMuOTc0YTEuNDkyIDEuNDkyIDAgMCAwLTEuMDI1LS40MDVoLTguMTUxYTUuMTMgNS4xMyAwIDAgMC01LjEyOSA1LjEyOHYxMS40MzVjNy42OTktMS42NzMgMTQuNDM0LTUuOTI4IDE5LjI1LTExLjgxN1oiIGZpbGw9IiNGRkRDOUQiLz48L3N2Zz4="
            alt="" />
        </div>
        <div class="content">
          <div class="title">{{ t('填写导入的信息') }}</div>
          <div class="subtitle">
            {{ t('请按照数据模板的格式准备导入数据，模板中的表头名称不可更改，表头行不能删除。') }}
          </div>
          <a class="card-download" target="_blank" @click="downloadTemplate">
            <img
              src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZD0iTTEuNjY3IDIuNjY3YzAtLjM2OS4yOTgtLjY2Ny42NjYtLjY2N2g0TDggNGg1LjY2N2MuMzY4IDAgLjY2Ni4yOTguNjY2LjY2N3Y4LjY2NmEuNjY3LjY2NyAwIDAgMS0uNjY2LjY2N0gyLjMzM2EuNjY3LjY2NyAwIDAgMS0uNjY2LS42NjdWMi42NjdaIiBzdHJva2U9IiMyRDcwRkYiIHN0cm9rZS13aWR0aD0iMS4xNjciIHN0cm9rZS1saW5lam9pbj0icm91bmQiLz48cGF0aCBkPSJtMTAgOS4zMzQtMi4wMDIgMkw2IDkuMzM4TTggNi42Njd2NC42NjYiIHN0cm9rZT0iIzJENzBGRiIgc3Ryb2tlLXdpZHRoPSIxLjE2NyIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+PC9zdmc+"
              alt="" />
            {{ t('下载模板') }}
          </a>
        </div>
      </div>
      <div class="line">
        <div class="icon">
          <img
            src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNzgiIGhlaWdodD0iNzgiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZD0iTTUuMTA5IDM4LjgyNGEzNC4zNCAzNC4zNCAwIDEgMCA2OC42NzggMCAzNC4zNCAzNC4zNCAwIDAgMC02OC42NzggMFoiIGZpbGw9IiNEREZGRTciLz48cGF0aCBkPSJNNTYuMjczIDIyLjkyNGgtMTguNjNhMS41IDEuNSAwIDAgMS0xLjAyNi0uNDA2bC00LjI0My0zLjk3NGExLjQ5MiAxLjQ5MiAwIDAgMC0xLjAyNi0uNDA1aC04LjE1YTUuMTMgNS4xMyAwIDAgMC01LjEzIDUuMTI4VjUzLjk2YTUuMTMgNS4xMyAwIDAgMCA1LjEzIDUuMTI5aDMzLjA3MWE1LjEzIDUuMTMgMCAwIDAgNS4xMjktNS4xMjlWMjguMDU2YTUuMTI1IDUuMTI1IDAgMCAwLTUuMTI1LTUuMTMyWm0tOC4zOTcgMTkuMTg0YTIuNzc3IDIuNzc3IDAgMCAxLTIuMDA4Ljg1NGMtLjcyNCAwLTEuNDE5LS4yOC0xLjkzOC0uNzg0bC0xLjI3Ni0xLjIyOHY3LjhhMi43OTIgMi43OTIgMCAxIDEtNS41ODUgMHYtNy45NTZsLTEuMjQgMS4xNjZhMi43OTUgMi43OTUgMCAwIDEtNC41MjQtMS4wNDEgMi43OTIgMi43OTIgMCAwIDEgLjY5OC0zLjAyM2w1LjkyLTUuNTczYTIuNzg5IDIuNzg5IDAgMCAxIDMuODUuMDIzbDYuMDMgNS44MTVhMi43ODMgMi43ODMgMCAwIDEgLjA3MyAzLjk0N1oiIGZpbGw9IiM1OUJGNzgiLz48cGF0aCBkPSJNNjEuNDAyIDI4LjA1NmE1LjEzIDUuMTMgMCAwIDAtNS4xMjktNS4xMjhoLTE4LjYzYTEuNSAxLjUgMCAwIDEtMS4wMjYtLjQwNmwtNC4yNDMtMy45NzhhMS40OTIgMS40OTIgMCAwIDAtMS4wMjYtLjQwNWgtOC4xNWE1LjEzIDUuMTMgMCAwIDAtNS4xMyA1LjEyOFY1My45NmE1LjEzIDUuMTMgMCAwIDAgNS4xMyA1LjEyOUgzOC40OGMxMS42NzctMy44NyAyMC41NzMtMTMuODM3IDIyLjkxNy0yNi4xM3YtNC45MDNoLjAwNFpNNDcuODc2IDQyLjEwOGEyLjc3NyAyLjc3NyAwIDAgMS0yLjAwOC44NTRjLS43MjQgMC0xLjQxOS0uMjgtMS45MzgtLjc4NGwtMS4yNzYtMS4yMjh2Ny44YTIuNzkyIDIuNzkyIDAgMSAxLTUuNTg1IDB2LTcuOTU2bC0xLjI0IDEuMTY2YTIuNzk1IDIuNzk1IDAgMCAxLTMuOTQ3LS4xMTcgMi43OTEgMi43OTEgMCAwIDEgLjEyMS0zLjk0N2w1LjkyLTUuNTczYTIuNzg5IDIuNzg5IDAgMCAxIDMuODUuMDIzbDYuMDMgNS44MTVhMi43ODMgMi43ODMgMCAwIDEgLjA3MyAzLjk0N1oiIGZpbGw9IiM2REQzOEMiLz48cGF0aCBkPSJNMzcuNjQzIDIyLjkyNGExLjUgMS41IDAgMCAxLTEuMDI2LS40MDZsLTQuMjQzLTMuOTc0YTEuNDkyIDEuNDkyIDAgMCAwLTEuMDI2LS40MDVoLTguMTVhNS4xMyA1LjEzIDAgMCAwLTUuMTMgNS4xMjhWNDguNjNjLjA3IDAgLjE0MS4wMDQuMjExLjAwNCA2LjkzOCAwIDEzLjM5My0yLjA2IDE4Ljc5NC01LjU5N3YtMi4yNDJsLTEuMjQgMS4xNjZhMi43OTUgMi43OTUgMCAwIDEtNC41MjQtMS4wNDEgMi43OTIgMi43OTIgMCAwIDEgLjY5OC0zLjAyM2w1LjkyLTUuNTczYTIuNzg5IDIuNzg5IDAgMCAxIDMuODUuMDIzbDMuNDA0IDMuMjg0YTM0LjIyNiAzNC4yMjYgMCAwIDAgNi4zNDItMTIuNzFoLTEzLjg4di4wMDRaIiBmaWxsPSIjODJEQTlEIi8+PHBhdGggZD0iTTM3LjA3IDIyLjgwN2ExLjU0IDEuNTQgMCAwIDEtLjQ1My0uMjg5bC00LjI0My0zLjk3NGExLjQ5MiAxLjQ5MiAwIDAgMC0xLjAyNi0uNDA1aC04LjE1YTUuMTMgNS4xMyAwIDAgMC01LjEzIDUuMTI4VjM1LjQ0YzcuNy0xLjk0MiAxNC4zNi02LjQ4MiAxOS4wMDEtMTIuNjMyWiIgZmlsbD0iI0E1RUZCQyIvPjwvc3ZnPg=="
            alt="" />
        </div>
        <div class="content">
          <div class="title">{{ t('上传填好的信息表') }}</div>
          <div class="subtitle">
            {{ t('文件后缀名必须为xls 或xlsx （即Excel格式），文件大小不得大于20M。') }}
          </div>
          <Upload :file-list="fileList" accept=".xls,.xlsx" :before-upload="beforeUpload" @remove="handleRemove">
            <a class="card-download">
              <img
                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTQiIGhlaWdodD0iMTQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZD0iTTExLjE3NiAyLjA3NWEzLjc4IDMuNzggMCAwIDAtNS4zMzkgMEwxLjc2IDYuMTVhLjE0LjE0IDAgMCAwLS4wNC4xLjE0LjE0IDAgMCAwIC4wNC4xbC41NzcuNTc2YS4xNC4xNCAwIDAgMCAuMTk4IDBsNC4wNzgtNC4wNzVhMi42NjMgMi42NjMgMCAwIDEgMS44OTUtLjc4NGMuNzE2IDAgMS4zOS4yNzggMS44OTQuNzg0LjUwNy41MDYuNzg1IDEuMTguNzg1IDEuODk0IDAgLjcxNi0uMjc4IDEuMzg3LS43ODUgMS44OTRsLTQuMTU2IDQuMTU0LS42NzMuNjc0YTEuNjE1IDEuNjE1IDAgMCAxLTIuNzU1LTEuMTRjMC0uNDMyLjE2Ny0uODM3LjQ3Mi0xLjE0MWw0LjEyMy00LjEyMmEuNTUxLjU1MSAwIDAgMSAuMzktLjE2MS41NDUuNTQ1IDAgMCAxIC4zODYuOTM2bC0zLjM3IDMuMzY3YS4xNC4xNCAwIDAgMC0uMDQuMS4xNC4xNCAwIDAgMCAuMDQuMWwuNTc3LjU3NmEuMTQuMTQgMCAwIDAgLjE5OCAwbDMuMzctMy4zNjhjLjMxLS4zMTEuNDgtLjcyNC40OC0xLjE2MyAwLS40MzktLjE3MS0uODUzLS40OC0xLjE2MmExLjY0OSAxLjY0OSAwIDAgMC0yLjMyOSAwbC0uNC40MDEtMy43MjIgMy43MmEyLjY5IDIuNjkgMCAwIDAtLjc5NCAxLjkxOCAyLjcwNyAyLjcwNyAwIDAgMCAyLjcxIDIuNzA2IDIuNyAyLjcgMCAwIDAgMS45MTctLjc5Mmw0LjgzMS00LjgyOGEzLjc1NyAzLjc1NyAwIDAgMCAxLjEwNS0yLjY3IDMuNzQ0IDMuNzQ0IDAgMCAwLTEuMTA1LTIuNjdaIiBmaWxsPSIjMkQ3MEZGIi8+PC9zdmc+"
                alt="" />
              {{ t('上传文件') }}
            </a>
          </Upload>
        </div>
      </div>
    </div>
    <div class="import-footer">
      <Button type="primary" :disabled="!fileList.length" :loading="loading" @click="uploadFile">
        {{ t('下一步') }}
      </Button>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Upload, UploadProps, message } from 'ant-design-vue';
  import { uploadProteinContent } from '@/services';
  import { downloadTemplateFile } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';
  import { t } from '@bmos/i18n';

  const props = defineProps({
    templateFile: {
      type: String,
      default: '',
    },
  });

  const emit = defineEmits(['next']);

  const next = (data: any) => {
    emit('next', data);
  };

  const downloadFn = (data: any) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      console.log('error', error);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, props.templateFile + '.xlsx');
    }
  };

  // 下载模板
  const downloadTemplate = async () => {
    try {
      const res = await downloadTemplateFile(props.templateFile + '.xlsx');
      downloadFn(res.data);
    } finally {
    }
  };

  const fileList = ref<UploadProps['fileList']>([]);

  // 移除
  const handleRemove: UploadProps['onRemove'] = (_file: any) => {
    fileList.value = [];
  };

  const beforeUpload: UploadProps['beforeUpload'] = file => {
    // 限制文件大小不超过20MB
    if (file.size / 1024 / 1024 > 20) {
      message.error(t('文件大小不能超过20MB'));
      return Upload.LIST_IGNORE;
    }
    fileList.value = file ? [file] : [];
    return false;
  };

  const loading = ref(false);

  // 文件上传
  const uploadFile = async () => {
    try {
      if (!fileList.value || fileList.value.length == 0) return;
      const formData = new FormData();
      formData.append('file', fileList.value[0]);
      loading.value = true;
      const { data } = await uploadProteinContent(formData);
      message.success(t('上传成功'));
      next(data ?? {});
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };
</script>

<style lang="less" scoped>
  .importCom {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .line {
      width: 100%;
      height: 130px;
      border: 1px solid #f0f0f0;
      border-radius: 8px;
      margin: 20px;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      .icon {
        width: 120px;
        padding: 20px;
      }
      .content {
        // flex: 1;
        // width: 100%;
        .title {
          font-size: 18px;
          color: #303133;
        }
        .subtitle {
          margin-top: 5px;
          font-size: 14px;
          color: #909398;
        }
      }
    }
  }

  .card-download {
    display: flex;
    align-items: center;
    font-size: 14px;
    margin-top: 10px;
  }
  .import-footer {
    width: 100%;
    // padding: auto;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
