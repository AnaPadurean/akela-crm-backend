CREATE TABLE IF NOT EXISTS akl_password_reset_token (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  token_hash VARCHAR(64) NOT NULL,
  expires_at TIMESTAMPTZ NOT NULL,
  used_at TIMESTAMPTZ NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_akl_prt_token_hash
  ON akl_password_reset_token (token_hash);

CREATE INDEX IF NOT EXISTS ix_akl_prt_user_id
  ON akl_password_reset_token (user_id);

CREATE INDEX IF NOT EXISTS ix_akl_prt_expires_at
  ON akl_password_reset_token (expires_at);

ALTER TABLE akl_password_reset_token
  ADD CONSTRAINT fk_akl_prt_user
  FOREIGN KEY (user_id) REFERENCES akl_user (id)
  ON DELETE CASCADE;
