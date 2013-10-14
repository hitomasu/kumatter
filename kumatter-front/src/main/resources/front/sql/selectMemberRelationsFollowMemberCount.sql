SELECT COUNT(*)
FROM   MEMBER A
       INNER JOIN (SELECT RELATIONS_ID,
                          FOLLOW_MEMBER_ID,
                          FOLLOWER_MEMBER_ID
                   FROM   MEMBER_RELATIONS
                   WHERE  FOLLOWER_MEMBER_ID = /*memberId*/"1") B
               ON A.MEMBER_ID = B.FOLLOW_MEMBER_ID
WHERE  A.DEL_FLG = 'N';