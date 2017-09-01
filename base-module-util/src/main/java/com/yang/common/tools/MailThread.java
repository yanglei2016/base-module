package com.yang.common.tools;

import java.util.List;

/**
 * 异步发送邮件线程
 * @author yanglei 2017年7月4日 下午3:03:21
 */
public class MailThread implements Runnable {

	private MailUtils mailUtil;
	private String subject;
	private String content;

	public MailThread(MailUtils mailUtil, String subject, String content) {
		this.mailUtil = mailUtil;
		this.subject = subject;
		this.content = content;
	}

	@Override
	public void run() {
		List<String> recipients = mailUtil.getRecipients();
		mailUtil.send(recipients, subject, content);
	}

}
