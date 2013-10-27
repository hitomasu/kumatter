package org.leihauoli.kumatter.service;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.param.InsertTweetParamDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

/**
 * ツイートテーブルのサービスクラス
 *
 * @author hitoshi_masuzawa
 *
 */
public class TweetService {

	@Resource
	protected JdbcManager jdbcManager;

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

	/**
	 * ツイート履歴テーブルから引数で渡されたメンバーIDリストの件数を取得
	 * @param memberIdList
	 * @return ツイート回数
	 */
	public long getTweetHistoryCount(final List<Long> memberIdList) {
		//2waySqlでListをバインド変数として渡す際は、sqlに渡す引数が一つの場合でもDTOかMAPにして渡す。
		final BeanMap beanMap = new BeanMap();
		beanMap.put("memberIdList", memberIdList);
		return jdbcManager.selectBySqlFile(Long.class, "front/sql/selectTweetHistoryCount.sql", beanMap)
				.getSingleResult();
	}

	/**
	 * ツイート履歴テーブルにツイート内容を登録
	 * @param memberId メンバーID
	 * @param tweet ツイート内容
	 * @return 登録件数
	 */
	public int insertTweet(final long memberId, final String tweet) {
		final InsertTweetParamDto param = new InsertTweetParamDto();
		param.memberId = memberId;
		param.tweet = tweet;
		return jdbcManager.updateBySqlFile("front/sql/insertTweet.sql", param).execute();
	}

	/**
	 * ツイート履歴テーブルのツイート内容を論理削除
	 * @param tweetHistoryId ツイート履歴ID
	 * @param versionNo バージョンNo
	 * @return 削除件数
	 */
	public int deleteTweet(final long tweetHistoryId, final long versionNo) {
		final BeanMap beanMap = new BeanMap();
		beanMap.put("tweetHistoryId", tweetHistoryId);
		beanMap.put("versionNo", versionNo);
		return jdbcManager.updateBySqlFile("front/sql/deleteTweet.sql", beanMap).execute();
	}

	/**
	 * TWEETテーブルからツイートを検索
	 * @param query 検索クエリ
	 * @return メンバーのリスト
	 */
	public List<TweetHistoryResultDto> getSearchTweetList(String query) {
		query = "%" + query + "%";
		final BeanMap beanMap = new BeanMap();
		beanMap.put("query", query);
		return jdbcManager.selectBySqlFile(TweetHistoryResultDto.class, "front/sql/selectSearchTweet.sql", beanMap)
				.getResultList();
	}

}
