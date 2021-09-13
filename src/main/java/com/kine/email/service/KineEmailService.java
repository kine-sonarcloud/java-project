package com.kine.email.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.Locale;
import java.util.Optional;

import com.kine.email.beans.KineEmailTemplateBean;
import com.kine.email.constants.KineTenantEmailConstants;
import com.kine.email.constants.KineTenantEmailConstants.KineVerificationErrorCode;
import com.kine.email.exceptions.KineEmailServiceException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

public class KineEmailService implements Serializable {

	private static final String WORRIED_ABOUT_TEAMS_PRODUCTIVITY = "Worried about Team's Productivity?-Kine can help with that";

	private static final String WORRIED_ABOUT_YOUR_PRODUCTIVITY = "Worried about Your Productivity?-Kine can help with that";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TEMPLATES = "/templates/";
	transient Configuration templateCfg;

	public KineEmailService() {
		templateCfg = new Configuration(Configuration.VERSION_2_3_31);
		templateCfg.setClassForTemplateLoading(this.getClass(), TEMPLATES);
		templateCfg.setDefaultEncoding("UTF-8");
		templateCfg.setLocale(Locale.US);
		templateCfg.setWrapUncheckedExceptions(true);
		templateCfg.setFallbackOnNullLoopVariable(false);
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @throws KineEmailServiceException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public Optional<AwsResponse> triggerEmail(KineEmailTemplateBean bean) throws KineEmailServiceException {
		try {
			Content subjectMessage = null;
			Template template = null;
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			Writer consoleWriter = new OutputStreamWriter(outputstream);

			template = templateCfg.getTemplate(bean.getKineEmailCase().getCode() + ".ftl");

			String mailSubject = "Welcome " + bean.getUserName() + "! Getting started with Kine";

			switch (bean.getKineEmailCase()) {
			case PWD_RESET_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_RESET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_RESET_SUBJECT).build();
				break;
			case PWD_RESET_SUCCESS_MAIL:
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_RESET_SUBJECT).build();
				break;
			case SUPER_ADMIN_VERIFY_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_VERIFY_USER
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_SUBJECT).build();
				break;
			case SUPER_ADMIN_SUCCESS_MAIL:
				subjectMessage = Content.builder().data(mailSubject).build();
				break;
			case SITE_ADMIN_LOGIN_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data(mailSubject).build();
				break;
			case SITE_ADMIN_LOGIN_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(mailSubject).build();
				break;
			case SITE_ADMIN_FIRSTFU_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data(WORRIED_ABOUT_TEAMS_PRODUCTIVITY).build();
				break;
			case SITE_ADMIN_FIRSTFU_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(WORRIED_ABOUT_TEAMS_PRODUCTIVITY).build();
				break;
			case SITE_ADMIN_SECONDFU_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data("Connect your tools to Kine-Login to Kine!").build();
				break;
			case SITE_ADMIN_SECONDFU_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data("Connect your tools to Kine-Set Password to access Kine!")
						.build();
				break;
			case DPT_ADMIN_VERIFY_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_VERIFY_USER
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_SUBJECT).build();
				break;
			case DPT_ADMIN_SUCCESS_MAIL:
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_CONGRATS_SUBJECT).build();
				break;
			case TM_LOGIN_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data(mailSubject).build();
				break;
			case TM_LOGIN_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(mailSubject).build();
				break;
			case TM_FIRSTFU_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data(WORRIED_ABOUT_YOUR_PRODUCTIVITY).build();
				break;
			case TM_FIRSTFU_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data(WORRIED_ABOUT_YOUR_PRODUCTIVITY).build();
				break;
			case TM_SECONDFU_SSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SITE_LOGIN);
				subjectMessage = Content.builder().data("Connect your tools to Kine-Login to Kine!").build();
				break;
			case TM_SECONDFU_NONSSO_MAIL:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data("Connect your tools to Kine-Set Password to access Kine!")
						.build();
				break;
			case USER_DELETE_MAIL:
				subjectMessage = Content.builder().data("Your Kine account has been deleted").build();
				break;
			case SITE_ADMIN_NOJOINER_MAIL:
				subjectMessage = Content.builder().data("Your invited Site Admin hasn’t joined yet!").build();
				break;
			case TM_NOJOINER_MAIL:
				subjectMessage = Content.builder().data("Your invited Team Member hasn’t joined yet!").build();
				break;
			case USER_RESEND_INVITE:
				bean.setKineVerificationToken(bean.getKineNextActionUrl() + KineTenantEmailConstants.KINE_SET_PWD
						+ bean.getKineVerificationToken());
				subjectMessage = Content.builder().data("Reminder to join Kine!").build();
				break;
			default:
				subjectMessage = Content.builder().data(KineTenantEmailConstants.MAIL_DELETE_SUBJECT).build();
				break;
			}

			template.process(bean, consoleWriter);
			Content bodyMessage = Content.builder().data(new String(outputstream.toByteArray())).build();

			SendEmailRequest sendEmailRequest = SendEmailRequest.builder().source(KineTenantEmailConstants.MAIL_SOURCE)
					.destination(Destination.builder().toAddresses(bean.getDestinationMailId()).build()).message(Message
							.builder().subject(subjectMessage).body(Body.builder().html(bodyMessage).build()).build())
					.build();

			return Optional.of(bean.getAwsSesClient().sendEmail(sendEmailRequest));
		} catch (AwsServiceException | IOException | TemplateException | SdkClientException e) {
			throw new KineEmailServiceException(KineVerificationErrorCode.ALREADY_EXISTS.getCode(), e.getMessage(), e);

		}
	}
}