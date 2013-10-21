package org.leihauoli.kumatter.form.search;

import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class SearchMemberForm {

	/** 検索クエリ */
	@Required(target = "index,doMemberSearch")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String query;

	/** 隠し検索クエリ */
	@Required(target = "doDeleteRelations,doFollow")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String hiddenQuery;

	/** 関係性ID */
	@Required(target = "doDeleteRelations")
	public Long relationsId;

	/** メンバーID */
	@Required(target = "doFollow")
	public Long memberId;
}
