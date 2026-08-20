import {
	reactive,
	ref
} from "vue"
export const useParams = () => {
	const signatureData = ref({})
	//上级参数
	const paramsData = ref();
	const seg = reactive({
		materialPartId: '',
		//领料单Id
		ordeId: '',
		//领料单名称
		ordeName: '--',
		//已有领料单
		selectedOrde: null,
		//货位ID
		targetMaterialPositionId: null,
		//货位名称
		targetMaterialPositionName: null
	})
	return {
		seg,
		paramsData,
		signatureData,
	}
}