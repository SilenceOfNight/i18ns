import React from 'react';
import styled from 'styled-components';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { message } from 'antd';

const Text = styled.span.attrs({
  className: 'copyable-text',
})`
  cursor: pointer;
`;

const CopyableText = props => {
  const { children, tips, ...args } = props;

  return (
    <CopyToClipboard
      text={children}
      onCopy={() => {
        message.success(tips);
      }}
    >
      <Text {...args}>{children}</Text>
    </CopyToClipboard>
  );
};

export default CopyableText;
