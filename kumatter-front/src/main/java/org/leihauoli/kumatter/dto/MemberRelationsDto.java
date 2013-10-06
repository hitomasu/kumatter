package org.leihauoli.kumatter.dto;

import java.sql.Date;

public class MemberRelationsDto {

	/** 関係性ID */
	public long relationsId;
	/** フォローメンバーID */
	public long followMemberId;
	/** フォロワーメンバーID */
	public long followerMemberId;
	/** 作成日時 */
	public Date registerTime;
	/** 更新日時 */
	public Date updateTime;
	/** バージョン番号 */
	public long versionNo;
}
