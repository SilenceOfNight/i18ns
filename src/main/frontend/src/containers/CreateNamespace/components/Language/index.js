import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import { Col, Input, Row, Select } from 'antd';

class Language extends PureComponent {
  static getDerivedStateFromProps(nextProps) {
    if ('value' in nextProps) {
      return nextProps.value || {};
    }
    return null;
  }

  constructor(props) {
    super(props);

    this.state = {
      name: null,
      value: null,
    };
  }

  handleChangeName = event => {
    const { value } = event.target;
    this.triggerChange({ name: value });
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
    const { name, value } = this.state;
    return (
      <Row gutter={8}>
        <Col span={8}>
          <Input value={value} onChange={this.handleChangeValue} placeholder="语种标识" />
        </Col>
        <Col span={16}>
          <Input value={name} onChange={this.handleChangeName} placeholder="语种名称" />
        </Col>
      </Row>
    );
  }
}

Language.propTypes = {
  value: PropTypes.shape({
    name: PropTypes.string,
    value: PropTypes.string,
  }),
  onChange: PropTypes.func,
};

export default Language;
