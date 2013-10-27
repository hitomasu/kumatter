package org.leihauoli.kumatter.action.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.leihauoli.kumatter.form.home.HomeForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

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

		//タイムライン関連を取得
		final List<Long> memberIdList = new ArrayList<Long>();
		memberIdList.add(loginDto.memberId); //自分のIDをListに追加
		//自分のツイート件数を取得
		contextDto.tweetCount = tweetService.getTweetHistoryCount(memberIdList);
		for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
			//フォローしているメンバーのIDをListにセット
			memberIdList.add(followMember.memberId);
		}
		//表示するタイムラインを取得
		contextDto.timeLine = tweetService.getTweetHistory(memberIdList);
		//ツイート日時をフォーマット
		for (final TweetHistoryResultDto tweet : contextDto.timeLine) {
			// 指定したフォーマットで日付が返される
			tweet.strRegisterTime = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(tweet.registerTime);
		}

		return showHome();
	}

	/**
	 * ツイートする
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String doTweet() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// ツイート内容をツイートテーブルに登録
		tweetService.insertTweet(loginDto.memberId, homeForm.tweet);

		return "index?redirect=true";
	}

	/**
	 * ツイート削除
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String doTweetDelete() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// ツイートを論理削除
		tweetService.deleteTweet(homeForm.tweetHistoryId, homeForm.versionNo);

		return "index?redirect=true";
	}

	/**
	 * ツイート検索
	 * @return　検索結果表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doTweetSearch() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		return "/search/searchTweet/";
	}

	/**
	 * メンバー検索
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doMemberSearch() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		return "/search/searchMember/";
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

		//tweet入力エリアをクリア
		homeForm.tweet = null;

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "home.jsp";
	}

}
