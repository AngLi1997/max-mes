// 字符串解密
function decryptedString(data) {
	if (typeof data !== 'string') return data;
	var decryptedString = Array.from(data).map(char => String.fromCharCode(char.charCodeAt(0) - 9)).join('');
	return decryptedString;
}
