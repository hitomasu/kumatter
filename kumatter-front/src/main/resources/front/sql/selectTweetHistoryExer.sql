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
/*IF insertTweetParamDto1 != null*/
		and A.MEMBER_ID = /*insertTweetParamDto1.memberId*/'4'
/*END*/
/*IF insertTweetParamDto2 != null*/
		and A.MEMBER_ID = /*insertTweetParamDto2.memberId*/'4'
/*END*/
/*IF insertTweetParamDto3 != null*/
		and A.MEMBER_ID = /*insertTweetParamDto3.memberId*/'4'
/*END*/

ORDER  BY A.REGISTER_TIME DESC;