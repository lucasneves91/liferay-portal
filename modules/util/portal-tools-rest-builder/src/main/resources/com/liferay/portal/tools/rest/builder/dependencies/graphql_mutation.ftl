package ${configYAML.apiPackagePath}.internal.graphql.mutation.${versionDirName};

<#compress>
	<#list openAPIYAML.components.schemas?keys as schemaName>
		import ${configYAML.apiPackagePath}.dto.${versionDirName}.${schemaName};
		import ${configYAML.apiPackagePath}.resource.${versionDirName}.${schemaName}Resource;
	</#list>
</#compress>

import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Generated;

import javax.ws.rs.core.Response;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Generated("")
public class Mutation {

	<#assign javaMethodSignatures = freeMarkerTool.getGraphQLJavaMethodSignatures(openAPIYAML, false) />

	<#list javaMethodSignatures?keys as schemaName>
		<#list javaMethodSignatures[schemaName] as javaMethodSignature>
			<#compress>
				<#list freeMarkerTool.getGraphQLMethodAnnotations(javaMethodSignature) as methodAnnotation>
					${methodAnnotation}
				</#list>

				<@compress single_line=true>
					public ${javaMethodSignature.returnType} ${javaMethodSignature.methodName}(
						<#list javaMethodSignature.javaParameters as javaParameter>
							${freeMarkerTool.getGraphQLParameterAnnotation(javaParameter)} ${javaParameter.parameterType} ${javaParameter.parameterName}

							<#if javaParameter_has_next>
								,
							</#if>
						</#list>
					) throws Exception {
				</@compress>
			</#compress>

			<#if stringUtil.equals(javaMethodSignature.returnType, "Response")>
				Response.ResponseBuilder responseBuilder = Response.ok();

				return responseBuilder.build();
			<#else>
				<@compress single_line=true>
					return _get${schemaName}Resource().${javaMethodSignature.methodName}(
						<#list javaMethodSignature.javaParameters as javaParameter>
							${javaParameter.parameterName}

							<#if javaParameter_has_next>
								,
							</#if>
						</#list>
					);
				</@compress>
			</#if>

			}

		</#list>
	</#list>

	<#list javaMethodSignatures?keys as schemaName>
		private static ${schemaName}Resource _get${schemaName}Resource() {
			return _${schemaName?uncap_first}ResourceServiceTracker.getService();
		}

		private static final ServiceTracker<${schemaName}Resource, ${schemaName}Resource> _${schemaName?uncap_first}ResourceServiceTracker;
	</#list>

	static {
		<#if javaMethodSignatures?size != 0>
			Bundle bundle = FrameworkUtil.getBundle(Mutation.class);
		</#if>

		<#list javaMethodSignatures?keys as schemaName>
			ServiceTracker<${schemaName}Resource, ${schemaName}Resource> ${schemaName?uncap_first}ResourceServiceTracker =
				new ServiceTracker<>(bundle.getBundleContext(), ${schemaName}Resource.class, null);

			${schemaName?uncap_first}ResourceServiceTracker.open();

			_${schemaName?uncap_first}ResourceServiceTracker = ${schemaName?uncap_first}ResourceServiceTracker;
		</#list>
	}

}