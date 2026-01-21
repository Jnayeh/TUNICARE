import { pgTable, serial, text, timestamp } from 'drizzle-orm/pg-core'
import { InferSelectModel } from 'drizzle-orm';

export const todos = pgTable('todos', {
  id: serial('id').primaryKey(),
  title: text('title').notNull(),
  createdAt: timestamp('created_at').defaultNow(),
  updatedAt: timestamp('updated_at').$onUpdate(() => new Date()),
})

export type Todo = InferSelectModel<typeof todos>;