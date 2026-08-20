import JSEncrypt from 'jsencrypt';

// 新建JSEncrypt对象
// let encryptor = new JSEncrypt();

// 设置公钥，可以从上面的非对称加密密钥生成网站中得到
let publickey =
	'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv2CMEF2HpFV+apdY8VzXpmY2lWKEobqkhrrpAwk+qz34C/AchFr9xT14B+JdHTwwrKlRsNDdvsQC2cRs0cQiGOe9zlr78RYfWHubqlnkeSNsfKnIky6ZMhimQLd5ruAHQ4iw3i5TT/slk2KDa+ipaPmOmMqTKsi20cNy4ABewqQMt3OsNZo6LzxuvUXWy3ibaliFPEv0KPQRx7cFsU/Kr0HPbAo5ERQwpAKeHCSw6UiOwM7IfYb9ZslPDcEH/LPCotbMocOgpBIy3DLi3KpJBt3iFEObzz4rAbFGgKe3waCe+imXndX/5QeFyBi4RCf6LhHqLUye6C289t2stz5UIwIDAQAB';
// Publickey方法设置到SEncrypt对象中
// encryptor.setPublickey(publickey);

// 对需要加密的数据进行加密，rspPassword就是加密密文
// let rsaPassWord = encryptor.encrypt('加密密文');

export function encrypt(data) {
	let encryptor = new JSEncrypt();
	encryptor.setPublicKey(publickey);
	return encryptor.encrypt(data);
	// return data;
}
