CREATE KEYSPACE highrate WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};

use highrate;

CREATE TABLE transfer_requests (
    timestamp timeuuid,
    user_id text,
    currency_from text,
    currency_to text,
    rate float,
    amount_buy float,
    amount_sell float,
    country text,
    time_placed timestamp,
    PRIMARY KEY (country, timestamp)
  );