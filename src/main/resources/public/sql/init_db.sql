CREATE TABLE IF NOT EXISTS webshop
(
  ws_id SERIAL PRIMARY KEY,
  ws_name VARCHAR UNIQUE,
  apikey VARCHAR UNIQUE
);


CREATE TABLE IF NOT EXISTS webshopAnalytics
(
  an_id SERIAL PRIMARY KEY,
  webshop_id INT REFERENCES webshop(ws_id),
  session_id VARCHAR NOT NULL,
  visit_start TIMESTAMP NOT NULL,
  visit_end TIMESTAMP,
  location VARCHAR,
  amount FLOAT NULL,
  currency VARCHAR NULL
);