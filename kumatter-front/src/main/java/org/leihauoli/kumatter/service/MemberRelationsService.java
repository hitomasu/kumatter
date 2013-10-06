package org.leihauoli.kumatter.service;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
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
	 * メンバー関係性テーブルからフォローメンバーIDをキーにリストを取得
	 * @param memberId
	 * @return 引数のメンバーIDのフォローされているリスト
	 */
	public List<MemberRelationsResultDto> getFollowerMemberList(final long memberId) {
		return jdbcManager.selectBySqlFile(MemberRelationsResultDto.class,
				"front/sql/selectMemberRelationsFollowerMember.sql", memberId).getResultList();
	}

	/**
	 * メンバー関係性テーブルからフォロワーメンバーIDをキーにリストを取得
	 * @param memberId
	 * @return 引数に指定されたメンバーIDのフォローしているリスト
	 */
	public List<MemberRelationsResultDto> getFollowMemberList(final long memberId) {
		return jdbcManager.selectBySqlFile(MemberRelationsResultDto.class,
				"front/sql/selectMemberRelationsFollowMember.sql", memberId).getResultList();
	}

	/**
	 * ツイート履歴テーブルから引数で渡されたメンバーIDリストのデータをリストで取得
	 * @param memberIdList
	 * @return ツイート履歴
	 */
	public List<TweetHistoryResultDto> getTweetHistory(final List<Long> memberIdList) {
		//2waySqlでListをバインド変数として渡す際は、sqlに渡す引数が一つの場合でもDTOかMAPにして渡す。
		final BeanMap beanMap = new BeanMap();
		beanMap.put("memberIdList", memberIdList);
		return jdbcManager.selectBySqlFile(TweetHistoryResultDto.class, "front/sql/selectTweetHistory.sql", beanMap)
				.getResultList();
	}
}
