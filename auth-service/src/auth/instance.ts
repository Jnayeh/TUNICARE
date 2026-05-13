import { betterAuth } from "better-auth";
import { drizzleAdapter } from "better-auth/adapters/drizzle";
import { db } from "../db/client.js";

const secret =
  process.env.BETTER_AUTH_SECRET ?? "0123456789abcdef0123456789abcdef0123456789";

if (process.env.NODE_ENV === "production" && secret.length < 32) {
  throw new Error("Set BETTER_AUTH_SECRET (min 32 chars) for production");
}

export const auth = betterAuth({
  // After `npx @better-auth/cli generate`, wire generated tables via { provider: "pg", schema }.
  database: drizzleAdapter(db, { provider: "pg" }),
  emailAndPassword: {
    enabled: true,
  },
  secret,
  baseURL: process.env.BETTER_AUTH_URL ?? "http://localhost:3000",
  trustedOrigins: (process.env.BETTER_AUTH_TRUSTED_ORIGINS ?? "http://localhost:5173")
    .split(",")
    .map((s) => s.trim())
    .filter(Boolean),
});
