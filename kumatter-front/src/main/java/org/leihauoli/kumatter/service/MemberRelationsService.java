package org.leihauoli.kumatter.service;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

/**
 * メンバー関係性テーブル関連のサービスクラス
 *
 * @author hitoshi_masuzawa
 *
 */
public class MemberRelationsService {

	@Resource
	protected JdbcManager jdbcManager;

	/**
	 * メンバー関係性テーブルからフォローされているメンバーのリストを取得
	 * @param memberId
	 * @return 引数のメンバーIDがフォローされているメンバーのリスト
	 */
	public List<MemberRelationsResultDto> getFollowerMemberList(final long memberId) {
		//TODO Takeshi Kato: こちらのSQLですが、無駄が多いです。SQL文の方にコメントを追記しましたので、見てみてください。
		//                   また、JavaDocのreturn文をちゃんと書いてあるのはとても良い事ですので、
		//                   あとは、返却される値がオブジェクト型の場合、NULLが返却される可能性があるのかどうかも、記載されているとベターです。
		//                   @return 引数のメンバーIDがフォローされているメンバーのリスト(Not Null.)
		//                   というように記載されてあるだけで、使う側としては安心してFor Each文にかけられたりしますので。

		return jdbcManager.selectBySqlFile(MemberRelationsResultDto.class,
				"front/sql/selectMemberRelationsFollowerMember.sql", memberId).getResultList();
	}

	/**
	 * メンバー関係性テーブルからフォローされているメンバーの件数を取得
	 * @param memberId
	 * @return 引数のメンバーIDがフォローされている件数
	 */
	public long getFollowerMemberCount(final long memberId) {
		//TODO Takeshi Kato: こちらのSQLもselectMemberRelationsFollowerMember.sql同様に改善できます。
		return jdbcManager.selectBySqlFile(Long.class, "front/sql/selectMemberRelationsFollowerMemberCount.sql",
				memberId).getSingleResult();
	}

	/**
	 * メンバー関係性テーブルからフォローしているメンバーのリストを取得
	 * @param memberId
	 * @return 引数に指定されたメンバーIDがフォローしているメンバーのリスト
	 */
	public List<MemberRelationsResultDto> getFollowMemberList(final long memberId) {
		//TODO Takeshi Kato: こちらのSQLもselectMemberRelationsFollowerMember.sql同様に改善できます。
		return jdbcManager.selectBySqlFile(MemberRelationsResultDto.class,
				"front/sql/selectMemberRelationsFollowMember.sql", memberId).getResultList();
	}

	/**
	 * メンバー関係性テーブルからフォローしているメンバーの件数を取得
	 * @param memberId
	 * @return 引数に指定されたメンバーIDがフォローしている件数
	 */
	public long getFollowMemberCount(final long memberId) {
		//TODO Takeshi Kato: こちらのSQLもselectMemberRelationsFollowerMember.sql同様に改善できます。
		return jdbcManager
				.selectBySqlFile(Long.class, "front/sql/selectMemberRelationsFollowMemberCount.sql", memberId)
				.getSingleResult();
	}

	/**
	 * メンバー関係性テーブルからレコードを物理削除
	 * @param relationsId 関係性ID
	 * @return 削除件数
	 */
	public int deleateRelations(final long relationsId) {
		final BeanMap beanMap = new BeanMap();
		beanMap.put("relationsId", relationsId);
		return jdbcManager.updateBySqlFile("front/sql/deleteMemberRelations.sql", beanMap).execute();
	}

	/**
	 * メンバー関係性テーブルにレコード登録
	 * @param memberId メンバーID
	 * @return 登録件数
	 */
	public int insertMemberRelations(final long followMemberId, final long followerMemberId) {
		//TODO Takeshi Kato: 細かいですが、JavaDocの@paramが最新化されていないようです。

		final BeanMap beanMap = new BeanMap();
		beanMap.put("followMemberId", followMemberId);
		beanMap.put("followerMemberId", followerMemberId);
		return jdbcManager.updateBySqlFile("front/sql/insertMemberRelations.sql", beanMap).execute();
	}

}
