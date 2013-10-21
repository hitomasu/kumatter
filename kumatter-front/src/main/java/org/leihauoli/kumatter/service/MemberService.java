package org.leihauoli.kumatter.service;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.param.InsertMemberParamDto;
import org.leihauoli.kumatter.dto.param.InsertMemberPasswordParamDto;
import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.MemberResultDto;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

/**
 * メンバーテーブル関連のサービスクラス
 *
 * @author hitoshi_masuzawa
 *
 */
public class MemberService {

	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * MEMBERテーブルからデータを取得
	 * @param memberId
	 * @return MemberResultDto
	 */
	public MemberResultDto getMember(final long memberId) {
		return jdbcManager.selectBySqlFile(MemberResultDto.class, "front/sql/selectMember.sql", memberId)
				.getSingleResult();
	}

	/**
	 * MEMBERテーブルからNickNameを引数にMemberIdを取得
	 * @param nickName
	 * @return memberId
	 */
	public Long getMemberIdNickName(final String nickName) {
		return jdbcManager.selectBySqlFile(Long.class, "front/sql/selectMemberIdWhereNickName.sql", nickName)
				.getSingleResult();
	}

	/**
	 * MEMBERテーブルからMailAddressを引数にMemberIdを取得
	 * @param mailAddress
	 * @return memberId
	 */
	public Long getMemberIdMailAddress(final String mailAddress) {
		return jdbcManager.selectBySqlFile(Long.class, "front/sql/selectMemberIdWhereMailAddress.sql", mailAddress)
				.getSingleResult();
	}

	/**
	 * MEMBER_PASSWORDテーブルからMemberIDを引数にpassWordを取得
	 * @param memberId
	 * @return passWord
	 */
	public String getPassWord(final Long memberId) {
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

	/**
	 * MEMBER_PASSWORDテーブルにパスワードを登録
	 * @param paramDto
	 * @return passWord
	 */
	public int insertMemberPassword(final InsertMemberPasswordParamDto paramDto) {
		return jdbcManager.updateBySqlFile("front/sql/insertMemberPassword.sql", paramDto).execute();
	}

	/**
	 * MEMBERテーブルからFIRST_NAME、LAST_NAME、NICK_NAMEでメンバーを検索
	 * @param memberID メンバーID
	 * @param query 検索クエリ
	 * @return メンバーのリスト
	 */
	public List<MemberRelationsResultDto> getSearchMemberList(final long memberID, String query) {
		query = "%" + query + "%";
		final BeanMap beanMap = new BeanMap();
		beanMap.put("memberID", memberID);
		beanMap.put("query", query);

		return jdbcManager.selectBySqlFile(MemberRelationsResultDto.class, "front/sql/selectSearchMember.sql", beanMap)
				.getResultList();
	}
}
