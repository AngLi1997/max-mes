<!-- 匹配采集点 -->
<template>
  <div class="task-planning-manage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('设备管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t('匹配采集点') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <div class="setting">
        <BMTable
          :dataSource="tableData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :pagination="false"
          :search="false"
          :scroll="{ x: 844, y: 400 }">
          <template #headerTitle>
            <div style="width: 300px">
              <BMForm ref="formRef" v-bind="formProps"></BMForm>
            </div>
          </template>
        </BMTable>
      </div>
    </BreadcrumbButton>
  </div>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    postEquipmentEquipmentIdAcquisitionPoint,
    getAcquisitionPointEnableByEquipmentDataProperty,
  } from '@/services';
  import { BMTable, TableColumn, FormProps, BMForm } from '@bmos/components';
  import { message, Select } from 'ant-design-vue';

  interface RowData {
    id: string;
    acquisitionPlatform?: null | { value: string; label: string; name: string };
    dataPropertyList?: Array<{ code: string; name: string }>;
  }

  const props = withDefaults(
    defineProps<{
      rowData: RowData;
    }>(),
    {},
  );
  const emit = defineEmits(['back']);

  const acquisitionPlatform = ref<string | undefined | null>('');
  const formRef = ref<InstanceType<typeof BMForm>>();
  const formProps: Ref<FormProps> = ref({
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'acquisitionPlatform',
        component: 'Select',
        label: t('数采平台'),
        required: true,
        componentProps: {
          options: [
            { value: 'hub', label: 'HUB' },
            { value: 'supCon', label: '中控' },
          ],
          clearable: false,
          onChange: async (val: string | undefined) => {
            acquisitionPlatform.value = val;
            await getAllPointNameList();
            tableData.value.forEach((item: any) => {
              item.value = undefined;
              item.acquisitionPointCode = undefined;
              item.acquisitionPointDataType = undefined;
              item.dataPointName = undefined;
            });
          },
        },
      },
    ],
  });
  //所有采集点名称下拉列表
  const allPointNameList = ref<any>([]);
  // 表格数据
  const tableData = ref<any>([]);
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('设备数据'),
      dataIndex: 'name',
    },
    {
      title: t('采集点名称'),
      dataIndex: 'name2',
      customRender: ({ record }: any) => (
        <div>
          <Select
            v-model:value={record.value}
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='name'
            showSearch
            onChange={(val: any, option: any) => {
              record.acquisitionPointCode = option.code;
              record.acquisitionPointDataType = option.dataType;
              record.dataPointName = option.dataPointName;
            }}
            fieldNames={{
              label: 'name',
              value: 'id',
            }}
            options={filterPointNameList(record.code)}></Select>
        </div>
      ),
    },
    {
      title: t('采集点编码'),
      dataIndex: 'acquisitionPointCode',
    },
    {
      title: t('数据类型'),
      dataIndex: 'acquisitionPointDataType',
      customRender: ({ record }: any) => {
        return (
          <div>
            {record.acquisitionPointDataType?.value === 'NUMBER'
              ? t('数值类型')
              : record.acquisitionPointDataType?.value === 'STRING'
              ? t('字符串类型')
              : record.acquisitionPointDataType?.value === 'DATETIME'
              ? t('时间类型')
              : '-'}
          </div>
        );
      },
    },
    {
      title: t('数据点位名称'),
      dataIndex: 'dataPointName',
    },
  ];
  // 返回管理页面
  const back = () => {
    emit('back');
  };
  // 从所有采集点列表中筛选每个设备数据对应的采集点列表
  const filterPointNameList = (code: any) => {
    const arr1: any = [];
    const arr2: any = [];
    allPointNameList.value?.forEach((item: any) => {
      if (item.equipmentTagDataCode === code) {
        arr1.push(item);
      }
      if (!item.equipmentTagDataCode) {
        arr2.push(item);
      }
    });
    return [...arr1, ...arr2];
  };
  // 保存
  const save = async () => {
    try {
      await formRef.value?.validate();
      const data = tableData.value?.map((item: any) => {
        return {
          code: item.code,
          value: item.value,
        };
      });
      const temp = data?.map((item: any) => item.value && item.value)?.filter((item2: any) => item2) ?? [];
      if (new Set([...temp]).size !== temp?.length) {
        message.error(t('不同设备数据不能匹配同个采集点'));
        return;
      }
      await postEquipmentEquipmentIdAcquisitionPoint(props.rowData.id, {
        acquisitionPlatform: acquisitionPlatform.value,
        equipmentBindAcquisitionVOS: data,
      });
      message.success(t('操作成功'));
      emit('back');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 获取所有采集点列表
  const getAllPointNameList = async () => {
    if (acquisitionPlatform.value) {
      const temp = props.rowData.dataPropertyList?.map((item: any) => item.code); //设备数据code集合
      const res = await getAcquisitionPointEnableByEquipmentDataProperty({
        codeSet: temp,
        acquisitionPlatform: acquisitionPlatform.value,
      });
      allPointNameList.value = res; //所有采集点列表
    }
    tableData.value = props.rowData.dataPropertyList?.map((item: any) => {
      return {
        ...item,
        name: item.name + '-' + item.code,
      };
    });
  };
  onMounted(async () => {
    acquisitionPlatform.value = props.rowData.acquisitionPlatform?.value || null;
    formRef.value?.setFieldsValue({
      acquisitionPlatform: acquisitionPlatform.value,
    });
    await getAllPointNameList();
  });
</script>

<style lang="less" scoped>
  .task-planning-manage {
    width: 100%;
    height: 100%;
  }
  .setting {
    width: 100%;
    height: calc(100% - 56px);
    background-color: var(--bmos-primary-color-white);
    padding: 12px 12px 0px 12px;
    display: flex;
    flex-direction: column;
    .batch-table {
      // flex: 1;
      // overflow-y: auto;
    }
  }
  :deep(.ems-table-cell) {
    overflow: visible;
  }
</style>
