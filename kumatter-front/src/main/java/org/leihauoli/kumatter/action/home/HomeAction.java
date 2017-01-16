package org.leihauoli.kumatter.action.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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
import org.seasar.struts.util.MessageResourcesUtil;

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

	//TODO Takeshi Kato: 下記のコメントは意味があまりないですね。もうちょっと具体性を持たせた方が良いです。
	// 各種情報保持用のDTO
	@Resource
	public ContextDto contextDto;

	// 関係性関連のサービス
	@Resource
	protected MemberRelationsService memberRelationsService;

	// ツイートテーブル関連のサービス
	@Resource
	protected TweetService tweetService;

	// 全角スペースを定数として宣言
	final private String ZENKAKU_SPACE = "　";

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
			//TODO Takeshi Kato: ツイートされた時間を、共通カラムである作成日時カラムの時間で表示していますが、これはBadです。
			//                   ツイートされた時間という業務的な情報と、カラムが作成された時間というDB管理レベルの情報は、一緒に扱うべきではありません。
			//                   例えば、くまさんアプリがリニューアルして、DB構造もリファクタして、バッチでツイートテーブルの内容を別テーブルに移行したい。
			//                   などのケースが発生した場合、作成日時カラムの時間は、ツイートされた時間とはずれて来てしまうかもしれません。
			//                   基本的に、画面に表示する日時情報などは業務上の情報ですので、共通カラムとは別に、例えば「ツイート日時」のような感じで、
			//                   別カラムとして情報を持つのが一般的＆Betterです。
		}

		return showHome();
	}

	/**
	 * ツイートする
	 * @return
	 */
	@Execute(validate = "check", validator = true, input = "index")
	public String doTweet() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// ツイート内容をツイートテーブルに登録
		tweetService.insertTweet(loginDto.memberId, homeForm.tweet);

		//		return index();
		return "index?redirect=true";
		//		return "http://www.yahoo.co.jp?redirect=true";
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

		//TODO Takeshi Kato: ツイートの検索でトークンチェックをする必要はないように思えます。
		//                   トークンチェックは、ダブルサブミットなどによって、データが2重登録される事などを防ぐのが主な目的です。
		//                   検索はたとえダブルサブミットされたとしても問題無いように思えますが、何か意図はありますか？

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

		//TODO Takeshi Kato: 会員検索でトークンチェックをする必要はないように思えます。
		//                   トークンチェックは、ダブルサブミットなどによって、データが2重登録される事などを防ぐのが主な目的です。
		//                   検索はたとえダブルサブミットされたとしても問題無いように思えますが、何か意図はありますか？

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

		// tweet入力エリアをクリア
		homeForm.tweet = null;

		// トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "home.jsp";
	}

	/**
	 * 文字列が空白のみの場合はエラー
	 * @return error
	 */
	public ActionMessages check() {
		final ActionMessages errors = new ActionMessages();

		String strTweet = homeForm.tweet;
		strTweet = strTweet.trim();
		strTweet = strTweet.replaceAll(ZENKAKU_SPACE, "");
		if (strTweet.isEmpty()) {
			errors.add(MessageResourcesUtil.getMessage("labels.tweet"), new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.tweet")));
		}

		return errors;
	}
}
