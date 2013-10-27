<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;フォロワー</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>フォロワーを表示</h4>
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
<c:if test="${contextDto.followerMemberList.size() == 0}"><li>フォローされているメンバーはいません。</li></c:if>
<c:forEach var="obj" items="${contextDto.followerMemberList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="relationsId" value="${f:h(obj.reverseRelationsId)}"/>
　　     <li>
            ${f:h(obj.firstName)}&nbsp;${f:h(obj.lastName)}<br />
            <s:link href="/home/otherTweet/?memberId=${f:h(obj.memberId)}&query=&backPath=/relations/follower">@${f:h(obj.nickName)}</s:link>
			<c:choose>
			    <c:when test="${f:h(obj.oneWayFlg)}"><s:submit property="doFollow" value="フォローする" />&nbsp;※フォローしていません</c:when>
			    <c:when test="${f:h(obj.oneWayFlg) == false}"><s:submit property="doDeleteRelations" value="フォロー解除" />&nbsp;※相互フォロー中</c:when>
			</c:choose>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
