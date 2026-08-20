import type { UseComponentsParams } from './useComponents';
import type { UseFormParams } from './useForm';

export * from './useComponents';
export * from './useForm';

export type ProcedureStepInstance = UseComponentsParams & UseFormParams;
