import { BMModalForm, FormProps } from '@bmos/components';
import { t } from '@bmos/i18n';
import { debounce } from '@bmos/utils';
import { Modal, message } from 'ant-design-vue';
import type { AntTreeNodeDropEvent, TreeProps } from 'ant-design-vue/es/tree';
import { Ref, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {
  BATCH_QUANTITY_PICK_BATCH_SUMMARY,
  BUSINESS_FORMULA_INFO_MATERIAL,
  CLEAN_CHECK,
  CLEAN_IMPLEMENT,
  CUSTOM_FIELD_BUTTON,
  EQUIPMENT_DATA_ACQUISITION,
  EQUIPMENT_DATA_DRAW_LIST,
  EQUIPMENT_INFO,
  FEED_RECYCLE,
  INGREDIENTS_INPUT,
  INGREDIENTS_PLAN_MATERIAL,
  INSPECTION_RESULTS,
  LIQUID_PREPARATION_INPUT,
  LIQUID_PREPARATION_MEASURE,
  LIQUID_PREPARATION_OUTPUT,
  LIQUID_PREPARATION_PLAN,
  MATERIAL_INPUT,
  MATERIAL_QUANTITY_PICK_MATERIAL,
  MATERIAL_RESERVE,
  OUTPUT_WEIGHING_CHILDREN,
  PICKING_RECEIVING_CHILDREN,
  PRODUCT_OUTPUT_CHILDREN,
  WEIGHING_DATA,
  WEIGHING_INGREDIENTS_CHILDREN,
} from '../../../components/Record/NodeList/enum';
import {
  recordCopyRecordItem,
  recordDeleteRecordItem,
  recordItemChangeName,
  recordItemChangeSort,
  recordItemDetail,
  recordListComponent,
} from '../../../services';
import { ACTION_TYPE } from '../enum';
export const useTree = (useEDITOR: any, useNode: any, saveTemplate: Function, NODE_ID: Ref<string[]>) => {
  const route = useRoute();
  const { INIT_CONTENT, IS_SHOW, NODE_ACTIVE_KEYS, spinClink } = useEDITOR;
  const { INST_NODE_LIST, SET_INST_NODE_LIST, ADD_NODE, NODE_CLICK } = useNode;
  const EXPANDED_KEYS = ref<string[]>([]);
  const SELECTED_KEYS = ref<string[]>([]);
  const CURRENT_NODE = ref<any>();
  const recordVersionId = route.params.record_id as any;
  const RecordData = reactive<Record<string, any>>({
    itemList: [],
    footerAndHeaderList: [],
    recordName: '',
    treePosition: '',
  });
  const isRage = ref(false);
  const clickNode = ref<any>({});
  const TREE_DATA = ref<any>([]);
  const searchMethod = ref();

  onMounted(async () => {
    await getTreeData();
    INIT_TREE_DATA();
  });

  const getTreeData = async () => {
    const { data } = await recordItemDetail({ recordVersionId });
    TREE_DATA.value = [
      {
        name: data.recordName,
        itemId: route.params.record_id,
        notShowMoreBtn: true,
        selectable: false,
        children: [...data.itemList],
      },
    ];
    if (SELECTED_KEYS.value.length == 0) {
      SELECTED_KEYS.value = [TREE_DATA.value[0].children[0].itemId];
    }
  };

  const initialValues = ref<FormProps['initialValues']>({ name: '' })!;
  const schemas: FormProps['schemas'] = [
    {
      field: 'name',
      component: 'Input',
      label: t('记录项名称'),
      required: true,
    },
  ];

  const SaveRecordItem = async (val: any, node: any) => {
    try {
      const res = await recordItemChangeName({
        id: node.id,
        name: val.name,
      });
      if (res.code === 0) {
        node.dataRef.name = val.name;
        // await saveTemplate();
        // INIT_TREE_DATA();
        await getTreeData();
        return Promise.resolve(true);
      }
    } catch (error: any) {
      message.error(error.message);
      return Promise.reject(false);
    }
  };
  const CopyRecordItem = async (val: any, node: any) => {
    try {
      const res = await recordCopyRecordItem({
        itemName: val.name,
        itemId: node.itemId,
      });
      if (res.code === 0) {
        INIT_TREE_DATA();

        return Promise.resolve(true);
      }
    } catch (error: any) {
      message.error(error.message);
      return Promise.reject(false);
    }
  };

  const DeleteRecordItem = async (node: any) => {
    Modal.confirm({
      title: t('是否删除该记录项'),
      content: t('记录项删除后无法恢复，是否删除?'),
      async onOk() {
        try {
          await saveTemplate();
          const res = await recordDeleteRecordItem({ itemId: node.id });
          if (res.code === 0) {
            TREE_DATA.value[0].children = TREE_DATA.value[0].children.filter((item: any) => {
              return item.itemId != node.itemId;
            });
            if (node.itemId === CURRENT_NODE.value.itemId) {
              INIT_TREE_DATA();
            }

            return Promise.resolve(true);
          }
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject(false);
        }
      },
    });
  };

  const HandleSubmit = async (type: number, node: any, val?: any) => {
    switch (type) {
      case ACTION_TYPE.EDIT:
        await SaveRecordItem(val, node);
        return Promise.resolve();
      case ACTION_TYPE.COPY:
        return await CopyRecordItem(val, node);
      case ACTION_TYPE.DELETE:
        return await DeleteRecordItem(node);
      default:
        break;
    }
  };

  const ACTION_LIST = [
    {
      title: t('编辑记录项'),
      action: 'EDIT_RECORD',
      render: (data: any) => {
        return (
          <BMModalForm
            title={t('编辑记录项')}
            onRegister={() => {
              initialValues.value && (initialValues.value.name = data.name);
            }}
            preventDefault={false}
            submit={async (val: any) => await HandleSubmit(ACTION_TYPE.EDIT, data, val)}
            formProps={{
              initialValues: data.name ? { ...initialValues.value, name: data.name } : initialValues.value,
              schemas,
            }}>
            {{
              trigger: () => t('编辑记录项'),
            }}
          </BMModalForm>
        );
      },
    },
    // {
    //   title: t('复制记录项'),
    //   action: 'COPY_RECORD',
    //   render(data: any) {
    //     return (
    //       <BMModalForm
    //         onRegister={() =>
    //           initialValues.value && (initialValues.value.copy_name = data.name)
    //         }
    //         submit={async (val: any) =>
    //           await HandleSubmit(ACTION_TYPE.COPY, data, val)
    //         }
    //         formProps={{
    //           initialValues: initialValues.value,
    //           schemas: schemasCopy,
    //         }}
    //         title={t('复制记录项')}>
    //         {{
    //           trigger: () => t('复制记录项'),
    //         }}
    //       </BMModalForm>
    //     );
    //   },
    // },
    {
      title: t('删除记录项'),
      action: 'DELETE_RECORD',
      render(data: any) {
        return <span onClick={() => HandleSubmit(ACTION_TYPE.DELETE, data)}>{t('删除记录项')}</span>;
      },
    },
  ];

  /**
   * description: 业务组件初始化时添加按钮
   */
  const addButtonForBusinessComponent = (data: any) => {
    let flagIndex: number = -1;
    data.forEach((item: any) => {
      if (item.componentList && item.componentList.length > 0) {
        let index = -1;
        item.componentList.forEach((component: any) => {
          switch (component.componentType) {
            // 生产BOM信息
            case 'BUSINESS_FORMULA_INFO':
              component.children.push({
                ...BUSINESS_FORMULA_INFO_MATERIAL.BUSINESS_FORMULA_INFO_MATERIAL_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 物料量领料
            case 'MATERIAL_QUANTITY_PICK':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'MATERIAL_QUANTITY_PICK_MATERIAL',
              );
              component.children.splice(index + 1, 0, {
                ...MATERIAL_QUANTITY_PICK_MATERIAL.MATERIAL_QUANTITY_PICK_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...MATERIAL_QUANTITY_PICK_MATERIAL.ADD_MATERIAL_RECEIVE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 批次量领料
            case 'BATCH_QUANTITY_PICK':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'BATCH_QUANTITY_PICK_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...BATCH_QUANTITY_PICK_BATCH_SUMMARY.BATCH_QUANTITY_PICK_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'BATCH_QUANTITY_PICK_BATCH',
              );
              component.children.splice(index + 1, 0, {
                ...BATCH_QUANTITY_PICK_BATCH_SUMMARY.BATCH_QUANTITY_PICK_BATCH_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...BATCH_QUANTITY_PICK_BATCH_SUMMARY.ADD_BATCH_RECEIVE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 成品产出
            case 'PRODUCT_OUTPUT':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'PRODUCT_OUTPUT_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...PRODUCT_OUTPUT_CHILDREN.PRODUCT_OUTPUT_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'PRODUCT_OUTPUT_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...PRODUCT_OUTPUT_CHILDREN.PRODUCT_OUTPUT_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...PRODUCT_OUTPUT_CHILDREN.PRODUCT_ADD_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 产出称量
            case 'OUTPUT_WEIGHING':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'OUTPUT_WEIGHING_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...OUTPUT_WEIGHING_CHILDREN.OUTPUT_WEIGHING_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'OUTPUT_WEIGHING_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...OUTPUT_WEIGHING_CHILDREN.OUTPUT_WEIGHING_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...OUTPUT_WEIGHING_CHILDREN.OUTPUT_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 领料接收
            case 'PICKING_RECEIVING':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'PICKING_RECEIVING_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...PICKING_RECEIVING_CHILDREN.PICKING_RECEIVING_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'PICKING_RECEIVING_BATCH',
              );
              component.children.splice(index + 1, 0, {
                ...PICKING_RECEIVING_CHILDREN.PICKING_RECEIVING_BATCH_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...PICKING_RECEIVING_CHILDREN.ADD_PICKING_RECEIVE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配料计划
            case 'INGREDIENTS_PLAN':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'INGREDIENTS_PLAN_MATERIAL',
              );
              component.children.splice(index + 1, 0, {
                ...INGREDIENTS_PLAN_MATERIAL.INGREDIENTS_PLAN_MATERIAL_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...INGREDIENTS_PLAN_MATERIAL.ADD_INGREDIENTS_PLAN_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配料称量
            case 'WEIGHING_INGREDIENTS':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'WEIGHING_INGREDIENTS_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...WEIGHING_INGREDIENTS_CHILDREN.WEIGHING_INGREDIENTS_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'WEIGHING_INGREDIENTS_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...WEIGHING_INGREDIENTS_CHILDREN.WEIGHING_INGREDIENTS_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...WEIGHING_INGREDIENTS_CHILDREN.ADD_WEIGHING_INGREDIENTS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配料投入
            case 'INGREDIENTS_INPUT':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'INGREDIENTS_INPUT_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...INGREDIENTS_INPUT.INGREDIENTS_INPUT_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'INGREDIENTS_INPUT_FEEDING_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...INGREDIENTS_INPUT.INGREDIENTS_INPUT_FEEDING_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...INGREDIENTS_INPUT.ADD_INGREDIENTS_INPUT_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配料回收
            case 'FEED_RECYCLE':
              index = component.children.findLastIndex((child: any) => child.componentType === 'FEED_RECYCLE_SUMMARY');
              component.children.splice(index + 1, 0, {
                ...FEED_RECYCLE.FEED_RECYCLE_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'FEED_RECYCLE_FEEDING_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...FEED_RECYCLE.FEED_RECYCLE_FEEDING_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...FEED_RECYCLE.FEED_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 设备信息
            case 'EQUIPMENT_INFO':
              component.children.push({
                ...EQUIPMENT_INFO.CUSTOM_FIELD_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 设备数采-数采点
            case 'EQUIPMENT_DATA_ACQUISITION':
              component.children.forEach((child: any, index: number) => {
                if (child.componentType === 'EQUIPMENT_DATA_ACQUISITION_GROUP') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                  flagIndex = index;
                }
              });
              // 在 flagIndex 后添加按钮
              component.children.splice(flagIndex + 1, 0, {
                ...EQUIPMENT_DATA_ACQUISITION.EQUIPMENT_DATA_ACQUISITION_GROUP_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...EQUIPMENT_DATA_ACQUISITION.EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 清场执行
            case 'CLEAN_IMPLEMENT':
              component.children.push({
                ...CLEAN_IMPLEMENT.CLEAN_IMPLEMENT_GROUP_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 清场检查
            case 'CLEAN_CHECK':
              component.children.push({
                ...CLEAN_CHECK.CLEAN_CHECK_GROUP_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 物料预定
            case 'MATERIAL_RESERVE':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'MATERIAL_RESERVE_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...MATERIAL_RESERVE.MATERIAL_RESERVE_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'MATERIAL_RESERVE_BATCH',
              );
              component.children.splice(index + 1, 0, {
                ...MATERIAL_RESERVE.MATERIAL_RESERVE_BATCH_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...MATERIAL_RESERVE.ADD_MATERIAL_RESERVE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 生产投料
            case 'MATERIAL_INPUT':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'MATERIAL_INPUT_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...MATERIAL_INPUT.MATERIAL_INPUT_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'MATERIAL_INPUT_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...MATERIAL_INPUT.MATERIAL_INPUT_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...MATERIAL_INPUT.ADD_MATERIAL_INPUT_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配液投入
            case 'LIQUID_PREPARATION_INPUT':
              component.children.forEach((child: any) => {
                if (child.componentType === 'LIQUID_PREPARATION_INPUT_DETAIL') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_INPUT_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_INPUT.LIQUID_PREPARATION_INPUT_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_INPUT_DETAIL',
              );
              index > -1 &&
                component.children.splice(index + 1, 0, {
                  ...LIQUID_PREPARATION_INPUT.LIQUID_PREPARATION_INPUT_BATCH_BUTTON,
                  fieldId: NODE_ID.value.pop()!,
                });
              component.children.push({
                ...LIQUID_PREPARATION_INPUT.ADD_LIQUID_INPUT_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配液计划
            case 'LIQUID_PREPARATION_PLAN':
              component.children.forEach((child: any) => {
                if (child.componentType === 'LIQUID_PREPARATION_PLAN_BATCH') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
                if (child.componentType === 'LIQUID_PREPARATION_PLAN_SUMMARY') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_PLAN_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_PLAN.LIQUID_PREPARATION_PLAN_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_PLAN_BATCH',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_PLAN.LIQUID_PREPARATION_PLAN_BATCH_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...LIQUID_PREPARATION_PLAN.ADD_LIQUID_PLAN_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配液量取
            case 'LIQUID_PREPARATION_MEASURE':
              component.children.forEach((child: any) => {
                if (child.componentType === 'LIQUID_PREPARATION_MEASURE_DETAIL') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_MEASURE_SUMMARY',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_MEASURE.LIQUID_PREPARATION_MEASURE_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_MEASURE_DETAIL',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_MEASURE.LIQUID_PREPARATION_MEASURE_DETAIL_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...LIQUID_PREPARATION_MEASURE.ADD_LIQUID_MEASURE_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 配液产出
            case 'LIQUID_PREPARATION_OUTPUT':
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_OUTPUT_SUMMARY',
              );
              component.children.forEach((child: any) => {
                if (child.componentType === 'LIQUID_PREPARATION_OUTPUT_DETAILS') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
              });
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_OUTPUT.LIQUID_PREPARATION_OUTPUT_SUMMARY_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              index = component.children.findLastIndex(
                (child: any) => child.componentType === 'LIQUID_PREPARATION_OUTPUT_DETAILS',
              );
              component.children.splice(index + 1, 0, {
                ...LIQUID_PREPARATION_OUTPUT.LIQUID_PREPARATION_OUTPUT_DETAILS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              component.children.push({
                ...LIQUID_PREPARATION_OUTPUT.ADD_LIQUID_OUTPUT_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 物料件信息
            case 'MATERIAL_INFO':
              component.children.push({
                ...CUSTOM_FIELD_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 称量数据
            case 'WEIGHING_DATA':
              component.children.push({
                ...WEIGHING_DATA.WEIGHING_DATA_DETAIL_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 设备数采绘图
            case 'EQUIPMENT_DATA_DRAW_LIST':
              component.children.push({
                ...EQUIPMENT_DATA_DRAW_LIST.EQUIPMENT_DATA_DRAW_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            // 批次检验结果信息
            case 'INSPECTION_RESULTS':
              component.children.forEach((child: any) => {
                if (child.componentType === 'BATCH_INSPECTION_RESULTS') {
                  child.children.push({
                    ...CUSTOM_FIELD_BUTTON,
                    fieldId: NODE_ID.value.pop()!,
                  });
                }
              });
              component.children.push({
                ...INSPECTION_RESULTS.BATCH_INSPECTION_RESULTS_BUTTON,
                fieldId: NODE_ID.value.pop()!,
              });
              break;
            default:
              break;
          }
        });
      }
    });
  };

  const INIT_TREE_DATA = async () => {
    try {
      await GET_RECORD();
      // 配业务组件添加按钮
      EXPANDED_KEYS.value = [route.params.record_id as string];
      SELECTED_KEYS.value = [TREE_DATA.value[0].children[0].itemId];
      CURRENT_NODE.value = {};
      INST_NODE_LIST.value = [];
    } catch (error: any) {
      throw 'init data failed';
    }
  };

  const GET_RECORD = async () => {
    const node = {
      node: {
        eventKey: TREE_DATA.value[0].children[0].itemId,
        dataRef: TREE_DATA.value[0].children[0],
      },
    };
    await TREE_SELECT([], node);
  };

  const TREE_SELECT = debounce(async (keys: KEY[], { node }: any) => {
    // 手动设置点击的记录项
    SELECTED_KEYS.value = [node.eventKey];
    // 切换前清空当前记录项点击样式
    // useEDITOR.EDITOR_INSTANCE.value?.resetUndo();
    // 获取点击的记录项数据
    const { data: component } = await recordListComponent({
      itemId: node.eventKey,
      recordVersionId,
    });
    // 记录项添加按钮
    if (!IS_SHOW.value) {
      addButtonForBusinessComponent([component]);
    }
    const nodeData = { ...node.dataRef, ...component };
    RecordData.treePosition = node.dataRef.id;
    RecordData.itemList = [component];
    // 渲染记录项
    SET_INST_NODE_LIST(nodeData);
    CURRENT_NODE.value = nodeData;
    INIT_CONTENT(nodeData, (component.pageConfig && JSON.parse(component.pageConfig)) || {});
    NODE_ACTIVE_KEYS.value = [];
  }, 100);

  type TreeDataItem = TreeProps['treeData'][number];
  const onDrop = async (info: AntTreeNodeDropEvent) => {
    const data: any[] = [...TREE_DATA.value[0].children];
    const changeSort = (
      data: TreeProps['treeData'],
      key: string | number,
      sort: number,
      bSort: number,
      callback: Function,
    ) => {
      data?.forEach((item, index) => {
        if (item.itemId === key) {
          item.sort = sort;
        } else if (item.sort <= sort && item.sort >= bSort) {
          callback(item, index, data);
        }
      });
    };
    const loop = (data: TreeProps['treeData'], key: string | number, callback: Function) => {
      if (!data) return;
      for (let index = 0; index < data.length; index++) {
        const element = data[index];
        if (element.itemId === key) {
          return callback && callback(element, index, data);
        }
        if (element.children && element.children.length > 0) {
          loop(element.children, key, callback);
        }
      }
    };
    const dragnode = info.dragNode;
    const node = info.node;
    const posit = info.dropPosition;
    let dragItem: TreeDataItem;
    if (info.dropToGap && info.dropPosition != -1) {
      if (node.key == dragnode.recordVersionId) {
        // 禁止拖拽的目标是父级的同级
        return;
      }
      changeSort(data, dragnode.key, node.sort, dragnode.sort, (item: TreeDataItem) => {
        item.sort--;
      });
      // 删除拖拽项
      loop(data, dragnode.key, (item: any, index: number, arr: TreeDataItem[]) => {
        arr.splice(index, 1);
        dragItem = item;
      });
      // 在目标位置加已删除的拖拽项
      loop(data, node.key, (item: any, index: number, arr: TreeDataItem[]) => {
        arr.splice(index + 1, 0, dragItem);
      });
    } else {
      if (posit === 0) {
        changeSort(data, dragnode.key, node.sort, dragnode.sort, (item: TreeDataItem) => {
          item.sort++;
        });
        loop(data, dragnode.key, (item: any, index: number, arr: TreeDataItem[]) => {
          arr.splice(index, 1);
          dragItem = item;
        });
        data.unshift(dragItem);
      }
    }
    TREE_DATA.value[0].children = data.map((item, index) => {
      item.sort = index + 1;
      return item;
    });
    // 拖拽后需要保存记录项顺序
    await recordItemChangeSort({
      recordVersionId,
      itemList: TREE_DATA.value[0].children,
    });
  };

  // 批量模式相关
  const addTemplate = () => {
    if (isRage.value && clickNode.value?.componentType) {
      ADD_NODE(clickNode.value);
      const data = { ...(INST_NODE_LIST.value?.[INST_NODE_LIST.value.length - 1] || {}) };
      NODE_CLICK(data.fieldId, [data.fieldId], {
        title: data.componentName,
        type: data.componentType,
        data,
        actived: true,
        key: data.fieldId,
      });
    }
  };

  return {
    TREE_DATA,
    TREE_SELECT,
    EXPANDED_KEYS,
    SELECTED_KEYS,
    INIT_TREE_DATA,
    CURRENT_NODE,
    ACTION_LIST,
    GET_RECORD,
    onDrop,
    RecordData,
    saveTemplate,
    spinClink,
    isRage,
    clickNode,
    searchMethod,
    getTreeData,
    addTemplate,
  };
};
