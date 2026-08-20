<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :destroyOnClose="true"
    wrapClassName="modalSizeMedium"
    class="add-expression-modal"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
    RenderCallbackParams,
  } from '@bmos/components';
  import {
    Row,
    message,
    Col,
    Textarea,
    Button,
    Space,
    FormItem,
    Input,
    Popover,
    Tag,
  } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ALL_TYPE, MODAL_STATUS } from '../types';
  import {
    reqExpressionParseGET,
    reqExpressionSave,
    reqExpressionUpdate,
  } from '@/api';
  import { QuestionCircleOutlined } from '@ant-design/icons-vue';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      treeData: any[];
      rowData?: any;
      status?: MODAL_STATUS;
      selectCategory?: string;
    }>(),
    {
      rowData: {},
      status: MODAL_STATUS.ADD,
      selectCategory: ALL_TYPE.ALL,
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('新增分类'));

  const isParsed = ref<boolean>(false);
  const request = async (formModal: any) => {
    if (!formModal.expressionParse || formModal.expressionParse?.length === 0) {
      if (isParsed.value) {
        return Promise.reject({
          message: t('公式表达式修改需重新解析公式参数'),
        });
      } else {
        return Promise.reject({
          message: t('新建公式需解析公式表达式参数'),
        });
      }
    }
    const params = {
      ...formModal,
    };
    const { expressionCategoryId, ...editParams } = params;
    switch (props.status) {
      case MODAL_STATUS.ADD:
        return await reqExpressionSave(params);
      case MODAL_STATUS.EDIT:
        editParams.id = props.rowData.id;
        return await reqExpressionUpdate(editParams);
      default:
        return Promise.reject();
    }
  };
  const submit = async (modalFormType: ModalFormType) => {
    try {
      await modalFormRef.value?.submit(request);
      if (props.status === MODAL_STATUS.EDIT) {
        message.success(t('编辑成功'));
      } else {
        message.success(t('新增成功'));
      }
      emit('updateTable');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  // 解析表达式
  const parseExpression = async (expression: string) => {
    if (!expression) return message.error(t('请填写公式表达式'));
    try {
      const { data } = await reqExpressionParseGET(expression);
      isParsed.value = true;
      modalFormRef.value?.formRef?.setFormModel(
        'expressionParse',
        data.map((item: string) => {
          return {
            key: item,
            value: undefined,
          };
        }),
      );
      okButtonProps.value = {
        disabled: false,
      };
    } catch (error: any) {
      error.message && message.error(error.message || t('解析失败'));
    }
  };

  const helps = [
    {
      title: t('函数'),
      items: ['max', 'min', 'abs', 'sqrt', 'log10', 'log2', 'ln'],
    },
    {
      title: t('操作符'),
      items: ['+', '-', '*', '/', '^'],
    },
    // {
    //   title: t('常量'),
    //   items: ['e'],
    // },
  ];
  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        field: 'expressionCategoryId',
        component: 'TreeSelect',
        label: t('公式分类'),
        required: true,
        componentProps: {
          disabled: true,
          treeData: props.treeData[0]?.children,
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('公式名称'),
        required: true,
      },
      {
        field: 'result',
        component: 'Input',
        label: t('计算结果'),
        required: true,
      },
      {
        field: 'expression',
        label: t('公式表达式'),
        required: true,
        component: ({
          formModel,
          formInstance,
          field,
        }: RenderCallbackParams) => {
          return (
            <>
              <Row gutter={8}>
                <Col span='18'>
                  <Textarea
                    v-model:value={formModel[field]}
                    placeholder={t('请输入公式表达式')}
                    onChange={() => {
                      modalFormRef.value?.formRef?.setFormModel(
                        'expressionParse',
                        [],
                      );
                      modalFormRef.value?.formRef?.validateFields([
                        'expression',
                      ]);
                      okButtonProps.value = {
                        disabled: false,
                      };
                    }}
                    rows={3}
                  />
                </Col>
                <Col span='6'>
                  <Space direction='vertical'>
                    <Popover
                      title={t('帮助')}
                      overlayClassName='expression-help-popover'>
                      {{
                        default: () => (
                          <QuestionCircleOutlined class='expression-help-icon' />
                        ),
                        content: () => (
                          <div class='container'>
                            {helps.map((items: any) => {
                              console.log(items, 111);

                              return (
                                <div class='item'>
                                  <div class='item-title'>{items.title}</div>
                                  <span class='item-content'>
                                    {items.items.map((item: string) => {
                                      return <Tag color='blue'>{item}</Tag>;
                                    })}
                                  </span>
                                </div>
                              );
                            })}
                          </div>
                        ),
                      }}
                    </Popover>

                    <Button
                      type='primary'
                      onClick={() => parseExpression(formModel[field])}>
                      {t('解析')}
                    </Button>
                  </Space>
                </Col>
              </Row>
            </>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请输入公式表达式'),
            },
          ];
        },
      },
      {
        field: 'expressionParse',
        label: t('公式表达式'),
        formItemProps: {
          labelCol: {
            span: 0,
          },
          wrapperCol: {
            span: 24,
          },
        },
        component: ({
          formModel,
          formInstance,
          field,
        }: RenderCallbackParams) => {
          // [
          //   {
          //     key: 'a',
          //     value: 'non ullamco cillum',
          //   },
          //   {
          //     key: 'b',
          //     value: 'pariatur',
          //   },
          // ];
          return (
            <>
              {formModel[field] &&
                formModel[field].map(
                  (item: { key: string; value: string }, index: number) => {
                    return (
                      <FormItem
                        labelCol={{ span: 6 }}
                        wrapperCol={{ span: 16 }}
                        label={t('参数') + item.key}
                        name={t('参数') + item.key}>
                        <Input
                          v-model:value={formModel[field][index]['value']}
                        />
                      </FormItem>
                    );
                  },
                )}
            </>
          );
        },
      },
    ],
  });

  const okButtonProps = ref({});
  watch(
    () => open.value,
    async val => {
      await nextTick();
      const expressionCategoryId = props.selectCategory
        ? props.selectCategory
        : ALL_TYPE.ALL;
      switch (props.status) {
        case MODAL_STATUS.ADD:
          title.value = t('新增公式');
          okButtonProps.value = {
            disabled: true,
          };
          await nextTick();
          modalFormRef.value?.formRef?.updateSchema({
            field: 'expressionCategoryId',
            componentProps: {
              disabled: false,
              treeData: props.treeData[0]?.children,
            },
          });
          modalFormRef.value?.formRef?.setFormModel(
            'expressionCategoryId',
            expressionCategoryId === ALL_TYPE.ALL
              ? undefined
              : expressionCategoryId,
          );
          modalFormRef.value?.formRef?.clearValidate();
          break;
        case MODAL_STATUS.EDIT:
          title.value = t('编辑公式');
          okButtonProps.value = {
            disabled: false,
          };
          await nextTick();
          modalFormRef.value?.formRef?.setFormProps({
            disabled: false,
          });
          modalFormRef.value?.formRef?.updateSchema({
            field: 'expressionCategoryId',
            componentProps: {
              disabled: true,
              treeData: props.treeData[0]?.children,
            },
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        case MODAL_STATUS.VIEW:
          title.value = t('查看公式');
          okButtonProps.value = {
            disabled: true,
          };
          await nextTick();
          modalFormRef.value?.formRef?.updateSchema({
            field: 'expressionCategoryId',
            componentProps: {
              disabled: true,
              treeData: props.treeData[0]?.children,
            },
          });
          modalFormRef.value?.formRef?.setFormProps({
            disabled: true,
          });
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
          });
          break;
        default:
          break;
      }
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less">
  .add-expression-modal {
    .expression-help-icon {
      cursor: pointer;
    }
  }
  .expression-help-popover {
    .container {
      padding-left: var(--bmos-padding-small);
      padding-right: var(--bmos-padding-small);
      .item-title {
        color: #000;
        margin-bottom: var(--bmos-margin-large);
        margin-top: var(--bmos-margin-large);
      }
      .plat-tag {
        background-color: var(--bmos-primary-color-tab);
        color: var(--bmos-primary-color);
        border: none;
        font-size: 14px;
        margin-inline-end: 9px;
        border-radius: 3px;
        line-height: 20px;
        padding: 2px 8px 2px 8px;
        margin-bottom: 5px;
      }
    }
  }
</style>
