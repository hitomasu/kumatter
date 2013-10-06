package org.leihauoli.kumatter.action.home;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.form.home.HomeForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
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

	// 各種情報保持用のDTO
	@Resource
	public ContextDto contextDispDto;

	// 関係性関連のサービス
	@Resource
	protected MemberRelationsService memberRelationsService;

	/**
	 * 初期表示
	 * @return　ホーム画面
	 */
	@Execute(validator = false)
	public String index() {

		//フォローされているメンバーを取得
		contextDispDto.followerMemberList = memberRelationsService.getFollowerMemberList(loginDto.memberId);

		//フォローしているメンバーを取得
		contextDispDto.followMemberList = memberRelationsService.getFollowMemberList(loginDto.memberId);

		//表示するタイムラインを取得
		final List<Long> followMemberIdList = new ArrayList<Long>();
		followMemberIdList.add(loginDto.memberId); //自分のIDをListに追加
		for (final MemberRelationsResultDto followMemberList : contextDispDto.followMemberList) {
			//フォローしているメンバーのIDをListにセット
			followMemberIdList.add(followMemberList.memberId);
		}
		contextDispDto.timeLine = memberRelationsService.getTweetHistory(followMemberIdList);

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
