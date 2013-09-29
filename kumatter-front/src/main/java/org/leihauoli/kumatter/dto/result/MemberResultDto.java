package org.leihauoli.kumatter.dto.result;

import java.sql.Date;

public class MemberResultDto {

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

}
