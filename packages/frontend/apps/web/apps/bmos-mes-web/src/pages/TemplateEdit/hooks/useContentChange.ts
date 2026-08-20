

export const useContentChange = (editor: any) => {
  const { DELETE_NODE } = editor;
  const contentChange = (id: KEY) => {
    const id_T = id + '_T';

    try {
      DELETE_NODE(id);
    } catch (error) {
      throw error;
    }
  };
  return { contentChange };
};
