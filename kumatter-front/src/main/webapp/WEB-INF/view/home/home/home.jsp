<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;ホーム</title>
</head>
<body>
<h1>kumatter</h1>
<s:form method="POST">
    <p>${f:h(loginDto.firstName)}&nbsp;${f:h(loginDto.lastName)}<br />@${f:h(loginDto.nickName)}<br /><s:submit property="logout" value="ログアウト" /></p>
</s:form>

ツイート：<s:link href="/relations/registration">${f:h(contextDto.tweetCount)}</s:link><br />
フォロー：<s:link href="/relations/registration">${f:h(contextDto.followMemberCount)}</s:link><br />
フォロワー：<s:link href="/relations/follower">${f:h(contextDto.followerMemberCount)}</s:link><br />


<s:form method="POST">
  <html:textarea property="tweet" cols="50" rows="3"/>
  <s:submit property="doTweet" value="ツイート" />
  <html:errors/>
</s:form>


<ul class="timeline">
<c:forEach var="obj" items="${contextDto.timeLine}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="tweetHistoryId" value="${f:h(obj.tweetHistoryId)}"/>
        <html:hidden property="versionNo" value="${f:h(obj.versionNo)}"/>
　　     <li>${f:h(obj.strRegisterTime)}<br />@${f:h(obj.nickName)}<br />${f:h(obj.tweet)}
        <c:if test="${f:h(loginDto.memberId) == f:h(obj.memberId)}">
	        <s:submit property="doTweetDelete" value="削除" />
        </c:if>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
