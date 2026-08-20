<!-- 编辑/查看 -->
<template>
  <BreadcrumbButton :loading>
    <template #breadcrumb>
      <Breadcrumb class="mes-breadcrumb">
        <breadcrumb-item @click="handleCancel">{{ t('生产批次配料') }}</breadcrumb-item>
        <breadcrumb-item>{{ titleDict[pageType] }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="handleCancel">{{ t('返回') }}</Button>
      <Button
        v-if="pageType !== 'view'"
        :loading="saveLoading"
        :disabled="tableData.length === 0"
        type="primary"
        @click="handleSave">
        {{ t('保存') }}
      </Button>
    </template>

    <div class="detail-content">
      <!-- 生产信息 -->
      <div class="section-container">
        <BMTableTitle :title="t('生产信息')" style="margin-bottom: 16px" />
        <BMForm ref="myFormRef" v-bind="formProps" />
      </div>
      <!-- 称量需求 -->
      <div style="flex: 1; overflow: hidden">
        <BMTable
          :dataSource="tableData"
          :columns="columns"
          row-key="key"
          :scroll="{ x: 844, y: 400 }"
          :search="false"
          :showRefresh="false"
          :pagination="false">
          <template #headerTitle>
            <BMTableTitle :title="t('称量需求')" />
          </template>
          <!-- <template #expandColumnTitle>{{}}</template> -->
          <template #expandedRowRender="{ record }">
            <BMTable
              :dataSource="record.batches"
              :columns="subColumns"
              row-key="id"
              :scroll="{ x: 844, y: 300 }"
              :search="false"
              :showRefresh="false"
              :pagination="false"></BMTable>
          </template>
        </BMTable>
      </div>
    </div>
  </BreadcrumbButton>
  <IngredientModal
    v-model:modalOpen="ingredientOpen"
    :params="ingredientParams"
    @ok="
      () => {
        isEditData = true;
        loadTableData();
      }
    " />
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import {
    BMTable,
    BMTableTitle,
    BMForm,
    type FormProps,
    type TableColumn,
    RenderCallbackParams,
  } from '@bmos/components';
  import { useRouter, useRoute } from 'vue-router';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { Button, Flex, message, Modal, Space } from 'ant-design-vue';
  import IngredientModal from './IngredientModal/index.vue';
  import {
    reqAllProductFormulaProcessEnableList,
    reqProductMaterialProductTreeReq,
    reqWeighingCenterTree,
    reqWeighingRequirementsEdit,
    reqWeighingRequirementsQueryInfo,
    reqWeighingRequirementsSave,
  } from '@/services';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const router = useRouter();
  const route = useRoute();
  const pageType = computed<'edit' | 'view'>(() => {
    return route.query.type as 'edit' | 'view'; // edit/view
  });

  const requireId = computed(() => {
    return route.query.id as string;
  });

  const titleDict = {
    edit: t('编辑称量需求'),
    view: t('查看称量需求'),
  };

  const myFormRef = ref();

  const productTree = ref<any[]>([]);
  const fetchProductOptionTree = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      // return data;
      // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      productTree.value = data;
      return loop(data);
    } catch (error) {
      //
    }
  };

  const getFormulaEnableList = async (productId: string) => {
    try {
      const { data } = await reqAllProductFormulaProcessEnableList(productId);
      myFormRef.value?.updateSchema({
        field: 'bomVersionId',
        componentProps: {
          options: data.map((item: any) => {
            return {
              ...item,
              label: item.productFormulaName + '-' + item.productFormulaVersionNo,
              value: item.productFormulaVersionId,
            };
          }),
        },
      });
    } catch (error) {
      return [];
    }
  };

  // 表单属性
  const formProps = reactive<FormProps>({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 120,
    layout: 'horizontal',
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 2,
    },
    showActionButtonGroup: false,
    disabled: pageType.value === 'view',
    schemas: [
      {
        field: 'productId',
        component: 'TreeSelect',
        required: true,
        label: t('产品信息'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: true,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            request: async () => {
              return await fetchProductOptionTree();
            },
            onSelect: (val: SelectValue) => {
              formModel.bomVersionId = undefined;
              if (val) {
                getFormulaEnableList(val as string);
              }
            },
          };
        },
      },
      {
        field: 'bomVersionId',
        component: 'Select',
        required: true,
        label: t('生产BOM'),
        componentProps: ({ formModel }: RenderCallbackParams) => ({
          showSearch: true,
          disabled: true,
          filterOption: (input: string, option: any) => {
            return option.label?.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          options: [],
          onChange: async () => {
            if (!formModel.bomVersionId) return;
            try {
              const { data } = await reqWeighingRequirementsQueryInfo({
                bomVersionId: formModel.bomVersionId,
                id: requireId.value,
              });
              tableData.value = data.formulas;
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        }),
      },
      {
        field: 'batchNo',
        component: 'Input',
        required: true,
        label: t('生产批号'),
        onChange: () => {
          isEditData.value = true;
        },
      },
      {
        field: 'centreWeighId',
        component: 'TreeSelect',
        required: true,
        label: t('称量中心'),
        componentProps: {
          fieldNames: { label: 'name', value: 'id' },
          request: async () => {
            try {
              const { data } = await reqWeighingCenterTree();
              return loopSelectableNotValueTree(data, 'isCategory', false);
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            isEditData.value = true;
          },
        },
      },
      {
        field: 'planDate',
        component: 'DatePicker',
        required: true,
        label: t('计划生产时间'),
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
          onChange: () => {
            isEditData.value = true;
          },
        },
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
        componentProps: {
          onChange: () => {
            isEditData.value = true;
          },
        },
      },
    ],
  });

  const ingredientParams = ref<any>({});
  const ingredientOpen = ref(false);

  // const handleIngredient = ({ selectedRows, formulaQuantity, enough }: any) => {
  //   const row = tableData.value.find((item: any) => item.key === ingredientParams.value.key) as any;
  //   if (!row) return;
  //   row.formulaQuantity = formulaQuantity;
  //   row.enough = enough;
  //   row.batches = selectedRows;
  // };

  // 表格列定义
  const columns: TableColumn[] = [
    {
      title: t('物料信息'),
      dataIndex: 'materialName',
      width: 150,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 150,
    },
    {
      title: t('折算方式'),
      dataIndex: 'dryAndPureType',
      width: 150,
      customRender: ({ record }) => {
        return record.dryAndPureType?.name ?? '-';
      },
    },
    {
      title: t('需求目标量'),
      dataIndex: 'requirementQuantity',
      width: 120,
      customRender: ({ record }: any) => {
        return `${record.requirementQuantity}${record.unit}`;
      },
    },
    {
      title: t('需求用途'),
      dataIndex: 'requirementUsage',
      width: 150,
    },
    {
      title: t('配料总量'),
      dataIndex: 'formulaQuantity',
      width: 150,
      customRender: ({ record }: any) => {
        return `${record.formulaQuantity}${record.unit}`;
      },
    },
    {
      title: t('状态'),
      dataIndex: 'enough',
      width: 150,
      customRender: ({ record }: any) => {
        const color = record.enough ? '#59BF78' : '#FF9A2F';
        return (
          <Flex align='center' gap={8}>
            <div style={{ width: '7px', height: '7px', borderRadius: '50%', backgroundColor: color }}></div>
            <span style={{ fontSize: '14px', color }}>{record.enough ? t('已满足') : t('未满足')}</span>
          </Flex>
        );
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      hideInTable: pageType.value === 'view',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('配料'),
          onClick: () => {
            // router.push({
            //   name: 'production-weighing-requirements-edit',
            //   query: { id: record.id },
            // });
            ingredientParams.value = record;
            ingredientOpen.value = true;
          },
        },
        {
          label: t('取消'),
          ifShow: record.batches?.length > 0,
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              content: t('确定要取消吗？'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                await reqWeighingRequirementsSave({
                  key: record.key,
                  groupId: requireId.value,
                  formulaMaterialId: record.formulaMaterialId,
                });
                isEditData.value = true;
                message.success(t('取消成功'));
                // await reqWeighingRequirementsCancel(record.id);
                loadTableData();
                return Promise.resolve();
              },
            });
          },
        },
      ],
    },
  ];

  // 子表格列定义
  const subColumns: TableColumn[] = [
    {
      title: t('物料批号'),
      dataIndex: 'storageMaterialBatchNo',
      width: 150,
    },
    {
      title: t('水分%'),
      dataIndex: 'hydration',
      width: 100,
    },
    {
      title: t('含量%'),
      dataIndex: 'noHydrationContent',
      width: 100,
    },
    // {
    //   title: t('理论量'),
    //   dataIndex: 'theoreticalQuantity',
    //   width: 100,
    // },
    {
      title: t('配料量'),
      dataIndex: 'formulaQuantity',
      width: 100,
      customRender: ({ record }: any) => {
        return `${record.formulaQuantity}${record.unit}`;
      },
    },
    {
      title: t('有效期至'),
      dataIndex: 'expiredDate',
      width: 150,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplier',
      width: 150,
    },
    {
      title: t('生产商'),
      dataIndex: 'producer',
      width: 150,
    },
  ];

  // 表格数据
  const tableData = ref([]);

  // 是否编辑过数据
  const isEditData = ref(false);

  const back = () => {
    router.push({
      name: 'weighing-requirements',
    });
  };

  // 取消
  const handleCancel = () => {
    if (!isEditData.value) return back();
    // 点击返回会给弹框提示
    Modal.confirm({
      title: t('提示'),
      wrapClassName: 'config-return-modal',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否对该称量需求进行保存'),
      footer() {
        return (
          <>
            <Space class='footer-btns'>
              <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
              <Button
                onClick={() => {
                  Modal.destroyAll();
                  back();
                }}>
                {t('不保存')}
              </Button>
              <Button
                type='primary'
                onClick={() => {
                  Modal.destroyAll();
                  handleSave();
                }}>
                {t('保存')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  const saveLoading = ref(false);
  // 保存
  const handleSave = async () => {
    // 保存逻辑
    try {
      saveLoading.value = true;
      const formData = await myFormRef.value?.validate();
      const params = {
        ...formData,
        formulaMaterialBatchDTOS: tableData.value,
        id: requireId.value,
      };
      // if (pageType.value === 'add') {
      //   await reqWeighingRequirementsCreate(params);
      // } else if (pageType.value === 'edit') {
      //   await reqWeighingRequirementsEdit(params);
      // }
      await reqWeighingRequirementsEdit(params);
      message.success(t('操作成功'));
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      saveLoading.value = false;
    }
  };

  const oldData = ref<any>({});

  const loadTableData = async () => {
    try {
      const { data } = await reqWeighingRequirementsQueryInfo({
        id: requireId.value,
      });
      await getFormulaEnableList(data.productId);
      oldData.value = data;
      myFormRef.value?.setFieldsValue({
        ...data,
        formulas: undefined,
      });
      tableData.value = data.formulas;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const loading = ref(false);

  onMounted(async () => {
    // if (pageType.value !== 'add') {
    loading.value = true;
    await loadTableData();
    loading.value = false;
    // }
  });
</script>

<style scoped lang="less">
  .detail-content {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
    padding-bottom: 8px;
  }

  .expand-detail {
    padding: 16px;
    background-color: #f8f8f9;
  }
</style>
