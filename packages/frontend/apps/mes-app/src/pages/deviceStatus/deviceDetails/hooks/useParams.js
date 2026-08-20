import {
	t
} from "@/utils/useBmosI18n";
import {
	reactive,
	ref
} from "vue"
import {
	getCurrentTime
} from '@/utils/time.js'
export const useParams = () => {
	//日志参数
	const signatureData = ref({})
	//是否返回刷新
	const isRefreshPage = ref(true);
	//详情信息
	const specifics = ref();
	//状态
	const isCurrent = ref(4);
	//状态
	const status = ref()
	//选择时间
	const dateValue = ref();
	//占用
	const occupation = ref();
	//弹窗数据
	const formData = reactive({
		//参数
		params: {},
		//校验
		rules: [ {
			name: 'stationId',
			checkType: 'notnull',
			checkRule: '',
			errorMsg: t('请选择工位')
		}],
		//表单
		form: [{
			name: t('生产批号'),
			mode: 'picker',
			key: 'batchNo',
			value: '',
			data: []
		}, {
			name: t('使用工位'),
			mode: 'picker',
			required: true,
			key: 'stationId',
			value: '',
			data: []
		}]
	})
	return {
		isCurrent,
		status,
		specifics,
		dateValue,
		formData,
		occupation,
		isRefreshPage,
		signatureData
	}
}