package org.leihauoli.kumatter.action.home;

import javax.annotation.Resource;

import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.form.home.HomeForm;
import org.leihauoli.kumatter.service.MemberService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * ホーム画面のアクションクラス
 * @author hitoshi_masuzawa
 */
@Authentication
public class HomeAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public HomeForm homeForm;

	// ログイン認証用のDTO
	@Resource
	public LoginDto loginDto;

	// ログインサービス
	@Resource
	protected MemberService memberService;

	/**
	 * 初期表示
	 * @return　ホーム画面
	 */
	@Execute(validator = false)
	public String index() {
		return showHome();
	}

	@Execute(validator = false)
	@RemoveSession(name = "loginDto")
	public String logout() {
		return "/login/login?redirect=true";
	}

	// HOME画面へ遷移
	private String showHome() {
		return "home.jsp";
	}

}
