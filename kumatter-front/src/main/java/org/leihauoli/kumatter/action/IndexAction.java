package org.leihauoli.kumatter.action;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.MemberTestDto;
import org.leihauoli.kumatter.form.IndexForm;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class IndexAction {

	//TODO Takeshi Kato:ERDに関しての指摘事項ですが、適切な場所が無かったので、こちらにコメント入れさせてもらいます。
	//                  会員情報テーブルに関して、会員ID+削除フラグ＋ニックネームなどで複合ユニークキーを作成していますが、
	//                  会員ID自体がPKでユニークになっています。上記の複合ユニークキーは実質意味のないものになっていますが、
	//                  何か作成した意図はありますか？
	//                  また、メールアドレスがユニークになっていないのは意図したものでしょうか？

	//TODO Takeshi Kato: 他に書くところがないので、ここに書いちゃいますね。
	//                   今の作りだと、ログ出力が設計できていないですね。そちらも余裕があればチャレンジしてみてください。
	//                   エラーが発生した場合などでも、現在はログが吐かれていないと思います。
	//                   Eclipseのコンソールパネルには、スタックトレースが出ているでしょうが、ログファイルは吐かれていません。
	//                   実際に作成したアプリをリリースして、運用するフェーズに入った時には、ログ出力もちゃんとされるようにしておかないと、
	//                   ユーザーの行動解析や、障害発生時の原因切り分けなどが出来なくなってしまいます。
	//                   そもそも障害ログが出ていないので、障害監視する事も出来ないですね。
	//                   RではExceptionHandleFilterを作って、障害ログ出力を管理しています。
	//                   同じようなアプローチで良いと思いますので、余裕があればチャレンジしてみてください。

	@Resource
	@ActionForm
	public IndexForm indexForm;

	@Resource
	protected JdbcManager jdbcManager;

	//TODO Takeshi Kato: 必須ではないですが、下記のアノテーションを付与しておかないと、暗黙的にArrayListがNewされてSeasarがDIします。
	//                   Rの美容チームだと付ける文化はないみたいですが、無駄なインスタンス生成がされなくなるので、覚えておいてください。
	//                   Rでは、文化にしたがって付けないままの方が良いでしょうけどね。
	// @Binding(bindingType=BindingType.NONE)
	public List<MemberTestDto> memberList;

	@Execute(validator = false)
	public String index() {
		//TODO Takeshi Kato: おそらくこのクラス自体が、不要なコードだと思いますので、削除できるタイミングが来たら、削除してください。
		//                   また、空のパッケージも残さない方が良いですので、exampleパッケージなどは削除するようにしてください。

		//TODO ログイン前はログイン画面、ログイン認証済みであればホーム画面。
		memberList = jdbcManager.selectBySqlFile(MemberTestDto.class, "front/sql/selectTest.sql").getResultList();
		indexForm.test = "zikkennzikken";
		//		return "index.jsp";
		return "/login/login";
	}
}
