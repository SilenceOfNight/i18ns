import React, { PureComponent } from 'react';
import styled from 'styled-components';
import LanguageValue from '../LanguageValue';
import { Button, Col, Row } from 'antd';

const LanguageValuesWrapper = styled.div.attrs({
  className: 'language-values-wrapper',
})`
  .mb32 {
    margin-bottom: 32px;
  }
`;

const ButtonGroup = styled.div.attrs({
  className: 'language-values-button-group',
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

  handleAddLanguageValue = (value = { language: null, value: null }) => {
    const { values } = this.state;
    const { onChange } = this.props;
    if (onChange) {
      onChange([...values, value]);
    }
  };

  handleRemoveLanguageValue = value => {
    const { values } = this.state;
    const { onChange } = this.props;
    if (onChange) {
      onChange(values.filter(current => current !== value));
    }
  };

  render() {
    const { options = [] } = this.props;
    const { values } = this.state;
    const valuesLength = values.length;
    const optionsLength = options.length;
    return (
      <LanguageValuesWrapper>
        {!valuesLength ? (
          <Button
            type="primary"
            icon="plus"
            shape="circle"
            onClick={() => {
              this.handleAddLanguageValue({ language: null, value: null });
            }}
          />
        ) : (
          values.map((value, index) => {
            const lastIndex = valuesLength - 1;
            return (
              <Row key={index} className={lastIndex !== index ? 'mb32' : ''}>
                <Col span={20}>
                  <LanguageValue
                    options={options.map(option => {
                      const selectedOption = values.find(current => {
                        return option.value === current.language;
                      });
                      return selectedOption ? { ...option, disabled: true } : option;
                    })}
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
                        this.handleRemoveLanguageValue(value);
                      }}
                    />
                    {lastIndex === index && valuesLength < optionsLength && (
                      <Button
                        type="primary"
                        icon="plus"
                        shape="circle"
                        onClick={() => {
                          this.handleAddLanguageValue({ language: null, value: null });
                        }}
                      />
                    )}
                  </ButtonGroup>
                </Col>
              </Row>
            );
          })
        )}
      </LanguageValuesWrapper>
    );
  }
}

export default LanguageValues;
