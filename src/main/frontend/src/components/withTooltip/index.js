import React from 'react';
import { Tooltip } from 'antd';

const withTooltip = (
  { attribute, ...args } = { attribute: 'title', placement: 'bottom' }
) => WrapperedComponent => props => {
  const { tooltip, [attribute]: title, ...wrapperedProps } = props;

  return (
    <Tooltip title={title} {...args} {...tooltip}>
      <WrapperedComponent {...wrapperedProps} />
    </Tooltip>
  );
};

export default withTooltip;
