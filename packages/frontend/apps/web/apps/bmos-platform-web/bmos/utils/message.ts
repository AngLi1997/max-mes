import { userStatus } from "../api/info"

const timer = {}

export const polling = (fn,time)=>{
  //console.log('ssssddddddddddsdsadadaasa');
  const key = Symbol()
  const excute = (fn,time)=>{
      fn()
      timer[key] = setTimeout(()=>{
          excute(fn,time)
      },time)
  }
  timer[key] = setTimeout(()=>excute(fn,time),time)
  
  return ()=>{
      clearTimeout(timer[key])
      delete timer[key]
  }
}  

export const vertify_user = (time: number = 5000) => {
  const status = async()=>{
   try {
    await userStatus()
   } catch (error) {
    console.log(error);
   }
  }
  return polling(status,time)
};


 