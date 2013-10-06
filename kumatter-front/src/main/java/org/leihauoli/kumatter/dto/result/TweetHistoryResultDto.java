package org.leihauoli.kumatter.dto.result;

import java.sql.Date;

public class TweetHistoryResultDto {

	/** ツイートID */
	public Long tweetHitory;
	/** メンバーID */
	public Long memberId;
	/** ツイート */
	public String tweet;
	/** 削除フラグ */
	public String delFlg;
	/** 作成日時 */
	public Date registerTime;
	/** 更新日時 */
	public Date updateTime;
	/** バージョンナンバー */
	public Long versionNo;
}
