import { Auth } from '@bmos/messager';
import { asyncMenu } from './router/async';
import { handleLang } from './utils/i18n';
(async () => {
  try {
    // localStorage.setItem(
    //   'BMOS-ACCESS-TOKEN',
    //   'eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoiODg4ODg4ODg4ODg4ODg4ODgyIiwibG9naW5fdG9rZW4iOiJlNDRlYWUyNC0yYjg0LTRmMzItOGMzNC0xOTlmYjA2Yzc4NzMifQ.s7OJKRtQIxK5H-BgECqQwVDa90qIiN-iK8AaZ13xLjUdLlR8ElXgNjHWL7m9w8Qn',
    // );
    await Auth({});
    await asyncMenu();
    await handleLang();
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();
