package org.leihauoli.kumatter.action.login;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberResultDto;
import org.leihauoli.kumatter.form.login.LoginForm;
import org.leihauoli.kumatter.service.MemberService;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

/**
 * ログイン画面のアクションクラス
 * @author hitoshi_masuzawa
 */
public class LoginAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public LoginForm loginForm;

	// ログイン認証用のDTO
	@Resource
	public LoginDto loginDto;

	// ログインサービス
	@Resource
	protected MemberService memberService;

	/**
	 * 初期表示
	 * @return　ログイン画面
	 */
	@Execute(validator = false)
	public String index() {
		return showLogin();
	}

	/**
	 * ログイン処理
	 * @return 遷移先
	 */
	@Execute(validator = true, input = "errorBackIndex")
	public String login() {

		//ニックネームからメンバーIDを取得
		//TODO Takeshi Kato: 指定したニックネーム、またはメールアドレスの会員がいるかどうかを判定したいという業務ロジックだと思いますが、
		//                   MemberServiceクラスに業務ロジックとして移動した方が良いと思います。
		//                   画面に特化したロジックはActionに、業務に特化したロジックはServiceクラスに記載する方が良いです。
		//                   そうすると、画面に依存しない業務ロジックが出来上がるため、業務ロジックが画面の変更や構成などに依存しなくなります。
		Long memberId = memberService.getMemberIdNickName(loginForm.id);
		if (memberId == null) {
			// メールアドレスからメンバーIDを取得
			memberId = memberService.getMemberIdMailAddress(loginForm.id);
		}

		// IDが存在しない場合はエラー
		if (memberId == null) {
			throw new ActionMessagesException("errors.id", true);
		}

		//TODO Takeshi Kato: 下記のコメント文は、コメントの書き方の悪い例といっても良いです。
		//                   コメントには、処理の内容を単純に記載するよりも、処理の意図や概要を記載するべきです。
		//                   下記のコメントに記載されている内容は、コードを追えばわかりますし、日本語としても読みづらいです。
		//                   「入力されたパスワードが、会員のログインパスワードと一致しなければログインエラーとする」などのように、
		//                   もっと意図や概要がわかるようなコメントを心がけた方が良いです。
		//                   また、意図をコードから読み取る事は大変な事＆汚いor複雑なコードだと意図を読み取るのも不可能だったりしますので、
		//                   意図をコメントに書くのは非常に有意義な事です。

		// メンバーパスワードテーブルからパスワードと入力されたパスワードが違えばエラー
		if (!loginForm.password.equals(memberService.getPassWord(memberId))) {
			throw new ActionMessagesException("errors.password", true);
		}

		// セッションにログイン情報を保存
		final MemberResultDto memberDto = memberService.getMember(memberId);
		Beans.copy(memberDto, loginDto).execute();

		return showHome();
	}

	/**
	 * 入力エラー時の変移先
	 * @return ログイン画面
	 */
	@Execute(validator = false)
	public String errorBackIndex() {
		return showLogin();
	}

	// ログイン画面へ遷移
	private String showLogin() {
		return "login.jsp";
	}

	// HOME画面へ遷移
	private String showHome() {
		return "/home/home?redirect=true";
	}

}
