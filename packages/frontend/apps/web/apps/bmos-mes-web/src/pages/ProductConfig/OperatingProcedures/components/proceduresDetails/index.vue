<template>
  <div class="add-code-rule">
    <Row class="add-code-rule-header">
      <Col :span="8">
        <slot name="breadcrumb">
          <Breadcrumb class="crumb">
            <breadcrumb-item @click="moKen">{{ t('操作规程') }}</breadcrumb-item>
            <breadcrumb-item>{{ t(modelName[props.state]) }}</breadcrumb-item>
          </Breadcrumb>
        </slot>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <slot name="btn">
            <Button @click="moKen">{{ t('返回') }}</Button>
            <Button v-if="props.state !== modalStatus.View" type="primary" @click="submit">
              {{ t('保存') }}
            </Button>
          </slot>
        </Space>
      </Col>
    </Row>
    <div class="code-rule-form">
      <BMForm ref="submitForm" v-bind="{ ...fromProps as FormProps, disabled:props.state === modalStatus.View }" />
      <div class="code-rule-form-pdf">
        <BMTableTitle :title="t('文件预览')" />
        <Upload class="form-pdf-upload" :before-upload="beforeUpload" :show-upload-list="false">
          <Button type="primary" :disabled="props.state === modalStatus.View">{{ t('上传文件') }}</Button>
        </Upload>
        <div class="pdf_size_box">
          <BMIcons class="pdf_size_icon" icon="enlarge" @click="sizeChange(true)" />
          <BMIcons class="pdf_size_icon" icon="narrow" @click="sizeChange(false)" />
        </div>
        <div class="form-pdf-cent">
          <PDFDew :type="fileList.type" :pdfSize="pdfSize" :file="fileList.file" />
        </div>
      </div>
    </div>
    <PermissionModal
      v-model:permissionOpen="permissionModalOpen"
      :type="false"
      :save-immediate="false"
      @ok="savePermission" />
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, FormProps, BMTableTitle, Recordable } from '@bmos/components';
  import PermissionModal from '@/components/PermissionDept/index.vue';
  import PDFDew from './components/PDFDew/index.vue';
  import { Upload, Button } from 'ant-design-vue';
  import { modalStatus, modelName } from '../../enum';
  import { useParams, useOperate } from './hooks';
  import { BMIcons } from '@bmos/icons';

  const pdfSize = ref(75);
  const props = withDefaults(
    defineProps<{
      state?: modalStatus;
      treeList?: Recordable | undefined;
      treeCutId?: string;
      detailsRow?: Recordable;
    }>(),
    {
      state: modalStatus.View,
      treeList: () => ({}),
      treeCutId: '',
      detailsRow: () => ({}),
    },
  );
  const emits = defineEmits(['back']);
  //返回
  const back = () => {
    emits('back');
  };
  const UseParams = useParams();
  const { isState, isModalOpen, fileList, fromProps, treeFormProps, submitForm, permissionModalOpen } = UseParams;
  const { submit, beforeUpload, savePermission, specificsAPI, moKen } = useOperate({ UseParams, back });

  const sizeChange = (flag: boolean) => {
    if (flag) {
      // 放大
      pdfSize.value += 25;
      if (pdfSize.value > 400) {
        pdfSize.value = 400;
      }
    } else {
      pdfSize.value -= 25;
      if (pdfSize.value < 25) {
        pdfSize.value = 25;
      }
    }
  };

  watch(
    () => props.state,
    val => {
      isState.value = val;
      treeFormProps.value = props.treeList[0]?.children;
      fromProps.initialValues!.categoryId = props.treeCutId || null;
      switch (props.state) {
        case modalStatus.View:
          specificsAPI(props.detailsRow);
          break;
        case modalStatus.Add:
          isModalOpen.value = true;
          break;
        case modalStatus.Edit:
          specificsAPI(props.detailsRow);
          break;
        case modalStatus.Copy:
          specificsAPI(props.detailsRow);
          break;
      }
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>
<style lang="less" scoped>
  .add-code-rule {
    width: 100%;
    height: 100%;
    .add-code-rule-header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
        li {
          cursor: pointer;
        }
      }
      .action {
        text-align: right;
      }
    }
    .code-rule-form {
      background-color: #fff;
      padding: var(--bmos-padding-small);
      height: calc(100% - 55px);
      overflow: hidden;
      .code-rule-form-pdf {
        position: relative;
        height: calc(100% - 113px);
        .form-pdf-upload {
          position: absolute;
          right: 0;
          top: 0;
          z-index: 10;
        }
        .form-pdf-cent {
          margin-top: 10px;
          background-color: #e4e4e4;
          height: calc(100% - 46px);
          overflow: auto;
          &::-webkit-scrollbar {
            background-color: #fff !important;
          }
        }
        .pdf_size_box {
          position: absolute;
          top: 62px;
          right: 16px;
          width: 50px;
          height: 80px;
          border-radius: 5px;
          background-color: #fff;
          box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
          display: flex;
          align-items: center;
          justify-content: space-around;
          flex-direction: column;
          z-index: 999;
          .pdf_size_icon {
            width: 20px;
            height: 20px;
          }
        }
      }
    }
  }
</style>
