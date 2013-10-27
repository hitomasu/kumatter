package org.leihauoli.kumatter.action.search;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.leihauoli.kumatter.form.search.SearchTweetForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.MemberService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

/**
 * ツイート検索機能のアクションクラス<br>
 * @author hitoshi_masuzawa
 */
@Authentication
public class SearchTweetAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public SearchTweetForm searchTweetForm;

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

	// メンバーテーブル関連のサービス
	@Resource
	protected MemberService memberService;

	// ツイートテーブル関連のサービス
	@Resource
	protected TweetService tweetService;

	/** 検索結果ツイートリスト */
	public List<TweetHistoryResultDto> searchTweetList;

	/**
	 * 初期表示
	 * @return　メンバー検索結果画面
	 */
	@Execute(validator = true, input = "/relations/follow")
	public String index() {

		searchTweet();

		//		return "/search/showSearchTweet/?redirect=true";
		return "/search/searchTweet/showSearchTweet/";
	}

	/**
	 * ツイート検索
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "showSearchTweet")
	public String doTweetSearch() {

		// ツイート検索
		searchTweet();

		return "/search/searchTweet/showSearchTweet/";
	}

	/**
	 * メンバー検索
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "showSearchTweet")
	public String doMemberSearch() {

		return "/search/searchMember/doMemberSearch/";
	}

	/**
	 * フォローする
	 * @return　画面
	 */
	@Execute(validator = true, input = "index")
	public String doFollow() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォローするメンバーをメンバー関係性テーブルに登録
		memberRelationsService.insertMemberRelations(searchTweetForm.memberId, loginDto.memberId);

		//検索結果を再現する為に検索クリエをセット
		searchTweetForm.query = searchTweetForm.hiddenQuery;

		return "/search/searchTweet/doTweetSearch/";
	}

	/**
	 * フォロー解除
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "showSearchTweet")
	public String doDeleteRelations() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォロー解除
		memberRelationsService.deleateRelations(searchTweetForm.relationsId);

		// 検索結果を再現する為に検索クエリをセット
		searchTweetForm.query = searchTweetForm.hiddenQuery;

		return "/search/searchTweet/doTweetSearch/";
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
	 * ツイート検索結果表示
	 * @return　メンバー検索結果表示画面
	 */
	@Execute(validator = false)
	public String showSearchTweet() {

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		//隠し検索クエリをフォームにセット
		searchTweetForm.hiddenQuery = searchTweetForm.query;

		return show();
	}

	public String show() {
		return "searchTweet.jsp";
	}

	/**
	 * ツイート検索の下請けメソッド
	 */
	private void searchTweet() {
		//フォローされているメンバーを取得
		contextDto.followerMemberList = memberRelationsService.getFollowerMemberList(loginDto.memberId);
		//フォローされている件数を取得
		contextDto.followerMemberCount = memberRelationsService.getFollowerMemberCount(loginDto.memberId);

		//フォローしているメンバーを取得
		contextDto.followMemberList = memberRelationsService.getFollowMemberList(loginDto.memberId);
		//フォローしている件数を取得
		contextDto.followMemberCount = memberRelationsService.getFollowMemberCount(loginDto.memberId);

		//検索クエリからツイートを検索
		searchTweetList = tweetService.getSearchTweetList(searchTweetForm.query);

		//ツイート日時をフォーマット
		for (final TweetHistoryResultDto tweet : searchTweetList) {
			// 指定したフォーマットで日付が返される
			tweet.strRegisterTime = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(tweet.registerTime);
		}

		// 検索結果ツイートにフォローフラグをセット
		for (final TweetHistoryResultDto searchTweet : searchTweetList) {
			// フォローフラグはデフォルトでfalse
			searchTweet.followFlg = false;
			for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
				if (searchTweet.memberId == followMember.memberId) {
					// 既にフォローしている場合はフォロー済みフラグがtrue
					searchTweet.followFlg = true;
					// 関係性IDをセット
					searchTweet.relationsId = followMember.relationsId;
					break;
				}
			}
		}
	}

}
