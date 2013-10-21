package org.leihauoli.kumatter.form.relations;

import org.seasar.struts.annotation.Required;

public class FollowerForm {

	/** 関係性ID */
	@Required(target = "doDeleteRelations")
	public Long relationsId;

	/** メンバーID */
	@Required(target = "doFollow")
	public Long memberId;
}
