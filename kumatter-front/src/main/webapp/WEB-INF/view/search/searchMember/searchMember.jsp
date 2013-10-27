<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;メンバー検索</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>メンバー検索</h4>
<s:form method="POST">
    <p>
        ${f:h(loginDto.firstName)}&nbsp;${f:h(loginDto.lastName)}<br />@${f:h(loginDto.nickName)}&nbsp;<s:submit property="logout" value="ログアウト" /><br />
        ツイート：<s:link href="/home/myTweet">${f:h(contextDto.tweetCount)}</s:link><br />
        フォロー：<s:link href="/relations/follow">${f:h(contextDto.followMemberCount)}</s:link><br />
        フォロワー：<s:link href="/relations/follower">${f:h(contextDto.followerMemberCount)}</s:link><br />
    </p>
</s:form>
<html:errors/>
<s:form method="POST">
  <html:text size="40" property="query"/>
  <s:submit property="doTweetSearch" value="ツイート検索" />
  <s:submit property="doMemberSearch" value="メンバー検索" />
</s:form>

<ul class="timeline">
<c:choose>
    <c:when test="${searchMemberList.size() == 0}"><li>検索結果に一致するメンバーは見つかりませんでした。</li></c:when>
    <c:when test="${searchMemberList.size() == null}"></c:when>
    <c:otherwise>${searchMemberList.size()}件ヒットしました</c:otherwise>
</c:choose>
<c:forEach var="obj" items="${searchMemberList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="relationsId" value="${f:h(obj.relationsId)}"/>
        <html:hidden property="hiddenQuery"/>
　　     <li>
            ${f:h(obj.firstName)}&nbsp;${f:h(obj.lastName)}<br />
            <s:link href="/home/otherTweet/?memberId=${f:h(obj.memberId)}&query=${f:h(hiddenQuery)}&backPath=/search/searchMember/">@${f:h(obj.nickName)}</s:link>
            <c:choose>
                <c:when test="${f:h(obj.followFlg)}"><s:submit property="doDeleteRelations" value="フォロー解除" /></c:when>
                <c:otherwise><s:submit property="doFollow" value="フォローする" /></c:otherwise>
            </c:choose>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
