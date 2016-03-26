package org.leihauoli.kumatter.action.registration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.dto.RegistrationDto;
import org.leihauoli.kumatter.dto.param.InsertMemberParamDto;
import org.leihauoli.kumatter.dto.param.InsertMemberPasswordParamDto;
import org.leihauoli.kumatter.form.registration.RegistrationForm;
import org.leihauoli.kumatter.service.MemberService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 会員登録画面のアクションクラス
 * @author hitoshi_masuzawa
 */
public class RegistrationAction {

	//TODO Takeshi Kato: 全体的にですが、フィールド変数のJavaDocの書き方が間違っています。以下のようなコメント文で記述するのが正しいです。
	/** アクションフォーム */

	// アクションフォーム
	@Resource
	@ActionForm
	public RegistrationForm registrationForm;

	// HTTPリクエスト
	@Resource
	protected HttpServletRequest request;

	// ログインサービス
	@Resource
	protected MemberService memberService;

	// 会員登録用DTO
	@Resource
	public RegistrationDto registrationDto;

	/**
	 * 初期表示
	 * @return　入力画面
	 */
	@Execute(validator = false)
	public String index() {

		return showRegistration();
	}

	/**
	 * 確認処理
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "checkValidate", input = "errorBackIndex")
	public String confirm() {

		// フォームの内容をセッションに保存
		setDto();
		//TODO Takeshi Kato: コメントに書いているロジックの内容を、もっとメソッド名に反映してあげましょう。メソッド名が雑過ぎます。
		//                      setXXXというメソッド名の場合、単純なセッターメソッドを連想させてしまいます。copyやprepareなど、他の言葉を使った方が望ましいです。
		//                      他人がコードを見た時に、メソッド名から処理内容が推測出来るようになっているのが望ましいですが、
		//                      setXXX, getXXXは、フィールド変数など単純な設定、取得をしているように連想されます。
		//                      Rのプロジェクトでは、むやみにset,getが乱用されていますが、真似しないでください。
		//                      DBから値を取得する時なども、getXXXではなく、selectXXXとした方が、DBを触っている感が名前から分かりますし、
		//                      取得したDBの値から、いろいろと加工したオブジェクトを取得するときなどは、prepareとした方が色々と準備している感が表現できます。

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return showConfirm();
	}

	/**
	 * 完了処理
	 * @return 完了画面
	 */
	@Execute(validator = false, input = "errorBackIndex")
	@RemoveSession(name = "registrationDto")
	public String doComplete() {
		//TODO Takeshi Kato: うろ覚えなのですが、@RemoveSessionをここで付与して、トークンチェックでエラーとなった場合って、
		//                   セッションDTOは破棄されませんでしたっけ？
		//                   もしかして、completeメソッドの方に、@RemoveSessionを付与した方が良いのでは？と思いました。

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// メンバーテーブルに会員情報をインサート
		final InsertMemberParamDto param = Beans.createAndCopy(InsertMemberParamDto.class, registrationDto).execute();
		final int insertMemberCount = memberService.insertMember(param);

		//TODO Takeshi Kato: 全体的にですが、共通カラムの作成日時や更新日時の値を、SQL上でMySQLのNOW関数を使って設定していますが、このやり方はBadです。
		//                    Now関数はPCのシステム時刻から現在時刻を取得します。
		//                    もし、テストで運用日付を固定にしてテストしたかったり、運用日付を来年にしてテストしたい。という事になった場合、
		//                    PCのシステム時刻をいじらなければならなくなります。複数台のサーバーで連携して動かしている場合には、さらに自体は深刻になります。
		//                    Webアプリに限らずですが、プログラムがシステム時刻に依存しないような設計は、必ず必要です。
		//                    Javaの new Date()や、Calendar.getInstance().getTime(), System.currentTimeMillis()なども、
		//                    システム時刻に依存してしまうので、基本的には使わない方針にするべきです。
		//
		//                    解決策としては、R2DateのようなHelperクラスを作るか、DateUtilのようなUtilクラスを作成して、
		//                    そのクラスから現在時刻を取得するようにする事です。
		//
		//                    例えば、DateUtilというクラスを作成して、以下のようなメソッドを作っておきます。
		//							public static Date getCurrentDate(){
		//								return new Date();
		//							}
		//
		//                    現在日時を取得したい場合には、全て上記のメソッドを呼び出して取得するようにして、
		//                    SQLもNOW関数を利用せずに、上記で取得した日時をDTOで設定するようにしてあげます。
		//
		//                    そのように設計しておくと、テストなどで運用日付を変更したい場合には、上記のメソッドを1行書き換えるだけで済みます。
		//                    diconファイルなどをうまく使って、返す日付をdiconファイルなどの設定ファイルに記載するようにする事も出来るでしょう。
		//
		//                    かなり重要な事なので、直接システム時刻を使うのは原則禁止されている事だというのを、覚えておいてください。
		//                    Rのプロジェクトで、SQL上でSYSDATEを使っているのを散見しますが、あれも本当は良くありません。

		if (insertMemberCount != 1) {
			//TODO 正常にインサートされなかった場合のエラー処理
		}

		// ニックネームからメンバーIDを取得
		final Long memberId = memberService.getMemberIdByNickName(registrationDto.nickName);
		if (memberId == null) {
			//TODO ニックネームからメンバーIDが取得できなかった場合のエラー処理
		}

		// パスワードテーブルに入力されたパスワードをインサート
		final InsertMemberPasswordParamDto passParam = new InsertMemberPasswordParamDto();
		passParam.memberId = memberId;
		passParam.password = registrationDto.password;
		final int insertPasswordCount = memberService.insertMemberPassword(passParam);
		if (insertPasswordCount != 0) {
			//TODO 正常にインサートされなかった場合のエラー処理
		}

		return "complete?redirect=true";
	}

	/**
	 * 完了画面へ遷移
	 * @return 完了画面
	 */
	@Execute(validator = false)
	public String complete() {
		return showComplete();
	}

	/**
	 * 戻る押下
	 * @return 入力画面
	 */
	@Execute(validator = false)
	public String goBack() {
		// フォームにセット
		setForm();
		//TODO Takeshi Kato: これもsetDto同様に名前が雑です。またコメント文も何を言いたいのかが分かりません。
		//                   個人プロジェクトであっても、コメントを書く場合には、他人に読まれることを想定して書いた方が良い練習になります。

		return showRegistration();
	}

	/**
	 * 入力エラー時の変移先
	 * @return 登録画面
	 */
	@Execute(validator = false)
	public String errorBackIndex() {
		return showRegistration();
	}

	/**
	 * フォームの内容をDTOにセット
	 */
	private void setDto() {
		registrationDto.firstName = registrationForm.firstName;
		registrationDto.lastName = registrationForm.lastName;
		registrationDto.mailAddress = registrationForm.mailAddress;
		registrationDto.nickName = registrationForm.nickName;
		registrationDto.password = registrationForm.password1;
	}

	/**
	 * DTOの内容をフォームにセット
	 */
	private void setForm() {
		registrationForm.firstName = registrationDto.firstName;
		registrationForm.lastName = registrationDto.lastName;
		registrationForm.mailAddress = registrationDto.mailAddress;
		registrationForm.nickName = registrationDto.nickName;
		registrationForm.password1 = registrationDto.password;
		registrationForm.password2 = registrationDto.password;
	}

	// 入力画面へ遷移
	private String showRegistration() {
		return "registration.jsp";
	}

	// 確認画面へ遷移
	private String showConfirm() {
		return "confirm.jsp";
	}

	// 完了画面へ遷移
	private String showComplete() {
		return "complete.jsp";
	}

	// HOME画面へ遷移
	private String showHome() {
		//TODO Takeshi Kato: このメソッドは利用されていないですね。不要なら削除しましょう。Eclipseでも警告が出ているはずです。
		return "/home/home?redirect=true";
	}

	/**
	 * ニックネームとメールアドレスの存在チェック
	 * パスワード1とパスワード2が同じ値かチェック
	 * @return エラー内容
	 */
	public ActionMessages checkValidate() {
		final ActionMessages errors = new ActionMessages();

		// ニックネームが既に存在しているかをチェック
		if (memberService.isExistMemberSpecifiedNickName(registrationForm.nickName)) {
			errors.add(MessageResourcesUtil.getMessage("labels.nickName"), new ActionMessage("errors.nickName"));
		}
		// メールアドレスが既に存在しているかをチェック
		if (memberService.isExistMemberSpecifiedmailAddress(registrationForm.mailAddress)) {
			errors.add(MessageResourcesUtil.getMessage("labels.mailAddress"), new ActionMessage("errors.mailAddress"));
		}
		//　パスワード1とパスワード2に同じ値が入力されているかをチェック
		if (!registrationForm.password1.equals(registrationForm.password2)) {
			errors.add(MessageResourcesUtil.getMessage("labels.password"), new ActionMessage("errors.password2"));
		}
		return errors;
		//TODO Hitoshi Masuzawa: サービスクラスに判定するメソッドを作成しました！
		//TODO 修正済　Takeshi Kato: MemberServiceが単純なDAOとして使っているからなのかもしれませんが、
		//                   やりたい事が「指定したニックネームのメンバーが存在しているかどうかを判定すること」なのであれば、
		//                   memberService.isExistMemberSpecifiedNickName(registrationForm.nickName)などのような感じで、
		//                   存在していればTrue, 存在していなければFalseを返すようなサービスクラス・メソッドを用意した方がわかりやすいと思います。
		//
		//                   また、変数名がnickNameになっていますが、実際には会員IDが入っていますので、変数名が嘘を付いてしまっています。
		//                   変数名とコメントに嘘が混じっているのは、どんな場合であれ悪なので気をつけてください。
		//                   下記のmailAddressも同様です。
		//
		//                   また、ニックネームやメールアドレスを一意にしたいのであれば、DB上で論理的に一意にした方が安全です。
		//                   プログラムがミスっていたとしても、DB側で整合性を保てるメリットは大きいです。
		//                   DB側でユニーク制約をかけていない理由があるのだとしたら、データ構造レベルで整合性を保てるメリットと、
		//                   上記理由がもたらすメリットとを、十分考えて天秤にかけてください。
		//                   プログラムバグなどで整合性が保たれないデータが出てくるようになった場合、それを復旧するコストは膨大になりますので。
		//                   特にプロジェクトで、DB設計担当になった場合には、なるべくデータ構造レベルで安全性を確保するように設計しておかないと、
		//                   メンバーのスキルや理解度によって、事故が発生する可能性も高くなりますので、ご注意を。

	}

	/**
	 * トークンチェック(executeアノテーションのvalidateに加える場合）
	 *
	 */
	private ActionMessages tokenCheck() {
		//TODO Takeshi Kato: このメソッドは利用されていないですね。不要なら削除しましょう。Eclipseでも警告が出ているはずです。

		final ActionMessages errors = new ActionMessages();
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid", "Token"));
		}
		return errors;
	}
}
