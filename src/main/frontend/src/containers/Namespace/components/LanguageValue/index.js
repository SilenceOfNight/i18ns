import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import { Col, Input, Row, Select } from 'antd';

const Option = Select.Option;

class LanguageValue extends PureComponent {
  static getDerivedStateFromProps(nextProps) {
    if ('value' in nextProps) {
      return nextProps.value || {};
    }
    return null;
  }

  constructor(props) {
    super(props);

    const { options, value } = props;

    this.state = {
      language: options && options.length ? options[0].value : null,
      value: null,
      ...value,
    };
  }

  handleChangeLanguage = language => {
    this.triggerChange({ language });
  };

  handleChangeValue = event => {
    const { value } = event.target;
    this.triggerChange({ value });
  };

  triggerChange = changeValue => {
    const { onChange } = this.props;
    if (onChange) {
      onChange({ ...this.state, ...changeValue });
    }
  };

  render() {
    const { options, select, input } = this.props;
    const { language, value } = this.state;

    // if (!options || !options.length) {
    //   return null;
    // }

    return (
      <Row gutter={6}>
        <Col span={8}>
          <Select value={language} onChange={this.handleChangeLanguage} {...select}>
            {options.map(option => {
              const { label, ...props } = option;
              return (
                <Option key="value" {...props}>
                  {label}
                </Option>
              );
            })}
          </Select>
        </Col>
        <Col span={16}>
          <Input value={value} onChange={this.handleChangeValue} {...input} />
        </Col>
      </Row>
    );
  }
}

LanguageValue.defaultProps = {
  defaultFirst: true,
  options: [],
  input: null,
  select: null,
};

LanguageValue.propTypes = {
  defaultFirst: PropTypes.bool,
  options: PropTypes.array,
};

export default LanguageValue;
