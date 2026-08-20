import { ref } from 'vue';
import { getServerTimeApi } from '@/api/webViewApi.js';
export let serverTime = ref(0);

export let timer = null;
// 获取当前时间戳
export const getCurrentTime = () => {
	const timestamp = serverTime.value;
	return formatTime(timestamp, 'datetime');
};

// 格式化时间戳为年-月-日
export const formatTime = (timeStamp, type = 'date') => {
	if (!timeStamp) {
		return '';
	}
	let date = new Date(timeStamp);
	let year = date.getFullYear();
	let month = date.getMonth() + 1;
	let day = date.getDate();
	let hour = date.getHours();
	let minute = date.getMinutes();
	let second = date.getSeconds();
	month < 10 ? month = `0${month}` : month;
	day < 10 ? day = `0${day}` : day;
	hour < 10 ? hour = `0${hour}` : hour;
	minute < 10 ? minute = `0${minute}` : minute;
	second < 10 ? second = `0${second}` : second;
	switch (type) {
		case 'date':
			return `${year}-${month}-${day}`;
		case 'datetime':
			return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
		default:
			break;
	}
};

export const getServerTime = async() => {
    try {
        const res = await getServerTimeApi();
        serverTime.value = res.data ? new Date(res.data).getTime() : new Date().getTime();
    } catch (error) {
        serverTime.value = new Date().getTime();
    }
	if (timer) {
		clearInterval(timer);
		timer = null;
	}
	timer = setInterval(() => {
		serverTime.value = serverTime.value + 200;
	}, 200);
};

export const clearTimer = () => {
	if (timer) {
		clearInterval(timer);
		timer = null;
	}
};

/**
 * @description: 时间戳转换 1714978583314 => 2024-04-12 16:30:12
 * @param {string} timestamp 
 * @returns {string} 2024-04-12 16:30:12
 */
export const timestampToTime = (timestamp) => {
	// 创建一个新的日期对象
	const date = new Date(Number(timestamp));
	// 获取年、月、日、时、分、秒
	const year = date.getFullYear();
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');
	const hours = date.getHours().toString().padStart(2, '0');
	const minutes = date.getMinutes().toString().padStart(2, '0');
	const seconds = date.getSeconds().toString().padStart(2, '0');
	// 返回格式化的日期时间字符串
	return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

export function getAdjacentMinutes(dateString, number = 1) {
  // 将字符串转换为Date对象
  var givenTime = new Date(dateString);

  // 计算前一分钟的时间
  var oneMinuteBefore = new Date(givenTime);
  oneMinuteBefore.setMinutes(givenTime.getMinutes() - number);

  // 计算后一分钟的时间
  var oneMinuteAfter = new Date(givenTime);
  oneMinuteAfter.setMinutes(givenTime.getMinutes() + number);

  // 返回结果
  return {
    beforeDate: timestampToTime(oneMinuteBefore.getTime()),
    afterDate: timestampToTime(oneMinuteAfter.getTime())
  };
}

  /* 
    @description: 传入日期和format，更具format返回时间戳，如果format不包含秒，则秒为0，如果不包含分，则分和秒为0 等等
    @param {string} time
    @param {string} format
  */
	export function getTimestamp(time, format) {
			const date = new Date(time);
			const year = date.getFullYear();
			const month = date.getMonth();
			const day = date.getDate();
			const hour = date.getHours();
			const minute = date.getMinutes();
			const second = date.getSeconds();
			if (format.includes('s')) {
				return new Date(year, month, day, hour, minute, second).getTime();
			} else if (format.includes('m')) {
				return new Date(year, month, day, hour, minute, 0).getTime();
			} else if (format.includes('H')) {
				return new Date(year, month, day, hour, 0, 0).getTime();
			} else if (format.includes('d')) {
				return new Date(year, month, day, 0, 0, 0).getTime();
			} else if (format.includes('M')) {
				return new Date(year, month, 1, 0, 0, 0).getTime();
			} else if (format.includes('y')) {
				// 每一年开始的时间戳
				return new Date(year, 0, 1, 0, 0, 0).getTime();
			} else {
				return new Date(year, month, day, hour, minute, second).getTime();
			}
		}
