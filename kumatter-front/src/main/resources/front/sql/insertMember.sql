INSERT INTO MEMBER (
        FIRST_NAME
        ,LAST_NAME
        ,NICK_NAME
        ,MAIL_ADDRESS
        ,DEL_FLG
        ,REGISTER_TIME
        ,UPDATE_TIME
        ,VERSION_NO
) VALUES (
        /*firstName*/"hitoshi"
        ,/*lastName*/"masuzawa"
        ,/*nickName*/"massu"
        ,/*mailAddress*/"h.masuzawa@leihauori.com"
        ,'N'
        ,NOW()
        ,NOW()
        ,0
        )