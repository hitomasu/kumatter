package org.leihauoli.kumatter.form.home;

import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class HomeForm {

	/** 検索クエリ */
	@Required(target = "doMemberSearch,doTweetSearch")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String query;

	/** tweet内容 */
	@Required(target = "doTweet")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String tweet;

	/** tweet履歴ID */
	@Required(target = "doTweetDelete")
	public Long tweetHistoryId;

	/** tweetバージョンナンバー */
	@Required(target = "doTweetDelete")
	public Long versionNo;

}
