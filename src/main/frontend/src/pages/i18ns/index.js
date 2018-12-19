import React, { PureComponent } from 'react';
import styled from 'styled-components';
import { connect } from 'dva';
import { mapStateToProps, mapDispatchToProps } from '@/utils/redux';
import {
  toggleVisibleCreateNamespace,
  deleteNamespace,
  queryNamespaces,
  setNamespaceId,
  getNamespaces,
  getNamespaceId,
} from './model';
import { Tabs, Modal } from 'antd';
import { Namespace, CreateNamespace } from '@/containers';

const TabPane = Tabs.TabPane;

const I18nsWrapper = styled.div`
  padding: 16px 32px;
`;

class I18ns extends PureComponent {
  componentDidMount() {
    const { queryNamespaces, setNamespaceId } = this.props;
    queryNamespaces({
      success(namespaces) {
        if (namespaces && namespaces.length) {
          setNamespaceId(namespaces[0].id);
        }
      },
    });
  }

  handleChangeTabs = activeKey => {
    const { setNamespaceId } = this.props;
    setNamespaceId(activeKey);
  };

  handleEditTabs = (targetKey, action) => {
    if ('add' === action) {
      const { toggleVisibleCreateNamespace } = this.props;
      toggleVisibleCreateNamespace();
    } else if ('remove' === action) {
      Modal.confirm({
        title: '确定删除命名空间',
        content: '删除命名空间后，该命名空间下的所有记录也会随之删除，且无法恢复。请谨慎处理！',
        onOk: () => {
          const { deleteNamespace } = this.props;
          deleteNamespace(targetKey);
        },
      });
    }
  };

  render() {
    const { namespaces, namespaceId } = this.props;

    return (
      <I18nsWrapper>
        <Tabs
          activeKey={namespaceId}
          onChange={this.handleChangeTabs}
          type="editable-card"
          // tabPosition="left"
          onEdit={this.handleEditTabs}
        >
          {namespaces.map(namespace => {
            return (
              <TabPane key={namespace.id} tab={namespace.name}>
                <Namespace namespace={namespace} />
              </TabPane>
            );
          })}
        </Tabs>
        <CreateNamespace />
      </I18nsWrapper>
    );
  }
}

export default connect(
  mapStateToProps({
    namespaces: getNamespaces,
    namespaceId: getNamespaceId,
  }),
  mapDispatchToProps({
    toggleVisibleCreateNamespace,
    deleteNamespace,
    queryNamespaces,
    setNamespaceId,
  })
)(I18ns);
