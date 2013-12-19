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
		//TODO Takeshi Kato: メソッド名は、せめてgetMemberIdByNickNameにした方が正しいですね。
		//                   単純なgetterメソッドではないですので、findMemberIdByNickNameなどにした方が、少しでも処理内容を表現出来ていて良いとは思います。
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
	public String getPassword(final Long memberId) {
		//TODO Takesh Kato: ★細かいですが、passwordは、pass wordではないので、getPasswordが正しいです。
		//TODO Hitoshi Masuzawa: getPasswordに修正しました！
		return jdbcManager.selectBySqlFile(String.class, "front/sql/selectPassWord.sql", memberId).getSingleResult();
	}

	/**
	 * MEMBERテーブルに会員情報を登録
	 * @param paramDto
	 * @return passWord
	 */
	public int insertMember(final InsertMemberParamDto paramDto) {
		//TODO Takeshi Kato: RだとServiceクラス＝DAOの役割になってしまっているので、insertとかupdateみたいなメソッド名を付けることが多いですね。
		//                   DAOが独立して存在していないので仕方がないかもしれませんが、
		//                   本来はServiceクラスは業務ロジックを記載するところなので、DBではなく、業務を意識したメソッド名になると良いなと思います。
		//                   registerMemberとかの方が良いのかなと。
		//                   DBFluteを使った場合、DAOがそちらに含まれるようになりますので、Serviceクラスの名前を業務的な名前にするように心がけてください。
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
