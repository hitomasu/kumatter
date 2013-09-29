<!DOCTYPE html>
<html>
<head>
    <title>kumatter&nbsp;会員登録</title>
</head>
<body>
<p>会員登録ページ</p>
<html:errors/>
<s:form method="POST">
<table>
    <tr>
        <th>ファーストネーム（名）：</th>
        <td><html:text property="firstName"/></td>
    </tr>
    <tr>
        <th>ラストネーム（姓）：</th>
        <td><html:text property="lastName"/></td>
    </tr>
    <tr>
        <th>ニックネーム：</th>
        <td><html:text property="nickName"/></td>
    </tr>
    <tr>
        <th>メールアドレス：</th>
        <td><html:text property="mailAddress"/></td>
    </tr>
    <tr>
        <th>パスワード：</th>
        <td><html:password property="password1"/></td>
    </tr>
    <tr>
        <th>パスワード（再入力）：</th>
        <td><html:password property="password2"/></td>
    </tr>
    <tr>
        <th colspan="2">
            <s:submit property="confirm" value="確認" />
        </th>
    </tr>
</table>
</s:form>
<s:link href="/login/login">ログイン画面に戻る</s:link>
</body>
</html>