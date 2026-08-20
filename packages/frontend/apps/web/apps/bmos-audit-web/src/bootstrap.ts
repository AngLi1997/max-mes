import { Auth } from '@bmos/messager';
import { asyncMenu } from './router/async';
import { handleLang } from './utils/handleLang';
(async () => {
  try {
    // localStorage.setItem('BMOS-ACCESS-TOKEN', '54a1646f-a2af-46c9-8395-26e80a2d4138')
    await Auth({});
    await asyncMenu();
    await handleLang();
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();
