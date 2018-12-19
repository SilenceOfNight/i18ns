import React, { PureComponent } from 'react';
import styled from 'styled-components';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  queryRecords,
  toggleVisibleCreateLanguage,
  deleteLanguage,
  toggleVisibleCreateRecord,
  createRecord,
  setRecord,
  modifyRecord,
  deleteRecord,
  selectRecords,
  deleteRecords,
  importRecords,
  exportRecords,
  getRecords,
  getSelecteds,
} from '@/pages/i18ns/model';
import CreateLanguage from './CreateLanguage';
import CreateRecord from './CreateRecord';
import ModifyRecord from './ModifyRecord';
import { LanguageColumn } from './components';
import { Button, Dropdown, Menu, Modal, Table, message } from 'antd';
import { CopyableText, FileUploader, withTooltip } from '@/components';
import moment from 'moment';

const TooltipButton = withTooltip()(Button);

const Container = styled.div.attrs({
  className: 'i18n-namespace-page',
})`
  .i18n-namespace-page-header {
    margin-bottom: 20px;

    & > :not(:first-child) {
      margin-left: 16px;
    }
  }

  .i18n-namespace-page-btn-group {
    & > :not(:first-child) {
      margin-left: 8px;
    }
  }
`;

class I18nPage extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      selectedRowKeys: [],
    };
  }

  componrntDidMount = () => {
    this.handleQueryRecords();
  };

  handleQueryRecords = () => {
    const { namespace, queryRecords } = this.props;
    const { id } = namespace;
    queryRecords(id);
  };

  handleSelectRow = selectedRowKeys => {
    const { selectRecords } = this.props;
    selectRecords(selectedRowKeys);
  };

  handleDeleteRecords = () => {
    const { namespace, deleteRecords, recordIds } = this.props;
    const { id: namespaceId } = namespace;
    Modal.confirm({
      title: '确定删除国际化资源？',
      content: '危险操作，被删除的国际化资源将无法还原。',
      onOk: () => {
        deleteRecords({ namespaceId, recordIds });
      },
    });
  };

  handleDeleteRecord = recordId => {
    const { namespace, deleteRecord } = this.props;
    const { id: namespaceId } = namespace;
    Modal.confirm({
      title: '确定删除国际化资源？',
      content: '危险操作，被删除的国际化资源将无法还原。',
      onOk: () => {
        deleteRecord({ namespaceId, recordId });
      },
    });
  };

  render() {
    const {
      records,
      recordIds,
      namespace,
      toggleVisibleCreateLanguage,
      toggleVisibleCreateRecord,
      exportRecords,
      importRecords,
    } = this.props;
    const { languages } = namespace;

    return (
      <Container>
        <div className="i18n-namespace-page-header">
          <Button type="primary" icon="plus" onClick={toggleVisibleCreateRecord}>
            添加资源
          </Button>

          <Dropdown.Button
            overlay={
              <Menu>
                {languages.map(({ name: label, value: language }) => (
                  <Menu.Item key={language}>
                    <FileUploader
                      onChange={event => {
                        const { files } = event.target;
                        if (!files) {
                          return;
                        }

                        const [file] = files;
                        const { name } = file;
                        if (!name.match('.+.json$')) {
                          message.error('目前暂时只支持导入*.json类型的国际化资源文件');
                          return;
                        }

                        const fileReader = new FileReader();
                        fileReader.readAsText(file);
                        fileReader.onload = event => {
                          const { result: fileData } = event.target;
                          const { id: namespaceId } = namespace;
                          importRecords({ namespaceId, language, fileData });
                        };
                      }}
                      accept="*.json"
                    >{`${label}资源`}</FileUploader>{' '}
                  </Menu.Item>
                ))}
              </Menu>
            }
          >
            导入资源
          </Dropdown.Button>

          <Button icon="export" onClick={exportRecords}>
            导出资源
          </Button>

          {!recordIds.length ? null : (
            <Button type="danger" icon="delete" onClick={this.handleDeleteRecords}>
              删除资源
            </Button>
          )}

          <Button icon="plus" onClick={toggleVisibleCreateLanguage}>
            添加语种
          </Button>
        </div>
        <Table
          columns={[
            {
              key: 'key',
              title: '唯一标识',
              dataIndex: 'key',
              render: key => {
                return <CopyableText tips="成功复制唯一标识">{key}</CopyableText>;
              },
            },
            ...(languages || []).map(language => {
              const { value, name } = language;
              return {
                key: value,
                title: (
                  <LanguageColumn
                    onDelete={() => {
                      const { deleteLanguage, namespace } = this.props;
                      const { id: namespaceId } = namespace;
                      Modal.confirm({
                        title: `确定删除语种${name}？`,
                        content: '危险操作，语种删除后相应的资源信息也会丢失。',
                        onOk: () => {
                          deleteLanguage({ namespaceId, language: value });
                        },
                      });
                    }}
                  >
                    {name}
                  </LanguageColumn>
                ),
                render: record => {
                  const { values } = record;
                  const languageValue = values.find(({ language }) => {
                    return value === language;
                  });
                  if (!languageValue) {
                    return null;
                  }

                  const { value: text, modifyAt, verifyAt } = languageValue;
                  let verified = modifyAt && verifyAt && moment(modifyAt).isBefore(verifyAt);
                  if (verified) {
                    return <CopyableText tips={`复制成功`}>{text}</CopyableText>;
                  }
                  return (
                    <Dropdown
                      overlay={
                        <Menu>
                          <Menu.Item>校验通过</Menu.Item>
                        </Menu>
                      }
                    >
                      <CopyableText tips={`复制成功`}>{text}</CopyableText>
                    </Dropdown>
                  );
                },
              };
            }),
            {
              key: 'operator',
              title: '操作',
              render: record => {
                return (
                  <div className="i18n-namespace-page-btn-group">
                    <TooltipButton
                      type="primary"
                      shape="circle"
                      size="small"
                      icon="edit"
                      title="修改"
                      onClick={() => {
                        const { setRecord } = this.props;
                        setRecord(record);
                      }}
                    />
                    <TooltipButton
                      type="danger"
                      shape="circle"
                      size="small"
                      icon="delete"
                      title="删除"
                      onClick={() => {
                        this.handleDeleteRecord(record.id);
                      }}
                    />
                  </div>
                );
              },
            },
          ]}
          dataSource={records}
          rowKey="id"
          rowSelection={{
            selectedRowKeys: recordIds,
            onChange: this.handleSelectRow,
          }}
          pagination={{
            showTotal: total => `Total ${total} items`,
            showSizeChanger: true,
            showQuickJumper: true,
          }}
        />
        <CreateLanguage />
        <CreateRecord />
        <ModifyRecord />
      </Container>
    );
  }
}

export default connect(
  mapStateToProps({
    records: getRecords,
    recordIds: getSelecteds,
  }),
  mapDispatchToProps({
    queryRecords,
    toggleVisibleCreateLanguage,
    deleteLanguage,
    toggleVisibleCreateRecord,
    createRecord,
    setRecord,
    modifyRecord,
    deleteRecord,
    selectRecords,
    deleteRecords,
    importRecords,
    exportRecords,
  })
)(I18nPage);
