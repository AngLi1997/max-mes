import { singletonCallback, singletonFunction } from '../type';

export const singletonDecoration = <T>(
  classes: new (...args:any[]) => T,
  callback: singletonCallback<T>,
): singletonFunction<T> => {
  let instance: T;

  return (...args) => {
    if (!instance) {
      instance = new classes(...args);
    }
    const returnField = callback(instance);

    return {
      Instance: instance,
      ...returnField,
    };
  };
};
