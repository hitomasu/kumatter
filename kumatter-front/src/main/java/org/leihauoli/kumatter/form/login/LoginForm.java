package org.leihauoli.kumatter.form.login;

import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class LoginForm {

	/** ID */
	@Required
	@Maxlength(maxlength = 250)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String id;

	//TODO Takeshi Kato: 上記の「id」ですが、実際にはニックネームかメールアドレスなのだと思います。
	//                   であれば、「loginKey」などのように変数名をもうちょっと考えた方が良いと思います。
	//                   「id」だと、どうしても会員IDを連想してしまいます。

	/** パスワード */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String password;
}
