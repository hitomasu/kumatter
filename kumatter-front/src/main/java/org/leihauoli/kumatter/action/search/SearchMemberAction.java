package org.leihauoli.kumatter.action.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.form.search.SearchMemberForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.MemberService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

/**
 * メンバー検索機能のアクションクラス<br>
 * @author hitoshi_masuzawa
 */
@Authentication
public class SearchMemberAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public SearchMemberForm searchMemberForm;

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

	// ツイート関連のサービス
	@Resource
	protected TweetService tweetService;

	// 検索結果メンバーリスト
	public List<MemberRelationsResultDto> searchMemberList;

	/**
	 * 初期表示
	 * @return　メンバー検索結果画面
	 */
	@Execute(validator = true, input = "showSearchMember")
	public String index() {

		searchMember();

		//		return "/search/showSearchMember/?redirect=true";
		return "/search/searchMember/showSearchMember/";
	}

	/**
	 * メンバー検索
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "showSearchMember")
	public String doMemberSearch() {

		//メンバー検索
		searchMember();

		return "/search/searchMember/showSearchMember/";
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
		memberRelationsService.insertMemberRelations(searchMemberForm.memberId, loginDto.memberId);

		//検索結果を再現する為に検索クエリをセット
		searchMemberForm.query = searchMemberForm.hiddenQuery;

		return "/search/searchMember/doMemberSearch/";
	}

	/**
	 * フォロー解除
	 * @return　フォロワー表示画面
	 */
	@Execute(validator = true, input = "showSearchMember")
	public String doDeleteRelations() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォロー解除
		memberRelationsService.deleateRelations(searchMemberForm.relationsId);

		// 検索結果を再現する為に検索クエリをセット
		searchMemberForm.query = searchMemberForm.hiddenQuery;

		return "/search/searchMember/doMemberSearch/";
	}

	/**
	 * ツイート検索
	 * @return　検索結果表示画面
	 */
	@Execute(validator = true, input = "index")
	public String doTweetSearch() {

		return "/search/searchTweet/doTweetSearch";
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
	 * メンバー検索結果表示
	 * @return　メンバー検索結果表示画面
	 */
	@Execute(validator = false)
	public String showSearchMember() {

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		//隠し検索クエリをフォームにセット
		searchMemberForm.hiddenQuery = searchMemberForm.query;

		return show();
	}

	public String show() {
		return "searchMember.jsp";
	}

	/**
	 * メンバー検索の下請けメソッド
	 */
	private void searchMember() {
		//フォローされているメンバーを取得
		contextDto.followerMemberList = memberRelationsService.getFollowerMemberList(loginDto.memberId);
		//フォローされている件数を取得
		contextDto.followerMemberCount = memberRelationsService.getFollowerMemberCount(loginDto.memberId);

		//フォローしているメンバーを取得
		contextDto.followMemberList = memberRelationsService.getFollowMemberList(loginDto.memberId);
		//フォローしている件数を取得
		contextDto.followMemberCount = memberRelationsService.getFollowMemberCount(loginDto.memberId);

		//自分のツイート件数を取得
		final List<Long> memberIdList = new ArrayList<Long>();
		//自分のIDをListに追加
		memberIdList.add(loginDto.memberId);
		contextDto.tweetCount = tweetService.getTweetHistoryCount(memberIdList);

		//検索クエリからメンバーを検索
		searchMemberList = memberService.getSearchMemberList(loginDto.memberId, searchMemberForm.query);

		// 検索結果メンバーに一方通行フラグをセット
		for (final MemberRelationsResultDto searchMember : searchMemberList) {
			// フォロー済みフラグはデフォルトでfalse
			searchMember.followFlg = false;
			for (final MemberRelationsResultDto followMember : contextDto.followMemberList) {
				if (searchMember.memberId == followMember.memberId) {
					// 既にフォローしている場合はフォロー済みフラグがtrue
					searchMember.followFlg = true;
					break;
				}
			}
		}
	}

}
