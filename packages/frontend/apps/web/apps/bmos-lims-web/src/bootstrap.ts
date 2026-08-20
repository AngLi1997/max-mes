import { Auth } from '@bmos/messager';
import { asyncMenu } from './router/async';
import { handleLang } from './utils/i18n';
(async () => {
  try {
    // localStorage.setItem(
    //   'BMOS-ACCESS-TOKEN',
    //   'eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoiMTc2MDg1MjE4OTc1ODkyMjc1MiIsImxvZ2luX3Rva2VuIjoiN2ZiOTk0NWQtMjYzNy00NTczLThmNWYtNzBmYjM5ZjlkNmI1In0.BwHgCerXpGvjy4DI0wyei_kb14WnWB7tJMI3XuQrXr2xY6NZTePTqO8_LuvMbtLq')
    await Auth({});
    await asyncMenu();
    await handleLang();
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();
