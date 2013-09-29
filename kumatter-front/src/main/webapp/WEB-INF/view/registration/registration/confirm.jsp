<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;会員登録確認</title>
</head>
<body>
<p>会員登録確認ページ</p>
<s:form method="POST">
<table>
    <tr>
        <th>ファーストネーム（名）：</th>
        <td>${f:h(registrationDto.firstName)}</td>
    </tr>
    <tr>
        <th>ラストネーム（姓）：</th>
        <td>${f:h(registrationDto.lastName)}</td>
    </tr>
    <tr>
        <th>ニックネーム：</th>
        <td>${f:h(registrationDto.nickName)}</td>
    </tr>
    <tr>
        <th>メールアドレス：</th>
        <td>${f:h(registrationDto.mailAddress)}</td>
    </tr>
        <th><s:submit property="goBack" value="戻る" /></th>
        <th><s:submit property="doComplete" value="登録" /></th>
    </tr>
</table>
</s:form>

</body>
</html>