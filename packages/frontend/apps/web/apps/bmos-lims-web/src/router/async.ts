import { getAsyncMenu } from '../utils';
import router from './index';


export const asyncMenu = async () => {
  const list = await getAsyncMenu();
  list.forEach(item=>{
    router.addRoute('Index',item);
  })
};
