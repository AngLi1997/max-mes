<!-- 参数设置管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :treeData="treeData"
    :defaultSelectedNode="treeData[0]"
    :search="[false]"
    :paginations="[paginationBig]"
    :fieldNames="{
      title: 'menuName',
      key: 'id',
      children: 'childMenuList',
    }"
    :treeField="{
      field: {
        menuIdentify: 'menuIdentify',
      },
    }"
    :requests="[getConfigPage as any]"
    :columns="[globalColumns]"
    @tree-select="treeSelect">
    <template #tableHeaderToolbar0="{ treeNode }">
      <span>
        <RemarkModal v-model:modalOpen="receivingLibraryRemarkModalOpen" :details="receivingLibraryRemarkDetails" />
        <GlobalEditModal v-model:modalOpen="globalEditModalOpen" :rowData="firstRowData" @ok="updateTableData" />
        <ReceivingLibraryEditModal
          v-model:modalOpen="receivingLibraryAddEditModalOpen"
          :status="receivingLibraryOperationStatus"
          :rowData="firstRowData"
          :treeNode="treeNode"
          @ok="updateTableData" />
        <MaterialEditModal v-model:modalOpen="materialEditModalOpen" :rowData="firstRowData" @ok="updateTableData" />
        <SinglePlasmaStationEditModal
          v-model:modalOpen="singlePlasmaStationEditModalOpen"
          :rowData="firstRowData"
          @ok="updateTableData" />
        <RoundingRuleEditModal
          v-model:modalOpen="roundingRuleEditModalOpen"
          :rowData="firstRowData"
          @ok="updateTableData" />
        <RoundingParameterEditModal
          v-model:modalOpen="roundingParameterEditModalOpen"
          :rowData="firstRowData"
          @ok="updateTableData" />
      </span>
      <Button
        v-if="treeNode?.menuIdentify === MenuIdentifyEnum.RECEIVING_LIBRARY_SETTING"
        v-hasAuth="210080002000003"
        type="primary"
        @click="addReceivingLibrary">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, TableColumn } from '@bmos/components';
  import {
    useGlobalTable,
    useInspectionTable,
    useTree,
    useReceivingLibraryTable,
    useMaterialTable,
    useSinglePlasmaStationTable,
    useRoundingRuleTable,
    useRoundingParameterTable,
  } from './hooks';
  import { postStaticDataConfigPage, postStaticDataConfigStationPage } from '@/services';
  import { isEmpty } from '@bmos/utils';
  import { MenuIdentifyEnum } from './types';
  import GlobalEditModal from './components/GlobalEditModal.vue';
  import { paginationBig } from '@/utils';
  import ReceivingLibraryEditModal from './components/ReceivingLibraryEditModal.vue';
  import RemarkModal from '@/components/RemarkModal';
  import MaterialEditModal from './components/MaterialEditModal.vue';
  import SinglePlasmaStationEditModal from './components/SinglePlasmaStationEditModal.vue';
  import RoundingRuleEditModal from './components/RoundingRuleEditModal.vue';
  import RoundingParameterEditModal from './components/RoundingParameterEditModal.vue';
  import { useConfig } from '@/stores';

  defineOptions({
    name: 'ParameterSettings',
    inheritAttrs: false,
  });

  const { refreshConfig } = useConfig();

  const pageRef = ref<any>();
  // 第一个table 行数据
  const firstRowData = ref<any>({});

  const getConfigPage = async (params: any) => {
    const { menuIdentify }: any = params;
    if (isEmpty(menuIdentify)) {
      return [];
    }
    if (menuIdentify === MenuIdentifyEnum.SINGLE_PLASMA_STATION_SETTING) {
      return await postStaticDataConfigStationPage(params);
    }
    return await postStaticDataConfigPage(params);
  };

  const updateTableData = () => {
    refreshConfig();
    pageRef.value?.fetchData(0);
  };

  const { treeData } = useTree();
  const { globalColumns, globalEditModalOpen } = useGlobalTable({
    pageRef,
    firstRowData,
  });

  const { inspectionColumns } = useInspectionTable({
    pageRef,
    firstRowData,
    globalEditModalOpen,
  });

  const {
    receivingLibraryColumns,
    receivingLibraryAddEditModalOpen,
    receivingLibraryOperationStatus,
    receivingLibraryRemarkModalOpen,
    receivingLibraryRemarkDetails,
    addReceivingLibrary,
  } = useReceivingLibraryTable({
    pageRef,
    firstRowData,
    updateTableData,
  });

  const { materialColumns, materialEditModalOpen } = useMaterialTable({
    pageRef,
    firstRowData,
  });

  const { singlePlasmaStationColumns, singlePlasmaStationEditModalOpen } = useSinglePlasmaStationTable({
    pageRef,
    firstRowData,
  });

  const { roundingRuleColumns, roundingRuleEditModalOpen } = useRoundingRuleTable({
    pageRef,
    firstRowData,
  });

  const { roundingParameterColumns, roundingParameterEditModalOpen } = useRoundingParameterTable({
    pageRef,
    firstRowData,
  });
  const setTableColumns = (columns: TableColumn[]) => {
    pageRef.value?.getTableRef(0)?.replaceColumn(columns);
  };
  const treeSelect = (_node: any, info: any) => {
    switch (info?.node?.menuIdentify) {
      case MenuIdentifyEnum.GLOBAL_PARAMETER_SETTING:
        setTableColumns(globalColumns);
        break;
      case MenuIdentifyEnum.INSPECTION_PARAMETER_SETTING:
        setTableColumns(inspectionColumns);
        break;
      case MenuIdentifyEnum.RECEIVING_LIBRARY_SETTING:
        setTableColumns(receivingLibraryColumns);
        break;
      case MenuIdentifyEnum.MATERIAL_PARAMETER_SETTING:
        setTableColumns(materialColumns);
        break;
      case MenuIdentifyEnum.SINGLE_PLASMA_STATION_SETTING:
        setTableColumns(singlePlasmaStationColumns);
        break;
      case MenuIdentifyEnum.ROUNDING_RULE_SETTING:
        setTableColumns(roundingRuleColumns);
        break;
      case MenuIdentifyEnum.ROUNDING_PARAMETER_SETTING:
        setTableColumns(roundingParameterColumns);
        break;
      default:
        setTableColumns([]);
        break;
    }
  };
</script>
