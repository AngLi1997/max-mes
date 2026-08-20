import { getStorageSync, removeStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { IP_CONFIG, BMOS_ACCESS_TOKEN } from '@/utils/uniStorage/const.js';
import { LOCK_SCREEN_URL } from './config.js';
import { setLockTimeout } from '@/utils/useLockScreenTimer.js';
import { typeLanguage } from '@/utils/useLocale.js';

const request = async({ url, method, data = {}, options = {}}) => {
	let appLocale = 'zh-Hans';
	try {
		appLocale = uni.getLocale();
	} catch (error) {
		console.log(error);
	}
	const language = typeLanguage[appLocale] || 'zh_CN';
	if (!LOCK_SCREEN_URL.includes(url)) {
		setLockTimeout();
	}
	const baseUrl = 'http://' + (getStorageSync(IP_CONFIG) || '172.30.1.160:80');
	const token = getStorageSync(BMOS_ACCESS_TOKEN) || '';
	if (options.header) {
		// encodeURIComponent header中的中文字符
		Object.keys(options.header).forEach(key => {
			if (typeof options.header[key] === 'string') {
				options.header[key] = encodeURIComponent(options.header[key]);
			}
		});
	}
	let header = Object.assign({ 'bmos-access-token': token, token, language }, options
		.header || {});
	let fullUrl = baseUrl + url;
	if (process.env.NODE_ENV === 'development') {
		// #ifdef H5
		fullUrl = url;
		// #endif
	}
	return new Promise((resolve, reject) => {
		uni.request({
			url: fullUrl,
			method: method || 'GET',
			header: header,
			timeout: 60000,
			data: data || {},
			...(options.responseType ? { responseType: options.responseType } : {}),
			success: (res) => {
				if (options.responseType === 'arraybuffer' && res.statusCode === 200) {
					resolve(res);
					return;
				}
				if (res.data.code === 401 || res.statusCode === 401) {
					// 强制下线时，提示信息
					if (res.data.code === 1040400) {
						uni.showToast({
							title: res.data.message,
							icon: 'none'
						});
					}
					// 接口状态401 清楚token并且跳转登录页
					removeStorageSync(BMOS_ACCESS_TOKEN);
					reject(res.data.message);
					uni.reLaunch({
						url: '/pages/login/index'
					});
				} else if (res.statusCode === 200 && res.data.code === 0) {
					resolve(res.data);
				} else if (res.statusCode === 200 && (res.data.code === 8104010 || res.data.code === 8104007 || res.data.code === 8104008)) {
					// 登录失败时code非0 但需要reslove
					resolve(res.data);
				} else {
					reject(res.data);
				}
			},
			fail: (err) => {
				uni.showToast({
					title: '请检查网络状态',
					icon: 'error'
				});
				if (url === '/api/app/platform/user/logout') {
					removeStorageSync(BMOS_ACCESS_TOKEN);
					uni.reLaunch({
						url: '/pages/login/index'
					});
				}
				reject(err);
			},
			complete: () => {

			}
		});
	});
};

const get = (url, params, options) => {
	return request({ url, data: params, method: 'GET', options });
};
const post = (url, data, options) => {
	return request({ url, data, method: 'POST', options });
};
const put = (url, data, options) => {
	return request({ url, data, method: 'PUT', options });
};

export default {
	get,
	post,
	put,
	request
};
