<template>
  <div class="table-data">
    <div v-for="item in equipmentDataList" :key="item.itemIndex" class="table-item">
      <BMForm :ref="el => getFormRefs(el, item)" v-bind="formProps" />
    </div>
    <div class="add-icon-box">
      <span class="add-icon" @click="addEquipment">
        <PlusCircleOutlined style="margin-right: 6px" />
        {{ t('新增设备') }}
      </span>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMForm, formInstance, RenderCallbackParams } from '@bmos/components';
  import { Button } from 'ant-design-vue';
  import { PlusCircleOutlined } from '@ant-design/icons-vue';
  import { reqEquipmentStationTreeEquipment, getEquipmentInfo } from '@/services';

  const itemIndex = ref(0);
  const props = defineProps({
    equipmentDataList: {
      type: Object,
      default: () => ({}),
    },
  });
  const emit = defineEmits(['change']);

  const formRefs = ref<{
    [key: string]: formInstance;
  }>({});
  const getFormRefs = (el: any, item: any) => {
    if (el) {
      formRefs.value[item.itemIndex] = el;
    }
  };
  const loopTree = (data: any) => {
    return data?.map((item: any) => {
      if (item.children && item.children?.length > 0) {
        item.children = [...item.children, ...(item.infoList || [])];
      } else {
        item.children = item?.infoList || [];
      }
      if (!item.infoList && item.infoList !== null) {
        //有此字段则为设备
        item.equipmentFlag = true;
        item.selectable = true;
      }
      if (item.children && item.children?.length > 0) {
        item.name = `${item.code}-${item.name}`;
        item.selectable = false;
        loopTree(item.children);
      }
      return item;
    });
  };
  const formProps = ref({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    schemas: [
      {
        component: 'TreeSelect',
        label: t('设备名称'),
        field: 'equipmentId',
        required: true,
        colProps: {
          span: 8,
        },
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            showSearch: true,
            treeNodeFilterProp: 'name',
            fieldNames: { label: 'name', value: 'id' },
            request: async () => {
              const res = await reqEquipmentStationTreeEquipment();
              const data = loopTree(res.data || []);
              return data;
            },
            onChange: async (value: any) => {
              formModel.equipmentDataPropertyCode = '';
              // 获取设备数据
              const data = await getEquipmentData(value);
              formInstance.updateSchema({
                field: 'equipmentDataPropertyCode',
                componentProps: {
                  options: data,
                },
              });
              const newData = props.equipmentDataList.map((item: any) => {
                if (item.itemIndex === formModel.itemIndex) {
                  item.equipmentId = value;
                  item.equipmentDataPropertyCode = '';
                }
                return item;
              });
              emit('change', newData);
            },
          };
        },
      },
      {
        component: 'Select',
        label: t('设备数据'),
        field: 'equipmentDataPropertyCode',
        required: true,
        colProps: {
          span: 8,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: { label: 'name', value: 'code' },
            onChange: (value: any) => {
              const newData = props.equipmentDataList.map((item: any) => {
                if (item.itemIndex === formModel.itemIndex) {
                  item.equipmentDataPropertyCode = value;
                }
                return item;
              });
              emit('change', newData);
            },
          };
        },
      },
      {
        field: 'itemIndex',
        colProps: {
          span: 8,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <div class='delete-button'>
                <Button
                  type='link'
                  onClick={() => {
                    deleteItem(formModel.itemIndex);
                  }}>
                  {t('删除')}
                </Button>
              </div>
            </>
          );
        },
      },
    ],
  });

  const deleteItem = (key: string) => {
    const newData: any[] = props.equipmentDataList.filter((item: any) => item.itemIndex !== key);
    emit('change', newData);
  };
  // 新增设备
  const addEquipment = () => {
    itemIndex.value += 1;
    const item = {
      equipmentDataPropertyCode: '',
      equipmentId: '',
      itemIndex: `b${itemIndex.value}`,
    };
    const newData = [...(props.equipmentDataList as any[]), item];
    emit('change', newData);
  };

  // 获取设备数据
  const getEquipmentData = async (equipmentId: string) => {
    const res = await getEquipmentInfo(equipmentId);
    return res.data.dataPropertyList || [];
  };

  watch(
    () => props.equipmentDataList,
    val => {
      nextTick(() => {
        val.forEach(async (item: any) => {
          formRefs.value[item.itemIndex]?.setFormModels(item);
          if (item.equipmentId) {
            // 获取设备数据
            const data = await getEquipmentData(item.equipmentId);
            formRefs.value[item.itemIndex]?.updateSchema({
              field: 'equipmentDataPropertyCode',
              componentProps: {
                options: data,
              },
            });
          }
        });
      });
    },
    {
      deep: true,
    },
  );
  const validateForm = async () => {
    try {
      const formRefList = Object.values(formRefs.value);
      const validateResult = await Promise.all(formRefList.map((formInstance: any) => formInstance?.validate()));
      return Promise.resolve(validateResult);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  defineExpose({
    validateForm,
  });
</script>

<style lang="less" scoped>
  .table-data {
    .table-item {
      width: 100%;
      height: 60px;
      border-bottom: 1px solid #e1e3e5;
      :deep(.delete-button) {
        width: 100%;
        display: flex;
        justify-content: flex-end;
      }
    }
    .add-icon-box {
      margin-top: 16px;
    }
    .add-icon {
      color: #2871ff;
      cursor: pointer;
      font-size: 14px;
    }
  }
</style>
