package com.example.portlet.filter;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;

import com.liferay.invitation.invite.members.service.MemberRequestLocalService;
import com.liferay.login.web.constants.LoginPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;

/**
 * class LoginRenderFilter: A portlet filter for the render phase that will inject
 * the service we're testing with for the JSP fragment bundle to access.
 *
 * @author dnebinger
 */
@Component(
	immediate = true,
	property = {
			// note both portlets are included here since we cannot be sure which portlet might get used.
			"javax.portlet.name=" + LoginPortletKeys.LOGIN,
			"javax.portlet.name=" + LoginPortletKeys.FAST_LOGIN
	},
	service = PortletFilter.class
)
public class LoginRenderFilter implements RenderFilter {
	@Override
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
		// set the request attribute so it is available when the JSP renders
		request.setAttribute("MemberRequestLocalService", _memberRequestLocalService);

		// let the filter chain do it's thing
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {

	}

	@Override
	public void destroy() {

	}

	/**
	 * setMemberRequestLocalService: Injects the service into this instance from OSGi.
	 * @param memberRequestLocalService
	 */
	@Reference(unbind = "-")
	protected void setMemberRequestLocalService(final MemberRequestLocalService memberRequestLocalService) {
		_memberRequestLocalService = memberRequestLocalService;
	}

	private MemberRequestLocalService _memberRequestLocalService;
}