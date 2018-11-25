import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  createNamespace,
  toggleVisibleCreateNamespace,
  isVisibleCreateNamespace,
} from '@/pages/i18ns/model';
import { Form, Input, Modal } from 'antd';
import { Keypress } from '@/components';
import { Languages } from './components';

const { Item: FormItem } = Form;

class CreateI18nNamespace extends PureComponent {
  handleOk = () => {
    const { createNamespace, form } = this.props;
    const { validateFields } = form;
    validateFields((error, namespace) => {
      if (!error) {
        createNamespace(namespace);
      }
    });
  };

  handleCancel = () => {
    const { toggleVisibleCreateNamespace } = this.props;
    toggleVisibleCreateNamespace();
  };

  render() {
    const { form, ...args } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Modal
        {...args}
        title="添加命名空间"
        onOk={this.handleOk}
        onCancel={this.handleCancel}
        destroyOnClose
      >
        <Keypress handler={this.handleOk}>
          <Form layout="vertical">
            <FormItem>
              {getFieldDecorator('name')(<Input placeholder="请在此处输入将新建的命名空间名称" />)}
            </FormItem>
            <FormItem>
              {getFieldDecorator('languages', {
                initialValue: [{ name: '中文', value: 'zh-CN' }, { name: '英文', value: 'en-US' }],
              })(<Languages />)}
            </FormItem>
          </Form>
        </Keypress>
      </Modal>
    );
  }
}

export default Form.create()(
  connect(
    mapStateToProps({
      visible: isVisibleCreateNamespace,
    }),
    mapDispatchToProps({ createNamespace, toggleVisibleCreateNamespace })
  )(CreateI18nNamespace)
);
