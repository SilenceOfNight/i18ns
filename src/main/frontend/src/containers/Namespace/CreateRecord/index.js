import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  getNamespaceId,
  getLangauges,
  isVisibleCreateRecord,
  createRecord,
  toggleVisibleCreateRecord,
} from '@/pages/i18ns/model';
import { Form, Input, Modal } from 'antd';
import { Keypress } from '@/components';
import { LanguageValues } from '../components';

const FormItem = Form.Item;
const TextArea = Input.TextArea;

class CreateI18nRecord extends PureComponent {
  handleOk = () => {
    const { createRecord, form, namespaceId } = this.props;
    const { validateFields } = form;
    validateFields((error, record) => {
      if (!error) {
        createRecord({ namespaceId, record });
      }
    });
  };

  handleCancel = () => {
    const { toggleVisibleCreateRecord } = this.props;
    toggleVisibleCreateRecord();
  };

  render() {
    const { form, languages, ...args } = this.props;
    const { getFieldDecorator } = form;

    return (
      <Modal
        {...args}
        title="添加资源"
        onOk={this.handleOk}
        onCancel={this.handleCancel}
        destroyOnClose
      >
        <Keypress handler={this.handleOk}>
          <Form layout="vertical">
            <FormItem>
              {getFieldDecorator('key', {
                rules: [
                  {
                    required: true,
                    message: '资源唯一标识不能为空',
                  },
                ],
              })(<Input placeholder="资源唯一标识" />)}
            </FormItem>
            <FormItem>
              {getFieldDecorator('values', {
                initialValue: languages.map(({ value }) => ({ language: value, value: '' })),
                rules: [
                  {
                    validator(rule, value, callback) {
                      if (!value || !value.length || !value.find(({ value }) => !!value)) {
                        callback('请至少1填写种语种资源');
                      }

                      callback();
                    },
                  },
                ],
              })(
                <LanguageValues
                  options={languages.map(({ name, value }) => ({ label: name, value }))}
                  input={{
                    placeholder: '资源语种信息',
                  }}
                />
              )}
            </FormItem>
            <FormItem>
              {getFieldDecorator('description')(<TextArea placeholder="资源描述" rows={4} />)}
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
      visible: isVisibleCreateRecord,
      languages: getLangauges,
      namespaceId: getNamespaceId,
    }),
    mapDispatchToProps({ createRecord, toggleVisibleCreateRecord })
  )(CreateI18nRecord)
);
