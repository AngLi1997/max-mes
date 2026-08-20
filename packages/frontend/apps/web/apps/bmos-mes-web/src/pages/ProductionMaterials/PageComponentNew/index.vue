<template>
  <keep-alive>
    <BMPageComponent
      v-if="!addOpen"
      ref="tableInstance"
      :treeData="treeData"
      :fieldNames="{
        title: 'showName',
        key: 'id',
      }"
      :treeField="treeField"
      :actionList="treeActionList"
      :selectedKeys="treeSelectedKeys"
      :showAllAddIcon="treeAllPermission"
      :rowKeys="['id', 'versionId']"
      :tableFields="tableFields"
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
        <Button
          v-if="categoryType === 0"
          v-hasAuth="120010001000001"
          type="primary"
          @click="addMaterialHandel(treeNode)">
          {{ t('新增') }}
        </Button>
        <Button
          v-if="categoryType === 1"
          v-hasAuth="120010002000001"
          type="primary"
          @click="addMaterialHandel(treeNode)">
          {{ t('新增') }}
        </Button>
        <Button
          v-if="categoryType === 2"
          v-hasAuth="120010003000001"
          type="primary"
          @click="addMaterialHandel(treeNode)">
          {{ t('新增') }}
        </Button>
        <Button v-if="categoryType === 0" v-hasAuth="120010001000002" type="default" @click="synchronizeHandel">
          {{ t('同步') }}
        </Button>
        <Button v-if="categoryType === 1" v-hasAuth="120010002000002" type="default" @click="synchronizeHandel">
          {{ t('同步') }}
        </Button>
        <Button v-if="categoryType === 2" v-hasAuth="120010003000002" type="default" @click="synchronizeHandel">
          {{ t('同步') }}
        </Button>
      </template>
      <template #tableHeaderTitle0>
        <BMTableTitle :title="tableTitle"></BMTableTitle>
      </template>
    </BMPageComponent>
  </keep-alive>
  <!-- 树新增编辑弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeOpen"
    :title="treeTitle"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="okModal"></BMModalForm>
  <!--新增编辑物料-->
  <!-- <AddMaterialModal v-model:open="addOpen" v-bind="addModalProps"></AddMaterialModal> -->
  <!-- 新增编辑物料改为新页面 -->
  <AddMaterialPage v-if="addOpen" v-bind="addModalProps" @backAndSave="backAndSave"></AddMaterialPage>
  <!--同步-->
  <SynchronizeModal
    v-model:open="open"
    :categoryType="props.categoryType"
    :tableInstance="tableInstance"
    :fetchTreeData="fetchTreeData"></SynchronizeModal>
  <!--批量记录-->
  <BatchRecordModal
    v-model:open="openBatchRecord"
    :categoryType="props.categoryType"
    :record-id="recordId"></BatchRecordModal>
</template>

<script setup lang="tsx">
  import AddMaterialPage from './components/AddMaterialPage/index.vue';
  // import AddMaterialModal from './components/AddMaterialModal/index.vue';
  import SynchronizeModal from './components/SynchronizeModal/index.vue';
  import BatchRecordModal from './components/BatchRecordModal/index.vue';
  import { MaterialTypeMap } from './const';
  import { getProductMaterialPageApi, getProductMaterialDetailApi } from '@/services';
  import { useTable, useTree } from './hooks';
  import { ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMModalForm, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import { isObject } from '@bmos/utils';

  const props = defineProps<{
    categoryType: number;
  }>();

  const { hasPermission } = usePermissionStore();

  const treeAllPermission = computed(() => {
    switch (props.categoryType) {
      case MaterialTypeMap.RawMaterial:
        return hasPermission('120010001000007');
      case MaterialTypeMap.MiddleProduct:
        return hasPermission('120010002000007');
      case MaterialTypeMap.Product:
        return hasPermission('120010003000008');
      default:
        return hasPermission('120010001000007');
    }
  });

  const tableTitle = computed(() => {
    return [t('原辅包列表'), t('中间品列表'), t('产品列表')][props.categoryType];
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
  const initialValues = ref<any>({
    expandInfo: {},
    fieldSaveDTOList: [],
  });
  const rowHandleId = ref<string>('');
  const isEdit = ref<boolean>(false);
  const addOpen = ref<boolean>(false);
  const open = ref<boolean>(false);
  const recordId = ref<string>('');
  const openBatchRecord = ref<boolean>(false);
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
  const getUnitIds = data => {
    if (data.unitExtendId) {
      return [data.unitId, data.unitExtendId];
    }
    return [data.unitId, data.unitId];
  };
  const handleEnum = (data: any) => {
    return isObject(data) ? data?.value : data;
  };
  // 查看编辑产品
  const watchEditMaterialInfo = async (record: any, edit: boolean) => {
    rowHandleId.value = record.id;
    isEdit.value = edit;
    try {
      const res = await getProductMaterialDetailApi({ id: record.id });
      const unitIds = getUnitIds(res.data);
      //用于回显表单值
      initialValues.value = { ...res.data, unitIds, echoUnitId: unitIds[1] };
      initialValues.value.expandInfo = { ...res.data?.expandInfo, timeUnit: handleEnum(res.data.expandInfo?.timeUnit) };
    } catch (error: any) {
      message.error(error.message);
    }
    addOpen.value = true;
  };
  // 批记录绑定
  const bindingRecordHandel = (record: any) => {
    recordId.value = record.id;
    openBatchRecord.value = true;
  };
  const { columns } = useTable({
    props: {
      categoryType: props.categoryType,
      watchEditMaterialInfo,
      bindingRecordHandel,
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
    return getProductMaterialPageApi({
      ...params,
      categoryType: props.categoryType,
      materialCategoryId: params.materialCategoryId === 'all' ? void 0 : params.materialCategoryId,
    });
  };

  // 新增产品
  const addMaterialHandel = async (treeNode: any) => {
    rowHandleId.value = '';
    initialValues.value = {
      materialCategoryId: treeNode.id === 'all' ? void 0 : treeNode.id,
      categoryCode: treeNode.code,
      expandInfo: {},
      fieldSaveDTOList: [], //自定义字段表格
    };
    if (props.categoryType === 1) initialValues.value.expandInfo.timeUnit = 1;
    isEdit.value = false;
    addOpen.value = true;
  };
  // 同步
  const synchronizeHandel = () => {
    open.value = true;
  };

  // 0717改为页面
  const backAndSave = async () => {
    addOpen.value = false;
  };
</script>

<style scoped></style>
