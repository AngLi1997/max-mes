import { NODE_TYPE } from '@/components/Record';
import { t } from '@bmos/i18n';
import type { ConfigFormProps, FormConfig } from '../types';
import { AllNodeType, HAS_FORM_BUSINESS_NODE } from './const';
import { useFormulaMaterialConfig } from './formConfig/componentsConfig/formulaMaterial';
import { useDateConfig } from './formConfig/dateConfig';
import { useNumberConfig } from './formConfig/numberConfig';
import { useSelectConfig } from './formConfig/selectConfig';
import { useStationConfig } from './formConfig/stationConfig';
import { useStationWithRequiredConfig } from './formConfig/stationWithRequiredConfig';
import { useTimeConfig } from './formConfig/timeConfig';
// 按批次量领料
import { useBatchQuantityPickConfig } from './formConfig/componentsConfig/batchQuantityPick/batchQuantityPick';
// 按批次量领料汇总
import { useBatchQuantityPickSummaryConfig } from './formConfig/componentsConfig/batchQuantityPick/batchQuantityPickSummary';
// 按物料量领料
import { useMaterialQuantityPickConfig } from './formConfig/componentsConfig/materialQuantityPick/materialQuantityPick';
// 按物料量领料 分组
import { useMaterialQuantityPickMaterialConfig } from './formConfig/componentsConfig/materialQuantityPick/materialQuantityPickMaterial';
// 领料接收 汇总
import { usePickingReceivingSummaryConfig } from './formConfig/componentsConfig/pickingReceiving/pickingReceivingSummary';
// 配料计划
import { useIngredientsPlanConfig } from './formConfig/componentsConfig/ingredientsPlan/ingredientsPlan';
// 配料计划 汇总
import { useIngredientsPlanSummaryConfig } from './formConfig/componentsConfig/ingredientsPlan/ingredientsPlanSummary';
// 配料称量
import { useWeighingIngredientsConfig } from './formConfig/componentsConfig/weighingIngredients/weighingIngredients';
// 配料称量 汇总
import { useWeighingIngredientsSummaryConfig } from './formConfig/componentsConfig/weighingIngredients/weighingIngredientsSummary';
// 配料投入
import { useIngredientsInputConfig } from './formConfig/componentsConfig/ingredientsInput/ingredientsInput';
// 配料投入 汇总
import { useIngredientsInputSummaryConfig } from './formConfig/componentsConfig/ingredientsInput/ingredientsInputSummary';
// 设备信息
import { useEquipmentInfoConfig } from './formConfig/componentsConfig/equipmentInfo/equipmentInfo';
// 设备信息 基础信息
import { useEquipmentInfoBasicConfig } from './formConfig/componentsConfig/equipmentInfo/equipmentInfoBasic';
// 设备数采
import { useEquipmentDataAcquisitionConfig } from './formConfig/componentsConfig/equipmentDataAcquisition/equipmentDataAcquisition';
// 生产投料
import { useFeedRecycleConfig } from './formConfig/componentsConfig/feedRecycle/feedRecycle';
// 生产投料 汇总
import { useFeedRecycleSummaryConfig } from './formConfig/componentsConfig/feedRecycle/feedRecycleSummary';
// 产出称量
import { useOutputWeighingConfig } from './formConfig/componentsConfig/outputWeighing/outputWeighing';
// 产出称量 汇总
import { useOutputWeighingSummaryConfig } from './formConfig/componentsConfig/outputWeighing/outputWeighingSummary';
// 成品产出
import { useProductOutputConfig } from './formConfig/componentsConfig/productOutput/productOutput';
// 清场执行
import { useCleanImplementConfig } from './formConfig/componentsConfig/cleanImplement/cleanImplement';
// 清场检查
import { useCleanCheckConfig } from './formConfig/componentsConfig/cleanCheck/cleanCheck';
// 清场信息
import { useCleanInfoConfig } from './formConfig/componentsConfig/cleanInfo/cleanInfo';
// 物料预定
import { useMaterialReserveConfig } from './formConfig/componentsConfig/materialReserve/materialReserve';
// 物料预定 汇总
import { useMaterialReserveSummaryConfig } from './formConfig/componentsConfig/materialReserve/materialReserveSummary';
// 物料投入
import { useMaterialInputConfig } from './formConfig/componentsConfig/materialInput/materialInput';
// 物料投入 汇总
import { useMaterialInputSummaryConfig } from './formConfig/componentsConfig/materialInput/materialInputSummary';
// 配液计划
import { useLiquidPreparationPlanConfig } from './formConfig/componentsConfig/liquidPreparationPlan/liquidPreparationPlan';
// 配液计划 汇总
import { useLiquidPreparationPlanSummaryConfig } from './formConfig/componentsConfig/liquidPreparationPlan/liquidPreparationPlanSummary';
// 配液量取
import { useLiquidPreparationMeasureConfig } from './formConfig/componentsConfig/liquidPreparationMeasure/liquidPreparationMeasure';
// 配液量取 汇总
import { useLiquidPreparationMeasureSummaryConfig } from './formConfig/componentsConfig/liquidPreparationMeasure/liquidPreparationMeasureSummary';
// 配液投入
import { useLiquidPreparationInputConfig } from './formConfig/componentsConfig/liquidPreparationInput/liquidPreparationInput';
// 配液投入 汇总
import { useLiquidPreparationInputSummaryConfig } from './formConfig/componentsConfig/liquidPreparationInput/liquidPreparationInputSummary';
// 配液产出
import { useLiquidPreparationOutputConfig } from './formConfig/componentsConfig/liquidPreparationOutput/liquidPreparationOutput';
// 配液产出 汇总
import { useLiquidPreparationOutputSummaryConfig } from './formConfig/componentsConfig/liquidPreparationOutput/liquidPreparationOutputSummary';
// 物料件信息
import { useMaterialInfoConfig } from './formConfig/componentsConfig/materialInfo/materialInfo';
// 设备数采绘图
import { useEquipmentDataDraw } from './formConfig/componentsConfig/equipmentDataDraw/equipmentDataDraw';

export type UseFormParams = {
  props: ConfigFormProps;
  isView: ComputedRef<boolean>;
  hasChange: Ref<boolean>;
};

export const useFormConfig = (useFormContext: UseFormParams) => {
  const { props, isView, hasChange } = useFormContext;

  // 数值配置
  const { numberConfig } = useNumberConfig({
    props,
    hasChange,
  });
  // select 配置
  const { selectConfig } = useSelectConfig({
    props,
    isView,
    hasChange,
  });
  // date
  const { dateConfig } = useDateConfig({
    props,
    hasChange,
  });
  // time
  const { timeConfig } = useTimeConfig({
    props,
    hasChange,
  });
  // 单独工位配置
  const { stationConfig: stationConfigWithOutTitle } = useStationConfig({
    props,
    hasChange,
    showStationTitle: false,
  });
  const { stationConfig: stationConfigWithTitle } = useStationConfig({
    props,
    hasChange,
    showStationTitle: true,
  });
  // 必填工位配置
  const { stationWithRequiredConfig: stationWithRequiredConfigNoTitle } = useStationWithRequiredConfig({
    props,
    hasChange,
    showStationTitle: false,
  });
  // 组件配置
  // 1、生产BOM信息组件
  const { formulaMaterialConfig } = useFormulaMaterialConfig({
    props,
    hasChange,
  });
  // 2、按批次量领料
  const { batchQuantityPickConfig } = useBatchQuantityPickConfig({
    props,
    hasChange,
  });
  // 3、按批次量领料汇总
  const { batchQuantityPickSummaryConfig } = useBatchQuantityPickSummaryConfig({
    props,
    hasChange,
  });
  // 4、按物料量领料
  const { materialQuantityPickConfig } = useMaterialQuantityPickConfig({
    props,
    hasChange,
  });
  // 5、按物料量领料 分组
  const { materialQuantityPickMaterialConfig } = useMaterialQuantityPickMaterialConfig({
    props,
    hasChange,
  });
  // 7、领料接收 汇总
  const { pickingReceivingSummaryConfig } = usePickingReceivingSummaryConfig({
    props,
    hasChange,
  });
  // 8、配料计划
  const { ingredientsPlanConfig } = useIngredientsPlanConfig({
    props,
    hasChange,
  });
  // 9、配料计划 汇总
  const { ingredientsPlanSummaryConfig } = useIngredientsPlanSummaryConfig({
    props,
    hasChange,
  });
  // 10、配料称量
  const { weighingIngredientsConfig } = useWeighingIngredientsConfig({
    props,
    hasChange,
  });
  // 11、配料称量 汇总
  const { weighingIngredientsSummaryConfig } = useWeighingIngredientsSummaryConfig({
    props,
    hasChange,
  });
  // 12、配料投入
  const { ingredientsInputConfig } = useIngredientsInputConfig({
    props,
    hasChange,
  });
  // 13、配料投入 汇总
  const { ingredientsInputSummaryConfig } = useIngredientsInputSummaryConfig({
    props,
    hasChange,
  });
  // 14、设备信息
  const { equipmentInfoConfig } = useEquipmentInfoConfig({
    props,
    hasChange,
  });
  // 15、设备信息 基础信息
  const { equipmentInfoBasicConfig } = useEquipmentInfoBasicConfig({
    props,
    hasChange,
  });
  // 16、设备数采
  const { equipmentDataAcquisitionConfig } = useEquipmentDataAcquisitionConfig({
    props,
    isView,
    hasChange,
  });
  // 19、投料回收
  const { feedRecycleConfig } = useFeedRecycleConfig({
    props,
    hasChange,
  });
  // 20、投料回收 汇总
  const { feedRecycleSummaryConfig } = useFeedRecycleSummaryConfig({
    props,
    hasChange,
  });
  // 21、产出称量
  const { outputWeighingConfig } = useOutputWeighingConfig({
    props,
    hasChange,
  });
  // 22、产出称量 汇总
  const { outputWeighingSummaryConfig } = useOutputWeighingSummaryConfig({
    props,
    hasChange,
  });
  // 23、成品产出
  const { productOutputConfig } = useProductOutputConfig({
    props,
    hasChange,
  });
  // 24、清场执行
  const { cleanImplementConfig } = useCleanImplementConfig({
    props,
    hasChange,
  });
  // 25、清场检查
  const { cleanCheckConfig } = useCleanCheckConfig({
    props,
    hasChange,
  });
  // 26、清场信息
  const { cleanInfoConfig } = useCleanInfoConfig({
    props,
    hasChange,
  });
  // 27、物料预定
  const { materialReserveConfig } = useMaterialReserveConfig({
    props,
    hasChange,
  });
  // 28、物料预定 汇总
  const { materialReserveSummaryConfig } = useMaterialReserveSummaryConfig({
    props,
    hasChange,
  });
  // 29、物料投入
  const { materialInputConfig } = useMaterialInputConfig({
    props,
    isView,
    hasChange,
  });
  // 30、物料投入 汇总
  const { materialInputSummaryConfig } = useMaterialInputSummaryConfig({
    props,
    hasChange,
  });
  // 31、配液计划
  const { liquidPreparationPlanConfig } = useLiquidPreparationPlanConfig({
    props,
    isView,
    hasChange,
  });
  // 32、配液计划 汇总
  const { liquidPreparationPlanSummaryConfig } = useLiquidPreparationPlanSummaryConfig({
    props,
    hasChange,
  });
  // 33、配液量取
  const { liquidPreparationMeasureConfig } = useLiquidPreparationMeasureConfig({
    props,
    hasChange,
  });
  // 34、配液量取 汇总
  const { liquidPreparationMeasureSummaryConfig } = useLiquidPreparationMeasureSummaryConfig({
    props,
    hasChange,
  });
  // 35、配液投入
  const { liquidPreparationInputConfig } = useLiquidPreparationInputConfig({
    props,
    hasChange,
  });
  // 36、配液投入 汇总
  const { liquidPreparationInputSummaryConfig } = useLiquidPreparationInputSummaryConfig({
    props,
    hasChange,
  });
  // 37、配液产出
  const { liquidPreparationOutputConfig } = useLiquidPreparationOutputConfig({
    props,
    hasChange,
  });
  // 38、配液产出 汇总
  const { liquidPreparationOutputSummaryConfig } = useLiquidPreparationOutputSummaryConfig({
    props,
    hasChange,
  });
  // 39、物料件信息
  const { materialInfoConfig } = useMaterialInfoConfig({
    props,
    hasChange,
  });
  // 40 设备数采绘图
  const { equipmentDataDrawConfig } = useEquipmentDataDraw({
    props,
    isView,
    hasChange,
  });

  // @ts-ignore
  const configMap: Map<AllNodeType, FormConfig> = new Map([
    [
      NODE_TYPE.TEXT,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.TEXT,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.NUMBER,
      {
        title: t('阈值设置'),
        formKeys: NODE_TYPE.NUMBER,
        formProps: {
          initialValues: {
            limit: 0,
            waring: false,
          },
          schemas: numberConfig.value,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.RADIO,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.RADIO,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.CHECKBOX,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.CHECKBOX,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.SELECT,
      {
        title: t('数据来源'),
        formKeys: NODE_TYPE.SELECT,
        formProps: {
          initialValues: {
            dataSource: 1,
            options: [
              {
                text: undefined,
                value: undefined,
              },
            ],
          },
          schemas: selectConfig.value,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.DATE,
      {
        title: t('格式配置'),
        formKeys: NODE_TYPE.DATE,
        formProps: {
          initialValues: {
            entryMethod: 1,
          },
          schemas: dateConfig.value,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.TIME,
      {
        title: t('格式配置'),
        formKeys: NODE_TYPE.TIME,
        formProps: {
          initialValues: {
            componentResultType: 'TIME',
          },
          schemas: timeConfig.value,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.SUBMIT_SIGN,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.SUBMIT_SIGN,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.REVIEW_SIGN,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.REVIEW_SIGN,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.HANDLE_SUBMIT_SIGN,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.HANDLE_SUBMIT_SIGN,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.HANDLE_REVIEW_SIGN,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.HANDLE_REVIEW_SIGN,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      NODE_TYPE.PHOTO,
      {
        title: t('工位信息'),
        formKeys: NODE_TYPE.PHOTO,
        formProps: {
          initialValues: {},
          schemas: stationWithRequiredConfigNoTitle,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT,
      {
        title: t('工位信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT,
        formProps: {
          initialValues: {},
          schemas: stationConfigWithOutTitle.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.PICKING_RECEIVING,
      {
        title: t('工位信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.PICKING_RECEIVING,
        formProps: {
          initialValues: {},
          schemas: stationConfigWithOutTitle.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.BUSINESS_FORMULA_INFO_MATERIAL,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.BUSINESS_FORMULA_INFO_MATERIAL,
        formProps: {
          initialValues: {},
          schemas: formulaMaterialConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.BATCH_QUANTITY_PICK,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.BATCH_QUANTITY_PICK,
        formProps: {
          initialValues: {},
          schemas: batchQuantityPickConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.BATCH_QUANTITY_PICK_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.BATCH_QUANTITY_PICK_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: batchQuantityPickSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_QUANTITY_PICK,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_QUANTITY_PICK,
        formProps: {
          initialValues: {},
          schemas: materialQuantityPickConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_QUANTITY_PICK_MATERIAL,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_QUANTITY_PICK_MATERIAL,
        formProps: {
          initialValues: {},
          schemas: materialQuantityPickMaterialConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.PICKING_RECEIVING_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.PICKING_RECEIVING_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: pickingReceivingSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.INGREDIENTS_PLAN,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.INGREDIENTS_PLAN,
        formProps: {
          initialValues: {},
          schemas: ingredientsPlanConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.INGREDIENTS_PLAN_MATERIAL,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.INGREDIENTS_PLAN_MATERIAL,
        formProps: {
          initialValues: {},
          schemas: ingredientsPlanSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.WEIGHING_INGREDIENTS,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.WEIGHING_INGREDIENTS,
        formProps: {
          initialValues: {},
          schemas: weighingIngredientsConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.WEIGHING_INGREDIENTS_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.WEIGHING_INGREDIENTS_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: weighingIngredientsSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.INGREDIENTS_INPUT,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.INGREDIENTS_INPUT,
        formProps: {
          initialValues: {},
          schemas: ingredientsInputConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.INGREDIENTS_INPUT_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.INGREDIENTS_INPUT_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: ingredientsInputSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.EQUIPMENT_INFO,
      {
        title: t('设备信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.EQUIPMENT_INFO,
        notShowTitle: true,
        formProps: {
          initialValues: {},
          schemas: equipmentInfoConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.EQUIPMENT_INFO_BASIC,
      {
        title: t('设备信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.EQUIPMENT_INFO_BASIC,
        notShowTitle: true,
        formProps: {
          initialValues: {},
          schemas: equipmentInfoBasicConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.EQUIPMENT_DATA_ACQUISITION,
      {
        title: t('设备数采'),
        formKeys: HAS_FORM_BUSINESS_NODE.EQUIPMENT_DATA_ACQUISITION,
        notShowTitle: true,
        formProps: {
          initialValues: {},
          schemas: equipmentDataAcquisitionConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.FEED_RECYCLE,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.FEED_RECYCLE,
        formProps: {
          initialValues: {},
          schemas: feedRecycleConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.FEED_RECYCLE_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.FEED_RECYCLE_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: feedRecycleSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.OUTPUT_WEIGHING,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.OUTPUT_WEIGHING,
        formProps: {
          initialValues: {},
          schemas: outputWeighingConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.OUTPUT_WEIGHING_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.OUTPUT_WEIGHING_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: outputWeighingSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.PRODUCT_OUTPUT,
      {
        title: t('工位信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.PRODUCT_OUTPUT,
        formProps: {
          initialValues: {},
          schemas: productOutputConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.CLEAN_IMPLEMENT,
      {
        title: t('功能配置'),
        formKeys: HAS_FORM_BUSINESS_NODE.CLEAN_IMPLEMENT,
        formProps: {
          initialValues: {},
          schemas: cleanImplementConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.CLEAN_CHECK,
      {
        title: t('功能配置'),
        formKeys: HAS_FORM_BUSINESS_NODE.CLEAN_CHECK,
        formProps: {
          initialValues: {},
          schemas: cleanCheckConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.CLEAN_INFO,
      {
        title: t('功能配置'),
        formKeys: HAS_FORM_BUSINESS_NODE.CLEAN_INFO,
        formProps: {
          initialValues: {},
          schemas: cleanInfoConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE,
        formProps: {
          initialValues: {},
          schemas: materialReserveConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: materialReserveSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE_BATCH,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_RESERVE_BATCH,
        formProps: {
          initialValues: {},
          schemas: materialReserveSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT,
      {
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT,
        notShowTitle: true,
        formProps: {
          initialValues: {},
          schemas: materialInputConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_INPUT_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: materialInputSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_PLAN,
      {
        title: t('物料信息'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_PLAN,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationPlanConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_PLAN_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_PLAN_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationPlanSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_MEASURE,
      {
        title: t('物料信息'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_MEASURE,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationMeasureConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_MEASURE_SUMMARY,
      {
        title: t('物料信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_MEASURE_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationMeasureSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_INPUT,
      {
        title: t('配液投入'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_INPUT,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationInputConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_INPUT_SUMMARY,
      {
        title: t('配液投入汇总'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_INPUT_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationInputSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_OUTPUT,
      {
        title: t('配液产出'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_OUTPUT,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationOutputConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_OUTPUT_SUMMARY,
      {
        title: t('配液产出汇总'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.LIQUID_PREPARATION_OUTPUT_SUMMARY,
        formProps: {
          initialValues: {},
          schemas: liquidPreparationOutputSummaryConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.WEIGHING_DATA,
      {
        title: t('称量数据'),
        notShowTitle: true,
        formKeys: HAS_FORM_BUSINESS_NODE.WEIGHING_DATA,
        formProps: {
          initialValues: {},
          schemas: stationConfigWithTitle.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.MATERIAL_INFO,
      {
        title: t('工位信息'),
        formKeys: HAS_FORM_BUSINESS_NODE.MATERIAL_INFO,
        formProps: {
          initialValues: {},
          schemas: materialInfoConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.EQUIPMENT_DATA_DRAW_LIST,
      {
        formKeys: HAS_FORM_BUSINESS_NODE.EQUIPMENT_DATA_DRAW_LIST,
        formProps: {
          initialValues: {
            equipmentPictureConfigList: [
              {
                formCode: 1,
              },
            ],
          },
          schemas: equipmentDataDrawConfig.value,
        },
      } as FormConfig,
    ],
    [
      HAS_FORM_BUSINESS_NODE.INSPECTION_RESULTS,
      {
        formKeys: HAS_FORM_BUSINESS_NODE.INSPECTION_RESULTS,
        formProps: {
          initialValues: {},
          schemas: materialInfoConfig.value,
        },
      } as FormConfig,
    ],
  ]);
  return {
    configMap,
  };
};
