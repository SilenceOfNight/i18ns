import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  getNamespaceId,
  isVisibleCreateLanguage,
  toggleVisibleCreateLanguage,
  createLanguage,
} from '@/pages/i18ns/model';
import { Keypress } from '@/components';
import { Form, Input, Modal } from 'antd';

const { Item: FormItem } = Form;

class CreateLanguage extends PureComponent {
  handleOk = () => {
    const { namespaceId, createLanguage, form } = this.props;
    const { validateFields } = form;
    validateFields((error, language) => {
      if (!error) {
        createLanguage({ namespaceId, language });
      }
    });
  };

  handleCancel = () => {
    const { toggleVisibleCreateLanguage } = this.props;
    toggleVisibleCreateLanguage();
  };

  render() {
    const { form, ...args } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Modal
        {...args}
        title="添加语种"
        onOk={this.handleOk}
        onCancel={this.handleCancel}
        destroyOnClose
      >
        <Keypress handler={this.handleOk}>
          <Form>
            <FormItem>{getFieldDecorator('value')(<Input placeholder="语种标识" />)}</FormItem>
            <FormItem>{getFieldDecorator('name')(<Input placeholder="语种名称" />)}</FormItem>
          </Form>
        </Keypress>
      </Modal>
    );
  }
}

export default Form.create()(
  connect(
    mapStateToProps({
      visible: isVisibleCreateLanguage,
      namespaceId: getNamespaceId,
    }),
    mapDispatchToProps({ createLanguage, toggleVisibleCreateLanguage })
  )(CreateLanguage)
);
