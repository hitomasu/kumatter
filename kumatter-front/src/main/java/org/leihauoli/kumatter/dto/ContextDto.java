package org.leihauoli.kumatter.dto;

import java.io.Serializable;
import java.util.List;

import org.leihauoli.kumatter.dto.result.MemberRelationsResultDto;
import org.leihauoli.kumatter.dto.result.TweetHistoryResultDto;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class ContextDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** フォローしているメンバーのリスト(関係性ID付き) */
	public List<MemberRelationsResultDto> followMemberList;
	/** フォローしているメンバーの人数 */
	public long followMemberCount;

	/** フォローされているメンバーのリスト(関係性ID付き) */
	public List<MemberRelationsResultDto> followerMemberList;
	/** フォローされているメンバーの人数 */
	public long followerMemberCount;

	/** ツイートした回数 */
	public long tweetCount;

	/** タイムライン(表示するツイート履歴のリスト) */
	public List<TweetHistoryResultDto> timeLine;

}
