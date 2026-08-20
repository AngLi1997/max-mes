<template>
  <BMLayout>
    <BMBasicPage :title="t('物料接收')" :default-padding="false" @left-click="toBack" @cancel="toBack" @confirm="submit">
      <BMForm ref="infoFormRef" v-bind="infoFormProps" />
      <BMFormTitle>{{ t('入库量') }}</BMFormTitle>
      <template v-for="item in inboundModel.inboundList" :key="item.key">
        <BMForm :ref="(el) => setFormRefs(el, item)" v-bind="inboundListFormProps" />
      </template>
      <view class="flex-center" @click="addInboundList">
        <wd-icon class-prefix="bmos-app-icon" name="tianjia" size="14.07rpx" color="#2871FF" />
        <wd-button type="text" style="margin: 0">
          {{ t('添加') }}
        </wd-button>
      </view>
      <BMFormTitle>{{ t('入库信息') }}</BMFormTitle>
      <BMForm ref="inboundInfoFormRef" v-bind="inboundInfoFormProps" />
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :label-list="labelList"
      :title="t('签名确认')"
      :signature-data="curParams"
      @confirm="confirmSignPopup"
    />
    <!-- 选择货位弹窗 -->
    <BMTreeModal
      v-model="positionId"
      v-model:open="showPositionModal"
      :title="t('暂存货位')"
      :tree-data="treePositionData"
      :field-names="{
        name: 'name',
        key: 'id',
        checkKey: 'level.value',
        checkKeyValue: 4,
        parentId: 'parentId',
        children: 'children',
      }"
      @confirm="confirmPosition"
    />
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="handleWeigh" @choose-printer-confirm="choosePrinterConfirm" />
  </BMLayout>
</template>

<script setup lang="jsx">
import {
  getMesUnitListDownExtendBound,
  getProductTreeApi,
  getStorageConfigTreeApi,
  reqMaterialFieldInfo,
  reqProductMaterialDetail,
  reqStorageMaterialBatchFieldList,
  reqStorageMaterialBatchListByMaterialId,
  reqStorageMaterialMangeQueryBatchDetail,
  reqStorageMaterialPrintStorageMaterialTagBatch,
  reqStorageMaterialReceiveMobile,
  scanWeighPositionCodeApi,
} from '@/api';
import { BMBasicPage, BMForm, BMFormTitle, BMLayout, BMSignModal, BMTreeModal } from '@/BMComponents/index.js';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { isEmpty } from 'lodash-es';
import { nextTick, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import Icon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

// 返回
const toBack = () => {
  uni.navigateBack();
};

const { bmosScanCode, init } = useScan();
// #ifdef APP-PLUS
init();
// #endif

const infoFormRef = ref(null);

const getMaterialTreeModalData = async (type) => {
  try {
    if (isEmpty(type)) {
      infoFormRef.value?.updateSchema({
        field: 'materialId',
        componentProps: {
          'tree-data': [],
        },
      });
      return;
    }
    const { data } = await getProductTreeApi({
      categoryType: type,
    });
    infoFormRef.value?.updateSchema({
      field: 'materialId',
      componentProps: {
        'tree-data': data,
      },
    });
  }
  catch (error) {
    console.log(error);
    infoFormRef.value?.updateSchema({
      field: 'materialId',
      componentProps: {
        'tree-data': [],
      },
    });
  }
};
const dyFieldList = ref([]);
const getDynamicField = async (materialId, batchNo) => {
  try {
    infoFormRef.value?.removeSchemaByFiled(dyFieldList.value.map(item => item.field));
    infoFormRef.value?.updateSchema([
      {
        field: 'originalBatchNo',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'productDate',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'expiredDate',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'hydration',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'noHydrationContent',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'reportNo',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'licenceNo',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'originalCode',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'supplier',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'producer',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'qualityStatus',
        componentProps: {
          disabled: false,
        },
      },
    ]);
    if (isEmpty(materialId)) {
      infoFormRef.value?.setFormModels({
        originalBatchNo: '',
        productDate: '',
        expiredDate: '',
        hydration: '',
        noHydrationContent: '',
        reportNo: '',
        licenceNo: '',
        originalCode: '',
        supplier: '',
        producer: '',
      });
      return;
    }

    if (isEmpty(batchNo)) {
      infoFormRef.value?.removeSchemaByFiled(dyFieldList.value.map(item => item.field));
      const { data: materialFieldList } = await reqMaterialFieldInfo(materialId);
      dyFieldList.value = materialFieldList.filter(item => item.fieldType === 'MaterialBatchCustomFields');
      const schemaList = materialFieldList.map((item) => {
        if (item.fieldType === 'MaterialBatchCustomFields') {
          return {
            field: item.field,
            component: 'Input',
            label: item.fieldName,
            defaultValue: '',
          };
        }
        return null;
      }).filter(item => item);
      infoFormRef.value?.setFormModels({
        originalBatchNo: '',
        productDate: '',
        expiredDate: '',
        hydration: '',
        noHydrationContent: '',
        reportNo: '',
        licenceNo: '',
        originalCode: '',
        supplier: '',
        producer: '',
      });
      infoFormRef.value?.appendSchemasByField(schemaList);
      return;
    }
    const { data: batchList } = await reqStorageMaterialBatchListByMaterialId({
      materialId,
      batchNo,
    });
    const curBatch = batchList?.find(item => item.materialBatchNo === batchNo);
    if (isEmpty(batchList) || !curBatch) {
      infoFormRef.value?.removeSchemaByFiled(dyFieldList.value.map(item => item.field));
      const { data: materialFieldList } = await reqMaterialFieldInfo(materialId);
      dyFieldList.value = materialFieldList.filter(item => item.fieldType === 'MaterialBatchCustomFields');
      const schemaList = materialFieldList.map((item) => {
        if (item.fieldType === 'MaterialBatchCustomFields') {
          return {
            field: item.field,
            component: 'Input',
            label: item.fieldName,
            defaultValue: '',
          };
        }
        else {
          return null;
        }
      }).filter(item => item);
      infoFormRef.value?.appendSchemasByField(schemaList);
      infoFormRef.value?.setFormModels({
        originalBatchNo: '',
        productDate: '',
        expiredDate: '',
        hydration: '',
        noHydrationContent: '',
        reportNo: '',
        licenceNo: '',
        originalCode: '',
        supplier: '',
        producer: '',
        qualityStatus: 'QUARANTINE',
      });
    }
    else if (curBatch) {
      const { data } = await reqStorageMaterialMangeQueryBatchDetail({
        id: curBatch.id,
      });
      infoFormRef.value?.setFormModels({
        originalBatchNo: data.factoryBatchNo,
        productDate: data.produceDate,
        expiredDate: data.expiredDate,
        hydration: data.hydration,
        noHydrationContent: data.noHydrationContent,
        reportNo: data.reportNo,
        licenceNo: data.licenceNo,
        originalCode: data.originalBatchNo,
        supplier: data.supplier,
        producer: data.producer,
        qualityStatus: data.qualityStatus?.value,
      });
      infoFormRef.value?.updateSchema([
        {
          field: 'originalBatchNo',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'productDate',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'expiredDate',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'hydration',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'noHydrationContent',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'reportNo',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'licenceNo',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'originalCode',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'supplier',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'producer',
          componentProps: {
            disabled: true,
          },
        },
        {
          field: 'qualityStatus',
          componentProps: {
            disabled: !!data.qualityStatus,
          },
        },
      ]);
      const { data: batchDynamicFieldList } = await reqStorageMaterialBatchFieldList(curBatch.id);
      dyFieldList.value = batchDynamicFieldList.filter(item => item.fieldType === 'MaterialBatchCustomFields');
      const schemaList = batchDynamicFieldList.map((item) => {
        if (item.fieldType === 'MaterialBatchCustomFields') {
          return {
            field: item.field,
            component: 'Input',
            label: item.fieldName,
            defaultValue: item.fieldValue,
            componentProps: {
              disabled: true,
            },
          };
        }
        else {
          return null;
        }
      }).filter(item => item);
      infoFormRef.value?.appendSchemasByField(schemaList);
    }
  }
  catch (error) {
    console.log(error);
  }
};
const inboundModelFormRefs = ref({});
const setFormRefs = (el, item) => {
  if (el) {
    inboundModelFormRefs.value[item.key] = el;
  }
};
const unitList = ref([]);

const setUnitOptions = async (materialId) => {
  try {
    Object.values(inboundModelFormRefs.value).forEach((formInstance) => {
      formInstance?.setFormModels({
        unitId: '',
        unitExtendId: '',
      });
    });
    if (isEmpty(materialId)) {
      unitList.value = [];
      return;
    }
    const { data } = await getMesUnitListDownExtendBound({
      materialId,
    });
    const { data: materialDetail } = await reqProductMaterialDetail(materialId);
    unitList.value = data.map(item => ({
      label: item.extendUnitName,
      id: item.id,
      expression: item.expression,
    }));

    unitList.value.unshift({
      label: materialDetail.unitName,
      id: materialDetail.unitId,
      isUnit: true,
      expression: t('标准单位'),
    });
  }
  catch (error) {
    console.log(error);
    unitList.value = [];
  }
};
const confirmMaterialTreeModal = async (val) => {
  if (isEmpty(val)) {
    infoFormRef.value?.setFormModels({
      specification: '',
    });
    setUnitOptions();
    return;
  }
  infoFormRef.value?.setFormModels({
    specification: val.specification,
  });
  setUnitOptions(val.id);
};

const infoFormProps = reactive({
  schemas: [
    {
      field: 'materialInfoTitle',
      component: 'FormTitle',
      label: t('物料信息'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'type',
      component: 'BMFormSelect',
      label: t('物料类型'),
      colProps: {
        span: 12,
      },
      required: true,
      componentProps: ({ formModel }) => {
        return {
          fieldNames: {
            label: 'label',
            value: 'value',
          },
          options: [
            {
              label: t('原辅包'),
              value: '0',
            },
            {
              label: t('中间品'),
              value: '1',
            },
          ],
          title: t('物料类型'),
          onChange: (val) => {
            formModel.materialName = '';
            formModel.materialId = '';
            formModel.materialBatchNo = '';
            formModel.specification = '';
            getMaterialTreeModalData(val);
            getDynamicField();
            try {
              unitList.value = [];
              Object.values(inboundModelFormRefs.value).forEach((formInstance) => {
                formInstance?.setFormModels({
                  unitId: '',
                  unitExtendId: '',
                });
              });
            }
            catch (error) {
              console.log(error);
            }
          },
        };
      },
    },
    {
      field: 'materialId',
      component: 'BMFormSelect',
      label: t('物料信息'),
      required: true,
      componentProps: ({ formModel }) => {
        return {
          title: t('物料信息'),
          type: 'tree',
          fieldNames: {
            name: 'showName',
            key: 'id',
            checkKey: 'categoryFlag',
            checkKeyValue: false,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
          onConfirm: (data) => {
            confirmMaterialTreeModal(data);
            getDynamicField(data.id);
          },
          onClear: () => {
            formModel.materialName = '';
            formModel.materialId = '';
            formModel.materialBatchNo = '';
            formModel.specification = '';
            confirmMaterialTreeModal();
            getDynamicField();
          },
        };
      },
    },
    {
      field: 'specification',
      component: 'Input',
      label: t('物料规格'),
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'materialBatchNo',
      component: 'Input',
      label: t('物料批号'),
      required: true,
      componentProps: () => {
        return {
          onConfirm: ({ value }) => {
            getDynamicField(infoFormRef.value.formModel?.materialId, value);
          },
          onBlur: ({ value }) => {
            getDynamicField(infoFormRef.value.formModel?.materialId, value);
          },
        };
      },
    },
    {
      field: 'originalBatchNo',
      component: 'Input',
      label: t('原厂批号'),
    },
    {
      field: 'productDate',
      component: 'BMFormDatePicker',
      label: t('生产日期'),
      componentProps: {
        formatDate: 'yyyy-MM-dd',
        maxDate: new Date(),
        valueFormat: 'yyyy-MM-dd',
      },
    },
    {
      field: 'expiredDate',
      component: 'BMFormDatePicker',
      label: t('有效期至'),
      required: true,
      componentProps: {
        formatDate: 'yyyy-MM-dd',
        minDate: new Date(),
        valueFormat: 'yyyy-MM-dd',
      },
    },
    {
      field: 'qualityStatus',
      component: 'BMFormSelect',
      label: t('质量状态'),
      colProps: {
        span: 12,
      },
      defaultValue: 'QUARANTINE',
      required: true,
      componentProps: () => {
        return {
          fieldNames: {
            label: 'label',
            value: 'value',
          },
          options: [
            { label: t('待验'), value: 'QUARANTINE' },
            { label: t('合格'), value: 'QUALIFIED' },
            { label: t('不合格'), value: 'UNQUALIFIED' },
            { label: t('已取样'), value: 'SAMPLED' },
            { label: t('限制性放行'), value: 'RESTRICTED_RELEASE' },
          ],
          title: t('质量状态'),
        };
      },
    },
    {
      field: 'hydration',
      component: 'Input',
      label: `${t('水分')}(%)`,
      dynamicRules: () => {
        return [
          {
            validator: (value) => {
              if (isEmpty(value)) {
                return Promise.resolve();
              }
              if (Number.isNaN(Number(value)) || Number(value) <= 0) {
                return Promise.reject(t('请输入为正数'));
              }
              // 如果值 整数或小数不能超过15位 则报错，否则通过
              const reg = /^-?\d{1,3}(?:\.\d{1,4})?$/;
              if (!reg.test(value)) {
                return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'noHydrationContent',
      component: 'Input',
      label: `${t('含量')}(%)`,
      dynamicRules: () => {
        return [
          {
            validator: (value) => {
              if (isEmpty(value)) {
                return Promise.resolve();
              }
              if (Number.isNaN(Number(value)) || Number(value) <= 0) {
                return Promise.reject(t('请输入为正数'));
              }
              // 如果值 整数或小数不能超过15位 则报错，否则通过
              const reg = /^-?\d{1,3}(?:\.\d{1,4})?$/;
              if (!reg.test(value)) {
                return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'reportNo',
      component: 'Input',
      label: t('报告单编号'),
    },
    {
      field: 'licenceNo',
      component: 'Input',
      label: t('放行单编号'),
    },
    {
      field: 'originalCode',
      component: 'Input',
      label: t('原始编码'),
    },
    {
      field: 'supplier',
      component: 'Input',
      label: t('供应商'),
    },
    {
      field: 'producer',
      component: 'Input',
      label: t('生产商'),
    },
  ],
});
const inboundModel = reactive({
  inboundList: [
    {
      key: Date.now(),
      singleQuantity: '',
      unitName: '',
      unitId: '',
      unitExtendId: '',
      size: '',
    },
  ],
});

const addInboundList = () => {
  const key = Date.now();
  const data = {
    key,
    first: 1,
    singleQuantity: '',
    unitName: '',
    unitId: '',
    unitExtendId: '',
    size: '',
  };
  inboundModel.inboundList.push(data);
  nextTick(() => {
    inboundModelFormRefs.value[key].setFormModels(data);
  });
};
const removeInboundInfo = (key) => {
  const index = inboundModel.inboundList.findIndex(item => item.key === key);
  inboundModel.inboundList.splice(index, 1);
  delete inboundModelFormRefs.value[key];
};
const inboundListFormProps = reactive({
  schemas: [
    {
      field: 'singleQuantity',
      component: 'Input',
      label: t('单件量'),
      colProps: {
        span: 8,
      },
      dynamicRules: () => {
        return [
          {
            required: true,
            message: t('请输入整数单件量'),
            validator: (val) => {
              if (!val)
                return Promise.reject(t('请输入整数单件量'));
              // 输入正数
              if (Number(val) <= 0) {
                return Promise.reject(t('请输入正数'));
              }
              if (!/^\d{1,10}(?:\.\d{1,9})?$/.test(val)) {
                return Promise.reject(t('正数部分最多为10位，小数部分最多9位'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'unitId',
      component: 'BMFormSelect',
      label: t('单位'),
      colProps: {
        span: 8,
      },
      required: true,
      componentProps: ({ formModel }) => {
        return {
          fieldNames: {
            label: 'label',
            value: 'id',
          },
          options: unitList.value,
          subLabel: 'expression',
          title: t('单位选择'),
          onConfirm: (data) => {
            if (data.isUnit) {
              formModel.unitExtendId = '';
            }
            else {
              formModel.unitExtendId = data.id;
            }
          },
          onClear: () => {
            formModel.unitExtendId = '';
            formModel.unitId = '';
          },
        };
      },
      dynamicRules: () => {
        return [
          {
            required: true,
            message: t('请选择单位'),
          },
        ];
      },
    },
    {
      field: 'size',
      component: 'Input',
      label: t('入库件数'),
      colProps: {
        span: 7,
      },
      dynamicRules: () => {
        return [
          {
            required: true,
            message: t('请输入整数入库件数'),
            validator: (val) => {
              if (!val)
                return Promise.reject(t('请输入整数入库件数'));
              if (!/^[1-9]\d*$/.test(val)) {
                return Promise.reject(t('请输入正整数'));
              }
              if (Number(val) > 99)
                return Promise.reject(t('小于100的正整数'));
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'formDelete',
      component: ({ formModel }) => {
        return (
          <Icon
            class-prefix="bmos-app-icon"
            name="shanchu2"
            size="18.75rpx"
            color="#FF5633"
            style={{
              cursor: 'pointer',
            }}
            onClick={() => {
              removeInboundInfo(formModel.key);
            }}
          />
        );
      },
      label: () => <view style={{ opacity: 0 }}>11</view>,
      vIf: ({ formModel }) => {
        return formModel.first === 1;
      },
      colProps: {
        span: 1,
      },
    },
  ],
});

const { showNotify } = useNotify();

const inboundInfoFormRef = ref();

const positionId = ref('');
const showPositionModal = ref(false);
const treePositionData = ref([]);
// 货位确认
const confirmPosition = (data) => {
  if (data) {
    inboundInfoFormRef.value?.setFormModels({
      materialPositionId: data.id,
      scanPositionValue: data.name,
    });
    positionId.value = data.id;
  }
  else {
    positionId.value = '';
    inboundInfoFormRef.value?.setFormModels({
      materialPositionId: '',
      scanPositionValue: '',
    });
  }
};
  // 扫描货位
const onScanSuccess = async (code) => {
  try {
    const { data } = await scanWeighPositionCodeApi({
      code,
    });
    inboundInfoFormRef.value?.setFormModels({
      materialPositionId: data.id,
      scanPositionValue: data.fullName,
    });
    positionId.value = data.id;
  }
  catch (error) {
    showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
const inboundInfoFormProps = reactive({
  schemas: [
    {
      field: 'materialPositionId',
      component: 'BMFormSelect',
      label: t('暂存货位'),
      defaultValue: '',
      componentProps: ({ formModel }) => {
        return {
          title: t('暂存货位'),
          type: 'tree',
          fieldNames: {
            name: 'name',
            key: 'id',
            checkKey: 'level.value',
            checkKeyValue: 4,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
          request: async () => {
            try {
              const res = await getStorageConfigTreeApi();
              return res.data;
            }
            catch (error) {
              console.log(error);
              return [];
            }
          },
          onConfirm: (data) => {
            confirmPosition(data);
          },
          onClear: () => {
            formModel.materialPositionId = '';
            formModel.scanPositionValue = '';
          },
        };
      },
      componentSlots: () => {
        // #ifdef H5
        return null;
        // #endif
        // #ifdef APP-PLUS
        return {
          right: () => {
            return (
              <Icon
                name="saomiao"
                size="14.06rpx"
                color="#2871FF"
                class-prefix="bmos-app-icon"
                onClick={(e) => {
                  e.stopPropagation();
                  bmosScanCode({
                    success: async (res) => {
                      const { result } = res;
                      if (!result) {
                        return;
                      }
                      const type = result.slice(0, 2);
                      const code = result.slice(2);
                      if (type !== '03' || !code) {
                        return;
                      }
                      onScanSuccess(code);
                    },
                    fail: () => {
                      showNotify({
                        type: 'danger',
                        message: t('扫描失败'),
                      });
                    },
                  });
                }}
              />
            );
          },
        };
        // #endif
      },
    },
    {
      field: 'linkExplain',
      component: 'Input',
      required: true,
      label: t('来源/去向'),
    },
  ],
});

const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('接收人'),
    signatureAction: 109,
    disabled: true,
  },
  {
    label: t('递交人'),
    signatureAction: 110,
    menuId: '121020004000001',
  },
]);
const bmosPrinterInstance = ref(null);
const needPrint = ref(false);
const curParams = ref({});
const printDevice = ref(null);

const submit = async () => {
  try {
    infoFormRef.value.validate();
    inboundInfoFormRef.value.validate();

    const inboundModelFormInstances = Object.values(inboundModelFormRefs.value);
    inboundModelFormInstances.map((formInstance) => {
      return formInstance.validate();
    });
    const infoResult = await infoFormRef.value.validate();
    const inboundListResult = await Promise.all(
      inboundModelFormInstances.map((formInstance) => {
        return formInstance.validate();
      }),
    );

    const inboundInfoResult = await inboundInfoFormRef.value.validate();
    curParams.value = {
      ...infoResult,
      ...inboundInfoResult,
      inboundList: inboundListResult,
      materialBatchFieldVOList: dyFieldList.value?.map((item) => {
        return {
          ...item,
          fieldValue: infoResult[item.field],
        };
      }),
    };
    printDevice.value = bmosPrinterInstance.value.print();
    if (printDevice.value) {
      showSign.value = true;
      needPrint.value = true;
    }
  }
  catch (error) {
    console.log(error);
  }
};
const handleWeigh = () => {
  showSign.value = true;
  needPrint.value = false;
};
const choosePrinterConfirm = (devicePrint) => {
  printDevice.value = devicePrint;
  showSign.value = true;
  needPrint.value = true;
};

const confirmSignPopup = async () => {
  try {
    const { userId1, userId2 } = signValue.value;
    curParams.value.receiverId = userId1;
    curParams.value.senderId = userId2;
    const { data } = await reqStorageMaterialReceiveMobile(curParams.value);
    if (needPrint.value) {
      await reqStorageMaterialPrintStorageMaterialTagBatch({
        body: data?.map((item) => {
          return { no: item };
        }),
        deviceId: printDevice.value.id,
        sceneId: curParams.value.type === '0' ? '121001010' : '121002014',
      });
    }
    showSign.value = false;
    showNotify({
      type: 'success',
      message: t('操作成功'),
    });
    signValue.value = {
      password1: '',
      loginName2: '',
      password2: '',
      userId2: '',
    };
    uni.navigateBack();
  }
  catch (error) {
    error.message
    && showNotify({
      type: 'danger',
      message: error.message,
    });
  }
};
</script>

<style lang="scss" scoped></style>
