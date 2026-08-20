<template>
  <!-- 新增编辑页面 -->
  <div class="addManage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('设备管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t(modelName[state]) }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button v-if="state !== modalStatus.View" type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <div class="setting">
        <!-- 表单 -->
        <BMForm ref="myFormRef" v-bind="formProps">
          <template #selectA>
            <Form ref="formRef" :colon="false" :model="statusForm" layout="vertical">
              <div v-for="(item, index) in statusForm.statusList" :key="index" class="status-item">
                <div class="status-name">{{ t(item.name) }}</div>
                <div class="duration">
                  <FormItem :name="['statusList', index, 'time']" :label="t('默认效期时长')" :rules="rule">
                    <InputNumber v-model:value="item.time.day" min="0" :precision="0"></InputNumber>
                    <div class="unit">{{ t('天') }}</div>
                    <InputNumber v-model:value="item.time.h" min="0" max="23" :precision="0"></InputNumber>
                    <div class="unit">{{ t('小时') }}</div>
                    <InputNumber v-model:value="item.time.min" min="0" max="59" :precision="0"></InputNumber>
                    <div class="unit">{{ t('分钟') }}</div>
                  </FormItem>
                </div>
                <FormItem
                  style="margin-left: 20px"
                  :name="['statusList', index, 'finishStatus']"
                  :label="t('初始状态')"
                  :rules="{ required: item.required, message: t('请选择初始状态'), trigger: 'change' }">
                  <RadioGroup v-model:value="item.finishStatus">
                    <Radio :value="true">{{ `${t('已')}${item.name}` }}</Radio>
                    <Radio :value="false">{{ `${t('未')}${item.name}` }}</Radio>
                  </RadioGroup>
                </FormItem>
              </div>
            </Form>
          </template>
        </BMForm>
        <!-- 设备信息表格 -->
        <BMTableTitle :title="t('设备信息')" />
        <div class="batch-table">
          <BMTable
            :columns="columns"
            :dataSource="tableData"
            row-key="id"
            :pagination="false"
            :search="false"
            :showToolBar="false"
            :scroll="{ x: 844, y: 400 }" />
        </div>
        <div class="button-add">
          <Button
            :disabled="props.state === modalStatus.View"
            :icon="h(PlusOutlined)"
            type="link"
            block
            @click="addSource">
            {{ t('新增') }}
          </Button>
        </div>
        <!-- 设备信息表格 -->
        <BMTableTitle :title="t('设备数据')" />
        <div class="batch-table">
          <BMTable
            :columns="columns2"
            :dataSource="tableData2"
            row-key="id"
            :pagination="false"
            :search="false"
            :showToolBar="false"
            :scroll="{ x: 844, y: 400 }" />
        </div>
        <div class="button-add">
          <Button
            :disabled="props.state === modalStatus.View"
            :icon="h(PlusOutlined)"
            type="link"
            block
            @click="addSource2">
            {{ t('新增') }}
          </Button>
        </div>
      </div>
    </BreadcrumbButton>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, Recordable, BMTableTitle, BMTable } from '@bmos/components';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { modalStatus, modelName } from '../../enum';
  import {
    getEquipmentCategoryList,
    getEquipmentTagTree,
    getQueryListDictDown,
    postEquipmentSave,
    putEquipmentUpdate,
    getMesUnitExtendListApi,
    getMesUnitListApi,
    reqExtendGetStandard,
  } from '@/services';
  import { FormItemRest, Tag, message, FormItem, Select, Input, Cascader } from 'ant-design-vue';
  import type { TableColumn } from '@bmos/components';
  import { InfoCircleOutlined } from '@ant-design/icons-vue';
  import { h } from 'vue';
  import { PlusOutlined } from '@ant-design/icons-vue';

  const emits = defineEmits(['back']);
  const props = withDefaults(
    defineProps<{
      state: modalStatus;
      treeList: Recordable;
      treeDataId: string;
      equipmentTagData: Object[];
      detailsRow: any;
    }>(),
    {},
  );
  // 判断默认效期不能为空
  const areAllValuesEmptyOrZero = (obj: any) => {
    return Object.values(obj).every(function (value) {
      return value == null || value === 0 || value === '';
    });
  };
  const rule = {
    required: true,
    validator: (rule: any, value: any) => {
      if (areAllValuesEmptyOrZero(value)) {
        return Promise.reject(t('效期时长不能为0'));
      }
      return Promise.resolve();
    },
  };
  const myFormRef = ref<any>();
  const selectedValues = ref<any>(); // 存设备类型多选树的ids
  const treeData = ref<any>();
  const formRef = ref<any>(null);
  const unitList = ref<any>([]); //单位
  // 状态表单
  const statusForm = ref<any>({
    statusList: [],
  });
  const formProps = reactive<any>({
    initialValues: {},
    disabled: props.state === modalStatus.View,
    labelWidth: 80,
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 3,
    alwaysShowLines: 3,
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'field1',
        noLabel: true,
        component: () => {
          return <BMTableTitle title={t('基础信息')} />;
        },
        colProps: {
          span: 24,
        },
      },
      {
        field: 'categoryId',
        label: t('设备分类'),
        component: 'TreeSelect',
        required: true,
        componentProps: () => {
          return {
            fieldNames: {
              children: 'children',
              label: 'name',
              value: 'id',
            },
            request: async () => {
              const setName = (tree: any) => {
                tree?.forEach((item: any) => {
                  item.name = `${item.code}-${item.name}`;
                  if (item.children && item.children.length) {
                    setName(item.children);
                  }
                });
              };
              const { data } = await getEquipmentCategoryList();
              setName(data);
              return data;
            },
            virtual: false,
            height: 200,
          };
        },
      },
      {
        field: 'name',
        label: t('设备名称'),
        component: 'Input',
        required: true,
      },
      {
        field: 'code',
        label: t('设备编号'),
        component: 'Input',
        required: true,
        componentProps: () => {
          return {
            disabled: props.state !== modalStatus.Add ? true : false,
          };
        },
      },
      {
        field: 'tagIdList',
        component: 'TreeSelect',
        label: t('设备类型'),
        required: true,
        componentProps: () => {
          return {
            multiple: true,
            showSearch: true,
            options: [],
            treeNodeFilterProp: 'name',
            fieldNames: {
              label: 'name',
              value: 'id',
              children: 'children',
            },
            request: async () => {
              try {
                const { data } = await getEquipmentTagTree();
                treeData.value = data;
                return data;
              } catch (error) {
                return [];
              }
            },
            onChange: (values: any) => {
              selectedValues.value = values;
              const selectedNodes = values.map((value: any) => {
                // 通过 value 在 treeData 中查找对应的节点对象
                const foundNode = findNodeByValue(treeData.value, value);
                return foundNode || value; // 如果找不到节点对象，返回 value
              });
              // 1.去重展示设备状态
              let temp = selectedNodes
                ?.map((item: any) => item.statusPropertyList)
                .reduce((acc: any, item2: any) => acc.concat(item2), []);
              temp = [...new Set(temp.map((item: any) => item.code))].map(code =>
                temp.find((item: any) => item.code === code),
              ); //根据code去重
              // 回显去重后的设备状态表单
              statusForm.value.statusList = temp.map((item: any) => {
                return {
                  ...item,
                  time: {
                    day: 0,
                    h: 0,
                    min: 0,
                    second: 0,
                  },
                  finishStatus: '',
                };
              });
              // 2.去重展示设备信息
              let temp2 = selectedNodes
                ?.map((item: any) => item.infoPropertyList)
                .reduce((acc: any, item2: any) => acc.concat(item2), []);
              temp2 = [...new Set(temp2.map((item: any) => item.code))].map(code =>
                temp2.find((item: any) => item.code === code),
              ); //根据code去重
              tableData.value = temp2?.map((item: any) => {
                return {
                  ...item,
                  showName: item.name + '-' + item.code,
                  disabled: true,
                };
              });
              // 3.去重展示设备数据
              let temp3 = selectedNodes
                ?.map((item: any) => item.dataPropertyList)
                .reduce((acc: any, item2: any) => acc.concat(item2), []);
              temp3 = [...new Set(temp3.map((item: any) => item.code))].map(code =>
                temp3.find((item: any) => item.code === code),
              ); //根据code去重
              tableData2.value = temp3?.map((item: any) => {
                return {
                  ...item,
                  showName: item.name + '-' + item.code,
                  disabled: true,
                  value: null,
                };
              });
            },
          };
        },
      },
      {
        field: 'description',
        label: t('描述'),
        component: 'Input',
      },
      {
        field: 'field2',
        noLabel: true,
        colProps: {
          span: 24,
        },
        component: () => {
          return (
            <FormItemRest>
              <BMTableTitle title={t('设备状态')} />
              <Tag color={'#F2F3F4'} icon={<InfoCircleOutlined />} style={'color:#909398'}>
                {t('选择设备类型后显示对应信息和状态')}
              </Tag>
            </FormItemRest>
          );
        },
      },
      {
        field: 'tagEquipmentStatusDTOList',
        noLabel: true,
        defaultValue: [],
        vIf: () => statusForm.value.statusList?.length > 0,
        slot: 'selectA',
        colProps: {
          span: 24,
        },
      },
    ],
  });
  // 设备信息表格列
  const columns: TableColumn[] = [
    {
      title: t('属性名称'),
      dataIndex: 'code',
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.code}
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='showName'
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}
            disabled={record.disabled || props.state === modalStatus.View}
            onChange={(val: any, option: any) => {
              record.name = option.label;
              record.value = '';
              record.units = '';
            }}
            fieldNames={{
              label: 'showName',
              value: 'value',
            }}
            options={infoNameOptions.value}></Select>
        </div>
      ),
    },

    {
      title: t('属性值'),
      dataIndex: 'value',
      customRender: ({ record }) => {
        return record?.code === 'WEIGHING_UNIT_001' || record?.code === 'CONTAINER_WEIGHT_UNIT_013' ? (
          <Cascader
            style={{ width: '100%' }}
            v-model:value={record.units}
            options={record?.unitList || unitList.value}
            fieldNames={{ label: 'unitName', value: 'unitId' }}
            disabled={props.state === modalStatus.View}
            loadData={loadData}
            placeholder='请选择单位'
            onChange={(value: any) => {
              if (!value) {
                record.value = '';
                record.units = '';
                return;
              }
              record.value = value[1];
            }}></Cascader>
        ) : (
          <Input disabled={props.state === modalStatus.View} v-model:value={record.value} placeholder={t('请输入')} />
        );
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('删除'),
          danger: true,
          ifShow: !record.disabled,
          disabled: props.state === modalStatus.View,
          onClick: (e: any) => {
            tableData.value = tableData.value.filter((value: any, index: any) => index !== e.index);
          },
        },
      ],
    },
  ];
  const tableData = ref<any>([
    {
      code: undefined,
      name: '',
      value: null,
      embed: true,
    },
  ]);
  const tableData2 = ref<any>([
    {
      code: undefined,
      name: '',
      value: null,
      embed: true,
    },
  ]);
  //  设备信息表格的第一列下拉框数据
  const infoNameOptions = ref<any>([]);

  // 设备数据表格列
  const columns2: TableColumn[] = [
    {
      title: t('属性名称'),
      dataIndex: 'code',
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.code}
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='showName'
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}
            disabled={record.disabled || props.state === modalStatus.View}
            onChange={(val: any, option: any) => {
              record.name = option.label;
            }}
            fieldNames={{
              label: 'showName',
              value: 'value',
            }}
            options={dataNameOptions.value}></Select>
        </div>
      ),
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('删除'),
          ifShow: !record.disabled,
          disabled: props.state === modalStatus.View,
          danger: true,
          onClick: (e: any) => {
            tableData2.value = tableData2.value.filter((value: any, index: any) => index !== e.index);
          },
        },
      ],
    },
  ];

  //  设备数据表格的第一列下拉框数据
  const dataNameOptions = ref<any>([]);
  // 新增设备信息
  const addSource = () => {
    const params = {
      code: undefined,
      name: '',
      value: null,
      embed: true,
    };
    tableData.value?.push(params);
  };
  // 新增设备数据
  const addSource2 = () => {
    const params = {
      code: undefined,
      name: '',
      value: null,
      embed: true,
    };
    tableData2.value?.push(params);
  };
  // 懒加载查扩展单位
  const loadData = (selectedOptions: any) => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    // // load options lazily
    getMesUnitExtendListApi(targetOption.unitId).then((res: any) => {
      targetOption.loading = false;
      (res.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.unitName = `${item.extendUnitName}(${item.expression})`;
        item.name = item.extendUnitName;
        item.parentId = targetOption.unitId;
      });
      targetOption.children = [
        {
          unitId: targetOption.unitId,
          unitName: `${targetOption.unitName}(${t('标准单位')})`,
          name: targetOption.unitName,
          isUnit: true,
        },
        ...res.data,
      ];
      unitList.value = [...unitList.value];
    });
  };
  //返回
  const back = () => {
    emits('back');
  };

  // 保存
  const save = async () => {
    const res = await myFormRef.value?.validate();
    await formRef.value?.validate();
    //存状态数组
    const temp = statusForm.value.statusList?.map((item: any) => {
      return {
        ...item,
        value: Object.values(item.time).join(','),
      };
    });
    let flag = true; //判断必填是否填完
    tableData.value?.forEach((item: any) => {
      if (item.required && !item.value) {
        message.error(`${item.name}${t('属性必填')}`);
        flag = false;
      }
    });
    if (!flag) return;
    try {
      const data = {
        ...res,
        tagEquipmentStatusDTOList: temp, //设备状态
        equipmentPropertyDTOList: tableData.value.filter((item: any) => item.code) || [], //设备信息
        equipmentDataPropertyDTOList: tableData2.value.filter((item: any) => item.code) || [], //设备数据
      };
      if (props.state === modalStatus.Add) {
        //新增
        await postEquipmentSave(data);
        message.success(t('新增成功'));
        back();
      } else {
        await putEquipmentUpdate({ ...data, id: props.detailsRow.id });
        message.success(t('编辑成功'));
        back();
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 树多选时候去查到对应的整个node节点
  const findNodeByValue = (nodes: any, value: any) => {
    for (let i = 0; i < nodes?.length; i++) {
      const node = nodes[i];
      if (node.id === value) {
        return node;
      }
      if (node.children) {
        const foundNode: any = findNodeByValue(node.children, value);
        if (foundNode) {
          return foundNode;
        }
      }
    }
    return null;
  };
  // 获取设备信息表格第一列下拉框总数据
  const getInfoNameOptions = async () => {
    try {
      const { data } = await getQueryListDictDown({ dictId: '160010002001' });
      infoNameOptions.value = data?.map((item: any) => {
        return {
          ...item,
          showName: item.label + '-' + item.value,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 获取设备数据表格第一列下拉框总数据
  const getDataNameOptions = async () => {
    try {
      const { data } = await getQueryListDictDown({ dictId: '160010002002' });
      dataNameOptions.value = data?.map((item: any) => {
        return {
          ...item,
          showName: item.label + '-' + item.value,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查看编辑时的回显
  const echoData = () => {
    myFormRef.value?.setFormModels({
      categoryId: props.detailsRow.categoryId,
      name: props.detailsRow.name,
      code: props.detailsRow.code,
      tagIdList: props.detailsRow.tagIdList?.map((item: any) => item.id),
      description: props.detailsRow.description,
    });
    statusForm.value.statusList = props.detailsRow.statusPropertyList?.map((item: any) => {
      const temp2 = item.value.split(',');
      return {
        ...item,
        time: {
          day: temp2[0],
          h: temp2[1],
          min: temp2[2],
          second: temp2[3],
        },
      };
    });
    tableData.value =
      props.detailsRow?.infoPropertyList ||
      []?.map((item: any) => {
        return {
          ...item,
          disabled: true, //回显的属性名称不可删除
        };
      });
    tableData.value.forEach(async (item: any) => {
      let temp: any;
      if (item?.code === 'WEIGHING_UNIT_001' || item?.code === 'CONTAINER_WEIGHT_UNIT_013') {
        const { data } = await reqExtendGetStandard({ id: item.value });
        temp = unitList.value?.find((item2: any) => (data?.parentUnitId || item.value) === item2.unitId);
        const temp2 = await getExtendUnit(temp.unitId);
        item.unitList = unitList.value.map((item3: any) => {
          if (item3.unitId === temp.unitId) {
            return {
              ...item3,
              isLeaf: false,
              children: [
                {
                  unitId: temp?.unitId,
                  unitName: `${temp.unitName}(${t('标准单位')})`,
                  name: temp.unitName,
                  isUnit: true,
                },
                ...temp2,
              ],
            };
          } else {
            return {
              ...item3,
              isLeaf: false,
              children: [],
            };
          }
        });
        item.units = [data?.parentUnitId || item.value, item.value];
      }
    });
    tableData2.value =
      props.detailsRow?.dataPropertyList ||
      []?.map((item: any) => {
        return {
          ...item,
          disabled: true, //回显的属性名称不可删除
        };
      });
  };
  const getExtendUnit = async (val: any) => {
    const { data } = await getMesUnitExtendListApi(val); //获取扩展单位
    return data.map((item: any) => {
      return {
        ...item,
        unitId: item.id,
        unitName: `${item.extendUnitName}(${item.expression})`,
        name: item.extendUnitName,
      };
    });
  };
  onMounted(async () => {
    getInfoNameOptions();
    getDataNameOptions();
    // 渲染单位
    try {
      const res = await getMesUnitListApi();
      unitList.value = res.data || [];
      unitList.value?.forEach((item: any) => {
        item.isLeaf = false;
        item.children = [];
      });
    } catch (error: any) {
      message.error(error.message);
    }
    switch (props.state) {
      case modalStatus.View:
        echoData();
        break;
      case modalStatus.Add:
        myFormRef.value?.setFormModels({
          categoryId: props.treeDataId,
        });

        break;
      case modalStatus.Edit:
        echoData();
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
    width: 100%;
    height: 100%;
    background-color: var(--bmos-primary-color-white);
    padding: 0px 12px 0px 12px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
  }
  :deep(.ems-table-cell) {
    overflow: visible;
  }
  .status-name {
    line-height: 86px;
    margin-left: 5px;
    margin-right: 20px;
  }
  .status-item {
    display: flex;
    align-items: start;
  }
  .duration {
    :deep(.ems-form-item .ems-form-item-control-input-content) {
      display: flex;
      align-items: center;
    }
    .unit {
      margin-left: 2px;
      margin-right: 10px;
    }
  }
  :deep(.ems-table-cell) {
    overflow: visible;
  }
  .button-add {
    width: 50px;
  }
</style>
