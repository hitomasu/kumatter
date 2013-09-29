package org.leihauoli.kumatter.dto;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class RegistrationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 名 */
	public String firstName;

	/** 姓 */
	public String lastName;

	/** ニックネーム */
	public String nickName;

	/** メールアドレス */
	public String mailAddress;

	/** パスワード */
	public String password;

}
