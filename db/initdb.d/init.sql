CREATE DATABASE IF NOT EXISTS lottery2;

INSERT INTO site_admin(id,password,tel) SELECT 1,'bdd44cbb5a413fe37ead3f207cc4d1ea','88888888' FROM dual WHERE NOT EXISTS (SELECT id FROM site_admin)
