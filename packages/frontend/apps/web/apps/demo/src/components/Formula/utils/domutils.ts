export const setNodeClassName = (
  target: HTMLElement | Element,
  name: string,
) => {
  try {
    if (!target || name === void 0) throw '';
    if (target.classList.contains(name)) {
      target.classList.add(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};

export const removeNodeClassName = (
  target: HTMLElement | Element,
  name: string,
) => {
  try {
    if (!target || name === void 0) throw '';
    if (target.classList.contains(name)) {
      target.classList.remove(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};
