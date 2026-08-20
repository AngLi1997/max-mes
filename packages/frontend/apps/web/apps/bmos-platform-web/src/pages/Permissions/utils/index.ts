export const diffArray = <T>(sources: T[], target: T[]): T[] => {
  if (!sources) throw 'the sources Array is undefined';
  if (!target || target.length === 0 || sources.length === 0) return sources;
  const target_set = new Set(target);
  const sources_set = new Set(sources);
  const diffs: T[] = [];
  sources_set.forEach(item => {
    if (!target_set.has(item)) {
      diffs.push(item);
    }
  });
  return diffs;
};
