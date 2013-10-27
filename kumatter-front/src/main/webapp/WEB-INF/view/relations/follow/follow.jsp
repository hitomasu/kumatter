<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;フォロー</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>フォローしているメンバーを表示</h4>
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
<c:if test="${contextDto.followMemberList.size() == 0}"><li>フォローしているメンバーはいません。</li></c:if>
<c:forEach var="obj" items="${contextDto.followMemberList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="relationsId" value="${f:h(obj.relationsId)}"/>
　　     <li>
            ${f:h(obj.firstName)}&nbsp;${f:h(obj.lastName)}<br />
            <s:link href="/home/otherTweet/?memberId=${f:h(obj.memberId)}&query=&backPath=/relations/follow">@${f:h(obj.nickName)}</s:link>
            <s:submit property="doDeleteRelations" value="フォロー解除" />
            <c:choose>
                <c:when test="${f:h(obj.oneWayFlg)}">※フォローされていません</c:when>
                <c:otherwise>※相互フォロー中</c:otherwise>
            </c:choose>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
