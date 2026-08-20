// 将数据存储在本地缓存中指定的 key 中，会覆盖掉原来该 key 对应的内容，这是一个异步接口。
export function setStorage(obj) {
	uni.setStorage(obj);
}
// 将 data 存储在本地缓存中指定的 key 中，会覆盖掉原来该 key 对应的内容，这是一个同步接口。
export function setStorageSync(...args) {
	try {
		uni.setStorageSync(...args);
	} catch (e) {
		console.log(e, 'setStorageSync');
	}
}
// 从本地缓存中异步获取指定 key 对应的内容。
export function getStorage(obj) {
	uni.getStorage(obj);
}
// 从本地缓存中同步获取指定 key 对应的内容。
export function getStorageSync(key) {
	let value
	try {
		value = uni.getStorageSync(key);
	} catch (e) {
		console.log(e, 'getStorageSync');
	}
	return value
}
// 异步获取当前 storage 的相关信息。
export function getStorageInfo(obj) {
	uni.getStorageInfo(obj);
}
// 同步获取当前 storage 的相关信息。
export function getStorageInfoSync() {
	let res
	try {
		res = uni.getStorageInfoSync();
	} catch (e) {
		console.log(e, 'getStorageInfoSync');
	}
	return res
}
// 从本地缓存中异步移除指定 key。
export function removeStorage(obj) {
	uni.removeStorage(obj);
}
// 从本地缓存中同步移除指定 key。
export function removeStorageSync(key) {
	try {
		uni.removeStorageSync(key);
	} catch (e) {
		console.log(e, 'removeStorageSync');
	}
}
//清理本地数据缓存。
export function clearStorage() {
	uni.clearStorage();
}
// 同步清理本地数据缓存。
export function clearStorageSync() {
	try {
		uni.clearStorageSync();
	} catch (e) {
		console.log(e, 'clearStorageSync');
	}
}