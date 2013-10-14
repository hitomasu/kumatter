package org.leihauoli.kumatter.action.relations;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.form.relations.FollowerForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * フォロワー画面のアクションクラス<br>
 * （フォローされているメンバー）
 * @author hitoshi_masuzawa
 */
@Authentication
public class FollowerAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public FollowerForm followerForm;

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

	// ツイートテーブル関連のサービス
	@Resource
	protected TweetService tweetService;

	/**
	 * 初期表示
	 * @return　ホーム画面
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

		// フォロワーメンバーの一方通行フラグをセット
		for (final MemberRelationsResultDto followerMember : contextDto.followerMemberList) {
			for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
				// 一方通行フラグはデフォルトでtrue
				followerMember.oneWayFlg = true;
				if (followerMember.memberId == followMember.memberId) {
					// 相互にフォローしあっている場合は一方通行フラグはfalse
					followerMember.oneWayFlg = false;
					break;
				}
			}
		}

		return showHome();
	}

	/**
	 * ツイートする
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String doTweet() {
		//
		//		//トークンチェック
		//		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
		//			throw new ActionMessagesException("errors.invalid", "Token");
		//		}
		//
		//		// ツイート内容をツイートテーブルに登録
		//		tweetService.insertTweet(loginDto.memberId, homeForm.tweet);
		//
		return "index?redirect=true";
	}

	/**
	 * ツイート削除
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String doTweetDelete() {
		//
		//		//トークンチェック
		//		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
		//			throw new ActionMessagesException("errors.invalid", "Token");
		//		}
		//
		//		// ツイートを論理削除
		//		tweetService.deleteTweet(homeForm.tweetHistoryId, homeForm.versionNo);
		//
		return "index?redirect=true";
	}

	/**
	 * ログアウト
	 * @return
	 */
	@Execute(validator = false)
	@RemoveSession(name = "loginDto")
	public String logout() {
		return "/login/login?redirect=true";
	}

	// HOME画面へ遷移
	private String showHome() {

		//検索クエリをクリア
		followerForm.query = null;

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "follower.jsp";
	}

}
