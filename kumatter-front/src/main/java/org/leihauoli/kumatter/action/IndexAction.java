package org.leihauoli.kumatter.action;

import java.util.List;

import javax.annotation.Resource;

import org.leihauoli.kumatter.dto.MemberTestDto;
import org.leihauoli.kumatter.form.IndexForm;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class IndexAction {

	@Resource
	@ActionForm
	public IndexForm indexForm;

	@Resource
	protected JdbcManager jdbcManager;

	public List<MemberTestDto> memberList;

	@Execute(validator = false)
	public String index() {
		//TODO ログイン前はログイン画面、ログイン認証済みであればホーム画面。
		memberList = jdbcManager.selectBySqlFile(MemberTestDto.class, "front/sql/selectTest.sql").getResultList();
		indexForm.test = "zikkennzikken";
		//		return "index.jsp";
		return "/login/login";
	}
}
