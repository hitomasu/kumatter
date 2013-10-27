<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;他メンバーのツイートを表示</title>
</head>
<body>
<h1><s:link href="/home/home">kumatter</s:link></h1>
<h4>${f:h(memberDto.nickName)}のツイート表示</h4>
<s:form method="POST">
    <p>
        ${f:h(memberDto.firstName)}&nbsp;${f:h(memberDto.lastName)}<br />@${f:h(memberDto.nickName)}<br />
        ツイート：${f:h(contextDto.tweetCount)}<br />
        フォロー：${f:h(contextDto.followMemberCount)}<br />
        フォロワー：${f:h(contextDto.followerMemberCount)}<br />
    </p>
</s:form>
<html:errors/>
<s:form method="POST">
   <html:hidden property="memberId" value="${f:h(memberDto.memberId)}"/>
   <html:hidden property="relationsId" value="${f:h(memberDto.relationsId)}"/>
   <html:hidden property="query" value="${f:h(query)}"/>
   <html:hidden property="backPath" value="${f:h(backPath)}"/>
	<c:choose>
		<c:when test="${f:h(memberDto.followFlg)}">
		   あなたは@${f:h(memberDto.nickName)}をフォローしています。
		   <s:submit property="doDeleteRelations" value="フォロー解除" />
		</c:when>
		<c:otherwise>
		   あなたは@${f:h(memberDto.nickName)}をフォローしていません。
		<s:submit property="doFollow" value="フォローする" />
		</c:otherwise>
	</c:choose>
</s:form>

<s:form method="POST">
	<html:hidden property="query" value="${f:h(query)}"/>
	<html:hidden property="backPath" value="${f:h(backPath)}"/>
	<s:submit property="goBack" value="前画面に戻る" />
</s:form>

  <ul class="timeline">
<c:if test="${contextDto.timeLine.size() == 0}"><li>ツイートがありません。</li></c:if>
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
