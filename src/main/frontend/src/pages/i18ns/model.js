import { mapValues } from 'lodash';
import { createSelector } from 'reselect';
import { action } from '@/utils/redux';
import * as i18n from '@/apis/i18n';
import { message } from 'antd';

const namespace = 'i18ns';

const State = {
  NAMESPACES: 'namespaces',
  NAMESPACE_ID: 'namespaceId',
  RECORDS: 'records',
  RECORD: 'record',
  VISIBLE_CREATE_NAMESPACE: 'visibleCreateNamespace',
  VISIBLE_CREATE_LANGUAGE: 'visibleCreateLanguage',
  VISIBLE_CREATE_RECORD: 'visibleCreateRecord',
};

const ActionType = {
  TOGGLE_VISIBLE_CREATE_NAMESPACE: 'toggleVisibleCreateNamespace',
  CREATE_NAMESPACE: 'createNamespace',
  DELETE_NAMESPACE: 'deleteNamespace',
  QUERY_NAMESPACES: 'queryNamespaces',
  SET_NAMESPACES: 'setNamespaces',
  UPDATE_NAMESPACE: 'updateNamespace',
  SET_NAMESPACE_ID: 'setnamespaceId',
  QUERY_RECORDS: 'queryRecords',
  SET_RECORDS: 'setRecords',
  TOGGLE_VISIBLE_CREATE_LANGUAGE: 'toggleVisibleCreateLanguage',
  CREATE_LANGAUGE: 'createLangauge',
  TOGGLE_VISIBLE_CREATE_RECORD: 'toggleVisibleCreateRecord',
  CREATE_RECORD: 'createRecord',
  SET_RECORD: 'setRecord',
  CHANGE_RECORD: 'changeRecord',
  MODIFY_RECORD: 'modifyRecord',
  DELETE_RECORD: 'deleteRecord',
};

const innerAction = action();
const outerAction = action(namespace);

export default {
  namespace,
  state: {
    [State.NAMESPACES]: [],
    [State.NAMESPACE_ID]: null,
    [State.RECORDS]: [],
    [State.RECORD]: null,
    [State.VISIBLE_CREATE_NAMESPACE]: false,
    [State.VISIBLE_CREATE_LANGUAGE]: false,
    [State.VISIBLE_CREATE_RECORD]: false,
  },
  reducers: {
    [ActionType.TOGGLE_VISIBLE_CREATE_NAMESPACE](state) {
      const { [State.VISIBLE_CREATE_NAMESPACE]: preVisible } = state;
      return { ...state, [State.VISIBLE_CREATE_NAMESPACE]: !preVisible };
    },
    [ActionType.SET_NAMESPACES](state, action) {
      const { payload: namespaces } = action;

      return { ...state, [State.NAMESPACES]: namespaces };
    },
    [ActionType.UPDATE_NAMESPACE](state, action) {
      const { payload: namespace } = action;
      const { [State.NAMESPACES]: preNamespaces } = state;
      const preIndex = preNamespaces.findIndex(tempNamespace => tempNamespace.id === namespace.id);
      const nextNamespaces =
        preIndex < 0
          ? preNamespaces.concat(namespace)
          : preNamespaces.map((tempNamespace, index) => {
              if (preIndex === index) {
                return namespace;
              }
              return tempNamespace;
            });
      return { ...state, [State.NAMESPACES]: nextNamespaces };
    },
    [ActionType.SET_NAMESPACE_ID](state, action) {
      const { payload } = action;
      return { ...state, [State.NAMESPACE_ID]: payload };
    },
    [ActionType.SET_RECORDS](state, action) {
      const { payload } = action;
      return { ...state, [State.RECORDS]: payload };
    },
    [ActionType.TOGGLE_VISIBLE_CREATE_LANGUAGE](state) {
      const { [State.VISIBLE_CREATE_LANGUAGE]: preVisible } = state;
      return { ...state, [State.VISIBLE_CREATE_LANGUAGE]: !preVisible };
    },
    [ActionType.TOGGLE_VISIBLE_CREATE_RECORD](state) {
      const { [State.VISIBLE_CREATE_RECORD]: preVisible } = state;
      return { ...state, [State.VISIBLE_CREATE_RECORD]: !preVisible };
    },
    [ActionType.SET_RECORD](state, action) {
      const { payload: record } = action;
      return { ...state, [State.RECORD]: record };
    },
    [ActionType.CHANGE_RECORD](state, action) {
      const { payload } = action;
      const changeFileds = mapValues(payload, filed => filed.value);
      const { [State.RECORD]: preRecord } = state;
      return { ...state, [State.RECORD]: { ...preRecord, ...changeFileds } };
    },
  },
  effects: {
    *[ActionType.CREATE_NAMESPACE](action, effects) {
      const { payload: {namespace, success} } = action;
      const { call, put } = effects;
      const { error, data } = yield call(i18n.createNamespace, namespace);
      if (error) {
        message.error(error.message);
        return;
      }
      yield put(innerAction(ActionType.UPDATE_NAMESPACE)(data));
      yield put(innerAction(ActionType.SET_NAMESPACE_ID)(data.id));
      success && success(data);
    },
    *[ActionType.DELETE_NAMESPACE](action, effects) {
      const { payload: namespaceId } = action;
      const { call, put } = effects;
      const { error } = yield call(i18n.deleteNamespace, namespaceId);
      if (error) {
        message.error(error.message);
        return;
      }
      const { error: queryError, data: queryData } = yield call(i18n.queryNamespaces);
      if (queryError) {
        message.error(queryError.message);
        return;
      }
      message.success('成功删除命名空间。');
      yield put(innerAction(ActionType.SET_NAMESPACES)(queryData));
      if (queryData && queryData.length) {
        const { id } = queryData[0];
        yield put(innerAction(ActionType.SET_NAMESPACE_ID)(id));
      }
    },
    *[ActionType.QUERY_NAMESPACES](action, effects) {
      const { payload: { success } } = action;
      const { call, put } = effects;
      const { data, error } = yield call(i18n.queryNamespaces);
      if (error) {
        message.error(error.message);
        return;
      }
      yield put(innerAction(ActionType.SET_NAMESPACES)(data));
      success && success(data);
    },
    *[ActionType.SET_NAMESPACE_ID](action, effects) {
      const { payload: namespaceId } = action;
      const { put } = effects;
      yield put(innerAction(ActionType.QUERY_RECORDS)(namespaceId));
    },
    *[ActionType.CREATE_LANGUAGE](action, effects) {
      const {
        payload: { namespaceId, language, success },
      } = action;
      const { call, put } = effects;
      const { data, error } = yield call(i18n.createLanguage, namespaceId, language);
      if (error) {
        message.error(error.message);
        return;
      }
      message.success('成功添加语言。');
      yield put(innerAction(ActionType.UPDATE_NAMESPACE)(data));
      success && success(data);
    },
    *[ActionType.QUERY_RECORDS](action, effects) {
      const { payload: namespaceId } = action;
      const { call, put } = effects;
      const { data, error } = yield call(i18n.queryRecords, namespaceId);
      if (error) {
        message.error(error.message);
        return;
      }
      yield put(innerAction(ActionType.SET_RECORDS)(data));
    },
    *[ActionType.CREATE_RECORD](action, effects) {
      const { payload } = action;
      const { namespaceId, record } = payload;
      const { call, put } = effects;
      const { error } = yield call(i18n.createRecord, namespaceId, record);
      if (error) {
        message.error(error.message);
        return;
      }
      message.success('成功添加资源。');
      yield put(innerAction(ActionType.QUERY_RECORDS)(namespaceId));
      yield put(innerAction(ActionType.TOGGLE_VISIBLE_CREATE_RECORD)());
    },
    *[ActionType.MODIFY_RECORD](action, effects) {
      const { payload } = action;
      const { namespaceId, recordId, record } = payload;
      const { call, put } = effects;
      const { error } = yield call(i18n.modifyRecord, namespaceId, recordId, record);
      if (error) {
        message.error(error.message);
        return;
      }
      message.success('成功修改资源。');
      yield put(innerAction(ActionType.QUERY_RECORDS)(namespaceId));
      yield put(innerAction(ActionType.SET_RECORD)(null));
    },
    *[ActionType.DELETE_RECORD](action, effects) {
      const { payload } = action;
      const { namespaceId, recordId } = payload;
      const { call, put } = effects;
      const { error } = yield call(i18n.deleteRecord, namespaceId, recordId);
      if (error) {
        message.error(error.message);
        return;
      }
      message.success('成功删除资源。');
      yield put(innerAction(ActionType.QUERY_RECORDS)(namespaceId));
    },
  },
};

export const toggleVisibleCreateNamespace = outerAction(ActionType.TOGGLE_VISIBLE_CREATE_NAMESPACE);
export const createNamespace = outerAction(ActionType.CREATE_NAMESPACE);
export const deleteNamespace = outerAction(ActionType.DELETE_NAMESPACE);
export const queryNamespaces = outerAction(ActionType.QUERY_NAMESPACES);
export const setNamespaceId = outerAction(ActionType.SET_NAMESPACE_ID);
export const queryRecords = outerAction(ActionType.QUERY_RECORDS);
export const toggleVisibleCreateLanguage = outerAction(ActionType.TOGGLE_VISIBLE_CREATE_LANGUAGE);
export const createLanguage = outerAction(ActionType.CREATE_LANGUAGE);
export const toggleVisibleCreateRecord = outerAction(ActionType.TOGGLE_VISIBLE_CREATE_RECORD);
export const createRecord = outerAction(ActionType.CREATE_RECORD);
export const setRecord = outerAction(ActionType.SET_RECORD);
export const changeRecord = outerAction(ActionType.CHANGE_RECORD);
export const modifyRecord = outerAction(ActionType.MODIFY_RECORD);
export const deleteRecord = outerAction(ActionType.DELETE_RECORD);

const getState = state => {
  return state[namespace];
};

export const isVisibleCreateNamespace = createSelector(
  [getState],
  state => {
    return state[State.VISIBLE_CREATE_NAMESPACE];
  }
);

export const getNamespaces = createSelector(
  [getState],
  state => {
    return state[State.NAMESPACES];
  }
);

export const getNamespaceId = createSelector(
  [getState],
  state => {
    return state[State.NAMESPACE_ID];
  }
);

export const getNamespace = createSelector(
  [getNamespaces, getNamespaceId],
  (namespaces, namespaceId) => {
    if (!namespaces.length || !namespaceId) {
      return null;
    }
    return namespaces.find(tempNamespace => {
      return tempNamespace.id === namespaceId;
    });
  }
);

export const getLangauges = createSelector(
  [getNamespace],
  namespace => {
    if (!namespace) {
      return [];
    }
    return !namespace.languages ? [] : namespace.languages;
  }
);

export const getRecords = createSelector(
  [getState],
  state => {
    return state[State.RECORDS];
  }
);

export const getRecord = createSelector(
  [getState],
  state => {
    return state[State.RECORD];
  }
);

export const isVisibleCreateLanguage = createSelector(
  [getState],
  state => {
    return state[State.VISIBLE_CREATE_LANGUAGE];
  }
);

export const isVisibleCreateRecord = createSelector(
  [getState],
  state => {
    return state[State.VISIBLE_CREATE_RECORD];
  }
);
