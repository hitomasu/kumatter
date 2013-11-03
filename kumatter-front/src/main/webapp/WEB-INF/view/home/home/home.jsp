<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;ホーム</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>タイムラインを表示</h4>

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
<s:form method="POST">
  <html:textarea property="tweet" cols="55" rows="3"/>
  <s:submit property="doTweet" value="ツイート" />
</s:form>
<ul class="timeline">
<c:if test="${contextDto.timeLine.size() == 0}"><li>タイムラインに表示する内容はありません。</li></c:if>
<c:forEach var="obj" items="${contextDto.timeLine}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="tweetHistoryId" value="${f:h(obj.tweetHistoryId)}"/>
        <html:hidden property="versionNo" value="${f:h(obj.versionNo)}"/>
　　     <li>${f:h(obj.strRegisterTime)}<br />
            <c:choose>
	            <c:when test="${f:h(loginDto.memberId) != f:h(obj.memberId)}">
	                <s:link href="/home/otherTweet/?memberId=${f:h(obj.memberId)}&query=&backPath=/home/home">@${f:h(obj.nickName)}<br /></s:link>
	            </c:when>
	            <c:otherwise>
                    自分<br />
	            </c:otherwise>
            </c:choose>
            ${f:h(obj.tweet)}
	        <c:if test="${f:h(loginDto.memberId) == f:h(obj.memberId)}">
		        <s:submit property="doTweetDelete" value="削除" />
	        </c:if>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
