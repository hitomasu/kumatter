package org.leihauoli.kumatter.form.relations;

import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

public class FollowerForm {

	/** tweet内容 */
	@Required(target = "doSerch")
	@Maxlength(maxlength = 140)
	//@Mask(mask = "^[\u0020-\u007E]+$", msg = @Msg(key = "errors.ascii"))
	public String query;

	/** メンバーID */
	@Required(target = "doFollow")
	public Long memberId;

	/** tweetバージョンナンバー */
	@Required(target = "doFollow")
	public Long versionNo;

}
