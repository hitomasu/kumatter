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
		Long memberId = memberService.getMemberIdNickName(loginForm.id);
		if (memberId == null) {
			// メールアドレスからメンバーIDを取得
			memberId = memberService.getMemberIdMailAddress(loginForm.id);
		}

		// IDが存在しない場合はエラー
		if (memberId == null) {
			throw new ActionMessagesException("errors.id", true);
		}

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