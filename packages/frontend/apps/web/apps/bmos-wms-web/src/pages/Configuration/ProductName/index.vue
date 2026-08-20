<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :fieldNames="{
      title: 'fullName',
      key: 'id',
    }"
    :treeField="treeField"
    :actionList="treeActionList"
    :selectedKeys="treeSelectedKeys"
    :rowKeys="['id', 'versionId']"
    :tableFields="tableFields"
    :defaultSelectedNode="treeData[0]"
    :requests="[getProductMaterialPage]"
    :columns="[columns]"
    :search="[true, false]"
    :formProps="[
      {
        showAdvancedButton: false,
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Button v-if="categoryType === 0" v-hasAuth="150010001000004" type="primary" @click="addMaterialHandel(treeNode)">
        {{ t('新增') }}
      </Button>
      <Button v-if="categoryType === 0" v-hasAuth="150010001000005" type="default" @click="synchronizeHandel">
        {{ t('同步') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="tableTitle"></BMTableTitle>
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
  <!--新增编辑弹框-->
  <AddMaterialModal v-model:open="addOpen" v-bind="addModalProps"></AddMaterialModal>
  <!--同步-->
  <SynchronizeModal
    v-model:open="open"
    :categoryType="props.categoryType"
    :tableInstance="tableInstance"
    :fetchTreeData="fetchTreeData"></SynchronizeModal>
</template>

<script setup lang="tsx">
  import AddMaterialModal from './components/AddMaterialModal/index.vue';
  import SynchronizeModal from './components/SynchronizeModal/index.vue';

  import { getCargoPageApi, getCargoDetailApi } from '@/services';
  import { useTable, useTree } from './hooks';
  import { ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMModalForm, BMPageComponent, BMTableTitle } from '@bmos/components';
  const props = defineProps({
    categoryType: {
      type: Number,
      default: 0,
    },
  });
  const tableTitle = computed(() => {
    return [t('货品列表'), t('中间品列表'), t('产品列表')][props.categoryType];
  });
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
  } = useTree(props);

  fetchTreeData();

  const tableInstance = ref<any>();
  const initialValues = ref({});
  const rowHandleId = ref<string>('');
  const isEdit = ref<boolean>(false);
  const addOpen = ref<boolean>(false);
  const open = ref<boolean>(false);
  const addModalProps = computed(() => {
    return {
      isEdit,
      id: rowHandleId,
      initialValues,
      tableInstance: tableInstance.value,
      categoryTreeData: treeData.value,
      categoryType: props.categoryType,
    };
  });
  // 构建单位和扩展单位回显ids
  const getUnitIds = (data: any) => {
    if (data.unitExtendId) {
      return [data.unitId, data.unitExtendId];
    }
    return [data.unitId, data.unitId];
  };
  // 查看编辑产品
  const watchEditMaterialInfo = async (record: any, edit: boolean) => {
    rowHandleId.value = record.id;
    isEdit.value = edit;
    try {
      const res = await getCargoDetailApi({ id: record.id });
      const unitIds = getUnitIds(res.data);
      initialValues.value = { ...res.data, unitIds, echoUnitId: unitIds[1] };
    } catch (error: any) {
      message.error(error.message);
    }
    addOpen.value = true;
  };
  const { columns } = useTable({
    props: {
      categoryType: props.categoryType,
      watchEditMaterialInfo,
    },
  });
  const tableFields = ref([
    {
      field: {
        id: 'id',
      },
    },
  ]);

  const getProductMaterialPage = (params: any) => {
    return getCargoPageApi({
      ...params,
      cargoCategoryId: params.cargoCategoryId === 'all' ? void 0 : params.cargoCategoryId,
    });
  };

  // 新增产品
  const addMaterialHandel = async treeNode => {
    rowHandleId.value = '';
    initialValues.value = {
      cargoCategoryId: treeNode.id === 'all' ? void 0 : treeNode.id,
      categoryCode: treeNode.code,
      isMember: false,
    };
    isEdit.value = false;
    addOpen.value = true;
  };
  // 同步
  const synchronizeHandel = () => {
    open.value = true;
  };
</script>

<style scoped></style>
