INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (1,'ACCOUNT_ACCESS','계정_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (2,'ACCOUNT_MANAGE','계정_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (3,'MENU_MANAGE','메뉴_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (4,'FILE_ACCESS','파일_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (5,'FILE_MANAGE','파일_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (6,'TAG_ACCESS','태그_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (7,'TAG_MANAGE','태그_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (8,'SEARCH_ACCESS','검색_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (9,'JOB_ACCESS','취업_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (10,'JOB_MANAGE','취업_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (11,'STATICS_MANAGE','통계_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (12,'ACCESS_MANAGE','접근_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (13,'CAREER_ACCESS','이력_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (14,'CAREER_MANAGE','이력_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (15,'REPORT_ACCESS','신고_접근');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (16,'REPORT_MANAGE','신고_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (17,'AUTHORITY_MANAGE','권한_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (18,'SCHEDULE_MANAGE','시간표_관리');
INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (19,'NOTICE_MANAGE','알림_관리');


INSERT INTO AUTHORITY_GROUP (AUTHORITY_GROUP_ID, DESCRIPTION, NAME, TYPE) VALUES (1,'비로그인 사용자','ANONYMOUS', 'ANONYMOUS');
INSERT INTO AUTHORITY_GROUP (AUTHORITY_GROUP_ID, DESCRIPTION, NAME, TYPE) VALUES (2,'로그인 사용자','MEMBER','DEFAULT');
INSERT INTO AUTHORITY_GROUP (AUTHORITY_GROUP_ID, DESCRIPTION, NAME, TYPE) VALUES (3,'로그인 관리자','ADMIN','SYSTEM');

INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (1, '다른 이메일 주소는?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (2, '나의 보물 1호는 ?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (3, '나의 출신 초등학교는?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (4, '나의 출신 고향은?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (5, '나의 이상형은?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (6, '어머니 성함은?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (7, '아버지 성함은?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (8, '가장 좋아하는 색깔은?');
INSERT INTO QUESTION (QUESTION_ID, TEXT) VALUES (9, '가장 좋아하는 음식은?');

INSERT INTO ACCOUNT (ACCOUNT_ID, EMAIL, ID_STRING, INFORMATION_OPEN_AGREE, STUDENT_ID, NAME, NICKNAME, PASSWORD, PHONE_NUMBER, TYPE, QUESTION_ID, ANSWER)
VALUES (1, 'djh20@naver.com', 'user', 'AGREE', '00000000', 'user', 'user', '{bcrypt}$2a$10$kQ7ZjT0yfgCDfD2pPRPdCu81ZPee.GCeuwniYdDS0dmr7889MGYZO'
, '00000000000', 'STUDENT', 4, '구미');

INSERT INTO ACCOUNT (ACCOUNT_ID, EMAIL, ID_STRING, INFORMATION_OPEN_AGREE, STUDENT_ID, NAME, NICKNAME, PASSWORD, PHONE_NUMBER, TYPE, QUESTION_ID, ANSWER)
VALUES (2, 'admin@admin.com', 'admin', 'AGREE', '00000001', 'admin', 'admin', '{bcrypt}$2a$10$kQ7ZjT0yfgCDfD2pPRPdCu81ZPee.GCeuwniYdDS0dmr7889MGYZO'
, '00000000001', 'ASSISTANT',  4, '구미');



INSERT INTO AUTHORITY_GROUP_ACCOUNT_MAPPING (AUTHORITY_GROUP_ACCOUNT_MAPPING_ID, AUTHORITY_GROUP_ID, ACCOUNT_ID) VALUES (1,2,1);
INSERT INTO AUTHORITY_GROUP_ACCOUNT_MAPPING (AUTHORITY_GROUP_ACCOUNT_MAPPING_ID, AUTHORITY_GROUP_ID, ACCOUNT_ID) VALUES (2,3,2);


INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (1,4,1);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (2,8,1);

INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (3,1,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (4,4,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (5,6,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (6,8,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (7,9,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (8,13,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (9,15,2);


INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (10,1,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (11,2,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (12,3,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (13,4,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (14,5,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (15,6,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (16,7,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (17,8,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (18,9,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (19,10,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (20,11,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (21,12,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (22,13,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (23,14,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (24,15,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (25,16,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (26,17,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (27,18,3);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (28,19,3);

INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (1, 1, '1', '09:00:00', '09:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (2, 2, '2', '10:00:00', '10:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (3, 3, '3', '11:00:00', '11:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (4, 4, '4', '12:00:00', '12:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (5, 5, '5', '13:00:00', '13:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (6, 6, '6', '14:00:00', '14:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (7, 7, '7', '15:00:00', '15:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (8, 8, '8', '16:00:00', '16:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (9, 9, '9', '17:00:00', '17:50:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (10, 10, 'A', '18:00:00', '18:45:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (11, 11, 'B', '18:55:00', '19:40:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (12, 12, 'C', '19:50:00', '20:35:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (13, 13, 'D', '20:45:00', '21:30:00');
INSERT INTO PERIOD (PERIOD_ID, PERIOD_ORDER, NAME, START_TIME, END_TIME) VALUES (14, 14, 'E', '21:40:00', '22:25:00');

INSERT INTO AUTHORITY (AUTHORITY_ID, NAME_ENG, NAME_KOR) VALUES (20, 'freeboard', '자유게시판');
INSERT INTO MENU (MENU_ID, DESCRIPTION, MENU_ORDER, MENU_TYPE, NAME_ENG, NAME_KOR, URL, AUTHORITY_ID) VALUES (1, '자유게시판', 1, 'BOARD', 'freeboard', '자유게시판', 'freeboard', 20);
INSERT INTO BOARD (BOARD_ID, NAME_ENG, NAME_KOR, MENU_ID) VALUES (1, 'freeboard', '자유게시판', 1);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (29,20,1);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (30,20,2);
INSERT INTO AUTHORITY_GROUP_AUTHORITY_MAPPING (AUTHORITY_GROUP_AUTHORITY_MAPPING_ID, AUTHORITY_ID, AUTHORITY_GROUP_ID) VALUES (31,20,3);

INSERT INTO TAG (TAG_ID, TEXT) VALUES (1, '1학년');
INSERT INTO TAG (TAG_ID, TEXT) VALUES (2, '2학년');
INSERT INTO TAG (TAG_ID, TEXT) VALUES (3, '3학년');
INSERT INTO TAG (TAG_ID, TEXT) VALUES (4, '4학년');