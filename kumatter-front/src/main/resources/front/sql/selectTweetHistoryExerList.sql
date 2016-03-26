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
where
       A.DEL_FLG = 'N'
AND
(
/*BEGIN*/
/*IF dto.insertTweetParamDto1 != null*/
OR A.MEMBER_ID = /*dto.insertTweetParamDto1.memberId*/'1'
/*END*/
/*IF dto.insertTweetParamDto2 != null*/
OR A.MEMBER_ID = /*dto.insertTweetParamDto2.memberId*/'1'
/*END*/
/*IF dto.insertTweetParamDto3 != null*/
OR A.MEMBER_ID = /*dto.insertTweetParamDto3.memberId*/'1'
/*END*/
/*END*/
)

ORDER  BY A.REGISTER_TIME DESC;