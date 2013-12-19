SELECT B.RELATIONS_ID,
       A.MEMBER_ID,
       A.FIRST_NAME,
       A.LAST_NAME,
       A.NICK_NAME,
       A.MAIL_ADDRESS,
       A.DEL_FLG,
       A.REGISTER_TIME,
       A.UPDATE_TIME,
       A.VERSION_NO
FROM   MEMBER A
       INNER JOIN MEMBER_RELATIONS B
               ON A.MEMBER_ID = B.FOLLOWER_MEMBER_ID
WHERE  A.DEL_FLG = 'N'
   AND B.FOLLOW_MEMBER_ID = /*memberId*/"1"
ORDER  BY A.MEMBER_ID ASC;

/* TODO Hitoshi Masuzawa:
   修正しました。ご指摘の通りです。
   SQL構文の知識がお粗末すぎました。
 */

/* TODO Takesh Kato
 MEMBER_RELATIONSをSELECTしてViewを作ったものをINNER JOINさせる理由が見当たりませんので、
 以下のように書いた方がシンプルですし、２回SELECT文が発行されないので高速です。
 ※データが大量にあり、特殊な状況の場合には、上記のように２回発行した方が高速な場合もありますが、それを意図したケースではないと思いますので。

　考え方としては、最終的に取得したい情報を持つテーブルをFrom句に記載し、
  絞り込み条件は、Where句に記載し、
  関連して別テーブルの情報を取得したい場合や、別テーブルの情報で絞り込みを行いたい場合には、JOINを行い、
  別テーブルの取得したい情報は、SELECT句に、
  別テーブルで絞込みをしたい情報は、Where句に書くのが良いと思います。
  ※ただし、別テーブルの絞り込み条件に関しては、ON句に書くという手もあります。

SELECT
       B.RELATIONS_ID,
       A.MEMBER_ID,
       A.FIRST_NAME,
       A.LAST_NAME,
       A.NICK_NAME,
       A.MAIL_ADDRESS,
       A.DEL_FLG,
       A.REGISTER_TIME,
       A.UPDATE_TIME,
       A.VERSION_NO
FROM MEMBER MEM
	INNER JOIN MEMBER_RELATIONS REL ON MEM.MEMBER_ID = REL.MEMBER_ID
WHERE
	MEM.DEL_FLG = 'N'
	AND REL.FOLLOW_MEMBER_ID = "1"
ORDER  BY MEM.MEMBER_ID ASC;
*/
