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
	public int deleteTweet(final String tweetHistoryId, final long versionNo) {
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
		//TODO Takeshi Kato: 引数変数を参照以外の用途で使うのはNGです。Sonarでも怒られます。
		//                   理由は、使う側は値が変更される事を想定していないからです。参照渡しの場合、呼び出し元の変数も変更されてしまいます。
		//                   引数変数とは別にローカル変数を用意して、値を引数として渡したいという用途と、検索用の文字列を構築したいという用途とで、
		//                   別の変数を利用するようにして、変数の責務を明確に使い分けていきましょう。

		query = "%" + query + "%";
		final BeanMap beanMap = new BeanMap();
		beanMap.put("query", query);
		return jdbcManager.selectBySqlFile(TweetHistoryResultDto.class, "front/sql/selectSearchTweet.sql", beanMap)
				.getResultList();
	}

}
