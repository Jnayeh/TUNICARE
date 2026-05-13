import { drizzle } from "drizzle-orm/postgres-js";
import postgres from "postgres";
import * as schema from "./schema.js";

const connectionString =
  process.env.DATABASE_URL ?? "postgresql://appuser:apppass@localhost:5434/qatra_auth";

const client = postgres(connectionString, { max: 10 });
export const db = drizzle(client, { schema });
