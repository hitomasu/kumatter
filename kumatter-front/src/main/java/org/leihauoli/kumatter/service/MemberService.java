package org.leihauoli.kumatter.service;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.param.InsertMemberParamDto;
import org.seasar.extension.jdbc.JdbcManager;

/**
 * ログイン認証のサービスクラス
 *
 * @author hitoshi_masuzawa
 *
 */
public class MemberService {

	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * MEMBERテーブルからNickNameを引数にMemberIdを取得
	 * @param nickName
	 * @return memberId
	 */
	public Integer getMemberIdNickName(final String nickName) {
		return jdbcManager.selectBySqlFile(Integer.class, "front/sql/selectMemberIdWhereNickName.sql", nickName)
				.getSingleResult();
	}

	/**
	 * MEMBERテーブルからMailAddressを引数にMemberIdを取得
	 * @param mailAddress
	 * @return memberId
	 */
	public Integer getMemberIdMailAddress(final String mailAddress) {
		return jdbcManager.selectBySqlFile(Integer.class, "front/sql/selectMemberIdWhereMailAddress.sql", mailAddress)
				.getSingleResult();
	}

	/**
	 * MEMBER_PASSWORDテーブルからMemberIDを引数にpassWordを取得
	 * @param memberId
	 * @return passWord
	 */
	public String getPassWord(final Integer memberId) {
		return jdbcManager.selectBySqlFile(String.class, "front/sql/selectPassWord.sql", memberId).getSingleResult();
	}

	/**
	 * MEMBERテーブルに会員情報を登録
	 * @param paramDto
	 * @return passWord
	 */
	public int insertMember(final InsertMemberParamDto paramDto) {
		return jdbcManager.updateBySqlFile("front/sql/insertMember.sql", paramDto).execute();
	}

}
