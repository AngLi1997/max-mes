import type { FormProps } from '@bmos/components';
import { DataNode } from 'ant-design-vue/es/tree';
export const useParams = () => {
  //tiber Ref
  const pageProcedures = ref(null);
  // 历史弹框
  const historyOpen = ref<boolean>(false);
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  //是否打开部门管理
  const permissionModalOpen = ref<boolean>(false);
  //是否打开弹窗
  const status = ref<boolean>(false);
  //是否打开启用弹框
  const flowStatus = ref<boolean>(false);
  //启用弹出框Name
  const flowName = ref(t('是否发起启用审核'));
  //类型
  const SubmitKey = ref<string>('');
  //树名称Name
  const modalTitle = ref(t('新增分类'));
  //版本选择的数据
  const isVersionId = ref<any>({});
  //控制节点树
  const treeAllField = reactive<{ selectedKeys: any[]; expandedKeys: any[] }>({
    selectedKeys: ['all'],
    expandedKeys: ['all'],
  });
  //设备管理树节点
  const treeData = ref<DataNode[]>([]);
  //tree 取值节点
  const fieldNames = {
    children: 'children',
    title: 'name',
    key: 'id',
  };
  //列表1请求id
  const treeField = reactive({
    field: {
      categoryId: 'id',
    },
  });
  //列表2请求
  const tableFields = reactive([
    {},
    {
      field: {
        parentId: 'id',
      },
    },
  ]);
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  });
  return {
    pageProcedures,
    historyOpen,
    flowName,
    flowStatus,
    permissionModalOpen,
    firstRowData,
    isVersionId,
    status,
    SubmitKey,
    modalTitle,
    treeData,
    treeField,
    tableFields,
    treeAllField,
    fieldNames,
    formFirstProps,
  };
};
