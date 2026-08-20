import {
	reactive,
	ref
} from 'vue';
export const useSubTab = () => {
	const paramsData = ref();
	const signatureData = ref({});
	const seg = reactive({
		materialPartId: '',
		targetMaterialPositionId: null,
		targetMaterialPositionName: null
	});
	return {
		seg,
		paramsData,
		signatureData
	};
};
