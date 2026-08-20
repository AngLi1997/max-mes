import { EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE } from '@/components/Record';
import { Recordable } from '@bmos/components';

export const useDynamicTable = ({ current_node, useNode, editor }: any) => {
  const { EDITOR_INSTANCE, IS_SHOW, RECORD_INSTANCE, NODE_ACTIVE_KEYS } = editor;
  const { ADD_EDA_DYNAMIC_TABLE_NODE, findNode, INST_NODE_LIST } = useNode;

  const equipmentDataAcquisitionDynamicTableOpen = ref<boolean>(false);

  const openEquipmentDataAcquisitionDynamicTableModal = (data: any) => {
    equipmentDataAcquisitionDynamicTableOpen.value = true;
    current_node.value = data.data;
  };

  const equipmentDataAcquisitionDynamicTableSubmit = (formValues: Recordable) => {
    const Node: any = {
      ...EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE.EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE,
      componentDetail: formValues,
    };
    ADD_EDA_DYNAMIC_TABLE_NODE(Node, current_node.value);
    equipmentDataAcquisitionDynamicTableOpen.value = false;
  };

  const addDynamicTableInEditor = (keys: any, data: any) => {
    try {
      if (!data.data || !data.data.componentDetail) {
        return;
      }
      const componentDetail = JSON.parse(data.data.componentDetail);
      const { rowNum, tableList, rowHeight } = componentDetail;
      if (!rowNum || !tableList) {
        return;
      }
      if (IS_SHOW.value && RECORD_INSTANCE.value) {
        NODE_ACTIVE_KEYS.value = keys;
        return;
      }
      const item: any = findNode(data.key!, INST_NODE_LIST.value);
      // 判断节点是否使用过
      if (item?.used) {
        editor.EDITOR_INSTANCE.value?.changeClickNodeList(data.key);
        NODE_ACTIVE_KEYS.value = keys;
        editor.EDITOR_INSTANCE.value.getNodeTop(data.key);
        return;
      }
      EDITOR_INSTANCE.value.addTable(data.key, {
        columns: tableList.length,
        rows: rowNum,
        rowHeight,
        headers: tableList.map((item: any) => {
          return {
            colName: item.colName,
            colWidth: item.colWidth,
          };
        }),
      });
      if (item) {
        item.used = true;
      }
      editor.EDITOR_INSTANCE.value?.changeClickNodeList(data.key);
      NODE_ACTIVE_KEYS.value = keys;
    } catch (error) {
      console.error(error);
    }
  };
  return {
    equipmentDataAcquisitionDynamicTableOpen,
    openEquipmentDataAcquisitionDynamicTableModal,
    equipmentDataAcquisitionDynamicTableSubmit,
    addDynamicTableInEditor,
  };
};
