<template>
  <div class="step1">
    <div
      :style="{
        width: '390px',
        margin: '0 auto',
      }">
      <BMForm ref="myFormRef" v-bind="formProps"></BMForm>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { formInstance, BMForm, FormProps } from '@bmos/components';
  import { Button } from 'ant-design-vue';

  defineOptions({
    name: 'StepForm',
  });

  const formProps: FormProps = {
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    labelWidth: 80,
    schemas: [
      {
        field: 'field1',
        label: t('模板'),
        component: ({ formModel }) => {
          return (
            <>
              <Button onClick={() => downloadFile()}>{t('下载模板')}</Button>
              <div
                style={{
                  background: 'var(--bmos-background-color)',
                  fontSize: '12px',
                  color: 'var(--bmos-fourth-level-text-color)',
                  lineHeight: '24px',
                  marginTop: '8px',
                  padding: '0 8px',
                }}>
                {t('请下载导入模板，并按要求填写，否则可能上传失败')}
              </div>
            </>
          );
        },
      },
      {
        field: 'file',
        component: 'Upload',
        label: t('上传文件'),
        required: true,
        componentProps: ({ formModel }) => {
          return {
            maxCount: 1,
            beforeUpload: file => {
              formModel.file = [file];
              return false;
            },
          };
        },
        componentSlots: {
          default: () => <Button type='primary'>{t('上传文件')}</Button>,
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  };

  function downloadFile(fileName: any) {
    const fileUrl = '/path/to/' + fileName; // 文件的URL地址
    window.open(fileUrl);
  }

  const myFormRef = ref<any>();
  const getFormValue = async () => {
    try {
      const formValue = myFormRef.value.validate();
      return Promise.resolve(formValue);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  defineExpose({
    myFormRef,
    getFormValue,
  });
</script>

<style lang="less" scoped>
  .step1 {
    padding-top: 60px;
    box-sizing: border-box;
  }
</style>
