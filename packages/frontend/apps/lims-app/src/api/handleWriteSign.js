
import request from '@/utils/request/request.js';

// 获取当前登录人的签名url /api/app/mes/mobile/signature/info
export const reqSignatureInfoApi = (userId) => request.get('/api/app/mes/mobile/signature/info', {
    userId
});

// 保存签名url到当前登录人所在账号 /api/app/mes/mobile/signature/save
export const reqSignatureSaveApi = (data) => request.post('/api/app/mes/mobile/signature/save', data);

// 保存手写签名组件 /api/app/mes/mobile/signature/component/save
export const reqSignatureComponentSaveApi = (data) => request.post('/api/app/mes/mobile/signature/component/save', data);

// 获取签名url 
export const reqSignatureImageBase64BuPathApi = (url) => request.get(url, {}, {
    headers: {
        'content-type': 'application/octet-stream'
    },
    responseType: 'arraybuffer'
});
