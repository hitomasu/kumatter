UPDATE TWEET_HISTORY
SET    DEL_FLG = 'Y',
       VERSION_NO = VERSION_NO + 1
WHERE  TWEET_HISTORY_ID = /*tweetHistoryId*/"1"
   AND VERSION_NO = /*versionNo*/"0";