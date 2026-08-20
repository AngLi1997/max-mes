import { sso } from '@bmos/messager'
const { navigatoFrom } = sso

interface SysUser {
  loginName?: string;
  password?: string;
  rentalIdList?: string[];
  status?: string;
  token?: string;
  userId?: string;
}

export const setUser = (userInfo:SysUser)=>{
  const { token='' } = userInfo
  navigatoFrom(token)
}