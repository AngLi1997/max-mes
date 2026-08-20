import { useI18n } from 'vue-i18n';

let t = (key) => key;
const init = () => {
	const { t: t1 } = useI18n();
	t = t1;
};

export {
	init,
	t
};
