<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;フォロワー</title>
</head>
<body>
<h1>kumatter</h1>
<s:form method="POST">
    <p>${f:h(loginDto.firstName)}&nbsp;${f:h(loginDto.lastName)}<br />@${f:h(loginDto.nickName)}<br /><s:submit property="logout" value="ログアウト" /></p>
</s:form>

ツイート：<s:link href="/registration/registration">${f:h(contextDto.tweetCount)}</s:link><br />
フォロー：<s:link href="/registration/registration">${f:h(contextDto.followMemberCount)}</s:link><br />
フォロワー：<s:link href="/registration/registration">${f:h(contextDto.followerMemberCount)}</s:link><br />


<s:form method="POST">
  <html:text property="query"/>
  <s:submit property="dosSarch" value="検索" />
  <html:errors/>
</s:form>


<ul class="timeline">
<c:forEach var="obj" items="${contextDto.followerMemberList}" varStatus="status">
    <s:form method="POST">
        <html:hidden property="memberId" value="${f:h(obj.memberId)}"/>
        <html:hidden property="versionNo" value="${f:h(obj.versionNo)}"/>
　　     <li>${f:h(obj.firstName)}&nbsp;${f:h(obj.lastName)}<br />@${f:h(obj.nickName)}
		<c:choose>
		    <c:when test="${f:h(obj.oneWayFlg)}"><s:submit property="doFollow" value="フォローする" /></c:when>
		    <c:when test="${f:h(obj.oneWayFlg) == false}"><s:submit property="doFollowClear" value="フォロー解除" /></c:when>
		</c:choose>
        </li>
    </s:form>
</c:forEach>
</ul>

</body>
</html>
