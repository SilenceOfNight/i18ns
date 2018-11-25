import http from '@/utils/http';

const CONTEXT = '/apis/v1/i18n';

export const createNamespace = namespace => {
  return http.post(`${CONTEXT}/namespaces`, namespace);
};

export const deleteNamespace = namespaceId => {
  return http.delete(`${CONTEXT}/namespaces/${namespaceId}`);
};

export const queryNamespaces = () => {
  return http.get(`${CONTEXT}/namespaces`);
};

export const createLanguage = (namespaceId, language) => {
  return http.post(`${CONTEXT}/namespaces/${namespaceId}/languages`, language);
};

export const queryRecords = namespaceId => {
  return http.get(`${CONTEXT}/namespaces/${namespaceId}/records`);
};

export const createRecord = (namespaceId, record) => {
  return http.post(`${CONTEXT}/namespaces/${namespaceId}/records`, record);
};

export const modifyRecord = (namespaceId, recordId, record) => {
  return http.put(`${CONTEXT}/namespaces/${namespaceId}/records/${recordId}`, record);
};

export const deleteRecord = (namespaceId, recordId) => {
  return http.delete(`${CONTEXT}/namespaces/${namespaceId}/records/${recordId}`);
};
