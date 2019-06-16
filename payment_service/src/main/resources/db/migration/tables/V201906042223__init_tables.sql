CREATE TABLE PAYMENT_TEMPLATE(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR (256) UNIQUE NOT NULL,
  user_id BIGINT NOT NULL,
  bank_id BIGINT NULL ,
  account_number VARCHAR(256) NULL ,
  recipient_first_name VARCHAR(256) NULL,
  recipient_last_name VARCHAR(256) NULL,
  recipient_bank_name VARCHAR(256) NULL,
  recipient_bank_address VARCHAR(512) NULL,
  recipient_bank_account VARCHAR(256) NULL,
  amount BIGINT NULL,
  currency VARCHAR(12) NULL,
  created_on TIMESTAMP NOT NULL,
  updated_on TIMESTAMP NOT NULL
);

CREATE TABLE PAYMENT(
  id BIGSERIAL PRIMARY KEY,
  status VARCHAR (32) NOT NULL,
  user_id BIGINT NOT NULL,
  bank_id BIGINT NOT NULL ,
  account_number VARCHAR(256) NOT NULL ,
  recipient_first_name VARCHAR(256) NOT NULL,
  recipient_last_name VARCHAR(256) NOT NULL,
  recipient_bank_name VARCHAR(256) NOT NULL,
  recipient_bank_address VARCHAR(512) NOT NULL,
  recipient_bank_account VARCHAR(256) NOT NULL,
  amount BIGINT NOT NULL,
  currency VARCHAR(12) NOT NULL,
  created_on TIMESTAMP NOT NULL,
  updated_on TIMESTAMP NOT NULL
);