export const notnull = (object, message) => {
  if (!object) {
    throw new Error(message);
  }
};

export const array = (object, message) => {
  if (!object && !Array.isArray(object)) {
    throw new Error(message);
  }
};
