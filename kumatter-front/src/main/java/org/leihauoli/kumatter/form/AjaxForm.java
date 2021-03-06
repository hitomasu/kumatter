package org.leihauoli.kumatter.form;

import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class AjaxForm {

	/** 検索クエリ */
	@Required(target = "doMemberSearch,doTweetSearch")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String query;

	/** tweet内容 */
	@Required(target = "doTweet")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String tweet; //TODO Takeshi Kato: ツイート文字数の上限が140ですが、DBのカラム長さを250にしているのには、明確な意図はありますか？
	//TODO Hitoshi Masuzawa:明確な意図はありません。上限ツイート数よりもカラム長をゆとりをもたせたほうがいいのかな、とも思いますが、これはやり過ぎですね。。

	/** tweet履歴ID */
	@Required(target = "doTweetDelete")
	public String tweetHistoryId;
	//TODO 修正済　Takeshi Kato: ActionFormのフィールドは、Stringかboolean(Boolean)のみにするべきです。
	//                   数値以外の値が改ざんされて入力された場合に、意図しないエラーが発生します。
	//                   この場合であれば、数値である事自体をアノテーションでチェックするか、Stringとして受け取ったあとに、自前で数値変換するべきです。
	//TODO Hitoshi Masuzawa:Stringに変更しました。

	/** tweetバージョンナンバー */
	@Required(target = "doTweetDelete")
	public Long versionNo;

	public String name;
	public String age;

}
