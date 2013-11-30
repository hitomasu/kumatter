package org.leihauoli.kumatter.dto.result;

import java.io.Serializable;
import java.sql.Date;

public class MemberRelationsResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 関係性ID */
	public Long relationsId; //TODO Takeshi Kato: 他の変数もそうですが、NULLになる事がない値なのであれば、longにした方が良いです。
	//                                            そうすると、NULLが入らないのだという意図を、言語レベルで表現できます。

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
	/** 削除フラグ */
	public String delFlg;
	/** 作成日時 */
	public Date registerTime;
	/** 更新日時 */
	public Date updateTime;
	/** バージョンナンバー */
	public Long versionNo;
	/** フォローフラグ */
	public boolean followFlg;
	/** 一方通行フラグ */
	public boolean oneWayFlg;
	/** 逆方向からの関係ID */
	public Long reverseRelationsId;

}
