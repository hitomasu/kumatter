SELECT B.RELATIONS_ID,
       B.FOLLOW_MEMBER_ID,
       B.FOLLOWER_MEMBER_ID,
       A.MEMBER_ID,
       A.FIRST_NAME,
       A.LAST_NAME,
       A.NICK_NAME,
       A.MAIL_ADDRESS,
       A.DEL_FLG,
       A.REGISTER_TIME,
       A.UPDATE_TIME,
       A.VERSION_NO
FROM   (SELECT *
        FROM   MEMBER
        WHERE  NOT MEMBER_ID = /*memberId*/"1") A
       LEFT JOIN (SELECT RELATIONS_ID,
                         FOLLOW_MEMBER_ID,
                         FOLLOWER_MEMBER_ID
                  FROM   MEMBER_RELATIONS
                  WHERE  FOLLOWER_MEMBER_ID = /*memberId*/"1") B
              ON A.MEMBER_ID = B.FOLLOW_MEMBER_ID
WHERE  A.FIRST_NAME LIKE /*query*/"kuma"
    OR A.LAST_NAME LIKE /*query*/"kuma"
    OR A.NICK_NAME LIKE /*query*/"kuma"
       AND A.DEL_FLG = 'N'
ORDER  BY A.MEMBER_ID ASC;