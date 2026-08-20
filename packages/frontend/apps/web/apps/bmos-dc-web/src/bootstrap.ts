import { Auth } from '@bmos/messager';
import { asyncMenu } from './router/async';
import { handleLang } from './utils/i18n';
(async () => {
  try {
    // localStorage.setItem(
    //   'BMOS-ACCESS-TOKEN',
    //   'eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyX2lkIjoiMTc3OTc3Njg3MDY1MzI4MDI1NiIsImxvZ2luX3Rva2VuIjoiOTM5OWY0ODQtY2Y3Ny00YmUzLTgzMjQtNzM3YWIxYWU5YzJmIn0.EtJcXycaBEvHOGLn5qQ-AK0jK7pFc7Vs2ukFVS3uU8AvpR0KRoIbMZVSkMNa4tGb',
    // );
    handleLang();
    await Auth({});
    await asyncMenu();
  } catch (error) {
    console.log(error);
  }
  await import('./render');
})();
