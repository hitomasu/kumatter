SELECT count(*)
FROM   TWEET_HISTORY A
       INNER JOIN MEMBER B
               ON( A.MEMBER_ID = B.MEMBER_ID )
WHERE  A.MEMBER_ID IN /*memberIdList*/( "1", "20", "21" )
       AND A.DEL_FLG = 'N';
