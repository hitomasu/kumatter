package org.leihauoli.kumatter.dto.result;

import java.io.Serializable;
import java.sql.Date;

public class MemberRelationsResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 関係性ID */
	public long relationsId;
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
	/** 一方通行フラグ */
	public boolean oneWayFlg;

}
