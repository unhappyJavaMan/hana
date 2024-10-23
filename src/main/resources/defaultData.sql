INSERT INTO `hana`.`user`
(`account`,
`create_date`,
`email`,
`nick_name`,
`password`,
`phone`,
`status`)
VALUES
(
'admin',
utc_timestamp(),
'admin@admin.com',
'admin',
'admin',
'0000000000',
'Active');
SELECT * FROM hana.user;