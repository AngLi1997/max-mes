<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="goBack">{{ t('批次摘要') }}</breadcrumb-item>
        <breadcrumb-item v-if="disabled">{{ t('摘要详情') }}</breadcrumb-item>
        <breadcrumb-item v-else>{{ type === 'add' ? t('新增摘要') : t('编辑摘要') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="goBack">{{ t('返回') }}</Button>
      <Button v-if="!disabled" type="primary" @click="saveData">{{ t('保存') }}</Button>
    </template>
    <BMTableTitle :title="t('摘要信息')"></BMTableTitle>
    <BMForm ref="myFormRef" v-bind="formProps" :disabled="disabled" @formModelChange="formModelChange"></BMForm>
    <div style="height: calc(100% - 110px)">
      <BMTable
        v-if="showTable"
        ref="tableInstance"
        :columns="columns"
        :show-search-border="false"
        row-key="id"
        :search="false"
        :pagination="{
          pageSize: 20,
        }"
        :dataSource="tableData"
        showIndex
        :scroll="{ x: 844, y: 400 }">
        <template #headerTitle>
          <BMTableTitle :title="t('摘要数据')"></BMTableTitle>
        </template>
        <template #toolbar>
          <Button v-if="!disabled" type="primary" @click="addData">
            {{ t('新增摘要') }}
          </Button>
        </template>
      </BMTable>
    </div>
  </BreadcrumbButton>
  <BMModalForm
    ref="handleFormRef"
    v-model:open="openAddModal"
    :title="t('新增摘要')"
    :formProps="addFormProps"
    wrapClassName="modalSizeMedium inbound-model">
    <template #footer>
      <Button @click="openAddModal = false">{{ t('取消') }}</Button>
      <Button type="primary" @click="addSubmit">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
  <Modal
    v-model:open="openRecordModal"
    :title="t('关联数据点')"
    style="top: 0px"
    :centered="true"
    :destroyOnClose="true"
    :maskClosable="false"
    :width="1300"
    @ok="selectDataPoints"
    @cancel="openRecordModal = false">
    <RelativeRecord
      ref="RelativeRecordRef"
      :processTreeData="processTreeData"
      :processData="processData"></RelativeRecord>
  </Modal>
</template>
<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { BMTableTitle, BMForm, BMTable, FormProps, TableColumn, BMTreeSelect, BMModalForm } from '@bmos/components';
  import { Input, message } from 'ant-design-vue';
  import { Breadcrumb, BreadcrumbItem, Button } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import {
    reqProcessListAll,
    lotSummaryCreate,
    lotSummaryEdit,
    queryDatasetListByProcessIdApi,
    queryDatasetDetailApi,
  } from '@/services';
  import RelativeRecord from './RelativeRecord/index.vue';

  const props = withDefaults(
    defineProps<{
      treeData: any;
      type: string;
      rowData: any;
      disabled: boolean;
    }>(),
    {
      treeData: [],
      type: 'add',
      rowData: {},
      disabled: false,
    },
  );
  const emit = defineEmits(['close', 'submit']);
  const myFormRef = ref();
  const goBack = () => {
    emit('close');
  };
  const processTreeData = ref();
  const tableInstance = ref();
  const expandedKeys = ref<any>([]);
  const openAddModal = ref(false);
  const handleFormRef = ref();
  const showTable = ref(true);
  const tableData = ref<any>([]);
  const openRecordModal = ref(false);
  const processData = ref({});
  const RelativeRecordRef = ref();
  const recordSelectIndex = ref();
  const deleteId = ref(0);
  const oldFormData = ref<any>({});

  const showUpdateData = async (data: any) => {
    const { list, name, productId, processId } = data;
    myFormRef.value?.setFieldsValue({ name, productId, processId });
    oldFormData.value = { name, productId, processId };
    tableData.value = list.map((item: any, index: any) => {
      item.deleteId = index;
      return item;
    });
    deleteId.value = tableData.value.length;
    nextTick(async () => {
      if (!processTreeData.value) {
        await formModelChange({ name, productId, processId });
      }
      await getAllOptions(data);
    });
  };
  const getAllOptions = async (data: any) => {
    const { list, productId, processId } = data;
    // 获取所属工艺
    const { data: processId_options } = await reqProcessListAll({ productId });
    myFormRef.value?.updateSchema({
      field: 'processId',
      componentProps: {
        options: [...processId_options],
      },
    });
    const process = processId_options.find((item: any) => processId == item.id) || {};
    processData.value = {
      processId,
      activeVersion: process.activeVersion,
    };
    list.map((item: any) => {
      let treeNode = processTreeData.value?.find((tree: any) => tree.id === item.datasetId);
      if (treeNode) {
        if (!treeNode.children) {
          treeNode.children = [
            {
              name: item.datasetPointName,
              id: item.datasetPointId,
            },
          ];
        } else {
          treeNode.children.push({
            name: item.datasetPointName,
            id: item.datasetPointId,
          });
        }
      }
    });
  };
  // 保存/编辑数据
  const saveData = async () => {
    if (tableData.value.length == 0) {
      message.error(t('请新增摘要数据'));
      return;
    }
    const flag = tableData.value.filter((item: any) => {
      return !item.labelName || !item.datasetPointId;
    });
    if (!flag) {
      message.error(t('请输入必填项'));
      return;
    }
    try {
      await myFormRef.value?.submit();
      const params = await myFormRef.value?.validate();
      const data = {
        ...params,
        list: tableData.value,
      };
      if (props.type === 'add') {
        // 新增数据
        await lotSummaryCreate(data);
      } else {
        // 编辑数据
        await lotSummaryEdit({ ...data, id: props.rowData.id });
      }
      message.success(t('操作成功'));
      goBack();
      emit('submit');
    } catch (error: any) {
      error?.message && message.error(error.message);
    }
  };

  const formModelChange = async (value: any) => {
    if (value.name != oldFormData.value.name) {
      // 名称改变时,不重新获取数据集
      oldFormData.value = { ...value };
      return;
    }
    if (!value.processId) {
      processTreeData.value = [];
    } else {
      const { data } = await queryDatasetListByProcessIdApi({ processId: value.processId, datasetType: 'POINT' });
      console.log('========数据获取', data);

      processTreeData.value = data.map((item: any) => {
        item.children = [];
        item.disabled = true;
        item.selectable = false;
        return item;
      });
    }
  };

  const onLoadData = async (treeNode: any) => {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise(async (resolve: (value?: unknown) => void) => {
      if (treeNode.dataRef.children) {
        resolve();
      }
      const { data } = await queryDatasetDetailApi({ id: treeNode.id });
      treeNode.dataRef.children = data.datasetPointList.map((item: any) => {
        item.isLeaf = true;
        return item;
      });
      processTreeData.value = processTreeData.value.map((item: any) => {
        item.disabled = item.children?.length <= 0;
        return item;
      });
      resolve();
    });
  };

  // 选择数据点
  const selectDataPoints = () => {
    const selectData = RelativeRecordRef.value.getClickNodeData();
    tableData.value[recordSelectIndex.value].datasetPointId = selectData.id;
    openRecordModal.value = false;
    let treeNode = processTreeData.value?.find((tree: any) => tree.id === selectData.datasetPointId);
    if (treeNode) {
      if (!treeNode.children) {
        treeNode.children = [
          {
            name: selectData.name,
            id: selectData.id,
          },
        ];
      } else {
        const data = treeNode.children.find((item: any) => {
          return item.id === selectData.id;
        });
        if (!data) {
          treeNode.children.push({
            name: selectData.name,
            id: selectData.id,
          });
        }
      }
    }
  };

  // 表单属性
  const formProps: Ref<FormProps> = ref({
    initialValues: {
      //默认值
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 130,
    showActionButtonGroup: false, //控制所有操作按钮是否展示
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('摘要名称'),
        required: true,
        colProps: {
          span: 8,
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品信息'),
        required: true,
        colProps: {
          span: 8,
        },
        componentProps: ({ formModel }: any) => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: async (value: any) => {
              // 清空所属工艺
              delete formModel.processId;
              // 获取所属工艺
              const { data } = await reqProcessListAll({
                productId: value,
              });
              myFormRef.value?.updateSchema({
                field: 'processId',
                componentProps: {
                  options: [...data],
                },
              });
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('所属工艺'),
        required: true,
        colProps: {
          span: 8,
        },
        componentProps: () => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            options: [],
            onChange: (value: any, options: any) => {
              processData.value = {
                processId: value,
                activeVersion: options.activeVersion,
              };
            },
          };
        },
      },
    ],
  });
  const addData = async () => {
    await myFormRef.value?.submit();
    openAddModal.value = true;
  };
  const addFormProps: Ref<FormProps> = ref({
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 120,
    schemas: [
      {
        field: 'num',
        component: 'Input',
        label: t('添加数量'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (rule, value) => {
                if (!value) {
                  return Promise.reject(t('请输入添加数量'));
                }
                if (!Number.isInteger(value * 1) || value * 1 == 0) {
                  return Promise.reject(t('请输入正整数'));
                }
                if (value * 1 > 100) {
                  return Promise.reject(t('添加数量不能超过100'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });
  const addSubmit = async () => {
    const { num } = (await handleFormRef.value?.validate()) || 0;
    for (let i = 0; i < num; i++) {
      tableData.value.push({
        deleteId: deleteId.value,
      });
      deleteId.value++;
    }
    openAddModal.value = false;
    handleFormRef.value?.formRef?.setFieldsValue({
      num: undefined,
    });
  };

  const columns: TableColumn[] = [
    {
      title: t('数据名称'),
      dataIndex: 'labelName',
      width: 100,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return <Input v-model:value={record.labelName} disabled={props.disabled} placeholder={t('请输入')}></Input>;
      },
    },
    {
      title: t('关联数据'),
      dataIndex: 'datasetPointId',
      customRender: ({ record }: any) => {
        if (props.disabled) {
          return <Input v-model:value={record.datasetPointName} disabled placeholder={t('请输入')}></Input>;
        }
        return (
          <BMTreeSelect
            v-model:value={record.datasetPointId}
            v-model:expandedKeys={expandedKeys}
            tree-data={processTreeData}
            show-search
            allow-clear
            load-data={onLoadData}
            style='width:100%;'
            treeNodeFilterProp='name'
            multiple={false}
            placeholder={t('请选择关联数据')}
            disabled={props.disabled}
            field-names={{ label: 'name', value: 'id' }}></BMTreeSelect>
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 60,
      actions: ({ record }) => [
        {
          label: t('预览选择'),
          ifShow: !props.disabled,
          onClick: () => {
            openRecordModal.value = true;
            recordSelectIndex.value = record.deleteId;
          },
        },
        {
          label: t('上移'),
          ifShow: !props.disabled,
          onClick: () => {
            if (record.deleteId == 0) {
              return;
            }
            const fieldData = tableData.value;
            let trueIndex = 0;
            fieldData.find((item: any, index: number) => {
              trueIndex = index;
              return item.deleteId === record.deleteId;
            });
            fieldData[trueIndex] = fieldData.splice(trueIndex - 1, 1, fieldData[trueIndex])[0];
            fieldData[trueIndex - 1].deleteId = trueIndex - 1;
            fieldData[trueIndex].deleteId = trueIndex;
          },
        },
        {
          label: t('下移'),
          ifShow: !props.disabled,
          onClick: () => {
            if (record.deleteId == tableData.value.length - 1) {
              return;
            }
            const fieldData = tableData.value;
            let trueIndex = 0;
            fieldData.find((item: any, index: number) => {
              trueIndex = index;
              return item.deleteId === record.deleteId;
            });
            fieldData[trueIndex] = fieldData.splice(trueIndex + 1, 1, fieldData[trueIndex])[0];
            fieldData[trueIndex + 1].deleteId = trueIndex + 1;
            fieldData[trueIndex].deleteId = trueIndex;
          },
        },
        {
          label: t('删除'),
          danger: true,
          ifShow: !props.disabled,
          onClick: () => {
            let index = 0;
            tableData.value = tableData.value.filter((item: any) => {
              if (item.deleteId != record.deleteId) {
                item.deleteId = index;
                index++;
                return true;
              }
              return false;
            });
          },
        },
      ],
    },
  ];
  // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
  const loopTree = (data: any) => {
    return data.map((item: any) => {
      if (item.categoryFlag) {
        item.selectable = false;
      } else {
        item.selectable = true;
      }
      item.label = item.mergeCode + '-' + item.name;
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  onMounted(() => {
    myFormRef.value.updateSchema({
      field: 'productId',
      componentProps: {
        treeData: loopTree(props.treeData[0].children) || [],
      },
    });
  });
  defineExpose({ showUpdateData });
</script>
<style scoped lang="less"></style>
