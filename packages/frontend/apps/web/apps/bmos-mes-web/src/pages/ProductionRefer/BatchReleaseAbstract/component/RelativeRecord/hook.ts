import { StyleEnum } from '@/components/Record';
import { reqDatasetQueryDatasetDetail, reqProcessVersionRecordOrderReq, reqRecordListComponentReq } from '@/services';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { message } from 'ant-design-vue';

export const useEDITOR = (props: any) => {
  const recordRef = ref();
  const templateActiveKeys = ref<string[]>([]);
  const datasetPointListIds = ref<string[]>([]);
  const datasetPointListData = ref();
  const datasetPointId = ref('');
  const EXPANDED_KEYS = ref<string[]>([]);
  const SELECTED_KEYS = ref<string[]>([]);
  const treeData = ref<any[]>([]);
  const isOnce = ref(true);
  const TREE_SELECT = async (_value: any, { node }: any) => {
    if (!recordRef.value) return;
    const { data } = await reqRecordListComponentReq({
      itemId: node.recordItemId,
      recordVersionId: node.recordVersionId,
    });
    // 回显记录项
    recordRef.value?.setContentByConfig(data);
    // 回显配置数据集样式
    const res = await reqDatasetQueryDatasetDetail({ id: node.datasetPointId });
    datasetPointId.value = node.datasetPointId;
    datasetPointListData.value = res.data;
    if (datasetPointListData.value) {
      setTimeout(() => {
        datasetPointListIds.value = datasetPointListData.value.datasetPointList.map((item: any) => {
          return item.fieldId;
        });
        recordRef.value?.setNodesStyle([...new Set(datasetPointListIds.value)], StyleEnum.dataBind);
      }, 500);
    }
  };
  const onLoadData = async (treeNode: any) => {
    return new Promise(async (resolve: (value?: unknown) => void) => {
      if (treeNode.dataRef.children) {
        resolve();
      }
      const { data } = await reqProcessVersionRecordOrderReq(
        props.processData?.processId,
        props.processData?.activeVersion,
      );
      treeNode.dataRef.children = data.map((item: any) => {
        item.isLeaf = true;
        item.name = item.recordItemName;
        item.datasetPointId = treeNode.id;
        return item;
      });
      if (isOnce.value) {
        SELECTED_KEYS.value = [treeNode.dataRef.children[0].id];
        TREE_SELECT('', { node: treeNode.dataRef.children[0] });
        isOnce.value = false;
      }
      resolve();
    });
  };

  // 点击组件
  const templateNodeClick = debounce((target: any, key: string) => {
    if (!key) return;
    if (key.indexOf('undefined') >= 0) return;
    if (datasetPointListIds.value.indexOf(key) < 0) {
      message.error(t('组件未配置数据点'));
      return;
    }
    templateActiveKeys.value = [key];
  }, 50);
  const getClickNodeData = () => {
    if (!templateActiveKeys.value[0]) {
      return null;
    }
    const data = datasetPointListData.value?.datasetPointList.find((item: any) => {
      return item.fieldId === templateActiveKeys.value[0];
    });
    return {
      ...data,
      datasetPointId: datasetPointId.value, //数据集id
    };
  };

  return {
    TREE_SELECT,
    onLoadData,
    recordRef,
    templateActiveKeys,
    templateNodeClick,
    getClickNodeData,
    EXPANDED_KEYS,
    SELECTED_KEYS,
    treeData,
  };
};
