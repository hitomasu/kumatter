SELECT A.TWEET_HISTORY_ID,
       A.MEMBER_ID,
       B.NICK_NAME,
       A.TWEET,
       A.DEL_FLG,
       A.REGISTER_TIME,
       A.UPDATE_TIME,
       A.VERSION_NO
FROM   TWEET_HISTORY A
       INNER JOIN MEMBER B
               ON( A.MEMBER_ID = B.MEMBER_ID )
WHERE  A.MEMBER_ID IN /*memberIdList*/( "1", "20", "21" )
       AND A.DEL_FLG = 'N'
ORDER  BY A.REGISTER_TIME DESC;