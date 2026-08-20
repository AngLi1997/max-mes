<template>
  <!-- 新增编辑页面 -->
  <div class="addManage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('物料追溯配置') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t(titleName[type]) }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button v-if="type !== 'look'" type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <!-- 表单 -->
      <div class="setting">
        <BMForm ref="myFormRef" v-bind="topFormProps"></BMForm>
      </div>
      <div class="content content_table">
        <div class="tree_box">
          <BMSearchTree
            v-if="showTree"
            ref="searchTreeRef"
            :expandedKeys="null"
            :showAllAddIcon="type !== 'look'"
            :showAction="type !== 'look'"
            :selectedKeys="selectedKeys"
            :defaultExpandAll="defaultExpandAll"
            :treeData="treeData"
            :fieldNames="{
              title: 'showName',
              key: 'uniqueKey',
            }"
            :action-list="treeActionList"
            @select="select"
            @action="handleTreeAction"></BMSearchTree>
        </div>
        <div v-if="selectedKeys.length && productType !== '根节点'" class="msg_box">
          <RightTable
            ref="RightTableRef"
            v-model:tableList="tableData"
            :type="type"
            :productType="productType"
            :processList="processList"></RightTable>
        </div>
        <Empty v-else class="msg_box" :emptyName="t('请选择具体产品或物料')" />
      </div>
      <!-- </div> -->
      <!-- 树新增编辑弹框 -->
      <BMModalForm
        ref="modalFormRef"
        v-model:open="treeOpen"
        :title="treeTitle"
        :formProps="formProps"
        wrapClassName="modalSizeMedium"
        @okModal="okModal"></BMModalForm>
    </BreadcrumbButton>
  </div>
</template>
<script lang="tsx" setup>
  import { Breadcrumb, BreadcrumbItem, Button, Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import {
    reqProductMaterialProductTreeReq,
    getPlanProcessList,
    getProcessRecursionRelationProcesses,
    reqMaterialTraceTemplateCreate,
    reqMaterialTraceTemplateEdit,
    reqMaterialTraceTemplateQueryDetail,
  } from '@/services';
  import { BMForm, BMSearchTree, ActionListItem, BMModalForm } from '@bmos/components';
  import { loopTree, findNodeByValue, flatTree, sortTreeNodes } from '../utils';
  import { cloneDeep } from '@bmos/utils';
  import ScopeNumber from './ScopeNumber.vue';
  import Empty from '@/components/Empty/index.vue';

  import { createVNode } from 'vue';
  import { t } from '@bmos/i18n';
  import RightTable from './RightTable.vue';
  const emit = defineEmits(['back']);
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    type: {
      type: String,
      default: () => '',
    },
    treeNodeId: {
      type: String,
      default: () => '',
    },
    productTree: {
      type: Array,
      default: () => [],
    },
  });
  const titleName: any = {
    add: t('新增模板'),
    edit: t('编辑模板'),
    look: t('查看模板'),
    copy: t('复制模板'),
  };
  const defaultExpandAll = ref<any>(true);
  const showTree = ref<any>(false);

  const myFormRef = ref<any>(); //最上方表单ref
  const modalFormRef = ref<any>(); //树弹框ref
  const productType = ref<any>(); // 可为产品（根节点）（只可配产出信息）、中间品的物料节点（可配消耗信息和产出信息）、原辅包的物料节点（只可配消耗信息）
  const selectedKeys = ref<any[]>([]);
  const treeNode = ref<any>(); //新增编辑弹框时存当前点击的树节点id
  const tableData = ref<any>(); //存右边表格的数据
  const RightTableRef = ref<any>();
  const tableIds = ref<any>(); //存详情接口里所有表格id集合
  const materialInfo = ref<any>(); //存物料类型下的物料信息
  const treeData = ref<any>([
    {
      uniqueKey: '',
      showName: '',
      productType: '根节点', //此处只是一个标识 不需要加国际化
    },
  ]);
  const treeOpen = ref<boolean>(false);
  const treeTitle = ref<string>(t('新增物料'));
  const processList = ref<any>(); //生产工艺改变时查下方表格的工艺列表
  // 上方表单
  const topFormProps = reactive<any>({
    initialValues: {},
    disabled: props.type === 'look',
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 3,
    alwaysShowLines: 3,
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'templateName',
        label: t('模板名称'),
        component: 'Input',
        required: true,
      },
      {
        field: 'productId',
        label: t('产品信息'),
        component: 'TreeSelect',
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            showSearch: true,
            treeNodeFilterProp: 'showName', //搜索相关
            placeholder: t('请选择产品'),
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            request: async () => {
              const { data } = await reqProductMaterialProductTreeReq();
              return loopTree(data) || [];
            },
            onChange: (value: any) => {
              // 选择产品信息后回显可选择的生产工艺
              // formInstance.clearValidate();
              if (value) {
                getProcessList(value);
                formModel.processId = undefined;
              } else {
                //清空产品名称下拉时
                formModel.processId = undefined;
                myFormRef.value?.updateSchema({
                  field: 'processId',
                  componentProps: {
                    options: [],
                  },
                });
              }
              // 产品字段切换或清空，追溯树需清空
              treeData.value = [
                {
                  uniqueKey: '',
                  showName: '',
                  productType: '根节点',
                },
              ];
              formProps.schemas[0].componentProps.treeData = treeData.value; //更新弹框里的树数据
              productType.value = '根节点';
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('生产工艺'),
        required: true,
        componentProps: () => {
          return {
            options: [],
            onChange: async (val: any, option: any) => {
              const temp = {
                uniqueKey: val,
                showName: option.name || '',
              };
              treeData.value[0].uniqueKey = temp.uniqueKey;
              treeData.value[0].showName = temp.showName;
              //更新根节点下一级的所有节点的parentId
              if (treeData.value[0].children?.length > 0) {
                treeData.value[0].children.forEach((item: any) => {
                  item.parentId = val;
                });
              }
              const { data } = await getProcessRecursionRelationProcesses({ processId: val });
              processList.value = data;
            },
          };
        },
      },
    ],
  });

  // 前端编辑表格
  const editTable = (data: any, editData: any) => {
    data?.forEach((item: any) => {
      if (item.uniqueKey == treeNode.value) {
        item.procedureStepDTOList = editData;
      } else {
      }
      if (item.children) {
        editTable(item.children, editData);
      }
    });
  };
  watch(
    () => tableData.value,
    val => {
      editTable(treeData.value, val);
    },
    {
      deep: true,
    },
  );
  // 新增编辑弹窗
  const formProps = reactive<any>({
    initialValues: {
      // parentId: '',
    },
    schemas: [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级节点'),
        required: true,
        componentProps: {
          disabled: true,
          fieldNames: {
            label: 'showName',
            value: 'uniqueKey',
          },
          treeData: treeData.value,
        },
      },

      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品信息'),
        required: true,
        vIf: () => (treeTitle.value == t('编辑产品') || treeTitle.value == t('新增产品') ? true : false),
        componentProps: {
          disabled: true,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          treeData: loopTree(cloneDeep(props.productTree)),
        },
      },
      {
        field: 'materialType',
        component: 'Select',
        label: t('物料类型'),
        required: true,
        vIf: () => (treeTitle.value == t('编辑产品') || treeTitle.value == t('新增产品') ? false : true),

        componentProps: ({ formModel }: any) => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
            onChange: (value: number) => {
              formModel.materialId = undefined;
              if (value === undefined) {
                modalFormRef.value?.formRef?.updateSchema({
                  field: 'materialId',
                  componentProps: {
                    treeData: [],
                  },
                });
              } else {
                setMaterialOptions(value);
              }
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'TreeSelect',
        label: t('物料信息'),
        required: true,
        vIf: () => (treeTitle.value == t('编辑产品') || treeTitle.value == t('新增产品') ? false : true),
        componentProps: ({ formModel }: any) => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: string) => {
              const temp = findNodeByValue(materialInfo.value, value);
              formModel.materialName = temp?.name;
              formModel.mergeCode = temp?.mergeCode;
            },
          };
        },
      },
      {
        field: 'showPercentYield',
        component: 'RadioGroup',
        label: t('物料平衡'),
        required: true,
        defaultValue: true,
        componentProps: {
          options: [
            {
              label: t('展示'),
              value: true,
            },
            {
              label: t('不展示'),
              value: false,
            },
          ],
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择物料平衡'),
            },
          ];
        },
      },
      {
        field: 'percentYieldRange',
        defaultValue: {
          lowerValue: null,
          lowerSymbol: undefined,
          upperSymbol: undefined,
          upperValue: null,
        },
        component: ({ formModel }: any) => {
          return (
            <ScopeNumber
              v-model:limit={formModel['percentYieldRange']}
              onUpdate:limit={(val: any) => {
                formModel['percentYieldRange'] = val;
              }}
            />
          );
        },
        label: t('平衡范围'),
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: false,
              validator: async () => {
                let lowerValue = formModel['percentYieldRange']?.['lowerValue'];
                let upperValue = formModel['percentYieldRange']?.['upperValue'];
                const reg = /^-?\d{1,15}(\.\d{1,15})?$/;
                if (!(lowerValue === null || lowerValue === undefined)) {
                  if (!reg.test(lowerValue)) {
                    return Promise.reject(t('最小值整数或小数不能超过15位'));
                  }
                }

                if (!(upperValue === null || upperValue === undefined)) {
                  if (!reg.test(upperValue)) {
                    return Promise.reject(t('最大值整数或小数不能超过15位'));
                  }
                }
                if (lowerValue === null) return Promise.resolve();
                if (lowerValue === undefined) return Promise.resolve();
                if (upperValue === null) return Promise.resolve();
                if (upperValue === undefined) return Promise.resolve();
                if (Number(lowerValue) > Number(upperValue)) {
                  return Promise.reject(t('最小值不能大于最大值'));
                }
                // 如果限制方式为范围限制(开区间)，则最小值和最大值不能相等
                if (Number(lowerValue) === Number(upperValue)) {
                  return Promise.reject(t('最大值需大于最小值'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'calcFlag',
        component: 'RadioGroup',
        label: t('平衡计算'),
        required: true,
        defaultValue: true,
        vIf: () => (treeTitle.value == t('编辑产品') || treeTitle.value == t('新增产品') ? false : true),

        componentProps: {
          options: [
            {
              label: t('参与'),
              value: true,
            },
            {
              label: t('不参与'),
              value: false,
            },
          ],
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择平衡计算'),
            },
          ];
        },
      },
    ],
  });
  // 返回
  const back = () => {
    emit('back');
  };
  // 递归函数，遍历树节点，将每个节点的procedureStepDTOList添加到数组中
  const addProcedureStepDTOLists = (data: any) => {
    let array: any = [];
    data?.forEach((item: any) => {
      array.push(item.procedureStepDTOList);
      if (item.children) {
        array = array.concat(addProcedureStepDTOLists(item.children));
      }
    });
    return array?.flat();
  };

  //保存
  const save = async () => {
    const res: any = await myFormRef.value?.validate();
    try {
      let flag = true; //校验每个节点的消耗信息或产出信息下不能有相同的配置
      const data = {
        ...res,
        materialDTOTree: treeData.value[0]?.children,
      };
      if (!data.materialDTOTree || data.materialDTOTree?.length === 0) return message.error(t('请添加产品或物料'));
      flatTree(data.materialDTOTree)?.forEach((item: any) => {
        const procedureStepIdAndType = item?.procedureStepDTOList
          ?.map((item2: any) => item2?.procedureStepId && item2?.procedureStepId + '-' + item2?.traceType)
          ?.filter((item3: any) => item3);
        if (procedureStepIdAndType && new Set(procedureStepIdAndType).size !== procedureStepIdAndType?.length) {
          flag = false;
          return message.error(item.mergeCode + '-' + item.materialName + '的产出信息或消耗信息配置重复');
        }
      });
      if (!flag) return;
      if (props.type === 'add' || props.type === 'copy') {
        await reqMaterialTraceTemplateCreate({ ...data, copy: props.type === 'copy' ? true : undefined });
      }
      if (props.type === 'edit') {
        const temp = addProcedureStepDTOLists(treeData.value)?.map((item: any) => item?.id);
        const stepRemoveIdList = tableIds.value?.filter((item: any) => !temp.includes(item));
        await reqMaterialTraceTemplateEdit({ ...data, id: props.rowData?.id, stepRemoveIdList });
      }
      message.success(t('操作成功'));
      emit('back');
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 产品下拉改变时获取生产工艺下拉列表
  const getProcessList = async (val: any) => {
    const data = { productId: val, active: true };
    const res: any = await getPlanProcessList(data);
    const options = res.data.map((item: any) => {
      return {
        ...item,
        label: item.name,
        value: item.id,
      };
    });
    myFormRef.value?.updateSchema({
      field: 'processId',
      componentProps: {
        options,
      },
    });
  };
  // 物料类型改变时候查物料名称
  const setMaterialOptions = async (value: number) => {
    try {
      const { data } = await reqProductMaterialProductTreeReq(value);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: loopTree(data) || [],
        },
      });
      materialInfo.value = data;
    } catch (error) {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: [],
        },
      });
    }
  };
  // 树操作列表
  const treeActionList: any[] = [
    {
      title: t('新增物料'),
      action: 'addChildren',
    },
    // 根节点(工艺)可以新增产品和新增物料
    {
      title: t('新增产品'),
      action: 'addProduct',
      ifShow: (node: any) => {
        return node.nodeLevelInTree == 1; //1为根节点 工艺
      },
    },
    {
      title: t('编辑产品'),
      action: 'editProduct',
      ifShow: (node: any) => {
        return node.materialType == 2;
      },
    },
    {
      title: t('删除产品'),
      action: 'deleteNode',
      ifShow: (node: any) => {
        return node.nodeLevelInTree > 1 && node.materialType == 2;
      },
    },
    {
      title: t('编辑物料'),
      action: 'editNode',
      ifShow: (node: any) => {
        return node.nodeLevelInTree > 1 && node.materialType !== 2;
      },
    },
    {
      title: t('删除物料'),
      action: 'deleteNode',
      ifShow: (node: any) => {
        return node.nodeLevelInTree > 1 && node.materialType !== 2;
      },
    },
  ];
  // 点整条数据节点
  const select = async (
    selected_Keys: any[],
    info: {
      event: 'select';
      selected: boolean;
      node: any;
      selectedNodes: any;
    },
  ) => {
    if (selected_Keys.length === 0) return;
    if (selectedKeys.value[0] === selected_Keys[0]) {
      return;
    }
    selectedKeys.value = selected_Keys;
    productType.value = info.node.productType === '根节点' ? '根节点' : info.node?.materialType;
    treeNode.value = info.node.uniqueKey;
    tableData.value = info.node?.procedureStepDTOList || [];
  };
  // 新增子分类、编辑分类、删除分类
  const handleTreeAction = (action: ActionListItem, node: any) => {
    if (action.action === 'addChildren') {
      addItem(node);
    }
    if (action.action === 'addProduct') {
      addProduct(node);
    }
    if (action.action === 'editProduct') {
      editProduct(node);
    }
    if (action.action === 'editNode') {
      editNodeFn(node);
    }
    if (action.action === 'deleteNode') {
      deleteNodeFn(node);
    }
  };
  // 新增树节点
  const addItem = async (node: any) => {
    if (node.productType === '根节点') {
      await myFormRef.value?.validate();
    }
    treeTitle.value = t('新增物料');
    formProps.initialValues = {
      parentId: node?.uniqueKey,
    };
    formProps.schemas[1].componentProps.disabled = false;
    formProps.schemas[2].componentProps.disabled = false;
    formProps.schemas[0].label = t('上级节点');

    treeOpen.value = true;
    treeNode.value = node.uniqueKey;
  };
  // 新增产品
  const addProduct = async (node: any) => {
    treeTitle.value = t('新增产品');
    const res: any = await myFormRef.value?.validate();
    formProps.initialValues = {
      parentId: node?.uniqueKey,
      productId: res.productId, //回显上方表单所选的产品
      materialType: 2,
      materialId: res.productId,
      mergeCode: findNodeByValue(props.productTree, res.productId).mergeCode,
      materialName: findNodeByValue(props.productTree, res.productId).name,
      showPercentYield: true,
    };
    formProps.schemas[0].label = t('上级节点');
    formProps.schemas[1].componentProps.disabled = true;

    treeOpen.value = true;
    treeNode.value = node.uniqueKey;
  };

  // 编辑产品
  const editProduct = async (node: any) => {
    treeTitle.value = t('编辑产品');
    formProps.initialValues = {
      parentId: node?.parentId,
      productId: node?.productId,
      materialType: node?.materialType,
      materialId: node?.materialId,
      mergeCode: node?.mergeCode,
      materialName: node?.materialName,
      showPercentYield: node.showPercentYield,
      percentYieldRange: node.percentYieldRange,
    };
    formProps.schemas[0].label = t('上级节点');
    formProps.schemas[1].componentProps.disabled = true;
    treeOpen.value = true;
    treeNode.value = node.uniqueKey;
  };

  // 编辑树节点
  const editNodeFn = async (node: any) => {
    formProps.schemas[0].label = t('上级节点');
    treeOpen.value = true;
    await setMaterialOptions(node.materialType);
    treeTitle.value = t('编辑物料');
    formProps.initialValues = {
      //回显
      parentId: node.parentId === '0' ? 'all' : node.parentId,
      // id: node.id,
      name: node.name,
      code: node.code,
      materialType: node.materialType,
      materialId: node.materialId,
      mergeCode: node?.mergeCode,
      materialName: node?.materialName,
      showPercentYield: node.showPercentYield,
      percentYieldRange: node.percentYieldRange,
      calcFlag: node.calcFlag,
    };
    formProps.schemas[0].label = t('上级节点');
    treeOpen.value = true;
    treeNode.value = node.uniqueKey;
  };
  // 删除树节点
  const deleteNodeFn = (node: any) => {
    treeNode.value = node.dataRef.parentId;
    const current = node.uniqueKey;
    Modal.confirm({
      title: node.materialType === 2 ? t('是否删除该产品节点') : t('是否删除该物料节点'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content:
        node.materialType === 2
          ? t('产品节点及子节点删除后无法恢复，是否删除？')
          : t('物料节点及子节点删除后无法恢复，是否删除？'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          deleteTreeNode(treeData.value, current);
          message.success(t('删除成功'));
          if (node.selected) {
            selectedKeys.value = [treeData.value[0].uniqueKey];
            productType.value = '根节点';
            // tableData.value = treeData.value[0]?.procedureStepDTOList || [];
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  // 前端新增树
  const addTreeNode = (data: any, addData: any) => {
    data?.forEach((item: any) => {
      if (item.uniqueKey == treeNode.value) {
        if (item.children && item.children.length > 0) {
          item.children?.push(addData);
        } else {
          item.children = [addData];
        }
      } else {
      }
      if (item.children) {
        addTreeNode(item.children, addData);
      }
    });
  };
  // 前端编辑树
  const editTreeNode = (data: any, editData: any) => {
    data?.forEach((item: any) => {
      if (item.uniqueKey == treeNode.value) {
        item.materialType = editData?.materialType;
        item.materialId = editData?.materialId;
        item.materialName = editData?.materialName;
        item.mergeCode = editData?.mergeCode;
        item.showName = editData?.showName || editData?.mergeCode + '-' + editData?.materialName;
        item.showPercentYield = editData?.showPercentYield;
        item.percentYieldRange = editData?.percentYieldRange;
        item.calcFlag = editData?.calcFlag;
      } else {
      }
      if (item.children) {
        editTreeNode(item.children, editData);
      }
    });
  };

  // 前端删除树
  const deleteTreeNode = (data: any, current: any) => {
    data?.forEach((item: any) => {
      if (item.uniqueKey == treeNode.value) {
        item.children = item.children.filter((item2: any) => item2?.uniqueKey !== current);
      } else {
      }
      if (item.children) {
        deleteTreeNode(item.children, current);
      }
    });
  };

  // 弹框确定
  const okModal = (instance: any) => {
    instance.validate().then(async (params: any) => {
      const data: any = { ...params };
      data.parentId = data.parentId === 'all' ? '0' : data.parentId;
      if (treeTitle.value === t('新增物料') || treeTitle.value === t('新增产品')) {
        addTreeNode(treeData.value, {
          uniqueKey: new Date().getTime().toString(),
          showName: data.mergeCode + '-' + data.materialName,
          ...data,
        });
      }
      if (treeTitle.value === t('编辑物料')) {
        // 编辑物料保存
        editTreeNode(treeData.value, { showName: data.mergeCode + '-' + data.materialName, ...data });
      }
      if (treeTitle.value === t('编辑产品')) {
        // 编辑产品保存
        editTreeNode(treeData.value, { ...data, uniqueKey: treeNode.value });
      }
      // 处理同层级升序排序
      treeData.value = sortTreeNodes(treeData.value);
      message.success(t('操作成功'));
      treeOpen.value = false;
    });
  };
  // 编辑页面把总表格数据查询添加到每个节点中
  const updateNodeByTable = (data: any, allTableData: any) => {
    data?.forEach((item: any) => {
      item.uniqueKey = item.id;
      item.showName = item.mergeCode ? item.mergeCode + '-' + item.materialName : item.materialName;
      const temp = allTableData?.find((item2: any) => item2.relationId === item.id);
      temp?.consumeStepList?.forEach((item3: any) => {
        item3.traceType = 1;
      });
      temp?.outputStepList?.forEach((item4: any) => {
        item4.traceType = 2;
      });
      const temp2 = temp?.consumeStepList.concat(temp?.outputStepList);
      item.procedureStepDTOList = temp2 || [];
      if (item.children) {
        updateNodeByTable(item.children, allTableData);
      }
    });
  };
  // 详情
  const getDetails = async () => {
    const { data } = await reqMaterialTraceTemplateQueryDetail({ id: props.rowData?.id });
    await getProcessList(data?.productId);
    const { data: processList1 } = await getProcessRecursionRelationProcesses({ processId: data.processId });
    processList.value = processList1;
    myFormRef.value?.setFieldsValue({
      templateName: data?.templateName,
      productId: data?.productId,
      processId: data?.processId,
    });
    treeData.value = [
      {
        id: data.processId,
        materialName: data.processName,
        showName: data.processName,
        productType: '根节点',
        children: data?.materialTree.map((item: any) => {
          return {
            ...item,
            parentId: data.processId, //给第一层加parentId
            productId: item.materialType === 2 ? data.productId : '',
          };
        }),
      },
    ];
    const allTableData = data.procedureStepDataList; //所有表格的数据
    updateNodeByTable(treeData.value, allTableData);
    treeData.value[0].parentId = data?.materialTree[0].id; //产品只有编辑产品 所以parentId用它的id
    formProps.schemas[0].componentProps.treeData = treeData.value;
    // 存所有表格id集合
    tableIds.value = data.procedureStepDataList
      .map((item: any) => item?.consumeStepList.concat(item?.outputStepList))
      ?.flat()
      ?.map((item2: any) => item2.id);
    defaultExpandAll.value = true;
    // 查看编辑进来时默认选中第一级
    selectedKeys.value = [treeData.value[0].uniqueKey];
    productType.value = '根节点';
    treeNode.value = treeData.value[0].uniqueKey;
    tableData.value = [];
    showTree.value = true;
  };

  onMounted(async () => {
    switch (props.type) {
      case 'add':
        myFormRef.value?.setFieldsValue({});
        if (props.treeNodeId) {
          myFormRef.value?.setFormModels({ productId: props.treeNodeId });
          await getProcessList(props.treeNodeId);
        }
        showTree.value = true;

        break;

      case 'edit':
        getDetails();
        break;
      case 'copy':
        getDetails();
        break;
      case 'look':
        getDetails();
        break;
    }
  });
</script>
<style lang="less" scoped>
  .addManage {
    width: 100%;
    height: 100%;
  }
  .setting {
    border-bottom: 1px solid var(--bmos-second-level-border-color);
  }
  .content {
    height: calc(100% - 48px - 22px);
    background-color: #fff;
  }
  .content_table {
    width: 100%;
    display: flex;
    justify-content: space-between;
    :deep(.mes-tabs-nav) {
      margin: 0;
    }
    .tree_box {
      width: 36%;
      min-width: 265px;
      box-sizing: border-box;
      border-right: 1px solid rgb(225, 227, 229, 1);
    }
    .msg_box {
      width: 64%;
      padding: 10px 0px 0px 16px;
    }
  }
  .delimiter {
    width: 10%;
    text-align: center;
    line-height: 34px;
    background-color: var(--bmos-disable-color);
    border-top: 1px solid var(--bmos-first-level-border-color);
    border-bottom: 1px solid var(--bmos-first-level-border-color);
  }
</style>
