-- 테스트 계정
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터, 개선 기대
insert into admin_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by,modified_at, modified_by)
values ('song', '{noop}song', 'ADMIN', 'song', 'song@mail.com', 'I am Song.', now(), 'song', now(), 'song'),
       ('mark', '{noop}mark', 'MANAGER', 'Mark', 'mark@mail.com', 'I am Mark.', now(), 'song', now(), 'song'),
       ('susan', '{noop}susan', 'MANAGER,DEVELOPER', 'Susan', 'Susan@mail.com', 'I am Susan.', now(), 'song', now(),'song'),
       ('jim', '{noop}jim', 'USER', 'Jim', 'jim@mail.com', 'I am Jim.', now(), 'song', now(), 'song')
;
