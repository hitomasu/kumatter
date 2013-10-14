package org.leihauoli.kumatter.dto.result;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class TweetHistoryResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ツイートID */
	public Long tweetHistoryId;
	/** メンバーID */
	public Long memberId;
	/** ニックネーム */
	public String nickName;
	/** ツイート */
	public String tweet;
	/** 削除フラグ */
	public String delFlg;
	/** 作成日時 */
	public Timestamp registerTime;
	/** 作成日時(文字列) */
	public String strRegisterTime;
	/** 更新日時 */
	public Date updateTime;
	/** バージョンナンバー */
	public Long versionNo;
}
