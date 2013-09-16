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

	/** パスワード */
	@Required
	@Maxlength(maxlength = 30)
	@Mask(mask = "^[a-zA-Z0-9!-/:-@¥[-`{-~]+$")
	public String pass;
}
