package org.leihauoli.kumatter.action.relations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.form.relations.FollowForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

/**
 * フォロー画面のアクションクラス<br>
 * （フォローしているメンバー）
 * @author hitoshi_masuzawa
 */
@Authentication
public class FollowAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public FollowForm followForm;

	// HTTPリクエスト
	@Resource
	protected HttpServletRequest request;

	// ログイン認証用のDTO
	@Resource
	public LoginDto loginDto;

	// 各種情報保持用のDTO
	@Resource
	public ContextDto contextDto;

	// 関係性関連のサービス
	@Resource
	protected MemberRelationsService memberRelationsService;

	// ツイート関連のサービス
	@Resource
	protected TweetService tweetService;

	/**
	 * 初期表示
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = false)
	public String index() {

		//フォローされているメンバーを取得
		contextDto.followerMemberList = memberRelationsService.getFollowerMemberList(loginDto.memberId);
		//フォローされている件数を取得
		contextDto.followerMemberCount = memberRelationsService.getFollowerMemberCount(loginDto.memberId);

		//フォローしているメンバーを取得
		contextDto.followMemberList = memberRelationsService.getFollowMemberList(loginDto.memberId);
		//フォローしている件数を取得
		contextDto.followMemberCount = memberRelationsService.getFollowMemberCount(loginDto.memberId);

		//タイムライン関連を取得
		final List<Long> memberIdList = new ArrayList<Long>();
		memberIdList.add(loginDto.memberId); //自分のIDをListに追加
		//自分のツイート件数を取得
		contextDto.tweetCount = tweetService.getTweetHistoryCount(memberIdList);

		// フォローメンバーの一方通行フラグをセット
		for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
			// 一方通行フラグはデフォルトでtrue
			followMember.oneWayFlg = true;
			for (final MemberRelationsResultDto followerMember : contextDto.followerMemberList) {
				if (followMember.memberId == followerMember.memberId) {
					// 相互にフォローしあっている場合は一方通行フラグはfalse
					followMember.oneWayFlg = false;
					break;
				}
			}
		}
		return showFollow();
	}

	/**
	 * フォローする
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doFollow() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォローするメンバーをメンバー関係性テーブルに登録
		memberRelationsService.insertMemberRelations(followForm.memberId, loginDto.memberId);
		return "/relations/follow?redirect=true";
	}

	/**
	 * フォロー解除
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doDeleteRelations() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォロー解除
		memberRelationsService.deleateRelations(followForm.relationsId);

		return "/relations/follow?redirect=true";
	}

	/**
	 * ツイート検索
	 * @return　検索結果表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doTweetSearch() {

		return "/search/searchTweet/";
	}

	/**
	 * メンバー検索
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doMemberSearch() {

		return "/search/searchMember/";
	}

	/**
	 * ログアウト
	 * @return　ログイン画面
	 */
	@Execute(validator = false)
	@RemoveSession(name = "loginDto")
	public String logout() {
		return "/login/login?redirect=true";
	}

	/**
	 * フォローしているメンバー表示
	 * @return　フォロワー表示画面
	 */
	private String showFollow() {

		//検索クエリをクリア
		followForm.query = null;

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "follow.jsp";
	}

}
