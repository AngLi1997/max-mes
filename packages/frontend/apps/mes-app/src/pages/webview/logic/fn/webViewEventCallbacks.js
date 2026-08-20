//textarea组件state映射
// default 默认状态
// saved 数据已保存状态
// changed 数据修改状态
// unusual 数据异常状态
// formula 公式状态
// unfilled 完成时未填写状态

import { setStorageSync, getStorageSync, removeStorageSync } from "@/utils/uniStorage/uniStorage.js"
import { USER_INFO, IP_CONFIG, BMOS_ACCESS_TOKEN } from "@/utils/uniStorage/const.js"
import { getCurrentTime } from "@/utils/time.js"
import { nullValueRef } from "@/utils/systemConfig/index.js"
import {
	getHtmlApi,
	getComponentsApi,
	getRecordDataApi,
	getStepGroupUserApi,
	getCopyRecordItemListApi,
	getRecordItemFileApi,
	postBatchSaveBusinessApi,
	getBusinessTriggerApi,
	reqFormulaCalculateApi
} from "@/api/webViewApi.js"
import { isEmptyObject } from '@/utils/func.js'
import { isEmpty } from '@/utils/is.js'
import { t } from "@/utils/useBmosI18n.js"
import { ref } from "vue"
import { encryptedString } from '../../utils/encryptedString.js'
import { checkThreshold, extractNumbersFromString } from '../../utils/fns.js'
import {
	showSignModalComponentRef,
	showFinishComponentRef,
	showMenuComponentRef,
	H5AppNavigateBack,
	showSaveTipsComponentRef,
	showTimeDateComponentRef,
	showWarningDataComponentRef,
	showHandleWriteSignPopupRef,
} from '@/pages/webview/utils/index.js'
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { selectComponentOpen, radioComponentOpen, checkboxComponentOpen, historyDataComponentOpen } from '@/pages/webview/logic/fn/index.js'
// 批次量领料
import { batchQuantityPick } from "./batchQuantityPick.js"
// 物料量领料
import { materialQuantityPick } from "./materialQuantityPick.js"
// 领料接收
import { pickingReceiving } from "./pickingReceiving.js"
// 配料计划
import { ingredientsPlan } from './ingredientsPlan.js'
// 配料称量
import { weighingIngredients } from './weighingIngredients.js'
// 成品产出
import { productOutput } from './productOutput.js'
// 产出称量
import { outputWeighing } from './outputWeighing.js'
// 设备信息
import { equipmentInfo } from './equipmentInfo.js'
// 设备数采
import { equipmentDataAcquisition } from './equipmentDataAcquisition.js'
// 生产投料
import { feedRecycling } from './feedRecycling.js'
// 配料投入
import { ingredientsInput } from './ingredientsInput.js'
//清场检查
import { clearanceInspection } from './clearanceInspection.js';
//物料预定
import { materialReservation } from './materialReservation.js';
//清场信息
import { clearingInformation } from './clearingInformation.js';
//清场执行
import { clearanceExecution } from './clearanceExecution.js'
// 拍照
import { takePhoto, poenTakePhotoHistory } from './takePhoto.js'
// 配液计划
import { liquidPlan } from './liquidPlan.js'
// 配液投入
import { liquidInvest } from './liquidInvest.js'
// 物料投入
import { materialInput } from './materialInput.js'
// 配液量取
import { liquidMeasure } from './liquidMeasure.js'
// 配液产出
import { liquidOutput } from './liquidOutput.js'
// 物料件信息
import { materialInfo } from './materialInfo.js'
// 称量数据
import { weighingData } from './weighingData.js'
// 设备数采绘图
import { equipmentDataDraw } from './equipmentDataDraw.js'
// 检验结果组件
import { inspectionResults } from './inspectionResults.js'


export const showQuickButton = ref(false);
export const showAnalysisButton = ref(false);
export const quickAnalysisData = ref(null);

export const eventsMap = new Map([
	["RENDER", render],
	["CLICK", componentClick],
	["INPUT", componentInput],
	['HIDE_QUICK_ANALYSIS', hideShortcutButton],
	['SHOW_QUICK_ANALYSIS', showShortcutButton],
	['SHOW_ANALYSIS_BUTTON_FUN', showAnalysisButtonFun],
])

// 仅查看状态
export const viewOnly = ref(false)
// 生产修订
export const productionRevision = ref(false)

// 判断是否是后端直接填值的业务组件
export const isBusinessComponent = (component) => {
	return ['BUSINESS_PRODUCT_INFO', 'BUSINESS_FORMULA_INFO'].includes(component.componentType)
}

const allButtonComponent = [
	"MATERIAL_INPUT_BUTTON",
	"BATCH_RECEIVE_BUTTON",
	"MATERIAL_RECEIVE_BUTTON",
	"PICKING_RECEIVE_BUTTON",
	"INGREDIENTS_PLAN_BUTTON",
	"WEIGHING_INGREDIENTS_BUTTON",
	"INGREDIENTS_INPUT_BUTTON",
	"MATERIAL_RESERVE_BUTTON",
	"LIQUID_PLAN_BUTTON",
	"LIQUID_MEASURE_BUTTON",
	"LIQUID_INPUT_BUTTON",
	"LIQUID_OUTPUT_BUTTON",
	"FEED_RECYCLE_BUTTON",
	"OUTPUT_BUTTON_ASSEMBLY",
	"PRODUCT_OUTPUT_BUTTON"
]

// 递归过滤树中的值组成新的数组
function filterTree(tree, result, parent) {
	tree.forEach(item => {
		if (item.children && item.children.length > 0) {
			filterTree(item.children, result,
				parent || {
					componentType: item.componentType,
					id: item.id,
					fieldId: item.fieldId,
					configInfo: item.configInfo,
				})
			parentComponentsMap.set(item.id, item)
		} else {
			if (item.used) {
				if (parent) {
					item.originalComponentType = item.componentType
					item.componentType = parent.componentType
					item.parent = { ...parent }
				}
				result.push(item)
			}
		}
	})
	return result
}

// 后端需要填值的业务组件列表
const needSaveBusinessComponentList = ref([])
// 是否主动赋值中
const activeAssignmentStatus = ref(false)
// 组件配置Map数据
export const componentsMap = new Map()
// 父组件配置Map数据
export const parentComponentsMap = new Map()
// 已经保存到服务器的组件数据map对象
const componentValuesMap = new Map()
// 页面参数（接口调用时使用）
export const urlQueryRef = ref({})
export const wvRef = ref(null)
export const batchNo = ref()
export const productName = ref()
export const productMergeCode = ref()
export const pageBasicDataRef = ref({})
// 复制的记录列表数据
export const copyRecordItemListRef = ref([])
// 当前展示的复制记录项序号
const currentCopyItemNo = ref(0)
// 刚才进入的组件id
export const componentId = ref('')
// 获取当前复制的记录项
export const getCurrentCopyRecordItem = () => {
	return copyRecordItemListRef.value[currentCopyItemNo.value] || { version: '0' }
}
// 获取未作废的复制记录项数量
export const getNotInvalidCopyRecordCount = () => {
	return copyRecordItemListRef.value.filter(item => item.discard === false).length
}

// 获取当前页面的基础数据
export const getPageBasicData = () => {
	return pageBasicDataRef.value
}

// 子组件获取父级组件  component:子组件 index:父级层级
export const getParentComponent = (component, index) => {
	const parent = parentComponentsMap.get(component.parentId)
	if (parent) {
		if (index === 1) {
			return parent
		} else {
			return getParentComponent(parent, index - 1)
		}
	}
	return null
}

//判断业务组件是否已填值
export const isBusinessComponentSaved = async () => {
	const data = {
		productPlanId: urlQueryRef.value.productPlanId,
		copyVersion: getCurrentCopyRecordItem().version,
		procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
	}
	return getBusinessTriggerApi(data)
}

// 业务组件填值
export const fillBusinessComponentData = () => {
	const data = {
		batchNo: urlQueryRef.value.batchNo,
		copyVersion: getCurrentCopyRecordItem().version,
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		processId: urlQueryRef.value.processId,
		processVersion: urlQueryRef.value.processVersion,
		procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
		productPlanId: urlQueryRef.value.productPlanId,
		reuse: pageBasicDataRef.value.reusable,
		recordItemId: pageBasicDataRef.value.recordItemId,
		recordVersionId: pageBasicDataRef.value.recordVersionId,
		procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
		processChangeNumber: urlQueryRef.value.processChangeNumber,
		businessComponentList: needSaveBusinessComponentList.value.map(item => {
			return {
				...item,
				componentType: item.originalComponentType,
			}
		})
	}
	return postBatchSaveBusinessApi(data)
}

// 业务组件填值校验
export async function checkBusinessComponentData() {
	if (viewOnly.value) return
	// 调用接口判断业务组件是否填值
	let businessComponentSaved = true
	try {
		const res = await isBusinessComponentSaved()
		businessComponentSaved = res.data
	} catch (e) { }
	// 如果业务组件不为空，调用业务组件填值接口
	if (needSaveBusinessComponentList.value.length > 0 && !businessComponentSaved) {
		try {
			await fillBusinessComponentData()
		} catch (e) { }
	}
}

// 回显时，校验number、time、设备数采自定义组件值是否超出阀值
export const checkThresholdValue = (component, value, defaultState) => {
	if (component.componentType === 'NUMBER') {
		return checkNumberComponentValue(component, value, defaultState)
	}
	if (component.componentType === 'TIME') {
		return checkTimeComponentValue(component, value, defaultState)
	}
	if (component.componentType === 'EQUIPMENT_DATA_ACQUISITION' && component.originalComponentType === 'CUSTOM_FIELD') {
		return checkEquipmentDataAcquisitionComponentValue(component, value, defaultState)
	}
	return defaultState

}

// 检查设备数采自定义组件值是否超出范围
export const checkEquipmentDataAcquisitionComponentValue = (component, result, defaultState) => {
	let value = ''
	try {
		value = parseFloat(result)
		if (isNaN(value)) {
			return defaultState
		}
	} catch (error) {
		return defaultState
	}
	try {
		if (component.parent.configInfo) {
			const data = JSON.parse(component.parent.configInfo);
			const list = data.equipmentDataAttrList || [];
			const item = list.find(item => {
				const componentDetail = JSON.parse(item.componentDetail);
				return componentDetail.fieldData === component.componentDetail.fieldData
			});
			if (item) {
				return checkThreshold('NUMBER', item, value, defaultState);
			}
			return defaultState;
		} else {
			return defaultState;
		}
	} catch (error) {
		return defaultState;
	}
}

// 检查number组件值是否超出范围
export const checkNumberComponentValue = (component, result, defaultState) => {
	let value = ''
	try {
		value = parseFloat(result)
		if (isNaN(value)) {
			return defaultState
		}
	} catch (error) {
		return defaultState
	}
	if (!component.configInfo) {
		return defaultState
	}
	const state = checkThreshold(component.componentType, component.configInfo, value, defaultState);
	wvRef.value.evalJS(`setComponentBackground('${encryptedString({
		...component,
		state,
	})}')`)
	return state
}

// 检查time组件值是否超出范围
export const checkTimeComponentValue = (component, result, defaultState) => {
	let value = ''
	try {
		if (component.valueExtension) {
			value = Number(JSON.parse(component.valueExtension).timeSeconds);
		} else {
			value = extractNumbersFromString(result, timeComponentFormat.value);
		}
	} catch (error) {
		return defaultState
	}
	if (!component.configInfo) {
		return defaultState
	}
	const state = checkThreshold(component.componentType, component.configInfo, value, defaultState);
	wvRef.value.evalJS(`setComponentBackground('${encryptedString({
		...component,
		state,
	})}')`)
	return state
}

const timeComponentFormat = ref([])
const getTimeComponentFormat = async () => {
	const systemInfoStore = useSystemInfoStore();
	const { getParameterByCode } = systemInfoStore;
	try {
		const data = getParameterByCode('platform.sys.time-format.configuration');
		timeComponentFormat.value = JSON.parse(data.value)
	} catch (error) {
		timeComponentFormat.value = []
	}
}

// 进入页面后webview触发的第一个事件
async function render({ wv, data, urlQuery }) {
	console.log('render-urlQuery', urlQuery)
	urlQueryRef.value = urlQuery
	viewOnly.value = urlQuery.executePaused === 'true'
	productionRevision.value = urlQuery.revision === 'true'
	wvRef.value = wv
	currentCopyItemNo.value = 0
	batchNo.value = urlQueryRef.value.batchNo
	productName.value=urlQueryRef.value.productName
	productMergeCode.value=urlQueryRef.value.productMergeCode
	wv.evalJS(`getAllText('${encryptedString({
		'取证人': t('取证人'),
		'取证时间': t('取证时间'),
		'备注': t('备注'),
		'设备信息': t('设备信息'),
		'设备数据': t('设备数据'),
		'采集人': t('采集人'),
		'采集时间': t('采集时间'),
	})}')`)
	const { htmlData, componentList, pattern } = await initFillData(data)
	wv.evalJS(`render('${encryptedString(htmlData)}')`)
	// 设置按钮多语言
	const btnText = [
		{ key: 'obsolete-class', text: t('已作废') },
		{ key: 'copy-class', text: t('副本') },
	]
	const baseUrl =
		"http://" +
		(getStorageSync(IP_CONFIG) || "172.30.1.160:80") +
		"/";
	wvRef.value && wvRef.value.evalJS(`saveUrl('${baseUrl}')`)
	wv.evalJS(`setBtnText('${JSON.stringify(btnText)}')`)
	wv.evalJS(`initElements('${encryptedString(componentList)}')`)
	await getCopyRecordItemList({})
	// 业务组件填值方法
	await checkBusinessComponentData()
	// 设置页面的状态
	wv.evalJS(`setPageViewState('${viewOnly.value && !productionRevision.value}')`)
	initFillData2()
	wv.evalJS("setTemplateScale(`" + pattern + "`)")
	// 隐藏页码
	wv.evalJS("removePageNo()")
	await getTimeComponentFormat()
}

// 初始化模板内容及组件配置
async function initFillData(data) {
	const { nodeId, processId, processVersion, productPlanId, nodeFunction, processChangeNumber, procedureChangeNumber } = urlQueryRef.value
	// 获取组件配置数据
	const res = await getComponentsApi({
		nodeId,
		processId,
		processVersion,
		productPlanId
	})
	const componentList = filterTree(res.data.componentConfigs || [], [])
	pageBasicDataRef.value = { ...res.data } || {}
	delete pageBasicDataRef.value.componentConfigs
	//获取模板内容
	const res1 = await getHtmlApi({ recordItemId: res.data.recordItemId, recordVersionId: res.data.recordVersionId })
	let header = ''
	let footer = ''
	if (res1.data.fileContent.indexOf('<!-- remove_header_flag -->') < 0) {
		header = res1.data.docxHeader?.headerPrimary?.content || ''
		footer = res1.data.docxFooter?.footerPrimary?.content || ''
	}
	const htmlData = header + res1.data.fileContent + footer || ''

	const pageConfig = JSON.parse(res1.data.pageConfig)
	// 获取当前步骤的执行班组下的人员
	await getStepGroupUser({
		nodeId,
		productPlanId,
		nodeFunction,
		processChangeNumber,
		procedureChangeNumber
	})
	// 构建组件Map数据
	construtComponentsMap(componentList)
	// 过滤出所有需要后端填值的业务组件
	needSaveBusinessComponentList.value = (res.data.componentConfigs || []).filter(item => {
		return isBusinessComponent(item)
	})
	return {
		htmlData,
		componentList: componentList,
		pattern: pageConfig.pattern
	}
}
// 填报数据及缓存数据回显
export async function initFillData2() {
	componentValuesMap.clear()
	// clearCacheComponentsData()
	activeAssignmentStatus.value = true
	const { productPlanId } = urlQueryRef.value
	const operationType = {
		'save': 'saved',
		'modify': 'changed'
	}
	const apiData = {
		copyVersion: getCurrentCopyRecordItem().version,
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		productPlanId: productPlanId,
		reuse: pageBasicDataRef.value.reusable,
		recordItemId: pageBasicDataRef.value.recordItemId,
	}
	// 获取组件填写的数据
	const res = await getRecordDataApi(apiData)
	let componentValues = res.data || []
	componentValues.forEach(item => {
		componentValuesMap.set(item.fieldId, item)
	})
	// 获取缓存的组件数据
	const cacheData = getCacheComponentsData() || {}
	// 传给webview赋值的list
	const componentList = []
	componentsMap.forEach(component => {
		let value = ''
		let state = ''
		let remark = ''
		let valueExtension = ''
    let emptyValue = false;
		let item = {}
		// 判断是否有保存值
		const valueItem = componentValuesMap.get(component.fieldId)
		// 服务端有数据，优先用服务端数据并清除缓存数据，否则使用缓存数据

		const cacheItem = cacheData[component.fieldId]
		if (valueItem) {
			value = valueItem.value
			if (cacheItem) {
				removeCacheComponentData({ fieldId: valueItem.fieldId, value: '' })
				delete cacheData[component.fieldId]
			}
			state = operationType[valueItem.operationType] || 'saved'
			// 如果是NUMBER、TIME、设备数采自定义组件，需要校验值是否超出阀值
			if (component.componentType === 'NUMBER'
				|| component.componentType === 'TIME'
				|| (component.componentType === 'EQUIPMENT_DATA_ACQUISITION' && component.originalComponentType === 'CUSTOM_FIELD')) {
				state = checkThresholdValue({ ...component, ...valueItem }, value, state)
			}
			emptyValue = valueItem.emptyValue || false
		} else {
			value = cacheItem ? cacheItem.value : ''
			remark = cacheItem ? cacheItem.remark : ''
			valueExtension = cacheItem ? cacheItem.valueExtension : ''
			state = 'default'
			emptyValue = cacheItem?.emptyValue || false
		}
		const baseUrl = 'http://' + (getStorageSync(IP_CONFIG) || '172.30.1.160:80');
		switch (component.componentType) {
			case 'RADIO':
				item = { ...component, emptyValue, state, value }
				if (item.valueExtension) {
					item.valueExtension = JSON.parse(item.valueExtension)
				}
				if (item.formulaId && state !== 'unusual') {
					item.state = "formula"
				}
				break;
			case 'CHECKBOX':
				if (value === nullValueRef.value) {
					value = value
				} else {
					if (!Array.isArray(value)) {
						try {
							value = JSON.parse(value)
						} catch (error) {
							value = []
						}
					}
				}
				if (item.valueExtension) {
					item.valueExtension = JSON.parse(item.valueExtension)
				}
				item = { ...component, emptyValue, state, value }
				if (item.formulaId && state !== 'unusual') {
					item.state = "formula"
				}
				break;
			case 'HANDLE_SUBMIT_SIGN':
				item = { ...component, emptyValue, state, value }
				if (item.value && !item.emptyValue) {
					item.value = `${baseUrl}/${item.value}`
				}
				break;
			case 'HANDLE_REVIEW_SIGN':
				item = { ...component, emptyValue, state, value }
				if (item.value && !item.emptyValue) {
					item.value = `${baseUrl}/${item.value}`
				}
				break;
			case 'PHOTO':
				item = { ...component, emptyValue, state, value }
				wvRef.value.evalJS(`setPhotoValue('${item}')`)
			default:
				item = { ...component, emptyValue, state, value, remark, valueExtension }
				if (item.formulaId && state !== 'unusual') {
					item.state = "formula"
				}
				break;
		}
		componentsMap.set(item.fieldId, item)
		componentList.push(item)
	})
	wvRef.value.evalJS(`echoData('${encryptedString(componentList)}')`)
	setTimeout(() => {
		activeAssignmentStatus.value = false
	}, 500)
}

// 构建组件Map数据
export function construtComponentsMap(list) {
	componentsMap.clear()
	list.forEach(item => {
		item.nullValue = nullValueRef.value
		if (item.componentDetail) {
			item.componentDetail = JSON.parse(item.componentDetail)
		}
		if (item.configInfo) {
			item.configInfo = JSON.parse(item.configInfo)
		}
		if (item.formulaId) {
			item.formulaField = ""
		}
		// 设置签名组件的班组信息
		if (item.componentType === 'SUBMIT_SIGN' || item.componentType === 'REVIEW_SIGN') {
			item.configInfo = { ...item.configInfo, options: signOptionsRef.value }
		}
		item.value = ""
		setComponentState(item)
		componentsMap.set(item.fieldId, item)
	})
}

// 设置组件state
export function setComponentState(item) {
	item.state = "default"
	if (item.formulaId) {
		item.state = "formula"
	}
	// 这里特殊处理一下，如果有children，就把children遍历
	if (item.children && item.children.length > 0) {
		item.children.forEach(child => {
			setComponentState(child)
		})
	}
}


export const totalPage = ref(1)
export const currentPage = ref(1)
// 上一页
export async function prePage() {
	if (currentCopyItemNo.value === 0) {
		return
	}
	const cacheData = getCacheComponentsData()
	if (!isEmptyObject(cacheData)) {
		uni.showToast({
			icon: 'none',
			title: t('数据未保存，请先保存')
		})
		return
	}
	currentCopyItemNo.value--
	totalPage.value = copyRecordItemListRef.value.length
	currentPage.value = currentCopyItemNo.value + 1
	const params = {
		current: currentPage.value,
		currentCopyItem: getCurrentCopyRecordItem()
	}
	wvRef.value.evalJS("renderPagination(`" + JSON.stringify(params) + "`)")
	// 业务组件填值方法
	await checkBusinessComponentData()
	initFillData2()
}
// 下一页
export async function nextPage() {
	if (currentCopyItemNo.value === copyRecordItemListRef.value.length - 1) {
		return
	}
	const cacheData = getCacheComponentsData()
	if (!isEmptyObject(cacheData)) {
		uni.showToast({
			icon: 'none',
			title: t('数据未保存，请先保存')
		})
		return
	}
	currentCopyItemNo.value++
	console.log('nextPage-currentCopyItemNo.value', currentCopyItemNo.value)
	totalPage.value = copyRecordItemListRef.value.length
	currentPage.value = currentCopyItemNo.value + 1
	const params = {
		current: currentPage.value,
		currentCopyItem: getCurrentCopyRecordItem()
	}
	wvRef.value.evalJS("renderPagination(`" + JSON.stringify(params) + "`)")
	// 业务组件填值方法
	await checkBusinessComponentData()
	initFillData2()
}


// 获取缓存关键字
export const getCacheKey = () => {
	const currentUser = getStorageSync(USER_INFO)
	const { productPlanId } = urlQueryRef.value
	const { recordItemId, procedureStepId, reusable } = pageBasicDataRef.value
	if (reusable) {
		return `${currentUser.userId}-${productPlanId}-${recordItemId}-${currentCopyItemNo.value}`
	}
	return `${currentUser.userId}-${productPlanId}-${procedureStepId}-${currentCopyItemNo.value}`
}

// 清除用户缓存的组件数据
export const clearCacheComponentsData = () => {
	const cacheKey = getCacheKey()
	removeStorageSync(cacheKey)
}

// 获取用户缓存的组件数据
export const getCacheComponentsData = () => {
	const cacheKey = getCacheKey()
	const cacheData = getStorageSync(cacheKey) || {}
	return cacheData
}

// 缓存单个组件数据
export const setCacheComponentData = ({
	fieldId,
	value,
	valueExtension = '',
	remark = '',
	state = 'default',
	emptyValue = false,
}) => {
	const id = getFieldId(fieldId)
	const component = componentsMap.get(id)
	if (value === '' || (Array.isArray(value) && value.length === 0)) {
		removeCacheComponentData({ fieldId, value: '' })
	} else {
		const cacheData = getCacheComponentsData() || {}
		cacheData[id] = { ...component, value, appTime: getCurrentTime(), remark, valueExtension, emptyValue }
		const cacheKey = getCacheKey()
		setStorageSync(cacheKey, cacheData)
	}
	const params = {
		fieldId: component.fieldId,
		componentType: component.componentType,
		value,
		valueExtension,
		remark,
		state,
		emptyValue,
	}
	componentsMap.set(id, { ...component, ...params })
}

// 移除单个组件的缓存值
export const removeCacheComponentData = ({ fieldId, value }) => {
	const id = getFieldId(fieldId)
	const cacheData = getCacheComponentsData() || {}
	delete cacheData[id]
	console.log('removeCacheComponentData', cacheData)
	const cacheKey = getCacheKey()
	setStorageSync(cacheKey, cacheData)
	const component = componentsMap.get(id)
	const params = {
		fieldId: component.fieldId,
		componentType: component.componentType,
		value,
		state: 'default',
	}
	componentsMap.set(id, { ...component, ...params })
}

export const signOptionsRef = ref([])
export const newSignOptionsRef = ref([])
// 当前步骤的执行班组下的人员
const getStepGroupUser = async (params) => {
	if (viewOnly.value && !productionRevision.value) return
	const res = await getStepGroupUserApi(params)
	signOptionsRef.value = (res.data || []).map(item => {
		const { userName, loginName, userId } = item
		return {
			text: `${userName}-${loginName}`,
			value: loginName,
			id: userId,
			userName
		}
	})
	newSignOptionsRef.value = (res.data || []).map(item => {
		const { userName, loginName, userId } = item
		return {
			label: userName,
			value: loginName,
			id: userId,
			userName
		}
	})
}

// 清除componentsMap的value值
const clearComponentsMapValue = (wv) => {
	const componentValues = []
	componentsMap.forEach(item => {
		item.value = ''
		setComponentState(item)
		componentValues.push(item)
	})
	wv.evalJS(`echoData('${encryptedString(componentValues)}')`)
}



// 获取复制的记录项列表
export async function getCopyRecordItemList({ lastPage = false }) {
	const { productPlanId, procedureChangeNumber, processChangeNumber, procedureStepModelId } = urlQueryRef.value
	const res = await getCopyRecordItemListApi({
		procedureChangeNumber,
		processChangeNumber,
		procedureStepModelId,
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		productPlanId,
		recordItemId: pageBasicDataRef.value.recordItemId,
		reuse: pageBasicDataRef.value.reusable,
		recordVersionId: pageBasicDataRef.value.recordVersionId
	})
	copyRecordItemListRef.value = res.data || []
	if (lastPage) {
		currentCopyItemNo.value = copyRecordItemListRef.value.length - 1
	}
	totalPage.value = copyRecordItemListRef.value.length
	currentPage.value = currentCopyItemNo.value + 1
	const params = {
		current: currentPage.value,
		currentCopyItem: getCurrentCopyRecordItem()
	}
	wvRef.value.evalJS("renderPagination(`" + JSON.stringify(params) + "`)")
}

// 获取组件id, id有两种, id_fild 或者 id，提取id
function getFieldId(id) {
	const idArr = id.split('_')
	if (idArr.length > 1) {
		return idArr[0]
	} else {
		return id
	}
}

const componentTypeOpenMap = new Map([
	['DATE', timeDateComponentOpen],
	['SELECT', selectComponentOpen],
	['SUBMIT_SIGN', signModalComponentOpen],
	['REVIEW_SIGN', signModalComponentOpen],
	['HANDLE_SUBMIT_SIGN', handleWriteSignPopupOpen],
	['HANDLE_REVIEW_SIGN', handleWriteSignPopupOpen],
	['BATCH_QUANTITY_PICK', batchQuantityPick],
	['MATERIAL_QUANTITY_PICK', materialQuantityPick],
	['PICKING_RECEIVING', pickingReceiving],
	['INGREDIENTS_PLAN', ingredientsPlan],
	['MATERIAL_INPUT', materialInput],
	['WEIGHING_INGREDIENTS', weighingIngredients],
	['PRODUCT_OUTPUT', productOutput],
	['OUTPUT_WEIGHING', outputWeighing],
	['EQUIPMENT_INFO', equipmentInfo],
	['EQUIPMENT_DATA_ACQUISITION', equipmentDataAcquisition],
	['FEED_RECYCLE', feedRecycling],
	['INGREDIENTS_INPUT', ingredientsInput],
	['TIME', timeDateComponentOpen],
	['CLEAN_CHECK', clearanceInspection],
	['MATERIAL_RESERVE', materialReservation],
	['CLEAN_INFO', clearingInformation],
	['CLEAN_IMPLEMENT', clearanceExecution],
	['PHOTO', takePhoto],
	['LIQUID_PREPARATION_PLAN', liquidPlan],
	['LIQUID_PREPARATION_INPUT', liquidInvest],
	['LIQUID_PREPARATION_MEASURE', liquidMeasure],
	['LIQUID_PREPARATION_OUTPUT', liquidOutput],
	['MATERIAL_INFO', materialInfo],
	['WEIGHING_DATA', weighingData],
	['EQUIPMENT_DATA_DRAW_LIST', equipmentDataDraw],
	['RADIO', radioComponentOpen],
	['CHECKBOX', checkboxComponentOpen],
	['INSPECTION_RESULTS', inspectionResults]
])

// 时间组件
function timeDateComponentOpen(component) {
	//  #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/timeDateComponent/index'
	})
	uni.$emit('page-timeDateComponent', {
		...component
	})
	//  #endif
	//  #ifdef H5
	showTimeDateComponentRef.value = true
	setTimeout(() => {
		uni.$emit('page-timeDateComponent', {
			...component
		})
	}, 0)
	//  #endif
}


function componentClick({ data }) {
	const id = getFieldId(data.id)
	const component = componentsMap.get(id)
	componentId.value = id
	console.log('componentClick-component', component)
	// 物料信息、生产BOM信息组件点击无交互
	if (component.state === 'saved' || component.state === 'changed' || component.state === 'formula' || component
		.state === 'unusual' || component.state === 'show') {
		if (component.state === 'formula' && !component.value) return
		console.log('组件状态为已保存，打开数据修订弹窗', component)
		if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(component.originalComponentType)) {
			if (viewOnly.value && !productionRevision.value) {
				// 查看页面 不执行组件填报操作
				return
			}
			const type = component.componentType
			// 判断基础组件是否有操作权限
			if (!component.hasRight) {
				uni.showToast({
					title: t('缺少工位操作权限'),
					icon: 'none'
				})
				return;
			}
			const fn = componentTypeOpenMap.get(type)
			fn && fn(component)
			return
		}
		switch (component.componentType) {
			case 'PHOTO':
				poenTakePhotoHistory({
					component,
					configInfo: { options: signOptionsRef.value }
				})
				break;
			default:
				historyDataComponentOpen(component)
				break;
		}
	} else {
		if (isBusinessComponent(component)) return
		if ((!['TEXT', 'NUMBER', 'TIME', 'RADIO', 'CHECKBOX', 'SELECT', 'DATE', 'HANDLE_SUBMIT_SIGN', 'HANDLE_REVIEW_SIGN', 'SUBMIT_SIGN', 'REVIEW_SIGN', 'PHOTO'].includes(component.componentType)) && productionRevision.value) {
			uni.showToast({
				title: t('已完成生产批次无法继续执行'),
				icon: 'none'
			})
			return
		}
		if (viewOnly.value && !productionRevision.value) {
			// 查看页面 不执行组件填报操作
			return
		}
		const type = component.componentType
		// 判断基础组件是否有操作权限
		if (!component.hasRight) {
			uni.showToast({
				title: t('缺少工位操作权限'),
				icon: 'none'
			})
			return;
		}
		const fn = componentTypeOpenMap.get(type)
		fn && fn(component)
	}
}

function componentInput({ data }) {
	console.log('componentInput-data', data)
	const id = getFieldId(data.fieldId)
	const component = componentsMap.get(id)
	if (component.state !== 'default') {
		return
	}
	let value = ''
	if (component.componentType === 'CHECKBOX') {
		value = component.value || []
		if (data.checked) {
			value.push(data.value)
		} else {
			value = value.filter(item => item !== data.value)
		}
	} else {
		value = data.value
	}
	setCacheComponentData({ fieldId: data.fieldId, value })
}

export function pageBack() {
	const cacheData = getCacheComponentsData()
	if (!isEmptyObject(cacheData) && !viewOnly.value) {
		saveDataComponentOpen();
	} else {
		if (urlQueryRef.value.taskId) {
			goBackToTargetPath('pages/home/index')
		} else {
			uni.navigateBack()
		}
	}
}

// 构造批量保存数据
export const constructBatchSaveData = (signValue = {}) => {
	const currentCopyRecordItem = getCurrentCopyRecordItem()
	const cacheComponentObj = getCacheComponentsData()
	const items = Object.keys(cacheComponentObj).map(key => {
		const item = cacheComponentObj[key]
		let value = item.value
		let valueExtension = item.valueExtension || ''
		if (Array.isArray(value)) {
			if (value.length === 0) {
				value = ''
			} else {
				value = JSON.stringify(value)
			}
		}
		if (Array.isArray(valueExtension)) {
			if (valueExtension.length == 0) {
				value = ''
			} else {
				valueExtension = JSON.stringify(valueExtension)
			}
		}

		// 生产修订时，添加复核人
		const data = {};
		if (productionRevision.value) {
			data.reviewUser = signValue.userId2;
			data.reviewTime = item.appTime
		}
		return {
			...data,
			componentType: item.componentType,
			fieldId: item.fieldId,
			operationTime: item.appTime,
			operationUser: signValue.userId1,
			remark: item.remark || '',
			value: value,
			valueExtension: valueExtension,
			emptyValue: item.emptyValue || false,
		}
	})
	const { batchNo, processId, processVersion, productPlanId, procedureChangeNumber, processChangeNumber } = urlQueryRef.value
	const { procedureStepId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value
	const data = {
		items,
		batchNo,
		copyVersion: currentCopyRecordItem.version,
		procedureStepId,
		processId,
		processVersion,
		productPlanId,
		recordItemId,
		recordVersionId,
		reuse: reusable,
		procedureChangeNumber,
		processChangeNumber,
    procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
	}

	return data
}

// 数据保存弹框
export function saveDataComponentOpen() {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/saveTipsComponent/index'
	})
	// #endif
	// #ifdef H5
	showSaveTipsComponentRef.value = true
	// #endif
}

// 打开下拉组件弹框
export function signModalComponentOpen(component) {
	console.log('打开签名组件弹框');
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/signModalComponent/index'
	})
	uni.$emit('page-signModalComponent', {
		...component
	})
	// #endif
	// #ifdef H5
	showSignModalComponentRef.value = true
	setTimeout(() => {
		uni.$emit('page-signModalComponent', {
			...component
		})
	}, 0)
	// #endif
}

export function handleWriteSignPopupOpen(component) {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewPopups/HandleWriteSignPopup/index'
	})
	uni.$emit('page-handleWriteSignPopup', {
		...component
	})
	// #endif
	// #ifdef H5
	showHandleWriteSignPopupRef.value = true
	setTimeout(() => {
		uni.$emit('page-handleWriteSignPopup', {
			...component
		})
	}, 0)
	// #endif
}

function checkComponentValue(component) {
	if (component.componentType === 'NUMBER') {
		if (component.state === 'unusual') {
			return false
		}
		if (checkNumberComponentValue(component, component.value, 'default') === 'unusual') {
			return false
		}
	}
	if (component.componentType === 'TIME') {
		if (component.state === 'unusual') {
			return false
		}
		if (checkTimeComponentValue(component, component.value, 'default') === 'unusual') {
			return false
		}
	}
	return true
}

// 打开数据超出阀值警告弹框
function warningPopupOpen() {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/warningDataComponent/index'
	})
	// #endif
	// #ifdef H5
	showWarningDataComponentRef.value = true
	// #endif
}

export function openSaveSignPopup() {
	signModalComponentOpen({
		componentType: 'SAVE',
	})
}

export function pageSave() {
	const cacheData = getCacheComponentsData()
	if (isEmptyObject(cacheData)) {
		uni.showToast({
			title: t('暂无数据需要保存'),
			icon: 'none'
		})
		return
	}
	// 如果数字、时间组件 的state 为 unusual 时，不允许保存
	let checkFlag = false
	Object.entries(cacheData).forEach(([key, value]) => {
		if (checkComponentValue(value) === false) {
			checkFlag = true
		}
	})
	if (checkFlag) {
		warningPopupOpen()
		return
	}
	signModalComponentOpen({
		componentType: 'SAVE'
	})
}

// 判断是否所有组件都填值
function isAllComponentFilled() {
	const cacheData = getCacheComponentsData()
	let flag = true
	if (componentsMap.size === 0) {
		return true
	}
	componentsMap.forEach((value, key) => {
		const cacheComponent = cacheData[key] || {}
		const valueItem = componentValuesMap.get(key)
		if (value.componentType === 'CHECKBOX' || value.componentType === 'RADIO') {
			return
		}
		if (allButtonComponent.includes(value.originalComponentType)) {
			return
		}
		if (!value.value && !cacheComponent.value && !valueItem) {
			flag = false
		}
	})
	return flag
}
// 获取未填值的组件/必填组件
function getShowUnfilledComponents(required = false) {
	const list = []
	const cacheData = getCacheComponentsData()
	if (componentsMap.size === 0) {
		return list
	}
	componentsMap.forEach((value, key) => {
		const cacheComponent = cacheData[key] || {}
		const valueItem = componentValuesMap.get(key)
		if (allButtonComponent.includes(value.originalComponentType)) {
			return
		}
		if ((Array.isArray(value.value) && value.value.length === 0) || (Array.isArray(cacheComponent.value) &&
			cacheComponent.value.length === 0)) {
			if (required) {
				if (value.configInfo && value.configInfo.required) {
					list.push(value)
				}
				return
			}
			list.push(value)
			return
		}
		if (!value.value && !cacheComponent.value && !valueItem) {
			if (required) {
				if (value.configInfo && value.configInfo.required) {
					list.push(value)
				}
				return
			}
			list.push(value)
			return
		}
	})

	return list
}

// 批量给组件录入空值
export function setEmptyValueForComponents() {
	const cacheData = getCacheComponentsData()
	componentsMap.forEach((value, key) => {
		const cacheComponent = cacheData[key] || {}
		const valueItem = componentValuesMap.get(key)
		if (isEmpty(value.value) && !cacheComponent.value && !valueItem && !allButtonComponent.includes(value.originalComponentType)) {
			// 给组件录入空值 nullValueRef.value
			setCacheComponentData({ fieldId: value.fieldId, value: nullValueRef.value, emptyValue: true})
			const params = {
				fieldId: value.fieldId,
				componentType: value.componentType,
				value: nullValueRef.value,
				remark: '',
				valueExtension: value.componentDetail,
				state: 'default',
				nullValueRef: nullValueRef.value,
				hasRight: value.hasRight,
				emptyValue: true,
				...(value.componentDetail ? { componentDetail: value.componentDetail } : {})
			}
			wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
		}
	})
}

// 点击页面完成按钮
export async function pageFinish({ wv, data, urlQuery }) {
	if (urlQueryRef.value.state === 4) {
		uni.showToast({
			title: t('步骤已完成'),
			icon: 'none',
		})
		return
	}
	try {
		const cacheData = getCacheComponentsData()
		if (isEmptyObject(cacheData)) {
			// 获取所有未填值的必填组件
			const unFilledComponentList = getShowUnfilledComponents(true);
			if (unFilledComponentList.length) {
				uni.showToast({
					title: t('有必填项未完成'),
					icon: 'none',
				})
				wvRef.value.evalJS(`addUnfilledState('${encryptedString(unFilledComponentList)}')`)
				return;
			}

			// 校验所有组件都填值
			const isFilled = isAllComponentFilled()
			// #ifdef APP-PLUS
			uni.navigateTo({
				url: '/pages/webviewComponent/finishComponent/index'
			})
			uni.$emit('page-finishComponent', {
				isFilled
			})
			// #endif
			// #ifdef H5
			showFinishComponentRef.value = true
			setTimeout(() => {
				uni.$emit('page-finishComponent', {
					isFilled
				})
			}, 0)
			// #endif       
			if (!isFilled) {
				// 获取所有未填值的组件
				const componentList = getShowUnfilledComponents()
				wvRef.value.evalJS(`addUnfilledState('${encryptedString(componentList)}')`)
			}
		} else {
			uni.showToast({
				title: t('记录数据未保存'),
				icon: 'none'
			})
			return
		}
	} catch (error) {
		uni.showToast({
			title: error.message,
			icon: 'none'
		})
	}

}

// 更多组件弹窗
function menuPopupOpen() {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/menuComponent/index'
	})
	uni.$emit('page-menuComponent')
	// #endif
	// #ifdef H5
	showMenuComponentRef.value = true
	setTimeout(() => {
		uni.$emit('page-menuComponent')
	}, 0)
	// #endif
}

export function pageMenu() {
	menuPopupOpen()
}

// 获取保存签名组件数据
export function getSignComponentData(data) {
	const copyRecordItem = getCurrentCopyRecordItem()
	const signData = {
		batchNo: urlQueryRef.value.batchNo,
		copyVersion: copyRecordItem.version,
		items: [{
			componentType: data.componentType,
			fieldId: data.fieldId,
			operationTime: data.appTime,
			operationUser: data.userId,
			remark: data.remark || '',
			value: data.value,
			valueExtension: data.valueExtension || '',
		}],
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		processId: urlQueryRef.value.processId,
		processVersion: urlQueryRef.value.processVersion,
		productPlanId: urlQueryRef.value.productPlanId,
		recordItemId: pageBasicDataRef.value.recordItemId,
		recordVersionId: pageBasicDataRef.value.recordVersionId,
		reuse: pageBasicDataRef.value.reusable,
		procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
		processChangeNumber: urlQueryRef.value.processChangeNumber,
    procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
	}
	return signData
}

// 获取记录项附件
export async function getRecordItemFile({ type }) {
	const params = {
		copyVersion: getCurrentCopyRecordItem().version,
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		productPlanId: urlQueryRef.value.productPlanId,
		recordItemId: pageBasicDataRef.value.recordItemId,
		reuse: pageBasicDataRef.value.reusable,
		processChangeNumber: urlQueryRef.value.processChangeNumber || 0,
		procedureChangeNumber: urlQueryRef.value.procedureChangeNumber || 0,
		type,
	}
	console.log('我是获取记录项附件的参数', params);
	try {
		const res = await getRecordItemFileApi(params)
		return res.data || []
	} catch (e) {
		//TODO handle the exception
		return []
	}
}

// 上传记录项附件
export async function uploadRecordItemFile({ path, type, success, fail }) {
	const params = {
		batchNo: urlQueryRef.value.batchNo,
		copyVersion: getCurrentCopyRecordItem().version,
		procedureStepId: pageBasicDataRef.value.procedureStepId,
		processId: urlQueryRef.value.processId,
		processVersion: urlQueryRef.value.processVersion,
		productPlanId: urlQueryRef.value.productPlanId,
		recordItemId: pageBasicDataRef.value.recordItemId,
		recordVersionId: pageBasicDataRef.value.recordVersionId,
		reuse: pageBasicDataRef.value.reusable,
		type: type,
		processChangeNumber: urlQueryRef.value.processChangeNumber || 0,
		procedureChangeNumber: urlQueryRef.value.procedureChangeNumber || 0,
	}

	console.log('我是上传记录项附件的参数', params);
	const baseUrl = 'http://' + (getStorageSync(IP_CONFIG) || '172.30.1.160:80')
	const apiUrl = "/api/app/mes/execute/attachment/upload"
	const token = getStorageSync(BMOS_ACCESS_TOKEN) || ''
	let header = { 'bmos-access-token': token, token }
	const data = {
		url: baseUrl + apiUrl,
		formData: params,
		header: header, // header 值
		success: res => {
			if (res.statusCode === 200) {
				success && success(res)
			} else {
				fail && fail(res)
			}
		},
		fail: e => {
			fail && fail(e)
		}
	}
	// #ifdef APP-PLUS
	data.name = 'file'
	data.filePath = path
	// #endif
	// #ifdef H5
	if (process.env.NODE_ENV === 'development') {
		data.url = apiUrl
	}
	data.files = [{
		name: 'file',
		file: path
	}]
	// #endif
	uni.uploadFile(data)
}

// 日期组件
export const datePopupConfirm = (data) => {
	setCacheComponentData({
		fieldId: data.fieldId,
		value: data.value,
		remark: data.remark,
		emptyValue: false
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: data.value,
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: false
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	H5AppNavigateBack()
}
export const datePopupNull = (data) => {
	console.log('datePopupNull', data)
	setCacheComponentData({
		fieldId: data.fieldId,
		value: data.value,
		remark: data.remark,
		emptyValue: true
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: data.value,
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: true
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	H5AppNavigateBack()
}
export const datePopupReset = (data) => {
	console.log('datePopupReset', data)
	removeCacheComponentData({
		fieldId: data.fieldId,
		value: data.value,
		emptyValue: false
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: '',
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: false
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	H5AppNavigateBack()
}

// 组件设置空值
export const setComponentNull = (data) => {
	setCacheComponentData({
		fieldId: data.fieldId,
		value: nullValueRef.value,
		remark: data.remark || '',
		emptyValue: true
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: nullValueRef.value,
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: true,
		...(data.componentDetail ? { componentDetail: data.componentDetail } : {})
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	H5AppNavigateBack()
}

// 组件重置值
export const setComponentReset = (data) => {
	removeCacheComponentData({
		fieldId: data.fieldId,
		value: '',
		emptyValue: false,
		...(data.componentDetail ? { componentDetail: data.componentDetail } : {})
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: '',
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: false,
		...(data.componentDetail ? { componentDetail: data.componentDetail } : {})
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	H5AppNavigateBack()
}

// 组件设置值
export const setComponentValue = (data, back = true) => {
	setCacheComponentData({
		fieldId: data.fieldId,
		value: data.value,
		valueExtension: data.valueExtension || '',
		remark: data.remark || '',
		emptyValue: data.emptyValue ? true : false,
		...(data.componentDetail ? { componentDetail: data.componentDetail } : {})
	})
	const params = {
		fieldId: data.fieldId,
		componentType: data.componentType,
		value: data.value,
		remark: data.remark || '',
		valueExtension: '',
		state: 'default',
		hasRight: data.hasRight,
		emptyValue: data.emptyValue ? true : false,
		nullValueRef: data.nullValueRef,
		...(data.componentDetail ? { componentDetail: data.componentDetail } : {})
	}
	wvRef.value.evalJS(`echoSingleData('${encryptedString(params)}')`)
	back && H5AppNavigateBack()
}
// 返回到指定他页面(默认返回webview)
export function goBackToTargetPath(targetPath = 'pages/webview/index') {
	var pages = getCurrentPages();
	const index = pages.findIndex((item) => item.route === targetPath);
	if (index > -1) {
		uni.navigateBack({
			delta: pages.length - index - 1
		});
	} else {
		uni.navigateBack();
	}
}

// 公式试算
export async function formulaCalculate() {
	await initFillData2()
	try {
		const data = constructBatchSaveData();
    if (data?.items?.length === 0) {
    	return
    }
		const res = await reqFormulaCalculateApi(data);
		const componentList = (res.data || []).map(item => {
			const component = componentsMap.get(item.fieldId) || {};
			if (item.componentType === 'CHECKBOX') {
				const value = item.value;
				if (value === nullValueRef.value || item.emptyValue) {
					item.value = value
				} else {
					if (!Array.isArray(value)) {
						try {
							item.value = JSON.parse(value)
						} catch (error) {
							item.value = []
						}
					}
				}
			}
			return {
				...component,
				...item,
				state: 'calculation'
			}
		});
		// webview回显计算结果
		wvRef.value.evalJS(`echoData('${encryptedString(componentList)}')`)
	} catch (error) {
		error.message && uni.showToast({
			title: error.message,
			icon: 'none'
		})
	}
}
// 隐藏快捷录入/趋势分析按钮
export function hideShortcutButton() {
	quickAnalysisData.value = null;
	showQuickButton.value = false;
	showAnalysisButton.value = false;
}

// 显示快捷录入/趋势分析按钮
export function showShortcutButton({ data }) {
	quickAnalysisData.value = data;
	showQuickButton.value = true;
	if (data.componentType === 'NUMBER') {
		showAnalysisButton.value = true;
	}
}

// 显示趋势分析按钮
export function showAnalysisButtonFun({ data }) {
	quickAnalysisData.value = data;
	if (data.componentType === 'NUMBER') {
		showAnalysisButton.value = true;
	}
}