CREATE TABLE payment_template(
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
  created_on TIMESTAMP DEFAULT current_timestamp,
  updated_on TIMESTAMP
);

CREATE TABLE payment(
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
  created_on TIMESTAMP DEFAULT current_timestamp,
  updated_on TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_changetimestamp_column()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_on = now();
  RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_payment_template_changetimestamp BEFORE UPDATE
  ON PAYMENT_TEMPLATE FOR EACH ROW EXECUTE PROCEDURE
  update_changetimestamp_column();

CREATE TRIGGER update_payment_changetimestamp BEFORE UPDATE
  ON PAYMENT FOR EACH ROW EXECUTE PROCEDURE
  update_changetimestamp_column();