import { FlowInstanceType } from '@/components/Flow/type';
import { Cell } from '@antv/x6';
import { Recordable, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isAsyncFunction, isFunction } from '@bmos/utils';

export type UseTableParams = {
  props: any;
  flowInstance: Ref<FlowInstanceType>;
};

export const useTable = ({ props, flowInstance }: UseTableParams) => {
  const tableInstance = ref<TableInstance>();

  const columns: TableColumn[] = [
    {
      title: t('节点名称'),
      dataIndex: 'elementName',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('处理人'),
      dataIndex: 'assigneeName',
      width: 190,
      resizable: true,
    },
    {
      title: t('处理行为'),
      dataIndex: 'stateName',
      width: 190,
      resizable: true,
    },
    {
      title: t('处理时间'),
      dataIndex: 'endTime',
      width: 190,
      resizable: true,
    },
    {
      title: t('审核意见'),
      dataIndex: 'comment',
      width: 190,
      resizable: true,
    },

    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 190,
      fixed: 'right',
      resizable: true,
    },
  ];

  const modalJson = ref<Recordable[]>([]);

  const allTableData = ref<Recordable[]>([]);
  const curTableData = ref<Recordable[]>([]);
  const curAuditNode = ref<Recordable>({});

  const getData = async () => {
    try {
      // 如果用户没有提供dataSource并且dataRequest是一个函数，那就进行接口请求
      if (
        Object.is(props.dataSource, undefined) &&
        (isFunction(props.dataRequest) || isAsyncFunction(props.dataRequest))
      ) {
        const { data } = await props?.dataRequest();
        modalJson.value = JSON.parse(data?.metaInfo || '[{}]').map(
          (item: any) => {
            const state = (data?.nodeStateList || []).find(
              (node: any) => node.elementKey === item.key,
            )?.state;
            return {
              ...item.metaInfo,
              data: {
                ...item.metaInfo.data,
                ...(state ? {
                  state: state,
                } : {state : 0}),
              },
            };
          },
        );
        // 4 审核通过 5 审核不通过 1 审核中
        allTableData.value = data?.nodeList;
        curAuditNode.value = (data?.nodeStateList || []).find(
          (node: any) => node.state === 1,
        );

        if (curAuditNode) {
          await nextTick();
          setCurTableDataByNodeId(curAuditNode.value.elementKey);
          flowInstance.value?.selectNodeById(curAuditNode.value.elementKey);
        }
        curTableData.value = data?.nodeList;
      }
    } catch (error) {}
  };

  const setCurTableDataByNodeId = (nodeId: string) => {
    if (nodeId) {
      curTableData.value = allTableData.value.filter(
        item => item.elementKey === nodeId,
      );
    } else {
      curTableData.value = [];
    }
  };

  const nodeClick = (cell: Cell) => {
    if (cell.id) {
      setCurTableDataByNodeId(cell.id);
    }
  };

  const graphIsReady = ref<boolean>(false);
  const graphRender = () => {
    graphIsReady.value = true;
  };

  watch(
    () => graphIsReady.value,
    () => {
      if (graphIsReady.value) {
        setTimeout(() => {
          curAuditNode.value?.elementKey &&
            flowInstance.value?.selectNodeById(curAuditNode.value.elementKey);
        }, 1000);
      }
    },
  );

  onMounted(async () => {
    getData();
  });

  return {
    tableInstance,
    columns,
    curTableData,
    modalJson,
    nodeClick,
    graphRender,
  };
};
