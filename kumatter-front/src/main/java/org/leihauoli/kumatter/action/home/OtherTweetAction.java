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
import org.leihauoli.kumatter.dto.result.MemberResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.leihauoli.kumatter.form.home.OtherTweetForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.MemberService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

/**
 * 他人のツイート画面のアクションクラス
 * @author hitoshi_masuzawa
 */
@Authentication
public class OtherTweetAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public OtherTweetForm otherTweetForm;

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

	// メンバーテーブル関連のサービス
	@Resource
	protected MemberService memberService;

	//　メンバー情報
	public MemberResultDto memberDto;

	/**
	 * 初期表示
	 * @return　ツイート画面
	 */
	@Execute(validator = false)
	public String index() {

		// メンバー情報をセット
		memberDto = memberService.getMember(otherTweetForm.memberId);

		//フォローされているメンバーを取得
		contextDto.followerMemberList = memberRelationsService.getFollowerMemberList(otherTweetForm.memberId);
		//フォローされている件数を取得
		contextDto.followerMemberCount = memberRelationsService.getFollowerMemberCount(otherTweetForm.memberId);

		//フォローしているメンバーを取得
		contextDto.followMemberList = memberRelationsService.getFollowMemberList(otherTweetForm.memberId);
		//フォローしている件数を取得
		contextDto.followMemberCount = memberRelationsService.getFollowMemberCount(otherTweetForm.memberId);

		//タイムライン関連を取得
		final List<Long> memberIdList = new ArrayList<Long>();
		memberIdList.add(otherTweetForm.memberId); //自分のIDをListに追加
		//ツイート件数を取得
		contextDto.tweetCount = tweetService.getTweetHistoryCount(memberIdList);
		//ツイート内容を取得
		contextDto.timeLine = tweetService.getTweetHistory(memberIdList);
		//ツイート日時をフォーマット
		for (final TweetHistoryResultDto tweet : contextDto.timeLine) {
			// 指定したフォーマットで日付が返される
			tweet.strRegisterTime = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒").format(tweet.registerTime);
		}

		// 検索結果メンバーに一方通行フラグをセット
		for (final MemberRelationsResultDto followerMember : contextDto.followerMemberList) {
			// フォロー済みフラグはデフォルトでfalse
			memberDto.followFlg = false;
			if (followerMember.memberId.equals(loginDto.memberId)) {
				// 既にフォローしている場合はフォロー済みフラグがtrue
				memberDto.followFlg = true;
				// 関係性IDをセット
				memberDto.relationsId = followerMember.relationsId;
				break;
			}
		}

		return showTweet();
	}

	/**
	 * 前画面へ戻る
	 * @return　前画面
	 */
	@Execute(validator = true, input = "/home/home/")
	public String goBack() {

		return otherTweetForm.backPath;
	}

	/**
	 * フォローする
	 * @return　画面
	 */
	@Execute(validator = true, input = "/home/home/")
	public String doFollow() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォローするメンバーをメンバー関係性テーブルに登録
		memberRelationsService.insertMemberRelations(otherTweetForm.memberId, loginDto.memberId);

		//検索結果を再現する為に検索クエリをセット
		otherTweetForm.query = otherTweetForm.query;
		//検索結果を再現する為に画面遷移をセット
		otherTweetForm.backPath = otherTweetForm.backPath;
		//ツイート表示画面を再表示する為にメンバーIDをセット
		otherTweetForm.memberId = otherTweetForm.memberId;

		return "/home/otherTweet/";
	}

	/**
	 * フォロー解除
	 * @return　画面
	 */
	@Execute(validator = true, input = "/home/home/")
	public String doDeleteRelations() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// フォロー解除
		memberRelationsService.deleateRelations(otherTweetForm.relationsId);

		//検索結果を再現する為に検索クエリをセット
		otherTweetForm.query = otherTweetForm.query;
		//検索結果を再現する為に画面遷移をセット
		otherTweetForm.backPath = otherTweetForm.backPath;
		//ツイート表示画面を再表示する為にメンバーIDをセット
		otherTweetForm.memberId = otherTweetForm.memberId;

		return "/home/otherTweet/";
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

	// ツイート画面へ遷移
	private String showTweet() {

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return "otherTweet.jsp";
	}

}
