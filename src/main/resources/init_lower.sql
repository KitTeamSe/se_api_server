insert into authority (authority_id, name_eng, name_kor) values (1,'ACCOUNT_ACCESS','계정_접근');
insert into authority (authority_id, name_eng, name_kor) values (2,'ACCOUNT_MANAGE','계정_관리');
insert into authority (authority_id, name_eng, name_kor) values (3,'MENU_MANAGE','메뉴_관리');
insert into authority (authority_id, name_eng, name_kor) values (4,'FILE_ACCESS','파일_접근');
insert into authority (authority_id, name_eng, name_kor) values (5,'FILE_MANAGE','파일_관리');
insert into authority (authority_id, name_eng, name_kor) values (6,'TAG_ACCESS','태그_접근');
insert into authority (authority_id, name_eng, name_kor) values (7,'TAG_MANAGE','태그_관리');
insert into authority (authority_id, name_eng, name_kor) values (8,'SEARCH_ACCESS','검색_접근');
insert into authority (authority_id, name_eng, name_kor) values (9,'JOB_ACCESS','취업_접근');
insert into authority (authority_id, name_eng, name_kor) values (10,'JOB_MANAGE','취업_관리');
insert into authority (authority_id, name_eng, name_kor) values (11,'STATICS_MANAGE','통계_관리');
insert into authority (authority_id, name_eng, name_kor) values (12,'ACCESS_MANAGE','접근_관리');
insert into authority (authority_id, name_eng, name_kor) values (13,'CAREER_ACCESS','이력_접근');
insert into authority (authority_id, name_eng, name_kor) values (14,'CAREER_MANAGE','이력_관리');
insert into authority (authority_id, name_eng, name_kor) values (15,'REPORT_ACCESS','신고_접근');
insert into authority (authority_id, name_eng, name_kor) values (16,'REPORT_MANAGE','신고_관리');
insert into authority (authority_id, name_eng, name_kor) values (17,'AUTHORITY_MANAGE','권한_관리');
insert into authority (authority_id, name_eng, name_kor) values (18,'SCHEDULE_MANAGE','시간표_관리');


insert into authority_group (authority_group_id, description, name, type) values (1,'비로그인 사용자','DEFAULT', 'ANONYMOUS');
insert into authority_group (authority_group_id, description, name, type) values (2,'로그인 사용자','MEMBER', 'NORMAL');
insert into authority_group (authority_group_id, description, name, type) values (3,'로그인 관리자','ADMIN', 'DEFAULT');

insert into question (question_id, text) values (1, '다른 이메일 주소는?');
insert into question (question_id, text) values (2, '나의 보물 1호는 ?');
insert into question (question_id, text) values (3, '나의 출신 초등학교는?');
insert into question (question_id, text) values (4, '나의 출신 고향은?');
insert into question (question_id, text) values (5, '나의 이상형은?');
insert into question (question_id, text) values (6, '어머니 성함은?');
insert into question (question_id, text) values (7, '아버지 성함은?');
insert into question (question_id, text) values (8, '가장 좋아하는 색깔은?');
insert into question (question_id, text) values (9, '가장 좋아하는 음식은?');

insert into account (account_id, email, id_string, information_open_agree, student_id, name, nickname, password, phone_number, type, question_id, answer)
values (1, 'djh20@naver.com', 'user', 'AGREE', '00000000', 'user', 'user', '{bcrypt}$2a$10$kQ7ZjT0yfgCDfD2pPRPdCu81ZPee.GCeuwniYdDS0dmr7889MGYZO'
, '00000000000', 'STUDENT', 4, '구미');

insert into account (account_id, email, id_string, information_open_agree, student_id, name, nickname, password, phone_number, type, question_id, answer)
values (2, 'admin@admin.com', 'admin', 'AGREE', '00000001', 'admin', 'admin', '{bcrypt}$2a$10$kQ7ZjT0yfgCDfD2pPRPdCu81ZPee.GCeuwniYdDS0dmr7889MGYZO'
, '00000000001', 'ASSISTANT',  4, '구미');



insert into authority_group_account_mapping (authority_group_account_mapping_id, authority_group_id, account_id) values (1,2,1);
insert into authority_group_account_mapping (authority_group_account_mapping_id, authority_group_id, account_id) values (2,3,2);


insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (1,4,1);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (2,8,1);

insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (3,1,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (4,4,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (5,8,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (6,9,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (7,13,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (8,15,2);


insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (9,1,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (10,2,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (11,3,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (12,4,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (13,5,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (14,6,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (15,7,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (16,8,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (17,9,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (18,10,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (19,11,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (20,12,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (21,13,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (22,14,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (23,15,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (24,16,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (25,17,3);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (26,18,3);

insert into period (period_id, period_order, name, start_time, end_time) values (1, 1, '1', '09:00:00', '09:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (2, 2, '2', '10:00:00', '10:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (3, 3, '3', '11:00:00', '11:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (4, 4, '4', '12:00:00', '12:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (5, 5, '5', '13:00:00', '13:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (6, 6, '6', '14:00:00', '14:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (7, 7, '7', '15:00:00', '15:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (8, 8, '8', '16:00:00', '16:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (9, 9, '9', '17:00:00', '17:50:00');
insert into period (period_id, period_order, name, start_time, end_time) values (10, 10, 'A', '18:00:00', '18:45:00');
insert into period (period_id, period_order, name, start_time, end_time) values (11, 11, 'B', '18:55:00', '19:40:00');
insert into period (period_id, period_order, name, start_time, end_time) values (12, 12, 'C', '19:50:00', '20:35:00');
insert into period (period_id, period_order, name, start_time, end_time) values (13, 13, 'D', '20:45:00', '21:30:00');
insert into period (period_id, period_order, name, start_time, end_time) values (14, 14, 'E', '21:40:00', '22:25:00');

insert into authority (authority_id, name_eng, name_kor) values (20, 'freeboard', '자유게시판');
insert into menu (menu_id, description, menu_order, menu_type, name_eng, name_kor, url, authority_id) values (1, '자유게시판', 1, 'BOARD', 'freeboard', '자유게시판', 'freeboard', 20);
insert into board (board_id, name_eng, name_kor, menu_id) values (1, 'freeboard', '자유게시판', 1);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (28,20,1);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (29,20,2);
insert into authority_group_authority_mapping (authority_group_authority_mapping_id, authority_id, authority_group_id) values (30,20,3);

insert into tag (tag_id, text) values (1, '1학년');
insert into tag (tag_id, text) values (2, '2학년');
insert into tag (tag_id, text) values (3, '3학년');
insert into tag (tag_id, text) values (4, '4학년');