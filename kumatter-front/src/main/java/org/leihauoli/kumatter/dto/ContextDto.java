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

	/** フォローされているメンバーのリスト(関係性ID付き) */
	public List<MemberRelationsResultDto> followerMemberList;

	/** タイムライン(表示するツイート履歴のリスト) */
	public List<TweetHistoryResultDto> timeLine;

}
