<!-- 请验单配置-详情 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnBack">
          {{ t('请验单配置') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ title }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnBack">{{ t('返回') }}</Button>
      <Button v-if="optionStatus !== OperationType.View" type="primary" @click="save">{{ t('保存') }}</Button>
    </template>
    <BMForm ref="formRef" v-bind="formProps" />
  </BreadcrumbButton>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    BMTable,
    TableColumn,
    BMForm,
    FormProps,
    RenderCallbackParams,
    BMTableTitle,
    formInstance,
    TableInstance,
  } from '@bmos/components';
  import { OperationType } from './type';
  import { Button, FormItemRest, Input, message, Select, TreeSelect } from 'ant-design-vue';
  import { SelectValue } from 'ant-design-vue/es/select';
  import {
    reqInspectConfigSave,
    reqInspectConfigQueryDetail,
    reqInspectConfigUpdate,
    reqPlatformDictListDictCode,
  } from '@/services';
  import { useWarn } from '@/hooks';

  const route = useRoute();
  const router = useRouter();

  const returnBack = () => {
    router.push({
      name: 'PleaseCheckConfig',
    });
  };

  const tableInstance = ref<TableInstance>();
  const formRef = ref<formInstance>();

  const pleaseCheckInternalFields = [
    {
      label: t('请验单号'),
      value: 'pleaseCheckNo',
    },
    {
      label: t('指令单编号'),
      value: 'instructionNo',
    },
    {
      label: t('生产批号'),
      value: 'productBatchNo',
    },
    {
      label: t('产线名称'),
      value: 'productLineName',
    },
    {
      label: t('产线编码'),
      value: 'productLineCode',
    },
    {
      label: t('生产批量'),
      value: 'productBatchQuantity',
    },
    {
      label: t('生产批量单位'),
      value: 'productBatchQuantityUnit',
    },
    {
      label: t('物料名称'),
      value: 'materialName',
    },
    {
      label: t('物料编码'),
      value: 'materialCode',
    },
    {
      label: t('物料批号'),
      value: 'materialBatchNo',
    },
    {
      label: t('取样量'),
      value: 'sampleQuantity',
    },
    {
      label: t('取样量单位'),
      value: 'sampleQuantityUnit',
    },
    {
      label: t('请验人'),
      value: 'inspector',
    },
  ];

  const dataTreeData = ref<any[]>([
    {
      label: t('请验单内置字段'),
      value: 'PleaseCheckInternalFields',
      selectable: false,
      children: pleaseCheckInternalFields,
    },
  ]);

  const { warnModal } = useWarn();
  const deleteRecord = (record: any) => {
    warnModal(t('是否删除该请验单数据'), {
      onOk: () => {
        const formData = formRef.value?.getFormModelByField('dataList');
        const index = formData.findIndex((item: any) => item.key === record.key);
        formData.splice(index, 1);
        formRef.value?.setFormModel('dataList', formData);
      },
    });
  };

  const upMove = (record: any) => {
    const formData = formRef.value?.getFormModelByField('dataList');
    const index = formData.findIndex((item: any) => item.key === record.key);
    if (index === 0) {
      return;
    }
    const temp = formData[index];
    formData[index] = formData[index - 1];
    formData[index - 1] = temp;
    formRef.value?.setFormModel('dataList', formData);
  };

  const downMove = (record: any) => {
    const formData = formRef.value?.getFormModelByField('dataList');
    const index = formData.findIndex((item: any) => item.key === record.key);
    if (index === formData.length - 1) {
      return;
    }
    const temp = formData[index];
    formData[index] = formData[index + 1];
    formData[index + 1] = temp;
    formRef.value?.setFormModel('dataList', formData);
  };

  const columns: TableColumn[] = [
    {
      title: t('请验单数据'),
      dataIndex: 'code',
      width: 100,
      customRender: ({ record }: any) => {
        return (
          <div class='editable-cell'>
            <TreeSelect
              value={record.code}
              treeDefaultExpandAll
              allowClear={false}
              placeholder={t('请验单数据')}
              treeData={dataTreeData.value}
              disabled={optionStatus.value === OperationType.View}
              onSelect={(value: any, node: any) => {
                record.code = value;
                record.dataName = node.label;
                record.showName = node.label;
                if (pleaseCheckInternalFields.some(item => item.value === value)) {
                  record.disableDefaultValue = true;
                  record.defaultValue = undefined;
                }
                formRef.value?.validateFields(['dataList']);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('展示名称'),
      dataIndex: 'showName',
      width: 100,
      customRender: ({ record }: any) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.showName}
              placeholder={t('展示名称')}
              allowClear
              maxlength={100}
              disabled={optionStatus.value === OperationType.View}
              onChange={(e: any) => {
                record.showName = e.target.value;
                formRef.value?.validateFields(['dataList']);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('是否必填'),
      width: 50,
      resizable: true,
      dataIndex: 'required',
      customRender: ({ record }: any) => {
        return (
          <div class='editable-cell'>
            <Select
              value={record.required}
              allowClear={false}
              disabled={optionStatus.value === OperationType.View}
              placeholder={t('请选择')}
              options={[
                {
                  label: t('必填'),
                  value: 1,
                },
                {
                  label: t('非必填'),
                  value: 0,
                },
              ]}
              onChange={(value: SelectValue) => {
                record.required = value;
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('默认值'),
      dataIndex: 'defaultValue',
      width: 100,
      customRender: ({ record }: any) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.defaultValue}
              placeholder={t('默认值')}
              allowClear
              maxlength={100}
              disabled={optionStatus.value === OperationType.View || record.disableDefaultValue}
              onChange={(e: any) => {
                record.defaultValue = e.target.value;
                formRef.value?.validateFields(['dataList']);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('上移'),
          ifShow: route.query?.status !== OperationType.View,
          onClick: () => {
            upMove(record);
          },
        },
        {
          label: t('下移'),
          ifShow: route.query?.status !== OperationType.View,
          onClick: () => {
            downMove(record);
          },
        },
        {
          label: t('删除'),
          ifShow: route.query?.status !== OperationType.View,
          danger: true,
          onClick: () => {
            deleteRecord(record);
          },
        },
      ],
    },
  ];
  const getKey = () => Math.random().toString(36).substr(2, 9);
  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 8,
    },
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'cargoInfo',
        noLabel: true,
        component: () => {
          return <BMTableTitle title={t('请验单信息')} />;
        },
        colProps: {
          span: 24,
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('请验单名称'),
        required: true,
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
      },
      {
        field: 'dataList',
        noLabel: true,
        colProps: {
          span: 24,
        },
        defaultValue: [],
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                if (value.length === 0) {
                  return Promise.reject(t('请验单缺少请验单数据'));
                }
                if (value.some((item: any) => !item.showName || !item.code)) {
                  return Promise.reject(t('请验单数据展示名称或请验单数据不能为空'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <BMTable
                  ref={tableInstance}
                  showIndex
                  search={false}
                  dataSource={formModel.dataList}
                  columns={columns}
                  row-key='id'
                  pagination={false}
                  scroll={{ x: 1200, y: 400 }}>
                  {{
                    headerTitle: () => {
                      return <BMTableTitle title={t('请验单数据')} />;
                    },
                    toolbar: () => {
                      return optionStatus.value !== OperationType.View ? (
                        <Button
                          type='primary'
                          onClick={() => {
                            formModel.dataList.push({
                              required: 1,
                              key: getKey(),
                            });
                          }}>
                          {t('新增数据')}
                        </Button>
                      ) : null;
                    },
                  }}
                </BMTable>
              </FormItemRest>
            </>
          );
        },
      },
    ],
  });

  const title = ref<string>(t('新增请验单'));
  const optionStatus = ref<string>(OperationType.Add);

  const setDetail = async (id: string) => {
    try {
      const { data } = await reqInspectConfigQueryDetail(id);
      formRef.value?.setFormModels({
        ...data,
        dataList: data.dataList
          .map((item: any) => {
            return {
              ...item,
              key: getKey(),
              required: item.required ? 1 : 0,
              disableDefaultValue: pleaseCheckInternalFields.some(it => it.value === item.code),
            };
          })
          .sort((a: any, b: any) => a.sort - b.sort),
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const getDict = async () => {
    // 获取字典
    try {
      const { data } = await reqPlatformDictListDictCode('InspectionCustomFields');
      dataTreeData.value.push({
        label: t('请验单自定义字段'),
        value: 'PleaseCheckCustomFields',
        selectable: false,
        children: data,
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 监听路由变化
  watch(
    () => route.query,
    async (query: any) => {
      await nextTick();
      const { status, id } = query;
      optionStatus.value = status as string;
      getDict();
      switch (status) {
        case OperationType.Add:
          title.value = t('新增请验单');
          break;
        case OperationType.Edit:
          title.value = t('编辑请验单');
          setDetail(id);
          break;
        case OperationType.View:
          title.value = t('请验单详情');
          formRef.value?.setFormProps({
            disabled: true,
          });
          setDetail(id);
          break;
      }
    },
    {
      immediate: true,
    },
  );

  const save = async () => {
    try {
      const formData = await formRef.value?.validate();
      const { dataList, ...rest } = formData;
      const data = dataList.map((item: any, index: number) => {
        return {
          code: item.code,
          dataName: item.dataName,
          defaultValue: item.defaultValue,
          required: item.required === 1,
          showName: item.showName,
          sort: index + 1,
          ...(item.id ? { id: item.id } : {}),
        };
      });
      if (optionStatus.value === OperationType.Edit) {
        await reqInspectConfigUpdate({ ...rest, dataList: data });
        message.success(t('编辑成功'));
      } else {
        await reqInspectConfigSave({ ...rest, dataList: data });
        message.success(t('保存成功'));
      }
      returnBack();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less" scoped>
  .mes-form {
    flex: 1;
    overflow: auto;
  }
</style>
