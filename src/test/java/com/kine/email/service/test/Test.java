package com.kine.email.service.test;

import com.kine.email.beans.KineEmailTemplateBean;
import com.kine.email.constants.KineTenantEmailConstants.KineEmailCase;
import com.kine.email.exceptions.KineEmailServiceException;
import com.kine.email.service.KineEmailService;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

public class Test {
	public static AwsCredentialsProvider getAwsCredntialsProvider() {

		return new AwsCredentialsProvider() {

			@Override
			public AwsCredentials resolveCredentials() {
				return new AwsCredentials() {

					@Override
					public String accessKeyId() {
						return "AKIAWKWCOLCVEXTUOK5X";
					}

					@Override
					public String secretAccessKey() {
						return "xp/MTgSXL8N7Z/x1LM0Unh0OmzXB3BLIW9nMRQPp";
					}

				};

			}

		};

	}

	public static void main(String[] a) {

		KineEmailTemplateBean bean = new KineEmailTemplateBean();

		bean.setDestinationMailId("ramya.s@kine.ai");
		bean.setDomainName("testDomain");
		bean.setKineEmailCase(KineEmailCase.SUPER_ADMIN_VERIFY_MAIL);
		bean.setKineNextActionUrl("testNextUrl");
		bean.setSuperAdminUserName("tsetSuperAdmin");
		bean.setKineVerificationToken("testToken");
		bean.setUserName("testUserName");

		SesClient sesClient = SesClient.builder().credentialsProvider(getAwsCredntialsProvider())
				.region(Region.US_EAST_1).build();

		try {
			new KineEmailService().triggerEmail(bean);
		} catch (KineEmailServiceException e) {
			e.printStackTrace();
		}
	}

}
