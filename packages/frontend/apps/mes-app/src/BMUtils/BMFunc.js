// 判断是否是ArrayBuffer类型
export function isArrayBuffer(value) {
  return Object.prototype.toString.call(value) === '[object ArrayBuffer]';
}

// 判断是否是Uint8Array类型
export function isUint8Array(value) {
  return Object.prototype.toString.call(value) === '[object Uint8Array]';
}

// ArrayBuffer转字符串
// encoding: utf-8, utf-16le, utf-16be, utf-32le, utf-32be
export function arrayBufferToString(buffer, encoding = 'utf-8') {
  const decoder = new TextDecoder(encoding);
  return decoder.decode(buffer);
}

// Uint8Array转字符串
export function uint8ArrayToString(array) {
  return String.fromCharCode.apply(null, array);
}

// 十六进制转 Uint8Array
export const hexToUint8Array = (hex) => {
  let byteArray = new Uint8Array(hex.length);
  let sendArray;
  if (hex.length % 2 !== 0) {
    sendArray = new Uint8Array((hex.length + 1) / 2);
  }
  else {
    sendArray = new Uint8Array(hex.length / 2);
  }
  let i;
  byteArray = hex;

  for (i = 0; i < sendArray.length; i++) {
    if (
      (byteArray[i * 2] >= '0' && byteArray[i * 2] <= '9')
      || (byteArray[i * 2] >= 'A' && byteArray[i * 2] <= 'F')
      || (byteArray[i * 2] >= 'a' && byteArray[i * 2] <= 'f')
    ) {
      sendArray[i]
        = Number.parseInt(byteArray[i * 2], 16) * 16
        + Number.parseInt(byteArray[i * 2 + 1], 16);
    }
    else {
      console.log('Error: Invalid hex character');
    }
  }
  return sendArray;
};

// 十六进制转字符串
export function hexToString(hexData) {
  // 移除所有空格和换行符
  hexData = hexData.replace(/\s+/g, '');

  // 将十六进制字符串转换为字节数组
  const bytes = [];
  for (let i = 0; i < hexData.length; i += 2) {
    bytes.push(Number.parseInt(hexData.substr(i, 2), 16));
  }

  // 将字节数组转换为字符串
  return String.fromCharCode.apply(null, bytes);
}
// 字符串转十六进制
export function stringToHex(str) {
  let hex = '';
  for (let i = 0; i < str.length; i++) {
    const code = str.charCodeAt(i);
    const hexCode = code.toString(16);
    hex += hexCode.padStart(2, '0');
  }
  return hex;
}

// MTSICS命令
export function buildMTSICSCommand(cmd) {
  const encoder = new TextEncoder();
  // 拼接命令字符串
  const str = `${cmd} `;
  // 转为字节
  const bytes = Array.from(encoder.encode(str));
  // 添加 CR LF
  bytes.push(0x0D, 0x0A);
  return new Uint8Array(bytes);
}
