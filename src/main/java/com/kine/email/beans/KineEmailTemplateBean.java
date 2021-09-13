package com.kine.email.beans;

import java.io.Serializable;

import org.eclipse.microprofile.graphql.Type;

import com.kine.email.constants.KineTenantEmailConstants.KineEmailCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.services.ses.SesClient;

@Data
@Type
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KineEmailTemplateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String destinationMailId;
	private String kineVerificationToken;
	private KineEmailCase kineEmailCase;
	private transient SesClient awsSesClient;
	private String kineNextActionUrl;
	private String domainName;
	private String superAdminUserName;
	private String siteAdminUserName;
	private String superAdminUserEmail;
	private String siteAdminUserEmail;

}
