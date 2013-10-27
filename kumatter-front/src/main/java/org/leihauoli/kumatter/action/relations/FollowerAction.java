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
import org.leihauoli.kumatter.form.relations.FollowerForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

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

		// フォロワーメンバーの一方通行フラグをセット
		for (final MemberRelationsResultDto followerMember : contextDto.followerMemberList) {
			// 一方通行フラグはデフォルトでtrue
			followerMember.oneWayFlg = true;
			for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
				if (followerMember.memberId == followMember.memberId) {
					// 相互にフォローしあっている場合は一方通行フラグはfalse
					followerMember.oneWayFlg = false;
					// 逆方向からの関係性IDをセット
					followerMember.reverseRelationsId = followMember.relationsId;
					break;
				}
			}
		}
		return showFollower();
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
		memberRelationsService.insertMemberRelations(followerForm.memberId, loginDto.memberId);
		return "/relations/follower?redirect=true";
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
		memberRelationsService.deleateRelations(followerForm.relationsId);

		return "/relations/follower?redirect=true";
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
	 * フォロワー表示画面
	 * @return　フォロワー表示画面
	 */
	private String showFollower() {

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "follower.jsp";
	}

}
