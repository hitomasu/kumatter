package org.leihauoli.kumatter.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.ContextDto;
import org.leihauoli.kumatter.dto.LoginDto;
import org.leihauoli.kumatter.dto.MemberTestDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.leihauoli.kumatter.form.AjaxForm;
import org.leihauoli.kumatter.service.MemberRelationsService;
import org.leihauoli.kumatter.service.TweetService;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

@Authentication
public class AjaxAction {

	@Resource
	@ActionForm
	public AjaxForm ajaxForm;

	@Resource
	protected JdbcManager jdbcManager;

	// HTTPリクエスト
	@Resource
	protected HttpServletRequest request;

	//	HTTPレスポンス
	@Resource
	protected HttpServletResponse response;

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

	// 全角スペースを定数として宣言
	final private String ZENKAKU_SPACE = "　";

	@Binding(bindingType = BindingType.NONE)
	public List<MemberTestDto> memberList;

	@Execute(validator = false)
	public String hello() {
		// ResponseUtil.writeで出力内容を詰める
		ResponseUtil.write("Ajaxの実験中です！！");
		// Ajaxの場合はページ遷移しないため return null;
		return null;
	}

	/**
	 * Ajaxのリクエストに対してJSON形式でレスポンス
	 * @return
	 * @throws IOException
	 */
	@Execute(validator = false)
	public String exer() throws IOException {
		//Mapの場合
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", ajaxForm.name);
		map.put("age", ajaxForm.age);

		//Listの場合
		final List<TweetHistoryResultDto> list = new ArrayList<TweetHistoryResultDto>();
		for (int i = 0; i < 10; i++) {
			final TweetHistoryResultDto tweetHistory = new TweetHistoryResultDto();
			tweetHistory.nickName = i + "test";
			tweetHistory.tweet = i + "tweettest";
			list.add(tweetHistory);
		}

		ResponseUtil.write(JSON.encode(map), "application/json");
		return null;
	}

	/**
	 * タイムライン初期表示
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String index() {

		//タイムラインに表示するデータを生成
		createTimeline();

		return "ajax.jsp";
	}

	/**
	 * ツイートする
	 * @return
	 */
	@Execute(validate = "check", validator = true, input = "index")
	public String doTweet() {

		//トークンチェック
		//		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
		//			throw new ActionMessagesException("errors.invalid", "Token");
		//		}

		// ツイート内容をツイートテーブルに登録
		tweetService.insertTweet(loginDto.memberId, ajaxForm.tweet);

		//タイムラインに表示するデータを生成
		createTimeline();

		return "ajax.jsp";
	}

	/**
	 * ツイート削除
	 * @return
	 */
	@Execute(validator = true, input = "index")
	public String doTweetDelete() {

		//トークンチェック
		//		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
		//			throw new ActionMessagesException("errors.invalid", "Token");
		//		}

		// ツイートを論理削除
		tweetService.deleteTweet(ajaxForm.tweetHistoryId, ajaxForm.versionNo);
		//タイムラインを生成
		createTimeline();

		return "ajax.jsp";
	}

	/**
	 * 文字列が空白のみの場合はエラー
	 * @return error
	 */
	public ActionMessages check() {
		final ActionMessages errors = new ActionMessages();

		String strTweet = ajaxForm.tweet;
		strTweet = strTweet.trim();
		strTweet = strTweet.replaceAll(ZENKAKU_SPACE, "");
		if (strTweet.isEmpty()) {
			errors.add(MessageResourcesUtil.getMessage("labels.tweet"), new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.tweet")));
		}

		return errors;
	}

	private void createTimeline() {
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
	}

}
