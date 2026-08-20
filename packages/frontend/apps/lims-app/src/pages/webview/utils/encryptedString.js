export function encryptedString(data) {
	let stringData = data;
	if (typeof data !== 'string') {
		stringData = JSON.stringify(data);
	}
	// let result = Array.from(stringData).map(char => String.fromCharCode(char.charCodeAt(0) + 9)).join('');
	let result = Array.from(stringData).map(char => {
		const str = String.fromCharCode(char.charCodeAt(0) + 9);
		if (str === '\\') {
			return '\\\\';
		}
		return str;
	}).join('');
	return result;
}
