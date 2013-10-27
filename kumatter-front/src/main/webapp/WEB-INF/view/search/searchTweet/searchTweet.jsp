<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;ツイート検索</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>ツイート検索</h4>
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
	<c:when test="${searchTweetList.size() == 0}"><li>検索結果に一致するツイートは見つかりませんでした。</li></c:when>
	<c:when test="${searchTweetList.size() == null}"></c:when>
	<c:otherwise>${searchTweetList.size()}件ヒットしました</c:otherwise>
</c:choose>
<c:forEach var="obj" items="${searchTweetList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="relationsId" value="${f:h(obj.relationsId)}"/>
        <html:hidden property="tweetHistoryId" value="${f:h(obj.tweetHistoryId)}"/>
        <html:hidden property="versionNo" value="${f:h(obj.versionNo)}"/>
        <html:hidden property="hiddenQuery"/>
　　     <li>${f:h(obj.strRegisterTime)}<br />
			<c:choose>
				<c:when test="${f:h(loginDto.memberId) == f:h(obj.memberId)}">自分<br /></c:when>
				<c:otherwise><s:link href="/home/otherTweet/?memberId=${f:h(obj.memberId)}&query=${f:h(hiddenQuery)}&backPath=/search/searchTweet/">@${f:h(obj.nickName)}</s:link>
					<c:choose>
		                <c:when test="${f:h(obj.followFlg)}"><s:submit property="doDeleteRelations" value="フォロー解除" /><br /></c:when>
		                <c:otherwise><s:submit property="doFollow" value="フォローする" /><br /></c:otherwise>
	                </c:choose>
				</c:otherwise>
			</c:choose>
			${f:h(obj.tweet)}
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
