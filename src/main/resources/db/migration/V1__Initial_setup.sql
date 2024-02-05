-- Версия миграции: 1 -- Создание таблиц для тестирования

-- ----------------------------
-- Sequence structure for expenses_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."expenses_id_seq";
CREATE SEQUENCE "public"."expenses_id_seq"
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 2147483647
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for users_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."users_id_seq";
CREATE SEQUENCE "public"."users_id_seq"
    INCREMENT 1
    MINVALUE  1
    MAXVALUE 2147483647
    START 1
    CACHE 1;

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS "public"."authorities";
CREATE TABLE "public"."authorities" (
                                        "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                        "authority" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
);

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO "public"."authorities" VALUES ('user', 'ROLE_USER');
INSERT INTO "public"."authorities" VALUES ('jeka', 'ROLE_USER');

-- ----------------------------
-- Table structure for expenses
-- ----------------------------
DROP TABLE IF EXISTS "public"."expenses";
CREATE TABLE "public"."expenses" (
                                     "id" int4 NOT NULL DEFAULT nextval('expenses_id_seq'::regclass),
                                     "user_id" int8,
                                     "amount" numeric,
                                     "date" timestamp(0),
                                     "category" varchar(255) COLLATE "pg_catalog"."default"
);

-- ----------------------------
-- Records of expenses
-- ----------------------------
INSERT INTO "public"."expenses" VALUES (15, 19, 200.00, '2024-02-03 00:00:00', 'Groceries');
INSERT INTO "public"."expenses" VALUES (17, 19, 1250.00, '2024-02-04 00:00:00', 'Entertainment');
INSERT INTO "public"."expenses" VALUES (19, 19, 250.00, '2024-03-04 00:00:00', 'Groceries');
INSERT INTO "public"."expenses" VALUES (20, 19, 30.00, '2024-03-05 00:00:00', 'Groceries');
INSERT INTO "public"."expenses" VALUES (21, 19, 330.00, '2024-03-10 00:00:00', 'Groceries');
INSERT INTO "public"."expenses" VALUES (16, 19, 5250, '2024-02-04 00:00:00', 'Car');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "public"."users";
CREATE TABLE "public"."users" (
                                  "id" int4 NOT NULL DEFAULT nextval('users_id_seq'::regclass),
                                  "email" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                  "username" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                  "password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                  "enabled" bool
);

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO "public"."users" VALUES (19, 'asdf@gmail.com', 'user', '$2a$10$D6klguD0shSDExmV.BAzUeRj03j1NO0IYHrjFv2sMtnW0LrQSjDou', 't');
INSERT INTO "public"."users" VALUES (20, 'asdf@asdf', 'jeka', '$2a$10$s5IL7Mmp9IMhV1EdIwE0C.7lylN9WyAtO5.C6pPeVe3ilsfS.Ihe.', 't');

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."expenses_id_seq"
    OWNED BY "public"."expenses"."id";
SELECT setval('"public"."expenses_id_seq"', 25, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."users_id_seq"
    OWNED BY "public"."users"."id";
SELECT setval('"public"."users_id_seq"', 22, true);

-- ----------------------------
-- Primary Key structure for table expenses
-- ----------------------------
ALTER TABLE "public"."expenses" ADD CONSTRAINT "expenses_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD CONSTRAINT "unique_username" UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table authorities
-- ----------------------------
ALTER TABLE "public"."authorities" ADD CONSTRAINT "fk_authorities_users" FOREIGN KEY ("username") REFERENCES "public"."users" ("username") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table expenses
-- ----------------------------
ALTER TABLE "public"."expenses" ADD CONSTRAINT "expenses_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "public"."users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
