<!-- 导入框 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('批量导入')"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @cancel="cancel">
    <template #footer>
      <div class="steps-action">
        <Button v-if="current === 0" @click="cancel">
          {{ t('取消') }}
        </Button>
        <Button v-if="current === 0" type="primary" @click="next">
          {{ t('下一步') }}
        </Button>
        <Button v-if="current == steps.length - 1" type="primary" @click="cancel">
          {{ t('确定') }}
        </Button>
      </div>
    </template>
    <Steps :current="current" :items="items" size="small"></Steps>
    <div v-show="current === 0" class="step">
      <div class="tip">
        <InfoCircleOutlined style="color: #ff9a2f" />
        {{ t('数据格式不规范可能导致导入失败,可') }}"
        <a @click="downloadFile">{{ t('下载模板') }}</a>
        "{{ t('导入') }}
      </div>
      <div class="importFile">
        <div style="margin">{{ t('导入文件') }}</div>
        <Upload :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove">
          <Button type="primary">{{ t('上传文件') }}</Button>
        </Upload>
      </div>
    </div>
    <div v-show="current === 1" class="step step2">
      <Progress :percent="defaultPercent" />
      <div>{{ t('文件导入中，请稍后') }}......</div>
    </div>
    <div v-show="current === 2" class="step">
      <div v-if="success" class="step3">
        <BMIcons icon="ImportSuccess" style="width: 40px; height: 40px"></BMIcons>
        <div style="margin-left: 18px">
          <div class="infoTip1">{{ t('批量导入已完成') }}</div>
        </div>
      </div>
      <div v-else class="step3">
        <BMIcons icon="ImportFail" style="width: 40px; height: 40px"></BMIcons>
        <div style="margin-left: 18px">
          <div class="infoTip1">{{ t('导入失败,请重试') }}</div>
        </div>
      </div>
    </div>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button, Steps, Upload, Progress } from 'ant-design-vue';
  import { InfoCircleOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';
  import { fileStreamDownload } from '@bmos/utils';
  import type { UploadProps } from 'ant-design-vue';

  const props = defineProps({
    downloadTemplate: {
      //下载模板接口
      type: Function,
      default: () => {
        return '';
      },
    },
    importFile: {
      //导入文件(点下一步按钮时调用)
      type: Function,
      default: () => {
        return '';
      },
    },
  });
  const emit = defineEmits(['updateTable']);
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const current = ref<number>(0);
  const success = ref<any>(true);
  const fileList = ref<UploadProps['fileList']>([]);
  const defaultPercent = ref<number>(0);
  const Timer = ref<any>(null);
  // 下一步
  const next = async () => {
    if (fileList.value?.length == 0 && current.value === 0) {
      return message.warning(t('请先上传文件'));
    }
    current.value++;
    setTimer();
    setTimeout(() => {
      const formData = new FormData();
      fileList.value?.forEach((file: any) => {
        // formData.append('files[]', file); 导入多个文件
        formData.append('file', file); //导入一个文件,导多个时后面的会覆盖前面的
      });
      props.importFile(formData).then((res: any) => {
        if (res.headers['error-message']) {
          message.error(decodeURI(res.headers['error-message']));
          success.value = false;
          current.value = 2;
          const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
          fileStreamDownload(res.data, fileName);
        } else {
          try {
            const uint8Array = new Uint8Array(res.data);
            const decoder = new TextDecoder();
            const jsonString = decoder.decode(uint8Array);
            const jsonObject = JSON.parse(jsonString);
            if (jsonObject.code) {
              message.error(jsonObject.message);
              success.value = false;
              current.value = 2;
            }
          } catch (error: any) {
            fileList.value = [];
            message.success(t('导入成功'));
            success.value = true;
            current.value = 2;
            emit('updateTable');
          }
        }
      });
    }, 500);
  };
  const move = () => {
    if (defaultPercent.value < 100) {
      const percent = defaultPercent.value + 10;
      defaultPercent.value = percent > 100 ? 100 : percent;
    } else {
      clearInterval(Timer.value);
    }
  };
  // 条子动
  const setTimer = () => {
    Timer.value = setInterval(() => {
      move();
    }, 50);
  };
  const steps = ref([
    {
      title: t('上传文件'),
    },
    {
      title: t('文件导入'),
    },
    {
      title: t('导入结束'),
    },
  ]);
  const items = steps.value.map(item => ({
    key: item.title,
    title: item.title,
  }));
  const openModal = () => {
    open.value = true;
  };
  // 取消
  const cancel = () => {
    open.value = false;
    fileList.value = [];
    current.value = 0;
    defaultPercent.value = 0;
    clearInterval(Timer.value);
    Timer.value = null;
  };
  // 截取
  const getContentBetweenChars = (str: any) => {
    return decodeURI(str.match(/filename=(\S*).xlsx/)[1]);
  };
  // 下载模板
  const downloadFile = async () => {
    try {
      const res = await props.downloadTemplate();
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 移除
  const handleRemove: UploadProps['onRemove'] = (file: any) => {
    const index: any = fileList.value?.indexOf(file);
    const newFileList = fileList.value?.slice();
    newFileList?.splice(index, 1);
    fileList.value = newFileList;
  };

  const beforeUpload: UploadProps['beforeUpload'] = file => {
    fileList.value = [...(fileList.value || []), file];
    return false;
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) {
        fileList.value = [];
        current.value = 0;
        defaultPercent.value = 0;
        clearInterval(Timer.value);
        Timer.value = null;
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal });
</script>
<style lang="less" scoped>
  .step {
    margin-top: 10px;
    min-height: 120px;
    .tip {
      margin: 0px 0px 15px;
      padding: 6px 10px;
      background-color: #f2f3f4;
      border-radius: 4px;
      color: #909398;
    }
    .importFile {
      display: flex;
      > div {
        margin: 6px 20px 0px 0px;
      }
    }
    .step3 {
      display: flex;
      align-items: center;
      justify-content: center;
      padding-top: 30px;
      .infoTip1 {
        font-size: 16px;
        color: #242526;
      }
    }
  }
  .step2 {
    width: 75%;
    padding-top: 40px;
    text-align: center;
    margin: 0 auto;
  }
</style>
