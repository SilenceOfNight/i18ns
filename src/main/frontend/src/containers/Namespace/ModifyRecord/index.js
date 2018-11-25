import React, { PureComponent } from 'react';
import { mapValues } from 'lodash';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  getRecord,
  getLangauges,
  getNamespaceId,
  modifyRecord,
  setRecord,
  changeRecord,
} from '@/pages/i18ns/model';
import { Keypress } from '@/components';
import { Form, Input, Modal } from 'antd';
import { LanguageValues } from '../components';

const { Item: FormItem, createFormField } = Form;
const TextArea = Input.TextArea;

class Modify18nRecord extends PureComponent {
  handleOk = () => {
    const { modifyRecord, form, namespaceId, record } = this.props;
    const { id: recordId } = record;
    const { validateFields } = form;
    validateFields((error, record) => {
      if (!error) {
        modifyRecord({ namespaceId, recordId, record });
      }
    });
  };

  handleCancel = () => {
    const { setRecord } = this.props;
    setRecord(null);
  };

  render() {
    const { form, languages, record, ...args } = this.props;
    const { getFieldDecorator } = form;

    return (
      <Modal
        {...args}
        title="修改资源"
        visible={!!record}
        onOk={this.handleOk}
        onCancel={this.handleCancel}
      >
        <Keypress handler={this.handleOk}>
          <Form layout="vertical">
            <FormItem>{getFieldDecorator('key')(<Input placeholder="资源唯一标识" />)}</FormItem>
            <FormItem>
              {getFieldDecorator('values')(
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

export default connect(
  mapStateToProps({
    record: getRecord,
    languages: getLangauges,
    namespaceId: getNamespaceId,
  }),
  mapDispatchToProps({ modifyRecord, setRecord, changeRecord })
)(
  Form.create({
    mapPropsToFields(props) {
      const { record } = props;
      if (!record) {
        return null;
      }

      return mapValues(record, value => createFormField({ value }));
    },
    onFieldsChange(props, changedFields) {
      const { changeRecord } = props;
      changeRecord(changedFields);
    },
  })(Modify18nRecord)
);
