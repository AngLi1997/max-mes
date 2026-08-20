<template>
  <div class="process-flow-container">
    <Row class="header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('生产指令单') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('批量创建') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="multipleBack">{{ t('返回') }}</Button>
          <Button type="primary" @click="multipleSave">{{ t('保存') }}</Button>
        </Space>
      </Col>
    </Row>
    <div class="setting">
      <BMTableTitle :title="t('生产信息')" />
      <!-- 上方表单 -->
      <BMForm ref="myFormRef" v-bind="formProps" @formModelChange="formModelChange">
        <template #selectB="{ formModel, field }">
          <Input v-model:value="formModel[field]" allow-clear :placeholder="t('请输入')" :addon-after="t('天')" />
        </template>
        <template #resetBefore>
          <Button type="primary" @click="batchCreate">
            {{ t('批量创建') }}
          </Button>
        </template>
      </BMForm>
      <BMTableTitle :title="t('指令单批次信息')" />
      <!-- 批量创建计划 -->
      <div class="batch-table">
        <BMTable
          ref="tableInstance"
          :dataSource="dataSource"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :showToolBar="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="{
            pageSize: 20,
          }"
          :formProps="formPropsTable"
          :show-index="true"></BMTable>
        <!-- 编辑弹框 待写 -->
        <editPlan
          ref="editPlanRef"
          :rowData="rowData"
          :tableData="dataSource"
          @updateTableData="updateTableData"></editPlan>
      </div>
    </div>
  </div>
  <!-- 流程图 -->
  <BMModalForm v-model:open="open" :title="t('生产工艺预览')" :footer="null" wrapClassName="modalSizeExtraLarge">
    <Flow
      ref="flowInstance"
      :modalJson="modalJson"
      :isShowLeftToolBar="false"
      :isShowTopToolBar="false"
      :showNextIcon="false"
      :showSetIcon="false"
      :showDivider="false"
      :isView="true"
      left-icon="Process2"
      next-icon="ProcessNext"
      :mouseenter="() => {}"
      class="flow" />
  </BMModalForm>
</template>

<script setup lang="tsx">
  import type { FormProps, TableInstance } from '@bmos/components';
  import {
    BMTable,
    BMEllipsis,
    TableColumn,
    BMForm,
    BMModalForm,
    RenderCallbackParams,
    BMTableTitle,
  } from '@bmos/components';
  import { reactive, ref, onMounted } from 'vue';
  import {
    Row,
    Col,
    Breadcrumb,
    BreadcrumbItem,
    Space,
    Button,
    message,
    InputGroup,
    FormItemRest,
    FormItem,
    Select,
  } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import editPlan from './editPlan/editPlan.vue';
  import {
    getPlanProductList,
    getPlanProcessList,
    planGetBatchNextUseNo,
    planBatchSave,
    reqGetDetailUsingGET,
    reqFormulaVersionDetail,
    reqFactoryLineListByProcessVersion, //通过工艺id获取产线,
    getParameter,
    reqGetProcessModelUsingGET,
    reqProductMaterialProductTreeReq,
  } from '@/services';
  import dayjs from 'dayjs';
  import Flow from '@/components/Flow/flow.vue';

  const tableInstance = ref<TableInstance>();
  const editPlanRef = ref();
  const rowData = ref();
  const batchNoCode = ref();
  const planNoCode = ref();
  const planNoCodeApplyTime = ref(); //计划编码回传编号日期
  const batchNoCodeApplyTime = ref(); //批号回传编号日期
  const processNum = ref(); //工艺下拉改变时存下拉列表的数量
  const processVersion = ref(); //工艺下拉改变时存所选的版本号
  const planNos = ref(); //存计划编号数组
  const productList = ref(); //存产品下拉框(非树结构产品)
  const batchNos = ref(); //存生产批号数组
  const productMark = ref(); //存产品标识
  const productionLineCode = ref(); //存产线code
  const productionStageCode = ref(); //存productionStageCode
  const planTypeList = ref<any>([]); //存指令单类型list(脚本而来)
  const temp = ref<any>(); //存指令单类型值
  const processModelId = ref(); //查流程图
  const modalJson = ref();
  const open = ref<boolean>(false); //流程图弹框
  const processList = ref([]); //存生产工艺list

  const emit = defineEmits(['multipleBackAndSave']);
  // 间隔时长和批次数量校验 0-100的正整数
  const validator1 = async (_rule: any, value: string) => {
    if (!value && value !== '0') {
      return Promise.reject(t('请输入间隔时长'));
    } else if (!/^([1-9][0-9]{0,1}|99)$/.test(value)) {
      return Promise.reject(t('请输入小于100的正整数!'));
    } else {
      return Promise.resolve();
    }
  };
  // 间隔时长和批次数量校验 0-100的正整数
  const validator2 = async (_rule: any, value: string) => {
    if (!value && value !== '0') {
      return Promise.reject(t('请输入批次数量'));
    } else if (!/^([1-9][0-9]{0,1}|99)$/.test(value)) {
      return Promise.reject(t('请输入小于100的正整数!'));
    } else {
      return Promise.resolve();
    }
  };
  // 时间 字符串 '2023-12-30' 加一天
  const addOneDay = (date = '2023-12-30', intervalDay = 1): any => {
    const d = new Date(date);
    d.setDate(d.getDate() + intervalDay);
    return dayjs(d.getTime()).format('YYYY-MM-DD');
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
  // 批量创建按钮
  const batchCreate = async () => {
    const res: any = await myFormRef.value?.validate();
    // 计划编号回传编号日期
    try {
      const data1 = {
        code: '',
        fields: {
          productName: res.productName,
          productMergeCode: res.productMergeCode,
          innerPackingSpecification: res.innerPackingSpecification,
          packingSpecification: res.packingSpecification,
          productPlanType: temp.value?.value,
          productMark: productMark.value,
          productionLineCode: productionLineCode.value,
          productionStageCode: productionStageCode.value,
        },
        num: res.num,
        processId: res.processId,
        type: 'PRODUCT_PLAN_NO',
      };
      const res1 = await planGetBatchNextUseNo(data1);
      // formModel.planNo = res1.data.nos; //回显计划编号
      planNos.value = res1.data?.nos || []; //存编号数组
      planNoCode.value = res1.data?.code || ''; //存计划编码规则code
      planNoCodeApplyTime.value = res1.data.applyTime;
    } catch (error: any) {
      message.error(error.message);
      planNoCode.value = '';
      planNos.value = [];
    }
    // 生产批号回传编号日期
    try {
      const data2 = {
        code: '',
        fields: {
          productName: res.productName,
          productMergeCode: res.productMergeCode,
          innerPackingSpecification: res.innerPackingSpecification,
          packingSpecification: res.packingSpecification,
          productPlanType: temp.value?.value,
          productMark: productMark.value,
          productionLineCode: productionLineCode.value,
          productionStageCode: productionStageCode.value,
        },
        num: res.num,
        processId: res.processId,
        type: 'PRODUCT_PLAN_BATCH_NO',
      };
      const res2 = await planGetBatchNextUseNo(data2);
      // formModel.batchNo = res2.data.nos; //回显生产批号
      batchNos.value = res2.data?.nos || []; //存批号数组
      batchNoCode.value = res2.data?.code || ''; //存批号编码规则code
      batchNoCodeApplyTime.value = res2.data.applyTime;
    } catch (error: any) {
      message.error(error.message);
      batchNoCode.value = '';
      batchNos.value = [];
    }
    // 生产计划编号和生产批号通过编号规则自动生成或手动输入(如下为需要手动输入)
    dataSource.value = Array.from({ length: res.num }).map((_, i) => {
      return {
        id: i + 1,
        planNo: '',
        batchNo: '',
        productDate: addOneDay(res.firstProductionTime, res.intervalDuration * i), //生产时间,依次递增
        type: res.type,
        unitId: res.unitId,
        unitName: res.unitName,
        batchQuantity: res.batchQuantity,
      };
    });
    // 当通过编号规则拿到了生产计划编号和生产批号
    if (planNos.value && planNos.value.length !== 0) {
      dataSource.value.forEach((item: any, i) => {
        item.planNo = planNos.value[i];
      });
    }
    if (batchNos.value && batchNos.value.length !== 0) {
      dataSource.value.forEach((item, i) => {
        item.batchNo = batchNos.value[i];
      });
    }
  };
  // 编辑
  const edit = async (row: any) => {
    editPlanRef.value.openModal();
    rowData.value = row;
  };

  // 返回
  const multipleBack = () => {
    emit('multipleBackAndSave');
  };
  // 批量保存
  const multipleSave = async () => {
    const data: any = await myFormRef.value?.validate();
    try {
      const datas = {
        ...data,
        productionLineCode: productionLineCode.value,
        productionStageCode: productionStageCode.value,
        productMark: productMark.value,
        batchNoCodeApplyTime: batchNoCodeApplyTime.value ? batchNoCodeApplyTime.value : '',
        planNoCodeApplyTime: planNoCodeApplyTime.value ? planNoCodeApplyTime.value : '',
        processNum: processNum.value,
        processVersion: processVersion.value ? processVersion.value : '',
        batchNoCode: batchNoCode.value,
        planNoCode: planNoCode.value,
        details: dataSource.value,
        productPlanType: temp.value?.value,
      };
      await planBatchSave(datas);
      // 关闭新建计划组件
      emit('multipleBackAndSave');
      message.success(t('保存成功'));
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const myFormRef = ref();
  // 表单属性
  const formProps = reactive({
    initialValues: {
      //默认值
      // type: 'PRODUCT', //批量创建时默认指令单类型为生产批次
      // id:'',
      // productId:'1',
      // productMergeCode:'200',
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 110,
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 2,
    },
    showAdvancedButton: true,
    showSubmitButton: false, //是否展示查询按钮
    showResetButton: false, //是否展示重置按钮
    // showActionButtonGroup: false,//控制所有操作按钮是否展示
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
            onChange: (val: any) => {
              const options = productList.value.find((item: any) => item.value === val);
              // 选择产品信息后回显产品编码和产品规格和及可选择的生产工艺
              formInstance.clearValidate();
              if (options) {
                formModel.productMergeCode = options.mergeCode;
                formModel.productSpecification = options.specification;
                getProcessList(val);
                formModel.processId = undefined;
                formModel.productName = options.name;
                productMark.value = options?.productMark; //产品标识
                formModel.planNo = '';
                formModel.batchNo = '';
                formModel.innerPackingSpecification = options.innerPackingSpecification || '';
                formModel.packingSpecification = options.packingSpecification || '';
                // 清空流程图
                modalJson.value = [];
              } else {
                //清空产品名称下拉时
                formModel.productMergeCode = '';
                formModel.productSpecification = '';
                formModel.processId = undefined;
                formModel.planNo = '';
                formModel.batchNo = '';
                formModel.innerPackingSpecification = '';
                formModel.packingSpecification = '';
                myFormRef.value?.updateSchema({
                  field: 'productionLineId',
                  componentProps: {
                    options: [],
                  },
                });
                formModel.type = undefined;
                formModel.productionLineId = undefined;
                processList.value = [];
                // 清空流程图
                modalJson.value = [];
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
          options: [],
          placeholder: t('选择产品自动关联'),
        },
      },
      {
        field: 'processId',
        label: t(''),
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
                    allowClear={true}
                    onChange={async (val: any, options: any) => {
                      if (options) {
                        formModel.processName = options?.name;
                        formModel.processVersion = options?.activeVersion;
                        processVersion.value = options?.activeVersion;
                        formModel.productionLineId = undefined;
                        getProductionLineList({ id: val, version: options?.activeVersion });
                        try {
                          const data = {
                            processId: val,
                            version: options.activeVersion ? options.activeVersion : '',
                          };
                          const res: any = await reqGetDetailUsingGET(data);
                          processModelId.value = res.data.processModelId;
                          processNum.value = res.data.procedures.length;
                          productionStageCode.value = res.data?.productionStageCode;
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
                          error.message && message.error(error.message);
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
                      } else {
                        //清空生产工艺下拉时
                        myFormRef.value?.updateSchema({
                          field: 'productionLineId',
                          componentProps: {
                            options: [],
                          },
                        });
                        formModel.productionLineId = undefined;
                        // 清空流程图
                        modalJson.value = [];
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
        componentProps: () => {
          return {
            showSearch: true,
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: (value: any, option: any) => {
              productionLineCode.value = option?.code; //产线code
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
                temp.value = {};
                return;
              }
              temp.value = planTypeList.value.find((item: any) => item?.label === val);
            },
          };
        },
      },
      {
        field: 'firstProductionTime',
        component: 'DatePicker',
        label: t('首批生产时间'),
        required: true,
        labelWidth: 138,
        colProps: {
          span: 8,
        },
        componentProps: {
          placeholder: t('请选择日期'),
        },
      },
      {
        field: 'intervalDuration',
        component: 'Input',
        label: t('间隔时长'),
        rules: [{ required: true, validator: validator1, trigger: 'blur' }],
        slot: 'selectB',
      },
      {
        field: 'num',
        component: 'InputNumber',
        label: t('批次数量'),
        rules: [{ required: true, validator: validator2, trigger: 'blur' }],
        colProps: {
          span: 4,
        },
        componentProps: {
          placeholder: t('请输入'),
        },
      },
    ],
  });

  // 表格属性
  const formPropsTable = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 4,
    },
    baseColProps: {
      span: 6,
    },
    showAdvancedButtonBadge: false,
    showAdvancedButton: false,
    showActionButtonGroup: false,
  });
  // 表格数据来源
  const dataSource = ref([]);

  // 表格列
  const columns: TableColumn[] = [
    {
      title: 'id',
      align: 'left',
      dataIndex: 'id',
      fixed: 'left',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('指令单编号'),
      align: 'left',
      dataIndex: 'planNo',
      fixed: 'left',
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      align: 'left',
      dataIndex: 'batchNo',
      hideInSearch: true,
    },
    {
      title: t('指令单生产时间'),
      align: 'left',
      dataIndex: 'productDate',
      hideInSearch: true,
    },
    {
      title: t('指令单类型'),
      align: 'left',
      dataIndex: 'type',
      hideInSearch: true,
      width: 150,
      formItemProps: {
        component: 'Select',
        order: 5,
        componentProps: () => ({
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
        }),
      },
      customRender: ({ record }) =>
        record.type === 'PRODUCT'
          ? '生产批次'
          : record.type === 'EXPERIMENT'
          ? '实验批次'
          : record.type === 'VERIFY'
          ? '验证批次'
          : '-',
    },
    {
      title: t('生产批量'),
      dataIndex: 'batchQuantity',
      hideInSearch: true,
      customRender: ({ record }) => (record.unitName ? record.batchQuantity + record.unitName : '_'),
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => (
        <div class='action-list'>
          <>
            <Button style='max-width: 100px; min-width: 40px' type='link' onClick={() => edit(record)}>
              <BMEllipsis tooltip={true} style='max-width: 100%'>
                {{
                  default: () => t('编辑'),
                  title: () => t('编辑'),
                }}
              </BMEllipsis>
            </Button>
          </>
        </div>
      ),
    },
  ];
  // 监听表单改变
  const formModelChange = (val: any) => {
    dataSource.value = [];
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
  // 产品下拉改变时获取生产工艺下拉列表
  const getProcessList = async (val: any) => {
    const data = { productId: val, active: true };
    const res: any = await getPlanProcessList(data);
    const datas = res.data.map((item: any) => {
      return {
        ...item,
        label: item.activeVersion ? item.name + '-' + item.activeVersion : item.name, //label拼接版本号
        value: item.id,
      };
    });
    processList.value = datas;
    // myFormRef.value?.updateSchema({
    //   field: 'processId',
    //   componentProps: {
    //     options: datas,
    //   },
    // });
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

  // 编辑弹窗保存
  const updateTableData = async (params: any, id: any, productDate: any, intervalDuration: any) => {
    // 弹框确定按钮（不需要顺延时间）
    if (!intervalDuration) {
      const index = dataSource.value.findIndex((item: any) => item.id === id);
      dataSource.value.splice(index, 1, params);
    }
    // 后续计划顺延按钮
    else {
      const index = dataSource.value.findIndex((item: any) => item.id === id);
      dataSource.value.splice(index, 1, params);
      const res = await myFormRef.value?.validate();
      const beforeDataSource = dataSource.value.filter(item => item.id <= index + 1);
      let extensionDataSource = dataSource.value.filter(item => item.id > index + 1);
      extensionDataSource = extensionDataSource.map((item: any) => {
        return {
          ...item,
          // productDate:'2000-01-01',
          productDate: addOneDay(params.productDate, res.intervalDuration * (item.id - index - 1)), //生产时间,依次递增
        };
      });
      dataSource.value = [...beforeDataSource, ...extensionDataSource];
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
  onMounted(() => {
    getProductList();
    getPlanTypeList();
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
    // width: 100%;
    height: calc(100% - 56px);
    // height:200px;
    // display: flex;
    background-color: var(--bmos-primary-color-white);
    padding-top: 5px;
    box-sizing: border-box;
    padding: 16px;
    display: flex;
    flex-direction: column;
    .batch-table {
      flex: 1;
      overflow-y: hidden;
    }
  }
  :deep(.mes-picker) {
    width: 100%;
  }
  // 后缀'天'间隔
  :deep(.mes-input-group .mes-input-group-addon:last-child) {
    padding: 0px 20px;
  }
  :deep(.mes-input-number) {
    width: 140%;
  }
  .flow {
    width: 100%;
    height: calc(100vh - 280px);
  }
</style>
