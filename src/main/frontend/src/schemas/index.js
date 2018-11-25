import { schema } from 'normalizr';

export const RecordSchema = new schema.Entity('records');

export const LanguageSchema = new schema.Entity('languages');

export const NamespaceSchema = new schema.Entity('namespaces', {
  languages: [LanguageSchema],
});
