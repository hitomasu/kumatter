<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;フォロー</title>
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


<s:form method="POST">
  <html:text property="query"/>
  <s:submit property="doMemberSearch" value="メンバー検索" />
  <html:errors/>
</s:form>


<ul class="timeline">
<c:if test="${searchMemberList.size() == 0}"><li>検索結果に一致するメンバーは見つかりませんでした。</li></c:if>
<c:forEach var="obj" items="${searchMemberList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="relationsId" value="${f:h(obj.relationsId)}"/>
        <html:hidden property="hiddenQuery"/>
　　     <li>${f:h(obj.firstName)}&nbsp;${f:h(obj.lastName)}<br />@${f:h(obj.nickName)}
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
