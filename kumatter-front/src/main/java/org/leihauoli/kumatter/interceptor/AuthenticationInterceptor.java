package org.leihauoli.kumatter.interceptor;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInvocation;
import org.leihauoli.kumatter.annotation.Authentication;
import org.leihauoli.kumatter.dto.LoginDto;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.struts.annotation.Execute;

public class AuthenticationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	// 認証用のDTO
	@Resource
	protected LoginDto loginDto;

	//TODO 修正済　Takeshi Kato: publicで定義する必要がない気がします。protectedにしましょう。変数やメソッドのスコープはなるべく小さい方が、疎結合な良いコードになります。
	//TODO Hitoshi Masuzawa: アクセス修飾子をprotectedに変更しました！

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		if (isTargetMethod(invocation) && !isLogin()) {
			// 認証エラーの場合はログイン画面へ遷移
			return "/login/login?redirect=true";
		}
		return invocation.proceed();
	}

	// 認証が必要なメソッドかチェック
	private boolean isTargetMethod(final MethodInvocation invocation) {
		final Method method = invocation.getMethod();
		final Class<?> clazz = invocation.getThis().getClass();

		// メソッドに@Executeが付与されているかチェック
		final Execute execute = method.getAnnotation(Execute.class);
		// クラスに@Authenticationが付与されているかチェック
		final Authentication auth = clazz.getAnnotation(Authentication.class);

		return execute != null && auth != null;
	}

	// ログイン認証されているかチェック
	private boolean isLogin() {
		return loginDto.memberId != null;
	}

}
