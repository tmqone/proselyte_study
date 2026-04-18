CREATE SCHEMA IF NOT EXISTS transaction;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE transaction.wallet_types (
                              uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              modified_at TIMESTAMP,
                              name VARCHAR(32) NOT NULL,
                              currency_code VARCHAR(3) NOT NULL,
                              status VARCHAR(18) NOT NULL,
                              archived_at TIMESTAMP,
                              user_type VARCHAR(15),
                              creator VARCHAR(255),
                              modifier VARCHAR(255)
);

CREATE TABLE transaction.wallets (
                         uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         modified_at TIMESTAMP,
                         name VARCHAR(32) NOT NULL,
                         wallet_type_uid UUID NOT NULL REFERENCES wallet_types(uid),
                         user_uid UUID NOT NULL,
                         status VARCHAR(30) NOT NULL,
                         balance DECIMAL NOT NULL DEFAULT 0.0,
                         archived_at TIMESTAMP
);

CREATE TYPE transaction.payment_type AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');
CREATE TABLE transaction.transactions (
                              uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              modified_at TIMESTAMP,
                              user_uid UUID NOT NULL,
                              wallet_uid UUID NOT NULL REFERENCES wallets(uid),
                              amount DECIMAL NOT NULL DEFAULT 0.0,
                              type payment_type NOT NULL,
                              status VARCHAR(32) NOT NULL,
                              comment VARCHAR(256),
                              fee DECIMAL,
                              target_wallet_uid UUID,         -- для transfer
                              payment_method_id BIGINT,       -- для deposit/withdrawal
                              failure_reason VARCHAR(256)
);

CREATE TABLE transaction.transaction_outbox (
    uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    processed_at TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(32) NOT NULL,
    event_type VARCHAR(32) NOT NULL,
    payload jsonb
)
