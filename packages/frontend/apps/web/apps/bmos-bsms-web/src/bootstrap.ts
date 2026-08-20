import { Auth } from '@bmos/messager';
import { asyncMenu } from './router/async';
import { handleLang } from './utils/i18n';

(async () => {
  try {
    // localStorage.setItem(
    //   'BMOS-ACCESS-TOKEN',
    //   'eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoiMTc2MDg1MDIwOTk4MDMyNTg4OCIsImxvZ2luX3Rva2VuIjoiYmE5OWM3OTAtNTBkOC00ZjMyLThiZjMtNjA5MzcwODNmZGI0In0.bhiqf2DSKvg-YrjAkhgULpo1IcRCYDES3A3LfjkGSYqXjS5brAe6vPJ-q_tP0G3E',
    // );
    await Auth({});
    await asyncMenu();
    await handleLang();
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();
