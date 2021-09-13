package com.kine.email.constants;

public class KineTenantEmailConstants {

	private KineTenantEmailConstants() {
	}

	public static final String MAIL_BODY = "Almost done, To complete your Kine.ai sign up, we just need to verify your email address:";
	public static final String MAIL_SUBJECT = "[Kine.ai] Verify your email to start using Kine.";
	public static final String MAIL_RESET_SUBJECT = "[Kine.ai] Reset your Password.";
	public static final String MAIL_DELETE_SUBJECT = "[Kine.ai] Your Kine Account Deleted successfully.";
	public static final String MAIL_CONGRATS_SUBJECT = "[Kine.ai] Login to start using Kine.";
	public static final String MAIL_RESET_SUBJECT_SUCCESS = "[Kine.ai] Your Kine Account password has been changed successfully!.";
	public static final String MAIL_SOURCE = "admin@kine.ai";
	public static final String KINE_VERIFY_USER = "verifyEmail/";
	public static final String KINE_RESET_PWD = "resetPassword/";
	public static final String KINE_SET_PWD = "setpassword/";
	public static final String KINE_SITE_LOGIN = "login";
	
	public static final String PROTOCOL = "http://";
	public static final String TOKEN_SEP = "&";

	public enum KineEmailCase {
		SUPER_ADMIN_VERIFY_MAIL("superAdminVerifyMail"), SUPER_ADMIN_SUCCESS_MAIL("superAdminCongratsMail"),
		SITE_ADMIN_LOGIN_SSO_MAIL("siteAdminLoginSSOMail"), SITE_ADMIN_LOGIN_NONSSO_MAIL("siteAdminLoginNonSSOMail"),
		SITE_ADMIN_FIRSTFU_SSO_MAIL("siteAdminFirstFollowupSSOMail"), SITE_ADMIN_FIRSTFU_NONSSO_MAIL("siteAdminFirstFollowupNonSSOMail"),
		SITE_ADMIN_SECONDFU_SSO_MAIL("siteAdminSecondFollowupSSOMail"), SITE_ADMIN_SECONDFU_NONSSO_MAIL("siteAdminSecondFollowupNonSSOMail"),
		DPT_ADMIN_VERIFY_MAIL("dptAdminVerifyMail"), DPT_ADMIN_SUCCESS_MAIL("dptAdminCongratsMail"),
		TM_LOGIN_SSO_MAIL("teamMemberLoginSSOMail"), TM_LOGIN_NONSSO_MAIL("teamMemberLoginNonSSOMail"),
		TM_FIRSTFU_SSO_MAIL("teamMemberFirstFollowupSSOMail"), TM_FIRSTFU_NONSSO_MAIL("teamMemberFirstFollowupNonSSOMail"),
		TM_SECONDFU_SSO_MAIL("teamMemberSecondFollowupSSOMail"), TM_SECONDFU_NONSSO_MAIL("teamMemberSecondFollowupNonSSOMail"),
		PWD_RESET_MAIL("pwdResetMail"), PWD_RESET_SUCCESS_MAIL("pwdResetSuccessMail"),
		USER_DELETE_MAIL("userDeleteMail"), DPT_ADMIN_DELETE_MAIL("dptAdminDeleteMail"),
		SITE_ADMIN_NOJOINER_MAIL("siteAdminNojoinerMail"), TM_NOJOINER_MAIL("teamMemberNojoinerMail"),
		USER_RESEND_INVITE("userResendInviteMail");

		private String code;

		private KineEmailCase(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum KineVerificationErrorCode {
		ALREADY_EXISTS(400), RESOURCE_NOT_FOUND(404), TOKEN_EXPIRED(100), TOKEN_VERIFICATION_FAILED(102);

		private int code;

		private KineVerificationErrorCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
}
