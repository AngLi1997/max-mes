<template>
  <div class="process-flow-container">
    <Row class="header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('生产指令单') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.state === 'add'">
            {{ t('新建指令单') }}
          </breadcrumb-item>
          <breadcrumb-item v-if="props.state === 'edit'">
            {{ t('编辑指令单') }}
          </breadcrumb-item>
          <breadcrumb-item v-if="lookOrDetails()">
            {{ t('指令单详情') }}
          </breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <ApprovalBtns
            v-if="props.state === 'details'"
            :settings="settings"
            :taskId="formInfo.taskId"
            :deploymentId="formInfo.deploymentId"
            :nodeId="formInfo.elementKey"
            :executionId="formInfo.executionId"
            :processInstanceId="formInfo.processInstanceId"
            @action="action" />
          <Button @click="back">{{ t('返回') }}</Button>
          <Button v-if="props.state === 'add' || props.state === 'edit'" type="primary" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="setting">
      <BMTableTitle :title="t('生产信息')" />
      <!-- 表单 -->
      <BMForm v-if="showBm" ref="myFormRef" v-bind="formProps"></BMForm>
      <BMTableTitle :title="t('关联信息')" />
      <div :class="[lookOrDetails() ? 'batch-table' : 'batch-table2']">
        <BMTable
          ref="tableInstance"
          :dataSource="tableData"
          :columns="columns"
          row-key="id"
          :showToolBar="false"
          :search="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="false"></BMTable>
      </div>
      <!-- 查看时需展示计划生产日期表格 -->
      <BMTableTitle v-if="lookOrDetails()" :title="t('计划生产日期')" />
      <div v-if="lookOrDetails()" class="batch-table">
        <BMTable
          :dataSource="tableData2"
          :columns="columns2"
          row-key="id"
          :showToolBar="false"
          :search="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="false"
          :showIndex="true"></BMTable>
      </div>
    </div>
  </div>
  <!-- 流程图 -->
  <BMModalForm v-model:open="open" :title="t('生产工艺预览')" :footer="null" wrapClassName="modalSizeExtraLarge">
    <Flow
      v-if="showFlow"
      ref="flowInstance"
      :modalJson="modalJson"
      :isShowLeftToolBar="false"
      :isShowTopToolBar="false"
      :showNextIcon="false"
      :showSetIcon="false"
      :showDivider="false"
      :isView="isView"
      left-icon="Process2"
      next-icon="ProcessNext"
      :mouseenter="() => {}"
      class="flow" />
  </BMModalForm>
  <BatchModal ref="batchModalRef" :rowData="rowData" @modalOk="modalOk"></BatchModal>
</template>
<script setup lang="tsx">
  import {
    Row,
    Col,
    Breadcrumb,
    BreadcrumbItem,
    Space,
    Button,
    message,
    InputGroup,
    Select,
    Input,
    FormItemRest,
    FormItem,
  } from 'ant-design-vue';
  import { BMForm, RenderCallbackParams, BMModalForm, BMTableTitle, TableColumn, BMTable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import Flow from '@/components/Flow/flow.vue';
  import ApprovalBtns from '@/components/Approval/components/ApprovalBtns/index.vue';
  import BatchModal from './components/batchModal.vue';
  import {
    getPlanProductList,
    getPlanProcessList,
    planSave,
    planEditSave,
    planGetNextUseNo,
    reqGetDetailUsingGET,
    reqGetProcessModelUsingGET,
    reqFormulaVersionDetail,
    reqProductMaterialProductTreeReq,
    getParameter,
    reqFactoryLineListByProcessVersion, //通过工艺id获取产线
    reqProcessRelationProcesses, //查询关联的工艺集合
    reqPlanRelationList, //查关联信息列表
    reqPlanDetail,
  } from '@/services';
  const emit = defineEmits(['backAndSave']);
  const props = defineProps({
    state: {
      type: String,
      default: '',
    },
    formInfo: {
      type: Object,
      default: () => ({}),
    },
  });
  const settings = computed(() => {
    try {
      return JSON.parse(props.formInfo.payload?.settings || {});
    } catch (error) {
      return {};
    }
  });
  const processList = ref([]); //存生产工艺list
  const isView = ref<boolean>(true);
  const flowInstance = ref();
  const showFlow = ref(true);
  const planNoCodeApplyTime = ref(); //指令单编码回传编号日期
  const batchNoCodeApplyTime = ref(); //批号回传编号日期
  const processNum = ref(); //工艺下拉改变时存流程节点数量
  const processVersion = ref(); //工艺下拉改变时存所选的版本号
  const open = ref<boolean>(false); //流程图弹框
  const processModelId = ref(); //查流程图
  const modalJson = ref();
  const myFormRef = ref();
  const productList = ref(); //存产品下拉框(非树结构产品)
  const planTypeList = ref<any>([]); //存指令单类型(脚本而来)
  const temp = ref<any>();
  const showBm = ref(true); //刷新表单
  const tableData = ref([]);
  const tableData2 = ref([]); //查看时需展示计划生产日期表格
  const batchModalRef = ref();
  const rowData = ref();

  const formProps = reactive<any>({
    initialValues: {},
    labelWidth: 110,
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 6,
      style: {
        textAlign: 'center',
      },
    },
    showAdvancedButton: true,
    showActionButtonGroup: false,
    // 处理日期为YYYY-MM-DD格式
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },

    schemas: [
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品名称'),
        required: true,
        componentProps: ({ formModel, formInstance }: any) => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: any) => {
              const options = productList.value.find((item: any) => item.value === value);
              // 选择产品信息后回显产品编码和产品规格和及可选择的生产工艺
              formInstance.clearValidate();
              if (options) {
                formModel.productMergeCode = options.mergeCode;
                formModel.productSpecification = options.specification;
                getProcessList(value);
                formModel.processId = undefined;
                formModel.productName = options.name;
                formModel.productMark = options?.productMark; //产品标识
                formModel.planNo = '';
                formModel.batchNo = '';
                formModel.unitId = undefined;
                formModel.unitName = undefined;
                formModel.batchQuantity = undefined;
                formModel.productionLineId = undefined;
                myFormRef.value?.updateSchema({
                  field: 'productionLineId',
                  componentProps: {
                    options: [],
                  },
                });
                formModel.type = undefined;
                formModel.innerPackingSpecification = options.innerPackingSpecification || '';
                formModel.packingSpecification = options.packingSpecification || '';
                // 清空流程图
                modalJson.value = [];
                showFlow.value = false;
                nextTick(() => {
                  showFlow.value = true;
                });
                tableData.value = [];
              } else {
                //清空产品名称下拉时
                formModel.productMergeCode = '';
                formModel.productSpecification = '';
                formModel.processId = undefined;
                formModel.planNo = '';
                formModel.batchNo = '';
                formModel.unitId = undefined;
                formModel.unitName = undefined;
                formModel.batchQuantity = undefined;
                formModel.productionLineId = undefined;
                formModel.innerPackingSpecification = '';
                formModel.packingSpecification = '';
                processList.value = [];
                myFormRef.value?.updateSchema({
                  field: 'productionLineId',
                  componentProps: {
                    options: [],
                  },
                });
                formModel.type = undefined;
                // 清空流程图
                modalJson.value = [];
                showFlow.value = false;
                nextTick(() => {
                  showFlow.value = true;
                });
                tableData.value = [];
              }
            },
          };
        },
      },
      {
        field: 'productMergeCode',
        component: 'Input',
        label: t('产品编码'),
        required: true,
        componentProps: {
          placeholder: t('选择产品自动关联'),
        },
      },
      {
        field: 'productSpecification',
        component: 'Input',
        label: t('产品规格'),
        required: true,
        componentProps: {
          placeholder: t('选择产品自动关联'),
        },
      },
      {
        field: 'processId',
        label: '',
        labelWidth: '27px',
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <FormItem
              name={['processId']}
              label={t('生产工艺')}
              rules={[
                {
                  required: true,
                  trigger: 'blur',
                  message: t('请选择生产工艺'),
                },
              ]}>
              <InputGroup compact>
                <FormItemRest>
                  <Select
                    v-model:value={formModel.processId}
                    style={{ width: 'calc(100% - 68px)' }}
                    disabled={props.state === 'add' ? false : true}
                    allowClear={true}
                    onChange={async (val: any, options: any) => {
                      if (options) {
                        formModel.planNo = '';
                        formModel.batchNo = '';
                        formModel.productionLineId = undefined;
                        formModel.type = undefined;
                        formModel.processName = options?.name;
                        formModel.processVersion = options?.activeVersion;
                        processVersion.value = options?.activeVersion;
                        getProductionLineList({ id: val, version: options?.activeVersion });
                        try {
                          const data = {
                            processId: val,
                            version: options.activeVersion ? options.activeVersion : '',
                          };
                          const res: any = await reqGetDetailUsingGET(data);
                          processModelId.value = res.data.processModelId;
                          processNum.value = res.data.procedures.length;
                          formModel.productionStageCode = res.data?.productionStageCode;
                          if (res.data.productFormulaVersionId) {
                            const { data: formulaDetail } = await reqFormulaVersionDetail({
                              versionId: res.data.productFormulaVersionId,
                            });
                            const { unitId, unitName, batchQuantity } = formulaDetail;
                            formModel.unitId = unitId;
                            formModel.unitName = unitName;
                            formModel.batchQuantity = batchQuantity;
                          }
                        } catch (error: any) {
                          message.error(error.message);
                        }
                        try {
                          const data2 = {
                            processModelId: processModelId.value
                              ? processModelId.value
                              : // : '7f2e87cd-2701-454a-9071-8d233e26d8ea', //用于测试切换流程图
                                '',
                          };
                          const res2: any = await reqGetProcessModelUsingGET(data2);
                          // 回显流程图
                          echoFlowchart(res2.data);
                        } catch (error: any) {
                          message.error(error.message);
                        }
                        //回显下方关联信息表格
                        try {
                          const res3 = await reqProcessRelationProcesses({ processId: val });
                          tableData.value = res3.data.map((item: any) => {
                            return {
                              ...item,
                              processId: item.id,
                              planIds: [],
                              batchNos: [],
                              checkedNodes1: [], //历史批次勾选的数组对象
                            };
                          });
                        } catch (error: any) {
                          message.error(error.message);
                        }
                      } else {
                        //清空时
                        myFormRef.value?.updateSchema({
                          field: 'productionLineId',
                          componentProps: {
                            options: [],
                          },
                        });
                        formModel.productionLineId = undefined;
                        formModel.type = undefined;
                        formModel.planNo = '';
                        formModel.batchNo = '';
                        // 清空流程图
                        modalJson.value = [];
                        tableData.value = [];
                      }
                    }}
                    placeholder={t('请选择生产工艺')}
                    options={processList.value}></Select>
                  <Button onClick={() => (open.value = true)}>{t('预览')}</Button>
                </FormItemRest>
              </InputGroup>
            </FormItem>
          );
        },
      },
      {
        field: 'productionLineId',
        component: 'Select',
        label: t('产线'),
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            showSearch: true,
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: (value: any, option: any) => {
              formModel.type = undefined;
              formModel.planNo = '';
              formModel.batchNo = '';
              formModel.productionLineCode = option?.code;
            },
          };
        },
      },
      {
        field: 'type',
        component: 'Select',
        label: t('指令单类型'),
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            options: [
              {
                label: t('生产批次'),
                value: 'PRODUCT',
              },
              {
                label: t('实验批次'),
                value: 'EXPERIMENT',
              },
              {
                label: t('验证批次'),
                value: 'VERIFY',
              },
            ],
            onChange: async (val: any) => {
              if (!val) {
                formModel.planNo = '';
                formModel.batchNo = '';
                return;
              }
              temp.value = planTypeList.value.find((item: any) => item?.label === val);
              if (!formModel.processId) return message.error(t('请先选择生产工艺'));
              if (props.state === 'add' && !formModel.productionLineCode) return message.error(t('请先选择产线'));
              // 指令单编号回传编号日期
              try {
                const data3 = {
                  code: '',
                  fields: {
                    productName: formModel.productName || props?.formInfo?.productName,
                    productMergeCode: formModel.productMergeCode,
                    innerPackingSpecification: formModel.innerPackingSpecification,
                    packingSpecification: formModel.packingSpecification,
                    productPlanType: temp.value?.value,
                    productMark: formModel.productMark,
                    productionLineCode: formModel?.productionLineCode,
                    productionStageCode: formModel?.productionStageCode,
                  },
                  processId: formModel.processId,
                  type: 'PRODUCT_PLAN_NO',
                };
                const res3 = await planGetNextUseNo(data3);
                formModel.planNo = res3.data?.no; //回显指令单编号
                formModel.planNoCode = res3.data?.code || ''; //存指令单编码规则code
                planNoCodeApplyTime.value = res3.data?.applyTime;
              } catch (error: any) {
                message.error(error.message);
                formModel.planNoCode = '';
                formModel.planNo = '';
              }
              // 生产批号回传编号日期
              try {
                const data4 = {
                  code: '',
                  fields: {
                    productName: formModel.productName || props?.formInfo?.productName,
                    productMergeCode: formModel.productMergeCode,
                    innerPackingSpecification: formModel.innerPackingSpecification,
                    packingSpecification: formModel.packingSpecification,
                    productPlanType: temp.value?.value,
                    productMark: formModel.productMark,
                    productionLineCode: formModel?.productionLineCode,
                    productionStageCode: formModel?.productionStageCode,
                  },
                  processId: formModel.processId,
                  type: 'PRODUCT_PLAN_BATCH_NO',
                };
                const res4 = await planGetNextUseNo(data4);
                formModel.batchNo = res4.data?.no; //回显指令单编号
                formModel.batchNoCode = res4.data?.code || ''; //存批号编码规则code
                batchNoCodeApplyTime.value = res4.data?.applyTime;
              } catch (error: any) {
                message.error(error.message);
                formModel.batchNoCode = '';
                formModel.batchNo = '';
              }
            },
          };
        },
      },
      {
        field: 'planNo',
        component: 'Input',
        label: t('指令单编号'),
        required: true,
        componentProps: { placeholder: t('请输入') },
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('生产批号'),
        required: true,
        componentProps: {
          placeholder: t('请输入'),
        },
      },
      {
        field: 'batchQuantity',
        label: t('生产批量'),
        required: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <Input
                v-model:value={formModel['batchQuantity']}
                style={{ width: '60%' }}
                placeholder={t('请输入生产批量')}
                disabled={props.state === 'add' || props.state === 'edit' ? false : true}
              />
              <FormItemRest>
                <Input
                  v-model:value={formModel['unitName']}
                  style={{ width: '40%' }}
                  disabled={true}
                  placeholder={t('请输入生产批量单位')}
                />
              </FormItemRest>
            </InputGroup>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                // 判断是否为正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(Number(value))) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                if (!Number(value)) {
                  return Promise.reject(t('请输入生产批量'));
                }
                if (!formModel['unitId']) {
                  return Promise.reject(t('请输入生产批量单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      // {
      //   field: 'productDate',
      //   component: 'DatePicker',
      //   label: t('计划生产时间'),
      //   required: true,
      //   componentProps: {
      //     placeholder: t('请选择日期'),
      //     disabled: false,
      //   },
      // },
    ],
  });
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('关联工艺'),
      dataIndex: 'name',
    },
    {
      title: t('关联批次'),
      dataIndex: 'relatedBatch',
      customRender: ({ record }: any) => (
        <div class={{ disabledRow: lookOrDetails(), relatedBatch: true }} onClick={() => openBatchModal(record)}>
          {record?.batchNos?.join('，')}
        </div>
      ),
    },
  ];
  // 计划生产日期表格列
  const columns2: TableColumn[] = [
    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
    },
    {
      title: t('计划开始日期'),
      dataIndex: 'startTime',
    },
    {
      title: t('计划结束日期'),
      dataIndex: 'endTime',
    },
  ];
  // 查看页面或审核处理页面
  const lookOrDetails = () => {
    return props.state === 'look' || props.state === 'details';
  };

  // 返回
  const back = () => {
    emit('backAndSave');
  };

  // 保存
  const save = async () => {
    const relationPlanList = tableData.value.map((item: any) => {
      return {
        batchNos: item.batchNos,
        planIds: item.planIds,
        processId: item.processId || item.id,
      };
    });
    const data: any = await myFormRef.value?.validate();
    // 新增时的表单保存
    if (props.state === 'add') {
      try {
        const params = {
          ...data,
          batchNoCodeApplyTime: batchNoCodeApplyTime.value ? batchNoCodeApplyTime.value : '',
          planNoCodeApplyTime: planNoCodeApplyTime.value ? planNoCodeApplyTime.value : '',
          processNum: processNum.value,
          processVersion: processVersion.value ? processVersion.value : '',
          productPlanType: temp.value?.value,
          relationPlanList,
        };
        await planSave(params);
        // 关闭新建指令单组件
        emit('backAndSave');
        message.success(t('保存成功'));
      } catch (error: any) {
        message.error(error.message);
      }
    }
    // 编辑时的表单保存
    else {
      try {
        const params = {
          id: props.formInfo.id,
          // productDate: data.productDate,
          type: data.type.value ? data.type.value : data.type,
          batchQuantity: data.batchQuantity,
          productionLineId: data?.productionLineId,
          batchNo: data?.batchNo,
          planNo: data?.planNo,
          relationPlanList,
        };
        await planEditSave(params);
        // 关闭新建指令单组件
        emit('backAndSave');
        message.success(t('保存成功'));
      } catch (error: any) {
        message.error(error.message);
      }
    }
  };
  // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
  const loopTree = (data: any) => {
    return data.map((item: any) => {
      if (item.categoryFlag) {
        item.selectable = false;
      } else {
        item.selectable = true;
      }
      item.label = item.mergeCode + '-' + item.name;
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  // 获取产品下拉列表
  const getProductList = async () => {
    const res: any = await getPlanProductList();
    const options = res.data.map((item: any) => {
      return {
        ...item,
        label: item.name,
        value: item.id,
      };
    });
    productList.value = options;
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      myFormRef.value?.updateSchema({
        field: 'productId',
        componentProps: {
          treeData: loopTree(data) || [],
        },
      });
    } catch (error) {
      myFormRef.value?.updateSchema({
        field: 'productId',
        componentProps: {
          treeData: [],
        },
      });
    }
  };
  // 指令单类型(脚本而来)
  const getPlanTypeList = async () => {
    try {
      const { data } = await getParameter('mes.ProductionPlanType');
      planTypeList.value = Object.keys(JSON.parse(data.value)).map((item: any) => {
        return {
          label: item,
          value: JSON.parse(data.value)[item],
        };
      });
    } catch (error) {
      planTypeList.value = [];
    }
  };
  // 产品下拉改变时获取生产工艺下拉列表
  const getProcessList = async (val: any) => {
    const data = { productId: val, active: true };
    const res: any = await getPlanProcessList(data);
    const options = res.data.map((item: any) => {
      return {
        ...item,
        // label: item.activeVersion ? item.name + '-' + item.activeVersion : item.name, //label拼接版本号
        label: props.formInfo.processName + '-' + props.formInfo.processVersion, //需求改动之后此处都是disabled状态 可直接拼接可能变动的版本号
        value: item.id,
      };
    });
    processList.value = options;
  };
  // 通过工艺id及版本获取产线下拉列表
  const getProductionLineList = async (data: any) => {
    try {
      const res = await reqFactoryLineListByProcessVersion(data);
      myFormRef.value?.updateSchema({
        field: 'productionLineId',
        componentProps: {
          options: res?.data || [],
        },
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 初始化
  const init = async () => {
    getProductList();
    getPlanTypeList();
    if (props.state === 'add') {
      formProps.initialValues = {};
      formProps.disabled = false;
      // 刷新表单
      showBm.value = false;
      nextTick(() => {
        showBm.value = true;
      });
    }
    if (props.state === 'look' || props.state === 'details') {
      formProps.initialValues = {};
      formProps.schemas.forEach((item: any) => {
        if (item.field !== 'processId') {
          myFormRef.value?.updateSchema({
            field: item.field,
            componentProps: {
              disabled: true,
            },
          });
        }
      });
      getProductList();
      myFormRef.value?.setFieldsValue(props.formInfo);
      myFormRef.value?.setFormModels({
        unitId: props.formInfo.unitId,
        unitName: props.formInfo.unitName,
      });
      getProcessList(props.formInfo.productId);
      getProductionLineList({ id: props.formInfo?.processId, version: props.formInfo?.processVersion });
      // 回显流程图
      echoFlow();
      echoTableData();
      getPlanDetail();
    }
    if (props.state === 'edit') {
      nextTick(() => {
        myFormRef.value?.setFieldsValue(props.formInfo);
        myFormRef.value?.setFormModels({
          unitId: props.formInfo.unitId,
          unitName: props.formInfo.unitName,
        });
        getProcessList(props.formInfo.productId);
        getProductionLineList({ id: props.formInfo?.processId, version: props.formInfo?.processVersion });
        formProps.schemas.forEach((item: any) => {
          if (
            item.field !== 'type' &&
            item.field !== 'productionLineId' &&
            item.field !== 'planNo' &&
            item.field !== 'batchNo'
          ) {
            myFormRef.value?.updateSchema({
              field: item.field,
              componentProps: {
                disabled: true,
              },
            });
          }
        });
        // 回显流程图
        echoFlow();
        echoTableData();
      });
    }
  };

  // 处理流程图数据方法
  const echoFlowchart = (val: any) => {
    modalJson.value = JSON.parse(val).map((item: any) => {
      const metaInfo = JSON.parse(item.metaInfo);
      return {
        ...metaInfo,
        data: {
          ...metaInfo.data,
        },
      };
    });
  };
  // 查看和编辑时回显流程图方法
  const echoFlow = async () => {
    // 回显流程图
    try {
      const data = {
        processId: props.formInfo.processId,
        version: props.formInfo.processVersion ? props.formInfo.processVersion : null,
      };
      const res: any = await reqGetDetailUsingGET(data);
      processModelId.value = res.data.processModelId;
      processNum.value = res.data.procedures.length;
    } catch (error: any) {
      message.error(error.message);
    }
    try {
      const data2 = {
        processModelId: processModelId.value ? processModelId.value : '', //用于测试切换流程图
      };
      const res2: any = await reqGetProcessModelUsingGET(data2);
      // 回显流程图
      echoFlowchart(res2.data);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 查看和编辑时回显关联信息表格的方法
  const echoTableData = async () => {
    try {
      const { data } = await reqPlanRelationList({ planId: props.formInfo.id });
      tableData.value = data?.map((item: any) => {
        return {
          ...item,
          id: item.processId,
          name: item.processName,
          planIds: item.relationBatchList?.map((item: any) => item.planId),
          batchNos: item.relationBatchList?.map((item: any) => item.planBatchNo),
          checkedNodes1: item.relationBatchList
            ?.filter((item: any) => item.related)
            ?.map((item2: any) => {
              return {
                id: item2.planId,
                batchNo: item2.planBatchNo,
              };
            }),
        };
      });
    } catch (error) {}
  };

  //详情接口回显计划生产日期信息
  const getPlanDetail = async () => {
    try {
      const { data: planDetail } = await reqPlanDetail(props.formInfo.id);
      tableData2.value = planDetail.procedureList;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 点击关联批次div
  const openBatchModal = (row: any) => {
    rowData.value = row;
    batchModalRef.value.openModal();
  };
  // 弹框确定
  const modalOk = (val1: any, val2: any, rowId: any, checkedNodes1: any) => {
    tableData.value.forEach((item: any) => {
      if (item.id === rowId) {
        item.planIds = val1;
        item.batchNos = val2;
        item.checkedNodes1 = checkedNodes1;
      }
    });
  };
  // 指令单审核-处理跳转 审核通过 审核退回 审核不通过按钮
  // 审核组件的操作
  const action = () => {
    emit('backAndSave');
  };

  onMounted(() => {
    init();
  });
</script>

<style scoped lang="less">
  .process-flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    .header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
      }
    }
  }
  .action {
    text-align: right;
  }
  .vertical-group-divider {
    padding: 0;
    margin: 0;
  }
  .setting {
    width: 100%;
    height: calc(100% - 56px);
    // display: flex;
    background-color: var(--bmos-primary-color-white);
    padding: 12px 12px 0px 12px;
    display: flex;
    flex-direction: column;
    .batch-table {
      flex: 0.5;
      overflow-y: hidden;
    }
    .batch-table2 {
      flex: 1;
      overflow-y: hidden;
    }
  }
  :deep(.mes-picker) {
    width: 100%;
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
  .flow {
    width: 100%;
    height: calc(100vh - 280px);
  }
  :deep(.relatedBatch) {
    width: 100%;
    height: 36px;
    padding: 6px;
    border: 1px solid #d4d7d9;
    border-radius: 4px;
    overflow: hidden;
    /* 超出部分用省略号表示 */
    text-overflow: ellipsis;
  }
  :deep(.relatedBatch):hover {
    border: 1px solid rgb(40, 113, 255);
  }
  :deep(.disabledRow) {
    pointer-events: none; //看得见 摸不着
    opacity: 0.5; /* 置灰效果，不过仍可见 */
  }
</style>
