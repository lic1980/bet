CREATE DATABASE IF NOT EXISTS lottery2;

INSERT INTO site_admin(id,password,tel) SELECT 1,'fea51dd140feafd57b1b8a48a40e3564','88888888' FROM dual WHERE NOT EXISTS (SELECT id FROM site_admin);
