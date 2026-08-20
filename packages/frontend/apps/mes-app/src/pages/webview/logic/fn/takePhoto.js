
import {
	showTakePhotoHistoryRef
} from '@/pages/webview/utils/index.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
/**
 * 拍照组件
 * @param {*} data 
 */
export const takePhoto = (data) => {
	const params = {
		...data,
		curFieldId: data.fieldId,
		imagesList: data.value === nullValueRef.value ? '' : data.value
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
    uni.navigateTo({
        url: `/pages/businessComponents/takePhotos/index?${query}`
    });
};
/**
 * 打开拍照记录详情
 * @param {*} data 
 */
export const poenTakePhotoHistory = (data) => {
	// #ifdef APP-PLUS
	uni.navigateTo({
		url: '/pages/webviewComponent/takePhotoComponent/index'
	});
	uni.$emit('page-historicalTakeFpoto', data);
	// #endif
	// #ifdef H5
	showTakePhotoHistoryRef.value = true;
	setTimeout(() => {
		uni.$emit('page-historicalTakeFpoto', data);
	}, 0);
	// #endif
};
