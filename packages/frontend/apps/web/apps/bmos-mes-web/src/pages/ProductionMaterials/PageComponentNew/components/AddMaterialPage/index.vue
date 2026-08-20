<template>
  <div class="process-flow-container">
    <Row class="header">
      <Col :span="16">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ infoTitle }}</breadcrumb-item>
          <breadcrumb-item>
            {{ addModalFormTitle }}
          </breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button v-if="!id.value || (isEdit.value && isEdit.value)" type="primary" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="setting">
      <BMTableTitle :title="t('基础信息')" />
      <!-- 表单 -->
      <BMForm v-if="showBm" ref="addModalFormRef" v-bind="modalFormProps"></BMForm>
      <BMTableTitle :title="t('扩展信息')" />
      <div class="batch-table">
        <BMTable
          :dataSource="tableData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :pagination="false"
          :search="false"
          :showToolBar="false"
          :scroll="{ x: 844, y: 400 }"></BMTable>
        <div v-if="!viewTitles.includes(addModalFormTitle)" class="button-add">
          <Button :icon="h(PlusOutlined)" type="link" block @click="addSource">{{ t('新增字段配置') }}</Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="tsx">
  import { Row, Col, Breadcrumb, BreadcrumbItem, Button, message, Select, Input, Modal } from 'ant-design-vue';
  import { BMForm, BMTableTitle, TableColumn, BMTable } from '@bmos/components';
  import type { FormProps, TableInstance } from '@bmos/components';
  import { h } from 'vue';
  import { PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';

  import { t } from '@bmos/i18n';
  import {
    postProductMaterialSaveApi,
    updateProductMaterialApi,
    getMaterialFieldList,
    reqMaterialFieldInfo,
  } from '@/services';
  import { useAddModalForm } from './hooks/useAddModalForm';

  const emit = defineEmits(['backAndSave']);
  const props = defineProps<{
    isEdit: Ref<boolean>;
    id: Ref<string>;
    open: boolean;
    categoryTreeData: [];
    categoryType: number;
    initialValues: Ref<any>;
    tableInstance: TableInstance | null;
  }>();
  const allFieldInfoOptions = ref<any>(); //存所有字段信息(表格第二列)(拼起来)
  const { categoryTreeData } = toRefs(props);
  const addModalFormRef = ref<any>(null);
  const addModalFormTitle = ref<string>('');
  const addTitles = [t('新增原辅包'), t('新增中间品'), t('新增产品')];
  const editTitles = [t('编辑原辅包'), t('编辑中间品'), t('编辑产品')];
  const viewTitles = [t('查看原辅包'), t('查看中间品'), t('查看产品')];
  const modalFormProps = ref<FormProps>({ schemas: [] });
  const getMaterialPrincipalListFn = ref<Function>(() => {});
  const showBm = ref(true); //刷新表单
  const infoTitle = computed(() => {
    return [t('原辅包信息'), t('中间品信息'), t('产品信息')][props.categoryType];
  });
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('字段类型'),
      dataIndex: 'fieldType',
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.fieldType}
            style='width: 100%'
            placeholder={t('请选择')}
            disabled={viewTitles.includes(addModalFormTitle.value)}
            optionFilterProp='label'
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}
            onChange={() => {
              record.field = '';
              record.fieldValue = '';
              record.showName = '';
              record.fieldName = '';
            }}
            fieldNames={{
              label: 'fieldTypeName',
              value: 'fieldType',
            }}
            options={fieldTypeOptions.value}></Select>
        </div>
      ),
    },
    {
      title: t('字段信息'),
      dataIndex: 'field',
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.showName}
            style='width: 100%'
            disabled={viewTitles.includes(addModalFormTitle.value)}
            placeholder={t('请选择')}
            optionFilterProp='label'
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}
            fieldNames={{
              label: 'showName',
              value: 'showName',
            }}
            onChange={(value: any, option: any) => {
              record.fieldName = option?.fieldName;
              record.showName = option?.showName;
            }}
            options={getFieldInfoOptions(record)}></Select>
        </div>
      ),
    },
    {
      title: t('字段值'),
      dataIndex: 'fieldValue',
      customRender: ({ record }) => {
        return (
          <Input
            disabled={
              viewTitles.includes(addModalFormTitle.value) ||
              record.fieldType === 'MaterialBatchCustomFields' ||
              record.fieldType === 'MaterialPieceCustomFields'
            }
            v-model:value={record.fieldValue}
            placeholder={t('请输入')}
          />
        );
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 100,
      hideInTable: props.id.value && !props.isEdit.value,
      actions: ({ record }: any) => [
        {
          label: t('删除'),
          danger: true,
          onClick: (e: any) => {
            Modal.confirm({
              title: t('提示'),
              icon: h(ExclamationCircleOutlined),
              content: t('是否删除该数据删除后无法恢复,是否删除?'),
              onOk() {
                tableData.value = tableData.value.filter((item, index) => index !== e.index);
                record.field = '';
                record.fieldValue = '';
                record.showName = '';
                record.fieldName = '';
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];
  // 表格数据
  const tableData = ref<any>([]);
  //  表格内的字段类型下拉框数据
  const fieldTypeOptions = ref<any>([]);
  // 新增字段配置
  const addSource = () => {
    const params = {
      // id: '',
      fieldType: '',
      field: '',
      fieldValue: '',
    };
    tableData.value.push(params);
  };
  // 根据字段类型查对应的字段信息
  const getFieldInfoOptions = (record: any) => {
    const temp = tableData.value?.map((item: any) => item.showName);
    let temp2 = fieldTypeOptions.value
      ?.find((item: any) => item.fieldType === record.fieldType)
      ?.fieldList?.map((item2: any) => {
        return {
          ...item2,
          showName: item2.fieldName + '-' + item2.field, //展示为数据标签-数据键值
          disabled: temp.includes(item2.fieldName + '-' + item2.field), // 判断设置字段类型disabled
        };
      });
    if (props.id.value) {
      // 编辑或查看
      // 把field和fieldName拼接起来
      const splicing = record.fieldName + '-' + record.field;
      const temp3 = temp2?.map((i: any) => i.showName);
      if (temp3?.includes(splicing)) {
        // 不用添加
      } else {
        temp2?.push({
          field: record.field,
          fieldName: record.fieldName,
          showName: record.fieldName + '-' + record.field,
          disabled: true,
        });
      }
      temp2 = temp2?.filter((item: any) => item.field && item.fieldName);
      return temp2;
    } else {
      return temp2;
    }
  };

  // 返回
  const back = () => {
    emit('backAndSave');
  };
  // 保存
  const save = async () => {
    tableData.value = tableData.value.map((item: any) => {
      return {
        ...item,
        fieldTypeName: getFieldTypeName(item.fieldType),
        // fieldName: getFieldName(item.showName),
        field: item.showName?.replace(`${item.fieldName}-`, ''),
      };
    });
    let flag = true; //判断扩展信息的字段类型和字段信息下拉选完没
    tableData.value?.forEach((item: any) => {
      if (!item.fieldType || !item.field) {
        flag = false;
      }
    });
    if (!flag) return message.error(t('扩展字段信息未选择'));
    addModalFormRef.value?.validate().then(async (data: any) => {
      try {
        await (data.id ? updateProductMaterialApi : postProductMaterialSaveApi)({
          categoryCode: props.categoryCode,
          ...data,
          fieldSaveDTOList: tableData.value,
        });
        emit('backAndSave');
        props.tableInstance?.fetchData(0);
        message.success(t(data.id ? '修改成功' : '新增成功'));
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 查生产物料的自定义字段信息(编辑和查看时候调用,用于回显表格)
  const getMaterialFieldInfo = async () => {
    try {
      const res = await reqMaterialFieldInfo(props.initialValues.value.id);
      tableData.value = res.data?.map((item: any) => {
        return {
          ...item,
          showName: item.fieldName + '-' + item.field,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 初始化弹框数据
  const initDefaultData = async () => {
    await getFieldList();
    if (props.id.value) {
      //  编辑或查看
      try {
        // 获取所属产品列表
        getMaterialPrincipalListFn.value(props.initialValues.value.materialCategoryId);
        modalFormProps.value.disabled = !props.isEdit.value;
      } catch (error: any) {
        message.error(error.message);
      }
      getMaterialFieldInfo();
      if (props.isEdit.value) {
        addModalFormTitle.value = editTitles[props.categoryType];
      } else {
        addModalFormTitle.value = viewTitles[props.categoryType];
        modalFormProps.value.disabled = true;
        // 刷新表单
        showBm.value = false;
        nextTick(() => {
          showBm.value = true;
        });
      }
    } else {
      // 新增
      modalFormProps.value.disabled = false;
      addModalFormTitle.value = addTitles[props.categoryType];
      tableData.value = [];
      if (props.initialValues.value.materialCategoryId) {
        // 获取所属产品列表
        getMaterialPrincipalListFn.value(props.initialValues.value.materialCategoryId);
      }
    }
  };
  // 查字段类型下拉框数据,其中有对应的字段信息fieldList
  const getFieldList = async () => {
    try {
      const res = await getMaterialFieldList();
      fieldTypeOptions.value = res.data;
      let arr: any = [];
      fieldTypeOptions.value?.forEach((item: any) => {
        arr.push(...item.fieldList);
      });
      arr = arr?.map((item: any) => {
        return {
          ...item,
          showName: item.fieldName + '-' + item.field,
        };
      });
      allFieldInfoOptions.value = arr;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 通过字段类型下拉框value去查对应的fieldTypeName
  const getFieldTypeName = (val: any) => {
    const obj = fieldTypeOptions.value?.find((item: any) => item?.fieldType === val);
    return obj?.fieldTypeName;
  };
  // 通过字段信息下拉框value去查对应的fieldName
  const getFieldName = (val: any) => {
    const obj = allFieldInfoOptions.value?.find((item: any) => item?.showName === val);
    return obj?.fieldName;
  };
  onMounted(() => {
    const data = useAddModalForm({
      categoryTreeData,
      initialValues: props.initialValues,
      categoryType: props.categoryType,
      add: !props.id.value,
    });
    modalFormProps.value = data.addModalFormProps; //回显表单值
    getMaterialPrincipalListFn.value = data.getMaterialPrincipalListFn;
    initDefaultData();
    // 刷新表单
    showBm.value = false;
    nextTick(() => {
      showBm.value = true;
    });
  });
</script>

<style scoped lang="less">
  .process-flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    .header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
      }
      .action {
        text-align: right;
      }
    }
  }
  .vertical-group-divider {
    padding: 0;
    margin: 0;
  }
  .setting {
    width: 100%;
    height: calc(100% - 56px);
    // display: flex;
    background-color: var(--bmos-primary-color-white);
    padding: 12px 12px 0px 12px;
    display: flex;
    flex-direction: column;
    .batch-table {
      // flex: 1;
      // overflow-y: auto;
      height: calc(100% - 465px);
    }
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
  .button-add {
    width: 150px;
  }
</style>
