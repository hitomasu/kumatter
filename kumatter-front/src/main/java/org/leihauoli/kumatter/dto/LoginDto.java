package org.leihauoli.kumatter.dto;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class LoginDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** メンバーID */
	public Long memberId;
	/** ファーストネーム*/
	public String firstName;
	/** ラストネーム */
	public String lastName;
	/** ニックネーム */
	public String nickName;
	/** メールアドレス */
	public String mailAddress;

}
