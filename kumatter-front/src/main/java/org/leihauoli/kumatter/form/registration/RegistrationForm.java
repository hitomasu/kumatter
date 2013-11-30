package org.leihauoli.kumatter.form.registration;

import org.seasar.struts.annotation.EmailType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class RegistrationForm {

	/** 名 */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String firstName; //TODO Takeshi Kato: 姓名とニックネームに利用できる文字を絞っているのは、どんな意図がありますか？

	/** 姓 */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String lastName;

	/** ニックネーム */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String nickName;

	/** メールアドレス */
	@Required
	@Maxlength(maxlength = 250)
	@EmailType
	public String mailAddress;

	/** パスワード1 */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String password1; //TODO Takeshi Kato: 短いパスワードを利用されないように、最小文字数も指定した方が良いと思います。

	/** パスワード2 */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String password2;

}
