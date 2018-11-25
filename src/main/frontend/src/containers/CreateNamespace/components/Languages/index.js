import React, { PureComponent } from 'react';
import styled from 'styled-components';
import Language from '../Language';
import { Button, Col, Row } from 'antd';

const LanguageWrapper = styled.div.attrs({
  className: 'languages-wrapper',
})`
  .mb32 {
    margin-bottom: 32px;
  }
`;

const ButtonGroup = styled.div.attrs({
  className: 'languages-button-group',
})`
  & > * {
    margin-left: 6px;
  }
`;

class LanguageValues extends PureComponent {
  static getDerivedStateFromProps(nextProps) {
    if ('value' in nextProps) {
      const { value = [] } = nextProps;
      return { values: value };
    }
    return null;
  }

  constructor(props) {
    super(props);

    this.state = { values: [] };
  }

  handleAddLanguage = (value = { name: null, value: null }) => {
    const { values } = this.state;
    const { onChange } = this.props;
    if (onChange) {
      onChange([...values, value]);
    }
  };

  handleRemoveLanguage = value => {
    const { values } = this.state;
    const { onChange } = this.props;
    if (onChange) {
      onChange(values.filter(current => current !== value));
    }
  };

  render() {
    const { values } = this.state;
    const valuesLength = values.length;
    return (
      <LanguageWrapper>
        {!valuesLength ? (
          <Button
            type="primary"
            icon="plus"
            shape="circle"
            onClick={() => {
              this.handleAddLanguage({ name: null, value: null });
            }}
          />
        ) : (
          values.map((value, index) => {
            const lastIndex = valuesLength - 1;
            return (
              <Row key={index} className={lastIndex !== index ? 'mb32' : ''}>
                <Col span={20}>
                  <Language
                    value={value}
                    onChange={changeValue => {
                      const { onChange } = this.props;
                      const changeValues = [...values];
                      changeValues[index] = changeValue;
                      onChange(changeValues);
                    }}
                  />
                </Col>
                <Col span={4}>
                  <ButtonGroup>
                    <Button
                      type="danger"
                      icon="minus"
                      shape="circle"
                      onClick={() => {
                        this.handleRemoveLanguage(value);
                      }}
                    />
                    {lastIndex === index && (
                      <Button
                        type="primary"
                        icon="plus"
                        shape="circle"
                        onClick={() => {
                          this.handleAddLanguage({ name: null, value: null });
                        }}
                      />
                    )}
                  </ButtonGroup>
                </Col>
              </Row>
            );
          })
        )}
      </LanguageWrapper>
    );
  }
}

export default LanguageValues;
