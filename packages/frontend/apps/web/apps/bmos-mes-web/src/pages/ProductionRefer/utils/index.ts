import { operationHistorySave } from '@/services';
import { message } from 'ant-design-vue';
export const saveOperationHistory = async (node: any, type: string) => {
  try {
    await operationHistorySave(node.id,type);
  } catch (error: any) {
    message.error(error.message);
  }
};
