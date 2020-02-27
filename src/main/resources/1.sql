

INSERT INTO ACCOUNT_TRANSACTION (ACC_NO, TRX_DT, TRX_NO, CNCL_YN, FEE, TRX_AMT)
SELECT 계좌번호,거래일자,거래번호,취소여부,수수료,금액
FROM csvread('classpath:data/데이터_거래내역.csv');

INSERT INTO ACCOUNT_INFO
SELECT *
FROM csvread('classpath:data/데이터_계좌정보.csv');

INSERT INTO BRAN_INFO
SELECT *
FROM csvread('classpath:data/데이터_관리점정보.csv');
