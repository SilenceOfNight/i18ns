import * as asserts from '@/utils/asserts';
import saveAs from 'file-saver';
import { flatten, unflatten } from 'flat';

export const exportRecords = (namespace, records) => {
  asserts.notnull(namespace, 'namespace cannot be null');
  asserts.array(records, 'records must be a array');

  const { languages } = namespace;
  languages.forEach(language => {
    const allRecords = records.reduce((allRecords, record) => {
      const { key, values } = record;
      const value = values.find(value => language.value === value.language);
      allRecords[key] = value.value;
      return allRecords;
    }, {});
    const unflattenRecords = unflatten(allRecords);
    const fileData = JSON.stringify(unflattenRecords, null, 2);
    const blob = new Blob([fileData], { type: 'text/plain;charset=utf-8' });
    saveAs(blob, `${namespace.name}.${language.value}.json`);
  });
};
