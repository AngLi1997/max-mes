import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { LOCK_SCREEN_TIME } from '@/utils/uniStorage/const.js';
import { ref } from 'vue';
let timer;
const value = ref(0);
const setLockTimeout = () => {
	if (timer) {
		clearTimeout(timer);
		timer = null;
	}
	const lockTime = getStorageSync(LOCK_SCREEN_TIME);
	if (lockTime === 0) {
		return;
	}
	value.value = lockTime || 10;
	timer = setTimeout(() => {
		let pages = getCurrentPages();
		const url = pages[pages.length - 1].$page.fullPath;
		if (url !== '/pages/login/index' && url !== '/pages/lockPage/index') {
			uni.navigateTo({
				url: '/pages/lockPage/index'
			});
			console.log('跳转到锁屏页面');
		} else {
			setLockTimeout();
		}
		clearTimeout(timer);
		timer = null;
	}, value.value * 1000 * 60);
};

export {
	setLockTimeout,
	value
};
