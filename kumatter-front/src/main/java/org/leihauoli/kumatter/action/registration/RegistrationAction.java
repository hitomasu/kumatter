package org.leihauoli.kumatter.action.registration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.TokenProcessor;
import org.leihauoli.kumatter.dto.RegistrationDto;
import org.leihauoli.kumatter.dto.param.InsertMemberParamDto;
import org.leihauoli.kumatter.form.registration.RegistrationForm;
import org.leihauoli.kumatter.service.MemberService;
import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 会員登録画面のアクションクラス
 * @author hitoshi_masuzawa
 */
public class RegistrationAction {

	// アクションフォーム
	@Resource
	@ActionForm
	public RegistrationForm registrationForm;

	// HTTPリクエスト
	@Resource
	protected HttpServletRequest request;

	// ログインサービス
	@Resource
	protected MemberService memberService;

	// 会員登録用DTO
	@Resource
	public RegistrationDto registrationDto;

	/**
	 * 初期表示
	 * @return　入力画面
	 */
	@Execute(validator = false)
	public String index() {

		return showRegistration();
	}

	/**
	 * 確認処理
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "checkValidate", input = "registration.jsp")
	public String confirm() {

		// フォームの内容をセッションに保存
		setDto();

		//トークンセット
		TokenProcessor.getInstance().saveToken(request);

		return showConfirm();
	}

	/**
	 * 完了処理
	 * @return 完了画面
	 */
	@Execute(validator = false, input = "registration.jsp")
	@RemoveSession(name = "registrationDto")
	public String doComplete() {

		//トークンチェック
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			throw new ActionMessagesException("errors.invalid", "Token");
		}

		// メンバーテーブルに会員情報をインサート
		final InsertMemberParamDto param = Beans.createAndCopy(InsertMemberParamDto.class, registrationDto).execute();
		final int count = memberService.insertMember(param);
		if (count != 1) {
			//TODO 正常にインサートされなかった場合のエラー処理
		}
		return "complete?redirect=true";
	}

	/**
	 * 完了画面へ遷移
	 * @return 完了画面
	 */
	@Execute(validator = false)
	public String complete() {
		return showComplete();
	}

	/**
	 * 戻る押下
	 * @return 入力画面
	 */
	@Execute(validator = false)
	public String goBack() {

		// フォームにセット
		setForm();

		return showRegistration();
	}

	/**
	 * フォームの内容をDTOにセット
	 */
	private void setDto() {
		registrationDto.firstName = registrationForm.firstName;
		registrationDto.lastName = registrationForm.lastName;
		registrationDto.mailAddress = registrationForm.mailAddress;
		registrationDto.nickName = registrationForm.nickName;
		registrationDto.password = registrationForm.password1;
	}

	/**
	 * DTOの内容をフォームにセット
	 */
	private void setForm() {
		registrationForm.firstName = registrationDto.firstName;
		registrationForm.lastName = registrationDto.lastName;
		registrationForm.mailAddress = registrationDto.mailAddress;
		registrationForm.nickName = registrationDto.nickName;
		registrationForm.password1 = registrationDto.password;
		registrationForm.password2 = registrationDto.password;
	}

	// 入力画面へ遷移
	private String showRegistration() {
		return "registration.jsp";
	}

	// 確認画面へ遷移
	private String showConfirm() {
		return "confirm.jsp";
	}

	// 完了画面へ遷移
	private String showComplete() {
		return "complete.jsp";
	}

	// HOME画面へ遷移
	private String showHome() {
		return "/home/home?redirect=true";
	}

	/**
	 * ニックネームとメールアドレスの存在チェック
	 * パスワード1とパスワード2が同じ値かチェック
	 * @return エラー内容
	 */
	public ActionMessages checkValidate() {
		final ActionMessages errors = new ActionMessages();

		// ニックネームが既に存在しているかをチェック
		final Integer nickName = memberService.getMemberIdNickName(registrationForm.nickName);
		if (nickName != null) {
			errors.add(MessageResourcesUtil.getMessage("labels.nickName"), new ActionMessage("errors.nickName"));
		}
		// メールアドレスが既に存在しているかをチェック
		final Integer mailAddress = memberService.getMemberIdMailAddress(registrationForm.mailAddress);
		if (mailAddress != null) {
			errors.add(MessageResourcesUtil.getMessage("labels.mailAddress"), new ActionMessage("errors.mailAddress"));
		}
		//　パスワード1とパスワード2に同じ値が入力されているかをチェック
		if (!registrationForm.password1.equals(registrationForm.password2)) {
			errors.add(MessageResourcesUtil.getMessage("labels.password"), new ActionMessage("errors.password2"));
		}
		return errors;
	}

	/**
	 * トークンチェック(executeアノテーションのvalidateに加える場合）
	 *
	 */
	private ActionMessages tokenCheck() {
		final ActionMessages errors = new ActionMessages();
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.invalid", "Token"));
		}
		return errors;
	}
}
