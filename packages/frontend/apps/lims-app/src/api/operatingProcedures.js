import request from '@/utils/request/request.js'
import { getStorageSync } from "@/utils/uniStorage/uniStorage.js"
import { BMOS_ACCESS_TOKEN } from "@/utils/uniStorage/const.js"
//app端查询文件信息 /api/app/mes/operate/rule/version/app/detail

export const getOperateVersionDetailsApi = (params) => request.get(`/api/app/mes/operate/rule/version/app/detail`, params);

//文件浏览  /api/app/mes/operate/rule/version/download
export const getOperateRulePreviewApi = (params) => request.get(`/api/app/mes/operate/rule/version/download`, params, {
    headers: {
        'content-type': 'application/octet-stream',
        'Bmos-Access-Token': getStorageSync(BMOS_ACCESS_TOKEN),
    },
    responseType: 'arraybuffer'
});