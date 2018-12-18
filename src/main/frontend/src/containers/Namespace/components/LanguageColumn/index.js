import React from 'react';
import styled from 'styled-components';
import { Button, Icon } from 'antd';

const LangauageColumnWrapper = styled.div.attrs({
  className: 'language-column-wrapper',
})`
  .language-column-label {
  }

  .language-column-delete-btn {
    cursor: pointer;
    opacity: 0;
    transition: all 0.25s ease-in-out;
    margin-left: 4px;
  }

  .language-column-delete-btn:hover {
    color: #ff4d4f;
  }

  .language-column-delete-btn:active {
    color: #cf1322;
  }

  &:hover .language-column-delete-btn {
    opacity: 1;
  }
`;

const LangaugeColumn = props => {
  const { children, onDelete } = props;
  return (
    <LangauageColumnWrapper>
      <span className="language-column-label">{children}</span>
      <Icon className="language-column-delete-btn" type="close" onClick={onDelete} />
    </LangauageColumnWrapper>
  );
};

export default LangaugeColumn;
