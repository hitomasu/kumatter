package org.leihauoli.kumatter.form.relations;

import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class FollowerForm {

	/** 検索クエリ */
	@Required(target = "doMemberSearch,doTweetSearch")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String query;

	/** 関係性ID */
	@Required(target = "doDeleteRelations")
	public Long relationsId;

	/** メンバーID */
	@Required(target = "doFollow")
	public Long memberId;
}
