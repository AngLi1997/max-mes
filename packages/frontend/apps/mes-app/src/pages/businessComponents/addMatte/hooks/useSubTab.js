import {
	ref
} from 'vue';

export const useSubTab = () => {
	// 初始数据
	const currentList = ref();
	// 锁定暂存量
	const orderQuantity = ref(0);
	// 勾选的数据
	const selectedData = ref([]);
	return {
		currentList,
		orderQuantity,
		selectedData
	};
};
