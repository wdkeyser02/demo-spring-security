INSERT INTO role(
	 active, created_at, created_by, updated_at, updated_by, description, name, is_default)
	VALUES (true, now(), 1, now(), 1, 'He/She has highest Privileges', 'SUPER_ADMIN', true);
	
INSERT INTO client(id, authorization_grant_types, client_authentication_methods, client_id, client_id_issued_at, client_name, 
client_secret, client_secret_expires_at, client_settings, redirect_uris, scopes, token_settings) 
VALUES('demo-client', 'refresh_token,client_credentials,authorization_code', 'client_secret_basic',
'demo-client', null, 'demo', '$2a$12$ngzfuVDXy2XhNaGtqFlGF.91JsXehU.WChE18HY.yJBeCjtHHe8EW', null, 
'{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}', 
'http://127.0.0.1:8905/demo/oauth2/code/client', 'read,openid,profile', 
'{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,
"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],
"settings.token.access-token-time-to-live":["java.time.Duration",86400.000000000],
"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat",
"value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],
"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000]}');

INSERT INTO USER_LOGIN(
	ENTITY_ID, ENTITY_TYPE, EMAIL,contact_number, PASSWORD, ROLE_ID, ACTIVE, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY)
	VALUES (null,null,'demo@gmail.com', '1234567890','$2a$12$FBZEnBrtffT35r/OEZfUJO6hbT62hExEzCtYCbGXXkr2SjChIi9r6',1,true, now(), 1,now(), 1);
