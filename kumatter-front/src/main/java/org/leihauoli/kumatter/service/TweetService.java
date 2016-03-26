package org.leihauoli.kumatter.service;

import java.util.ArrayList;
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

		for (final Long value : memberIdList) {
			final InsertTweetParamDto insertTweetParamDto = new InsertTweetParamDto();
			insertTweetParamDto.memberId = value;
			beanMap.put("dto" + value.toString(), insertTweetParamDto);
		}

		class ParamDto {
			public ArrayList<InsertTweetParamDto> paramList;
			public Long memberId;
			public InsertTweetParamDto[] insertTweetParamDto;
			public InsertTweetParamDto insertTweetParamDto1;
			public InsertTweetParamDto insertTweetParamDto2;
			public InsertTweetParamDto insertTweetParamDto3;
			public InsertTweetParamDto insertTweetParamDto4;
			public BeanMap map;
			public ParamDto dto;
		}

		final ArrayList<InsertTweetParamDto> list = new ArrayList<InsertTweetParamDto>();

		for (final Long value : memberIdList) {
			final InsertTweetParamDto insertTweetParamDto = new InsertTweetParamDto();
			insertTweetParamDto.memberId = value;
			list.add(insertTweetParamDto);
		}
		final ParamDto paramDto = new ParamDto();
		paramDto.paramList = list;
		paramDto.memberId = new Long(1);
		paramDto.insertTweetParamDto = new InsertTweetParamDto[2];
		paramDto.insertTweetParamDto1 = new InsertTweetParamDto();
		paramDto.insertTweetParamDto1.memberId = new Long(1);
		paramDto.insertTweetParamDto2 = new InsertTweetParamDto();
		paramDto.insertTweetParamDto2.memberId = new Long(2);
		paramDto.insertTweetParamDto3 = new InsertTweetParamDto();
		paramDto.insertTweetParamDto3.memberId = new Long(3);
		paramDto.map = beanMap;
		paramDto.dto = paramDto;
		//		class PareDto {
		//			public InsertTweetParamDto insertTweetParamDto1;
		//			public InsertTweetParamDto insertTweetParamDto2;
		//			public InsertTweetParamDto insertTweetParamDto3;
		//		}
		//		final PareDto parentDto = new PareDto();
		//		parentDto.insertTweetParamDto1 = new InsertTweetParamDto();
		//		parentDto.insertTweetParamDto1.memberId = (long) 1;
		//
		//		parentDto.insertTweetParamDto2 = new InsertTweetParamDto();
		//		parentDto.insertTweetParamDto2.memberId = (long) 2;
		//
		//		parentDto.insertTweetParamDto3 = new InsertTweetParamDto();
		//		parentDto.insertTweetParamDto3.memberId = (long) 3;

		return jdbcManager.selectBySqlFile(TweetHistoryResultDto.class, "front/sql/selectTweetHistoryExerList.sql",
				paramDto).getResultList();
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
	public List<TweetHistoryResultDto> getSearchTweetList(final String query) {
		//TODO 修正済　Takeshi Kato: 引数変数を参照以外の用途で使うのはNGです。Sonarでも怒られます。
		//                   理由は、使う側は値が変更される事を想定していないからです。参照渡しの場合、呼び出し元の変数も変更されてしまいます。
		//                   引数変数とは別にローカル変数を用意して、値を引数として渡したいという用途と、検索用の文字列を構築したいという用途とで、
		//                   別の変数を利用するようにして、変数の責務を明確に使い分けていきましょう。

		//TODO Hitoshi Masuzawa ローカル変数を新しく用意する形に変更しました。

		final String likeQuery = "%" + query + "%";
		final BeanMap beanMap = new BeanMap();
		beanMap.put("query", likeQuery);
		return jdbcManager.selectBySqlFile(TweetHistoryResultDto.class, "front/sql/selectSearchTweet.sql", beanMap)
				.getResultList();
	}

}
