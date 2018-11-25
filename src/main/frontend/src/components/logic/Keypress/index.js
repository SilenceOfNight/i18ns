import { cloneElement, memo } from 'react';
import PropTypes from 'prop-types';

const Keypress = memo(props => {
  const { when, handler, children } = props;

  return cloneElement(children, {
    onKeyPress: event => {
      if (when(event, props)) {
        handler && handler();
      }
    },
  });
});

Keypress.defaultProps = {
  action: 'Enter',
  handler: () => {},
  when: (event, props) => {
    const { key } = event;
    const { action } = props;
    return key.toLocaleLowerCase() === action.toLocaleLowerCase();
  },
};

Keypress.propTypes = {
  action: PropTypes.string,
  handler: PropTypes.func,
  when: PropTypes.func,
};

export default Keypress;
