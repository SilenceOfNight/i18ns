const isFunc = func => {
  return func && typeof func === 'function';
};

export const action = namespace => actionType => payload => {
  const type = namespace ? `${namespace}/${actionType}` : actionType;
  return { type, payload };
};

export const mapStateToProps = mappings => state => {
  return Object.entries(mappings).reduce((props, [propName, selector]) => {
    if (!isFunc(selector)) {
      throw new Error(`The ${propName} selector is null or not a function.`);
    }
    props[propName] = selector(state);
    return props;
  }, {});
};

export const mapDispatchToProps = mappings => dispatch => {
  return Object.entries(mappings).reduce((props, [propName, action]) => {
    if (!isFunc(action)) {
      throw new Error(`The ${propName} action is null or not a function.`);
    }
    props[propName] = payload => {
      dispatch(action(payload));
    };
    return props;
  }, {});
};
