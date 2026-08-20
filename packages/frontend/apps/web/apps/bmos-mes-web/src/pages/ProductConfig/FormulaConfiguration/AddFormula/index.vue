<template>
  <div class="process-flow-container">
    <Row class="header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item @click="Back">{{ title1 }}</breadcrumb-item>
          <breadcrumb-item>{{ title2 }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button
            v-if="queryData.status !== 'viewVersion' && queryData.status !== 'formulaApprove'"
            type="primary"
            @click="Save">
            {{ t('保存') }}
          </Button>
          <Button @click="Back">{{ t('返回') }}</Button>
          <ApprovalBtns
            :settings="settings"
            :taskId="queryData.taskId"
            :deploymentId="queryData.deploymentId"
            :nodeId="queryData.elementKey"
            :executionId="queryData.executionId"
            :processInstanceId="queryData.processInstanceId"
            @action="action" />
        </Space>
      </Col>
    </Row>
    <div class="setting">
      <!-- 上方表单 -->
      <BMForm ref="myFormRef" v-bind="formProps"></BMForm>
      <div class="batch-table">
        <BMTable
          ref="tableInstance"
          :dataSource="dataSource"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :headerTitle="t('生产BOM物料')"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="{
            pageSize: 20,
          }"
          :formProps="formPropsTable">
          <template #toolbar>
            <Button
              v-if="queryData.status !== 'formulaApprove' && queryData.status !== 'viewVersion'"
              type="primary"
              @click="lookOrEdit({}, 'add')">
              {{ t('新增物料') }}
            </Button>
          </template>
        </BMTable>
        <!-- 编辑查看新增弹框 待写 -->
        <lookOrEditModal
          ref="editPlanRef"
          :rowData="rowData"
          :type="type"
          :index="index"
          :dataSource="dataSource"
          @addTableData="addTableData"
          @updateTableData="updateTableData"></lookOrEditModal>
        <!-- 新增时需要数据权限弹框 -->
        <PermissionModal
          v-model:permissionOpen="permissionModalOpen"
          :processId="''"
          :type="false"
          @ok="savePermission" />
      </div>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import type { FormProps } from '@bmos/components';
  import { BMTable, TableColumn, BMForm } from '@bmos/components';
  import { reactive, ref, onMounted, computed, createVNode } from 'vue';
  import { Row, Col, Breadcrumb, BreadcrumbItem, Space, Button } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { useRoute, useRouter } from 'vue-router';
  import { omit } from '@bmos/utils';
  import lookOrEditModal from './components/lookOrEditModal.vue';
  import {
    reqFormulaExtendUnit, //查询物料绑定拓展单位
    reqProductMaterialProductTreeReq,
    reqFormulaVersionDetail, //查版本详情
    getProductMaterialDetailApi,
    reqFormulaAddSave, //新增生产BOM
    reqFormulaVersionAddSave, //新增生产BOM版本(保存按钮调用)
    reqFormulaVersionEditSave, //编辑生产BOM版本(保存按钮调用)
  } from '@/services';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { FormItem, FormItemRest, InputGroup, Select, InputNumber, message, Modal } from 'ant-design-vue';
  import { loopTree } from './utils';
  import PermissionModal from '../components/PermissionModal.vue';
  import ApprovalBtns from '@/components/Approval/components/ApprovalBtns/index.vue'; //审核
  import { isObject } from '@bmos/utils';

  const settings = computed(() => {
    try {
      return JSON.parse(queryData.settings || {});
    } catch (error) {
      return {};
    }
  });
  // 获取路由上的 query 参数
  const route = useRoute();
  const router = useRouter();
  const queryData = route.query;
  const title1 = ref(t('生产BOM配置'));
  const title2 = ref();
  const tableInstance = ref<any>();
  const editPlanRef = ref();
  const productTreeData = ref([]); //上方表单产品树
  const rowData = ref();
  const type = ref();
  const index = ref<any>(); //表格index
  const myFormRef = ref();
  const curMaterialInfo = ref<any>({});
  const unitOptions = ref<any>([]); //批量-单位
  // 数据权限modal
  const permissionModalOpen = ref<boolean>(false);
  // 编辑/查看/新增
  const lookOrEdit = async (row: any, actionType: string) => {
    type.value = actionType;
    editPlanRef.value.openModal();
    rowData.value = row;
  };

  // 返回
  const Back = () => {
    if (queryData.status === 'viewVersion' || queryData.status === 'formulaApprove') {
      //查看版本和生产BOM审核时直接返回
      router.go(-1);
    } else {
      // 点击返回会给弹框提示
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对该生产BOM进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button onClick={() => noSaveBack()}>{t('不保存')}</Button>
                <Button type='primary' onClick={() => saveFunAndBack()}>
                  {t('保存')}
                </Button>
              </Space>
            </>
          );
        },
      });
    }
  };
  // 取消
  const cancelModal = () => {
    Modal.destroyAll();
  };
  // 不保存
  const noSaveBack = () => {
    cancelModal();
    router.go(-1);
  };
  // 弹框里的保存
  const saveFunAndBack = async () => {
    Modal.destroyAll();
    const res: any = await myFormRef.value?.validate();
    try {
      const data = {
        ...res,
        materialList: dataSource.value,
      };
      if (data.materialList.length == 0) {
        return message.error(t('生产BOM需至少包含一种生产BOM物料'));
      }
      // 如果是新增生产BOM,走出现部门授权弹框里调用保存 savePermission方法
      if (queryData.status === 'addFormula') {
        permissionModalOpen.value = true;
        return;
      }
      // 新增版本
      if (queryData.status === 'addVersion') {
        const data2 = { ...data, productFormulaId: queryData.formulaId };
        await reqFormulaVersionAddSave(data2);
        message.success(t('新增版本成功'));
        router.go(-1);
      }
      // 编辑版本
      if (queryData.status === 'editVersion') {
        const data2 = { ...data, id: queryData.versionId };
        await reqFormulaVersionEditSave(data2);
        message.success(t('编辑版本成功'));
        router.go(-1);
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 保存按钮
  const Save = async () => {
    const res: any = await myFormRef.value?.validate();
    const data = {
      ...res,
      materialList: dataSource.value,
    };
    if (data.materialList.length == 0) {
      return message.error(t('生产BOM需至少包含一种生产BOM物料'));
    }
    // 如果是新增生产BOM,走出现部门授权弹框里调用保存 savePermission方法
    if (queryData.status == 'addFormula') {
      Modal.confirm({
        title: t('提示'),
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否保存产品生产BOM'),
        async onOk() {
          permissionModalOpen.value = true;
        },
      });
    }
    // 版本编辑
    try {
      // 新增版本
      if (queryData.status == 'addVersion') {
        const data2 = { ...data, productFormulaId: queryData.formulaId };
        await reqFormulaVersionAddSave(data2);
        message.success(t('新增版本成功'));
        router.go(-1);
      }
      // 编辑版本
      if (queryData.status == 'editVersion') {
        const data2 = { ...data, id: queryData.versionId };
        await reqFormulaVersionEditSave(data2);
        message.success(t('编辑版本成功'));
        router.go(-1);
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 数据权限弹框所选确定按钮(新增生产BOM)
  const savePermission = async (deptIds: any) => {
    const res: any = await myFormRef.value?.validate();
    const data = {
      ...res,
      materialList: dataSource.value,
      deptIds,
    };
    try {
      await reqFormulaAddSave(data);
      message.success(t('新增成功'));
      router.go(-1);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 表单属性
  const formProps = reactive<any>({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 80,
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
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('BOM名称'),
        required: true,
        labelWidth: '90px',
        componentProps: {
          disabled: queryData.status !== 'addFormula',
        },
      },
      {
        field: 'versionNo',
        component: 'Input',
        required: true,
        label: t('版本号'),
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        required: true,
        label: t('产品'),
        componentProps: ({ formInstance }: any) => {
          return {
            showSearch: true,
            treeNodeFilterProp: 'showName', //搜索相关
            disabled: queryData.status !== 'addFormula',
            placeholder: t('请选择产品'),
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            treeData: productTreeData.value || [],
            onChange: (value: string) => {
              formInstance.setFormModels({
                batchQuantity: undefined,
                unitId: undefined,
              });
              // 物料名称改变时候查单位
              setCodeAndSpecification(value);
            },
          };
        },
      },
      {
        field: 'sendBackList',
        noLabel: true,
        colProps: {
          span: 8,
        },
        // required: true,
        labelWidth: '10px',
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        component: ({ formModel }: any) => {
          return (
            <>
              <Row>
                <Col span={24}>
                  <FormItem
                    name={['batchQuantity']}
                    labelCol={{ style: { width: '80px' } }}
                    label={t('批量')}
                    rules={[
                      {
                        required: true,
                        message: t('请输入批量'),
                      },
                      {
                        trigger: 'blur',
                        validator: async (_rule: any, value: string) => {
                          if (!value) return Promise.resolve();
                          // 输入正数
                          if (Number(value) <= 0) {
                            return Promise.reject(t('请输入正数'));
                          }
                          const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                          if (!reg.test(value)) {
                            return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                          }
                          if (!formModel['unitId']) {
                            return Promise.reject(t('请选择单位'));
                          }
                          return Promise.resolve();
                        },
                      },
                    ]}>
                    <InputGroup compact>
                      <InputNumber
                        v-model:value={formModel['batchQuantity']}
                        stringMode={true}
                        style={{ width: '70%' }}
                        placeholder={t('请输入批量')}
                      />
                      <FormItemRest>
                        <Select
                          v-model:value={formModel['unitId']}
                          style={{ width: '30%' }}
                          fieldNames={{
                            label: 'name',
                            value: 'unitId',
                          }}
                          onChange={() => {
                            myFormRef.value?.validateFields([['batchQuantity']]);
                          }}
                          placeholder={t('请选择单位')}
                          dropdownMatchSelectWidth={300}
                          options={unitOptions.value}>
                          {{
                            option: (option: any) => {
                              return (
                                <div class='flex-between'>
                                  <span>{option.name}</span>
                                  <span class='fourth-level-text'>{option.expression}</span>
                                </div>
                              );
                            },
                          }}
                        </Select>
                      </FormItemRest>
                    </InputGroup>
                  </FormItem>
                </Col>
              </Row>
            </>
          );
        },
      },
      {
        field: 'description',
        component: 'Input',
        label: t('版本描述'),
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
  const dataSource = ref<any>([]);

  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      hideInSearch: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      hideInSearch: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      hideInSearch: true,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      hideInSearch: true,
      customRender: ({ record }) => (
        <div>
          {record.materialType?.value == 0 || record.materialType == 0
            ? t('原辅包')
            : record.materialType?.value == 1 || record.materialType == 1
            ? t('中间品')
            : ''}
        </div>
      ),
    },
    {
      title: t('数量'),
      dataIndex: 'quantity',
      hideInSearch: true,
      customRender: ({ record }) => <div>{record.quantity == 0 ? '-' : record.quantity}</div>,
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: queryData.status !== 'viewVersion' && queryData.status !== 'formulaApprove',
          onClick: (e: any) => {
            index.value = e.index;
            lookOrEdit(record, 'edit');
          },
        },
        {
          label: t('查看'),
          onClick: () => {
            lookOrEdit(record, 'look');
          },
        },
        {
          label: t('删除'),
          ifShow: queryData.status !== 'viewVersion' && queryData.status !== 'formulaApprove',
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  dataSource.value = dataSource.value.filter(
                    (item: any) => item?.materialMergeCode !== record.materialMergeCode,
                  );
                  message.success(t('删除成功'));
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error);
                  return Promise.reject();
                }
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];
  // 判断status来源
  const determineStatus = () => {
    switch (queryData.status) {
      case 'addFormula':
        title2.value = t('新增生产BOM');
        break;
      case 'addVersion':
        title2.value = t('新增版本');
        break;
      case 'editVersion':
        title2.value = t('编辑生产BOM');
        break;
      case 'viewVersion':
        title2.value = t('查看生产BOM');
        myFormRef.value?.setFormProps({
          disabled: true,
        });
        break;
      case 'formulaApprove':
        title1.value = t('生产BOM审核');
        title2.value = t('审核处理');
        myFormRef.value?.setFormProps({
          disabled: true,
        });
        break;
      default:
        title1.value = t('产品生产BOM');
        break;
    }
  };
  // 处理后端枚举
  const handleEnum = (data: any) => {
    return isObject(data) ? data?.value : data;
  };
  //回显页面数据
  const echoData = async () => {
    // 若为新增生产BOM
    if (queryData.status == 'addFormula') {
      myFormRef.value?.setFieldsValue({});
      dataSource.value = [];
      if (queryData.treeNodeId && !queryData.categoryFlag) {
        myFormRef.value?.setFormModels({ productId: queryData.treeNodeId });
        setCodeAndSpecification(queryData.treeNodeId);
      }
      return;
    }
    try {
      const data = {
        versionId: queryData.versionId,
      };
      const res: any = await reqFormulaVersionDetail(data);
      // myFormRef.value?.setFieldsValue(res.data);//由于有未定义field的值,所以用setFormModels赋值
      const formData = omit(res.data, ['materialList']);
      myFormRef.value?.setFormModels(formData);
      dataSource.value = res.data.materialList?.map((item: any) => {
        return {
          ...item,
          dryPureType: handleEnum(item.dryPureType),
          materialType: handleEnum(item.materialType),
          quantityType: handleEnum(item.quantityType),
          unpackingToleranceType: handleEnum(item.unpackingToleranceType),
          chargeMixtureToleranceType: handleEnum(item.chargeMixtureToleranceType),
          oddmentToleranceType: handleEnum(item.oddmentToleranceType),
          liquidMeasureToleranceType: handleEnum(item.liquidMeasureToleranceType),
          oddLiquidMeasureToleranceType: handleEnum(item.oddLiquidMeasureToleranceType),
        };
      });
      setCodeAndSpecification(formData.productId);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 获取上方表单产品
  const getProductTreeData = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      productTreeData.value = loopTree(data) || [];
    } catch (error) {}
  };

  // (产品改变)物料名称改变时候查对应的单位
  // 物料名称改变时候查对应的单位
  const setCodeAndSpecification = async (value: any) => {
    try {
      if (!value) {
        unitOptions.value = [];
        return;
      }
      const { data } = await getProductMaterialDetailApi({
        id: value,
      });
      curMaterialInfo.value = data;
      getUnitOptions(value, data.unitId);
    } catch (error) {
      unitOptions.value = [];
    }
  };

  const getUnitOptions = async (unitId: string, unitId2: string) => {
    try {
      const extendRes = await reqFormulaExtendUnit({ materialId: unitId });
      (extendRes.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.name = item.extendUnitName;
      });
      unitOptions.value = [
        {
          unitId: unitId2,
          expression: t('标准单位'),
          name: curMaterialInfo.value.unitName,
          isUnit: true,
        },
        ...extendRes.data,
      ];
    } catch (error) {}
  };
  // 表格新增
  const addTableData = (params: any) => {
    dataSource.value.push({
      ...params,
    });
  };
  // 表格编辑
  const updateTableData = (params: any, index: any) => {
    dataSource.value.splice(index, 1, { ...params });
  };
  // 审核的action
  const action = () => {
    router.go(-1);
  };

  onMounted(() => {
    determineStatus();
    echoData();
    getProductTreeData();
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
    height: calc(100% - 56px);
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
</style>
