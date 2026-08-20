// 历史数据组件
import { showHistoryDataComponentRef } from '@/pages/webview/utils/index.js';

// 打开历史数据组件
export function historyDataComponentOpen(component) {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/historyDataComponent/index'
	});
	uni.$emit('page-historyDataComponent', component);
	// #endif
	// #ifdef H5
	showHistoryDataComponentRef.value = true;
	setTimeout(() => {
		uni.$emit('page-historyDataComponent', component);
	}, 0);
	// #endif
}

// 关闭历史数据组件
export function historyDataComponentClose() {
    // #ifdef APP-PLUS
    uni.navigateBack();
    // #endif
    // #ifdef H5
    showHistoryDataComponentRef.value = false;
    // #endif
}

