import { reqExpressionParseGET, reqExpressionSave, reqExpressionUpdate } from '@/api';
import { QuestionCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button, Col, FormItemRest, Input, Popover, Row, Table, Tag, Textarea, message } from 'ant-design-vue';
import { modalStatus } from '../enum';
export const useModalForm = (useTree: any) => {
  const { treeData, pageExpression } = useTree;
  //弹窗开关
  const modalInstance = reactive({
    addStorage: false,
  });
  //弹窗确定按钮
  const isDisabled = ref<Object>({ disabled: true });
  //弹窗类型
  const formModalType = ref<string>(modalStatus.Add);
  //弹窗初始化数据
  const formDefaultValue = ref<FormProps['initialValues'] | any>({
    expressionCategoryId: '',
    position: '',
  });
  //帮助提示
  const helps = [
    {
      title: t('函数'),
      items: ['max', 'min', 'abs', 'sqrt', 'log10', 'log2', 'ln'],
    },
    {
      title: t('操作符'),
      items: ['+', '-', '*', '/', '^'],
    },
    {
      title: t('常量'),
      items: ['e'],
    },
  ];
  const modalFormRef = ref<ModalFormInstance | any>();
  //解析公式
  const parseExpression = async (value: string) => {
    if (!value) return message.error(t('请填写公式表达式'));
    try {
      const { data } = await reqExpressionParseGET(value);
      await modalFormRef.value?.formRef?.setFormModel(
        'expressionParse',
        data.map((item: string) => {
          return {
            key: item,
            value: undefined,
          };
        }),
      );
      isDisabled.value = {
        disabled: false,
      };
    } catch (error: any) {
      error.message && message.error(error.message || t('解析失败'));
    }
  };
  //弹窗fom表单
  const addFomUse = computed(() => {
    const schemas = [
      {
        field: 'expressionCategoryId',
        component: 'TreeSelect',
        label: t('公式分类'),
        required: true,
        componentProps: {
          disabled: formModalType.value == modalStatus.Edit ? true : false,
          treeData: treeData?.value[0]?.children,
          fieldNames: {
            children: 'children',
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
        label: () => {
          return (
            <>
              <span>{t('表达式')}</span>
              <Popover title={t('帮助')} overlayClassName='expression-help-popover'>
                {{
                  default: () => <QuestionCircleOutlined class='expression-help-icon' />,
                  content: () => (
                    <div class='container'>
                      {helps.map((items: any) => {
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
            </>
          );
        },
        required: true,
        labelWidth: 75,
        component: ({ formModel, field }: RenderCallbackParams) => {
          return (
            <Row gutter={8} class='colRow'>
              <Col span='18'>
                <Textarea
                  v-model:value={formModel[field]}
                  placeholder={t('请输入表达式')}
                  rows={1}
                  onChange={() => {
                    modalFormRef.value?.formRef?.setFormModel('expressionParse', []);
                    isDisabled.value = {
                      disabled: true,
                    };
                  }}
                />
              </Col>
              <Col span='2'>
                <Button style='margin-left:16px' type='primary' onClick={() => parseExpression(formModel[field])}>
                  {t('解析')}
                </Button>
              </Col>
            </Row>
          );
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入表达式'),
            },
          ];
        },
      },
      {
        field: 'expressionParse',
        label: t(''),
        labelWidth: 10,
        formItemProps: {
          labelCol: {
            span: 0,
          },
          wrapperCol: {
            span: 24,
          },
        },
        component: ({ formModel, field }: RenderCallbackParams) => {
          return (
            <>
              {formModel[field] && (
                <Table
                  class='expressionTable'
                  pagination={false}
                  dataSource={formModel[field]}
                  columns={[
                    {
                      title: t('参数'),
                      dataIndex: 'key',
                      key: 'key',
                      width: 100,
                    },
                    {
                      title: t('参数命名'),
                      dataIndex: 'value',
                      key: 'value',
                      width: 200,
                    },
                  ]}
                  v-slots={{
                    bodyCell: ({ column, index }: any) => {
                      return column.title == t('参数') ? (
                        <span> {formModel[field][index]['key']}</span>
                      ) : (
                        <FormItemRest>
                          <Input v-model:value={formModel[field][index]['value']} />
                        </FormItemRest>
                      );
                    },
                  }}></Table>
              )}
            </>
          );
        },
      },
    ];
    return {
      schemas,
      disabled: false,
    };
  });
  //确认
  const handleModalSubmit = async (fromModel: any) => {
    switch (formModalType.value) {
      case modalStatus.Add:
        await handleModalAdd(fromModel);
        break;
      case modalStatus.Edit:
        await handleModalEdit(fromModel);
        break;
      case modalStatus.View:
        modalInstance.addStorage = false;
        break;
    }
  };
  //新增
  const handleModalAdd = async (fromModel: any) => {
    try {
      const res = await reqExpressionSave(fromModel);
      if (res.code === 0) message.success(t('新增成功'));
      pageExpression.value?.fetchData();
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  //编辑
  const handleModalEdit = async (fromModel: any) => {
    try {
      const res = await reqExpressionUpdate(fromModel);
      if (res.code === 0) message.success(t('编辑成功'));
      pageExpression.value?.fetchData();
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const storageAdd = (treeNode: any, isType: any) => {
    formModalType.value = isType;
    switch (formModalType.value) {
      case modalStatus.Add:
        addProcessing(treeNode);
        break;
      case modalStatus.Edit:
        //处理数据
        processing(treeNode);
        break;
      case modalStatus.View:
        //显示隐藏
        addFomUse.value.disabled = true;
        //处理数据
        processing(treeNode);
        break;
    }
  };
  //新增数据处理
  const addProcessing = (fromData: any) => {
    let expressionCategoryId = '';
    expressionCategoryId = fromData.id === 'all' ? void 0 : fromData.id;
    formDefaultValue.value = { expressionCategoryId };
    isDisabled.value = {
      disabled: true,
    };
    modalInstance.addStorage = true;
  };
  // 查询和编辑数据处理
  const processing = (fromData: any) => {
    formDefaultValue.value = { ...fromData };
    if (fromData.expressionParse.length > 0) {
      isDisabled.value = {
        disabled: false,
      };
    }
    modalInstance.addStorage = true;
  };
  return {
    addFomUse,
    modalInstance,
    formDefaultValue,
    formModalType,
    modalFormRef,
    isDisabled,
    storageAdd,
    handleModalSubmit,
  };
};
