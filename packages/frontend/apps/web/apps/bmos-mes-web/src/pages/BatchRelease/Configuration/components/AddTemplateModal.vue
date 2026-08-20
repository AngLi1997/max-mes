<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('新增模板')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, NormalModalForm, Recordable, RenderCallbackParams } from '@bmos/components';
  import { Button, message, Space, UploadProps } from 'ant-design-vue';
  import { reqLotReleaseTemplateCreateTemplate, reqLotReleaseTemplateUploadTemplate } from '@/services';
  import { LoadingOutlined, UploadOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';
  import DepartMent from '@/components/DepartMent/index.vue';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('templateModalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      treeData: any[];
      treeNode?: Recordable;
    }>(),
    {
      treeData: () => [],
      treeNode: () => ({}),
    },
  );
  const customRequest: UploadProps['customRequest'] = (options: any) => {
    const formData = new FormData();
    formData.append('file', options.file);
    reqLotReleaseTemplateUploadTemplate(formData)
      .then((res: any) => {
        options.onSuccess(res.data as any);
      })
      .catch((error: any) => {
        error.message && message.error(error.message);
        options.onError(error);
      });
  };
  const departIconRender = (model: Recordable) => {
    const style_icon = {
      width: '16px',
      height: '16px',
      marginRight: '8px',
      verticalAlign: 'sub',
    };
    if (!model['deptIds'] || model['deptIds']?.length === 0) {
      return h(BMIcons, {
        icon: 'Depart',
        style: style_icon,
      });
    } else {
      return h(BMIcons, {
        icon: 'Success',
        style: style_icon,
      });
    }
  };

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        props.treeNode?.id !== 'all' &&
          modalFormRef.value?.formRef?.setFieldsValue({
            categoryId: props.treeNode?.id,
          });
        modalFormRef.value?.formRef?.updateSchema({
          field: 'categoryId',
          componentProps: {
            treeData: props.treeData,
          },
        });
      }
    },
  );

  const departMentRef = ref<InstanceType<typeof DepartMent>>();

  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'categoryId',
        component: 'TreeSelect',
        label: t('分类'),
        required: true,
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'fileList',
        component: 'Upload',
        label: t('上传模板'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            // 只允许表格文件
            accept: '.xlsx, .xls, .xlsm',
            maxCount: 1,
            customRequest,
            onChange: (info: any) => {
              formModel.fileList = info.fileList;
              if (info.file.status === 'done') {
                formModel.templateUrl = info.file.response;
              } else {
                formModel.templateUrl = undefined;
              }
            },
          };
        },
        componentSlots: {
          default: () => (
            <Button type='default'>
              <UploadOutlined></UploadOutlined>
              {t('上传模板')}
            </Button>
          ),
          itemRender: ({ values }: any) => {
            const { fileList } = values;
            const file = fileList?.[0] || {};
            return (
              <Space>
                {file.status === 'uploading' && <LoadingOutlined></LoadingOutlined>}
                {file.status === 'done' && <BMIcons icon='Success' style='width: 14px'></BMIcons>}
                <span style={file.status === 'error' ? 'color: red' : ''}>{file.name}</span>
              </Space>
            );
          },
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请上传模板'),
              trigger: 'change',
              validator: () => {
                if (!formModel['templateUrl']) {
                  return Promise.reject(t('请上传模板'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('模板名称'),
        required: true,
      },
      {
        field: 'version',
        component: 'Input',
        label: t('版本号'),
        required: true,
      },
      {
        field: 'deptIds',
        label: t('部门授权'),
        required: true,
        component: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <>
              <NormalModalForm
                title={t('部门授权')}
                submit={async () => {
                  const ids = departMentRef.value?.getSelectKeys();
                  formModel['deptIds'] = ids;
                  if (ids?.length) {
                    formInstance.clearValidate(['deptIds']);
                  }
                  return Promise.resolve();
                }}>
                {{
                  default: () => (
                    <DepartMent
                      ref={departMentRef}
                      checks={formModel['deptIds']}
                      type={false}
                      isAdd={true}></DepartMent>
                  ),
                  trigger: () => (
                    <Button icon={departIconRender(formModel)} class='depart-btn'>
                      {t('选择部门')}
                    </Button>
                  ),
                }}
              </NormalModalForm>
            </>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              trigger: 'change',
              validator: () => {
                if (!formModel['deptIds'] || formModel['deptIds']?.length === 0) {
                  return Promise.reject(t('请选择部门授权'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      await reqLotReleaseTemplateCreateTemplate({
        categoryId: formModal.categoryId,
        deptIds: formModal.deptIds,
        name: formModal.name,
        remark: formModal.remark,
        templateUrl: formModal.templateUrl,
        version: formModal.version,
      });
      emit('ok');
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less">
  .depart-btn {
    display: inline-flex;
    column-gap: 8px;
    align-items: center;
  }
</style>
