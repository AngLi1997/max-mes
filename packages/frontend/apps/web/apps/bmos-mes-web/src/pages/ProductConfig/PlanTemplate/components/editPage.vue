<!-- 新增编辑页面 -->
<template>
  <div class="addManage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('生产计划模板') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ breadcrumbTitle }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button v-if="type !== 'view'" type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <BMForm ref="formRef" v-bind="formProps"></BMForm>
      <div class="table">
        <BMTable
          :key="tableKey"
          row-key="id"
          :dataSource="tableData"
          :columns="columns"
          :search="false"
          :scroll="{ x: 1044, y: 400 }"
          :showRefresh="false"
          :defaultExpandAllRows="true"
          :expandedRowKeys="expandedRowKeys"
          :showSearchBorder="true"
          :pagination="false"
          :showIndex="true"
          @expand="onExpand">
          <template #toolbar>
            <Button type="primary" :disabled="props.type === 'view'" @click="addProcess">
              {{ t('新增工艺') }}
            </Button>
          </template>
          <template #expandedRowRender="{ record }">
            <BMTable
              :columns="columns2"
              :dataSource="record.procedureDurationList"
              :pagination="false"
              :search="false"
              :showToolBar="false"
              :showIndex="true"
              :scroll="{ x: 400, y: 300 }" />
          </template>
        </BMTable>
      </div>
    </BreadcrumbButton>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, formInstance, BMTable, TableColumn } from '@bmos/components';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import {
    getEffectiveProcessListTreeReq,
    getProcedureList,
    reqFactoryLineListByProcessVersion,
    reqGetDetailUsingGET,
    reqFormulaVersionDetail,
    reqPlanTemplateSave,
    reqPlanTemplateEdit,
    reqPlanTemplateDetail, //通过模板id获取详情,
    reqProcessRelationProcesses, //通过工艺id获取关联批次工艺
  } from '@/services';
  import {
    InputGroup,
    Select,
    Input,
    FormItemRest,
    Button,
    TreeSelect,
    Modal,
    message,
    InputNumber,
  } from 'ant-design-vue';
  import { loopSelectableNotValueTree, isNullOrUnDef } from '@bmos/utils';
  import { findNodeByValue } from '../utils';
  import { createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const formRef = ref<formInstance>();
  const emits = defineEmits(['back']);
  const breadcrumbTitle = ref<any>();
  const tableKey = ref<any>(0);
  const allowSave = ref<any>(true);
  const props = withDefaults(
    defineProps<{
      type: string;
      rowData: Object;
    }>(),
    {},
  );
  const treeData = ref<any>(); //工艺树
  const expandedRowKeys = ref<any>([]);
  const formProps = reactive<FormProps>({
    initialValues: {},
    baseColProps: {
      span: 8,
    },
    showActionButtonGroup: false,
    schemas: [
      {
        label: t('模板名称'),
        field: 'name',
        component: 'Input',
        required: true,
        componentProps: () => {
          return {
            disabled: props.type !== 'add',
          };
        },
      },
    ],
  });
  // 主表格列
  const columns: TableColumn[] = [
    {
      title: t('工艺名称'),
      dataIndex: 'processId',
      width: 280,
      resizable: true,
      customRender: ({ record }: any) => (
        <TreeSelect
          v-model:value={record.processId}
          style='width: 100%'
          popupClassName='processId-tree-select'
          placeholder={t('请选择')}
          treeNodeFilterProp='showName'
          listHeight={233}
          virtual={false}
          disabled={props.type === 'view'}
          showSearch
          allow-clear
          onChange={async (val: any, option: any) => {
            record.processVersion = '';
            record.processVersion2 = '';
            record.procedureDurationList = []; //先清空对应工序
            record.intervalDuration = ''; //清空工艺开始间隔时长
            record.productionLineId = undefined; //清空产线
            record.relationBatchSortList = [];
            record.relationProcessesList = [];
            record.reuseBatchNumber = false;
            if (!val) {
              //清空下拉时
              record.executionDuration = '';
              record.batchQuantity = ''; //默认数据为该工艺绑定生产BOM配置的批量
              record.unitName = '';
              record.unitId = '';
              return;
            }
            record.processName = option[0]; //工艺名称
            // 通过 id 在 treeData 中查找对应的节点对象
            const foundNode = findNodeByValue(treeData.value, val);
            record.processVersion2 = '';
            record.processVersion = foundNode.activeVersion; //回显版本
            try {
              const { data } = await reqFactoryLineListByProcessVersion({
                //查工艺对应的产线list
                id: foundNode.id,
                version: foundNode.activeVersion,
              });
              record.productionLineList = data;

              const { data: processDetail } = await reqGetDetailUsingGET({
                //查工艺完整详情
                processId: foundNode.id,
                version: foundNode.activeVersion,
              });
              const { data: formulaDetail } = await reqFormulaVersionDetail({
                //查配方版本详情
                versionId: processDetail?.productFormulaVersionId,
              });
              record.batchQuantity = formulaDetail.batchQuantity; //默认数据为该工艺绑定生产BOM配置的批量
              record.unitName = formulaDetail.unitName;
              record.unitId = formulaDetail.unitId;
              const { data: procedureDetail } = await getProcedureList({
                //查对应工序集合
                processId: foundNode.id,
                version: foundNode.activeVersion,
              });
              record.procedureDurationList = procedureDetail?.map((item: any, index: any) => {
                return {
                  name: item?.name,
                  procedureId: item?.procedureId,
                  sort: index,
                };
              });
              linkage();
            } catch (error: any) {
              message.error(error.message);
            }
          }}
          fieldNames={{
            label: 'showName',
            value: 'id',
          }}
          treeData={treeData.value}
        />
      ),
    },
    {
      title: t('工艺版本'),
      dataIndex: 'processVersion',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <Input
            style={record.processVersion2 && (record.processVersion === record.processVersion2 ? '' : 'color: red')}
            disabled
            v-model:value={record.processVersion}
            placeholder={t('请输入')}
          />
        );
      },
    },
    {
      title: t('工艺开始间隔时长(天)'),
      dataIndex: 'intervalDuration',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <InputNumber
            style='width: 100%'
            controls={false}
            min={0}
            precision={0}
            disabled={props.type === 'view'}
            v-model:value={record.intervalDuration}
            placeholder={t('请输入')}
          />
        );
      },
    },
    {
      title: t('工艺执行时长(天)'),
      dataIndex: 'executionDuration',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        if (record.processId) {
          const temp = record?.procedureDurationList
            ?.map((item: any) => {
              let finallyValue = undefined;
              if (!isNullOrUnDef(item.executionDuration)) {
                finallyValue = item.executionDuration + (isNullOrUnDef(finallyValue) ? 0 : finallyValue);
              }
              if (!isNullOrUnDef(item.intervalDuration)) {
                finallyValue = item.intervalDuration + (isNullOrUnDef(finallyValue) ? 0 : finallyValue);
              }
              return finallyValue;
            })
            .filter((item2: any) => !isNullOrUnDef(item2));
          const max = temp.length > 0 ? Math.max.apply(null, temp) : '';
          record.executionDuration = max;
        }
        return (
          <InputNumber
            min={0}
            precision={0}
            disabled
            v-model:value={record.executionDuration}
            placeholder={t('请输入')}></InputNumber>
        );
      },
    },
    {
      title: t('产线'),
      dataIndex: 'productionLineId',
      width: 220,
      resizable: true,
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.productionLineId}
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='showName'
            showSearch
            allow-clear
            getPopupContainer={triggerNode => triggerNode.parentNode}
            disabled={props.type === 'view'}
            fieldNames={{
              label: 'name',
              value: 'id',
            }}
            onChange={(val: any, option: any) => {
              if (!val) {
                record.productionLineId = undefined;
                record.productionLineName = '';
                record.productionLineCode = '';
                return;
              }
              record.productionLineName = option?.name;
              record.productionLineCode = option?.code;
            }}
            options={record?.productionLineList || []}></Select>
        </div>
      ),
    },
    {
      title: t('生产批量'),
      dataIndex: 'batchQuantity',
      width: 180,
      resizable: true,
      customRender: ({ record }: any) => (
        <InputGroup compact>
          <InputNumber
            v-model:value={record.batchQuantity}
            style={{ width: '65%' }}
            min={0}
            placeholder={t('请输入生产批量')}
            disabled={props.type === 'view'}
          />
          <FormItemRest>
            <Input v-model:value={record.unitName} style={{ width: '35%' }} disabled={true} />
          </FormItemRest>
        </InputGroup>
      ),
    },
    {
      title: t('关联生产批次'),
      dataIndex: 'relationBatchSortList',
      width: 250,
      resizable: true,
      customRender: ({ record }) => (
        <div>
          <Select
            v-model:value={record.relationBatchSortList}
            mode='multiple'
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='showName'
            disabled={props.type === 'view'}
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}
            fieldNames={{
              label: 'showName',
              value: 'reallyId',
            }}
            onDropdownVisibleChange={async (open: any) => {
              if (open) {
                //查关联生产批次下拉
                const { data: relationProcessesList } = await reqProcessRelationProcesses({
                  processId: record.processId,
                });
                let temp = tableData.value?.map((item: any) => {
                  return {
                    key: item.id, //唯一键
                    processId: item.processId,
                  };
                });
                temp = temp?.filter(
                  (item: any) => relationProcessesList?.map((item2: any) => item2.id).includes(item.processId),
                );
                const temp2 = temp?.map((item: any) => {
                  const temp3 = relationProcessesList?.find((item2: any) => item.processId == item2.id);
                  const Index = tableData.value.findIndex((item3: any) => item3.id == item.key);
                  return {
                    id: item.processId,
                    val: Index,
                    activeVersion: temp3?.activeVersion,
                    name: temp3?.name,
                    showName: Index + 1 + '-' + temp3?.name,
                    reallyId: item.key + '-' + item.processId, //下拉框唯一标识
                  };
                });
                record.relationProcessesList = temp2;
              }
            }}
            options={record.relationProcessesList}></Select>
        </div>
      ),
    },
    {
      title: t('生产批号沿用'),
      dataIndex: 'reuseBatchNumber',
      width: 150,
      resizable: true,
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.reuseBatchNumber}
            style='width: 100%'
            placeholder={t('请选择')}
            disabled={props.type === 'view'}
            allow-clear
            onChange={(val: any) => {
              if (val === undefined) {
                record.reuseBatchNumber = undefined;
              }
            }}
            options={[
              { label: t('是'), value: true },
              { label: t('否'), value: false },
            ]}></Select>
        </div>
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: () => [
        {
          label: t('上移'),
          disabled: props.type === 'view',
          onClick: (e: any) => {
            moveUp(e);
            linkage();
          },
        },
        {
          label: t('下移'),
          disabled: props.type === 'view',
          onClick: (e: any) => {
            moveDown(e);
            linkage();
          },
        },
        {
          label: t('删除'),
          danger: true,
          disabled: props.type === 'view',
          onClick: (e: any) => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                tableData.value = tableData.value?.filter((value, index) => index !== e.index);
                linkage();
              },
            });
          },
        },
      ],
    },
  ];
  // 子表格列
  const columns2: TableColumn[] = [
    {
      title: t('工序名称'),
      dataIndex: 'name',
      width: 120,
    },
    {
      title: t('工序开始间隔时长(天)'),
      dataIndex: 'intervalDuration',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <InputNumber
            style='width: 100%'
            controls={false}
            min={0}
            precision={0}
            disabled={props.type === 'view'}
            v-model:value={record.intervalDuration}
            placeholder={t('请输入')}
          />
        );
      },
    },
    {
      title: t('工序执行时长(天)'),
      dataIndex: 'executionDuration',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <InputNumber
            style='width: 100%'
            controls={false}
            min={0}
            precision={0}
            disabled={props.type === 'view'}
            v-model:value={record.executionDuration}
            placeholder={t('请输入')}
          />
        );
      },
    },
  ];
  const tableData = ref<any>([]);
  // 新增工艺
  const addProcess = () => {
    if (tableData.value?.length >= 50) {
      message.error(t('最多配置50个'));
      return;
    }
    const newData = {
      id: new Date().getTime(),
      processKey: new Date().getTime().toString(),
      batchQuantity: '',
      executionDuration: '',
      intervalDuration: '',
      procedureDurationList: [],
      processId: undefined,
      processVersion: '',
      productionLineId: undefined,
      relationBatchSortList: [],
      reuseBatchNumber: false, //是否沿用批号 //默认否
    };
    tableData.value.push(newData);
    expandedRowKeys.value = [...expandedRowKeys.value, newData.id];
  };
  // 展开/收缩
  const onExpand = (expanded: any, record: any) => {
    if (expanded) {
      // 设置展开窗Key，代表展开操作
      expandedRowKeys.value.push(record.id);
    } else {
      // 代表折叠操作
      if (expandedRowKeys.value.length) {
        expandedRowKeys.value = expandedRowKeys.value.filter((v: any) => {
          return v !== record.id;
        });
      }
    }
  };
  // 上移
  const moveUp = (e: any) => {
    const index = tableData.value.findIndex((item, index) => index === e.index);
    if (index === 0) return;
    const temp = tableData.value[index];
    tableData.value[index] = tableData.value[index - 1];
    tableData.value[index - 1] = temp;
  };
  // 下移
  const moveDown = (e: any) => {
    const index = tableData.value.findIndex((item, index) => index === e.index);
    if (index === tableData.value.length - 1) return;
    const temp = tableData.value[index];
    tableData.value[index] = tableData.value[index + 1];
    tableData.value[index + 1] = temp;
  };
  // 保存
  const save = async () => {
    const res = await formRef.value?.validate();
    if (tableData.value.length === 0) {
      message.error(t('工艺不能为空'));
      return;
    }
    allowSave.value = true;
    tableData.value.forEach((item: any) => {
      if (!item.processId) {
        allowSave.value = false;
        return message.error(t('请先选择工艺'));
      }
      if (!item.intervalDuration && item.intervalDuration !== 0) {
        allowSave.value = false;
        return message.error(t('工艺开始间隔时长不能为空'));
      }
      if (!item.executionDuration && item.executionDuration !== 0) {
        allowSave.value = false;
        return message.error(t('工艺执行时长不能为空'));
      }
      if (item.reuseBatchNumber === undefined) {
        allowSave.value = false;
        return message.error(t('生产批号沿用不能为空'));
      }
      if (item.procedureDurationList && item.procedureDurationList.length > 0) {
        item.procedureDurationList.forEach((item2: any) => {
          if (item2.intervalDuration !== 0 && !item2.intervalDuration) {
            allowSave.value = false;
            return message.error(t('工序开始间隔时长不能为空'));
          }
          if (item2.executionDuration !== 0 && !item2.executionDuration) {
            allowSave.value = false;
            return message.error(t('工序执行时长不能为空'));
          }
        });
      }
    });
    if (allowSave.value == false) return;
    const data = {
      batchList: tableData.value.map((item: any, index: any) => {
        return {
          ...item,
          relationBatchSortList: item.relationBatchSortList?.map((item: any) => {
            const temp = tableData.value?.findIndex((item1: any) => item1.id + '-' + item1.processId == item);
            return temp;
          }),
          sort: index,
        };
      }),
    };
    try {
      props.type === 'add'
        ? await reqPlanTemplateSave({ ...data, name: res.name })
        : await reqPlanTemplateEdit({ ...data, id: props.rowData.id });
      props.type === 'add' ? message.success(t('新增成功')) : message.success(t('编辑成功'));
      back();
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 返回
  const back = () => {
    emits('back');
  };
  // 联动更新关联生产批次
  const linkage = () => {
    tableData.value?.forEach((item: any) => {
      item?.relationProcessesList?.forEach((item2: any) => {
        const temp = tableData.value?.findIndex((item3: any) => item3.id + '-' + item3.processId == item2.reallyId);
        (item2.val = temp), (item2.showName = temp + 1 + '-' + item2.name);
      });
      item.relationProcessesList = item?.relationProcessesList?.filter((item1: any) => item1.val >= 0);
      const temp2 = item?.relationProcessesList?.map((item2: any) => item2.reallyId);
      item.relationBatchSortList = item.relationBatchSortList?.filter((item3: any) => temp2.includes(item3));
    });
  };
  // 回显数据
  const echoData = async () => {
    const { data } = await reqPlanTemplateDetail({ id: props.rowData.id });
    formRef.value?.setFormModels({
      name: data.name,
    });
    const temp: any = [];
    const promise = data?.templateBatchList?.map(async (item: any) => {
      const { data: productionLineList } = await reqFactoryLineListByProcessVersion({
        //查工艺对应的产线list
        id: item.processId,
        version: item.activeVersion || item.processVersion,
      });
      temp.push({
        ...item,
        relationBatchSortList: item.relationBatchSortList?.map((item1: any) => {
          const temp = item?.relationProcessesList?.find((item2: any) => item2.val == item1)?.reallyId;
          return temp;
        }),
        procedureDurationList: item?.procedureList, //工序数组对象
        productionLineList,
        processVersion2: item?.processVersion, //检查模版的工艺版本与当前生效版本是否一致
        processVersion: item?.activeVersion, //不一致时工艺版本为红色字体，以生效工艺版本数据呈现
        id: item?.processKey, //手动加唯一键
      });
    });
    await Promise.all(promise);
    tableData.value = temp?.sort((a: any, b: any) => a.sort - b.sort);
    expandedRowKeys.value = temp?.map((item: any) => item.id);
    tableKey.value++;
  };
  // 获取工艺树
  const getTreeData = async () => {
    try {
      const { data } = await getEffectiveProcessListTreeReq({ activeProcess: true, filterPermission: true });
      treeData.value = loopSelectableNotValueTree(data, 'isFlag', true);
    } catch (error) {}
  };
  onMounted(() => {
    getTreeData();
    switch (props.type) {
      case 'view':
        breadcrumbTitle.value = t('查看生产计划模板');
        echoData();
        break;
      case 'add':
        breadcrumbTitle.value = t('新增生产计划模板');
        break;
      case 'edit':
        breadcrumbTitle.value = t('编辑生产计划模板');
        echoData();
        break;
    }
  });
</script>
<style lang="less" scoped>
  .addManage {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    .table {
      flex: 1;
      overflow-y: scroll;
    }
    .container {
      padding: 0px;
    }
    :deep(.bmos-tool-bar) {
      padding: 0px 0px 12px 0px;
    }
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
</style>
