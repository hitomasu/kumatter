package org.leihauoli.kumatter.action.login;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.form.login.LoginForm;
import org.leihauoli.kumatter.service.LoginService;
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
	protected LoginService loginService;

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
	@Execute(validator = true, input = "login.jsp")
	public String login() {

		final String id = loginForm.id;
		final String pass = loginForm.pass;

		//ニックネームからメンバーIDを取得
		Integer memberId = loginService.getMemberIdNickName(id);
		if (memberId == null) {
			// メールアドレスからメンバーIDを取得
			memberId = loginService.getMemberIdMailAddress(id);
		}

		// IDが存在しない場合はエラー
		if (memberId == null) {
			throw new ActionMessagesException("errors.id", true);
		}

		// メンバーパスワードテーブルからパスワードを取得
		final String resultPass = loginService.getPassWord(memberId);

		// パスワードが間違えていた場合はエラー
		if (!pass.equals(resultPass)) {
			throw new ActionMessagesException("errors.pass", true);
		}
		// ログイン認証処理
		loginDto.memberId = memberId;

		return showHome();
	}

	// ログイン画面へ遷移
	private String showLogin() {
		return "login.jsp";
	}

	// HOME画面へ遷移
	private String showHome() {
		return "/home/home";
	}

}
