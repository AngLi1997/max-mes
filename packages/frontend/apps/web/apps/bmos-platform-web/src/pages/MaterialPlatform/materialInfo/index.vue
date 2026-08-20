<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :defaultSelectedKeys="['all']"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :treeField="treeField"
    :actionList="treeActionList"
    :selectedKeys="treeSelectedKeys"
    :showAllAddIcon="hasPermission('100040002000009')"
    :rowKeys="['id', 'versionId']"
    :tableFields="tableFields"
    :requests="[loadData]"
    :columns="[columns]"
    :search="[true, false]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode, instance }">
      <Button v-hasAuth="100040002000001" type="primary" @click="addMaterialHandel(treeNode)">
        {{ t('新增') }}
      </Button>
      <Button v-hasAuth="100040002000002" @click="issuedOpen = true">{{ t('下发') }}</Button>
      <Button v-hasAuth="100040002000003" @click="() => ImportModalRef.openModal()">
        {{ t('导入') }}
      </Button>
      <Dropdown :trigger="['click']">
        <Button>
          {{ t('导出') }}
        </Button>
        <template #overlay>
          <Menu>
            <MenuItem key="1" @click="export1(instance, 'screen')">{{ t('导出筛选数据') }}</MenuItem>
            <MenuItem key="2" @click="export1(instance, 'currentPage')">{{ t('导出当前页数据') }}</MenuItem>
          </Menu>
        </template>
      </Dropdown>
      <Dropdown v-if="false">
        <Button v-hasAuth="100040002000008">
          {{ t('更多') }}
          <BMIcons
            icon="Group"
            style="width: 14px; height: 14px; transform: translate(6px, -2px); vertical-align: middle"></BMIcons>
        </Button>
        <template #overlay>
          <Menu>
            <MenuItem key="1" @click="startStop('start')">
              {{ t('启用') }}
            </MenuItem>
            <MenuItem key="2" @click="startStop('stop')">{{ t('停用') }}</MenuItem>
          </Menu>
        </template>
      </Dropdown>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('物料信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <!-- 树新增编辑弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeOpen"
    :title="treeTitle"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="okModal"></BMModalForm>
  <!--新增编辑物料-->
  <BMModalForm
    ref="addModalFormRef"
    v-model:open="addOpen"
    :title="addModalFormTitle"
    :formProps="addModalFormProps"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <template v-if="!addModalFormProps.disabled">
        <Button @click="addOpen = false">{{ t('取消') }}</Button>
        <Button type="primary" @click="okAddModal">
          {{ t('确定') }}
        </Button>
      </template>
      <Button v-else type="primary" @click="addOpen = false">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
  <!--下发-->
  <IssuedModal v-model:open="issuedOpen" :tableInstance="tableInstance"></IssuedModal>
  <!-- 导入-弹框 -->
  <ImportModal
    ref="ImportModalRef"
    :downloadTemplate="reqMaterialImportTemplate"
    :importFile="reqMaterialImport"
    @updateTable="updateData"></ImportModal>
  <!--单位配置-->
  <UnitConfig v-model:open="unitConfigOpen" :rowData="rowData"></UnitConfig>
</template>

<script setup lang="tsx">
  import IssuedModal from './components/IssuedModal.vue';
  import ImportModal from '@/components/ImportModal/index.vue';
  import UnitConfig from './components/UnitConfig.vue';
  import {
    getMaterialPageApi,
    getMaterialPrincipalListApi,
    postMaterialSaveApi,
    updateMaterialApi,
    getUnitListApi,
    getMaterialDetailApi,
    reqMaterialImportTemplate,
    reqMaterialImport,
    reqMaterialExport,
  } from '@/api/materialPlatform/materialInfo';
  import { fileStreamDownload } from '@bmos/utils';

  import { useTable, useTree } from './hooks';
  import { ref } from 'vue';
  import { message, Dropdown, Menu, MenuItem } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import { BMModalForm, BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { RenderCallbackParams } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();

  const {
    treeField,
    treeData,
    fetchTreeData,
    treeActionList,
    treeSelectedKeys,
    treeOpen,
    treeTitle,
    formProps,
    modalFormRef,
    okModal,
    handleTreeAction,
  } = useTree();

  fetchTreeData();

  const tableInstance = ref<any>();
  const addModalFormRef = ref<any>(null);
  const materialPrincipalList = ref([]);
  const unitList = ref([]);
  const categoryCode = ref<string>('');
  const queryParams = ref<any>(); //存查询过的参数

  const addModalFormProps = ref<any>({
    schemas: [
      {
        field: 'materialCategoryId',
        component: 'TreeSelect',
        label: t('分类'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            treeData: treeData.value[0].children,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            disabled: !!addModalFormProps.value.initialValues.id,
            onSelect: (value: any, node: any) => {
              materialPrincipalList.value = [];
              formModel.principalMaterialId = undefined;
              categoryCode.value = '';
              if (value) {
                getMaterialPrincipalList(value);
                categoryCode.value = node.code;
              }
            },
          };
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('编码'),
        required: true,
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('规格'),
        required: true,
      },
      {
        field: 'unitId',
        component: 'Select',
        label: t('单位'),
        required: true,
        componentProps: () => {
          return {
            options: unitList,
            fieldNames: {
              label: 'unitName',
              value: 'unitId',
            },
          };
        },
      },
      {
        field: 'subMaterial',
        component: 'RadioGroup',
        label: t('成员物料'),
        required: true,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('是'), value: true },
              { label: t('否'), value: false },
            ],
            onChange: (e: any) => {
              if (!e.target.value) {
                formModel.principalMaterialId = undefined;
              }
              formInstance.clearValidate(['principalMaterialId']);
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择成员物料'),
              validator: (rule: any, value: any) => {
                if (value === undefined) {
                  return Promise.reject(t('请选择成员物料'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'principalMaterialId',
        component: 'Select',
        label: t('所属物料'),
        required: ({ formModel }: any) => {
          return formModel.subMaterial;
        },
        componentProps: ({ formModel }: any) => {
          return {
            options: materialPrincipalList.value,
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            disabled: !formModel.subMaterial === true,
          };
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });
  const addOpen = ref<boolean>(false);
  const addModalFormTitle = ref<string>('');

  const issuedOpen = ref<boolean>(false);
  const ImportModalRef = ref<any>();

  // 查看编辑产品
  const watchEditMaterialInfo = async (record: any, isSee: boolean) => {
    try {
      const res = await getMaterialDetailApi({ id: record.id });
      getMaterialPrincipalList(res.data.materialCategoryId);
      addModalFormProps.value.initialValues = { ...res.data };
      addModalFormTitle.value = isSee ? t('查看物料信息') : t('编辑物料信息');
      addModalFormProps.value.disabled = isSee;
      addOpen.value = true;
    } catch (error: any) {
      message.error(error.message);
    }
    addOpen.value = true;
  };
  const { columns, unitConfigOpen, rowData, startStop } = useTable({
    props: {
      watchEditMaterialInfo,
    },
    tableInstance,
  });
  const tableFields = ref([
    {
      field: {
        id: 'id',
      },
    },
  ]);

  const getUnitList = async () => {
    try {
      const res = await getUnitListApi();
      unitList.value = res.data || [];
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 新增产品
  const addMaterialHandel = async (treeNode: any) => {
    if (treeNode?.id && treeNode?.id !== 'all') {
      getMaterialPrincipalList(treeNode.id);
      categoryCode.value = treeNode?.code;
    } else {
      categoryCode.value = '';
    }

    addModalFormProps.value.disabled = false;
    addModalFormProps.value.initialValues = {
      materialCategoryId: treeNode?.id && treeNode?.id !== 'all' ? treeNode.id : undefined,
    };
    addOpen.value = true;
    addModalFormTitle.value = t('新增物料信息');
  };
  const getMaterialPrincipalList = async (id: string) => {
    try {
      const res = await getMaterialPrincipalListApi({
        materialCategoryId: id,
      });
      (res.data || []).forEach((item: any) => (item.name = `${item.mergeCode}-${item.name}`));
      materialPrincipalList.value = res.data || [];
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const okAddModal = () => {
    addModalFormRef.value.validate().then(async (data: any) => {
      try {
        await (data.id ? updateMaterialApi : postMaterialSaveApi)({
          categoryCode: categoryCode.value,
          ...data,
        });
        addOpen.value = false;
        tableInstance.value?.fetchData();
        message.success(data.id ? t('修改物料成功') : t('新增物料成功'));
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  const updateData = () => {
    tableInstance.value?.fetchData();
  };
  const loadData: any = (params: any): Promise<any> => {
    if (params.materialCategoryId === 'all') {
      delete params.materialCategoryId;
    }
    queryParams.value = params;
    return getMaterialPageApi(params as any);
  };
  // 截取
  const getContentBetweenChars = (str: any) => {
    return decodeURI(str?.match(/filename=(\S*).xlsx/)[1]);
  };
  // 导出筛选数据/导出当前页数据
  const export1 = async (instance: any, type: any) => {
    const data = type === 'screen' ? instance.queryFormRef?.getFormValues() : queryParams.value;
    const data2 = { ...data, allFlay: type === 'screen' ? true : false };
    try {
      const res: any = await reqMaterialExport(data2);
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  getUnitList();
</script>

<style scoped></style>
