// import { createLogger } from 'redux-logger';

// const logger = createLogger({});

export const dva = {
  config: {
    // onAction: [logger],
    onError(err) {
      err.preventDefault();
      console.error(err.message);
    },
  },
};
